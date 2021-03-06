package com.gilded.gbjam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gilded.gbjam.Structure.StructureAndMap;
import com.gilded.gbjam.Tile.TextureAndMap;

public class Art {
	
	public final static int DIRECTIONS = 4;
	
	public static TextureRegion[][] mainCharacterWalk;
	public static TextureRegion[][] mainCharacterStanding;
	public static byte[][] mainCharacterMap;
	
	public static TextureRegion[][] enemyWalk;
	public static byte[][] enemyMap;
	
	public static TextureAndMap[][] tiles;
	
	public static Hashtable<String, int[]> offsets;
	
	public static TextureRegion splashScreen;

	public static TextureRegion healthIcon;
	public static TextureRegion healthFill;
	public static TextureRegion weaponIcon;
	public static TextureRegion generalBar;
    
    private String currentWeaponPath;
	// More specific things...
	
	//Item time
	public static TextureRegion[][] items;
	public static byte[][] itemsMap;
	
	public static boolean loaded = false;
	
	public static void load () throws IOException {
		splashScreen = load("res/splash.png", 160, 144);
		healthIcon = load("res/heart.png", 7, 6);
		healthFill = load("res/healthfill.png", 4, 6);
		generalBar = load("res/healthbarframe.png", 128, 8);
		weaponIcon = load("res/weaponIcon.png", 32, 32);
		
		items = split("res/items.png", 24, 24);
		itemsMap = makeCollisionMap(new Pixmap(Gdx.files.internal("res/items_map.png")));
		
		loadSpritesFromCommonSheet();
		
		mainCharacterWalk = split("res/newplayer.png", 32, 30);
		mainCharacterMap = makeCollisionMap(new Pixmap(Gdx.files.internal("res/newplayer_map.png")));
		
		enemyWalk = split("res/enemy.png", 32, 30);
		enemyMap = makeCollisionMap(new Pixmap(Gdx.files.internal("res/enemy_map.png")));
		
		tiles = splitTextureMap("res/tiles.png", "res/tiles_map.png", GBJam.TILESIZE, GBJam.TILESIZE);
		
		loaded = true;
	}
	
	private static void loadSpritesFromCommonSheet() throws IOException {
		// Loads from the txt file the offset of each byte[][] array
		loadOffsets(Gdx.files.internal("res/sprites.txt"));
		Pixmap spriteMap = new Pixmap(Gdx.files.internal("res/sprites_map.png"));
		
		// Loads an atlas with all the TextureRegions we'll be able to get from structures.png
		TextureAtlas spriteAtlas = new TextureAtlas(Gdx.files.internal("res/sprites.txt"), true);
		
		Structure.airplane = loadStructure("airplane", spriteAtlas, spriteMap);
		Structure.rock = loadStructure("rock", spriteAtlas, spriteMap);
		Structure.palm = loadStructure("palm", spriteAtlas, spriteMap);
		Structure.palm_empty = loadStructure("palm_empty", spriteAtlas, spriteMap);
		Structure.temple = loadStructure("temple", spriteAtlas, spriteMap);
		Structure.temple_door = loadStructure("temple_door", spriteAtlas, spriteMap);
		Structure.bush = loadStructure("bush", spriteAtlas, spriteMap);
		Item.sword = loadStructure("sword", spriteAtlas, spriteMap);
		Item.slingshot = loadStructure("slingshot", spriteAtlas, spriteMap);
		Item.flaregun = loadStructure("flaregun", spriteAtlas, spriteMap);
		Item.torch = loadStructure("torch", spriteAtlas, spriteMap);
		Item.coconut = loadStructure("coconut", spriteAtlas, spriteMap);
		
		Structure.tree = new StructureAndMap[4];
		Structure.tree[0] = loadStructure("tree", spriteAtlas, spriteMap);
		Structure.tree[1] = loadStructure("tree1", spriteAtlas, spriteMap);
		Structure.tree[2] = loadStructure("tree2", spriteAtlas, spriteMap);
		Structure.tree[3] = loadStructure("stump", spriteAtlas, spriteMap);
		
		Coconut.coconut = loadStructure("coconut_falling", spriteAtlas, spriteMap);
	}
	
	private static StructureAndMap loadStructure(String name, TextureAtlas structureAtlas, Pixmap structureMap) {
		StructureAndMap structure = new StructureAndMap();
		structure.structure = structureAtlas.findRegion(name);
		structure.map = makeCollisionMap((AtlasRegion)structure.structure, structureMap, offsets.get(name));
		
		return structure;
	}

	private static TextureRegion[][] split (String name, int width, int height) {
		return split(name, width, height, false);
	}

