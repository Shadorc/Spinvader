package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import me.shadorc.spinvader.graphic.Frame.Mode;

public class Options extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon background;

	private JCheckBox antialias;
	private JCheckBox fullscreen;

	Options() {
		super(new GridLayout(1, 2));

		background = Sprite.get("menu_background.jpg");

		Font font = Text.createFont("space_invaders.ttf", 30);

		antialias = new JCheckBox("Anti-aliasing", true);
		antialias.setFont(font);
		antialias.setForeground(Color.WHITE);
		antialias.setOpaque(false);
		antialias.setFocusable(false);

		fullscreen = new JCheckBox("FullScreen", true);
		fullscreen.setFont(font);
		fullscreen.setForeground(Color.WHITE);
		fullscreen.setOpaque(false);
		fullscreen.setFocusable(false);

		ArrayList <String> sizes = new ArrayList <String> ();
		for(DisplayMode dm : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayModes()) {
			String size = dm.getWidth() + "x" + dm.getHeight();
			if(!sizes.contains(size)) {
				sizes.add(size);
			}
		}

		JComboBox <String> resolution = new JComboBox <String> (sizes.toArray(new String[sizes.size()]));
		resolution.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
		resolution.setOpaque(false);
		resolution.setFocusable(false);

		font = font.deriveFont(50f);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 4, true);
		Border emptyBorder = BorderFactory.createEmptyBorder(250, 50, 250, 50);

		TitledBorder titleBorder1 = BorderFactory.createTitledBorder(lineBorder, "Video Settngs", TitledBorder.CENTER, TitledBorder.TOP);
		TitledBorder titleBorder2 = BorderFactory.createTitledBorder(lineBorder, "Audio Settings", TitledBorder.CENTER, TitledBorder.TOP);

		titleBorder1.setTitleFont(font);
		titleBorder2.setTitleFont(font);
		titleBorder1.setTitleColor(Color.BLACK);
		titleBorder2.setTitleColor(Color.BLACK);

		Border border1 = BorderFactory.createCompoundBorder(emptyBorder, titleBorder1);
		Border border2 = BorderFactory.createCompoundBorder(emptyBorder, titleBorder2);

		JPanel video = new JPanel(new GridLayout(5, 1));
		video.setOpaque(false);
		video.setBorder(border1);
		video.add(antialias);
		video.add(fullscreen);
		video.add(resolution);
		this.add(video);

		JPanel audio = new JPanel(new GridLayout(5, 1));
		audio.setOpaque(false);
		audio.setBorder(border2);
		this.add(audio);

		this.addKeyListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}

	public boolean isAntialiasEnable() {
		return antialias.isSelected();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Frame.setPanel(Mode.MENU);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {	}

	@Override
	public void keyTyped(KeyEvent e) {	}
}
