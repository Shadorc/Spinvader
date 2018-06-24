package me.shadorc.spinvader.graphic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

import me.shadorc.spinvader.Main;

public class Text {

	public static int getCenterWidth(Graphics g, String text) {
		return (Main.getFrame().getWidth() - Text.getWidth(g, text)) / 2;
	}

	public static int getWidth(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
	}

	public static int getHeight(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
	}

	public static Font createFont(String name, int size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Text.class.getResourceAsStream("/res/" + name)).deriveFont(Font.PLAIN, size * Frame.getScaleX());
		} catch (FontFormatException | IOException e) {
			return new Font("Consolas", Font.PLAIN, (int) (size * Frame.getScaleX()));
		}
	}

	public static Font getFont(String name, int size) {
		return new Font(name, Font.BOLD, (int) (size * Frame.getScaleX()));
	}
}