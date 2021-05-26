package me.shadorc.spinvader.graphic;

import me.shadorc.spinvader.Main;
import me.shadorc.spinvader.Main.Mode;
import me.shadorc.spinvader.Storage;
import me.shadorc.spinvader.Storage.Data;
import me.shadorc.spinvader.utils.DisplayUtils;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Options extends JPanel implements KeyListener, ItemListener, ChangeListener, DocumentListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private final ImageIcon background;

    private final JCheckBox fullscreen;
    private final JSpinner resSpinner;
    private final JButton backButton, applyButton;

    public Options() {
        super(new BorderLayout());

        background = Sprite.get("menu_background.jpg");

        Font font = Text.createFont("space_invaders.ttf", 30);

        JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        centralPanel.setOpaque(false);

        JCheckBox antialias = new JCheckBox("Anti-aliasing", true);
        antialias.setName(Data.ANTIALIASING_ENABLE.toString());
        antialias.setSelected(Storage.isEnable(Data.ANTIALIASING_ENABLE));
        antialias.setFont(font);
        antialias.addItemListener(this);
        antialias.setForeground(Color.WHITE);
        antialias.setOpaque(false);
        antialias.setFocusable(false);

        fullscreen = new JCheckBox("FullScreen", true);
        fullscreen.setName(Data.FULLSCREEN_ENABLE.toString());
        fullscreen.setSelected(Storage.isEnable(Data.FULLSCREEN_ENABLE));
        fullscreen.setFont(font);
        fullscreen.addItemListener(this);
        fullscreen.setForeground(Color.WHITE);
        fullscreen.setOpaque(false);
        fullscreen.setFocusable(false);

        JSlider musicVolSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, Integer.parseInt(Storage.getData(Data.MUSIC_VOLUME)));
        musicVolSlider.setName(Data.MUSIC_VOLUME.toString());
        musicVolSlider.addChangeListener(this);
        musicVolSlider.setFocusable(false);
        musicVolSlider.setOpaque(false);
        musicVolSlider.setMajorTickSpacing(10);
        musicVolSlider.setMinorTickSpacing(1);
        musicVolSlider.setPaintTicks(true);
        musicVolSlider.setPaintLabels(true);
        musicVolSlider.setForeground(Color.WHITE);

        JSlider soundVolSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, Integer.parseInt(Storage.getData(Data.SOUND_VOLUME)));
        soundVolSlider.setName(Data.SOUND_VOLUME.toString());
        soundVolSlider.addChangeListener(this);
        soundVolSlider.setFocusable(false);
        soundVolSlider.setOpaque(false);
        soundVolSlider.setMajorTickSpacing(10);
        soundVolSlider.setMinorTickSpacing(1);
        soundVolSlider.setPaintTicks(true);
        soundVolSlider.setPaintLabels(true);
        soundVolSlider.setForeground(Color.WHITE);

        SpinnerListModel resModel = new SpinnerListModel(DisplayUtils.RESOLUTIONS_MAP.keySet().toArray());
        resSpinner = new JSpinner(resModel);
        resSpinner.setValue(Storage.getData(Data.RESOLUTION));
        resSpinner.setName(Data.RESOLUTION.toString());
        resSpinner.setOpaque(false);
        resSpinner.setFocusable(false);
        JFormattedTextField resTextField = ((DefaultEditor) resSpinner.getEditor()).getTextField();
        resTextField.getDocument().addDocumentListener(this);
        resTextField.setEditable(false);
        resTextField.setFocusable(false);

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
        videoOptionsPanel.add(resSpinner);
        centralPanel.add(videoOptionsPanel);

        CompoundBorder audioCompoundBorder = BorderFactory.createCompoundBorder(
                margeBorder,
                BorderFactory.createTitledBorder(lineBorder, "Audio Settings", TitledBorder.CENTER, TitledBorder.TOP, font, Color.BLACK));

        JPanel audioOptionsPanel = new JPanel(new GridLayout(5, 1));
        audioOptionsPanel.setOpaque(false);
        audioOptionsPanel.setBorder(audioCompoundBorder);
        audioOptionsPanel.add(musicVolSlider);
        audioOptionsPanel.add(soundVolSlider);
        centralPanel.add(audioOptionsPanel);

        this.add(centralPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.setOpaque(false);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setOpaque(false);
        backButton.setFocusable(false);
        backButton.addActionListener(this);
        backButton.setBackground(Color.WHITE);

        applyButton = new JButton("Apply");
        applyButton.setPreferredSize(new Dimension(200, 50));
        applyButton.setOpaque(false);
        applyButton.setFocusable(false);
        applyButton.addActionListener(this);
        applyButton.setBackground(Color.WHITE);

        buttonsPanel.add(backButton, BorderLayout.WEST);
        buttonsPanel.add(applyButton, BorderLayout.EAST);

        this.add(buttonsPanel, BorderLayout.PAGE_END);

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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Main.setMode(Mode.MENU);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        JCheckBox source = (JCheckBox) event.getSource();
        if (source.equals(fullscreen)) {
            Main.getFrame().setFullscreen(event.getStateChange() == ItemEvent.SELECTED);
        }
        Storage.save(source.getName(), source.isSelected());
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        JSlider source = (JSlider) event.getSource();
        Storage.save(source.getName(), source.getValue());
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        Storage.save(resSpinner.getName(), resSpinner.getValue());
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton source = (JButton) event.getSource();
        if (source.equals(backButton)) {
            Main.setMode(Mode.MENU);
        } else if (source.equals(applyButton)) {
            DisplayUtils.SCREEN.setDisplayMode(DisplayUtils.RESOLUTIONS_MAP.get(resSpinner.getValue()));
        }

    }
}
