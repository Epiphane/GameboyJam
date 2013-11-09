package com.gilded.gbjam;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;

public class Level {
	public List<Entity> entities = new ArrayList<Entity>();
	
	private Pixmap level;
	/** Two dimensional array with all the tiles */
	public Tile[][] tiles;
	/** Two dimensional array with all the entities in each tile */
	public ArrayList<Entity>[][] entityMap;
	/** Holds all the structures that you are drawn over */
	public ArrayList<Structure>[] structures;
	private final int width, height;
	
	/**
	 * Back reference to the screen that holds this level
	 */
	private Screen screen;
	
	private Player player;
	
	public int[] spawn;
	
	/**
	 * Creates the white room level
	 * 
	 * @param screen Screen calling this constructor
	 * @param width width of level
	 * @param height height of level
	 * @param xSpawn X-Coordinate of the spawn
	 * @param ySpawn Y-Coordinate of the spawn
	 */
	@SuppressWarnings("unchecked")
	public Level(InGameScreen screen, int width, int height, int xSpawn, int ySpawn, Player player) {
		super();
		if(level != null) {
			System.out.println("Bro! We don't load image levels anymore!");
		}
		
		// Set level size and the screen it is in
		this.width = width;
		this.height = height;
		this.screen = screen;
		
		if(screen.camera != null) {
			screen.camera.width = width * GBJam.TILESIZE;
			screen.camera.height = height * GBJam.TILESIZE;
		}
		
		spawn = new int[] { -1, -1 };
		
		// Initialize array for each tile
		tiles = new Tile[width][height];

		// Initialize list of entities for each tile
		entityMap = new ArrayList[width][height];
		structures = new ArrayList[height];
		
		// Load the image/tile map into the walls array
		for(int y = 0; y < height; y ++) {
			structures[y] = new ArrayList<Structure>();
			for(int x = 0; x < width; x ++) {
				// Create empty Entity list here
				entityMap[x][y] = new ArrayList<Entity>();
				
				// Set all tiles to water
				tiles[x][y] = new Tile(Tile.WATER, Tile.FULL);
			}
		}
	}
	
	public void init(Player player) {
		this.player = player;
		player.currentLevel = this;
		add(player);
		
	}

	/**
	 * Create beach level.
	 * @param landDirection - corresponds to GBJam.N, GBJam.E, etc...
	 */
	public void createBeachLevel(int landDirection) {
		int anchorX = -1, anchorY = -1;
		// Set anchors
		if((landDirection & GBJam.N) == GBJam.N) anchorY = height - 1;
		else if((landDirection & GBJam.S) == GBJam.S) anchorY = 0;

		if((landDirection & GBJam.E) == GBJam.E) anchorX = 0;
		else if((landDirection & GBJam.W) == GBJam.W) anchorX = width - 1;
		
		int[] heights = new int[] {width * 3 / 4, height * 3 / 4};
		if(anchorX == -1) {
			anchorX = width / 2;
			heights[0] = width;
		}
		if(anchorY == -1) {
			anchorY = height / 2;
			heights[1] = height;
		}
		createBasicIsland(anchorX, anchorY, heights);
		
		refineIsland();
		
		// If the mountain is N/S oriented, then we need to set spawn[1] or someone could get stuck!
		if(anchorY != -1) {
			spawn[1] = height / 2;
			int dir = 1; // Going north by default
			if(anchorY == 0) { 	// South
				dir = -1;
			}
			
			if(anchorX <= 0) { // Need to check left wall
				if(anchorX == -1) { // Need to check both walls
					while(spawn[1] > 0 && spawn[1] < tiles[0].length - 1 && tiles[tiles.length-1][spawn[1]].blocker) spawn[1] += dir;
				}
				while(spawn[1] > 0 && spawn[1] < tiles[0].length - 1 && tiles[0][spawn[1]].blocker) spawn[1] += dir;
			}
			else { // Need to check right wall
				while(spawn[1] > 0 && spawn[1] < tiles[0].length - 1 && tiles[tiles.length-1][spawn[1]].blocker) spawn[1] += dir;
			}
		}
		
		// If the mountain is E/W oriented, then we need to set spawn[0] or someone could get stuck!
		if(anchorX != -1) {
			spawn[0] = width / 2;
			int dir = 1; // Going west by default
			if(anchorY == 0) { 	// East
				dir = -1;
			}
			
			if(anchorY <= 0) { // Need to check top wall
				if(anchorY == -1) { // Need to check both walls
					while(spawn[0] > 0 && spawn[0] < tiles.length - 1 && tiles[spawn[0]][tiles[0].length-1].blocker) spawn[0] += dir;
				}
				while(spawn[0] > 0 && spawn[0] < tiles.length - 1 && tiles[spawn[0]][0].blocker) spawn[0] += dir;
			}
			else { // Need to check bottom wall
				while(spawn[0] > 0 && spawn[0] < tiles.length - 1 && tiles[spawn[0]][tiles[0].length-1].blocker) spawn[0] += dir;
			}
		}
	}
	
