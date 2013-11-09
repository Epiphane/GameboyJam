package com.gilded.gbjam;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameHUD
{
    private Table overlayTable;
    private Stage overlayStage;
    private Label healthTextLabel;
    private Label hungerTextLabel;
    
    public GameHUD()
    {
        // Pixmap myPixmap = new
        // Pixmap(Gdx.files.internal("res/newplayer.png"));
        // myTexture = new Texture(myPixmap);
        // myTextureRegion = new TextureRegion(myTexture, 0, 0, 256, 30);
        // System.out.println(myPixmap.getWidth());
        // System.out.println(myPixmap.getHeight());
        // myTable = new Table();
        // myTable.setFillParent(true);
        // stage.addActor(myTable);
    }

    public void create()
    {
        overlayStage = new Stage();
//        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        overlayStage.draw();
        Table.drawDebug(overlayStage);
        overlayStage.addActor(table);

        // Add widgets to the table here.
    }

    public void render(Screen screen, Camera camera)
    {
        // screen.draw(myTextureRegion, 150, 10 );
        Table.drawDebug(overlayStage);
    }

}
