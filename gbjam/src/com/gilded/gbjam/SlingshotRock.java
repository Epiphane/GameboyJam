package com.gilded.gbjam;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SlingshotRock extends Entity {

	private int dir = GBJam.S;
	private double dx, dy;
	public InGameScreen screen;

	private TextureRegion[][] sheet;
	
	//How long the rock lasts, its gravity, etc.
	private static final int ROCK_SPEED = 4;
	private static final double ROCK_GRAVITY = -0.02;
	private static final int ROCK_LIFESPAN = 25;
	
	/** How long the rock's been alive.  Rock is destroyed when currLife > ROCK_LIFESPAN */
	public int currLife;
	
	/**
	 * Sets the player to a default spot and sets up its sprite sheet.
	 * 
	 * @param x
	 * @param y
	 */
	public SlingshotRock(int x, int y, int direction) {
		super(Art.itemsMap);

		this.x = x;
		this.y = y;
		dir = direction;

		Point dp = Utility.offsetFromDir(direction);
		dx = dp.x * ROCK_SPEED;
		dy = dp.y * ROCK_SPEED;
		
		//For some reason a tick is called right as the rock is called. Undo that.
		x -= dx;
		y -= dy;
		
		currLife = 0;
		
		this.sheet = Art.items;
		w = sheet[0][0].getRegionWidth();
		h = sheet[0][0].getRegionHeight();
	}
	
	@Override
	public void tick(Input input) {
		//Check if we're dead
		currLife++;
		if(currLife > ROCK_LIFESPAN) {
			removed = true;
			return;
		}
		
		//Otherwise fly around at the speed of sound
		tryMove(dx,dy);
//		x += dx;
//		y += dy;
		
		//Get some gravity in your life
		if(dir == GBJam.W || dir == GBJam.E) {
			dy -= ROCK_GRAVITY;
		}
	}
	
	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(this.sheet[0][0], (int) x, (int) y);
	}

}
