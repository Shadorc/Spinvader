package me.shadorc.spinvader.graphic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

public class Text {

	public static int getCenterWidth(Graphics g, String text) {
		return (int) (Frame.getWidth()-Text.getWidth(g, text))/2;
	}

	public static int getWidth(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
	}

	public static int getHeight(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
	}

	public static Font createFont(String name, int size) {
		size *= Frame.getScaleX();
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Text.class.getResourceAsStream("/res/" + name)).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			return new Font("Consolas", Font.PLAIN, size);
		}
	}

	public static Font getFont(String name, int size) {
		return new Font(name, Font.BOLD, (int) (size*Frame.getScaleX()));
	}
}