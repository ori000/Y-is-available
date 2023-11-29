import javax.swing.*;


import Shared.Dtos.*;
import Shared.Enums.ReactionType;
import Shared.Requests.AddCommentRequest;
import Shared.Requests.AddNewPeopleRequest;
import Shared.Requests.AddCommentRequest;
import Shared.Enums.ReactionType;
import Shared.Requests.AddPostRequest;
import Shared.Requests.AddReactionRequest;
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
    private JButton commentButton;
    private JTextArea commentTextArea;
    private JButton submitCommentButton;
    private boolean isCommentAreaVisible = false;

    public PostPanel(ClientSocket client_socket, ObjectOutputStream outputStream, ObjectInputStream inputStream,
            PostDto post) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        commentTextArea = new JTextArea(3, 20);
        Styles.styleTextArea(commentTextArea);
        commentTextArea.setVisible(false);

        submitCommentButton = new JButton("Submit Comment");
        Styles.styleButtonSmall(submitCommentButton);
        submitCommentButton.setVisible(false);

        JPanel postContentPanel1 = new JPanel();
        Styles.stylePanel(postContentPanel1);
        JLabel postContent = new JLabel(post.getPostText());
        Styles.styleLabel(postContent);
        postContentPanel1.add(postContent);

        // add comments in jlabels
        JPanel commentsPanel = new JPanel();
        Styles.stylePanel(commentsPanel);
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(Color.WHITE);
        commentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        for (CommentDto comment : post.getComments()) {
            JLabel commentLabel = new JLabel(comment.getCommentText());
            Styles.styleLabel(commentLabel);
            commentsPanel.add(commentLabel);
        }

        postContentPanel1.add(commentsPanel);
        

        JPanel postContentPanel = new JPanel();
        Styles.stylePanel(postContentPanel);
        JComboBox<ReactionType> reactionButton = new JComboBox<ReactionType>();

        UserDto user = client_socket.getUserInfoByToken(outputStream, inputStream);

        reactionButton.addItem(ReactionType.LIKE);
        reactionButton.addItem(ReactionType.LOVE);
        reactionButton.addItem(ReactionType.LAUGH);
        reactionButton.addItem(ReactionType.SAD);
        reactionButton.addItem(ReactionType.ANGRY);


        reactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReactionType reactionType = (ReactionType) reactionButton.getSelectedItem();
                System.out.println(reactionType);
                //send reaction to server
                try {
                    // 1 - Create a socket and connect to the server
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address + " and sending to port number: " + ClientSocket.client_port_number);
                    
                    BaseRequest<AddReactionRequest> addReactionRequest = new BaseRequest(client_socket.getUserToken(), new AddReactionRequest(post.getPostId(), reactionType));
                    

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("ADD_REACTION");
                    outputStream.writeObject(addReactionRequest);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    boolean reactedPosted = (boolean) inputStream.readObject();
                    
                    if (reactedPosted) {
                        JOptionPane.showMessageDialog(null, "Reacted Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed Reacting", "Error", JOptionPane.ERROR_MESSAGE);                
                    }
                } 
                catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }
        });


        commentButton = new JButton("Comment");
        Styles.styleButtonSmall(commentButton);


        //set value of the reaction button to the user's reaction that he already reacted with
        for (ReactionDto reaction : post.getReactions()) {
            System.out.println(reaction.getReactionType());
            System.out.println(reaction.getUserId()+ "" + user.getUserId());
            if (reaction.getUserId() == user.getUserId()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        reactionButton.setSelectedItem(reaction.getReactionType());
                        reactionButton.setToolTipText(String.valueOf(reactionButton.getSelectedItem()));
                    }
                });
            }
        }

        postContentPanel.add(reactionButton);
        postContentPanel.add(commentButton);
               postContentPanel.add(submitCommentButton);
        postContentPanel.add(commentTextArea);

        submitCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentTextArea.getText();

                AddCommentRequest addCommentRequest = new AddCommentRequest(post.getPostId(), comment);
                BaseRequest<AddCommentRequest> commentRequest = new BaseRequest(client_socket.getUserToken(), addCommentRequest);

                // User Registration
                try {
                    // 1 - Create a socket and connect to the server
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address + " and sending to port number: " + ClientSocket.client_port_number);

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("ADD_COMMENT");
                    outputStream.writeObject(commentRequest);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    boolean commentedSuccess = (boolean) inputStream.readObject();
                    
                    if (commentedSuccess) {
                        JOptionPane.showMessageDialog(null, "Comment posted Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed Adding A Comment", "Error", JOptionPane.ERROR_MESSAGE);                
                    }
                } 
                catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                
                // Resetting commentTextArea
                commentTextArea.setText("");
                commentTextArea.setVisible(false);
            }
        });

        commentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCommentAreaVisible = !isCommentAreaVisible;
                commentTextArea.setVisible(isCommentAreaVisible);
                submitCommentButton.setVisible(isCommentAreaVisible);
            }
        });

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

