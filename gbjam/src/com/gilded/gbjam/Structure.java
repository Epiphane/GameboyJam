package com.gilded.gbjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Structure extends Collideascope {
	public static class StructureAndMap {
		public TextureRegion structure;
		public byte[][] map;
	}
	public static StructureAndMap airplane, rock, palm, palm_empty;
	
	public static final byte BLOCKER = -1;
	public static final byte DRAWOVER = 1;
	
	public boolean blocker, doActionOnCollision;
	public boolean removed;
	
	protected Level level;
	
	protected TextureRegion display;
	
	protected ActionHandler actionHandler;
	
	public Structure(StructureAndMap structure, int x, int y, Level level) {
		this(structure, x, y, new ActionHandler(), level);
	}
	
	public Structure(StructureAndMap structure, int x, int y, ActionHandler actionHandler, Level level) {
		super(structure.map);
		this.display = structure.structure;
		this.w = structure.map.length;
		this.h = structure.map[0].length;
		this.x = x * GBJam.TILESIZE;
		this.y = y * GBJam.TILESIZE - (h - GBJam.TILESIZE);
		
		this.level = level;
		
		this.xSlot = x;
		this.ySlot = y;
		
		blocker = true;
				
		this.actionHandler = actionHandler;
	}
	
	public void init(Level level) {	
		this.level = level;
	}
	
	public void tick(Input input) {
	}
	
	public void render(Screen screen, Camera camera) {
		screen.draw(display, x, y);
	}
	
	public boolean inTheWay(int x, int y, byte[][] map) {
		if(!blocker) return false;
		return super.inTheWay(x, y, map);
	}
	
	/**
	 * Returns false, because we don't want to remove me
	 * @param entity
	 * @return
	 */
	public boolean doAction(Entity entity) {
		return actionHandler.doAction(entity, this);
	}
	
	public boolean doPlayerAction(Player player) {
		return actionHandler.doPlayerAction(player, this);
	}
	
	public static Structure Rock(int x, int y, Level level) {
		return new Structure(rock, x, y, level);
	}
	
	public static Structure Airplane(int x, int y, Level level) {
		return new Structure(airplane, x, y, level);
	}
	
	public static Structure PalmTree(int x, int y, Level level) {
		return new Structure(palm, x, y, new PalmActionHandler(), level);
	}
	
	private static class ActionHandler {
		public boolean doAction(Entity entity, Structure parent) {
			return false;
		}
		public boolean doPlayerAction(Player player, Structure parent) {
			System.out.println(this+ ": Ouch!");
			Collideascope.printMap(parent.collisionMap);
			return false;
		}
	}
	
	private static class PalmActionHandler extends ActionHandler {
		public boolean doAction(Entity entity, Structure parent) {
			return false;
		}
		public boolean doPlayerAction(Player player, Structure parent) {
			parent.level.add(new Coconut(parent.x + GBJam.TILESIZE, parent.y + 5));
			parent.display = palm_empty.structure;
			parent.actionHandler = new ActionHandler();
			return false;
		}
	}
}
