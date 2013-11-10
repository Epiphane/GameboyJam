package com.gilded.gbjam;

/** Extended by both Structure and Entity
 * 
 * @author Thomas
 *
 */
public class Collideascope {
	protected byte[][] collisionMap;
	public int x, y;
	public int xSlot, ySlot;
	public int w, h;
		
	public Collideascope(byte[][] map) {
		collisionMap = map;
	}
	
	public boolean inTheWay(int x, int y, byte[][] map) {
		//System.out.println(x+", "+y+" ["+map.length+","+map[0].length+"]");
		//System.out.println(this.x+", "+this.y+" ["+this.w+","+this.h+"]");
		
		x -= this.x;
		y -= this.y;
		
		// If it's not in bounds then derp
		if(x + map.length < 0 || y + map[0].length < 0 || x > this.collisionMap.length || y > this.collisionMap[0].length) return false;
		
		return collide(x, y, map);
	}
	
	/**
	 * Check if something is colliding with me
	 * 
	 * @param x - x value relative to me
	 * @param y - y value relative to me
	 * @param w - width of object
	 * @param h - height of object
	 * @return
	 */
	public boolean collide(int x, int y, byte[][] map) {
		for(int i = Math.max(x, 0); i < Math.min(x + map.length, collisionMap.length); i ++) {
			for(int j = Math.max(y, 0); j < Math.min(y + map[0].length, collisionMap[0].length); j ++) {
				if((map[i - x][j - y] & collisionMap[i][j]) != 0) // Collision! {
					return true;
				
			}
		}
		
		return false;
	}
	
	public static void printMap(byte[][] map) {
		for(int j = 0; j < map[0].length; j ++) {
			for(int i = 0; i < map.length; i ++) {
				System.out.print(map[i][j]*-1+" ");
			}
			System.out.println();
		}
	}
}
