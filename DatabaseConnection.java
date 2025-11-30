import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/sagaofarcaneswarriors";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "";    
    
    /**
     * Mendapatkan objek Connection ke database MySQL.
     * @return Connection objek
     */
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
            pstmt.setString(2, pass); // *Catatan: Dalam aplikasi nyata, password harus di-hash (misalnya, MD5 atau bcrypt)*

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Jika ada baris (rs.next() == true), maka login berhasil
            }
        } catch (SQLException e) {
            System.err.println("Login Error: " + e.getMessage());
            return false;
        }
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
    
    public void saveCheckpoint(String userId, int bossLevel) {
        String sql = "INSERT INTO storymode (id_user, id_hero, current_hp, current_mp, duration, boss_level) " +
                     "VALUES (?, '001', 1, 1, '00:00:00', ?) " + 
                     "ON DUPLICATE KEY UPDATE boss_level = ?"; 

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String userIdStr = String.valueOf(userId);

            pstmt.setString(1, userIdStr);
            pstmt.setInt(2, bossLevel);
            pstmt.setInt(3, bossLevel); 
            
            pstmt.executeUpdate();
            System.out.println("Checkpoint saved successfully for user " + userIdStr + " at Boss Level " + bossLevel);

        } catch (SQLException e) {
            System.err.println("Save Checkpoint Error: " + e.getMessage());
        }
    }

    public int loadCheckpoint(String userId) {
        String sql = "SELECT boss_level FROM storymode WHERE id_user = ?";
        
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

}