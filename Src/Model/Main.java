package Src.Model;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanelContainer;
    
    // Panel Halaman (Cuma 2 dulu)
    private LoginPanel loginPanel;
    private MainMenu mainMenu;
    
    private DatabaseConnection dbConnection; 
    private String activeUserId;

    public Main() {
        setTitle("Arcane Saga - Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); 
        setLocationRelativeTo(null); 
        setResizable(false);

        // 1. Konek Database
        dbConnection = new DatabaseConnection();
        
        // 2. Setup Card Layout
        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);

        // 3. Buat Panel
        loginPanel = new LoginPanel(this);
        mainMenu = new MainMenu(this);
        // gamePanel = new GamePanel(this); // <-- SKIP DULU
        
        // 4. Masukkan Panel ke Container
        mainPanelContainer.add(loginPanel, "LOGIN");
        mainPanelContainer.add(mainMenu, "MENU");
        // mainPanelContainer.add(gamePanel, "GAME"); // <-- SKIP DULU

        add(mainPanelContainer);
        
        // Mulai di Login
        cardLayout.show(mainPanelContainer, "LOGIN");
    }

    // --- Method untuk dipanggil Panel lain ---
    public DatabaseConnection getDB() { 
        return dbConnection; 
    }
    
    public String getActiveUserId() { 
        return activeUserId; 
    }

    // Dipanggil LoginPanel saat sukses
    public void onLoginSuccess(String userId, String username) {
        this.activeUserId = userId;
        System.out.println("Login Success: " + username);
        cardLayout.show(mainPanelContainer, "MENU");
    }

    public void startGame(String heroName) {
        JOptionPane.showMessageDialog(this, 
            "Game Dimulai!\nHero yang dipilih: " + heroName + "\n(GamePanel belum ada, jadi stop di sini dulu ya!)");

    }
    
    public void logout() {
        this.activeUserId = null;
        cardLayout.show(mainPanelContainer, "LOGIN");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
