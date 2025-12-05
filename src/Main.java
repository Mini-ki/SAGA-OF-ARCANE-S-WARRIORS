package src;

import javax.swing.*;

import src.model.DatabaseConnection;
import src.view.LoginPanel;
import src.view.MainMenu;
import src.view.GamePanel; 

import java.awt.*;
import java.net.URL;

public class Main extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanelContainer;
    
    private LoginPanel loginPanel;
    private MainMenu mainMenu;
    private GamePanel gamePanel; 

    private DatabaseConnection dbConnection; 
    private String activeUserId;

    public Main() {
        setTitle("Arcane Saga - Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); 
        setLocationRelativeTo(null); 
        setResizable(false);

        dbConnection = new DatabaseConnection();
        
        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        mainMenu = new MainMenu(this);

        mainPanelContainer.add(loginPanel, "LOGIN");
        mainPanelContainer.add(mainMenu, "MENU");

        add(mainPanelContainer);
        
        cardLayout.show(mainPanelContainer, "LOGIN");
    }

    public DatabaseConnection getDB() { 
        return dbConnection; 
    }
    
    public String getActiveUserId() { 
        return activeUserId; 
    }

    public void onLoginSuccess(String userId, String username) {
        this.activeUserId = userId;
        System.out.println("Login Success: " + username);
        cardLayout.show(mainPanelContainer, "MENU");
    }

    public void startGame(String menu) {
        if (gamePanel == null) {
            gamePanel = new GamePanel(this, menu);
            mainPanelContainer.add(gamePanel, "GAME");
        }

        cardLayout.show(mainPanelContainer, "GAME");
    }

    public void backToMenu() {
        cardLayout.show(mainPanelContainer, "MENU");
    }

    public void logout() {
        this.activeUserId = null;
        cardLayout.show(mainPanelContainer, "LOGIN");
    }

    public DatabaseConnection getDb() {
        return dbConnection;
    }

    public void showMainMenu() {
        cardLayout.show(mainPanelContainer, "MENU");
    }

    public String getCurrentUserId() {
        return activeUserId;
    }
    
    public int getCurrentBossLevel() {
        return 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

}
