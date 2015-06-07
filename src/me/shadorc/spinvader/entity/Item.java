package me.shadorc.spinvader.entity;

import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.graphic.Frame;
import me.shadorc.spinvader.graphic.Game;
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
				this.fireMode = Frame.getGame().getSpaceship().getFireMode()+1;
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
			Frame.getGame().delEntity(this);
			switch (type) {
				case EXPLOSIVE:
					Frame.getGame().getSpaceship().activeBomb();
					break;

				case FIREMODE:
					Frame.getGame().getSpaceship().setFireMode(fireMode);
					break;

				case LIFE:
					Frame.getGame().getSpaceship().heal(1);
					Sound.play("life.wav", 0.20);
					break;
			}
		}
	}

	@Override
	public void move(double delta) {
		y += (float) ((speed * delta) / 30);

		if(y >= Frame.getHeight()) {
			Frame.getGame().delEntity(this);
		}
	}

	public static void generate(float x, float y) {
		switch (Game.rand(100)) {
			case 0:
				Frame.getGame().addEntity(new Item(x, y, Bonus.LIFE));
				break;
			case 1:
				Frame.getGame().addEntity(new Item(x, y, Bonus.EXPLOSIVE));
				break;
			case 2:
				if(Frame.getGame().getSpaceship().getFireMode() < 4)
					Frame.getGame().addEntity(new Item(x, y, Bonus.FIREMODE));
				break;
		}
	}
}
