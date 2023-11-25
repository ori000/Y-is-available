import java.io.*;
import java.net.Socket;
// Other imports...

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream serverInput;
    private ObjectOutputStream serverOutput;

    boolean registered;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        try {
            // Initializing serverOutput first to ensure serverInput can read object headers sent by serverOutput
            serverOutput = new ObjectOutputStream(socket.getOutputStream());
            serverInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Object receivedObject;
            System.out.println("Handling a client...");
            while ((receivedObject = serverInput.readObject()) != null) {
                if (receivedObject instanceof String) {
                    String command = (String) receivedObject;
                    if (command.equals("REGISTER")) {
                        System.out.println("User Registration...");
                        User user = (User) serverInput.readObject();
                        registered = DatabaseConnector.registerUser(user);
                        serverOutput.writeBoolean(registered);
                        serverOutput.flush();
                    } else if (command.equals("LOGIN")){
                        // Handle LOGIN command
                    }
                    // Handle other commands...
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            if (serverOutput != null) serverOutput.close();
            if (serverInput != null) serverInput.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
