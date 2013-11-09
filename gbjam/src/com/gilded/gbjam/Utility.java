package com.gilded.gbjam;

import java.awt.Point;
import java.util.Random;

/****
 * Provides some handy static utility things for the rest of the game.
 */
public class Utility {
	private static final int SEED = 37;
	
	public static Random numGen = new Random(37);
	
	/**
	 * Turns from direction -> offset point
	 * @param direction Which direction are you looking at?
	 * @return	A point describing the offset, (x, y)
	 */
	public static Point offsetFromDir(int direction) {
		Point result = new Point(0, 0);
		
		switch(direction) {
		case GBJam.N:
			result.y = -1;
			break;
		case GBJam.E:
			result.x = 1;
			break;
		case GBJam.S:
			result.y = 1;
			break;
		case GBJam.W:
			result.x = -1;
			break;
		}
		
		return result;
	}
	
	/**
	 * Maps from direction like "Input.LEFT" -> "GBJam.W"
	 * @param input An input key to be processed
	 * @return A direction usable by an entity
	 */
	public static int inputToDirection(int input, int defaultReturn) {
		//Negative 1 means no input. Return the default argument (probably the old direction)
		if(input == -1) return defaultReturn;
		
		switch(input) {
		case Input.LEFT:
			return GBJam.W;
		case Input.RIGHT:
			return GBJam.E;
		case Input.UP:
			return GBJam.N;
		case Input.DOWN:
			return GBJam.S;
		case Input.ACTION:
			return defaultReturn;
		//TODO: "Run" button or whatever goes here.
		}
		
		throw new IllegalArgumentException("Argument should have been an input constant. Instead it's " + input);
	}
}
