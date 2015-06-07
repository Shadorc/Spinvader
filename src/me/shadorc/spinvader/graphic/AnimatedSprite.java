package me.shadorc.spinvader.graphic;

import java.awt.Image;

import javax.swing.ImageIcon;

public class AnimatedSprite {

	private float x, y;
	private float duration;
	private ImageIcon img;
	private double start;

	public AnimatedSprite(float x, float y, ImageIcon img, float duration) {
		this.x = x;
		this.y = y;
		this.img = img;
		this.duration = duration;
		this.start = System.currentTimeMillis();
	}

	public void update() {
		if(System.currentTimeMillis() - start >= duration) {
			Frame.getGame().delSprite(this);
		}
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public Image getImage() {
		return img.getImage();
	}
}
