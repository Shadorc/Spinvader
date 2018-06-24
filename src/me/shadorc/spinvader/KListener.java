package me.shadorc.spinvader;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KListener extends KeyAdapter {

	private final Map<Integer, Boolean> keysDown;
	private final Map<Integer, Boolean> keysPressed;

	public KListener() {
		super();
		this.keysDown = new HashMap<>();
		this.keysPressed = new HashMap<>();
	}

	public boolean isKeyDown(int key) {
		return keysDown.getOrDefault(key, false);
	}

	public boolean wasKeyPressed(int key) {
		return keysPressed.remove(key) != null;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		keysDown.putIfAbsent(event.getKeyCode(), true);
		keysPressed.putIfAbsent(event.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keysDown.remove(event.getKeyCode());
	}

}