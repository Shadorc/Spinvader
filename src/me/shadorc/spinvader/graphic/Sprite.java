package me.shadorc.spinvader.graphic;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Sprite {

	public static ImageIcon generateSprite() {

		Image img = Sprite.get("invadersSprite.png").getImage();

		//Create BufferedImage to get sub image.
		BufferedImage bu_img = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bu_img.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		int width = 50;
		int height = 40;
		int column = Game.rand(7);
		int row = Game.rand(16);

		bu_img = bu_img.getSubimage((column*width + 15*column), (row*height + 10*row), 55, 40);

		return new ImageIcon(bu_img);
	}

	public static ImageIcon resize(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}

	public static ImageIcon get(String name) {
		return new ImageIcon(Sprite.class.getResource("/img/" + name));
	}

	public static ImageIcon get(String name, int width, int height) {
		return new ImageIcon(Sprite.get(name).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
	}
}