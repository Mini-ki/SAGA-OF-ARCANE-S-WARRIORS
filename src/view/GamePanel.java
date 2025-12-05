package src.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import src.Main;
import src.model.Hero;
import src.model.Monster;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamePanel extends JPanel {
    
    public enum GameState {
        HERO_SELECTION, BATTLE
    }
    
    private Main mainApp;
    private GameState currentState;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Clip backgroundMusic;
    
    private JPanel heroSelectionPanel;
    private JLabel statusLabelHero;
    private JLabel imageLabelHero;
    private JPanel statsPanelContainer;
    private JLabel hpLabel, mpLabel, atkLabel, defLabel;
    private JLabel cd1Label, cd2Label, cdUltLabel;
    private JLabel heroNameLabel;
    
    private JTextArea battleLogArea;
    private JPanel battlePanelContainer;
    private JPanel skillButtonPanel;
    private JLabel turnInfoLabel;
    
    // Battle state
    private List<Hero> availableHeroes;
    private List<Hero> selectedTeam;
    private int currentHeroIndex = 0;
    
    private List<Hero> playerTeam;
    private Monster currentMonster;
    private Hero activeHero;
    private int turnCounter = 1;
    private boolean isTurnActive = true;
    private Timer cooldownTimer;
   
    private Image heroImage;
    private Image monsterImage;
    private Image bgImage;

    private long battleStartTime = 0;
    private int elapsedSeconds = 0;
    private Timer elapsedTimeTimer;

    private static final int BATTLE_WIDTH = 1280;
    private static final int BATTLE_HEIGHT = 720;
    
    public GamePanel(Main app, String menu) {
        this.mainApp = app;
        this.availableHeroes = new ArrayList<>();
        this.selectedTeam = new ArrayList<>();
        this.setName("GamePanel");

        initializeHeroes();
        setupUI(menu);
    }

    private void setupUI(String menu) {
        setLayout(new BorderLayout());
        setBackground(Theme.COLOR_BACKGROUND);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Theme.COLOR_BACKGROUND);
        
        JPanel heroSelectionScreen = createHeroSelectionScreenEnhanced();
        cardPanel.add(heroSelectionScreen, "HERO_SELECTION");
        
        JPanel battleScreen = new JPanel();
        battleScreen.setBackground(Theme.COLOR_BACKGROUND);
        cardPanel.add(battleScreen, "BATTLE");
        
        add(cardPanel, BorderLayout.CENTER);
        
        if(menu == "newgame"){
            mainApp.getDb().newGame(mainApp.getActiveUserId());
            showHeroSelection();
        }
        else{
            String[] listIdHero = mainApp.getDb().getLoadIdHero(mainApp.getActiveUserId());
            selectedTeam.add(Hero.createHero(listIdHero[0]));
            selectedTeam.add(Hero.createHero(listIdHero[1]));

            for (Hero hero : selectedTeam) {
                hero.setCurrentHp(mainApp.getDb().getLoadHp(mainApp.getActiveUserId(), hero.getIdCharacter()));
                hero.setCurrentMp(mainApp.getDb().getLoadMp(mainApp.getActiveUserId(), hero.getIdCharacter()));
            }
            int levelLoad = mainApp.getDb().loadCheckpoint(mainApp.getActiveUserId());

            startBattle(selectedTeam, levelLoad);
        }
    }
  
    private JPanel createHeroSelectionScreenEnhanced() {
        heroSelectionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, Theme.COLOR_BACKGROUND,
                    0, getHeight(), new Color(35, 35, 50)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Animated diagonal accent lines - Gold color
                g2.setColor(new Color(255, 215, 0, 15));
                g2.setStroke(new BasicStroke(2));
                for (int i = 0; i < getWidth(); i += 60) {
                    g2.drawLine(i, 0, i + 40, getHeight());
                }
            }
        };
        
        heroSelectionPanel.setLayout(new BorderLayout(20, 20));
        heroSelectionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // ===== TOP SECTION: Title =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        statusLabelHero = new JLabel("PILIH HERO 1 DARI 2");
        statusLabelHero.setFont(Theme.FONT_TITLE.deriveFont(32f));
        statusLabelHero.setForeground(Theme.COLOR_PRIMARY);
        statusLabelHero.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Pilih tim terbaikmu untuk bertarung");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE.deriveFont(14f));
        subtitleLabel.setForeground(Theme.COLOR_TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        topPanel.add(statusLabelHero, BorderLayout.NORTH);
        topPanel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        topPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        heroSelectionPanel.add(topPanel, BorderLayout.NORTH);
        
        // ===== CENTER SECTION: Hero Display + Stats =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setOpaque(false);
        
        JPanel imageFramePanel = createHeroImageFrame();
        centerPanel.add(imageFramePanel);
       
        JPanel statsAndInfoPanel = createStatsAndInfoPanel();
        centerPanel.add(statsAndInfoPanel);
        
        heroSelectionPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ===== BOTTOM SECTION: Navigation =====
        JPanel navigationPanel = createEnhancedNavigationPanel();
        heroSelectionPanel.add(navigationPanel, BorderLayout.SOUTH);
        
        displayCurrentHero();
        return heroSelectionPanel;
    }
    
    private JPanel createHeroImageFrame() {
        JPanel framePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         
                int width = getWidth();
                int height = getHeight();
                GradientPaint borderGradient = new GradientPaint(
                    0, 0, Theme.COLOR_PRIMARY,
                    width, height, Theme.COLOR_ACCENT
                );
                g2.setPaint(borderGradient);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(5, 5, width - 10, height - 10, 20, 20);
                
                // Inner glow - Gold
                g2.setColor(new Color(255, 215, 0, 20));
                g2.drawRoundRect(8, 8, width - 16, height - 16, 18, 18);
            }
        };
        
        framePanel.setLayout(new BorderLayout());
        framePanel.setOpaque(false);
        
        imageLabelHero = new JLabel();
        imageLabelHero.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabelHero.setVerticalAlignment(SwingConstants.CENTER);
        imageLabelHero.setBackground(Theme.COLOR_PANEL);
        imageLabelHero.setOpaque(true);
        
        framePanel.add(imageLabelHero, BorderLayout.CENTER);
        framePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        return framePanel;
    }
    
    private JPanel createStatsAndInfoPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
     
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        namePanel.setOpaque(false);
        
        heroNameLabel = new JLabel("Hero Name");
        heroNameLabel.setFont(Theme.FONT_TITLE.deriveFont(28f));
        heroNameLabel.setForeground(Theme.COLOR_PRIMARY);
        
        namePanel.add(Box.createHorizontalStrut(10));
        namePanel.add(heroNameLabel);
        
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // ===== STATS GRID =====
        statsPanelContainer = createEnhancedStatsPanel();
        mainPanel.add(statsPanelContainer);
        
        mainPanel.add(Box.createVerticalGlue());
        
        return mainPanel;
    }
    
    private JPanel createEnhancedStatsPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        
        // ===== MAIN STATS =====
        JPanel mainStatsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        mainStatsPanel.setOpaque(false);
        
        hpLabel = new JLabel("1000");
        mpLabel = new JLabel("500");
        atkLabel = new JLabel("100");
        defLabel = new JLabel("50");
        
        mainStatsPanel.add(createStatCardEnhanced("HP", hpLabel, new Color(50, 200, 50)));
        mainStatsPanel.add(createStatCardEnhanced("MP", mpLabel, Theme.COLOR_ACCENT));
        mainStatsPanel.add(createStatCardEnhanced("ATK", atkLabel, new Color(255, 100, 50)));
        mainStatsPanel.add(createStatCardEnhanced("DEF", defLabel, Theme.COLOR_PRIMARY));
        
        container.add(mainStatsPanel);
        container.add(Box.createVerticalStrut(15));
        
        // ===== COOLDOWNS =====
        JLabel cdTitleLabel = new JLabel("COOLDOWNS");
        cdTitleLabel.setFont(Theme.FONT_SUBTITLE.deriveFont(12f));
        cdTitleLabel.setForeground(Theme.COLOR_TEXT_SECONDARY);
        container.add(cdTitleLabel);
        container.add(Box.createVerticalStrut(8));
        
        JPanel cdPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        cdPanel.setOpaque(false);
        
        cd1Label = new JLabel("3T");
        cd2Label = new JLabel("4T");
        cdUltLabel = new JLabel("6T");
        
        cdPanel.add(createCooldownBadge("Skill1 (F)", cd1Label, Theme.COLOR_ACCENT));
        cdPanel.add(createCooldownBadge("Skill2 (S)", cd2Label, new Color(200, 100, 255)));
        cdPanel.add(createCooldownBadge("Ultimate (U)", cdUltLabel, Theme.COLOR_PRIMARY));
        
        container.add(cdPanel);
        return container;
    }
    
    private JPanel createStatCardEnhanced(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40),
                    0, h, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
                
                // Border
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 15, 15);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SUBTITLE.deriveFont(11f));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        valueLabel.setFont(Theme.FONT_HEADER.deriveFont(22f));
        valueLabel.setForeground(Theme.COLOR_TEXT);
        valueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createCooldownBadge(String title, JLabel cdLabel, Color color) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Badge background
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                
                // Border
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
            }
        };
        
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setOpaque(false);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BODY.deriveFont(9f));
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        cdLabel.setFont(Theme.FONT_HEADER.deriveFont(18f));
        cdLabel.setForeground(Theme.COLOR_TEXT);
        cdLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        badge.add(titleLabel);
        badge.add(Box.createVerticalStrut(2));
        badge.add(cdLabel);
        
        return badge;
    }
    
    private JPanel createEnhancedNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        
        // Left navigation
        JButton prevButton = createModernButton("PREVIOUS", Theme.COLOR_ACCENT);
        prevButton.addActionListener(e -> navigateHero(-1));
        
        panel.add(prevButton);
        panel.add(Box.createHorizontalGlue());
        
        // Center buttons
        JButton selectButton = createModernButton("SELECT HERO", new Color(50, 200, 100));
        selectButton.addActionListener(e -> selectCurrentHero());
        panel.add(selectButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // Right navigation
        JButton nextButton = createModernButton("NEXT", Theme.COLOR_ACCENT);
        nextButton.addActionListener(e -> navigateHero(1));
        panel.add(nextButton);
        
        panel.add(Box.createHorizontalGlue());
        
        // Back button
        JButton backBtn = createModernButton("BACK TO MENU", new Color(200, 100, 100));
        backBtn.addActionListener(e -> mainApp.showMainMenu());
        panel.add(backBtn);
        
        return panel;
    }
    
    private JButton createModernButton(String text, Color accentColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                if (getModel().isPressed()) {
                    // Pressed state
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 200));
                } else if (getModel().isArmed()) {
                    // Hover state
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 150));
                } else {
                    // Normal state
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 100));
                }
                
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                
                // Border - Gold or primary color
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
                
                // Text
                super.paintComponent(g);
            }
        };
        
        button.setPreferredSize(new Dimension(140, 50));
        button.setFont(Theme.FONT_SUBTITLE.deriveFont(12f));
        button.setForeground(Theme.COLOR_TEXT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        
        return button;
    }
    
    private void displayCurrentHero() {
        Hero hero = availableHeroes.get(currentHeroIndex);
        
        try {
            String imageUrl = hero.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL resourceUrl = loadAssets(imageUrl);
                
                if (resourceUrl != null) {
                    ImageIcon icon = new ImageIcon(resourceUrl);
                    Image image = icon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH);
                    imageLabelHero.setIcon(new ImageIcon(image));
                    imageLabelHero.setText("");
                    System.out.println("Hero image loaded successfully: " + imageUrl);
                } else {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image image = icon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH);
                    imageLabelHero.setIcon(new ImageIcon(image));
                    imageLabelHero.setText("");
                    System.out.println("Hero image loaded from direct path: " + imageUrl);
                }
            } else {
                imageLabelHero.setText("NO IMAGE");
                imageLabelHero.setForeground(new Color(255, 100, 100));
            }
        } catch (Exception e) {
            imageLabelHero.setText("IMAGE ERROR");
            imageLabelHero.setForeground(new Color(255, 100, 100));
            System.err.println("Error loading hero image: " + e.getMessage());
        }
        
        // Update hero info
        heroNameLabel.setText(hero.getName());
        
        hpLabel.setText(String.valueOf((int)hero.getMaxHp()));
        mpLabel.setText(String.valueOf((int)hero.getMaxMp()));
        atkLabel.setText(String.valueOf((int)hero.getAttack()));
        defLabel.setText(String.valueOf((int)hero.getDefense()));
        
        cd1Label.setText(hero.baseCooldowns.get("Skill1") + "T");
        cd2Label.setText(hero.baseCooldowns.get("Skill2") + "T");
        cdUltLabel.setText(hero.baseCooldowns.get("Ultimate") + "T");
        
        heroSelectionPanel.revalidate();
        heroSelectionPanel.repaint();
    }
    
    private void navigateHero(int direction) {
        currentHeroIndex = (currentHeroIndex + direction + availableHeroes.size()) % availableHeroes.size();
        displayCurrentHero();
    }
    
    private void selectCurrentHero() {
        if (selectedTeam.size() >= 2) {
            JOptionPane.showMessageDialog(this, "Pemilihan Selesai. Tim sudah penuh.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Hero currentHero = availableHeroes.get(currentHeroIndex);
        if (selectedTeam.contains(currentHero)) {
            JOptionPane.showMessageDialog(this, currentHero.getName() + " sudah dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        selectedTeam.add(currentHero);
        statusLabelHero.setText("PILIH HERO " + (selectedTeam.size() + 1) + " DARI 2");
        
        if (selectedTeam.size() == 2) {
            JOptionPane.showMessageDialog(this, "Tim Selesai! Memulai Battle.", "Ready", JOptionPane.INFORMATION_MESSAGE);
            mainApp.getDb().newGame(mainApp.getCurrentUserId());
            for (Hero hero : selectedTeam) {
                mainApp.getDb().saveCheckpoint(mainApp.getActiveUserId(), hero.getIdCharacter(), "M01", (int) hero.getMaxHp(), (int) hero.getMaxMp(), 1, true, "00:00:00");
            }
            startBattle(selectedTeam, 1);
        }
    }
    
    private void startBattle(List<Hero> team, int level) {
        loadSound();
        battleStartTime = System.currentTimeMillis();
        elapsedSeconds = 0;

        elapsedTimeTimer = new Timer(1000, evt -> {
            elapsedSeconds++;
            if (turnInfoLabel != null) {
                turnInfoLabel.setText("WAKTU: " + formatTime(elapsedSeconds) + " | " + activeHero.getName());
            }
            battlePanelContainer.repaint();
        });
        elapsedTimeTimer.start();
        this.playerTeam = team;
        this.activeHero = team.get(0);
        this.currentMonster = Monster.createMonster(level);
        this.turnCounter = 1;
        this.isTurnActive = true;
        
        loadBattleImages();
        
        JPanel battleScreen = createBattleScreen();
        cardPanel.add(battleScreen, "BATTLE");
        
        currentState = GameState.BATTLE;
        cardLayout.show(cardPanel, "BATTLE");
        
        setupCooldownTimer();
        
        logMessage("Battle dimulai melawan " + currentMonster.getName() + " di Level " + level);
        logMessage("Giliran: " + activeHero.getName());
    }
    
    private void loadBattleImages() {
        try {
            String heroImageUrl = activeHero.getImageUrl();
            if (heroImageUrl != null && !heroImageUrl.isEmpty()) {
                URL heroResourceUrl = loadAssets(heroImageUrl);
                
                if (heroResourceUrl != null) {
                    ImageIcon heroIcon = new ImageIcon(heroResourceUrl);
                    heroImage = heroIcon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                    System.out.println("Battle hero image loaded: " + heroImageUrl);
                } else {
                    ImageIcon heroIcon = new ImageIcon(heroImageUrl);
                    heroImage = heroIcon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                    System.out.println("Battle hero image loaded from direct path: " + heroImageUrl);
                }
            }
            
            String monsterImageUrl = currentMonster.getImageUrl();
            if (monsterImageUrl != null && !monsterImageUrl.isEmpty()) {
                URL monsterResourceUrl = loadAssets(monsterImageUrl);
                
                if (monsterResourceUrl != null) {
                    ImageIcon monsterIcon = new ImageIcon(monsterResourceUrl);
                    monsterImage = monsterIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
                    System.out.println("Battle monster image loaded: " + monsterImageUrl);
                } else {
                    ImageIcon monsterIcon = new ImageIcon(monsterImageUrl);
                    monsterImage = monsterIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
                    System.out.println("Battle monster image loaded from direct path: " + monsterImageUrl);
                }
            }
            
            URL bgResourceUrl = loadAssets("assets/battlebacground.png");
            if (bgResourceUrl != null) {
                ImageIcon bgIcon = new ImageIcon(bgResourceUrl);
                bgImage = bgIcon.getImage().getScaledInstance(BATTLE_WIDTH, BATTLE_HEIGHT, Image.SCALE_SMOOTH);
                System.out.println("Battle background loaded from resources");
            } else {
                System.err.println("Background image not found in resources");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading battle images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private JPanel createBattleScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(Theme.COLOR_BACKGROUND);
        
        battlePanelContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBattleScene((Graphics2D) g);
            }
        };
        battlePanelContainer.setPreferredSize(new Dimension(BATTLE_WIDTH, BATTLE_HEIGHT - 150));
        screen.add(battlePanelContainer, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(Theme.COLOR_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Theme.COLOR_PRIMARY));
        bottomPanel.setPreferredSize(new Dimension(BATTLE_WIDTH, 150));
        
        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setFont(Theme.FONT_BODY);
        battleLogArea.setBackground(new Color(0, 0, 0, 180));
        battleLogArea.setForeground(Theme.COLOR_TEXT);
        battleLogArea.setLineWrap(true);
        
        JScrollPane logScroll = new JScrollPane(battleLogArea);
        logScroll.setBorder(BorderFactory.createLineBorder(Theme.COLOR_PRIMARY, 1));
        logScroll.setPreferredSize(new Dimension(BATTLE_WIDTH / 3, 140));
        
        skillButtonPanel = createSkillButtonsPanel();
        
        turnInfoLabel = new JLabel("Giliran: " + activeHero.getName());
        turnInfoLabel.setForeground(Theme.COLOR_ACCENT);
        turnInfoLabel.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD, 12));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Theme.COLOR_BACKGROUND);
        infoPanel.add(turnInfoLabel, BorderLayout.NORTH);
        infoPanel.add(logScroll, BorderLayout.CENTER);
        
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(skillButtonPanel, BorderLayout.CENTER);
        
        screen.add(bottomPanel, BorderLayout.SOUTH);
        
        return screen;
    }
    
    private JPanel createSkillButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        panel.setBackground(Theme.COLOR_BACKGROUND);
        panel.setPreferredSize(new Dimension(BATTLE_WIDTH * 2 / 3, 140));
        
        panel.add(createSkillButton("BASIC ATTACK", "basic", 0, 0, 140, 60));
        panel.add(createSkillButton("ULTIMATE " + activeHero.baseCooldowns.get("Ultimate") + "s", "ultimate", 
            activeHero.baseCooldowns.get("Ultimate"), activeHero.skillCooldowns.get("Ultimate"), 120, 60));
        panel.add(createSkillButton("SKILL2 " + activeHero.baseCooldowns.get("Skill2") + "s", "skill2",
            activeHero.baseCooldowns.get("Skill2"), activeHero.skillCooldowns.get("Skill2"), 120, 60));
        panel.add(createSkillButton("SKILL1 " + activeHero.baseCooldowns.get("Skill1") + "s", "skill1",
            activeHero.baseCooldowns.get("Skill1"), activeHero.skillCooldowns.get("Skill1"), 120, 60));
        
        panel.add(createSkillButton("EXCHANGE", "exchange", 0, 0, 80, 60));
        panel.add(createSkillButton("REGEN HP", "regenHp",
            activeHero.baseCooldowns.get("RegenHp"), activeHero.skillCooldowns.get("RegenHp"), 80, 60));
        panel.add(createSkillButton("REGEN MP", "regenMp",
            activeHero.baseCooldowns.get("RegenMp"), activeHero.skillCooldowns.get("RegenMp"), 80, 60));
        panel.add(createSkillButton("AWAKENING", "awakening",
            activeHero.baseCooldowns.get("Awakening"), activeHero.skillCooldowns.get("Awakening"), 80, 60));
        panel.add(createSkillButton("ITEM", "item", 0, 0, 80, 60));

        return panel;
    }
    
    private JButton createSkillButton(String label, String actionCommand, int baseCd, int currentCd, int width, int height) {
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD, 11f));
        
        button.setBackground(new Color(0, 0, 0, 170));
        button.setForeground(Theme.COLOR_TEXT);
        button.setBorder(BorderFactory.createLineBorder(Theme.COLOR_PRIMARY, 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        boolean isOnCooldown = !actionCommand.equals("basic") && 
                              !actionCommand.equals("exchange") && 
                              currentCd > 0;
        
        if (isOnCooldown) {
            button.setBackground(Color.decode("#8B0000"));
            button.setText(label.split(" ")[0] + " CD: " + currentCd + "s");
        } else {
            button.setText(label);
        }
        
        button.setEnabled(isTurnActive && !isOnCooldown);
        button.addActionListener(e -> handleAction(actionCommand));
        
        return button;
    }
    
    private void drawBattleScene(Graphics2D g2) {
        int width = battlePanelContainer.getWidth();
        int height = battlePanelContainer.getHeight();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint bg = new GradientPaint(
            0, 0, Theme.COLOR_BACKGROUND,
            0, height, new Color(60, 20, 10)
        );
        g2.setPaint(bg);
        g2.fillRect(0, 0, width, height);
        
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, width, height, null);
        }
        
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRect(0, 0, width, height);
        
        if (heroImage != null) {
            g2.drawImage(heroImage, 50, height - 250, 200, 250, null);
        }
        
        if (monsterImage != null) {
            if(currentMonster.getName() == "Omicron"){
                g2.drawImage(monsterImage, width - 650, height - 470, 700, 500, null);
            }
            else if(currentMonster.getName() == "Omega"){
                g2.drawImage(monsterImage, width - 650, height - 620, 1000, 600, null);
            }
            else if(currentMonster.getName() == "Mu"){
                g2.drawImage(monsterImage, width - 1100, height - 620, 1000, 600, null);
            }
            
        }
        
        drawHPBar(g2, 30, 30, 300, 20, 
            activeHero.getName(), 
            (int)activeHero.getCurrentHp(), 
            (int)activeHero.getMaxHp(), 
            Color.GREEN);
        
        drawHPBar(g2, 30, 60, 250, 16,
            "MP",
            (int)activeHero.getCurrentMp(),
            (int)activeHero.getMaxMp(),
            Theme.COLOR_ACCENT);
        
        drawHPBar(g2, width - 430, 30, 400, 20,
            currentMonster.getName(),
            (int)currentMonster.getCurrentHp(),
            (int)currentMonster.getMaxHp(),
            Color.RED);
        
        g2.setColor(Theme.COLOR_PRIMARY);
        g2.setFont(Theme.FONT_SUBTITLE.deriveFont(16f));
        
        if (isTurnActive) {
            g2.setColor(Color.decode("#00FF00"));
            g2.drawString("[YOUR TURN]", width / 2 - 60, 65);
        } else {
            g2.setColor(Color.decode("#FF6600"));
            g2.drawString("[ENEMY TURN]", width / 2 - 65, 65);
        }
    }
    
    private void drawHPBar(Graphics2D g2, int x, int y, int width, int height, String name, int current, int max, Color color) {
        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(x, y, width, height, 10, 10);
        
        int hpWidth = (int) ((double) current / max * width);
        g2.setColor(color);
        g2.fillRoundRect(x, y, hpWidth, height, 10, 10);
        
        g2.setColor(Theme.COLOR_PRIMARY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 10, 10);
        
        g2.setColor(Theme.COLOR_TEXT);
        g2.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD, 12f));
        String text = name + " " + current + "/" + max;
        g2.drawString(text, x + 10, y + height - 4);
    }
    
    private void setupCooldownTimer() {
        cooldownTimer = new Timer(1000, e -> {
            for (String key : activeHero.skillCooldowns.keySet()) {
                int cd = activeHero.skillCooldowns.get(key);
                if (cd > 0) activeHero.skillCooldowns.put(key, cd - 1);
            }
            
            refreshSkillButtons();
            battlePanelContainer.repaint();
        });
        
        cooldownTimer.start();
    }
    
    private void handleAction(String action) {
        if (!isTurnActive) return;
        if (!activeHero.isAlive()) {
            logMessage(activeHero.getName() + " sudah pingsan. Ganti Hero terlebih dahulu.");
            return;
        }
        
        switch (action) {
            case "basic":
                logMessage(activeHero.getName() + " melakukan Basic Attack!");
                activeHero.attack(currentMonster);
                break;
                
            case "skill1":
                if (activeHero.skillCooldowns.get("Skill1") > 0) {
                    logMessage("Skill1 masih cooldown!");
                    return;
                }
                logMessage(activeHero.useSkill1(currentMonster));
                activeHero.skillCooldowns.put("Skill1", activeHero.baseCooldowns.get("Skill1"));
                break;
                
            case "skill2":
                if (activeHero.skillCooldowns.get("Skill2") > 0) {
                    logMessage("Skill2 masih cooldown!");
                    return;
                }
                logMessage(activeHero.useSkill2(currentMonster));
                activeHero.skillCooldowns.put("Skill2", activeHero.baseCooldowns.get("Skill2"));
                break;
                
            case "ultimate":
                if (activeHero.skillCooldowns.get("Ultimate") > 0) {
                    logMessage("Ultimate masih cooldown!");
                    return;
                }
                logMessage(activeHero.useUltimate(currentMonster));
                activeHero.skillCooldowns.put("Ultimate", activeHero.baseCooldowns.get("Ultimate"));
                break;
                
            case "regenHp":
                if (activeHero.skillCooldowns.get("RegenHp") > 0) {
                    logMessage("RegenHp masih cooldown!");
                    return;
                }
                activeHero.setCurrentHp(Math.min(activeHero.getMaxHp(), activeHero.getCurrentHp() + 200));
                logMessage(activeHero.getName() + " memulihkan 200 HP.");
                activeHero.skillCooldowns.put("RegenHp", activeHero.baseCooldowns.get("RegenHp"));
                break;
                
            case "regenMp":
                if (activeHero.skillCooldowns.get("RegenMp") > 0) {
                    logMessage("RegenMp masih cooldown!");
                    return;
                }
                activeHero.setCurrentMp(Math.min(activeHero.getMaxMp(), activeHero.getCurrentMp() + 200));
                logMessage(activeHero.getName() + " memulihkan 200 MP.");
                activeHero.skillCooldowns.put("RegenMp", activeHero.baseCooldowns.get("RegenMp"));
                break;
                
            case "exchange":
                exchangeHero();
                return;
            
            case "item":
                if(!mainApp.getDb().getStatusItem(mainApp.getActiveUserId(), activeHero.getIdCharacter())){
                    if(activeHero.getIdCharacter() == "H01"){
                        activeHero.BloodthirstAxe();
                    }
                    else if(activeHero.getIdCharacter() == "H02"){
                        activeHero.VampiricBlade();
                    }
                    else if(activeHero.getIdCharacter() == "H03"){
                        activeHero.DemonHunterSword();
                    }
                    else if(activeHero.getIdCharacter() == "H04"){
                        activeHero.CrimsonArmor();
                    }
                    else if(activeHero.getIdCharacter() == "H05"){
                        activeHero.EndlessWarboots();
                    }
                    else if(activeHero.getIdCharacter() == "H06"){
                        activeHero.WingsOfTheFallen();
                    }
                    logMessage("Item Spesial Digunakan!");
                    mainApp.getDb().saveStatusItem(mainApp.getActiveUserId(), activeHero.getIdCharacter());
                    break;
                }
                logMessage("Tidak ada Item yang tersedia!");
                return;
            
            case "awakening":
                if (activeHero.getCurrentHp() < activeHero.getMaxHp() * 0.15){
                    activeHero.activateAwakening();
                    logMessage("AWAKENINGGGG!");
                }
                logMessage("Awakening belum memenuhi syarat!!");
                
                return;
                
            default:
                logMessage("Aksi tidak dikenal: " + action);
                return;
        }
        
        isTurnActive = false;
        refreshSkillButtons();
        
        if (checkWinCondition()) return;
        
        monsterTurnSequence();
    }
    
    private void monsterTurnSequence() {
        Timer monsterTimer = new Timer(1500, evt -> {
            if (currentMonster.isAlive()) {
                monsterAction();
            }
            
            if (checkWinCondition()) return;
            
            nextTurn();
            isTurnActive = true;
            refreshSkillButtons();
            battlePanelContainer.repaint();
            ((Timer) evt.getSource()).stop();
        });
        monsterTimer.setRepeats(false);
        monsterTimer.start();
    }
    
    private void monsterAction() {
        if (!currentMonster.isAlive()) return;

        currentMonster.performAI(activeHero);
        currentMonster.updateTurn();
        logMessage(activeHero.getName() + " menerima kerusakan.");
    }
    
    private void exchangeHero() {
        int currentIndex = playerTeam.indexOf(activeHero);
        int nextIndex = (currentIndex + 1) % playerTeam.size();
        Hero newHero = playerTeam.get(nextIndex);
        
        if (!newHero.isAlive()) {
            logMessage(newHero.getName() + " pingsan! Tidak bisa diganti.");
            return;
        }
        
        activeHero = newHero;
        logMessage("Hero diganti! " + activeHero.getName() + " masuk ke pertarungan.");
        
        loadBattleImages();
        refreshSkillButtons();
        battlePanelContainer.repaint();
    }
    
    private void refreshSkillButtons() {
        if (skillButtonPanel != null) {
            skillButtonPanel.removeAll();
            skillButtonPanel.add(createSkillButton("BASIC ATTACK", "basic", 0, 0, 140, 60));
            skillButtonPanel.add(createSkillButton("ULTIMATE " + activeHero.baseCooldowns.get("Ultimate") + "s", "ultimate",
                activeHero.baseCooldowns.get("Ultimate"), activeHero.skillCooldowns.get("Ultimate"), 120, 60));
            skillButtonPanel.add(createSkillButton("SKILL2 " + activeHero.baseCooldowns.get("Skill2") + "s", "skill2",
                activeHero.baseCooldowns.get("Skill2"), activeHero.skillCooldowns.get("Skill2"), 120, 60));
            skillButtonPanel.add(createSkillButton("SKILL1 " + activeHero.baseCooldowns.get("Skill1") + "s", "skill1",
                activeHero.baseCooldowns.get("Skill1"), activeHero.skillCooldowns.get("Skill1"), 120, 60));
            
            skillButtonPanel.add(createSkillButton("EXCHANGE", "exchange", 0, 0, 80, 60));
            skillButtonPanel.add(createSkillButton("REGEN HP ", "regenHp",
                activeHero.baseCooldowns.get("RegenHp"), activeHero.skillCooldowns.get("RegenHp"), 80, 60));
            skillButtonPanel.add(createSkillButton("REGEN MP ", "regenMp",
                activeHero.baseCooldowns.get("RegenMp"), activeHero.skillCooldowns.get("RegenMp"), 80, 60));
            skillButtonPanel.add(createSkillButton("AWAKENING ", "awakening",
                activeHero.baseCooldowns.get("Awakening"), activeHero.skillCooldowns.get("Awakening"), 80, 60));
            skillButtonPanel.add(createSkillButton("ITEM", "item", 0, 0, 80, 60));

            skillButtonPanel.revalidate();
            skillButtonPanel.repaint();
        }
        
        turnInfoLabel.setText("WAKTU: " + formatTime(elapsedSeconds) + " | " + activeHero.getName());
    }
    
    private boolean checkWinCondition() {
        if (!currentMonster.isAlive()) {
            logMessage(currentMonster.getName() + " dikalahkan!");
            showNextLevelDialog();
            elapsedTimeTimer.stop();
            return true;
        }
        
        if (playerTeam.stream().allMatch(c -> !c.isAlive())) {
            logMessage("Semua Hero dikalahkan. GAME OVER.");
            JOptionPane.showMessageDialog(this, "GAME OVER! Tim Anda dikalahkan.", "Kalah", JOptionPane.ERROR_MESSAGE);
            cooldownTimer.stop();
            mainApp.showMainMenu();
            elapsedTimeTimer.stop();
            return true;
        }
        return false;
    }
    
    private void showNextLevelDialog() {
        cooldownTimer.stop();
        
        int nextLevel = mainApp.getDb().loadCheckpoint(mainApp.getActiveUserId()) + 1;
        Object[] options = {"Lanjut Level " + nextLevel, "Kembali ke Menu"};
        
        int choice = JOptionPane.showOptionDialog(
            this,
            "Selamat! Anda berhasil mengalahkan " + currentMonster.getName() + "!\n" +
            "Apakah Anda ingin melanjutkan ke Level " + nextLevel + "?",
            "Level Selesai!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        for (Hero hero : playerTeam) {
            mainApp.getDb().saveCheckpoint(mainApp.getCurrentUserId(), hero.getIdCharacter(), currentMonster.getIdCharacter(), (int) hero.getCurrentHp(), (int) hero.getCurrentMp(),nextLevel, mainApp.getDb().getStatusItem(mainApp.getActiveUserId(), hero.getIdCharacter()), formatTime(elapsedSeconds));
        }

        if (choice == JOptionPane.YES_OPTION) {
            resetHeroStateForNextLevel();
            startBattle(playerTeam, nextLevel);
        } else {
            mainApp.showMainMenu();
        }
        backgroundMusic.stop();
        elapsedTimeTimer.stop(); 
    }
    
    private void resetHeroStateForNextLevel() {
        for (Hero hero : playerTeam) {
            hero.skillCooldowns = new HashMap<>(hero.baseCooldowns);
        }
    }
    
    private void nextTurn() {
        turnCounter++;
    }
    
    private void logMessage(String message) {
        if (battleLogArea == null) return;
        battleLogArea.append(">> [" + formatTime(elapsedSeconds) + "] " + message + "\n");
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }
    
    private void initializeHeroes() {
        availableHeroes.add(Hero.createHero("H01"));
        availableHeroes.add(Hero.createHero("H02"));
        availableHeroes.add(Hero.createHero("H03"));
        availableHeroes.add(Hero.createHero("H04"));
        availableHeroes.add(Hero.createHero("H05"));
    }
    
    private URL loadAssets(String urlImage) {
        URL imageUrl = getClass().getClassLoader().getResource(urlImage);
        
        if (imageUrl != null) {
            System.out.println("Asset loaded successfully: " + urlImage);
            return imageUrl;
        } else {
            System.err.println("Asset not found in classpath: " + urlImage);
            return null;
        }
    }
    
    public void showHeroSelection() {
        currentState = GameState.HERO_SELECTION;
        selectedTeam.clear();
        currentHeroIndex = 0;
        statusLabelHero.setText("PILIH HERO 1 DARI 2");
        displayCurrentHero();
        cardLayout.show(cardPanel, "HERO_SELECTION");
    }

    private void loadSound() {
        try {
            String soundPath = "/assets/sound/background_music.wav";
            URL soundUrl = getClass().getResource(soundPath);
            
            if (soundUrl == null) {
                System.err.println("Sound file tidak ditemukan: " + soundPath);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Debug Music loaded and playing...");
        } catch (Exception e) {
            System.out.println("Debug exception: " + e);
        }
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    public void reset() {
        if (cooldownTimer != null) {
            cooldownTimer.stop();
        }
        showHeroSelection();
    }
}
