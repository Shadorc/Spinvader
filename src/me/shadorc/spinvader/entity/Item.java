package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.Utils;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Sprite;

public class Item extends Entity {

	private float accelY;
	private float speedX, speedY;
	private int fireMode;
	private Bonus type;

	Item(float x, float y, Bonus type) {
		super(x, y, 0, null);

		this.type = type;
		this.accelY = 0;
		this.speedX = 0;
		this.speedY = 0.3f * Frame.getScaleY();

		String spriteName = null;
		switch (type) {
			case FIREMODE:
				this.fireMode = Main.getGame().getSpaceship().getFireMode()+1;
				spriteName = "firemode_" + fireMode + ".png";
				break;

			case COIN:
				this.speedY = -this.speedY;
				this.speedX = (Utils.randFloat(0.3f)-0.15f)*Frame.getScaleX();
				this.accelY = 0.0015f;
				spriteName = "coin.png";
				break;

			case EXPLOSIVE:
				spriteName = "explosion.png";
				break;

			case LIFE:
				spriteName = "life.png";
				break;
		}
		this.img = Sprite.get(spriteName, 50, 50);
	}

	@Override
	public void collidedWith(Entity en) {
		if(en instanceof Spaceship) {
			Main.getGame().delEntity(this);
			switch (type) {
				case EXPLOSIVE:
					Main.getGame().getSpaceship().activeBomb();
					break;

				case FIREMODE:
					Main.getGame().getSpaceship().setFireMode(fireMode);
					break;

				case LIFE:
					Main.getGame().getSpaceship().heal(1);
					Sound.play("life.wav", 0.20, Data.SOUND_VOLUME);
					break;

				case COIN:
					Main.getGame().incMoney(1);
					break;
			}
		}
	}

	@Override
	public void move(double delta) {
		this.speedY += this.accelY*delta;
		this.x += this.speedX*delta;
		this.y += this.speedY*delta;

		if(this.y >= Main.getFrame().getHeight()) {
			Main.getGame().delEntity(this);
		}
	}

	public static void generate(float x, float y) {
		Main.getGame().addEntity(new Item(x, y, Bonus.COIN));
		switch (Utils.randInt(200)) {
			case 0: 
			case 1:
				Main.getGame().addEntity(new Item(x, y, Bonus.LIFE));
				break;
			case 2:
				if(!Main.getGame().getSpaceship().hasExplosiveAmmo())
					Main.getGame().addEntity(new Item(x, y, Bonus.EXPLOSIVE));
				break;
			case 3:
				if(Main.getGame().getSpaceship().getFireMode() < 4)
					Main.getGame().addEntity(new Item(x, y, Bonus.FIREMODE));
				break;
		}
	}
}