	/**
	 * Create a basic island framework by starting at the middle and "descending"
	 */
	public static final double DROP_CONST = 0.4;
	private void createBasicIsland(int x, int y, int[] height) {
		if(x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length) return;
		
		if(height[0] < 4 && Math.random() * height[0] < DROP_CONST) {
			height[0] --;
		}
		if(height[1] < 4 && Math.random() * height[1] < DROP_CONST) {
			height[1] --;
		}
		
		if(height[0] <= 0 || height[1] <= 0) return;
		
		int[] currentElevation = tiles[x][y].elevation;
		if(height[0] > currentElevation[0] && height[1] > currentElevation[1]) {
			Tile newTile = new Tile(Tile.SAND, Tile.FULL + Math.abs((x - y) % 2));
			newTile.elevation[0] = Math.max(currentElevation[0], height[0]);
			newTile.elevation[1] = Math.max(currentElevation[1], height[1]);
			tiles[x][y] = newTile;
		}
		else {
			return;
		}
		
		createBasicIsland(x - 1, y, new int[] {height[0] - 1, height[1] });
		createBasicIsland(x, y - 1, new int[] {height[0], height[1] - 1 });
		createBasicIsland(x, y + 1, new int[] {height[0], height[1] - 1 });
		createBasicIsland(x + 1, y, new int[] {height[0] - 1, height[1] });	
	}
	
	/**
	 * Refine the edges of the island, changing the sand tiles to sand fading into water tiles
	 */
	private void refineIsland() {
		Tile[][] newTiles = new Tile[tiles.length][tiles[0].length];
		for(int x = 0; x < tiles.length; x ++) {
			for(int y = 0; y < tiles[0].length; y ++) {
				Tile currentTile = tiles[x][y];
				if(currentTile.type == Tile.WATER) {
					newTiles[x][y] = currentTile;
					continue;
				}
				
				int flag = 0;
				if(x == 0 || y == tiles[0].length - 1 || 
						tiles[x - 1][y + 1].type == Tile.SAND &&
						tiles[x - 1][y].type == Tile.SAND &&
						tiles[x][y + 1].type == Tile.SAND) flag ++;
				if(x == tiles.length - 1 || y == tiles[0].length - 1 || 
						tiles[x + 1][y + 1].type == Tile.SAND &&
						tiles[x + 1][y].type == Tile.SAND &&
						tiles[x][y + 1].type == Tile.SAND) flag += 2;
				if(x == tiles.length - 1 || y == 0 ||
						tiles[x + 1][y - 1].type == Tile.SAND &&
						tiles[x + 1][y].type == Tile.SAND &&
						tiles[x][y - 1].type == Tile.SAND) flag += 4;
				if(x == 0 || y == 0 ||
						tiles[x - 1][y - 1].type == Tile.SAND &&
						tiles[x - 1][y].type == Tile.SAND &&
						tiles[x][y - 1].type == Tile.SAND) flag += 8;
				
				if(flag == 0 || flag == 15) {
					newTiles[x][y] = currentTile;
					if(Math.random() > 0.9 && x > 2 && y > 2 && x < tiles.length - 3 && y < tiles[0].length - 3) {
						if(flag == 0 || Math.random() > 0.5) {
							structures[y].add(Structure.Rock(x, y));
						}
						else {
							structures[y].add(Structure.PalmTree(x, y));
						}
					}
				}
				else {
					newTiles[x][y] = new Tile(Tile.WATER, flag);
				}
			}
		}
		tiles = newTiles;
	}
	
