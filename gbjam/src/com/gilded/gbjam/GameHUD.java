package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameHUD
{
    private Sprite healthIcon;
    private Sprite hungerIcon;
    private Sprite generalBar;
    private Sprite currentWeapon;
    
    private String currentWeaponPath;
 
    private int activeWeaponID = -1;
    
//    private final int ICON_BAR_SPACING = 5;
//    private final int HUNGER_HEALTH_BAR_SPACING =  10;
    
    private TextureRegion[] itemImageArray = new TextureRegion[5];
    
    public GameHUD(int activeWeaponID)
    {
    	healthIcon = new Sprite(Art.load("res/heart.png", 32, 32));
    	hungerIcon = new Sprite(Art.load("res/realmeat.png", 32, 32));
    	generalBar = new Sprite(Art.load("res/healthbarframe.png", 32, 250));
    	
    	this.activeWeaponID = activeWeaponID;
    	
    	/* Quick reference for items in the itemImageArray
    	 * -----------------------------------------------
    	 * 0 = sword
    	 * 1 = slingshot
    	 * 2 = flaregun
    	 * 3 = torch
    	 * 4 = coconut
    	 * */    	
    	itemImageArray[0] = Item.sword.structure;
    	itemImageArray[1] = Item.slingshot.structure;
    	itemImageArray[2] = Item.flaregun.structure;
    	itemImageArray[3] = Item.torch.structure;
    	itemImageArray[4] = Item.coconut.structure;
    	
    	currentWeapon = new Sprite(itemImageArray[activeWeaponID]);
    }

    
    public void render(Screen screen, Camera camera)
    {
    	//Draw both the HP bar ion and the actual HP Bar
    	screen.draw(healthIcon, camera.x, camera.y);
    	screen.draw(generalBar, camera.x + 5, camera.y + 5);
    	
//    	//Draw both the Hunger bar icon and the actual hunger bar
       	screen.draw(hungerIcon, camera.x + 10, camera.y + 10);
       	screen.draw(generalBar, camera.x + 15, camera.y + 15);
       	
       	//Draw the player's current weapon in the bottom left corner
       	screen.draw(currentWeapon, camera.x + 5, camera.y + 10);
    }
}
