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

class SearchPanel extends JPanel {

    public SearchPanel(ClientSocket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
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
    }

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
