package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Structure {
	public static final byte BLOCKER = -1;
	public static final byte DRAWOVER = 1;
	
	public int x, y;
	public int xSlot, ySlot;
	public int w, h;
	
	public boolean blocker;
	
	protected Level level;
	
	private TextureRegion display;
	private byte[][] collisionMap;
	
	public Structure(TextureRegion display, byte[][] map, int x, int y, int w, int h) {
		this.display = display;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		blocker = true;
		
		collisionMap = map;
	}
	
	public void init(Level level) {	
		this.level = level;
	}
	
	public void tick(Input input) {
	}
	
	public void render(Screen screen, Camera camera) {
		screen.draw(display, x, y - h + GBJam.TILESIZE);
	}
	
	public boolean inTheWay(int x, int y, byte[][] map) {
		x -= this.x;
		y -= this.y - GBJam.TILESIZE;

		if(!blocker) return false;
		
		// If it's not in bounds then derp
		if(x + map.length * 4 < 0 || y + map[0].length * 4 < 0 || x > this.w || y > this.h) return false;
		
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
		// Convert x and y into values that line up with the maps
		x /= 4;
		y /= 4;
		
		for(int i = Math.max(x, 0); i < Math.min(x + map.length, collisionMap.length); i ++) {
			for(int j = Math.max(y, 0); j < Math.min(y + map[0].length, collisionMap[0].length); j ++) {
				if((map[i - x][j - y] & collisionMap[i][j]) != 0) // Collision!
					return true;
			}
		}
		
		return false;
	}
}
