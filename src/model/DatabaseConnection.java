package src.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/sagaofarcaneswarriors";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "";    
    
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the database.");
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean login(String user, String pass) {
        String sql = "SELECT username FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass); 

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public String getIdUsers(String user) {
        String sql = "SELECT id_users FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String idUser = rs.getString("id_users");
                    System.out.println("DEBUG: id_user untuk username '" + user + "' = " + idUser);
                    return idUser; 
                } else {
                    System.out.println("DEBUG: Tidak ada user dengan username '" + user + "'");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getIdUsers: " + e.getMessage());
        }

        return null; 
    }

    public boolean register(String user, String pass) {
        String newId = String.valueOf(System.currentTimeMillis() % 10000); 
        String sql = "INSERT INTO users (id_users, username, password) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newId);
            pstmt.setString(2, user);
            pstmt.setString(3, pass);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            // Error code 1062 biasanya menunjukkan UNIQUE constraint violation (username sudah ada)
            if (e.getErrorCode() == 1062) { 
                System.err.println("Registration Error: Username already exists.");
            } else {
                System.err.println("Registration Error: " + e.getMessage());
            }
            return false;
        }
    }
    
    public void saveCheckpoint(String userId, String idHero, String idMonster,int currentHp, int currentMp, int bossLevel, boolean item) {

        String sql = "INSERT INTO storymode (id_user, id_hero, id_monster, current_hp, current_mp, duration, boss_level, item) " +
                    "VALUES (?, ?, ?, ?, ?, '00:00:00', ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "id_hero = VALUES(id_hero), " +
                    "id_monster = VALUES(id_monster), " +
                    "current_hp = VALUES(current_hp), " +
                    "current_mp = VALUES(current_mp), " +
                    "boss_level = VALUES(boss_level)";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);     
            pstmt.setString(2, idHero);     
            pstmt.setString(3, idMonster);  
            pstmt.setInt(4, currentHp);   
            pstmt.setInt(5, currentMp);     
            pstmt.setInt(6, bossLevel);
            pstmt.setBoolean(7, item);     

            pstmt.executeUpdate();
            System.out.println("Checkpoint saved for user " + userId);

        } catch (SQLException e) {
            System.err.println("Save Checkpoint Error: " + e.getMessage());
        }
    }


    public int loadCheckpoint(String userId) {
        String sql = "SELECT boss_level FROM storymode WHERE id_user = ? ORDER BY boss_level DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("boss_level");
                }
            }
        } catch (SQLException e) {
            System.err.println("Load Checkpoint Error: " + e.getMessage());
        }
        return 0; 
    }

    public void newGame(String userId) {
        String sql = "DELETE FROM storymode WHERE id_user = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("DEBUG: " + rowsDeleted + " row(s) deleted for userId = " + userId);

        } catch (SQLException e) {
            System.err.println("Error in newGame: " + e.getMessage());
        }
    }

    public int getLoadHp(String userId, String idHero){
        String sql = "SELECT current_hp FROM storymode WHERE id_user = ? and id_hero = ? ORDER BY boss_level DESC";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, userId);
            pstmt.setString(2, idHero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("current_hp");
                }
            }
        } catch (SQLException e) {
            System.err.println("Load Checkpoint Error: " + e.getMessage());
        }
        return 0; 
    }

    public int getLoadMp(String userId, String idHero){
        String sql = "SELECT current_mp FROM storymode WHERE id_user = ? and id_hero = ? ORDER BY boss_level DESC";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, userId);
            pstmt.setString(2, idHero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("current_mp");
                }
            }
        } catch (SQLException e) {
            System.err.println("Load Checkpoint Error: " + e.getMessage());
        }
        return 0; 
    }

    public String[] getLoadIdHero(String userId) {
        String sql = "SELECT id_hero FROM storymode WHERE id_user = ?";

        List<String> heroes = new ArrayList<>();

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
          
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {  
                    String idHero = rs.getString("id_hero");
                    System.out.println("DEBUG: id_hero = " + idHero);
                    heroes.add(idHero);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getLoadIdHero: " + e.getMessage());
        }
        
        return heroes.toArray(new String[0]);
    }


    public boolean getStatusItem(String userId, String idHero){
        String sql = "SELECT item FROM storymode WHERE id_user = ? and id_hero = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, userId);
            pstmt.setString(2, idHero);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("item") ==  0) {
                        return true;
                    }
                    else{
                        return false;
                    }
                    
                }
            }
        } catch (SQLException e) {
            System.err.println("Load Checkpoint Error: " + e.getMessage());
        }
        return false; 
    }

    public boolean saveStatusItem(String userId, String idHero){
        String sql = "UPDATE storymode SET item = FALSE WHERE id_user = ? and id_hero = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, userId);
            pstmt.setString(2, idHero);
        } catch (SQLException e) {
            System.err.println("Save Checkpoint Error: " + e.getMessage());
        }
        return false; 
    }

}