	private static TextureRegion[][] split (String name, int width, int height, boolean flipX) {
		Texture texture = new Texture(Gdx.files.internal(name));
		System.out.println(name + ": " + texture.getWidth());
		int xSlices = texture.getWidth() / width;
		int ySlices = texture.getHeight() / height;
		TextureRegion[][] res = new TextureRegion[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				res[x][y] = new TextureRegion(texture, x * width, y * height, width, height);
				res[x][y].flip(flipX, true);
			}
		}
		return res;
	}

	private static TextureAndMap[][] splitTextureMap (String name, String mapName, int width, int height) {
		return splitTextureMap(name, mapName, width, height, false);
	}

	private static TextureAndMap[][] splitTextureMap (String name, String mapName, int width, int height, boolean flipX) {
		Texture texture = new Texture(Gdx.files.internal(name));
		Pixmap textureMap = new Pixmap(Gdx.files.internal(mapName));
		System.out.println(name + ": " + texture.getWidth());
		int xSlices = texture.getWidth() / width;
		int ySlices = texture.getHeight() / height;
		TextureAndMap[][] res = new TextureAndMap[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				res[x][y] = new TextureAndMap();
				res[x][y].texture = new TextureRegion(texture, x * width, y * height, width, height);
				res[x][y].texture.flip(flipX, true);
				
				res[x][y].map = new byte[width][height];
				for(int i = 0; i < width; i ++) {
					for(int j = 0; j < height; j ++) {
						int pixel = (textureMap.getPixel(x*width + i, y*height + j) >>> 8);
						res[x][y].map[i][j] = (byte) (pixel >>> 16);
					}
				}
			}
		}
		return res;
	}

	public static TextureRegion load (String name, int width, int height) {
		Texture texture = new Texture(Gdx.files.internal(name));
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		region.flip(false, true);
		return region;
	}
	
	public static byte[][] makeCollisionMap(AtlasRegion region, Pixmap map, int[] offset) {
		byte[][] result = new byte[region.getRegionWidth()][region.getRegionHeight()];
		
		for(int i = 0; i < result.length; i ++) {
			for(int j = 0; j < result[0].length; j ++) {
				int pixel = (map.getPixel(i+offset[0], j+offset[1]) >>> 8);
				result[i][j] = (byte) (pixel >>> 16);
			}
		}
		
		return result;
	}
	
	public static byte[][] makeCollisionMap(Pixmap map) {
		byte[][] result = new byte[map.getWidth()][map.getHeight()];
		
		for(int i = 0; i < result.length; i ++) {
			for(int j = 0; j < result[0].length; j ++) {
				int pixel = (map.getPixel(i, j) >>> 8);
				//if((pixel & 0xff0000) > 0) result[i][j] = -1; // It's a blocker!
				//else {
				//	if((pixel & 0x00ff00) > 0) result[i][j] = 1; // It's a draw over thing!
				//}
				result[i][j] = (byte) (pixel >>> 16);
			}
		}

		return result;
	}
	
	public static void loadOffsets(FileHandle packFile) throws IOException {
		offsets = new Hashtable<String, int[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
        try {
            boolean pageImage = false;
            while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    if (line.trim().length() == 0)
                            pageImage = false;
                    else if (!pageImage) {
                        readValue(reader);

                        readTuple(reader);
                        readValue(reader);

                        pageImage = true;
                    }
                    else {
                            boolean rotate = Boolean.valueOf(readValue(reader));

                            readTuple(reader);
                            int left = Integer.parseInt(tuple[0]);
                            int top = Integer.parseInt(tuple[1]);

                            readTuple(reader);
                            int width = Integer.parseInt(tuple[0]);
                            int height = Integer.parseInt(tuple[1]);

                            Region region = new Region();
                            region.left = left;
                            region.top = top;
                            region.width = width;
                            region.height = height;
                            region.name = line;
                            region.rotate = rotate;

                            if (readTuple(reader) == 4) { // split is optional
                                    region.splits = new int[] {Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                            Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                                    if (readTuple(reader) == 4) { // pad is optional, but only present with splits
                                            region.pads = new int[] {Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                                    Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                                            readTuple(reader);
                                    }
                            }

                            region.originalWidth = Integer.parseInt(tuple[0]);
                            region.originalHeight = Integer.parseInt(tuple[1]);

                            readTuple(reader);
                            region.offsetX = Integer.parseInt(tuple[0]);
                            region.offsetY = Integer.parseInt(tuple[1]);

                            region.index = Integer.parseInt(readValue(reader));

                            offsets.put(line, new int[] {left, top});
                    }
            }
	    //} catch (Exception ex) {
	     //       throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
	    } finally {
	    	reader.close();
	    }
	}

    static final String[] tuple = new String[4];
    /** Returns the number of tuple values read (2 or 4). */
    static int readTuple (BufferedReader reader) throws IOException {
            String line = reader.readLine();
            int colon = line.indexOf(':');
            if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line);
            int i = 0, lastMatch = colon + 1;
            for (i = 0; i < 3; i++) {
                    int comma = line.indexOf(',', lastMatch);
                    if (comma == -1) {
                            if (i == 0) throw new GdxRuntimeException("Invalid line: " + line);
                            break;
                    }
                    tuple[i] = line.substring(lastMatch, comma).trim();
                    lastMatch = comma + 1;
            }
            tuple[i] = line.substring(lastMatch).trim();
            return i + 1;
    }

    static String readValue (BufferedReader reader) throws IOException {
            String line = reader.readLine();
            int colon = line.indexOf(':');
            if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line);
            return line.substring(colon + 1).trim();
    }
}
