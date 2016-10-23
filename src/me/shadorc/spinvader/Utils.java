package me.shadorc.spinvader;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Random;

public class Utils {

	private static Random rand = new Random();
	private static GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static DisplayMode displayMode = screen.getDisplayMode();

	public static int rand(int i) {
		return rand.nextInt(i);
	}

	public static GraphicsDevice getScreen() {
		return screen;
	}

	public static DisplayMode getDisplayMode() {
		return displayMode;
	}
}
