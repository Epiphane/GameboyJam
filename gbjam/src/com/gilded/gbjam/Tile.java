package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generic class that keeps track of all information about tiles
 * 
 * @author ThomasSteinke
 *
 */
public class Tile {
	//    			Tile types:          
	public static final int WALL = 0;             //    0: Basic Wall        
	public static final int WALL2 = 1;            //    1: Alternate     
	public static final int DEEP_GRASS = 2;       //    2: Deep grass        
	public static final int GRASSY = 3;           //    3: Grassy            
	public static final int LIGHT_GRASS = 4;      //    4: Light grass       
	public static final int FLOWER_GRASS1 = 5;    //    5+6: Grassy w/ flower
	public static final int FLOWER_GRASS2 = 6;    //    
	public static final int VERY_LIGHT_GRASS = 7; //    7+8: Very light grass
	public static final int NO_GRASS = 8;         //    
	public static final int SIGN = 9;             //    9: Sign              
	public static final int WATER_OPEN = 10;      //    10: Water            
	public static final int WATER_WALL_W = 11;    //    11-15: Water w/ walls
	public static final int WATER_WALL_NW = 12;   //
	public static final int WATER_WALL_N = 13;    //
	public static final int WATER_WALL_NE = 14;   //
	public static final int WATER_WALL_E = 15;    //
	
	private int type;

	public TextureRegion display;
	public boolean blocker;
	
	/** The message stored on a sign */
	private String message;

	public Tile(byte tileNum) {
		int yimg = 0;
		int ximg = tileNum;
		display = Art.tiles[ximg][yimg];

		if(tileNum == WALL || tileNum == WALL2 || tileNum == SIGN) blocker = true;
	
		type = tileNum;
	}
	
	public Tile(int pixel) {
		int yimg = pixel >>> 19;
		int ximg = ((pixel & 0x00ff00) >>> 12);
		display = Art.tiles[ximg][yimg];
	
		type = GRASSY;
	}
	
	public void doAction() {
		if(type == SIGN) {
			System.out.println(message);
		} else if(type == WALL) {
			System.out.println("That's a sexy, sexy wall");
		}
	}
}
