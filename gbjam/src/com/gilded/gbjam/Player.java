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
		
		//There are 64 pixels between each tile. We need to show all 4 frames of the
		//walk cycle, so 
		int directionAnimStart = GBJam.DIRECTIONS[this.dir] * 3 / 2;
		screen.draw(this.sheet[0][0], xp + 1, yp - 4);
	}
	
	/**
	 * Run an update loop on the player
	 * @param input
	 */
	public void tick(Input input) {
		// If we're directly on top of a tile, we don't want new input.
		if((dy == 0 || y % GBJam.TILESIZE == 0) && (dx == 0 || x % GBJam.TILESIZE == 0)) {
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
			case Input.ACTION:
				//Call "action" on the tile we're facing
				level.activateTile(dir);
				//Make sure that the action key doesn't get repeatedly called
				//if it's held down
				input.buttonStack.delete(Input.ACTION);
				break;
			}
			
			/*if(walk) {
				frame ++;
				if(frame > 29) frame = 0;
			}
			else {
				frame = 0;
			}*/
		}
		//However, if we're in between tiles, increment the "frame" counter.
		else {
			frame++;
			if(frame > 64) frame = 0;
		}
		
		tryMove(dx * GBJam.TILESIZE / 16, dy * GBJam.TILESIZE / 16);
	}
	
	public void outOfBounds() {
		// Override default delete
	}
}
