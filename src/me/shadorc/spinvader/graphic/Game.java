package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.entity.Boss;
import me.shadorc.spinvader.entity.Enemy;
import me.shadorc.spinvader.entity.Entity;
import me.shadorc.spinvader.entity.Spaceship;
import me.shadorc.spinvader.graphic.Frame.Mode;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private static boolean gameOver = false;
	private static boolean showHitbox = false;
	private static boolean showDebug = false;
	private static boolean update = false;

	private static ArrayList <Entity> entities;
	private static Spaceship spaceship;

	private static int FPS_CAP = 60;
	private static int score = 0;
	private static int money = 0;
	private static int level = 0; //FIXME: level start at 1

	private Image background;
	private KListener listener;

	private Thread updated;
	private Thread generation;

	private Sound music;

	private int fps = 0; 

	Game() {
		super();
		entities = new ArrayList <Entity>();

		spaceship = new Spaceship(Frame.getWidth()/2, Frame.getHeight()/2);
		entities.add(spaceship);

		background = Sprite.get("background.png").getImage();

		listener = new KListener();

		updated = new Thread(this);
		generation = new Thread();

		music = new Sound("Savant - Spaceheart.wav", 0.5);

		this.addKeyListener(listener);
		this.setFocusable(true);

		//Hide cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);
	}

	@Override
	public void run() {
		double loopTime = System.currentTimeMillis();

		while(update) {
			try {
				Thread.sleep(1000/FPS_CAP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			double delta = System.currentTimeMillis() - loopTime;
			loopTime = System.currentTimeMillis();

			this.update(delta);
			this.repaint();

			fps = (int) (1000/delta);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform transform = g2d.getTransform();
		g2d.scale(Frame.getScaleX(), Frame.getScaleY());

		//		if(Options.isAntialiasEnable()) {
		//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//		}

		g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);

		//Create a copy of entities array to avoid ConcurrentModificationException
		for(Entity en : new ArrayList <Entity> (entities)) {
			g2d.drawImage(en.getImage(), en.getX(), en.getY(), null);
			if(showHitbox) {
				Rectangle re = en.getHitbox();
				g.setColor(Color.RED);
				g.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
			}
		}

		//Life bar
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, (int) ((Frame.getHeight()/spaceship.getMaximumLife())*(spaceship.getMaximumLife()-spaceship.getLife())), 30, Frame.getHeight());

		g2d.setFont(new Font("Consolas", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getWidth() - 150, 30);
		g2d.drawString("Money : " + money, Frame.getWidth() - 150, 60);

		if(showDebug) {
			int mb = 1024*1024;
			Runtime runtime = Runtime.getRuntime();

			ArrayList <String> infos = new ArrayList <String> ();
			infos.add("Resolution: " + Frame.getWidth() + "x" + Frame.getHeight());
			infos.add("Level: " + level);
			infos.add("Life: " + spaceship.getLife());
			infos.add("Entities: " + entities.size());
			infos.add(fps + " FPS");
			infos.add("Memory used: "+ (runtime.totalMemory() - runtime.freeMemory())/mb + "/" + (runtime.totalMemory()/mb) + " Mo");
			infos.add("Threads: " + Thread.activeCount());

			for(int i = 30; i < infos.size() * 30; i+=30) {
				g2d.drawString(infos.get(i/30), 40, i);
			}
		}

		if(gameOver) {
			//Transparent filter to darken the game 
			g2d.setPaint(new Color(0, 0, 0, 0.5f));
			g2d.fillRect(0, 0, Frame.getWidth(), Frame.getHeight());

			g2d.setFont(new Font("Consolas", Font.BOLD, 200));
			g2d.setColor(Color.RED);

			String fir = "GAME OVER !";
			int start = this.getCenteredText(g2d, fir);
			g.drawString(fir, start, Frame.getHeight()/2);

			g2d.setFont(new Font("Consolas", Font.BOLD, 30));
			g2d.setColor(Color.WHITE);

			String sec = "Press \"Esc\" to return to the menu.";
			start = this.getCenteredText(g2d, sec); 
			g2d.drawString(sec, start, Frame.getHeight()/2 + 50);
		}

		g2d.setTransform(transform);
	}

	public void update(double delta) {
		if(gameOver) {
			if(listener.getKeysPressed().contains(KeyEvent.VK_ESCAPE)) {
				this.stop();
			}
			return;
		}

		//Create a copy of entities array to avoid ConcurrentModificationException
		for(int key : new ArrayList <Integer> (listener.getKeysPressed())) {
			if(key == KeyEvent.VK_ESCAPE)		this.stop();
			else if(key == KeyEvent.VK_LEFT)	spaceship.moveLeft(delta);
			else if(key == KeyEvent.VK_RIGHT)	spaceship.moveRight(delta);
			else if(key == KeyEvent.VK_UP)		spaceship.moveForward(delta);
			else if(key == KeyEvent.VK_DOWN)	spaceship.moveBackward(delta);
			else if(key == KeyEvent.VK_SPACE)	spaceship.shoot();
		}

		if(listener.wasKeyPressed(KeyEvent.VK_F3)) showDebug = !showDebug;
		if(listener.wasKeyPressed(KeyEvent.VK_F4)) showHitbox = !showHitbox;

		if(!generation.isAlive() && !this.isEnemyAlive()) {
			generation = new Thread(new Runnable() {
				@Override
				public void run() {
					generate(36);
				}
			});
			generation.start();
		}

		for(Entity en : new ArrayList <Entity> (entities)) {
			en.move(delta);

			if(en instanceof Enemy || en instanceof Boss) {
				en.shoot();
			}

			for(Entity en1 : new ArrayList <Entity> (entities)) {
				if(en != en1 && en.getHitbox().intersects(en1.getHitbox())) {
					en.collidedWith(en1);
					en1.collidedWith(en);
				}
			}
		}
	}

	public void start() {
		music.start();
		update = true;
		updated.start();
	}

	public void stop() {
		music.stop();
		update = false;
		gameOver = false;
		Frame.setPanel(Mode.MENU);
	}

	public static void gameOver() {
		Sound.play("Game Over.wav", 0.5);
		gameOver = true;
	}

	private boolean isEnemyAlive() {
		for(Entity en : entities) {
			if(en instanceof Enemy || en instanceof Boss) {
				return true;
			}
		}
		return false;
	}

	public static void addEntity(Entity en) {
		entities.add(en);
	}

	public static void removeEntity(Entity en) {
		entities.remove(en);
	}

	public static void increaseScore(int i) {
		score += i;		
	}

	public static void increaseMoney(int i) {
		money += i;		
	}

	public static void increaseLife(int i) {
		spaceship.heal(i);
	}

	private int getCenteredText(Graphics g, String str) {
		int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int start = Frame.getWidth() / 2 - stringLen / 2;

		return start;
	}

	public static void bringDownEnemies() {
		for(Entity en : entities) {
			if(en instanceof Enemy) {
				((Enemy) en).goDown();
			}
		}
	}

	private void generate(int enemies) {
		level++;	

		if(level < 8) {
			final int line = 3;					//Lines
			final int column = enemies/line;	//Columns
			final int blanck = 100;				//Space without enemies
			final int space = 20;				//Space between enemies

			//Get the original sprite
			ImageIcon enemySprite = Sprite.generateSprite(level); 

			//Space occupied by each enemy counting the blanck without enemies 
			int xSize = (Frame.getWidth()-blanck)/column;	

			//Enemy width without space between them
			int enemyWidth = xSize - space; 

			//Resize enemy height depending upon its width
			float scale = (float) Math.max(enemySprite.getIconWidth(), enemyWidth) / Math.min(enemySprite.getIconWidth(), enemyWidth); 
			int enemyHeight = (int) (enemySprite.getIconHeight()*scale); 

			//Space occupied by each enemy counting the space between them 
			int ySize = enemyHeight + space; 

			//Resize original sprite with new dimension
			enemySprite = Sprite.resize(enemySprite, enemyWidth, enemyHeight); 

			for(int y = 0; y < line; y++) {
				for(int x = 0; x < column; x++) {
					entities.add(new Enemy(xSize*x, ySize*y - Frame.getHeight()/3, enemySprite));
				}
			}

		} else if(level == 8) {
			entities.add(new Boss(100, 50));

		} else {
			level = 1;
		}
	}

	public static int rand(int i) {
		return new Random().nextInt(i);
	}
}
