package com.gilded.gbjam;

public class Boar extends Enemy {
	public Boar(int x, int y) {
		super(Art.enemyMap);
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(Screen screen, Camera camera) {
		screen.draw(Art.enemyWalk[0][0], (int) x, (int) y);
	}
	
	@Override
	public void tick(Input input) {
		
	}

}
