package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import me.shadorc.spinvader.graphic.Frame.Mode;

public class Menu extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private String title = "Spinvader";

	private ImageIcon background;
	private Font font;

	private JButton selectedButton;
	private JButton start;
	private JButton options;
	private JButton quit;

	private double sleep = System.currentTimeMillis();

	Menu() {
		super(new GridBagLayout());
		this.addKeyListener(this);

		background = Sprite.get("menu_background.jpg");

		font = Text.getFont("space_invaders.ttf", 200);

		JPanel buttons = new JPanel(new GridLayout(3, 0, 0, 35));
		buttons.setPreferredSize(new Dimension(700, 350));
		buttons.setOpaque(false);

		start = this.createButton("Start");
		options = this.createButton("Options");
		quit = this.createButton("Quit");

		selectedButton = start;

		buttons.add(start);
		buttons.add(options);
		buttons.add(quit);

		this.add(buttons);
	}		

	private JButton createButton(String name) {
		JButton button = new JButton();		
		button.setText("<html><font size=7 color=red>" + name.substring(0, 1) + "<font color=black>" + name.substring(1));

		button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
		button.setBackground(Color.WHITE);

		button.setFocusable(false);
		button.addActionListener(this);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				JButton bu = (JButton) e.getSource();
				bu.setBackground(new Color(150, 150, 150));
				if(bu != selectedButton) {
					selectedButton.setBackground(Color.WHITE);
					selectedButton = bu;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JButton bu = (JButton) e.getSource();
				bu.setBackground(Color.WHITE);
			}
		});

		return button;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform transform = g2d.getTransform();
		g2d.scale(Frame.getScaleX(), Frame.getScaleY());

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		g2d.drawString(title, Text.getTextCenterWidth(g2d, title), Frame.getHeight()/5);

		g2d.setTransform(transform);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton bu = (JButton) event.getSource();

		if(bu == start) {
			Frame.setPanel(Mode.GAME);
		} else if(bu == options) {
			Frame.setPanel(Mode.OPTIONS);
		} else if(bu == quit) {
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if((System.currentTimeMillis() - sleep >= 75) && (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)) {
			sleep = System.currentTimeMillis();

			JButton buttons[] = {start, options, quit};

			selectedButton.setBackground(Color.WHITE);

			int index = Arrays.asList(buttons).indexOf(selectedButton) + (key == KeyEvent.VK_DOWN ? 1 : -1);
			if(index > 2) {
				index = 0;
			} else if(index < 0) {
				index = 2;
			}

			selectedButton = buttons[index];
			selectedButton.setBackground(new Color(150, 150, 150));
		}

		if(key == KeyEvent.VK_ENTER) {
			selectedButton.doClick();
		}

		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {	}

	@Override
	public void keyTyped(KeyEvent e) {	}
}
