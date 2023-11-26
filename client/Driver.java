import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Driver {
    // a driver class thet creates a single socket and calls registration form


    public static void main(String[] args) throws IOException {
        // create a socket
        ClientSocket socket = new ClientSocket();

        // // 2 - Create output-input stream to send-receive the user object to the server
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        

        // run registration form
        new RegistrationForm(socket, outputStream, inputStream);
        
        // PostsPage postsPage = new PostsPage();
        // new MainPage(socket, outputStream, inputStream);
    }
}
