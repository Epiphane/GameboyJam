package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Structure {
	public static class StructureAndMap {
		public TextureRegion structure;
		public byte[][] map;
	}
	public static StructureAndMap airplane, rock, palm;
	
	public static final byte BLOCKER = -1;
	public static final byte DRAWOVER = 1;
	
	public int x, y;
	public int xSlot, ySlot;
	public int w, h;
	
	public boolean blocker, doActionOnCollision;
	public boolean removed;
	
	protected Level level;
	
	private TextureRegion display;
	private byte[][] collisionMap;
	
	public Structure(StructureAndMap structure, int x, int y) {
		this.display = structure.structure;
		this.x = x * GBJam.TILESIZE;
		this.y = y * GBJam.TILESIZE;
		this.w = structure.map.length;
		this.h = structure.map[0].length;
		
		this.xSlot = x;
		this.ySlot = y;
		
		blocker = true;
		
		collisionMap = structure.map;
	}
	
	public void init(Level level) {	
		this.level = level;
	}
	
	public void tick(Input input) {
	}
	
	public void render(Screen screen, Camera camera) {
		screen.draw(display, x - GBJam.TILESIZE, y - GBJam.TILESIZE);
	}
	
	public boolean inTheWay(int x, int y, byte[][] map) {
		//System.out.println(x+", "+y+" ["+map.length+","+map[0].length+"]");
		//System.out.println(this.x+", "+this.y+" ["+this.w+","+this.h+"]");
		
		x -= this.x;
		y -= this.y;

		if(!blocker) return false;
		
		// If it's not in bounds then derp
		if(x + map.length < 0 || y + map[0].length < 0 || x > this.w || y > this.h) return false;
		//System.out.println("Ouch");
		
		return collide(x, y, map);
	}
	
	/**
	 * Check if something is colliding with me
	 * 
	 * @param x - x value relative to me
	 * @param y - y value relative to me
	 * @param w - width of object
	 * @param h - height of object
	 * @return
	 */
	public boolean collide(int x, int y, byte[][] map) {
		for(int i = Math.max(x, 0); i < Math.min(x + map.length, collisionMap.length); i ++) {
			for(int j = Math.max(y, 0); j < Math.min(y + map[0].length, collisionMap[0].length); j ++) {
				if((map[i - x][j - y] & collisionMap[i][j]) != 0) // Collision! {
					return true;
				
			}
		}
		
		return false;
	}
	
	public static Structure Rock(int x, int y) {
		return new Structure(rock, x, y);
	}
	
	public static Structure Airplane(int x, int y) {
		return new Structure(airplane, x, y);
	}
	public static Structure PalmTree(int x, int y) {
		return new Structure(palm, x, y);
	}
	
	/**
	 * Returns false, because we don't want to remove me
	 * @param entity
	 * @return
	 */
	public boolean doAction(Entity entity) {
		return false;
	}
}
