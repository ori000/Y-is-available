import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean registerUser(String firstName, String lastName, String email, String phoneNumber, String region, String password) throws NoSuchAlgorithmException {
        String sql = "INSERT INTO users (first_name, last_name, email, phone_number, region, password) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, region);
            pstmt.setString(6, hashPassword(password)); // Hash the password
    
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
    
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
    
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    String hashedPassword = hashPassword(password); // Hash the entered password
                    return storedPassword.equals(hashedPassword); // Compare the hashes
                }
                return false; // User not found
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] hashedPassword = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
