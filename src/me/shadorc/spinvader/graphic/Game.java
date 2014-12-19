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
import me.shadorc.spinvader.entity.BulletEntity;
import me.shadorc.spinvader.entity.EnemyEntity;
import me.shadorc.spinvader.entity.SpaceshipEntity;
import me.shadorc.spinvader.entity.Entity.Direction;

public class Game extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private SpaceshipEntity spaceship;
	private ArrayList <EnemyEntity> ennemies;
	private Random rand = new Random();

	private ImageIcon background;
	private KListener listener;
	private int score = 0;

	private static Sound music;

	private boolean showHitbox = false;
	private boolean generating = false;

	private double time = System.currentTimeMillis();
	private int fps = 0, frame = 0;
	private static Timer update;

	Game() {

		spaceship = new SpaceshipEntity(Frame.getScreenWidth()/2, Frame.getScreenHeight()/2);
		ennemies = EnemyEntity.generate(36);

		background = new ImageIcon(this.getClass().getResource("/img/background.png"));
		listener = new KListener();

		music = new Sound("Savant - Amerika.wav", 1);
		//		music.start();

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

		for(BulletEntity bu : spaceship.getBullets()) {
			g2d.drawImage(bu.getImage(), (int) bu.getX(), (int) bu.getY(), null);
		}

		for(BulletEntity bu : EnemyEntity.getBullets()) {
			g2d.drawImage(bu.getImage(), (int) bu.getX(), (int) bu.getY(), null);
		}

		for(EnemyEntity en : ennemies) {
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
		g2d.drawString("Ennemies: " + ennemies.size(), 10, 90);
		g2d.drawString("Show Hitbox: " + showHitbox, 10, 120);
		g2d.drawString("Life: " + spaceship.getLife(), 10, 150);

		if(System.currentTimeMillis() - time >= 500) {
			fps = frame*2;
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

		if(ennemies.size() == 0 && !generating) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					generating = true;
					ennemies.addAll(EnemyEntity.generate(36));
					generating = false;
				}
			}).start();
		} else if(rand.nextInt(10) == 0 && !generating) {
			ennemies.get(rand.nextInt(ennemies.size())).shoot();
		}

		Direction dir = null;
		Direction dir1 = null;

		for(EnemyEntity en : ennemies) {
			dir1 = en.move();
			if(dir == null && dir1 != null) {
				dir = dir1;
			}
		}

		if(dir != null) {
			for(EnemyEntity en : ennemies) {
				en.setDirection(dir);
				//FIXME : Si les ennemis touchent le sol, ils vont appelé 12 fois gameOver() le temps que la boucle se finisse
				en.goDown();
			}
		}

		for(int i = 0; i < spaceship.getBullets().size(); i++) {
			BulletEntity bu = spaceship.getBullets().get(i);

			bu.move();

			if(!bu.isAlive()) {
				spaceship.getBullets().remove(bu);
			} else {
				for(int j = 0; j < ennemies.size(); j++) {

					EnemyEntity en = ennemies.get(j);

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
		}

		for(int i = 0; i < EnemyEntity.getBullets().size(); i++) {
			BulletEntity bu = EnemyEntity.getBullets().get(i);

			bu.move();

			if(!bu.isAlive()) {
				EnemyEntity.getBullets().remove(bu);
			} else {
				if(Collision.test(bu, spaceship)) {

					spaceship.hit();
					EnemyEntity.getBullets().remove(bu);

					if(spaceship.getLife() == 0) {
						//						gameOver();
					}
				}
			}
		}
	}

	public static void gameOver() {
		music.stop();
		update.stop();
		Frame.setPanel(new Menu());
	}
}
