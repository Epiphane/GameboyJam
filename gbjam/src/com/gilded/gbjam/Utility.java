package com.gilded.gbjam;

import java.awt.Point;

/****
 * Provides some handy static utility things for the rest of the game.
 */
public class Utility {
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
}
