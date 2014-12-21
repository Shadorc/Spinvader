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

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.entity.EnemyEntity;
import me.shadorc.spinvader.entity.Entity;
import me.shadorc.spinvader.entity.SpaceshipEntity;

public class Game extends JPanel implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;

	private SpaceshipEntity spaceship;

	private ImageIcon background;
	private KListener listener;

	private int score = 0;
	private int level = 1;

	private static Sound music;

	private boolean showHitbox = false;
	private boolean showDebug = true;
	private boolean gameOver = false;

	private Thread th;

	private double fpsTime = System.currentTimeMillis();
	private double loopTime = System.currentTimeMillis();
	private int fps = 0, frame = 0;
	private Timer update;

	private ArrayList <Entity> entities;

	Game() {

		spaceship = new SpaceshipEntity(Frame.getScreenWidth() / 2, Frame.getScreenHeight() / 2, this);

		entities = new ArrayList <Entity>();
		entities.add(spaceship);
		entities.addAll(EnemyEntity.generate(36, this));

		background = new ImageIcon(this.getClass().getResource("/img/background.png"));
		listener = new KListener();

		th = new Thread(this);

		music = new Sound("Savant - Amerika.wav", 1);
		music.start();

		this.addKeyListener(listener);
		this.setFocusable(true);

		//Hide cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);

		update = new Timer(1, this);
		update.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		//FIXME: If thread is generating enemiies, concurrencial modification exception occured.
		for(Entity en : entities) {
			g2d.drawImage(en.getImage(), (int) en.getX(), (int) en.getY(), null);
			if(showHitbox) {
				en.drawHitbox(g2d);
			}
		}

		//Life bar
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, (int) ((Frame.getScreenHeight()/5)*(5-spaceship.getLife())), 30, Frame.getScreenHeight());

		g2d.setFont(new Font("Consolas", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getScreenWidth() - 150, 30);

		if(showDebug) {
			g2d.drawString(fps + " FPS", 10, 30);
			g2d.drawString("Resolution: " + Frame.getScreenWidth() + "x" + Frame.getScreenHeight(), 10, 60);
			g2d.drawString("Entities: " + entities.size(), 10, 90);
			g2d.drawString("Show Hitbox: " + showHitbox, 10, 120);
			g2d.drawString("Life: " + spaceship.getLife(), 10, 150);
		}

		if(gameOver) {

			//Transparent filter to darken the game 
			g2d.setPaint(new Color(0, 0, 0, 0.5f));
			g2d.fillRect(0, 0, Frame.getScreenWidth(), Frame.getScreenHeight());

			g2d.setFont(new Font("Consolas", Font.BOLD, 200));
			g2d.setColor(Color.RED);

			String fir = "GAME OVER !";
			int start = this.getCenteredText(g2d, fir);
			g.drawString(fir, start, Frame.getScreenHeight()/2);

			g2d.setFont(new Font("Consolas", Font.BOLD, 30));
			g2d.setColor(Color.WHITE);

			String sec = "Press \"Esc\" to return to the menu.";
			start = this.getCenteredText(g2d, sec); 
			g.drawString(sec, start, Frame.getScreenHeight()/2 + 50);
		}

		if(System.currentTimeMillis() - fpsTime >= 500) {
			fps = frame * 2;
			frame = 0;
			fpsTime = System.currentTimeMillis();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();

		if(gameOver) {
			if(listener.getKeysPressed().contains(KeyEvent.VK_ESCAPE)) {
				update.stop();
				music.stop();
				Frame.setPanel(new Menu());
			}
			return;
		}

		double delta = System.currentTimeMillis() - loopTime;
		loopTime = System.currentTimeMillis();

		frame++;

		for(int key : listener.getKeysPressed()) {
			if(key == KeyEvent.VK_ESCAPE)	System.exit(0);
			if(key == KeyEvent.VK_LEFT)		spaceship.moveLeft(delta);
			if(key == KeyEvent.VK_RIGHT)	spaceship.moveRight(delta);
			if(key == KeyEvent.VK_UP)		spaceship.moveForward(delta);
			if(key == KeyEvent.VK_DOWN)		spaceship.moveBackward(delta);
			if(key == KeyEvent.VK_SPACE)	spaceship.shoot();
			if(key == KeyEvent.VK_F3)		showDebug = !showDebug;
			if(key == KeyEvent.VK_F4)		showHitbox = !showHitbox;
		}

		if(!th.isAlive() &&  this.getEnemies().size() == 0) {
			th = new Thread(this);
			th.start();
		}

		for(int i = 0; i < entities.size(); i++) {
			Entity en = entities.get(i);

			en.move(delta);

			if(en instanceof EnemyEntity) {
				((EnemyEntity) en).update();
			}

			for(int o = 0; o < entities.size(); o++) {
				Entity en1 = entities.get(o);
				if(en.getHitbox().intersects(en1.getHitbox())) {
					en.collidedWith(en1);
					en1.collidedWith(en);
				}
			}
		}
	}

	public void gameOver() {
		//		music.stop();
		gameOver = true;
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

	public void addEntity(Entity en) {
		entities.add(en);
	}

	public void removeEntity(Entity en) {
		entities.remove(en);
	}

	@Override
	public void run() {
		entities.addAll(EnemyEntity.generate(36, this));
	}

	public void increaseScore(int i) {
		score += i;		
	}

	private int getCenteredText(Graphics g, String str) {
		int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int start = Frame.getScreenWidth() / 2 - stringLen / 2;

		return start;
	}
}
