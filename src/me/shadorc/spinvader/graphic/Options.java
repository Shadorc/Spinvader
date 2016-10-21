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
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.graphic.Frame.Mode;

public class Options extends JPanel implements KeyListener, ItemListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon background;

	private JCheckBox antialias, fullscreen;
	private JSlider musicVolSlider, soundVolSlider;

	Options() {
		super(new GridLayout(1, 2));

		background = Sprite.get("menu_background.jpg");

		Font font = Text.createFont("space_invaders.ttf", 30);

		antialias = new JCheckBox("Anti-aliasing", true);
		antialias.setSelected(Storage.isEnable(Data.ANTIALIASING_ENABLE));
		antialias.setFont(font);
		antialias.addItemListener(this);
		antialias.setForeground(Color.WHITE);
		antialias.setOpaque(false);
		antialias.setFocusable(false);

		fullscreen = new JCheckBox("FullScreen", true);
		fullscreen.setSelected(Storage.isEnable(Data.FULLSCREEN_ENABLE));
		fullscreen.setFont(font);
		fullscreen.addItemListener(this);
		fullscreen.setForeground(Color.WHITE);
		fullscreen.setOpaque(false);
		fullscreen.setFocusable(false);

		musicVolSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, Integer.parseInt(Storage.getData(Data.MUSIC_VOLUME)));
		musicVolSlider.addChangeListener(this);
		musicVolSlider.setFocusable(false);
		musicVolSlider.setOpaque(false);
		musicVolSlider.setMajorTickSpacing(10);
		musicVolSlider.setMinorTickSpacing(1);
		musicVolSlider.setPaintTicks(true);
		musicVolSlider.setPaintLabels(true);
		musicVolSlider.setForeground(Color.WHITE);

		soundVolSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, Integer.parseInt(Storage.getData(Data.SOUND_VOLUME)));
		soundVolSlider.addChangeListener(this);
		soundVolSlider.setFocusable(false);
		soundVolSlider.setOpaque(false);
		soundVolSlider.setMajorTickSpacing(10);
		soundVolSlider.setMinorTickSpacing(1);
		soundVolSlider.setPaintTicks(true);
		soundVolSlider.setPaintLabels(true);
		soundVolSlider.setForeground(Color.WHITE);

		ArrayList <String> sizes = new ArrayList <String> ();
		for(DisplayMode dm : Frame.getScreen().getDisplayModes()) {
			String size = dm.getWidth() + "x" + dm.getHeight();
			if(!sizes.contains(size)) {
				sizes.add(size);
			}
		}

		JComboBox <String> resolution = new JComboBox <String> (sizes.toArray(new String[sizes.size()]));
		resolution.setBorder(BorderFactory.createEmptyBorder(75, 0, 75, 0));
		resolution.setOpaque(false);
		resolution.setFocusable(false);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 4, true);
		Border margeBorder = BorderFactory.createEmptyBorder(75, 20, 75, 20);
		font = Text.createFont("space_invaders.ttf", 50);

		CompoundBorder videoCompoundBorder = BorderFactory.createCompoundBorder(
				margeBorder,
				BorderFactory.createTitledBorder(lineBorder, "Video Settings", TitledBorder.CENTER, TitledBorder.TOP, font, Color.BLACK));

		JPanel videoOptionsPanel = new JPanel(new GridLayout(5, 1));
		videoOptionsPanel.setOpaque(false);
		videoOptionsPanel.setBorder(videoCompoundBorder);
		videoOptionsPanel.add(antialias);
		videoOptionsPanel.add(fullscreen);
		videoOptionsPanel.add(resolution);
		this.add(videoOptionsPanel);

		CompoundBorder audioCompoundBorder = BorderFactory.createCompoundBorder(
				margeBorder, 
				BorderFactory.createTitledBorder(lineBorder, "Audio Settings", TitledBorder.CENTER, TitledBorder.TOP, font, Color.BLACK));

		JPanel audioOptionsPanel = new JPanel(new GridLayout(5, 1));
		audioOptionsPanel.setOpaque(false);
		audioOptionsPanel.setBorder(audioCompoundBorder);
		audioOptionsPanel.add(musicVolSlider);
		audioOptionsPanel.add(soundVolSlider);
		this.add(audioOptionsPanel);

		this.addKeyListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
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
			Storage.save(Data.FULLSCREEN_ENABLE, fullscreen.isSelected());
		}
		else if(event.getSource() == antialias) {
			Storage.save(Data.ANTIALIASING_ENABLE, antialias.isSelected());
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if(event.getSource() == musicVolSlider) {
			Storage.save(Data.MUSIC_VOLUME, ((JSlider) event.getSource()).getValue());
		}
		else if(event.getSource() == soundVolSlider) {
			Storage.save(Data.SOUND_VOLUME, ((JSlider) event.getSource()).getValue());
		}
	}
}
