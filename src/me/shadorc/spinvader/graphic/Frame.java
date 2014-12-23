package me.shadorc.spinvader.graphic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Frame {

	private static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("Spinvader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setContentPane(new Menu());
		frame.setUndecorated(true);
		frame.pack();

		new Options();

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if(device.isFullScreenSupported()) {
			device.setFullScreenWindow(frame);
		} else {
			JOptionPane.showMessageDialog(null, "Le mode plein ecran n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
		}

		frame.setVisible(true);
	}

	public static void setPanel(JPanel pane) {
		frame.getContentPane().removeAll();
		frame.setContentPane(pane);
		frame.getContentPane().requestFocus();
		frame.getContentPane().revalidate();
		frame.repaint();
	}

	public static int getWidth() {
		return frame.getWidth();
	}

	public static int getHeight() {
		return frame.getHeight();
	}
}
