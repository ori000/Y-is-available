
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DatabaseConnector {

    //Get the connection to the database
    public static Connection getConnection() throws SQLException {
        Env env = new Env(); //data structure containing info about the database connection MySQL
        String URL = env.getUrl(); 
        String USER = env.getUsername(); 
        String PASSWORD = env.getPassword(); 
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //Register a new user, return true if new user is added successfully to the database
    public static boolean registerUser (User user) throws NoSuchAlgorithmException {
    
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        String region = user.getRegion();
        String password = user.getPassword();
        String username = user.getUsername();

        String sql = "INSERT INTO users (first_name, last_name, email, phone_number, region, password, username) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //Connecting to the database and prepare to inject the sql statement
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Fill the sql insertion with user data
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, region);
            pstmt.setString(6, hashPassword(password)); // Hash the password
            pstmt.setString(7, username);
    
            int affectedRows = pstmt.executeUpdate(); //SQL injection
            return affectedRows > 0; //success if affected rows > 0

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Validate user login, return true if login is successful (user was already registered)
    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
    
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // After connecting to DB, search if user email already exists in the DB
            pstmt.setString(1, email);
    
            try (ResultSet resultSet = pstmt.executeQuery()) { //Executes the query and stores the result in a ResultSet.
                if (resultSet.next()) {  //if a result was found for the provided email.
                    String storedPassword = resultSet.getString("password");
                    String hashedPassword = hashPassword(password); // Hash the entered password
                    return storedPassword.equals(hashedPassword); // Compare the hashes, it true -> login successful
                }
                return false; // User not found
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //helper method to hash the password
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
