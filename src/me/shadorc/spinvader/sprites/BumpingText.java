package me.shadorc.spinvader.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import me.shadorc.spinvader.graphic.Text;

public class BumpingText extends Effect {

	private String text;
	private Font font;
	private Color color;
	private float defaultSize, size;
	private long elapsed;

	public BumpingText(float x, float y, float duration, String text, Font font, Color color, float size) {
		super(x, y, duration);
		this.text = text;
		this.color = color;
		this.font = font;
		this.defaultSize = size;
	}

	@Override
	public void update(double delta) {
		this.elapsed = System.currentTimeMillis()-start;
		if(elapsed < duration) {
			int alpha = (int) (255-elapsed*255/duration);
			this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
			this.size -= delta/20;
			this.font = this.font.deriveFont(size);
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		if(elapsed < duration) {
			g2d.setFont(font);
			g2d.setColor(color);
			g2d.drawString(text, x-Text.getWidth(g2d, text)-10, y+Text.getHeight(g2d, text));
		}
	}

	public void setText(String text) {
		this.text = text;
		this.duration = 1000;
		this.size = defaultSize;
		this.start = System.currentTimeMillis();
	}
}