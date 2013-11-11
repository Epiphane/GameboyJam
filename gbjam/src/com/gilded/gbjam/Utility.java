package com.gilded.gbjam;

import java.awt.Point;
import java.util.Random;

import com.sun.xml.internal.ws.api.pipe.NextAction;

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
	
	/**
	 * Gimme a random direction
	 */
	public static int randomCardinalDirection() {
		//Choose a random number 0-3 and call "inputToDirection" on it
		return inputToDirection(numGen.nextInt(4), 1);
	}
	
	/**
	 * Gimme a random direction as an offset (including NE, SW, etc.)
	 */
	public static Point randomOffset() {
		int rando = numGen.nextInt(8);
		switch(rando) {
		case 0:
			return new Point(0, -1);
		case 1:
			return new Point(1, -1);
		case 2:
			return new Point(1, 0);
		case 3:
			return new Point(1, 1);
		case 4:
			return new Point(0, 1);
		case 5:
			return new Point(-1, 1);
		case 6:
			return new Point(-1, 0);
		case 7:
			return new Point(-1, -1);
		default:
			System.out.println("wut. Random number gave " + rando);	
			break;
		}
		
		System.out.println("Something awful happened.");
		return new Point(0, 0);
	}
	
	/** Generate a random number from (mean-variance) to (mean+variance) */
	public static int randomRange(int mean, int variance) {
		return mean + variance - (Utility.numGen.nextInt(variance * 2));
	}
	
	public static int randomNumber(int max) {
		return Utility.numGen.nextInt(max);
	}
	
	public static float randomFloat(int max) {
		return Utility.numGen.nextFloat() * max;
	}
	
	public static float randomFloat() {
		return Utility.numGen.nextFloat();
	}
}
