package com.gilded.gbjam;


public class Item extends Structure {
	public static StructureAndMap slingshot, sword;

	public Item(StructureAndMap structure, int x, int y) {
		super(structure, x, y);
		doActionOnCollision = true;
		
		this.x = x;
		this.y = y;
	}
	
	public boolean doAction(Entity entity) {
		if(entity instanceof Player) {
			((Player)entity).pickUp(this);
			return true;
		}
		return false;
	}
	
	public static Item Sword(int x, int y) {
		return new Item(sword, x, y);
	}

	public static Item Slingshot(int x, int y) {
		return new Item(slingshot, x, y);
	}
}
