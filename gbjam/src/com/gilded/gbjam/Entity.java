package com.gilded.gbjam;

import java.util.Random;

public abstract class Entity extends Collideascope {
	protected boolean onGround = false;
	protected static Random random = new Random();
		
	public double dx, dy;
	protected double bounce = 0.05;

	public Level currentLevel;
	
	public boolean removed = false;
	
	public boolean interactsWithWorld = false;
	
	public Entity(byte[][] collisionMap) {
		super(collisionMap);
	}
	
	public void init(Level level) {	
		this.currentLevel = level;
	}
	
	/**
	 * Try to move specified distance.
	 * s
	 * @param dx
	 * @param dy
	 * @return False if we're blocked, true if we're not
	 */
	public boolean tryMove(double dx, double dy) {
		boolean result = false;
		onGround = false;
		Collideascope thingIHit = null;
		// First, try to move horizontally
		thingIHit = currentLevel.canMove(this, x + dx, y, w, h, dx, 0, collisionMap, canPass);
		if(thingIHit == null) {
			x += dx;
			result = true;
		}
		else {
			// Hit a wall
			hitSomething(dx, 0, thingIHit);
			if(dx < 0) {
				double xx = x / GBJam.TILESIZE;
				dx = -(xx - (int) xx) * GBJam.TILESIZE;
			}
			else {
				double xx = (x + w) / GBJam.TILESIZE;
				dx = GBJam.TILESIZE - (xx - (int) xx) / GBJam.TILESIZE;
			}
			dx *= -bounce;
		}
		
		// Next, move vertically
		thingIHit = currentLevel.canMove(this, x, y + dy, w, h, 0, dy, collisionMap, canPass);
		if(thingIHit == null) {
			y += dy;
			result = true;
		}
		else {
			// Hit the wall
			hitSomething(0, dy, thingIHit);
			if(dy < 0) {
				double yy = y / GBJam.TILESIZE;
				dy = -(yy - (int) yy) * GBJam.TILESIZE;
			}
			else {
				double yy = (y + h) / GBJam.TILESIZE;
				dy = GBJam.TILESIZE - (yy - (int) yy) / GBJam.TILESIZE;
			}
			dy *= -bounce;
		}
		
		return result;
	}
	
	/**
	 * Called when you run into a wall
	 * 
	 * @param dx
	 * @param dy
	 */
	public void hitSomething(double dx, double dy, Collideascope thing) {
		if(dx != 0) this.dx = 0;
		if(dy != 0) this.dy = 0;
		//System.out.println(thing);
	}
	
	public void tick(Input input) {
	}
	
	public abstract void render(Screen screen, Camera camera);
	
	public void doPlayerAction(Player player) {
		
	}
	
	public void outOfBounds() {
		/*if(y < 0) return;
		removed = true;*/
	}
	
	public void remove() {
		removed = true;
	}

	public boolean removed() {
		return removed;
	}
}
