package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.entity.Boss;
import me.shadorc.spinvader.entity.Enemy;
import me.shadorc.spinvader.entity.Entity;
import me.shadorc.spinvader.entity.Spaceship;
import me.shadorc.spinvader.graphic.Frame.Mode;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private static Random rand = new Random();
	private static int FPS_CAP = 60;

	private static boolean showHitbox = false;
	private static boolean showDebug = false;

	private boolean isRunning;
	private boolean gameOver;
	private boolean highScore;

	private ArrayList <Entity> entitiesBuffer;
	private ArrayList <Entity> entities;

	private ArrayList <AnimatedSprite> animatedSprites;
	private ArrayList <AnimatedSprite> spritesBuffer;

	private Spaceship spaceship;

	private KListener listener;
	private Image background;
	private Sound music;

	private float scoreSize;
	private int level;
	private int score;
	private int multiplicator;
	private double multiTime;
	private int fps;

	private Thread generation;
	private Thread updated;

	Game() {
		super();

		this.isRunning = false;
		this.gameOver = false;

		this.entities = new ArrayList <Entity>();
		this.animatedSprites = new ArrayList <AnimatedSprite> ();

		this.spaceship = new Spaceship(Frame.getWidth()/2, Frame.getHeight()/2);
		this.entities.add(spaceship);

		this.listener = new KListener();
		this.background = Sprite.get("background.png").getImage();
		this.music = new Sound("Savant - Spaceheart.wav", 1);

		this.multiplicator = 1;
		this.scoreSize = 50;
		this.level = 0;
		this.score = 0;
		this.fps = 0;

		this.generation = new Thread();
		this.updated = new Thread(this);

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

		while(isRunning) {
			double delta = System.currentTimeMillis() - loopTime;
			loopTime = System.currentTimeMillis();

			fps = (int) (1000/delta);
			entitiesBuffer = new ArrayList <Entity> (entities);
			spritesBuffer = new ArrayList <AnimatedSprite> (animatedSprites);

			this.update(delta);
			this.repaint();

			try {
				Thread.sleep(1000/FPS_CAP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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

		for(Entity en : entitiesBuffer) {
			g2d.drawImage(en.getImage(), en.getX(), en.getY(), null);
			if(showHitbox) {
				Rectangle re = en.getHitbox();
				g2d.setColor(Color.RED);
				g2d.drawRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight());
			}
		}

		for(AnimatedSprite sprite : spritesBuffer) {
			g2d.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), null);
		}

		//Life bar
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, (int) ((Frame.getHeight()/spaceship.getMaximumLife())*(spaceship.getMaximumLife()-spaceship.getLife())), 30, Frame.getHeight());

		g2d.setFont(Text.createFont("space_age.ttf", (int) scoreSize));
		g2d.setColor(Color.RED);
		g2d.drawString("Score : " + score, Frame.getWidth()-Text.getWidth(g2d, "Score : " + score)-10, Text.getHeight(g2d, "Score : " + score));

		if(multiplicator > 1) {
			g2d.setFont(Text.createFont("space_age.ttf", (int) scoreSize*2));
			float alpha = (float) (1-(System.currentTimeMillis()-multiTime)/1000);
			if(alpha < 0) alpha = 0;
			g2d.setPaint(new Color(1, 1, 0, alpha));
			g2d.drawString("X" + multiplicator, Frame.getWidth()-Text.getWidth(g2d, "X" + multiplicator)-10, Text.getHeight(g2d, "X" + multiplicator)+20);
		}

		if(showDebug) {
			int mb = 1024*1024;
			Runtime runtime = Runtime.getRuntime();

			g2d.setFont(Text.createFont("Consolas", 20));
			g2d.setColor(Color.RED);

			ArrayList <String> debugInfos = new ArrayList <String> ();
			debugInfos.add("Resolution: " + Frame.getWidth() + "x" + Frame.getHeight());
			debugInfos.add("Level: " + level);
			debugInfos.add("Life: " + spaceship.getLife());
			debugInfos.add("Entities: " + entitiesBuffer.size());
			debugInfos.add(fps + " FPS");
			debugInfos.add("Memory used: "+ (runtime.totalMemory() - runtime.freeMemory())/mb + "/" + (runtime.totalMemory()/mb) + " Mo");
			debugInfos.add("Threads: " + Thread.activeCount());

			for(int i = 0; i < debugInfos.size(); i++) {
				g2d.drawString(debugInfos.get(i), 40, (i+1)*(5+Text.getHeight(g2d, debugInfos.get(i))));
			}
		}

		if(gameOver) {
			//Transparent filter to darken the game 
			g2d.setPaint(new Color(0, 0, 0, 0.5f));
			g2d.fillRect(0, 0, Frame.getWidth(), Frame.getHeight());

			g2d.setFont(Text.createFont("space_age.ttf", 200));
			g2d.setColor(Color.RED);

			String text = "GAME OVER !";
			g.drawString(text, Text.getTextCenterWidth(g2d, text), Text.getHeight(g2d, text)+50);

			if(highScore) {
				g2d.setFont(Text.createFont("space_age.ttf", 100));
				g2d.setColor(Color.GREEN);

				text = "HIGHSCORE !";
				g.drawString(text, Text.getTextCenterWidth(g2d, text), Frame.getHeight()/3);
			}

			g2d.setFont(Text.createFont("space_age.ttf", 50));
			g2d.setColor(Color.WHITE);

			ArrayList <Integer> scores = Storage.getScores();
			for(int i = 0; i < scores.size(); i++) {
				String str = (i+1) + ". " + scores.get(i);
				int textHeight = Text.getHeight(g2d, str)+5;
				//Frame.getHeight()/2 : Frame center
				//(textHeight*scores.size())/2 : Leaderboard height 
				g2d.drawString(str, Text.getTextCenterWidth(g2d, str), Frame.getHeight()/2 - (textHeight*scores.size())/2 + (textHeight*i));
			}

			g2d.setFont(Text.getFont("Consolas", 30));
			g2d.setColor(Color.WHITE);

			text = "Press \"Esc\" to return to the menu";
			g2d.drawString(text, Text.getTextCenterWidth(g2d, text), Frame.getHeight()-(Frame.getHeight()/4));

			text = "Press \"Enter\" to restart";
			g2d.drawString(text, Text.getTextCenterWidth(g2d, text), Frame.getHeight()-(Frame.getHeight()/4)+Text.getHeight(g2d, text));
		}

		g2d.setTransform(transform);
	}

	public void update(double delta) {
		if(gameOver) {
			if(listener.wasKeyPressed(KeyEvent.VK_ESCAPE))	this.stop(false);
			if(listener.wasKeyPressed(KeyEvent.VK_ENTER))	this.stop(true);
			return;
		}

		//Score bumping
		if(scoreSize > 50)	scoreSize -= delta/20;
		if(System.currentTimeMillis() - multiTime > 1000) multiplicator = 1;

		if(listener.isKeyDown(KeyEvent.VK_ESCAPE))	this.stop(false);
		if(listener.isKeyDown(KeyEvent.VK_LEFT))	spaceship.moveLeft(delta);
		if(listener.isKeyDown(KeyEvent.VK_RIGHT))	spaceship.moveRight(delta);
		if(listener.isKeyDown(KeyEvent.VK_UP))		spaceship.moveForward(delta);
		if(listener.isKeyDown(KeyEvent.VK_DOWN))	spaceship.moveBackward(delta);
		if(listener.isKeyDown(KeyEvent.VK_SPACE))	spaceship.shoot();

		if(listener.wasKeyPressed(KeyEvent.VK_F3)) 	showDebug = !showDebug;
		if(listener.wasKeyPressed(KeyEvent.VK_F4)) 	showHitbox = !showHitbox;

		if(!generation.isAlive() && !this.isEnemyAlive()) {
			generation = new Thread(new Runnable() {
				@Override
				public void run() {
					generate(36);
				}
			});
			generation.start();
		}

		for(Entity en : entitiesBuffer) {
			en.move(delta);

			if(en instanceof Enemy || en instanceof Boss) {
				en.shoot();
			}

			for(Entity en1 : entitiesBuffer) {
				if(!en.equals(en1) && en.getHitbox().intersects(en1.getHitbox())) {
					en.collidedWith(en1);
					en1.collidedWith(en);
				}
			}
		}

		for(AnimatedSprite sprite : spritesBuffer) {
			sprite.update();
		}
	}

	public void start() {
		music.start();
		isRunning = true;
		updated.start();
	}

	public void stop(boolean restart) {
		music.stop();
		isRunning = false;
		gameOver = false;
		Frame.setPanel(restart ? Mode.GAME : Mode.MENU);
	}

	public void gameOver() {
		if(!gameOver) {
			Sound.play("Game Over.wav", 0.5);
			highScore = Storage.getScores().isEmpty() || (score > Storage.getScores().get(0));
			Storage.saveData(score);
			gameOver = true;
		}
	}

	private boolean isEnemyAlive() {
		for(Entity en : entitiesBuffer) {
			if(en instanceof Enemy || en instanceof Boss) {
				return true;
			}
		}
		return false;
	}

	public synchronized void addEntity(Entity en) {
		entities.add(en);
	}

	public synchronized void delEntity(Entity en) {
		entities.remove(en);
	}

	public synchronized void addSprite(float x, float y, ImageIcon img, float duration) {
		animatedSprites.add(new AnimatedSprite(x, y, img, duration));
	}

	public synchronized void delSprite(AnimatedSprite sprite) {
		animatedSprites.remove(sprite);
	}

	public void incScore(int num) {
		multiplicator++;
		multiTime = System.currentTimeMillis();
		score += num*multiplicator;
		scoreSize = 60;
	}

	public Spaceship getSpaceship() {
		return spaceship;
	}

	public int getLevel() {
		return level;
	}

	public void bringDownEnemies() {
		for(Entity en : entitiesBuffer) {
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
			ImageIcon enemySprite = Sprite.generateSprite(); 

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
					this.addEntity(new Enemy(xSize*x, ySize*y - Frame.getHeight()/3, enemySprite));
				}
			}

		} else if(level == 8) {
			this.addEntity(new Boss(100, 50));

		} else {
			level = 1;
		}
	}

	public void explosion(float x, float y, float radius) {
		Ellipse2D zone = new Ellipse2D.Double(x-radius/2, y-radius/2, radius, radius);

		for(Entity en : entitiesBuffer) {
			if(zone.intersects(en.getHitbox()) && en instanceof Enemy) {
				double distance = Math.sqrt(Math.pow(x-en.getX(), 2)+Math.pow(y-en.getY(), 2));
				((Enemy) en).takeDamage((float) distance/200);
			}
		}

		this.addSprite(x-radius/2, y-radius/2, Sprite.get("explosion.png", (int) radius, (int) radius), 100);
	}

	public static int rand(int i) {
		return rand.nextInt(i);
	}
}
