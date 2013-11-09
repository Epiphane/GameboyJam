package com.gilded.gbjam;


public class InGameScreen extends Screen {
	public static final int WORLD_WIDTH = 6;
	public static final int WORLD_HEIGHT = 4;
	
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
		world = new Level[WORLD_WIDTH][WORLD_HEIGHT];
		for(int i = 0; i < WORLD_WIDTH; i ++) {
			for(int j = 0; j < WORLD_HEIGHT; j ++) {
				int direction = 0;
				if(i == 0) direction += GBJam.W;
				if(i == WORLD_WIDTH - 1) direction += GBJam.E;
				if(j == 0) direction += GBJam.N;
				if(j == WORLD_HEIGHT - 1) direction += GBJam.S;
				world[i][j] = new Level(this, 15, 12, GBJam.TILESIZE * 2, GBJam.TILESIZE * 2, player);
				world[i][j].createBeachLevel(direction);
				
				
				
				if(i == 0 && j == 2) world[i][j].createStartLevel(player);
			}
		}
	}
	
	public void setLevel(int x, int y) {
		this.x = x;
		this.y = y;
		if(currentLevel != null)
			currentLevel.remove(player);
		currentLevel = world[x][y];
		player.currentLevel = currentLevel;
		currentLevel.init(player);
	}
	
	public void changeLevel(int direction) {
		if(direction == GBJam.N && y > 0) y --; 
		if(direction == GBJam.S && y < world[0].length - 1) y ++;
		if(direction == GBJam.W && x > 0) x --;
		if(direction == GBJam.E && x < world.length - 1) x ++;
		
		if(currentLevel != null)
			currentLevel.remove(player);
		
//		System.out.printf("Changing level to %d, %d\n",x,y);
		currentLevel = world[x][y];
		player.currentLevel = currentLevel;
		
		if((direction == GBJam.N || direction == GBJam.S) && currentLevel.spawn[0] >= 0) {
			player.x = currentLevel.spawn[0] * GBJam.TILESIZE;
//			System.out.println("Changing x to "+player.x* GBJam.TILESIZE);
		}
		if((direction == GBJam.E || direction == GBJam.W) && currentLevel.spawn[1] >= 0) {
			player.y = currentLevel.spawn[1] * GBJam.TILESIZE;
//			System.out.println("Changing y to "+player.y* GBJam.TILESIZE);
		}
		currentLevel.init(player);
	}
	
	public void tick(Input input) {
		currentLevel.tick(input);
	}
	
	@Override
	public void render() {
		currentLevel.render(this, camera);
	}
}
