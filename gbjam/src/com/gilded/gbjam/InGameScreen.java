package com.gilded.gbjam;


public class InGameScreen extends Screen {
	public Level[][] world;
	private Level currentLevel;
	private Player player;
	private int x, y;
	
	public InGameScreen() {
		camera = new Camera(GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT);
		
		// Create player
		player = new Player(0, 6 * GBJam.TILESIZE);
		player.currentLevel = currentLevel;
		player.screen = this;
		
		// TODO: Get rid of this once you can actually click new game
		newGame();
		setLevel(0,2);
	}
	
	public void newGame() {
		world = new Level[8][8];
		for(int i = 0; i < 8; i ++) {
			for(int j = 0; j < 8; j ++) {
				int direction = 0;
				if(i == 0) direction += GBJam.W;
				if(i == 7) direction += GBJam.E;
				if(j == 0) direction += GBJam.N;
				if(j == 7) direction += GBJam.S;
				world[i][j] = new Level(this, 15, 12, GBJam.TILESIZE * 2, GBJam.TILESIZE * 2, player);
				world[i][j].createBeachLevel(direction);
				
				if(i == 0 && j == 2) world[i][j].createStartLevel();
			}
		}
	}
	
	public void setLevel(int x, int y) {
		this.x = x;
		this.y = y;
		currentLevel = world[x][y];
		player.currentLevel = currentLevel;
	}
	
	public void changeLevel(int direction) {
		if(direction == GBJam.N && y > 0) y --; 
		if(direction == GBJam.S && y < world[0].length - 1) y ++;
		if(direction == GBJam.W && x > 0) x --;
		if(direction == GBJam.E && x < world.length - 1) x ++;
		
		System.out.printf("Changing level to %d, %d\n",x,y);
		currentLevel = world[x][y];
		player.currentLevel = currentLevel;
	}
	
	public void tick(Input input) {
		currentLevel.tick(input);
	}
	
	@Override
	public void render() {
		currentLevel.render(this, camera);
	}
}
