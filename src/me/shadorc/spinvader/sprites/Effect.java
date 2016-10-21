package me.shadorc.spinvader.sprites;

import java.awt.Graphics2D;

import me.shadorc.spinvader.graphic.Frame;

public class Effect {

	protected float x, y;
	protected float duration;
	protected double start;

	public Effect(float x, float y, float duration) {
		this.x = x;
		this.y = y;
		this.duration = duration;
		this.start = System.currentTimeMillis();
	}

	public void update() {
		if(System.currentTimeMillis() - start >= duration) {
			Frame.getGame().delEffect(this);
		}
	}

	public void render(Graphics2D g2d) { }

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

}
