package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generic class that keeps track of all information about tiles
 * 
 * @author ThomasSteinke
 *
 */
public class Tile {
	/*
	 * Tile types:
	 * 0: Basic Wall
	 * 1: Wall 2
	 * 2: Deep grass
	 * 3: Grassy
	 * 4: Light grass
	 * 5+6: Grassy w/ flower
	 * 7+8: Very light grass
	 * 9: Sign
	 * 10: Water
	 * 11-15: Water w/ walls
	 */
	public static final int WALL = 0;
	public static final int WALL2 = 1;
	public static final int DEEP_GRASS = 2;
	public static final int GRASSY = 3;
	public static final int LIGHT_GRASS = 4;
	public static final int FLOWER_GRASS1 = 5;
	public static final int FLOWER_GRASS2 = 6;
	public static final int VERY_LIGHT_GRASS = 7;
	public static final int NO_GRASS = 8;
	public static final int SIGN = 9;
	public static final int WATER_OPEN = 10;
	public static final int WATER_WALL_W = 11;
	public static final int WATER_WALL_NW = 12;
	public static final int WATER_WALL_N = 13;
	public static final int WATER_WALL_NE = 14;
	public static final int WATER_WALL_E = 15;
	
	public TextureRegion display;
	public boolean blocker;
	
	public Tile(byte tileNum) {
		int yimg = 0;
		int ximg = tileNum;
		display = Art.tiles[ximg][yimg];
		
		if(tileNum == WALL || tileNum == WALL2 || tileNum == SIGN) blocker = true;
	}
}
