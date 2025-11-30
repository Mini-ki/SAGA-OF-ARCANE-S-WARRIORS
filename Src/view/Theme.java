package Src.view;

import java.awt.*;
import javax.swing.*;

public class Theme {
    
    // Warna Nuansa Dark & Gold
    public static final Color COLOR_BACKGROUND = new Color(25, 25, 35);
    public static final Color COLOR_PANEL = new Color(40, 40, 50);
    public static final Color COLOR_PRIMARY = new Color(255, 215, 0); 
    public static final Color COLOR_ACCENT = new Color(0, 200, 255);  
    public static final Color COLOR_TEXT = new Color(240, 240, 240);
    public static final Color COLOR_TEXT_SECONDARY = new Color(180, 180, 180);
    
    // Font
    public static final Font FONT_HEADER = new Font("Serif", Font.BOLD, 42); 
    public static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 42);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    
    // Style Helper untuk Tombol
    public static void styleButton(JButton btn) {
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(new Color(20, 20, 30));
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 240, 100), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Style Helper untuk Input Text
    public static void styleTextField(JTextField field) {
        field.setOpaque(false);
        field.setForeground(COLOR_ACCENT);
        field.setCaretColor(COLOR_PRIMARY);
        field.setFont(new Font("Monospaced", Font.BOLD, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}