package me.shadorc.spinvader.graphic;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import me.shadorc.spinvader.utils.RandUtils;

public class Sprite {

	public static ImageIcon generateSprite() {

		Image img = Sprite.get("invadersSprite.png").getImage();

		// Create BufferedImage to get sub image.
		BufferedImage buImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = buImg.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		int width = 50;
		int height = 40;
		int column = RandUtils.randInt(7);
		int row = RandUtils.randInt(16);

		buImg = buImg.getSubimage((column * width + 15 * column), (row * height + 10 * row), 55, 40);

		return new ImageIcon(buImg);
	}

	public static ImageIcon resize(ImageIcon img, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resizedImg.createGraphics();

		g2d.setComposite(AlphaComposite.Src);
		g2d.drawImage(img.getImage(), 0, 0, width, height, null);
		g2d.dispose();

		return new ImageIcon(resizedImg);
	}

	public static ImageIcon get(String name) {
		return new ImageIcon(Sprite.class.getResource("/img/" + name));
	}

	public static ImageIcon get(String name, int width, int height) {
		return Sprite.resize(Sprite.get(name), (int) (width * Frame.getScaleX()), (int) (height * Frame.getScaleY()));
	}
}