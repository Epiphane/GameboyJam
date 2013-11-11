package com.gilded.gbjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	public static Sound playerHit;
	public static Sound enemyHit;
	public static Sound pickup;
	
	public static void loadSounds() {
		playerHit = Gdx.audio.newSound(Gdx.files.internal("sfx/ouch.wav"));
		enemyHit = Gdx.audio.newSound(Gdx.files.internal("sfx/hit.wav"));
		pickup = Gdx.audio.newSound(Gdx.files.internal("sfx/pickup.wav"));
	}
}
