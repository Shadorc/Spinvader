package me.shadorc.spinvader.graphic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	public static void main(String[] args) {
		frame = new Frame();
	}

	Frame() {
		super("Spinvader");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setContentPane(new Menu());
		this.setUndecorated(true);
		this.pack();

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (device.isFullScreenSupported()) {
			device.setFullScreenWindow(this);
		} else {
			JOptionPane.showMessageDialog(null, "Le mode plein ecran n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		}

		this.setVisible(true);
	}

	public static void setPanel(JPanel pane) {
		frame.getContentPane().removeAll();
		frame.setContentPane(pane);
		frame.getContentPane().requestFocus();
		frame.getContentPane().revalidate();
		frame.repaint();
	}

	public static int getScreenWidth() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
	}

	public static int getScreenHeight() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
	}

	public static void gameOver() {
		setPanel(new Menu());
	}
}
