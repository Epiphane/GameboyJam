package com.gilded.gbjam;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "GB Jam";
		cfg.useGL20 = false;
		
		// Set size of the window (Game size is 160x144, so anything that scales from that is good)
		cfg.width = 640;
		cfg.height = 576;
		
		new LwjglApplication(new GBJam(), cfg);
	}
}