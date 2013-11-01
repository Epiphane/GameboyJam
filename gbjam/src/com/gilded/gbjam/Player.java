package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
	private int dir = GBJam.S;
	private int frame = 0;
	
	private TextureRegion[][] sheet;
	
	/**
	 * Sets the player to a default spot and sets up its sprite sheet.
	 * 
	 * @param x
	 * @param y
	 */
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		w = GBJam.TILESIZE - 1;
		h = GBJam.TILESIZE - 1;
		bounce = 0;
		
		this.sheet = Art.mainCharacterWalk;
	}
	
	/**
	 * Renders the player to the screen. Doesn't take into account the camera
	 * offset, since it's transformed automatically by the level
	 */
	@Override
	public void render(Screen screen, Camera camera) {
		int xp = (int)x;
		int yp = (int)y;
		
		int stepFrame = frame / 10;
		int directionAnimStart = GBJam.DIRECTIONS[this.dir] * 3 / 2;
		screen.draw(this.sheet[directionAnimStart + stepFrame][0], xp + 1, yp - 4);
	}
	
	/**
	 * Run an update loop on the player
	 * @param input
	 */
	public void tick(Input input) {
		// If we're in between tiles, keep moving...
		if((dy != 0 && y % GBJam.TILESIZE != 0) || (dx != 0 && x % GBJam.TILESIZE != 0)) {
			// Step forward in animation
			frame ++;	
			if(frame > 29) frame = 0;
		}
		
		// ...Otherwise look for new input
		else {
			// Stop moving
			dx = dy = 0;
			boolean walk = false;
			
			// Get next input and interpret it
			switch(input.buttonStack.peek()) {
			case Input.LEFT:
				walk = true;
				dir = GBJam.W;
				dx = -1;
				break;
			case Input.RIGHT:
				walk = true;
				dir = GBJam.E;
				dx = 1;
				break;
			case Input.UP:
				walk = true;
				dir = GBJam.N;
				dy = -1;
				break;
			case Input.DOWN:
				walk = true;
				dir = GBJam.S;
				dy = 1;
				break;
			}
			
			if(walk) {
				frame ++;
				if(frame > 29) frame = 0;
			}
			else {
				frame = 0;
			}
		}
		
		tryMove(dx * GBJam.TILESIZE / 16, dy * GBJam.TILESIZE / 16);
	}
	
	public void outOfBounds() {
		// Override default delete
	}
}
