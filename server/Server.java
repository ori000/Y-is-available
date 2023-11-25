import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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
        sendPortNumber(); //send the port number to the client
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

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter server's port number: ");
        int port = scan.nextInt(); // Take server port as an argument in the CLI
        try {
            Server server = new Server(port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
