package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameHUD
{
	private TextureRegion currentWeapon;
	
    private int activeWeaponID = -1;
    private String currentWeaponPath;
    
//    private final int ICON_BAR_SPACING = 5;
//    private final int HUNGER_HEALTH_BAR_SPACING =  10;
    
    private TextureRegion[] itemImageArray = new TextureRegion[5];
    
    public GameHUD(int activeWeaponID)
    {
    	
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
    	
    	currentWeapon = itemImageArray[activeWeaponID];
    }

    
    public void render(Screen screen, Camera camera)
    {
    	//Draw both the HP bar ion and the actual HP Bar
    	int hp = ((InGameScreen) screen).player.health;
    	
    	screen.draw(Art.generalBar, camera.x +17, camera.y + 5);
    	for(int i = 0; i < hp; i ++)
        	screen.draw(Art.healthFill, camera.x + 18 + i, camera.y + 6);
    	screen.draw(Art.healthIcon, camera.x + hp + 18, camera.y + 6);
    	
    	
       	//Draw the player's current weapon in the bottom left corner
    	screen.draw(Art.weaponIcon, camera.x + 5, camera.y + 107);
       	screen.draw(currentWeapon, camera.x + 18 - currentWeapon.getRegionWidth()/2, camera.y + 123 - currentWeapon.getRegionHeight()/2);
    }
}
