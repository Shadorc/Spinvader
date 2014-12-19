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

import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.entity.BulletEntity;
import me.shadorc.spinvader.entity.EnemyEntity;
import me.shadorc.spinvader.entity.Entity;
import me.shadorc.spinvader.entity.SpaceshipEntity;

public class Game extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private SpaceshipEntity spaceship;

	private Random rand = new Random();

	private ImageIcon background;
	private KListener listener;
	private int score = 0;

	private static Sound music;

	private boolean showHitbox = true;
	private boolean generating = false;

	private double fpsTime = System.currentTimeMillis();
	private double loopTime = System.currentTimeMillis();
	private int fps = 0, frame = 0;
	private static Timer update;

	private ArrayList <Entity> entities;

	Game() {

		entities = new ArrayList <Entity>();

		spaceship = new SpaceshipEntity(Frame.getScreenWidth() / 2, Frame.getScreenHeight() / 2, this);

		entities.add(spaceship);
		entities.addAll(EnemyEntity.generate(36, this));

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

		for(Entity en : entities) {
			g2d.drawImage(en.getImage(), (int) en.getX(), (int) en.getY(), null);
			if(showHitbox) {
				en.drawHitbox(g2d);
			}
		}

		g2d.setFont(new Font("Consolas", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getScreenWidth() - 150, 30);
		g2d.drawString(fps + " FPS", 10, 30);
		g2d.drawString("Resolution: " + Frame.getScreenWidth() + "x" + Frame.getScreenHeight(), 10, 60);
		g2d.drawString("Ennemies: " + this.getEnemies().size(), 10, 90);
		g2d.drawString("Show Hitbox: " + showHitbox, 10, 120);
		g2d.drawString("Life: " + spaceship.getLife(), 10, 150);
		g2d.drawString("Bullet: " + this.getBullets().size(), 10, 180);

		if(System.currentTimeMillis() - fpsTime >= 500) {
			fps = frame * 2;
			frame = 0;
			fpsTime = System.currentTimeMillis();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();

		double delta = System.currentTimeMillis() - loopTime;
		loopTime = System.currentTimeMillis();

		frame++;

		for(int key : listener.getKeysPressed()) {
			if(key == KeyEvent.VK_ESCAPE)	System.exit(0);
			if(key == KeyEvent.VK_LEFT)		spaceship.moveLeft();
			if(key == KeyEvent.VK_RIGHT)	spaceship.moveRight();
			if(key == KeyEvent.VK_UP)		spaceship.moveForward();
			if(key == KeyEvent.VK_DOWN)		spaceship.moveBackward();
			if(key == KeyEvent.VK_SPACE)	spaceship.shoot();
		}

		if(!this.isEnemiesAlive() && !generating) {
			Game game = this;
			new Thread(new Runnable() {
				@Override
				public void run() {
					generating = true;
					entities.addAll(EnemyEntity.generate(36, game));
					generating = false;
				}
			}).start();
		} else if(rand.nextInt(10) == 0 && !generating) {
			this.getEnemies().get(rand.nextInt(this.getEnemies().size())).shoot();
		}

		for(int i = 0; i < entities.size(); i++) {
			Entity en = entities.get(i);

			en.move(delta);

			for(int o = 0; o < entities.size(); o++) {
				Entity en1 = entities.get(o);
				if(en.getHitbox().intersects(en1.getHitbox())) {
					en.collidedWith(en1);
					en1.collidedWith(en);
				}
			}
		}
	}

	public static void gameOver() {
		music.stop();
		update.stop();
		Frame.setPanel(new Menu());
	}
	
	private ArrayList <BulletEntity> getBullets() {
		ArrayList <BulletEntity> list = new ArrayList <BulletEntity>();
		for(Entity en : entities) {
			if(en instanceof BulletEntity) {
				list.add((BulletEntity) en);
			}
		}
		return list;
	}

	private ArrayList <EnemyEntity> getEnemies() {
		ArrayList <EnemyEntity> list = new ArrayList <EnemyEntity>();
		for(Entity en : entities) {
			if(en instanceof EnemyEntity) {
				list.add((EnemyEntity) en);
			}
		}
		return list;
	}

	private boolean isEnemiesAlive() {
		for(Entity en : entities) {
			if(en instanceof EnemyEntity) {
				return true;
			}
		}
		return false;
	}

	public void addEntity(Entity en) {
		entities.add(en);
	}

	public void removeEntity(Entity en) {
		entities.remove(en);
	}
}
