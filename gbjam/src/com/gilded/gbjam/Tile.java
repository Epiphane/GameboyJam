package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generic class that keeps track of all information about tiles
 * 
 * @author ThomasSteinke
 *
 */
public class Tile {	
	public static class TextureAndMap {
		public TextureRegion texture;
		public byte[][] map;
	}
	public static final int WATER = 0;
	public static final int SAND = 2;
	public static final int DIRT = 4;
	public static final int SIGN = 6;
	
	public static final int FULL = 0;
	public static final int FULL_VARY = 1;
	
	public int type;
	public int variety;
	public int[] elevation;

	public TextureAndMap textureAndMap;
	public boolean blocker;
	
	/** The message stored on a sign */
	private String message;
	
	public Tile(int pixel) {
		int yimg = pixel >>> 19;
		int ximg = ((pixel & 0x00ff00) >>> 12);
		textureAndMap = Art.tiles[ximg][yimg];
	
		type = SAND;
	}
	
	public Tile(int type, int variety, int[] elevation) {
		this(type, variety);
		this.elevation = elevation;
	}
	
	public Tile(int type, int variety) {
		textureAndMap = Art.tiles[variety][type];
		
		this.type = type;
		if(this.type == WATER) blocker = true;
		this.variety = variety;
		
		this.elevation = new int[] {0,0};
	}
	
	public void changeTile(int type, int variety) {
		textureAndMap = Art.tiles[variety][type];
		
		this.type = type;
		this.variety = variety;
	}
	
	public void reloadImage() {
		textureAndMap = Art.tiles[variety][type];
	}
	
	public boolean doAction() {
		if(type == SIGN) {
			System.out.println(message);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "Tile: image at " + type + ", " + variety;
	}

	public boolean inTheWay(int xc, int yc, byte[][] map) {
		if(!blocker) return false;


		return collide(xc, yc, map);
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
//		System.out.println(x+", "+y);
		for(int i = Math.max((x - 5), 0); i < Math.min((x-5) + map.length, textureAndMap.map.length); i ++) {
			for(int j = Math.max((y - 3), 0); j < Math.min((y-5) + map[0].length, textureAndMap.map[0].length); j ++) {
				if((map[i - (x - 5)][j - (y - 5)] & textureAndMap.map[i][j]) != 0) // Collision! {
					return true;
				
			}
		}
		
		return false;
	}
	
	public static void printMap(byte[][] map) {
		for(int i = 0; i < map.length; i ++) {
			for(int j = 0; j < map[0].length; j ++) {
				System.out.print(map[j][i]);
			}
			System.out.println();
		}
	}

	/**
	 * Checks to see if this tile is of a specific type and variety
	 * @param water2
	 * @param i
	 * @return
	 */
	public boolean isTypeAndVariety(int type, int variety) {
		return (this.type == type && this.variety == variety);
	}
}
