package com.gilded.gbjam;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;

import com.esotericsoftware.tablelayout.Cell;

public class InGameScreen extends Screen
{
    public static final int WORLD_WIDTH = 4;
    public static final int WORLD_HEIGHT = 4;

    public Level[][] world;
    public Level inTheTemple;
    
    private Level currentLevel;
    public int[] justAtLevel;
    public int[] returnSpawn;
    private final Player player;
    private int x, y;

    private final Stage myStage;
    private final Table myTable;

    public InGameScreen()
    {
        camera = new Camera(GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT);

        // Create player
        player = new Player(0, 6 * GBJam.TILESIZE);
        player.currentLevel = currentLevel;
        player.screen = this;

        // TODO: Get rid of this once you can actually click new game
        newGame();
        setLevel(0, 1);
        justAtLevel = new int[] {0,1};

        // Trying to implement the UI (really shitty structure right now, just
        // testing)
        myStage = new Stage();
        myTable = new Table();

        Pixmap meatPixmap = new Pixmap(Gdx.files.internal("res/realmeat.png"));
        Pixmap heartPixmap = new Pixmap(Gdx.files.internal("res/heart.png"));
        Pixmap hpBarPixmap = new Pixmap(Gdx.files.internal("res/healthbarframe.png"));
        Pixmap hpPixmap = new Pixmap(Gdx.files.internal("res/healthfill.png"));
        
        Texture meatTexture = new Texture(meatPixmap);
        Texture heartTexture = new Texture(heartPixmap);
        Texture hpBarFrameTexture = new Texture(hpBarPixmap);
        Texture hpTexture = new Texture(hpPixmap);
        
        TextureRegion hpTextureRegion = new TextureRegion(hpTexture, 0, 0, 10, 10);

//        Sprite mySprite = new Sprite(healthBarFrameTexture);
//        mySprite.setBounds(0, 0, 200, 100);
        Image meat = new Image(meatTexture);
        Image heart = new Image(heartTexture);
        Image hpBar = new Image(hpBarFrameTexture);
        Image hungerBar = new Image(hpBarFrameTexture);
        Image hp = new Image(hpTextureRegion);
        hp.scale(-.1f, hp.getScaleY());
//        healthBarFrame.setSize(400, 400);
//        healthBarFrame.size(500f, 500f);

        myTable.setFillParent(true);
        myTable.debugTable();
        myStage.addActor(myTable);
        myTable.add(heart).width(32).top().right().pad(5);
//        myTable.stack(hp, hpBar).height(32).width(50).padRight(85);
        
        
//        myTable.getCell(hpBar).height(32).width(230).padRight(85);
        myTable.add(hpBar).height(32).width(230).padRight(85);  
        myTable.add(meat).width(32).pad(5);
        myTable.add(hungerBar).height(32).width(230).padRight(25);
        myTable.top().left();
//        System.out.printf("%f",myTable.getPrefHeight());
        myTable.row();
//        myTable.add

//        myTable.debugTable();
//        myTable.debugCell();
        // myTable.row();
        // myTable.left().top().pad(20);
        // myTable.row();
        // myTable.add(hungerLabel).width(100);
        // myTable.right().top().pad(20);
        // Table.drawDebug(myStage);
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
        currentLevel.render(this, camera);

        myStage.draw();
//        Table.drawDebug(myStage);
    }

    public void resize()
    {
        myStage.setViewport(WORLD_WIDTH, WORLD_HEIGHT, true);
        myTable.setFillParent(true);
        myTable.invalidate();
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
