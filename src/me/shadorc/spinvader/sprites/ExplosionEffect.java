package me.shadorc.spinvader.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

import me.shadorc.spinvader.graphic.Frame;

public class ExplosionEffect extends Effect {

	private float elapsed;
	private float maxRadius;

	public ExplosionEffect(float x, float y, float duration, float maxRadius) {
		super(x, y, duration);
		this.maxRadius = maxRadius;
	}

	@Override
	public void update() {
		this.elapsed = (float) (System.currentTimeMillis() - start);
		if(elapsed >= duration) {
			Frame.getGame().delEffect(this);
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		Color backupColor = g2d.getColor();
		g2d.setColor(new Color(1, 0.5f, 0, 1-Math.min(1, elapsed/duration)));
		int radius = (int) (elapsed*maxRadius/duration);
		g2d.fillOval((int) x-radius/2, (int) y-radius/2, radius, radius);
		g2d.setColor(backupColor);
	}
}