package me.shadorc.spinvader.graphic;

import java.awt.Graphics;

public class Text {

	public static  int getTextCenterWidth(Graphics g, String text) {
		int textWidth = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
		return Frame.getWidth()/2 - textWidth/2;
	}
}