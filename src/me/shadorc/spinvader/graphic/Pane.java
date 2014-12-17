package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import me.shadorc.spinvader.Collision;
import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.element.Bullet;
import me.shadorc.spinvader.element.Enemy;
import me.shadorc.spinvader.element.SpaceShip;

public class Pane extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private SpaceShip spaceship;
	private ArrayList <Enemy> ennemies;
	private Random rand = new Random();

	private KListener listener;
	private ImageIcon background;
	private int score = 0;

	private Sound music;

	private boolean end = false;
	private boolean showHitbox = false;

	private double time = System.currentTimeMillis();
	private int fps = 0, frame = 0;
	private Timer update;

	Pane() {

		spaceship = new SpaceShip(Frame.getScreenWidth()/2, Frame.getScreenHeight()/2);
		ennemies = new ArrayList <Enemy> ();
		listener = new KListener();
		background = new ImageIcon(this.getClass().getResource("/img/background.png"));

		ennemies.addAll(Enemy.generate(76));

		music = new Sound("Savant - Amerika.wav", 1);
		music.start();

		this.addKeyListener(listener);
		this.setFocusable(true);

		//Hide cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);

		update = new Timer(5, this);
		update.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		for(Bullet bu : spaceship.getBullets()) {
			g2d.drawImage(bu.getImage(), (int) bu.getX(), (int) bu.getY(), null);
		}

		for(Bullet bu : Enemy.getBullets()) {
			g2d.drawImage(bu.getImage(), (int) bu.getX(), (int) bu.getY(), null);
		}

		for(Enemy en : ennemies) {
			g2d.drawImage(en.getImage(), (int) en.getX(), (int) en.getY(), null);

			if(showHitbox) {
				en.drawHitbox(g2d);
			}
		}

		g2d.drawImage(spaceship.getImage(), (int) spaceship.getX(), (int) spaceship.getY(), null);

		if(showHitbox) {
			spaceship.drawHitbox(g2d);
		}

		g2d.setFont(new Font("Consolas", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getScreenWidth() - 150, 30);
		g2d.drawString(fps + " FPS", 10, 30);
		g2d.drawString("Resolution: " + Frame.getScreenWidth() + "x" + Frame.getScreenHeight(), 10, 60);
		g2d.drawString("Bullets: " + spaceship.getBullets().size(), 10, 90);
		g2d.drawString("Ennemies: " + ennemies.size(), 10, 120);
		g2d.drawString("Show Hitbox: " + showHitbox, 10, 150);
		g2d.drawString("Alive: " + !end, 10, 180);
		g2d.drawString("Life: " + spaceship.getLife(), 10, 210);

		if(System.currentTimeMillis() - time >= 1000) {
			fps = frame;
			frame = 0;
			time = System.currentTimeMillis();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();	

		frame++;

		for(int key : listener.getKeysPressed()) {
			if(key == KeyEvent.VK_ESCAPE) System.exit(0);
			if(key == KeyEvent.VK_LEFT)	  spaceship.moveLeft();
			if(key == KeyEvent.VK_RIGHT)  spaceship.moveRight();
			if(key == KeyEvent.VK_UP)	  spaceship.moveForward();
			if(key == KeyEvent.VK_DOWN)	  spaceship.moveBackward();
			if(key == KeyEvent.VK_SPACE)  spaceship.shoot();
		}

		if(ennemies.size() == 0) {
			ennemies.addAll(Enemy.generate(76));

		} else if(rand.nextInt(10) == 0) {
			ennemies.get(rand.nextInt(ennemies.size())).shoot();
		}

		for(int i = 0; i < spaceship.getBullets().size(); i++) {
			Bullet bu = spaceship.getBullets().get(i);

			bu.move();

			if(!bu.isAlive())
				spaceship.getBullets().remove(bu);
		}

		for(int i = 0; i < Enemy.getBullets().size(); i++) {
			Bullet bu = Enemy.getBullets().get(i);

			bu.move();

			if(!bu.isAlive())
				Enemy.getBullets().remove(bu);
		}

		for(Enemy en : ennemies) {
			en.move();
		}

		for(int i = 0; i < spaceship.getBullets().size(); i++) {
			Bullet bu = spaceship.getBullets().get(i);

			for(int j = 0; j < ennemies.size(); j++) {

				Enemy en = ennemies.get(j);

				if(Collision.test(bu, en)) {

					en.hit();
					spaceship.getBullets().remove(bu);

					if(en.getLife() == 0) {
						ennemies.remove(ennemies.get(j));
						score++;
					}
				}
			}
		}

		for(int i = 0; i < Enemy.getBullets().size(); i++) {

			Bullet bu = Enemy.getBullets().get(i);

			if(Collision.test(bu, spaceship)) {

				spaceship.hit();
				Enemy.getBullets().remove(bu);

				if(spaceship.getLife() == 0) {
					end = true;
					//					music.stop();
					//					update.stop();
					//					Frame.setPanel(new Menu());
				}
			}
		}
	}
}
