package com.gilded.gbjam;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InGameScreen extends Screen
{
    public static final int WORLD_WIDTH = 3;
    public static final int WORLD_HEIGHT = 3;

    public Level[][] world;
    public Level inTheTemple;
    
    private Level currentLevel;
    private GameHUD headsUpDisplay;
    public int[] justAtLevel;
    public int[] returnSpawn;
    private final Player player;
    private int x, y;

    public InGameScreen()
    {
        camera = new Camera(GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT);

        // Create player
        player = new Player(0, 6 * GBJam.TILESIZE);
        player.currentLevel = currentLevel;
        player.screen = this;
        
        //Create the HUD for the in-game screne
        headsUpDisplay = new GameHUD(player.activeItem);

        // TODO: Get rid of this once you can actually click new game
        newGame();
        setLevel(0, 1);
        justAtLevel = new int[] {0,1};
    }

    /**
     * Change the level. if we're in a special place x == -1, so go back to where we came from
     * @param direction
     */
    public void changeLevel(int direction)
    {
        if(x == -1) {
        	setLevel(justAtLevel[0],justAtLevel[1]);
        	player.x = returnSpawn[0];
  			player.y = returnSpawn[1];
        }
        else {
	        if(direction == GBJam.N && y > 0)
	        {
	            y--;
	        }
	        if(direction == GBJam.S && y < world[0].length - 1)
	        {
	            y++;
	        }
	        if(direction == GBJam.W && x > 0)
	        {
	            x--;
	        }
	        if(direction == GBJam.E && x < world.length - 1)
	        {
	            x++;
	        }
	
	        System.out.printf("Changing level to %d, %d\n", x, y);
	        setLevel(x,y);
        }
    }

    public void newGame()
    {
    	// Create special levels
    	inTheTemple = new Level(this, 10, 9, GBJam.TILESIZE * 2,GBJam.TILESIZE * 2, player);
    	inTheTemple.createTempleLevel();
    	
        world = new Level[WORLD_WIDTH][WORLD_HEIGHT];
        for(int i = 0; i < WORLD_WIDTH; i++)
        {
            for(int j = 0; j < WORLD_HEIGHT; j++)
            {
                int direction = 0;
                
                // Initialize beaches and sneeches on the edge
                if (i == 0 || j == 0 || i == WORLD_WIDTH - 1 || j == WORLD_HEIGHT - 1) {
                    if(i == 0)
                    {
                        direction += GBJam.W;
                    }
                    if(i == WORLD_WIDTH - 1)
                    {
                        direction += GBJam.E;
                    }
                    if(j == 0)
                    {
                        direction += GBJam.N;
                    }
                    if(j == WORLD_HEIGHT - 1)
                    {
                        direction += GBJam.S;
                    }
                    
	                world[i][j] = new Level(this, 15, 12, GBJam.TILESIZE * 2,
	                            GBJam.TILESIZE * 2, player);
	                world[i][j].createBeachLevel(direction);
                    
                    if(i == 0 && j == 1)
                    {
                        world[i][j].createStartLevel(player);
                    }
                }
                // Initialize forest otherwise
                else {
                    if(i == 1)
                    {
                        direction += GBJam.W;
                    }
                    if(i == WORLD_WIDTH - 2)
                    {
                        direction += GBJam.E;
                    }
                    if(j == 1)
                    {
                        direction += GBJam.N;
                    }
                    if(j == WORLD_HEIGHT - 2)
                    {
                        direction += GBJam.S;
                    }
	                world[i][j] = new Level(this, 15, 12, GBJam.TILESIZE * 2,
	                            GBJam.TILESIZE * 2, player);
	                world[i][j].createForestLevel(direction);
                }
                
                // Initialize the temple at (4,4)
                if(i == 1 && j == 2) {	                
                	world[i][j].createToTempleLevel();
                }
            }
        }
    }

    @Override
    public void render()
    {
    	this.getSpriteBatch().begin();
    	
        currentLevel.render(this, camera);
        headsUpDisplay.render(this, camera);
        
        this.getSpriteBatch().end();
    }
    
    /**
     * Actually change the level
     * 
     * @param newLevel
     */
    public void setLevel(Level newLevel) {
        if(currentLevel != null)
        {
            currentLevel.remove(player);
        }
        currentLevel = newLevel;
        player.currentLevel = currentLevel;
        currentLevel.init(player);
        
        camera.width = currentLevel.getWidth() * GBJam.TILESIZE;
        camera.height = currentLevel.getHeight() * GBJam.TILESIZE;
    }

    /**
     * Choose what level we're going to
     * @param x
     * @param y
     */
    public void setLevel(int x, int y)
    {
        justAtLevel = new int[] {this.x,this.y};
        this.x = x;
        this.y = y;
        setLevel(world[x][y]);
    }

    /**
     * Go to a new speciall level!
     * @param newLevel
     */
    public void setSpecialLevel(Level newLevel, int[] returnSpawn)
    {
        justAtLevel = new int[] {this.x,this.y};
        this.returnSpawn = returnSpawn;
        x = -1; y = -1;
        setLevel(newLevel);
    }

    public void tick(Input input)
    {
        currentLevel.tick(input);
    }
}
