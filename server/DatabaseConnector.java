
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
import java.util.UUID;

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

    public static String validateLogin(LoginRequest request) throws InterruptedException{
        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, request.getEmail());

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    String hashedPassword = hashPassword(request.getPassword()); // Hash the entered password
                    System.out.println(storedPassword.equals(hashedPassword));
                    if (storedPassword.equals(hashedPassword)) {
                        return addToken(request.getEmail());
                    }  // Compare the hashes
                }
                return ""; // User not found
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    // update user table to add the token to database ehere user.email = given email
    public static String addToken(String email) {
        String token = generateToken();
        String sql = "UPDATE users SET token = ? WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.setString(2, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) System.out.println("Token added successfully!");
            return token;
        } catch (SQLException e) {
            System.out.println("Token addition failed!");
            e.printStackTrace();
            return null;
        }
    }

    // generate user token
    public static String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    // invalidate user token by removing it from database   
    private static void invalidateUserToken(String token){
        String sql = "UPDATE users SET token = NULL WHERE token = ?";

        try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, token);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) System.out.println("Token removed successfully!");
        } catch (SQLException e) {
            System.out.println("Token removal failed!");
            e.printStackTrace();
        }
    }

    // logout
    public static void logout(String username) {
        invalidateUserToken(username);
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

// region Edit User Information
    
    public static boolean editUser(BaseRequest<EditUserRequest> request) {
            Env env = new Env();
            String URL = env.getUrl();
            String USER = env.getUsername();
            String PASSWORD = env.getPassword();
    
            String query = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, region = ?, phone_number = ? WHERE username = ?";
    
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
                
                EditUserRequest editUserRequest = request.getPayLoad();

                stmt.setString(1, editUserRequest.getFirstName());
                stmt.setString(2, editUserRequest.getLastName());
                stmt.setString(3, editUserRequest.getEmail());
                stmt.setString(4, editUserRequest.getRegion());
                stmt.setString(5, editUserRequest.getPhoneNumber());
                stmt.setString(6, editUserRequest.getUsername());
    
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) System.out.println("User edited successfully!");
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("User edit failed!");
                e.printStackTrace();
                return false;
            }
        }


// region Get User By Username
    public static UserDto getUser(String usernameOrToken) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "SELECT user_id, first_name, last_name, username, email, region, phone_number FROM Users WHERE username = ? OR token = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usernameOrToken);
            stmt.setString(2, usernameOrToken);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    UserDto user = new UserDto(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        null,
                        resultSet.getString("region"),
                        resultSet.getString("phone_number"),
                        null);

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

   
//region Add A Post
    public static boolean addPost(BaseRequest<AddPostRequest> addPostRequest) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "INSERT INTO Posts (user_id, post_text) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            

            var user_id = getUser(addPostRequest.getToken()).getUserId();

            stmt.setInt(1, user_id);
            stmt.setString(2, addPostRequest.getPayLoad().getPostText());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) System.out.println("Post added successfully!");
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Post addition failed!");
            e.printStackTrace();
            return false;
        }
    }


//region Add A Comment
    public static boolean addComment(BaseRequest<AddCommentRequest> addCommentRequest) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "INSERT INTO Comments (post_id, user_id, comment_text) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            var user_id = getUser(addCommentRequest.getToken()).getUserId();
            
            stmt.setInt(1, addCommentRequest.getPayLoad().getPostID());
            stmt.setInt(2, user_id);
            stmt.setString(3, addCommentRequest.getPayLoad().getComment());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) System.out.println("Comment added successfully!");
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Comment addition failed!");
            e.printStackTrace();
            return false;
        }
    }

//add Like 


//region get the list of friends + posts

    //get the list of friends
    public static List<UserDto> getFriends(String token) {

        String query = "SELECT u.user_id, u.first_name, u.last_name, u.username, u.email, u.region, u.phone_number " +
                       "FROM Users u " +
                       "JOIN Friendships f ON (u.user_id = f.user1 OR u.user_id = f.user2) " +
                       "WHERE (f.user1 = ? OR f.user2 = ?)";

        List<UserDto> friends = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            int user_id = getUser(token).getUserId();
            
            stmt.setInt(1, user_id);
            stmt.setInt(2, user_id);
            // stmt.setString(3, username);

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
        System.out.println(friends.size());
        return friends;
    }

    public static boolean addNewPeople(BaseRequest<AddNewPeopleRequest> addNewPeopleRequest) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "INSERT INTO Friendships (user1, user2) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)) {

        var user_id = getUser(addNewPeopleRequest.getToken()).getUserId();
        
        stmt.setInt(1, user_id);
        stmt.setInt(2, addNewPeopleRequest.getPayLoad().getUser2ID());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) System.out.println("Friend added successfully!");
        return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("Friend addition failed!");
            e.printStackTrace();
            return false;
        }
    }

    public static List<UserDto> getPostsCommentsLikesForUsers(List<UserDto> users) {
        List<UserDto> result = users;

        String postQuery = "SELECT post_id, user_id, post_text, post_date FROM Posts WHERE user_id = ?";
        
        try (Connection conn = getConnection()) {

            for(int i = 0; i < result.size(); i++) {

                // Retrieve posts
                try (PreparedStatement postStmt = conn.prepareStatement(postQuery)) {

                    postStmt.setInt(1, users.get(i).getUserId());

                    try (ResultSet postResultSet = postStmt.executeQuery()) {

                        List<PostDto> posts = new ArrayList<>();

                        while (postResultSet.next()) {

                            // Retrieve comments for the post
                            List<CommentDto> comments = getCommentsForPost(conn, users.get(i).getUserId(), postResultSet.getInt("post_id"));

                            // Retrieve likes for the post
                            List<Integer> likes = getLikesForPost(conn, users.get(i).getUserId(), postResultSet.getInt("post_id"));
                            
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
                        result.get(i).setPosts(posts);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //get the list of new people
    public static List<UserDto> getNewPeople(String token) {

        String query = "SELECT u.user_id, u.first_name, u.last_name, u.username, u.email, u.region, u.phone_number " +
                       "FROM Users u " +
                       "JOIN Friendships f ON (u.user_id = f.user1 OR u.user_id = f.user2) " +
                       "WHERE (f.user1 != ? OR f.user2 != ?)";

        List<UserDto> newPeople = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            int user_id = getUser(token).getUserId();
            
            stmt.setInt(1, user_id);
            stmt.setInt(2, user_id);
            // stmt.setString(3, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    UserDto newPerson = new UserDto(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        null,
                        resultSet.getString("region"),
                        resultSet.getString("phone_number"),
                        null);

                    newPeople.add(newPerson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(newPeople.size());
        return newPeople;
    }

    private static List<CommentDto> getCommentsForPost(Connection conn, int userId, int postId) throws SQLException {
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

    private static List<Integer> getLikesForPost(Connection conn, int userId, int postId) throws SQLException {
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
