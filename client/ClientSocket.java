//Represents a client socket that connects to the server socket

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

// make one single instane
public class ClientSocket{
    InetAddress client_ip_address; //client IP address
    static int client_port_number; //host port number
    Socket socket; //client socket

    //create the client socket
    public ClientSocket() {
        try {
            this.client_ip_address = InetAddress.getByName("localhost");
            client_port_number = getServerPortNumber();
            this.socket = new Socket(client_ip_address, client_port_number);
            this.socket.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get the Server's port number
    public int getServerPortNumber() {
        try {
            Socket tsocket = new Socket(InetAddress.getByName("localhost"), 5987);
            DataInputStream tinput = new DataInputStream(tsocket.getInputStream());
            int port_numb = tinput.readInt();
            tinput.close();
            return port_numb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public OutputStream getOutputStream()   {
        try {
            return this.socket.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getInputStream() {
        try {
            return this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() throws IOException {
        this.socket.close();
    }
}
