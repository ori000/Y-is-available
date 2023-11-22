import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import javax.swing.JOptionPane;


public class ClientHandler implements Runnable  { //Implementing logic of server handling one client
    
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    ObjectInputStream serverInput; 
    ObjectOutputStream serverOutput; 
    //private String userName;

    public ClientHandler(Socket client, ServerSocket server) {
        // Initialize client-server connection
        this.clientSocket = client;
        this.serverSocket = server;
        System.out.println("Connection with server established...");
    }

    @Override
    public void run() {
        try {
            ObjectInputStream serverInput = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            
            Object receivedObject;
            while ((receivedObject = serverInput.readObject()) != null) {
                if (receivedObject instanceof String) {
                    String command = (String) receivedObject;
                    if (command.equals("REGISTER")) {
                        // If command is "REGISTER", expect a User object next
                        User user = (User) serverInput.readObject();
                        handleRegistration(user); // Modify this method according to your logic
                    } else if (command.equals("LOGIN")){

                    }
                    else if (command.equals("MESSAGE")){

                    }
                    else if (command.equals("LOGOUT")){

                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnections();
            // if (userName != null) {
            //     server.removeUser(userName);
            //     server.broadcastMessage(userName + " has logged out.", userName, false);
            // }
        }
    }

    //function to terminates connections
    private void closeConnections() {
        try {
            if (serverOutput != null) serverOutput.close();
            if (serverInput != null) serverInput.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRegistration(User user){
        boolean isRegistered = false; //Initially set to false
        try {
            isRegistered = DatabaseConnector.registerUser(user);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            serverOutput.writeBoolean(isRegistered); //send success/failure message to client
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /* 
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
*/
}
