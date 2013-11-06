package com.gilded.gbjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {
	public final static int DIRECTIONS = 4;
	
	public static TextureRegion[][] mainCharacterWalk;
	public static TextureRegion[][] mainCharacterStanding;
	public static TextureRegion[][] tiles;
	public static TextureAtlas structureAtlas;
	
	// More specific things...
	public static TextureRegion airplane;
	public static byte[][] airplaneMap;
	
	public static void load () {
		structureAtlas = new TextureAtlas(Gdx.files.internal("res/structures.txt"), true);
		airplane = structureAtlas.findRegion("airplane");

		Pixmap structureMap = new Pixmap(Gdx.files.internal("res/structures_map.png"));
		airplaneMap = makeCollisionMap(structureAtlas.findRegion("airplane"), structureMap);
		
		mainCharacterWalk = split("res/newplayer.png", 64, 120);
		tiles = split("res/tiles.png", GBJam.TILESIZE, GBJam.TILESIZE);
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
	
	public static byte[][] makeCollisionMap(TextureRegion region, Pixmap map) {
		byte[][] result = new byte[region.getRegionWidth()][region.getRegionHeight()];
		
		int[] mapOffset = new int[] { region.getRegionX(), region.getRegionY() };
		for(int i = 0; i < result.length; i ++) {
			for(int j = 0; j < result[0].length; j ++) {
				int pixel = (map.getPixel(i+mapOffset[0], j+mapOffset[1]) >>> 8);
				if((pixel & 0xff0000) > 0) result[i][j] = -1; // It's a blocker!
				else {
					if((pixel & 0x00ff00) > 0) result[i][j] = 1; // It's a draw over thing!
				}
			}
		}
		
		return result;
	}
}
