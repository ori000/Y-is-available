import java.io.*;
import java.net.Socket;
import Shared.Dtos.*;
import Shared.Requests.*;

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
                System.out.println(receivedObject);
                if (receivedObject instanceof String) {
                    String command = (String) receivedObject;
                    switch (command) {
                        case "REGISTER":
                            System.out.println("User Registration...");
                            RegisterationRequest registerationRequest = (RegisterationRequest) serverInput.readObject();
                            registered = DatabaseConnector.registerUser(registerationRequest);
                            serverOutput.writeBoolean(registered);
                            serverOutput.flush();
                            break;
                        case "LOGIN":
                            System.out.println("User Login...");
                            LoginRequest loginRequest = (LoginRequest) serverInput.readObject();
                            boolean isValid = DatabaseConnector.validateLogin(loginRequest);
                            serverOutput.writeBoolean(isValid);
                            serverOutput.flush();
                            break;
                    
                        default:
                            break;
                    }
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