	public void createStartLevel(Player player) {
		for(ArrayList<Structure> array : structures) array.clear();
		while(tiles[(int) (player.x / GBJam.TILESIZE)][height / 2].type != Tile.SAND) player.x += GBJam.TILESIZE;
		
		player.x += GBJam.TILESIZE * 3;

		int xSlot = (int) (player.y) / GBJam.TILESIZE + 2;
		int ySlot = (int) (player.y) / GBJam.TILESIZE - 4;
		structures[ySlot].add(Structure.Airplane(0, ySlot));

		ySlot += 2;
		int x = xSlot * GBJam.TILESIZE;
		int y = ySlot * GBJam.TILESIZE;
		structures[ySlot].add(Item.Sword(x + 14, y));
		structures[ySlot].add(Item.Slingshot(x + 28, y));
	}
	
	/**
	 * Add an entity to the game. Automatically figures out what tile it's on.
	 * 
	 * @param e - The thingy
	 */
	public void add(Entity e) {
		entities.add(e);
		e.init(this);
		
		// Determine 'slot' that Entity is in in world
		e.xSlot = (int)((e.x + e.w / 2.0) / GBJam.TILESIZE);
		e.ySlot = (int)((e.y + e.h / 2.0) / GBJam.TILESIZE);
		
		// If that's within the viewport, add the entity
		if(e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < width && e.ySlot < height)
			entityMap[e.xSlot][e.ySlot].add(e);
	}
	
	public void remove(Entity e) {
		entities.remove(e);
		
		// Determine 'slot' that Entity is in in world
		e.xSlot = (int)((e.x + e.w / 2.0) / GBJam.TILESIZE);
		e.ySlot = (int)((e.y + e.h / 2.0) / GBJam.TILESIZE);
		
		// If that's within the viewport, add the entity
		if(e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < width && e.ySlot < height)
			entityMap[e.xSlot][e.ySlot].remove(e);
	}
	
	/**
	 * Update loop for the level. Cycles through all the entities
	 * and has them update themselves.
	 */
	public void tick(Input input) {
		for(int i = 0; i < entities.size(); i ++) {
			Entity e = entities.get(i);
			int xSlotOld = e.xSlot;
			int ySlotOld = e.ySlot;
			
			// Perform calculations on Entity
			if(!e.removed) e.tick(input);

			// Determine 'slot' that Entity is in in world
			e.xSlot = (int)((e.x + e.w / 2.0) / GBJam.TILESIZE);
			e.ySlot = (int)((e.y + e.h / 2.0) / GBJam.TILESIZE);
			
			// Entity has been removed for whatever reason
			if(e.removed) {
				// If it was once within the viewport, remove it
				if(xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < width && ySlotOld < height) {
					entityMap[xSlotOld][ySlotOld].remove(e);
				}
				entities.remove(i--);
			}
			else {
				// Only make changes if it moved slots
				if(e.xSlot != xSlotOld || e.ySlot != ySlotOld) {
					// If it was once within the viewport, remove it from that spot
					if(xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < width && ySlotOld < height)
						entityMap[xSlotOld][ySlotOld].remove(e);

					// If it's still within the level boundaries, add it
					if(e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < width && e.ySlot < height)
						entityMap[e.xSlot][e.ySlot].add(e);
					else
						e.outOfBounds();
				}
			}
		}
	}
	
