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
	
	/** Handle to the level so we can access enemies */
	private Level currentLevel;
	
	/** How long the rock's been alive.  Rock is destroyed when currLife > ROCK_LIFESPAN */
	public int currLife;
	
	/**
	 * Sets the player to a default spot and sets up its sprite sheet.
	 * 
	 * @param x
	 * @param y
	 */
	public SlingshotRock(int x, int y, int direction, Level currentLevel) {
		super(Art.itemsMap);

		this.x = x;
		this.y = y;
		dir = direction;

		this.currentLevel = currentLevel;
		
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
		//Check whether we hit an enemy
		for(Entity e : currentLevel.entities) {
			//All we care about are enemies
			if(!(e instanceof Enemy)) continue;
			
			if(e.inTheWay((int) (x), (int) (y), collisionMap)) {
				System.out.println("Hit enemy!");
			}
		}
		
		//Check if we're dead
		currLife++;
		if(currLife > ROCK_LIFESPAN) {
			removed = true;
			return;
		}
		
		//Otherwise fly around at the speed of sound
		tryMove(dx,dy);
		
		//Get some gravity in your life
		if(dir == GBJam.W || dir == GBJam.E) {
			dy -= ROCK_GRAVITY;
		}
	}
	
	public void hitWall(double dx, double dy) {
		super.hitWall(dx, dy);
		removed = true;
	}
	
	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(this.sheet[0][0], (int) x, (int) y);
	}

}
