import java.io.*;
import java.net.Socket;
import java.util.List;

import Shared.Dtos.*;
import Shared.Requests.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream serverInput;
    private ObjectOutputStream serverOutput;

    boolean registered;

    public ClientHandler(Socket socket) {
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
                            String token = DatabaseConnector.validateLogin(loginRequest);
                            serverOutput.writeObject(token);
                            serverOutput.flush();
                            break;
                        case "GET_POSTS":
                            System.out.println("Getting Posts...");
                            token = (String) serverInput.readObject();
                            List<UserDto> friendsList = DatabaseConnector.getFriends(token);
                            List<UserDto> postsResponse = DatabaseConnector.getPostsCommentsLikesForUsers(friendsList);
                            serverOutput.writeObject(postsResponse);
                            serverOutput.flush();
                            break;
                        case "GET_USER":
                            System.out.println("Getting User...");
                            String usernameToGet = (String) serverInput.readObject();
                            UserDto userResponse = DatabaseConnector.getUser(usernameToGet);
                            serverOutput.writeObject(userResponse);
                            serverOutput.flush();
                            break;
                        case "GET_FRIENDS":
                            System.out.println("Getting Friends...");
                            String usernameToGetFriends = (String) serverInput.readObject();
                            List<UserDto> friendsResponse = DatabaseConnector.getFriends(usernameToGetFriends);
                            serverOutput.writeObject(friendsResponse);
                            serverOutput.flush();
                            break;
                        case "ADD_POST":
                            System.out.println("Adding Post...");
                            BaseRequest<AddPostRequest> addPostRequest = (BaseRequest<AddPostRequest>) serverInput.readObject();
                            boolean addPostResponse = DatabaseConnector.addPost(addPostRequest);
                            serverOutput.writeObject(addPostResponse);
                            serverOutput.flush();
                            break;
                        case "ADD_COMMENT":
                            System.out.println("Adding Comment...");
                            BaseRequest<AddCommentRequest> addCommentRequest = (BaseRequest<AddCommentRequest>) serverInput.readObject();
                            boolean addCommentResponse = DatabaseConnector.addComment(addCommentRequest);
                            serverOutput.writeObject(addCommentResponse);
                            serverOutput.flush();
                            break;
                        case "GET_NEW_PEOPLE":
                            System.out.println("Getting New People...");
                            String usernameToGetNewPeople = (String) serverInput.readObject();
                            List<UserDto> getNewPeopleResponse = DatabaseConnector.getNewPeople(usernameToGetNewPeople);
                            serverOutput.writeObject(getNewPeopleResponse);
                            serverOutput.flush();
                            break;
                        case "ADD_NEW_PEOPLE":
                            System.out.println("Adding Friend...");
                            BaseRequest<AddNewPeopleRequest> addNewPeopleRequest = (BaseRequest<AddNewPeopleRequest>) serverInput.readObject();
                            boolean addNewPeopleResponse = DatabaseConnector.addNewPeople(addNewPeopleRequest);
                            serverOutput.writeObject(addNewPeopleResponse);
                            serverOutput.flush();
                            break;
                        case "ADD_REACTION":
                            System.out.println("Adding Like...");
                            BaseRequest<AddReactionRequest> addReactionRequest = (BaseRequest<AddReactionRequest>) serverInput.readObject(); 
                            DatabaseConnector.handleAddReaction(addReactionRequest);
                            serverOutput.writeObject(true);
                            serverOutput.flush();
                            break;
                        case "LOGOUT":
                            System.out.println("Logging out...");
                            String usernameToLogout = (String) serverInput.readObject();
                            DatabaseConnector.logout(usernameToLogout);
                            serverOutput.writeObject(true);
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
