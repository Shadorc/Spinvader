package me.shadorc.spinvader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KListener implements KeyListener {

	private ArrayList <Integer> keys;
	private ArrayList <Integer> keysPressed;

	public KListener() {
		keys = new ArrayList <Integer>();
		keysPressed = new ArrayList <Integer> ();
	}

	public ArrayList <Integer> getKeysPressed() {
		return keys;
	}
	
	public boolean wasKeyPressed(int key) {
		if(keysPressed.contains(key)) {
			keysPressed.remove(keysPressed.indexOf(key));
			return true;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		if(!keys.contains(key)) {
			keys.add(key);
		}
		if(!keysPressed.contains(key)) {
			keysPressed.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		try {
			keys.remove(keys.indexOf(event.getKeyCode()));
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("Error while deleting key.");
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}
}