package com.gilded.gbjam;

import java.awt.Point;

public class Boar extends Enemy {
	/** How long we'll wait between walking cycles */
	public static final int WAIT_LENGTH = 120;
	public static final int WAIT_VARIANCE = 60;
	private int currWait = 0;
	private boolean waiting;
	/** How many tiles the Boar walks during a walk cycle */
	public static final int WALK_LENGTH = 4;
	public static final int WALK_VARIANCE = 1;
	private int walkDistance = 0;
	private boolean walking;
	
	// === Animation Stuff ===
	public int walkFrame = 0;
	/** How many frames are in the walk animation */
	private static final int WALK_FRAMES = 4;
	
	private int dx, dy;
	
	private int currDirection;
	
	public Boar(int x, int y) {
		super(Art.enemyMap);
		this.x = x;
		this.y = y;
		
		waiting = true;
		walking = false;
		
		currDirection = GBJam.S;
		
		//Choose a number from WAIT_LENGTH - WAIT_VARIANCE to WAIT_LENGTH + WAIT_VARIANCE
		currWait = WAIT_LENGTH + WAIT_VARIANCE - (Utility.numGen.nextInt(WAIT_VARIANCE * 2));
	}

	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(Art.enemyWalk[frame][BOAR], (int) x, (int) y);
	}
	
	@Override
	public void tick(Input input) {
		//Boarhavior: wait around for 1-3 seconds then walk in a random direction
		//for 3-5 tiles
		if(waiting) {
			currWait--;
			if(currWait <= 0) {
				waiting = false;
				walking = true;
				//Choose a number from (WALK_LENGTH - WALK_VARIANCE) * TILESIZE to (WALK_LENGTH + WALK_VARIANCE) * TILESIZE
				walkDistance = WALK_LENGTH + WALK_VARIANCE - (Utility.numGen.nextInt(WALK_VARIANCE * 2));
				walkDistance *= GBJam.TILESIZE;
				
				//Advance the walk frame
				walkFrame++;
				
				//Choose a direction
				currDirection = Utility.randomDirection();
				//Set the offset according to direction
				Point newDir = Utility.offsetFromDir(currDirection);
				dx = newDir.x;
				dy = newDir.y;
			}
		} else if(walking) {
			walkDistance--;
			boolean blocked = tryMove(dx * GBJam.TILESIZE / 16, dy * GBJam.TILESIZE / 16);
			if(walkDistance <= 0 ) {
				waiting = true;
				walking = false;
				//Choose a number from WAIT_LENGTH - WAIT_VARIANCE to WAIT_LENGTH + WAIT_VARIANCE
				currWait = WAIT_LENGTH + WAIT_VARIANCE - (Utility.numGen.nextInt(WAIT_VARIANCE * 2));
			}
		}
	}

}
