package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class Options extends JPanel implements KeyListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon background;

	private static JCheckBox antialias = new JCheckBox("Anti-aliasing", true);
	private static JCheckBox fullscreen = new JCheckBox("FullScreen", true);

	Options() {
		super(new GridLayout(1, 2));

		background = Sprite.get("menu_background.jpg");

		Font font = Text.createFont("space_invaders.ttf", 30);

		antialias.setFont(font);
		antialias.setForeground(Color.WHITE);
		antialias.setOpaque(false);
		antialias.setFocusable(false);

		fullscreen.setFont(font);
		fullscreen.addItemListener(this);
		fullscreen.setForeground(Color.WHITE);
		fullscreen.setOpaque(false);
		fullscreen.setFocusable(false);

		ArrayList <String> sizes = new ArrayList <String> ();
		for(DisplayMode dm : Frame.getScreen().getDisplayModes()) {
			String size = dm.getWidth() + "x" + dm.getHeight();
			if(!sizes.contains(size)) {
				sizes.add(size);
			}
		}

		JComboBox <String> resolution = new JComboBox <String> (sizes.toArray(new String[sizes.size()]));
		resolution.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
		resolution.setOpaque(false);
		resolution.setFocusable(false);

		font = Text.createFont("space_invaders.ttf", 50);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 4, true);

		TitledBorder titleBorder1 = BorderFactory.createTitledBorder(lineBorder, "Video Settngs", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder1.setTitleFont(font);
		titleBorder1.setTitleColor(Color.BLACK);

		TitledBorder titleBorder2 = BorderFactory.createTitledBorder(lineBorder, "Audio Settings", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder2.setTitleFont(font);
		titleBorder2.setTitleColor(Color.BLACK);

		JPanel video = new JPanel(new GridLayout(5, 1));
		video.setOpaque(false);
		video.setBorder(titleBorder1);
		video.add(antialias);
		video.add(fullscreen);
		video.add(resolution);
		this.add(video);

		JPanel audio = new JPanel(new GridLayout(5, 1));
		audio.setOpaque(false);
		audio.setBorder(titleBorder2);
		this.add(audio);

		this.addKeyListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}

	public static boolean isAntialiasEnable() {
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

	@Override
	public void itemStateChanged(ItemEvent event) {
		if(event.getSource() == fullscreen) {
			Frame.setFullscreen(event.getStateChange() == ItemEvent.SELECTED);
		}
	}
}
