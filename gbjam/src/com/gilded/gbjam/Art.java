package com.gilded.gbjam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Art {
	public final static int DIRECTIONS = 4;
	
	public static TextureRegion[][] mainCharacterWalk;
	public static TextureRegion[][] mainCharacterStanding;
	public static byte[][] mainCharacterMap;
	public static TextureRegion[][] tiles;
	public static TextureAtlas structureAtlas;
	
	public static Hashtable<String, int[]> offsets;
	
	// More specific things...
	public static TextureRegion airplane;
	public static byte[][] airplaneMap;
	
	//Item time
	public static TextureRegion[][] items;
	public static byte[][] itemsMap;
	
	public static boolean loaded = false;
	
	public static void load () throws IOException {
		loadOffsets(Gdx.files.internal("res/structures.txt"));

		structureAtlas = new TextureAtlas(Gdx.files.internal("res/structures.txt"), true);
		airplane = structureAtlas.findRegion("airplane");

		Pixmap structureMap = new Pixmap(Gdx.files.internal("res/structures_map.png"));
		
		items = split("res/items.png", 24, 24);
		itemsMap = makeCollisionMap(new Pixmap(Gdx.files.internal("res/items_map.png")));
		
		airplaneMap = makeCollisionMap((AtlasRegion) airplane, structureMap, offsets.get("airplane"));
		
		mainCharacterWalk = split("res/newplayer.png", 32, 30);
		mainCharacterMap = makeCollisionMap(new Pixmap(Gdx.files.internal("res/newplayer_map.png")));
		tiles = split("res/tiles.png", GBJam.TILESIZE, GBJam.TILESIZE);
		
		loaded = true;
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
