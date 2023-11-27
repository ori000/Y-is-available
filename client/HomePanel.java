import javax.swing.*;


import Shared.Dtos.*;
import Shared.Requests.AddPostRequest;
import Shared.Requests.BaseRequest;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class PostPanel extends JPanel {
    private JButton likeButton;
    private JButton commentButton;

    public PostPanel(ClientSocket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream,
            PostDto post) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        JPanel postContentPanel1 = new JPanel();
        Styles.stylePanel(postContentPanel1);
        JLabel postContent = new JLabel(post.getPostText());
        Styles.styleLabel(postContent);
        postContentPanel1.add(postContent);

        JPanel postContentPanel = new JPanel();
        Styles.stylePanel(postContentPanel);
        likeButton = new JButton("Like");
        Styles.styleButtonSmall(likeButton);

        commentButton = new JButton("Comment");
        Styles.styleButtonSmall(commentButton);

        postContentPanel.add(likeButton);
        postContentPanel.add(commentButton);

        // add panel borders padding and margins
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));

        add(postContentPanel1);
        add(postContentPanel);
    }
}

class NewPostPanel extends JPanel {
    private JButton postButton;
    private JTextArea postTextArea;

    public NewPostPanel(ClientSocket client_socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        JPanel postContentPanel = new JPanel();
        Styles.stylePanel(postContentPanel);

        postTextArea = new JTextArea("What's happening?");
        Styles.styleTextArea(postTextArea);
        postContentPanel.add(postTextArea);

        JPanel postButtonPanel = new JPanel();
        Styles.stylePanel(postButtonPanel);
        postButton = new JButton("Post");
        Styles.styleButtonSmall(postButton);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch data from text fields including the password
                String postContent = postTextArea.getText();

                AddPostRequest addPostRequest = new AddPostRequest(postContent, null);
                BaseRequest<AddPostRequest> postRequest = new BaseRequest(client_socket.getUserToken(), addPostRequest);

                // User Registration
                try {
                    // 1 - Create a socket and connect to the server
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address
                            + " and sending to port number: " + ClientSocket.client_port_number);

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("ADD_POST");
                    outputStream.writeObject(postRequest);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    boolean postedSuccess = (boolean) inputStream.readObject();

                    if (postedSuccess) {
                        JOptionPane.showMessageDialog(null, "Posted Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed Adding A Post", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        postButtonPanel.add(postButton);

        // add panel borders padding and margins
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));

        add(postContentPanel);
        add(postButtonPanel);
    }

}

public class HomePanel extends JPanel {
    public HomePanel(ClientSocket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setLayout(new BorderLayout());

        NewPostPanel newPostPanel = new NewPostPanel(clientSocket, outputStream, inputStream);

        // add a scroll pannel for posts
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add posts to the scroll pannel
        JPanel postsPanel = new JPanel();
        Styles.stylePanel(postsPanel);
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);
        postsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        postsPanel.add(newPostPanel);

        // get all posts from server
        List<UserDto> usersPosts = getPosts(clientSocket, outputStream, inputStream);

        // add posts to the posts panel
        for (UserDto user : usersPosts) {
            for (PostDto post : user.posts) {
                System.out.println(post.getPostText());
                PostPanel postPanel = new PostPanel(clientSocket, outputStream, inputStream, post);
                postsPanel.add(postPanel);
            }
        }

        scrollPane.setViewportView(postsPanel);

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // get all posts from server
    private List<UserDto> getPosts(ClientSocket client_socket, ObjectOutputStream outputStream,
            ObjectInputStream inputStream) {
        try {
            // 1 - Create a socket and connect to the server
            System.out.println("Client socket created with IP: " + client_socket.client_ip_address
                    + " and sending to port number: " + ClientSocket.client_port_number);

            // 3 - Send the User object to the server and tell the server to register
            outputStream.writeObject("GET_POSTS");
            outputStream.writeObject(client_socket.getUserToken());
            System.out.println("Getting posts...");

            // Read the response from the server
            List<UserDto> postList = (List<UserDto>) inputStream.readObject();

            return postList;
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return new ArrayList<UserDto>();
    }
    
}