package me.shadorc.spinvader.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon background;

	private String title = "Spinvader";

	private JButton start;
	private JButton options;
	private JButton quit;

	Menu() {
		super(new GridLayout(3, 3));

		background = new ImageIcon(this.getClass().getResource("/img/menu_background.jpg"));

		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());

		JPanel buttons = new JPanel(new GridLayout(3, 0, 0, 35));
		buttons.setOpaque(false);

		start = this.createButton(new JButton("Start"));
		options = this.createButton(new JButton("Options"));
		quit = this.createButton(new JButton("Quit"));

		buttons.add(start);
		buttons.add(options);
		buttons.add(quit);

		this.add(buttons);

		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());
	}

	private JButton createButton(JButton button) {
		String name = button.getText();
		button.setText("<html><font size=7 color=red>" + name.substring(0, 1) + "<font color=black>" + name.substring(1));

		button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
		button.setBackground(Color.WHITE);

		button.addActionListener(this);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				((JButton) e.getSource()).setBackground(new Color(220, 220, 220));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				((JButton) e.getSource()).setBackground(Color.WHITE);
			}
		});

		return button;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);

		g2d.setFont(new Font("Consolas", Font.BOLD, 200));
		g2d.setColor(Color.BLACK);

		//Text centered
		int stringLen = (int) g2d.getFontMetrics().getStringBounds(title, g2d).getWidth();
		int start = Frame.getScreenWidth() / 2 - stringLen / 2;

		g2d.drawString(title, start, Frame.getScreenHeight() / 6);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton bu = (JButton) event.getSource();

		if(bu == start) {
			Frame.setPanel(new Game());
		} else if(bu == options) {
			Frame.setPanel(new Options());
		} else if(bu == quit) {
			System.exit(0);
		}
	}
}
