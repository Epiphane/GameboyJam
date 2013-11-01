package com.gilded.gbjam;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class Input implements InputProcessor {
	public class InputStack {
		private class Node {
			int button;
			Node next;
			public Node(int button) {
				this.button = button;
			}
		}
		
		private Node currentButton;
		
		public void push(int button) {
			Node next = currentButton;
			currentButton = new Node(button);
			currentButton.next = next;
		}
		
		public int pull(int button) {
			if(currentButton == null) return -1;
			
			if(currentButton.button == button) {
				currentButton = currentButton.next;
				return button;
			}
			
			Node cursor = currentButton;
			while(cursor.next != null) {
				if(cursor.next.button == button) {
					cursor.next = cursor.next.next;
					return button;
				}
				cursor = cursor.next;
			}
			return -1;
		}
		
		public int peek() {
			return (currentButton == null) ? -1 : currentButton.button;
		}
	}
	
	// Static key values
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	// Button arrays
	public boolean[] buttons = new boolean[32];
	public boolean[] oldButtons = new boolean[32];
	
	public InputStack buttonStack;

	public Input() {
		super();
		buttonStack = new InputStack();
	}
	
	/**
	 * Sets button in the array to state of the key
	 * 
	 * @param key
	 * @param down
	 */
	public void set(int key, boolean down) {
		//buttons = new boolean[32];
		// Defaults to nothing pressed in case it's not a recognized keystroke
		int button = -1;
		
		// Go through key possibilities for recognized input
		if (key == Keys.DPAD_UP) button = UP;
		if (key == Keys.DPAD_DOWN) button = DOWN;
		if (key == Keys.DPAD_LEFT) button = LEFT;
		if (key == Keys.DPAD_RIGHT) button = RIGHT;
		
		// If it's recognized, set the state in the array
		if(button >= 0) {
			if(down) {
				buttonStack.push(button);
			}
			else {
				buttonStack.pull(button);
			}
		}
	}
	
	/**
	 * Set the button inputs to the past
	 */
	public void tick() {
		for (int i = 0; i < buttons.length; i ++)
			oldButtons[i] = buttons[i];
	}
	
	public void releaseAllKeys() {
		buttons = new boolean[32];
	}
	
	public boolean keyDown(int keycode) {
		set(keycode, true);
		return false;
	}

	public boolean keyUp(int keycode) {
		set(keycode, false);
		return false;
	}
	
	/**
	 * Not useful right now but I gotta implement it
	 */
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
