package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
import me.shadorc.spinvader.graphic.Sprite;

public class Boss implements Entity {

	private float x, y;
	private float speed, shootSpeed, shootTime;
	private double lastShoot;
	private float life;

	private ImageIcon img;
	private Direction dir;

	public Boss(float x, float y) {
		this.x = x;
		this.y = y;

		dir = Direction.RIGHT;
		img = Sprite.get("boss.png", 335, 170);

		speed = 15;
		shootSpeed = 20;
		lastShoot = System.currentTimeMillis();
		shootTime = Game.rand(1000)+500;
		life = 50;
	}

	@Override
	public int getX() {
		return (int) x;
	}

	@Override
	public int getY() {
		return (int) y;
	}

	@Override
	public float getLife() {
		return life;
	}

	@Override
	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	@Override
	public Image getImage() {
		return img.getImage();
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.SPACESHIP) {
				life--;
				Game.delEntity(en);

				if(this.life <= 0) {
					Sound.play("AlienDestroyed.wav", 0.15);
					Game.delEntity(this);
					Game.incScore(300);
					if(Game.rand(50) == 0) {
						Game.addEntity(new Item(x, y, Bonus.LIFE));
					}
				}
			}
		}
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
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
			Game.addEntity(new Bullet((x + img.getIconWidth()/3), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY));
			Game.addEntity(new Bullet((img.getIconWidth() - img.getIconWidth()/3), (y + img.getIconHeight()), Direction.DOWN, shootSpeed, Type.ENEMY));

			lastShoot = System.currentTimeMillis();
			shootTime = Game.rand(1000)+500;
		}
	}
}
