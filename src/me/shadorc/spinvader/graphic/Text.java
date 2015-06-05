package me.shadorc.spinvader.graphic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

public class Text {

	public static int getTextCenterWidth(Graphics g, String text) {
		double textWidth = g.getFontMetrics().getStringBounds(text, g).getWidth();
		return (int) (Frame.getWidth()/2 - textWidth/2);
	}

	public static int getWidth(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
	}

	public static int getHeight(Graphics g, String text) {
		return (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
	}

	public static Font getFont(String name, int size) {
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Frame.class.getResourceAsStream("/res/" + name)).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			font = new Font("Consolas", Font.PLAIN, size);
		}
		return font;
	}
}