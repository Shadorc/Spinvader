package me.shadorc.spinvader;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Random;

public class Utils {

	private static Random rand = new Random();
	private static GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static DisplayMode displayMode = screen.getDisplayMode();
	private static HashMap <String, DisplayMode> resolutionMap = Utils.generateRes();

	private static HashMap <String, DisplayMode> generateRes() {
		HashMap <String, DisplayMode> resolutionMap = new HashMap <> ();
		for(DisplayMode dm : screen.getDisplayModes()) {
			String size = dm.getWidth() + "x" + dm.getHeight();
			if(!resolutionMap.containsKey(size)) {
				resolutionMap.put(size, dm);
			}
		}
		return resolutionMap;
	}

	public static int rand(int i) {
		return rand.nextInt(i);
	}

	public static GraphicsDevice getScreen() {
		return screen;
	}

	public static DisplayMode getDisplayMode() {
		return displayMode;
	}

	public static HashMap <String, DisplayMode> getResolutions() {
		return resolutionMap;
	}
}