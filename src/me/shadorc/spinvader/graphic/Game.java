package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
	private int money = 0;
	private int level = 1;

	private static Sound music;

	private boolean showHitbox = false;
	private boolean showDebug = false;
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
		entities.addAll(EnemyEntity.generate(36, level, this));

		background = new ImageIcon(this.getClass().getResource("/img/background.png"));
		listener = new KListener();

		th = new Thread(this);

		music = new Sound("Savant - Spaceheart.wav", 1);
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

		if(Options.isAntialiasEnable()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		//FIXME: If thread is generating enemies, concurrencial modification exception occured.
		for(Entity en : entities) {
			g2d.drawImage(en.getImage(), (int) en.getX(), (int) en.getY(), null);
			if(showHitbox) {
				Rectangle re = en.getHitbox();
				g.setColor(Color.RED);
				g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
			}
		}

		//Life bar
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, (int) ((Frame.getScreenHeight()/spaceship.getMaximumLife())*(spaceship.getMaximumLife()-spaceship.getLife())), 30, Frame.getScreenHeight());

		g2d.setFont(new Font("Consolas", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getScreenWidth() - 150, 30);
		g2d.drawString("Money : " + money, Frame.getScreenWidth() - 150, 60);

		if(showDebug) {
			int mb = 1024*1024;
			Runtime runtime = Runtime.getRuntime();

			ArrayList <String> infos = new ArrayList <String> ();
			infos.add("Resolution: " + Frame.getScreenWidth() + "x" + Frame.getScreenHeight());
			infos.add("Level: " + level);
			infos.add("Life: " + spaceship.getLife());
			infos.add("Entities: " + entities.size());
			infos.add(fps + " FPS");
			infos.add("Used Memory: "+ (runtime.totalMemory() - runtime.freeMemory())/mb + "/" + (runtime.totalMemory()/mb) + " Mo");
			infos.add("Threads: " + Thread.activeCount());

			for(int i = 30; i < infos.size() * 30; i+=30) {
				g2d.drawString(infos.get(i/30), 40, i);
			}
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
			g2d.drawString(sec, start, Frame.getScreenHeight()/2 + 50);
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

		frame++;

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

		if(!th.isAlive() &&  !this.isEnemyAlive()) {
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
		gameOver = true;
		new Sound("Game Over.wav", 0.5).start();
		//FIXME: Memory leaks when dying, find why
	}

	private boolean isEnemyAlive() {
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

	@Override
	public void run() {
		entities.addAll(EnemyEntity.generate(36, level, this));
		level++;
	}

	public void increaseScore(int i) {
		score += i;		
	}

	public void increaseMoney(int i) {
		money += i;		
	}

	public void increaseLife(int i) {
		spaceship.heal(i);
	}

	private int getCenteredText(Graphics g, String str) {
		int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int start = Frame.getScreenWidth() / 2 - stringLen / 2;

		return start;
	}

	public void descendreEnnemis() {
		for(Entity en : entities) {
			if(en instanceof EnemyEntity) {
				((EnemyEntity) en).setToReach();
			}
		}
	}
}
