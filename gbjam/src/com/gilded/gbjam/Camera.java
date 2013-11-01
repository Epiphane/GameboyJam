package com.gilded.gbjam;

public class Camera {
	public int x, y, width, height;

	public Camera (int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		if(x < 0) this.x = 0;
		if(y < 0) this.y = 0;
		if(x + GBJam.GAME_WIDTH > width) this.x = width - GBJam.GAME_WIDTH;
		if(y + GBJam.GAME_HEIGHT > height) this.y = height - GBJam.GAME_HEIGHT;
	}
}