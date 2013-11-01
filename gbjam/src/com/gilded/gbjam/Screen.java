package com.gilded.gbjam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public abstract class Screen {
	// Keeps track of the tile offset
	public Camera camera;
	
	private final String[] chars = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", ".,!?:;\"'+-=/\\< "};
	private SpriteBatch spriteBatch;

	protected static Random random = new Random();
	private GBJam appListener;

	/**
	 * Make sure that the sprites are removed
	 */
	public void removed() {
		spriteBatch.dispose();
	}
	
	/**
	 * Create projection matrix for displaying and a large sprite batch
	 * @param appListener
	 */
	public final void init(GBJam appListener) {
		this.appListener = appListener;
		
		Matrix4 projection = new Matrix4();
		projection.setToOrtho(0, GBJam.GAME_WIDTH, GBJam.GAME_HEIGHT, 0, -1, 1);
		
		spriteBatch = new SpriteBatch(100);
		spriteBatch.setProjectionMatrix(projection);
	}

	/**
	 * Change between screens e.g. TitleScreen and InGameScreen
	 * 
	 * @param screen
	 */
	protected void setScreen(Screen screen) {
		appListener.setScreen(screen);
	}
	
	/**
	 * Draw a TextureRegion onto the screen
	 * 
	 * @param region
	 * @param x
	 * @param y
	 */
	public void draw(TextureRegion region, int x, int y) {
		int width = region.getRegionWidth();
		if(width < 0) width *= -1;
		spriteBatch.draw(region, x, y, width, region.getRegionHeight());
	}
	
	/**
	 * Draw a string onto the screen. Commented out until we get a good font file
	 * 
	 * @param string
	 * @param x
	 * @param y
	 */
	public void drawString(String string, int x, int y) {
		string = string.toUpperCase();
		for(int i = 0; i < string.length(); i ++) {
			char ch = string.charAt(i);
			for(int ys = 0; ys < chars.length; ys ++) {
				int xs = chars[ys].indexOf(ch);
				if(xs >= 0) {
					//draw(Art.guys[xs][ys+9], x + i * 6, y);
				}
			}
		}
	}
	
	public abstract void render();
	
	public abstract void tick(Input input);
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
}
