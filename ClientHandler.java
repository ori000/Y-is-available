import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String userName;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] tokens = inputLine.split(" ");
                String command = tokens[0].toUpperCase();

                switch (command) {
                    case "LOGIN":
                        handleLogin(tokens);
                        break;
                    case "MESSAGE":
                        handleMessage(tokens);
                        break;
                    case "LOGOUT":
                        handleLogout();
                        return; // Exit the loop and end the thread
                    // Additional commands can be handled here
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
            if (userName != null) {
                server.removeUser(userName);
                server.broadcastMessage(userName + " has logged out.", userName, false);
            }
        }
    }

    private void handleLogin(String[] tokens) {
        if (tokens.length == 3) {
            String username = tokens[1];
            String password = tokens[2];

            if (server.authenticateUser(username, password)) {
                this.userName = username;
                server.addUser(username, this);
                sendMessage("Login successful");
                server.broadcastMessage(username + " has logged in.", username, false);
            } else {
                sendMessage("Login failed");
            }
        }
    }

    private void handleMessage(String[] tokens) {
        if (tokens.length >= 3) {
            String message = tokens[1];
            boolean isPrivate = Boolean.parseBoolean(tokens[2]);
            server.broadcastMessage(message, this.userName, isPrivate);
        }
    }

    private void handleLogout() {
        server.removeUser(userName);
        closeConnections();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnections() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFriend(String otherUsername) {
        Env env = new Env();
        String URL = env.getUrl();
        String USER = env.getUsername();
        String PASSWORD = env.getPassword();

        String query = "SELECT COUNT(*) FROM Friendships WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, this.userName);
            stmt.setString(2, otherUsername);
            stmt.setString(3, otherUsername);
            stmt.setString(4, this.userName);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
