import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import org.mindrot.jbcrypt.BCrypt;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Map<String, ClientHandler> activeUsers;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newFixedThreadPool(10);
        activeUsers = new ConcurrentHashMap<>();
    }

    public void start() {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                pool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, String senderUsername, boolean isPrivate) {
        activeUsers.forEach((username, clientHandler) -> {
            if (!isPrivate || clientHandler.isFriend(senderUsername)) {
                clientHandler.sendMessage(message);
            }
        });
    }

    public boolean authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yApp", "username", "password");
            String sql = "SELECT password FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return verifyPassword(password, storedHash);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close all connections
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean verifyPassword(String password, String storedHash) {
        try {
            return BCrypt.checkpw(password, storedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addUser(String username, ClientHandler clientHandler) {
        activeUsers.put(username, clientHandler);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    public static void main(String[] args) {
        int port = 1234; // Replace with the desired port
        try {
            Server server = new Server(port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
