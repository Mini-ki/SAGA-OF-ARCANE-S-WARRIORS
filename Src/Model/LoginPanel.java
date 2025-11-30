package Src.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class LoginPanel extends JPanel { 
    private Main mainApp;
    private JTextField userField; 
    private JPasswordField passField;
    private BufferedImage backgroundImage;

    public LoginPanel(Main mainApp) {
        this.mainApp = mainApp;
        loadImages();
        setLayout(new GridBagLayout()); 
        setBackground(Theme.COLOR_BACKGROUND);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(0, 0, 0, 180));
        contentPanel.setBorder(BorderFactory.createLineBorder(Theme.COLOR_PRIMARY, 2));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 

        // Judul
        JLabel titleLabel = new JLabel("ARCANE SAGA", SwingConstants.CENTER);
        titleLabel.setFont(Theme.FONT_HEADER);
        titleLabel.setForeground(Theme.COLOR_PRIMARY);
        gbc.gridy = 0; 
        contentPanel.add(titleLabel, gbc);

        // Input User
        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setForeground(Theme.COLOR_TEXT_SECONDARY);
        userLabel.setFont(Theme.FONT_SUBTITLE);
        gbc.gridy = 1; 
        contentPanel.add(userLabel, gbc);

        userField = new JTextField(15);
        Theme.styleTextField(userField);
        gbc.gridy = 2; 
        contentPanel.add(userField, gbc);

        // Input Pass
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setForeground(Theme.COLOR_TEXT_SECONDARY);
        passLabel.setFont(Theme.FONT_SUBTITLE);
        gbc.gridy = 3; 
        contentPanel.add(passLabel, gbc);

        passField = new JPasswordField(15);
        Theme.styleTextField(passField);
        gbc.gridy = 4; 
        contentPanel.add(passField, gbc);

        // Tombol
        JButton loginButton = new JButton("LOG IN");
        Theme.styleButton(loginButton);
        gbc.gridy = 5; 
        gbc.insets = new Insets(20, 20, 10, 20);
        contentPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("REGISTER");
        Theme.styleButton(registerButton);
        registerButton.setBackground(Theme.COLOR_PANEL);
        registerButton.setForeground(Theme.COLOR_PRIMARY);
        gbc.gridy = 6; 
        gbc.insets = new Insets(0, 20, 20, 20);
        contentPanel.add(registerButton, gbc);

        add(contentPanel);

        loginButton.addActionListener(e -> processLogin());
        registerButton.addActionListener(e -> processRegister());
    }
    
    private void processLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword()); 

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi Username & Password!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mainApp.getDB().login(username, password)) {
            // Login sukses, kirim ID "1" (dummy) dan username ke Main
            mainApp.onLoginSuccess("1", username); 
        } else {
            JOptionPane.showMessageDialog(this, "Login Gagal! Cek User/Pass.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processRegister() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi data registrasi!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mainApp.getDB().register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal! Username mungkin terpakai.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void loadImages() {
        try {
            var bgUrl = getClass().getResource("/assets/bg_main.jpg");
            if (bgUrl != null) backgroundImage = ImageIO.read(bgUrl);
        } catch (Exception e) {}
    }
}