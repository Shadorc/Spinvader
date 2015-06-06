package me.shadorc.spinvader.entity;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Spaceship implements Entity {

	private float x, y;
	private float speed;
	private float lifeMax, life;

	private double lastShoot;
	private int shootSpeed, shootTime;

	private ImageIcon img;

	private int fireMode;

	public Spaceship(int x, int y) {
		this.x = x;
		this.y = y;

		speed = 25;
		lifeMax = 5;
		life = lifeMax;

		lastShoot = 0;
		shootSpeed = 50;
		shootTime = 200;

		fireMode = 1;

		img = Sprite.get("spaceship.png", 150, 150);
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
	public Image getImage() {
		return img.getImage();
	}

	@Override
	public Rectangle getHitbox() {
		return new Rectangle((int) x, (int) y, img.getIconWidth(), img.getIconHeight());
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Enemy) {
			Frame.getGame().gameOver();
		}

		else if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.ENEMY) {
				life--;
				Frame.getGame().delEntity(en);
				if(life <= 0) {
					Frame.getGame().gameOver();
				}
			}
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis() - lastShoot) >= shootTime) {
			switch(fireMode) {
				case 1:
					Frame.getGame().addEntity(new Bullet(x + img.getIconWidth()/2, y, Direction.UP, shootSpeed, Type.SPACESHIP));
					break;
				case 2:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					break;
				case 3:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()/2, y, Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					break;
				case 4:
					Frame.getGame().addEntity(new Bullet(x, y+img.getIconHeight(), Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()/3, y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth()-(img.getIconWidth()/3), y+img.getIconHeight()/2, Direction.UP, shootSpeed, Type.SPACESHIP));
					Frame.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight(), Direction.UP, shootSpeed, Type.SPACESHIP));
					break;
			}

			Sound.play("spaceship_shoot.wav", 0.1);
			lastShoot = System.currentTimeMillis();
		}
	}

	@Override
	public void move(double delta) { }

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
