package src.view;

import javax.swing.*;

import src.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MainMenu extends JPanel {
    private Main mainApp;
    private BufferedImage backgroundImage;

    public MainMenu(Main mainApp) {
        this.mainApp = mainApp;
        loadImages();
        setLayout(new GridBagLayout());
        setBackground(Theme.COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0); 
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("MAIN MENU", SwingConstants.CENTER);
        titleLabel.setFont(Theme.FONT_HEADER);
        titleLabel.setForeground(Theme.COLOR_PRIMARY);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JButton btnNewGame = new JButton("NEW GAME");
        Theme.styleButton(btnNewGame);
        gbc.gridy = 1; 
        add(btnNewGame, gbc);

        JButton btnContinue = new JButton("CONTINUE");
        Theme.styleButton(btnContinue);
        gbc.gridy = 2; 
        add(btnContinue, gbc);
        
        JButton btnExit = new JButton("EXIT GAME");
        Theme.styleButton(btnExit);
        btnExit.setBackground(new Color(100, 30, 30));
        btnExit.setForeground(Color.WHITE);
        gbc.gridy = 3; 
        add(btnExit, gbc);

        btnNewGame.addActionListener(e -> { mainApp.startGame("newgame");});

        // Continue
        btnContinue.addActionListener(e -> {
            String userId = mainApp.getActiveUserId();
            if (userId == null) {
                mainApp.logout();
                return;
            }

            int level = mainApp.getDB().loadCheckpoint(userId);
            if (level > 0) {
                JOptionPane.showMessageDialog(this, "Save Data Ditemukan: Level " + level + "\n(Melanjutkan...)");
                mainApp.startGame("continue");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada Save Data.");
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void loadImages() {
        try {
            var bgUrl = getClass().getResource("/assets/bg_main.jpg");
            if (bgUrl != null) backgroundImage = ImageIO.read(bgUrl);
        } catch (Exception e) {}
    }
}