package me.shadorc.spinvader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KListener implements KeyListener {

	private ArrayList <Integer> keys;

	public KListener() {
		this.keys = new ArrayList <Integer>();
	}

	public ArrayList <Integer> getKeysPressed() {
		return keys;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		if(!keys.contains(key)) {
			keys.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keys.remove(keys.indexOf(event.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}
}