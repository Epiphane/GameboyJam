package com.gilded.gbjam;

public abstract class Enemy extends Entity {

	public static final int BOAR = 0;
	public static final int BAT  = 1;
	
	protected int frame;
	
	public Enemy(byte[][] collisionMap) {
		super(collisionMap);
	}
	
	public static Enemy makeEnemy(int x, int y, int type) {
		switch(type) {
		case BOAR:
			return new Boar(x, y);
		case BAT:
			return new Bat(x, y);
		default:
			System.out.println("Tried to generate enemy of wrong type: " + type);
			break;
		}
		
		System.out.println("How did we even get here??!?!");
		return null;
	}

}
