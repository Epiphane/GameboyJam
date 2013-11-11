package com.gilded.gbjam;

import java.awt.Point;

public class Boar extends Enemy {
	
	// === Behavior Stuff ===
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
	private int walkTicks = 0;
	/** How many frames are in the walk animation */
	private static final int WALK_FRAMES = 4;
	/** How long each frame takes (in ticks) */
	private static final int FRAME_LENGTH = 10;
	
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
		currWait = Utility.randomRange(WAIT_LENGTH, WAIT_VARIANCE);
	}

	@Override
	public void render(Screen screen, Camera camera) {

		//Adjust the frame-number by dividing by 2 and multiplying by how many
		//frames are in each walk-cycle
		int baseFrame = (GBJam.DIRECTIONS[currDirection]/2) * WALK_FRAMES;

		//Modify baseFrame based on how long we've been walking in this direction
		baseFrame += frame;

		screen.draw(Art.enemyWalk[baseFrame][BOAR], (int) x, (int) y);
	}
	
	@Override
	public void tick(Input input) {
		if(input.buttonStack.peek() == Input.ACTION) {
//			printMap(collisionMap);
			//Enable to debug collision map I spose.
		}
		
		//Boarhavior: wait around for 1-3 seconds then walk in a random direction
		//for 3-5 tiles
		if(waiting) {
			currWait--;
			if(currWait <= 0) {
				///// SWITCH TO WALKING /////
				waiting = false;
				walking = true;
				//Choose a number from (WALK_LENGTH - WALK_VARIANCE) * TILESIZE to (WALK_LENGTH + WALK_VARIANCE) * TILESIZE
				walkDistance = Utility.randomRange(WALK_LENGTH, WALK_VARIANCE);
				walkDistance *= GBJam.TILESIZE;
				
				//Choose a direction
				currDirection = Utility.randomCardinalDirection();
				//Set the offset according to direction
				Point newDir = Utility.offsetFromDir(currDirection);
				dx = newDir.x;
				dy = newDir.y;
			}
		} else if(walking) {
			walkDistance--;

			//Advance the walk frame
			walkTicks++;
			
			if(walkTicks % FRAME_LENGTH == 0) {
				//The boar has walked long enough such that we need to go forward a frame
				frame++;
				walkTicks = 0;
				//Make frame roll over
				frame = frame % WALK_FRAMES;
			}
			
			boolean pathClear = tryMove(dx * GBJam.TILESIZE / 16, dy * GBJam.TILESIZE / 16);
			if(!pathClear) System.out.println("hit a blocker!!!");
			if(walkDistance <= 0 || !pathClear) {
				///// SWITCH TO IDLING /////
				waiting = true;
				walking = false;
				
				frame = 0;
				
				//Choose a number from WAIT_LENGTH - WAIT_VARIANCE to WAIT_LENGTH + WAIT_VARIANCE
				currWait = Utility.randomRange(WAIT_LENGTH, WAIT_VARIANCE);
			}
		}
	}

}
