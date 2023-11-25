
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import Shared.Dtos.*;
import Shared.Requests.*;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean registerUser(RegisterationRequest request) throws NoSuchAlgorithmException {
        String firstName = request.getFirstName(),
                lastName = request.getLastName(),
                email = request.getEmail(),
                phoneNumber = request.getPhoneNumber(),
                region = request.getRegion(),
                password = request.getPassword(),
                username = request.getUsername();
                
        String sql = "INSERT INTO users (first_name, last_name, email, phone_number, region, password, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, region);
            pstmt.setString(6, hashPassword(password)); // Hash the password
            pstmt.setString(7, username);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) System.out.println("User registered successfully!");
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("User registration failed!");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(LoginRequest request) throws InterruptedException{
        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, request.getEmail());

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    String hashedPassword = hashPassword(request.getPassword()); // Hash the entered password
                    System.out.println(storedPassword.equals(hashedPassword));
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


//region get the list of friends + posts

    //get the list of friends
    public List<UserDto> getFriends(String username) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "SELECT u.user_id, u.first_name, u.last_name, u.username, u.email, u.region, u.phone_number, u.user_numbers " +
                       "FROM Users u " +
                       "JOIN Friendships f ON (u.user_id = f.user1 OR u.user_id = f.user2) " +
                       "WHERE (f.user1 = ? OR f.user2 = ?) AND u.username != ?";

        List<UserDto> friends = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    UserDto friend = new UserDto(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        null,
                        resultSet.getString("region"),
                        resultSet.getString("phone_number"),
                        null);

                    friends.add(friend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    public void getPostsCommentsLikesForFriends(List<UserDto> friends) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String postQuery = "SELECT post_id, user_id, post_text, post_date FROM Posts WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (UserDto friend : friends) {
                // Retrieve posts
                try (PreparedStatement postStmt = conn.prepareStatement(postQuery)) {
                    postStmt.setInt(1, friend.getUserId());
                    try (ResultSet postResultSet = postStmt.executeQuery()) {
                        List<PostDto> posts = new ArrayList<>();
                        while (postResultSet.next()) {
                            // Retrieve comments for the post
                            List<CommentDto> comments = getCommentsForPost(conn, friend.getUserId(), postResultSet.getInt("post_id"));

                            // Retrieve likes for the post
                            List<Integer> likes = getLikesForPost(conn, friend.getUserId(), postResultSet.getInt("post_id"));
                            
                            //initialize the post
                            PostDto post = new PostDto(
                                    postResultSet.getInt("post_id"),
                                    postResultSet.getInt("user_id"),
                                    postResultSet.getString("post_text"),
                                    postResultSet.getString("post_date"),
                                    comments,
                                    likes,
                                    null
                                    );

                            // Retrieve comments for the post

                            posts.add(post);
                        }
                        friend.setPosts(posts);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<CommentDto> getCommentsForPost(Connection conn, int userId, int postId) throws SQLException {
        List<CommentDto> comments = new ArrayList<>();
        String commentQuery = "SELECT comment_id, post_id, user_id, comment_text, comment_date FROM Comments WHERE post_id IN (SELECT post_id FROM Posts WHERE user_id = ?)";
        try (PreparedStatement commentStmt = conn.prepareStatement(commentQuery)) {
            commentStmt.setInt(1, userId);
            try (ResultSet commentResultSet = commentStmt.executeQuery()) {
                while (commentResultSet.next()) {
                    CommentDto comment = new CommentDto(
                        commentResultSet.getInt("comment_id"),
                        commentResultSet.getInt("post_id"),
                        commentResultSet.getInt("user_id"),
                        commentResultSet.getString("comment_text"),
                        commentResultSet.getString("comment_date"));
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

    private List<Integer> getLikesForPost(Connection conn, int userId, int postId) throws SQLException {
        List<Integer> likes = new ArrayList<>();
        String likeQuery = "SELECT like_id, post_id, user_id FROM Likes WHERE post_id IN (SELECT post_id FROM Posts WHERE user_id = ?)";

        try (PreparedStatement likeStmt = conn.prepareStatement(likeQuery)) {
            likeStmt.setInt(1, userId);
            try (ResultSet likeResultSet = likeStmt.executeQuery()) {
                while (likeResultSet.next()) {
                    likes.add(likeResultSet.getInt("user_id"));
                }
            }
        }
        return likes;
    }

    //endregion
}
