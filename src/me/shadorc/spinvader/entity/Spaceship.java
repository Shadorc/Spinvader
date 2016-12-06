package me.shadorc.spinvader.entity;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Spaceship extends Entity {

	private static ImageIcon SPRITE = Sprite.get("spaceship.png", 150, 150);

	private float speedX, speedY;
	private float lifeMax;

	private double lastShoot;
	private float bulletSpeed, reloadTime;

	private boolean explosiveAmmo;
	private int fireMode;

	public Spaceship(int x, int y) {
		super(x, y, 5, SPRITE);

		this.speedX = 1.5f * Frame.getScaleX();
		this.speedY = 1.5f * Frame.getScaleY();
		this.lifeMax = this.getLife();

		this.bulletSpeed = 1.7f * Frame.getScaleY();
		this.reloadTime = 150;
		this.lastShoot = 0;

		this.fireMode = 1;
		this.explosiveAmmo = false;
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Enemy) {
			this.die();
		}

		else if(en instanceof Bullet) {
			if(((Bullet) en).getType() == Type.ENEMY) {
				this.takeDamage(1);
				Main.getGame().delEntity(en);
			}
		}
	}

	@Override
	public void shoot() {
		if((System.currentTimeMillis()-lastShoot) >= reloadTime) {
			switch(fireMode) {
				case 1:
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth()/2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 2:
					Main.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 3:
					Main.getGame().addEntity(new Bullet(x, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth()/2, y, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
				case 4:
					Main.getGame().addEntity(new Bullet(x, y+img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth()/3, y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth()-(img.getIconWidth()/3), y+img.getIconHeight()/2, bulletSpeed, Direction.UP, Type.SPACESHIP));
					Main.getGame().addEntity(new Bullet(x+img.getIconWidth(), y+img.getIconHeight(), bulletSpeed, Direction.UP, Type.SPACESHIP));
					break;
			}

			Sound.play("spaceship_shoot.wav", 0.10, Data.SOUND_VOLUME);
			this.lastShoot = System.currentTimeMillis();
		}
	}

	@Override
	public void die() {
		Main.getGame().gameOver();
	}

	public void moveLeft(double delta) {
		if(this.x >= 0) {
			this.x -= this.speedX*delta;
		}
	}

	public void moveRight(double delta) {
		if(this.x <= (Main.getFrame().getWidth() - this.img.getIconWidth())) {
			this.x += this.speedX*delta;
		}
	}

	public void moveForward(double delta) {
		if(this.y >= 0) {
			this.y -= this.speedY*delta;
		}
	}

	public void moveBackward(double delta) {
		if(this.y <= (Main.getFrame().getHeight() - this.img.getIconHeight())) {
			this.y += this.speedY*delta;
		}
	}

	public void heal(int i) {
		if(this.life < this.lifeMax) {
			this.life += i;
		}
		this.life = Math.min(this.lifeMax, this.lifeMax);
	}

	public void setFireMode(int fireMode) {
		this.fireMode = fireMode;
	}

	public void activeBomb() {
		this.explosiveAmmo = true;
	}

	public int getFireMode() {
		return this.fireMode;
	}

	public float getMaximumLife() {
		return this.lifeMax;
	}

	public boolean hasExplosiveAmmo() {
		return this.explosiveAmmo;
	}
}
