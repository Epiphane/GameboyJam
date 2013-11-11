package com.gilded.gbjam;

import java.awt.Point;

public class Bat extends Enemy {

	// === Behavior stuff ===
	private static final int CHANGE_DIR_LENGTH = 180;
	private static final int CHANGE_DIR_VARIANCE = 60;
	/** How long we've spent going this direction */
	private int currDirTime;
	
	// === Animation stuff ===
	/** How long each frame lasts, in ticks */
	private static final int FRAME_LENGTH = 8;
	/** How many frames are in the animation cycle */
	private static final int NUM_FRAMES = 2;
	private int frameTicks = 0;
	private int frame = 0;
	
	public Bat(int x, int y) {
		super(Art.enemyMap);
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(Art.enemyWalk[frame][BAT], (int) x, (int) y);
	}

	@Override
	public void tick(Input input) {
		//Bathavior: never stop moving. Change directions every 0.5-2.5 seconds.
		//Bats can move diagonally!
		
		//See if we should update the frame
		frameTicks++;
		if(frameTicks % FRAME_LENGTH == 0) {
			frame++;
			//We've reached the end of the frame cycle, start afresh
			frame %= NUM_FRAMES;
		}
		
		//Move ya body
		boolean gotBlocked = !tryMove(dx, dy);
		
		//See if we should change the current direction
		currDirTime--;
		if(currDirTime <= 0 || gotBlocked) {
			//Set a new time to wait
			currDirTime = Utility.randomRange(CHANGE_DIR_LENGTH, CHANGE_DIR_VARIANCE);
		
			//Choose a new direction to go
			Point newDir = Utility.randomOffset();
			dx = newDir.x;
			dy = newDir.y;
			
			System.out.println("Changing dir");
		}
	}
}