	Matrix4 matrix = new Matrix4();
	/**
	 * Render the level to the screen
	 * 
	 * @param screen
	 * @param camera
	 */
	public void render(Screen screen, Camera camera) {
		camera.move((int)player.x - GBJam.GAME_WIDTH / 2,(int)player.y - GBJam.GAME_HEIGHT / 2);
		
		matrix.idt();
		matrix.setToTranslation(camera.x * -1, camera.y * -1, 0);
		screen.getSpriteBatch().setTransformMatrix(matrix);
		screen.getSpriteBatch().begin();
		
		// Start off at the camera's location
		int xo = camera.x / GBJam.TILESIZE;
		int yo = camera.y / GBJam.TILESIZE;
		
		// Draw all the tiles between xo and the end of screen, and yo and the end of screen
		for(int y = yo; y < yo + camera.height / GBJam.TILESIZE; y ++) {
			if(y >= 0 && y < height) {
				for(int x = xo; x < xo + camera.width / GBJam.TILESIZE; x ++) {
					if(x >= 0 && x < width) {
						Tile tile = tiles[x][y];
						
						// Draw the tile!
						screen.draw(tile.textureAndMap.texture, x * GBJam.TILESIZE, y * GBJam.TILESIZE);
					}
				}
			}
		}
		
		for(int y = 0; y < height; y ++) {
			if(y >= 0 && y < height) {

				for(int x = xo; x < xo + camera.width / GBJam.TILESIZE; x ++) {
					if(x >= 0 && x < width) {
						for(Entity entity : entityMap[x][y]) {
							entity.render(screen, camera);
						}
					}
				}
				
				for(Structure structure : structures[y]) {
					structure.render(screen, camera);
				}
	
			}
		}
		
		screen.getSpriteBatch().end();
	}

	public boolean canMove(Entity entity, double xc, double yc, int w, int h,
			double dx, double dy, byte[][] map) {

		// Buffer
		double e = 0;
		
		// Set initial and goal values
		int x0 = Math.max((int)(xc / GBJam.TILESIZE),0);
		int y0 = Math.max((int)(yc / GBJam.TILESIZE),0);
		int x1 = Math.min((int)((xc + w - e) / GBJam.TILESIZE),tiles.length-1);
		int y1 = Math.min((int)((yc + h - e) / GBJam.TILESIZE),tiles[0].length-1);
		
		// Good so far...
		boolean ok = true;

		for(int y = y0; y <= y1; y ++) {
			for(int x = x0; x <= x1; x ++) {
				if(x >= 0 && y >= 0 && x < width && y < height) {
					Tile tile = tiles[x][y];
					if(tile.inTheWay((int) xc - (x + 1) * GBJam.TILESIZE, (int) yc - (y) * GBJam.TILESIZE, map)) {
						//System.out.println(player.xSlot + " x " + x);
						return false;
					}
				}
			}
		}
		
		for(int y = 0; y < structures.length; y ++) {
			for(Structure structure : structures[y]) {
				if(structure.inTheWay((int) xc, (int) yc, map)) {
					if(structure.doActionOnCollision)
						if(structure.doAction(entity))
							structures[y].remove(structure);
					//System.out.println(player.ySlot + " x " + structure.ySlot);
					return false;
				}
			}
		}
		
		return ok;
	}

	public Player getPlayer() {
		return player;
	}


	
	/**
	 * This method tells the tile at playerposition + dir
	 * to do its on-enter action
	 * @param dir Which direction the player is facing
	 */
	public void activateTile(int dir) {

		int tileX = (int) player.x / GBJam.TILESIZE;
		int tileY = (int) player.y / GBJam.TILESIZE;
		
		switch(dir) {
		case GBJam.N:
			tileY--;
			break;
		case GBJam.E:
			tileX++;
			break;
		case GBJam.S:
			tileY++;
			break;
		case GBJam.W:
			tileX--;
			break;
		}
		
		//If they tried to activate a tile off-screen... that doesn't even make
		//sense. What are you doing??? Get down from there!
		if(tileX < 0 || tileY < 0 || tileX >= tiles.length || tileY >= tiles[0].length) System.out.println("dagnabbit!");
		else tiles[tileX][tileY].doAction();
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
