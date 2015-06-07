package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Spaceship extends Entity {

	private float speed;
	private float lifeMax;

	private double lastShoot;
	private int bulletSpeed, reloadTime;
	private int fireMode;

	public Spaceship(int x, int y) {
		super(x, y, 5, Sprite.get("spaceship.png", 150, 150));

		speed = 25;

		lifeMax = 5;

		bulletSpeed = 50;
		lastShoot = 0;
		reloadTime = 200;
		fireMode = 1;
	}

	public void heal(int i) {
		if(life < lifeMax) {
			life += i;
		}
	}

	public void setFireMode(int fireMode) {
		this.fireMode = fireMode;
	}

	public int getFireMode() {
		return fireMode;
	}

	public float getMaximumLife() {
		return lifeMax;
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Enemy) {
			this.die();
		}

		else if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.ENEMY) {
				this.takeDamage(1);
				Frame.getGame().delEntity(en);
			}
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= reloadTime) {
			switch(fireMode) {
				case 1:
					Frame.getGame().addEntity(new Bullet(x + img.getIconWidth()/2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 2:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 3:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()/2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 4:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()/3, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()-(img.getIconWidth()/3), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
			}

			Sound.play("spaceship_shoot.wav", 0.1);
			lastShoot = System.currentTimeMillis();
		}
	}

	@Override
	public void die() {
		Frame.getGame().gameOver();
	}

	public void moveLeft(double delta) {
		if(x >= 0) {
			x -= (speed * delta) / 30;
		}
	}

	public void moveRight(double delta) {
		if(x <= Frame.getWidth() - img.getIconWidth()) {
			x += (speed * delta) / 30;
		}
	}

	public void moveForward(double delta) {
		if(y >= 0) {
			y -= (speed * delta) / 30;
		}
	}

	public void moveBackward(double delta) {
		if(y <= Frame.getHeight() - img.getIconHeight()) {
			y += (speed * delta) / 30;
		}
	}
}
