package me.shadorc.spinvader.sprites;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

public class AnimatedSprite extends Effect {

	private final ImageIcon img;

	public AnimatedSprite(float x, float y, ImageIcon img, int duration) {
		super(x, y, duration);
		this.img = img;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(img.getImage(), (int) x, (int) y, null);
	}
}
