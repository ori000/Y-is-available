import javax.swing.*;

import Shared.Requests.AddPostRequest;
import Shared.Requests.BaseRequest;

import Shared.Dtos.*;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class NavBarPanel extends JPanel {
    private JButton homeButton;
    private JButton exploreButton;
    private JButton notificationsButton;
    private JButton messagesButton;
    private JButton bookmarksButton;
    private JButton listsButton;
    private JButton profileButton;
    private JButton moreButton;

    public NavBarPanel(ClientSocket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        homeButton = new JButton("Home");
        Styles.styleNavBarButton(homeButton);

        exploreButton = new JButton("Explore");
        Styles.styleNavBarButton(exploreButton);

        notificationsButton = new JButton("Notifications");
        Styles.styleNavBarButton(notificationsButton);

        messagesButton = new JButton("Messages");
        Styles.styleNavBarButton(messagesButton);

        bookmarksButton = new JButton("Bookmarks");
        Styles.styleNavBarButton(bookmarksButton);

        listsButton = new JButton("Lists");
        Styles.styleNavBarButton(listsButton);

        profileButton = new JButton("Profile");
        Styles.styleNavBarButton(profileButton);

        moreButton = new JButton("More");
        Styles.styleNavBarButton(moreButton);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));

        add(homeButton);
        add(exploreButton);
        add(notificationsButton);
        add(messagesButton);
        add(bookmarksButton);
        add(listsButton);
        add(profileButton);
        add(moreButton);
    }
}

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

class SearchPage extends JFrame {

    public SearchPage(ClientSocket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setTitle("Search Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        Styles.stylePanel(searchPanel); // Apply your styling here

        // Scroll Panel for User Search Results
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        Styles.stylePanel(resultPanel); // Apply yosur styling here
        scrollPane.setViewportView(resultPanel);

        // Add components to the frame
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Search Button Action Listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                List<UserDto> searchResults = performSearch(clientSocket, outputStream, inputStream, searchText);

                // Update the result panel with search results
                resultPanel.removeAll();
                for (UserDto user : searchResults) {
                    JPanel userPanel = createUserPanel(user, clientSocket, outputStream, inputStream);
                    resultPanel.add(userPanel);
                }
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        });
        for (Component component : resultPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel userPanel = (JPanel) component;
                for (Component innerComponent : userPanel.getComponents()) {
                    if (innerComponent instanceof JLabel) {
                        JLabel clickableusername = (JLabel) innerComponent;
                        // Now you can use myLabel as needed
                        clickableusername.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                // On click, send user info to the backend
                                String clickedUsername = ((JLabel) e.getSource()).getText();
                                List<UserDto> userList = sendUserInfo(clientSocket, outputStream, inputStream,
                                        clickedUsername);
                                UserDto user = userList.get(0);
                                displayUserPosts(user);
                            }
                        });
                        break; // Break if you want to stop at the first JLabel found
                    }
                }
            }
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to perform search
    private List<UserDto> performSearch(ClientSocket client_socket, ObjectOutputStream outputStream,
            ObjectInputStream inputStream, String searchText) {
        try {
            outputStream.writeObject("SEARCH_USER");
            outputStream.writeObject(searchText);
            System.out.println("Searching for users: " + searchText);
            // Read the response from the server
            List<UserDto> userList = (List<UserDto>) inputStream.readObject();
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred while searching", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<UserDto>();
        }
    }

    private List<UserDto> sendUserInfo(ClientSocket client_socket, ObjectOutputStream outputStream,
            ObjectInputStream inputStream, String clickedUsername) {
        try {
            outputStream.writeObject("GET_USER_INFO");
            outputStream.writeObject(clickedUsername);
            System.out.println("Sending user info request for: " + clickedUsername);
            List<UserDto> userList = (List<UserDto>) inputStream.readObject();
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while sending user info", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<UserDto>();
        }
    }

    private JPanel createUserPanel(UserDto user, ClientSocket clientsocket, ObjectOutputStream outputStream,
            ObjectInputStream inputStream) {
        JPanel panel = new JPanel();
        JLabel usernameLabel = new JLabel(user.getUsername());
        panel.add(usernameLabel);
        // Add a mouse click listener to the JLabel
        return panel;
    }

    private void displayUserPosts(UserDto user) {
        JFrame postFrame = new JFrame(user.getUsername() + "'s Posts");
        postFrame.setLayout(new BorderLayout());
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));

        for (PostDto post : user.getPosts()) {
            JPanel singlePostPanel = new JPanel();
            singlePostPanel.setLayout(new BoxLayout(singlePostPanel, BoxLayout.Y_AXIS));
            singlePostPanel.add(new JLabel("Post: " + post.getPostText()));
            singlePostPanel.add(new JLabel("Date: " + post.getPostDate()));
            singlePostPanel.add(new JLabel("Likes: " + post.getLikes().size()));

            // Display Comments
            for (CommentDto comment : post.getComments()) {
                singlePostPanel.add(new JLabel("Comment: " + comment.getCommentText()));
            }

            postPanel.add(singlePostPanel);
            postPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between posts
        }

        JScrollPane scrollPane = new JScrollPane(postPanel);
        postFrame.add(scrollPane, BorderLayout.CENTER);
        postFrame.pack();
        postFrame.setVisible(true);
    }
}

class MainPage extends JFrame {

    public MainPage(ClientSocket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        NavBarPanel navBarPanel = new NavBarPanel(clientSocket, outputStream, inputStream);
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

        add(navBarPanel, BorderLayout.WEST);

        pack();
        setLocationRelativeTo(null);
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