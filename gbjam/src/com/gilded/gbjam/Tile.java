package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generic class that keeps track of all information about tiles
 * 
 * @author ThomasSteinke
 *
 */
public class Tile extends Collideascope {	
	public static class TextureAndMap {
		public TextureRegion texture;
		public byte[][] map;
	}
	public static final int WATER = 0;
	public static final int SAND = 2;
	public static final int DIRT = 4;
	public static final int TEMPLE = 6;
	public static final int FOREST = 18;
	public static final int SIGN = 20;
	
	public static final int FULL = 0;
	public static final int FULL_VARY = 1;
	
	public int type;
	public int variety;
	public int[] elevation;

	public TextureAndMap textureAndMap;
	public boolean blocker;
	
	/** The message stored on a sign */
	private String message;
	
//	public Tile(int pixel) {
//		super()
//		
//		int yimg = pixel >>> 19;
//		int ximg = ((pixel & 0x00ff00) >>> 12);
//		textureAndMap = Art.tiles[ximg][yimg];
//	
//		type = SAND;
//	}
	
	public Tile(int type, int variety, int[] elevation) {
		this(type, variety);
		this.elevation = elevation;
	}
	
	public Tile(int type, int variety) {
		super(Art.tiles[variety][type].map);
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
		//if(!blocker) return false;


		return super.inTheWay(xc, yc, map);
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
