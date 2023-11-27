import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Map<String, ClientHandler> activeUsers ;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        try (FileWriter writer = new FileWriter("port.txt")) {
            writer.write(String.valueOf(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool = Executors.newFixedThreadPool(100); //number of similtanious clients
        activeUsers = new ConcurrentHashMap<>();
    }

    public void start() {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
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