class NewPeoplePanel extends JPanel {

    public NewPeoplePanel(ClientSocket client_socket, ObjectOutputStream outputStream, ObjectInputStream inputStream, List<UserDto> users) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(RIGHT_ALIGNMENT);

        for (UserDto user : users) {
            JPanel userPanel = new JPanel();
            Styles.stylePanel(userPanel);

            JLabel usernameLabel = new JLabel(user.getUsername());
            Styles.styleLabel(usernameLabel);
            userPanel.add(usernameLabel);

            JButton followButton = new JButton("Follow");
            Styles.styleButtonSmall(followButton);

            followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                AddNewPeopleRequest addNewPeopleRequest = new AddNewPeopleRequest(user.getUserId());
                BaseRequest<AddNewPeopleRequest> addFriendRequest = new BaseRequest(client_socket.getUserToken(), addNewPeopleRequest);

                // User Registration
                try {
                    // 1 - Create a socket and connect to the server
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address + " and sending to port number: " + ClientSocket.client_port_number);

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("ADD_NEW_PEOPLE");
                    outputStream.writeObject(addFriendRequest);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    boolean friendSuccess = (boolean) inputStream.readObject();
                    
                    if (friendSuccess) {
                        JOptionPane.showMessageDialog(null, "Sent Friend Request Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed Adding A Friend", "Error", JOptionPane.ERROR_MESSAGE);                
                    }
                } 
                catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }
        });
            //insert to the friendships table base request containing the token and use it to get the current user and add this user and any of the chosen users into the friendships table
            userPanel.add(followButton);

            add(userPanel);
        }

        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));
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

        List<UserDto> newPeople = getNewPeople(clientSocket, outputStream, inputStream);
        NewPeoplePanel newPeoplePanel = new NewPeoplePanel(clientSocket, outputStream, inputStream, newPeople);

        scrollPane.setViewportView(postsPanel);

        add(scrollPane, BorderLayout.CENTER);


        add (newPeoplePanel, BorderLayout.EAST);

        // each 10 seconds refresh 
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //remove all posts
                postsPanel.removeAll();
                newPeoplePanel.removeAll();

                remove(newPeoplePanel);
                
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
                
                add (newPeoplePanel, BorderLayout.EAST);

                postsPanel.revalidate();
                postsPanel.repaint();
            }
        });

        timer.start();

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

    private List<UserDto> getNewPeople(ClientSocket client_socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        try {
            // 1 - Create a socket and connect to the server
            System.out.println("Client socket created with IP: " + client_socket.client_ip_address + " and sending to port number: " + ClientSocket.client_port_number);
            
            

            // 3 - Send the User object to the server and tell the server to register
            outputStream.writeObject("GET_NEW_PEOPLE");
            outputStream.writeObject(client_socket.getUserToken());
            System.out.println("Getting new people...");

            // Read the response from the server
            List<UserDto> newPeopleList = (List<UserDto>) inputStream.readObject();
            
            return newPeopleList;
        } 
        catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        } 
        return new ArrayList<UserDto>();  
    }
    
}