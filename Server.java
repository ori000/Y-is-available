import java.io.*;
import java.net.*;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static ServerSocket serverSocket;
    private ExecutorService pool;
    private Map<String, ClientHandler> activeUsers;
    public static int port_number; //the server should have a well known port number

    public static void main(String[] args) {
        ServerConfig.PORT_NUMBER = Integer.parseInt(args[0]); // Take server port as an argument in the CLI
        try {  
            Server server = new Server(port_number);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server(int port_number) throws IOException {
        serverSocket = new ServerSocket(port_number); //create a server socket listening on given port
        pool = Executors.newFixedThreadPool(10); //max number of threads running
        activeUsers = new ConcurrentHashMap<>();
    }

    public void start() {
        System.out.println("Server started on port number: " + ServerConfig.PORT_NUMBER); //serverSocket.getLocalPort()
        try {
            while (true) {
                //Handling multiple clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client is connected : " + clientSocket);

                System.out.println("Assigning new thread for this client");
                ClientHandler clientHandler = new ClientHandler(clientSocket, serverSocket);
                pool.execute(clientHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    //function to stop the server from running
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

    public static int get_server_port_number() {
        System.out.println("Server port number: " + port_number);
        return port_number;
    }
}
