package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
	private int dir = GBJam.S;
	public InGameScreen screen;

	/** How many frames are in the walk animation */
	private static final int WALK_FRAMES = 4;
	private int frame = 0;
	/** How many "ticks" until a walk frame is advanced */
	private static final int FRAME_LENGTH = 10;
	private int walkTicks = 0;
	private boolean idle = true;
	private int idleTicks = 0, nextLook = 100000, nextIdle = 0;
	private static final int NUM_IDLE_ANIMS = 4;

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
		bounce = 0;

		this.sheet = Art.mainCharacterWalk;
		w = sheet[0][0].getRegionWidth();
		h = sheet[0][0].getRegionHeight();
	}

	/**
	 * Renders the player to the screen. Doesn't take into account the camera
	 * offset, since it's transformed automatically by the level
	 */
	@Override
	public void render(Screen screen, Camera camera) {
		int xp = (int) x;
		int yp = (int) y - 4;

		if(idle) {
			screen.draw(this.sheet[frame][1], xp, yp);
		} else {
			//Adjust the frame-number by dividing by 2 and multiplying by how many
			//frames are in each walk-cycle
			int baseFrame = (GBJam.DIRECTIONS[dir]/2) * WALK_FRAMES;

			//Modify baseFrame based on how long we've been walking in this direction
			baseFrame += frame;

			screen.draw(this.sheet[baseFrame][0], xp, yp);
		}
	}

	/**
	 * Run an update loop on the player
	 * @param input
	 */
	public void tick(Input input) {
		/** Are we currently walking? */
		boolean walk = false;
		// If we're directly on top of a tile, we don't want new input.
		if(true) { //(dy == 0 || y % GBJam.TILESIZE == 0) && (dx == 0 || x % GBJam.TILESIZE == 0)) {
			// Stop moving
			dx = dy = 0;

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
				currentLevel.activateTile(dir);
				//Make sure that the action key doesn't get repeatedly called
				//if it's held down
				input.buttonStack.delete(Input.ACTION);
				break;
			}
		}

		if(walk || dx != 0 || dy != 0) {
			//If the player is walking, increment the "frame" counter
			idle = false;
			walkTicks++;
			if(walkTicks % FRAME_LENGTH == 0) {
				//The player has walked long enough such that we need to go forward a frame
				frame++;
				walkTicks = 0;
				//Make frame roll over
				frame = frame % WALK_FRAMES;
			}
			
			nextIdle = 0;
		} else {
			//If the player stops walking, start the "idle" animation
			walkTicks = 0;
			idle = true;
			idleTicks++;
			//Check if we should do an "idle" animation and reset the idle counter
			if(idleTicks > nextLook) {
				nextLook = 100000; //Get nextLook outta the way
				nextIdle = (int) (Math.random() * 50) + 35;
				idleTicks = 0;
				frame = (int) (Math.random() * (NUM_IDLE_ANIMS - 1)) + 1;
			}
			//Check if we should switch back to default idling position
			if(idleTicks > nextIdle) {
				nextIdle = 100000;
				nextLook = (int) (Math.random() * 50) + 105;
				idleTicks = 0;
				frame = 0;
			}
		}

		tryMove(dx * GBJam.TILESIZE / 16, dy * GBJam.TILESIZE / 16);
	}

	public void outOfBounds() {
		if(x < 0) {
			screen.changeLevel(GBJam.W);
			x = (currentLevel.getWidth() - 1) * GBJam.TILESIZE;
		}
		if(x / GBJam.TILESIZE > currentLevel.getWidth()) {
			screen.changeLevel(GBJam.E);
			x = 0;
		}
		if(y < 0) {
			screen.changeLevel(GBJam.N);
			y = (currentLevel.getHeight() - 1) * GBJam.TILESIZE;
		}
		if(y / GBJam.TILESIZE > currentLevel.getHeight()) {
			screen.changeLevel(GBJam.S);
			y = 0;
		}
	}
}
