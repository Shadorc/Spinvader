package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Bullet extends Entity {

	private static ImageIcon SPRITE = Sprite.get("bullet.png", 5, 22);

	private float speed;

	private Direction dir;
	private Type type;

	public Bullet(float x, float y, float speed, Direction dir, Type type) {
		super(x, y, 0, SPRITE);

		this.speed = speed * Frame.getScaleY();
		this.type = type;
		this.dir = dir;
	}

	@Override
	public void move(double delta) {
		y += (speed*delta)/30 * (dir == Direction.DOWN ? 1 : -1);

		if(y <= 0 || y >= Main.getFrame().getHeight()) {
			Main.getGame().delEntity(this);
		}
	}

	public Type getType() {
		return type;
	}
}