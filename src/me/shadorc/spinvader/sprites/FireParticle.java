package me.shadorc.spinvader.sprites;

import java.awt.Color;

public class FireParticle {

	private final long start, duration;
	private float x, y;
	private final float x0, y0;
	private final float a;
	private float radius;
	private Color color;

	public FireParticle(float x, float y, float a, int radius, Color color, int duration) {
		this.x = x;
		this.x0 = x;
		this.y = y;
		this.y0 = y;
		this.a = a;
		this.radius = radius;
		this.color = color;
		this.duration = duration;
		this.start = System.currentTimeMillis();
	}

	public void update(double delta) {
		this.x += (20d * delta) / 30d;
		this.y = a * (x - x0) + y0;
		this.radius += (float) (System.currentTimeMillis() - this.start) / this.duration * 3f;
		float alpha = Math.max(0, 1f - (float) (System.currentTimeMillis() - this.start) / this.duration);
		this.color = new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getRadius() {
		return (int) radius;
	}

	public Color getColor() {
		return color;
	}

	public long getStart() {
		return start;
	}
}