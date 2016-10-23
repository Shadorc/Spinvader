package me.shadorc.spinvader;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Random;

public class Utils {

	private static Random rand = new Random();

	public static int rand(int i) {
		return rand.nextInt(i);
	}

	public static GraphicsDevice getScreen() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}
}
