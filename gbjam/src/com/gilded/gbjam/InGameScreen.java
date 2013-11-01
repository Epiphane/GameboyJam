package com.gilded.gbjam;


public class InGameScreen extends Screen {
	private Level level;
	
	public InGameScreen() {
		camera = new Camera(GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT);
		level = new Level(this, 20, 18, GBJam.TILESIZE * 2, GBJam.TILESIZE * 2, Art.level);
	}
	
	public void tick(Input input) {
		if(!level.getPlayer().removed())
			level.getPlayer().tick(input);
	}
	
	@Override
	public void render() {
		level.render(this, camera);
	}
}
