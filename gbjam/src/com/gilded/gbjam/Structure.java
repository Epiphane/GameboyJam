package com.gilded.gbjam;


public abstract class Structure {
	public double dx, dy;
	public double x, y;
	public int xSlot, ySlot;
	protected double bounce = 0.05;
	public int w = 10, h = 10;
	
	protected Level level;
	
	public void init(Level level) {	
		this.level = level;
	}
	
	/**
	 * Called when you run into a wall
	 * s
	 * @param dx
	 * @param dy
	 */
	public void hitWall(double dx, double dy) {
		if(dx != 0) this.dx = 0;
		if(dy != 0) this.dy = 0;
	}
	
	public void tick(Input input) {
	}
	
	public abstract void render(Screen screen, Camera camera);
}
