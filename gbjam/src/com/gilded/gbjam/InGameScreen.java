package com.gilded.gbjam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class InGameScreen extends Screen
{
    public static final int WORLD_WIDTH = 6;
    public static final int WORLD_HEIGHT = 4;

    public Level[][] world;
    private Level currentLevel;
    private Player player;
    private int x, y;

    private Stage myStage;
    private Table myTable;
    private Skin mySkin;

    public InGameScreen()
    {
        camera = new Camera(GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT);

        // Create player
        player = new Player(0, 6 * GBJam.TILESIZE);
        player.currentLevel = currentLevel;
        player.screen = this;

        // TODO: Get rid of this once you can actually click new game
        newGame();
        setLevel(0, 2);

        // Trying to implement the UI (really shitty structure right now, just testing)
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        myStage = new Stage();
        myTable = new Table();
        
        mySkin = new Skin();
        mySkin.add("default", pixmap);
        mySkin.add("default", new BitmapFont());
        
        LabelStyle textLabelStyle = new LabelStyle();
        textLabelStyle.font = new BitmapFont();
        mySkin.add("default", textLabelStyle);
        
        Label healthLabel = new Label("Health", mySkin);
        Label hungerLabel = new Label("Hunger", mySkin);
        
        myTable.setFillParent(true);
        myTable.debugTable();
        myStage.addActor(myTable);
        healthLabel.setColor(Color.RED);
        healthLabel.setAlignment(Align.center);
        hungerLabel.setColor(Color.RED);
        hungerLabel.setAlignment(Align.center);
        myTable.add(healthLabel).expand().top().left().height(20).width(250);
//        myTable.getCell(healthLabel).center();
//        myTable.top().left().pad(10);
        myTable.add(hungerLabel).expand().top().right().height(20).width(250);
        myTable.row();
//        myTable.row();
//        myTable.left().top().pad(20);
//        myTable.row();
//        myTable.add(hungerLabel).width(100);
//        myTable.right().top().pad(20);
//        Table.drawDebug(myStage);
    }
    
    public void resize()
    {
        myStage.setViewport(WORLD_WIDTH, WORLD_HEIGHT);
        myTable.setFillParent(true);
        myTable.invalidate();
    }

    public void newGame()
    {
        world = new Level[WORLD_WIDTH][WORLD_HEIGHT];
        for (int i = 0; i < WORLD_WIDTH; i++)
        {
            for (int j = 0; j < WORLD_HEIGHT; j++)
            {
                int direction = 0;
                if (i == 0)
                    direction += GBJam.W;
                if (i == WORLD_WIDTH - 1)
                    direction += GBJam.E;
                if (j == 0)
                    direction += GBJam.N;
                if (j == WORLD_HEIGHT - 1)
                    direction += GBJam.S;
                world[i][j] = new Level(this, 15, 12, GBJam.TILESIZE * 2,
                            GBJam.TILESIZE * 2, player);
                world[i][j].createBeachLevel(direction);

                if (i == 0 && j == 2)
                    world[i][j].createStartLevel(player);
            }
        }
    }

    public void setLevel(int x, int y)
    {
        this.x = x;
        this.y = y;
        if (currentLevel != null)
            currentLevel.remove(player);
        currentLevel = world[x][y];
        player.currentLevel = currentLevel;
        currentLevel.init(player);
    }

    public void changeLevel(int direction)
    {
        if (direction == GBJam.N && y > 0)
            y--;
        if (direction == GBJam.S && y < world[0].length - 1)
            y++;
        if (direction == GBJam.W && x > 0)
            x--;
        if (direction == GBJam.E && x < world.length - 1)
            x++;

        if (currentLevel != null)
            currentLevel.remove(player);

        System.out.printf("Changing level to %d, %d\n", x, y);
        currentLevel = world[x][y];
        player.currentLevel = currentLevel;
        currentLevel.init(player);
    }

    public void tick(Input input)
    {
        currentLevel.tick(input);
    }

    @Override
    public void render()
    {
        currentLevel.render(this, camera);
        
        myStage.draw();
        Table.drawDebug(myStage);
    }
}
