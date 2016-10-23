package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.Utils;
import me.shadorc.spinvader.graphic.Sprite;

public class Item extends Entity {

	private float speed;
	private int fireMode;
	private Bonus type;

	Item(float x, float y, Bonus type) {
		super(x, y, 0, null);

		this.type = type;
		this.speed = 5;

		String spriteName = null;
		switch (type) {
			case EXPLOSIVE:
				spriteName = "explosion.png";
				break;

			case FIREMODE:
				this.fireMode = Main.getGame().getSpaceship().getFireMode()+1;
				spriteName = "firemode_" + fireMode + ".png";
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
			}
		}
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Main.getFrame().getHeight()) {
			Main.getGame().delEntity(this);
		}
	}

	public static void generate(float x, float y) {
		switch (Utils.rand(100)) {
			case 0:
				Main.getGame().addEntity(new Item(x, y, Bonus.LIFE));
				break;
			case 1:
				Main.getGame().addEntity(new Item(x, y, Bonus.EXPLOSIVE));
				break;
			case 2:
				if(Main.getGame().getSpaceship().getFireMode() < 4)
					Main.getGame().addEntity(new Item(x, y, Bonus.FIREMODE));
				break;
		}
	}
}
