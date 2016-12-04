package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.Utils;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;
import me.shadorc.spinvader.sprites.AnimatedSprite;

public class Boss extends Entity {

	private static ImageIcon SPRITE = Sprite.get("boss.png", 335, 170);

	private float speedX;

	private float bulletSpeedY, reloadTime;
	private double lastShoot;

	private Direction dir;

	public Boss(float x, float y) {
		super(x, y, 50, SPRITE);

		this.bulletSpeedY = 0.7f * Frame.getScaleY();
		this.reloadTime = this.generateShootTime();
		this.lastShoot = System.currentTimeMillis();

		this.dir = Direction.RIGHT;
		this.speedX = 0.5f * Frame.getScaleX();
	}

	@Override
	public void move(double delta) {
		x += speedX*delta * (dir == Direction.RIGHT ? 1 : -1);

		if(dir == Direction.RIGHT && x >= Main.getFrame().getWidth() - img.getIconWidth()) {
			x = (float) (Main.getFrame().getWidth() - img.getIconWidth());
			dir = Direction.LEFT;

		} else if(dir == Direction.LEFT && x <= 0) {
			x = 0;
			dir = Direction.RIGHT;
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= reloadTime) {
			Main.getGame().addEntity(new Bullet((x + img.getIconWidth()/3), (y + img.getIconHeight()), bulletSpeedY, Direction.DOWN, Type.ENEMY));
			Main.getGame().addEntity(new Bullet((x + img.getIconWidth() - img.getIconWidth()/3), (y + img.getIconHeight()), bulletSpeedY, Direction.DOWN, Type.ENEMY));

			lastShoot = System.currentTimeMillis();
			reloadTime = this.generateShootTime();
		}
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				this.takeDamage(1);
				Main.getGame().delEntity(en);
			}
		}
	}

	@Override
	public void die() {
		Sound.play("AlienDestroyed.wav", 0.10, Data.SOUND_VOLUME);
		Main.getGame().addEffect(new AnimatedSprite(x, y, Sprite.get("explosion.png", img.getIconWidth(), img.getIconHeight()), 100));
		Main.getGame().delEntity(this);
		Main.getGame().incScore(300);
		Item.generate(x, y);
	}

	private int generateShootTime() {
		return Utils.randInt(1000)+500;
	}
}