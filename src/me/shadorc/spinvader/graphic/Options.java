package me.shadorc.spinvader.graphic;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class Options extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon background;

	private static JCheckBox antialias;

	Options() {
		super(new GridBagLayout());

		background = new ImageIcon(this.getClass().getResource("/img/menu_background.jpg"));

		Font font = new Font("Consolas", Font.PLAIN, 30);

		antialias = new JCheckBox("Anti-aliasing");
		antialias.setFont(font);
		antialias.setOpaque(false);

		this.add(antialias);
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
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Frame.setPanel(new Menu());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
