import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigInteger;

public class PasswordResetTokenManager {

    private static SecureRandom random = new SecureRandom();

    public static String generateToken() {
        return new BigInteger(130, random).toString(32);
    }

    public static void saveToken(String email, String token) {
        String sql = "INSERT INTO password_reset_tokens (email, token, expiration_time) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE token = ?, expiration_time = ?";

        // Set the token to expire in 1 hour
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        String formattedExpirationTime = expirationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, token);
            pstmt.setString(3, formattedExpirationTime);
            pstmt.setString(4, token); // For the update case
            pstmt.setString(5, formattedExpirationTime); // For the update case

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
