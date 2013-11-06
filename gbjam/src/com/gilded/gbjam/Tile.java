package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generic class that keeps track of all information about tiles
 * 
 * @author ThomasSteinke
 *
 */
public class Tile {	
	public static final int WATER = 0;
	public static final int SAND = 2;
	public static final int DIRT = 4;
	public static final int SIGN = 6;
	
	public static final int FULL = 0;
	public static final int FULL_VARY = 1;
	
	public int type;
	public int variety;
	public int[] elevation;

	public TextureRegion display;
	public boolean blocker;
	
	/** The message stored on a sign */
	private String message;
	
	public Tile(int pixel) {
		int yimg = pixel >>> 19;
		int ximg = ((pixel & 0x00ff00) >>> 12);
		display = Art.tiles[ximg][yimg];
	
		type = SAND;
	}
	
	public Tile(int type, int variety, int[] elevation) {
		this(type, variety);
		this.elevation = elevation;
	}
	
	public Tile(int type, int variety) {
		display = Art.tiles[variety][type];
		
		this.type = type;
		this.variety = variety;
		
		this.elevation = new int[] {0,0};
	}
	
	public void changeTile(int type, int variety) {
		display = Art.tiles[variety][type];
		
		this.type = type;
		this.variety = variety;
	}
	
	public void reloadImage() {
		display = Art.tiles[variety][type];
	}
	
	public void doAction() {
		if(type == SIGN) {
			System.out.println(message);
		}
	}
	
	public String toString() {
		return "Tile: image at " + type + ", " + variety;
	}
}
