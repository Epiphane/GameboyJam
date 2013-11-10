package com.gilded.gbjam;


public class Item extends Structure {
	public static StructureAndMap slingshot, sword, flaregun, torch, coconut;
	
	private int timeSinceinit;

	public Item(StructureAndMap structure, int x, int y, Level level) {
		super(structure, x, y, level);
		doActionOnCollision = true;
		
		this.x = x;
		this.y = y;
		
		timeSinceinit = 0;
	}
	
	public boolean doAction(Entity entity) {
		if(entity instanceof Player) {
			((Player)entity).pickUp(this);
			return true;
		}
		return false;
	}
	
	public static Item Sword(int x, int y, Level level) {
		return new Item(sword, x, y, level);
	}

	public static Item Slingshot(int x, int y, Level level) {
		return new Item(slingshot, x, y, level);
	}
	
	public static Item Coconut(int x, int y, Level level) {
		return new Item(coconut, x, y, level);
	}
	
	public static Item FlareGun(int x, int y, Level level) {
		return new Item(flaregun, x, y, level);
	}
	
	public void render(Screen screen, Camera camera) {
		// Use timeSinceInit to create a slight "floating" effect
		screen.draw(display, x, (int) (y + Math.sin(timeSinceinit++ / 16) * 2));
	}
}
