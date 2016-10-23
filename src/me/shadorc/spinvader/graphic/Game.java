package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import me.shadorc.spinvader.KListener;
import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Main.Mode;
import me.shadorc.spinvader.Sound;
import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.entity.Boss;
import me.shadorc.spinvader.entity.Enemy;
import me.shadorc.spinvader.entity.Entity;
import me.shadorc.spinvader.entity.Spaceship;
import me.shadorc.spinvader.sprites.AnimatedSprite;
import me.shadorc.spinvader.sprites.BumpingText;
import me.shadorc.spinvader.sprites.Effect;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private static int FPS_CAP = 60;

	private boolean isRunning, isGameOver, isNewRecord;
	private boolean showHitbox, showDebug;

	private ArrayList <Entity> entities, entitiesBuffer;
	private ArrayList <Effect> effects, effectsBuffer;

	private Spaceship spaceship;

	private KListener listener;
	private Image background;
	private Sound music;

	private long multiplierStartTime;
	private int scoreMultiplier;
	private BumpingText textMultiplier;

	private int level;
	private int score;
	private int fps;

	private Thread generation;
	private Thread runningThread;

	public Game() {
		super();

		this.isRunning = false;
		this.isGameOver = false;

		this.showHitbox = false;
		this.showDebug = false;

		this.entities = new ArrayList <Entity> ();
		this.entitiesBuffer = new ArrayList <Entity> ();

		this.effects = new ArrayList <Effect> ();
		this.effectsBuffer = new ArrayList <Effect> ();

		this.spaceship = new Spaceship(Main.getFrame().getWidth()/2, Main.getFrame().getHeight()/2);
		this.addEntity(spaceship);

		this.listener = new KListener();
		this.background = Sprite.get("background.jpg").getImage();
		this.music = new Sound("Savant - Spaceheart.wav", 1, Data.MUSIC_VOLUME);

		this.scoreMultiplier = 1;
		this.textMultiplier = new BumpingText(Main.getFrame().getWidth(), 20, 0, null, Text.createFont("space_age.ttf", 120), new Color(255, 255, 0, 0), 120);
		this.addEffect(textMultiplier);

		this.level = 0;
		this.score = 0;
		this.fps = 0;

		this.generation = new Thread();
		this.runningThread = new Thread(this);

		this.addKeyListener(listener);
		this.setFocusable(true);

		//Hide cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);
	}

	@Override
	public void run() {
		long loopTime = System.nanoTime();

		while(isRunning) {
			double delta = (System.nanoTime() - loopTime)/Math.pow(10, 6); //Converto to ms
			loopTime = System.nanoTime();

			fps = (int) Math.round(1000d/delta);
			entitiesBuffer = new ArrayList <Entity> (entities);
			effectsBuffer = new ArrayList <Effect> (effects);

			this.update(delta);
			this.repaint();

			long elapsedNanos = System.nanoTime() - loopTime; //Time to render in ns
			long nanosToSleep = (long) Math.max(0, (Math.pow(10, 9)/FPS_CAP - elapsedNanos)); //Time to sleep to match FPS_CAP in ns

			long start = System.nanoTime();
			while(System.nanoTime()-start < nanosToSleep); //Sleep
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		if(Storage.isEnable(Data.ANTIALIASING_ENABLE)) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);

		for(Entity en : entitiesBuffer) {
			g2d.drawImage(en.getImage(), en.getX(), en.getY(), null);
			if(showHitbox) {
				Rectangle hitbox = en.getHitbox();
				g2d.setColor(Color.RED);
				g2d.drawRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
			}
		}

		for(Effect effect : effectsBuffer) {
			effect.render(g2d);
		}

		//Life bar
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int) (spaceship.getHitbox().getX()-1), (int) (spaceship.getHitbox().getMaxY()-1), (int) (spaceship.getLife()*spaceship.getHitbox().getWidth()/spaceship.getMaximumLife()+2), (int) (20*Frame.getScaleY())+2);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) spaceship.getHitbox().getX(), (int) (spaceship.getHitbox().getMaxY()), (int) (spaceship.getLife()*spaceship.getHitbox().getWidth()/spaceship.getMaximumLife()), (int) (20*Frame.getScaleY()));

		String sc = "Score : " + score;
		g2d.setFont(Text.createFont("space_age.ttf", 50));
		g2d.setColor(Color.RED);
		g2d.drawString(sc, Main.getFrame().getWidth()-Text.getWidth(g2d, sc)-10, Text.getHeight(g2d, sc));

		if(showDebug) {
			int mb = 1024*1024;
			Runtime runtime = Runtime.getRuntime();

			g2d.setFont(Text.createFont("Consolas", 20));
			g2d.setColor(Color.RED);

			ArrayList <String> debugInfos = new ArrayList <String> ();
			debugInfos.add("Resolution: " + Main.getFrame().getWidth() + "x" + Main.getFrame().getHeight());
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

		if(isGameOver) {
			//Transparent filter to darken the game 
			g2d.setPaint(new Color(0, 0, 0, 0.5f));
			g2d.fillRect(0, 0, Main.getFrame().getWidth(), Main.getFrame().getHeight());

			g2d.setFont(Text.createFont("space_age.ttf", 200));
			g2d.setColor(Color.RED);

			String gameOver = "GAME OVER !";
			g.drawString(gameOver, Text.getCenterWidth(g2d, gameOver), Text.getHeight(g2d, gameOver)+50);

			if(isNewRecord) {
				g2d.setFont(Text.createFont("space_age.ttf", 100));
				g2d.setColor(Color.GREEN);

				String highscore = "HIGHSCORE !";
				g.drawString(highscore, Text.getCenterWidth(g2d, highscore), Main.getFrame().getHeight()/3);
			}

			g2d.setFont(Text.createFont("space_age.ttf", 50));
			g2d.setColor(Color.WHITE);

			ArrayList <Integer> scores = Storage.getScores();
			for(int i = 0; i < scores.size(); i++) {
				String str_score = (i+1) + ". " + scores.get(i);
				int textHeight = Text.getHeight(g2d, str_score)+5;
				g2d.drawString(str_score, Text.getCenterWidth(g2d, str_score), (Main.getFrame().getHeight()+textHeight*(2*i-scores.size()))/2);
			}

			g2d.setFont(Text.getFont("Consolas", 30));
			g2d.setColor(Color.WHITE);

			String str_goHome = "Press \"Esc\" to return to the menu";
			g2d.drawString(str_goHome, Text.getCenterWidth(g2d, str_goHome), 3*Main.getFrame().getHeight()/4);

			String str_restart = "Press \"Enter\" to restart";
			g2d.drawString(str_restart, Text.getCenterWidth(g2d, str_restart), 3*Main.getFrame().getHeight()/4+Text.getHeight(g2d, str_restart));
		}
	}

	public void update(double delta) {
		if(isGameOver) {
			if(listener.wasKeyPressed(KeyEvent.VK_ESCAPE))	this.stop(false);
			if(listener.wasKeyPressed(KeyEvent.VK_ENTER))	this.stop(true);
			return;
		}

		if(System.currentTimeMillis()-multiplierStartTime > 1000) scoreMultiplier = 1;

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
					Game.this.generate(36);
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

		for(Effect effect : effectsBuffer) {
			effect.update(delta);
		}
	}

	public void start() {
		music.start();
		isRunning = true;
		runningThread.start();
	}

	public void stop(boolean restart) {
		music.stop();
		isRunning = false;
		isGameOver = false;
		Main.setMode(restart ? Mode.GAME : Mode.MENU);
	}

	public void gameOver() {
		if(!isGameOver) {
			Sound.play("Game Over.wav", 0.5, Data.MUSIC_VOLUME);
			isNewRecord = Storage.getScores().isEmpty() || (score > Storage.getScores().get(0));
			Storage.addScore(score);
			isGameOver = true;
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

	public synchronized void addEffect(Effect effect) {
		effects.add(effect);
	}

	public synchronized void delEffect(Effect effect) {
		effects.remove(effect);
	}

	public void incScore(int sc) {
		this.scoreMultiplier++;
		this.multiplierStartTime = System.currentTimeMillis();
		this.score += sc*this.scoreMultiplier;
		this.textMultiplier.setText("X" + scoreMultiplier);
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
			int xSize = (Main.getFrame().getWidth()-blanck)/column;	

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
					this.addEntity(new Enemy(xSize*x, ySize*y - Main.getFrame().getHeight()/3, enemySprite));
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

		Effect explosion = new AnimatedSprite(x-radius/2, y-radius/2, Sprite.get("explosion.png", (int) radius, (int) radius), 100);
		this.addEffect(explosion);
	}

	public int getScoreMultiplier() {
		return scoreMultiplier;
	}

}
