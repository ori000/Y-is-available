import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Driver {
    // a driver class thet creates a single socket and calls registration form

    public static void main(String[] args) throws IOException {
        // 1 - Create a socket client 
        ClientSocket socket = new ClientSocket();
        System.out.println(socket.socket.toString());


        // 2 - Create output-input stream to send-receive the user object to the server
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.socket.getInputStream());
        new RegistrationForm(socket, outputStream, inputStream);
    }
}
