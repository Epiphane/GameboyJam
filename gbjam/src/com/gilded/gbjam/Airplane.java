package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Airplane extends Structure {

	public Airplane(int x, int y) {
		super(Art.airplane, x, y, 312, 172);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Collides with plane along the diagonal lines of the plane
	 * 
	 * Plane's bottom goes from (0,90) to (250,170)
	 * Plane's top goes from (0, -20.5) -> (60,0) to (250,65)
	 * 						 and (250, 65) to (310, 130)
	 * 
	 * Parameters are left, top, right, and bottom values relative to the plane
	 */
	public boolean collide(int l, int t, int r, int b) {
		// Check if the top right collides with the bottom of the airplane
		if(r < 250) {
			// Is it over the bottom?
			if(t < 90 + (80 * r / 250)) {
				// Is it under the top?
				if(b > -20 + (85 * l / 250)) {
					return true;
				}
			}
		}
		// Otherwise you're at the nose
		else {
			if(b > 65 + (65 * (l - 250) / 60)) {
				return true;
			}
		}
		
		return false;
	}
}
