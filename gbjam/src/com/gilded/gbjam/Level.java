package com.gilded.gbjam;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;

public class Level {
	public List<Entity> entities = new ArrayList<Entity>();
	
	private Pixmap level;
	/** One dimensional array with all the tiles (wraps horizontally) */
	public Tile[] tiles;
	/** One dimensional array with all the entities in each tile (wraps horizontally) */
	public ArrayList<Entity>[] entityMap;
	private final int width, height;
	
	/**
	 * Back reference to the screen that holds this level
	 */
	private Screen screen;
	
	private Player player;
	private int ySpawn;
	private int xSpawn;
	
	/**
	 * Creates the white room level
	 * 
	 * @param screen Screen calling this constructor
	 * @param width width of level
	 * @param height height of level
	 * @param xSpawn X-Coordinate of the spawn
	 * @param ySpawn Y-Coordinate of the spawn
	 */
	public Level(Screen screen, int width, int height, int xSpawn, int ySpawn, Pixmap level) {
		super();
		this.level = level;
		
		// Set level size and the screen it is in
		this.width = width;
		this.height = height;
		this.screen = screen;
		
		if(screen.camera != null) {
			screen.camera.width = width * Art.TILESIZE;
			screen.camera.height = height * Art.TILESIZE;
		}
		
		// Spawn point for main character
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		
		// Initialize array for each tile
		tiles = new Tile[width * height];

		// Initialize list of entities for each tile
		entityMap = new ArrayList[width * height];
		
		// Load the image/tile map into the walls array
		for(int y = 0; y < height; y ++) {
			for(int x = 0; x < width; x ++) {
				// Create empty Entity list here
				entityMap[x + y*width] = new ArrayList<Entity>();

				// Load pixel's RGB into an integer
				int col = (this.level.getPixel(x, y) & 0xffffff00) >>> 8;
				byte wall = -1; // Default nothingness
				
				
				if(col == 0x000000)			// Default wall
					wall = 0;
				else if(col == 0x555555)	// Second kind of wall
					wall = 1;
				
				else if(col == 0x007700)	// Grass 1
					wall = 2;
				else if(col == 0x009900)	// Grass 2
					wall = 3;
				else if(col == 0x00cc00)	// Grass 3 (brighter)
					wall = 4;
				else if(col == 0x22cc00)	// Grass 3 (brighter + flower 1)
					wall = 5;
				else if(col == 0x44cc00)	// Grass 3 (brighter + flower 2)
					wall = 6;
				else if(col == 0xcccccc)	// Grass 4 (brightest)
					wall = 7;
				else if(col == 0xffffff)	// Grass 4 (white)
					wall = 8;
				
				else if(col == 0x441100)	// Sign
					wall = 9;
				
				else if(col == 0x0000cc)	// Water 1 (empty)
					wall = 10;
				else if(col == 0x1100cc)	// Water 1 (E wall)
					wall = 11;
				else if(col == 0x2200cc)	// Water 1 (NE Corner)
					wall = 12;
				else if(col == 0x3300cc)	// Water 1 (N wall)
					wall = 13;
				else if(col == 0x4400cc)	// Water 1 (NW Corner)
					wall = 14;
				else if(col == 0x5500cc)	// Water 1 (W wall)
					wall = 15;
				
				// Set wall in byte array
				tiles[x + y * width] = new Tile(wall);
			}
		}
		
		// Create player
		player = new Player(this.xSpawn, this.ySpawn);
		add(player);
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
		e.xSlot = (int)((e.x + e.w / 2.0) / Art.TILESIZE);
		e.ySlot = (int)((e.y + e.h / 2.0) / Art.TILESIZE);
		
		// If that's within the viewport, add the entity
		if(e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < width && e.ySlot < width)
			entityMap[e.xSlot + e.ySlot * width].add(e);
	}
	
	/**
	 * Update loop for the level. Cycles through all the entities
	 * and has them update themselves.
	 */
	public void tick() {
		for(int i = 0; i < entities.size(); i ++) {
			Entity e = entities.get(i);
			int xSlotOld = e.xSlot;
			int ySlotOld = e.ySlot;
			
			// Perform calculations on Entity
			if(!e.removed) e.tick();

			// Determine 'slot' that Entity is in in world
			e.xSlot = (int)((e.x + e.w / 2.0) / Art.TILESIZE);
			e.ySlot = (int)((e.y + e.h / 2.0) / Art.TILESIZE);
			
			// Entity has been removed for whatever reason
			if(e.removed) {
				// If it was once within the viewport, remove it
				if(xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < width && ySlotOld < width) {
					entityMap[xSlotOld + ySlotOld * width].remove(e);
				}
				entities.remove(i--);
			}
			else {
				// Only make changes if it moved slots
				if(e.xSlot != xSlotOld || e.ySlot != ySlotOld) {
					// If it was once within the viewport, remove it from that spot
					if(xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < width && ySlotOld < width)
						entityMap[xSlotOld + ySlotOld * width].remove(e);

					// If it's still within the level boundaries, add it
					if(e.xSlot >= 0 && e.ySlot >= 0 && e.xSlot < width && e.ySlot < width)
						entityMap[e.xSlot + e.ySlot * width].add(e);
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
		matrix.idt();
		matrix.setToTranslation(camera.x * -1, camera.y * -1, 0);
		screen.getSpriteBatch().setTransformMatrix(matrix);
		screen.getSpriteBatch().begin();
		
		// Start off at the camera's location
		int xo = camera.x / Art.TILESIZE;
		int yo = camera.y / Art.TILESIZE;
		
		// Draw all the tiles between xo and the end of screen, and yo and the end of screen
		for(int x = xo; x < xo + camera.width / Art.TILESIZE; x ++) {
			for(int y = yo; y < yo + camera.height / Art.TILESIZE; y ++) {
				if(x >= 0 && y >= 0 && x < width && y < height) {
					Tile tile = tiles[x + y * width];
					
					// Draw the tile!
					screen.draw(tile.display, x * Art.TILESIZE, y * Art.TILESIZE);
				}
			}
		}
		
		// Draw all entities
		for(int i = entities.size() - 1; i >= 0; i --) {
			Entity e = entities.get(i);
			e.render(screen, camera);
		}
		
		screen.getSpriteBatch().end();
	}

	public boolean canMove(Entity entity, double xc, double yc, int w, int h,
			double dx, double dy) {

		// Buffer
		double e = 0;
		
		// Set initial and goal values
		int x0 = (int)(xc / Art.TILESIZE);
		int y0 = (int)(yc / Art.TILESIZE);
		int x1 = (int)((xc + w - e) / Art.TILESIZE);
		int y1 = (int)((yc + h - e) / Art.TILESIZE);
		
		// Good so far...
		boolean ok = true;
		
		for(int x = x0; x <= x1; x ++)
			for(int y = y0; y <= y1; y ++) {
				if(x >= 0 && y >= 0 && x < width && y < height) {
					Tile tile = tiles[x + y * width];
					ok &= !tile.blocker;
				}
			}
		
		return ok;
	}

	public Player getPlayer() {
		return player;
	}
}
