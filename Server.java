import java.io.*;
import java.net.*;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.SwingUtilities;

public class Server {
    public static ServerSocket serverSocket;
    private ExecutorService pool;
    private Map<String, ClientHandler> activeUsers;
    public static int port_number; //the server should have a well known port number

    public static void main(String[] args) {
        int port_number = Integer.parseInt(args[0]); //taking the port number from the cmd line
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
        port_number = serverSocket.getLocalPort();
        System.out.println("Server started on port number: " + port_number); //serverSocket.getLocalPort()
        sendPortNumber();
        try {
            while (true) {
                //Handling multiple clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client is connected : " + clientSocket);

                System.out.println("Assigning new thread for this client");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                pool.execute(clientHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    //function to send the port number to the client
    public void sendPortNumber() {
        //create a temporary server socket to send the port number to the client
        try {
            ServerSocket tempServerSocket = new ServerSocket(5987);
            //send the port number to the client
            Socket tempSocket = tempServerSocket.accept();
            DataOutputStream output = new DataOutputStream(tempSocket.getOutputStream());
            output.writeInt(serverSocket.getLocalPort());
            output.flush();
            tempSocket.close();
            tempServerSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
