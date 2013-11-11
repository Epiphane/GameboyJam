package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gilded.gbjam.Structure.StructureAndMap;

public class Coconut extends Entity {
	public static StructureAndMap coconut;
	
	private int dir = GBJam.S;
	private double dx, dy;
	public InGameScreen screen;
	
	//How long the rock lasts, its gravity, etc.
	private static final double COCONUT_GRAVITY = -0.02;
	private static final int COCONUT_LIFESPAN = 15;
	
	/** How long the rock's been alive.  Rock is destroyed when currLife > ROCK_LIFESPAN */
	public int currLife;
	
	/**
	 * Sets the player to a default spot and sets up its sprite sheet.
	 * 
	 * @param x
	 * @param y
	 */
	public Coconut(int x, int y) {
		super(coconut.map);

		this.x = x;
		this.y = y;

		dx = 0;
		dy = 2;
		
		//For some reason a tick is called right as the rock is called. Undo that.
		x -= dx;
		y -= dy;
		
		currLife = 0;
		
		w = coconut.structure.getRegionWidth();
		h = coconut.structure.getRegionHeight();
	}
	
	@Override
	public void tick(Input input) {
		//Check if we're dead
		currLife++;
		if(currLife > COCONUT_LIFESPAN) {
			removed = true;
			currentLevel.addStructure(Item.Coconut((int)x, (int)y, currentLevel), (int) (y / GBJam.TILESIZE));
			return;
		}
		
		//Otherwise fly around at the speed of sound
		tryMove(dx,dy);
//		x += dx;
//		y += dy;
		
		//Get some gravity in your life
		if(dir == GBJam.W || dir == GBJam.E) {
			dy -= COCONUT_GRAVITY;
		}
	}

	public void hitSomething(double dx, double dy, Collideascope thingHit) {
		super.hitSomething(dx, dy, thingHit);
		removed = true;
	}
	
	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(coconut.structure, (int) x, (int) y);
		System.out.println(y);
	}

}
