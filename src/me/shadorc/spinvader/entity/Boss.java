package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Boss extends Entity {

	private float speed;

	private float bulletSpeed, reloadTime;
	private double lastShoot;

	private Direction dir;

	public Boss(float x, float y) {
		super(x, y, 50, Sprite.get("boss.png", 335, 170));

		this.reloadTime = this.generateShootTime();
		this.bulletSpeed = 20;
		this.lastShoot = System.currentTimeMillis();

		this.dir = Direction.RIGHT;
		this.speed = 15 * Frame.getScaleX();
	}

	@Override
	public void move(double delta) {
		x += (float) ((speed * delta) / 30) * (dir == Direction.RIGHT ? 1 : -1);

		if(dir == Direction.RIGHT && x >= Frame.getWidth() - img.getIconWidth()) {
			x = (float) (Frame.getWidth() - img.getIconWidth());
			dir = Direction.LEFT;

		} else if(dir == Direction.LEFT && x <= 0) {
			x = 0;
			dir = Direction.RIGHT;
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= reloadTime) {
			Frame.getGame().addEntity(new Bullet((x + img.getIconWidth()/3), (y + img.getIconHeight()), bulletSpeed, Direction.DOWN, Type.ENEMY));
			Frame.getGame().addEntity(new Bullet((img.getIconWidth() - img.getIconWidth()/3), (y + img.getIconHeight()), bulletSpeed, Direction.DOWN, Type.ENEMY));

			lastShoot = System.currentTimeMillis();
			reloadTime = this.generateShootTime();
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				this.takeDamage(1);
				Frame.getGame().delEntity(en);
			}
		}
	}

	@Override
	public void die() {
		Sound.play("AlienDestroyed.wav", 0.10);
		Frame.getGame().addSprite(x, y, Sprite.get("explosion.png", img.getIconWidth(), img.getIconHeight()), 100);
		Frame.getGame().delEntity(this);
		Frame.getGame().incScore(300);
		Item.generate(x, y);
	}

	private int generateShootTime() {
		return Game.rand(1000)+500;
	}
}