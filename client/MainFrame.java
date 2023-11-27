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

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();

    public MainFrame(ClientSocket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setTitle("Main Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(800, 600));

        // Sidebar panel
        JPanel sidebarPanel = new JPanel(new GridBagLayout());
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margins for the sidebar
        sidebarPanel.setBackground(Color.LIGHT_GRAY); // Background color for the sidebar

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End of row
        gbc.fill = GridBagConstraints.HORIZONTAL; // Horizontal fill
        gbc.insets = new Insets(5, 0, 5, 0); // Padding between buttons

        // Central panel with CardLayout
        JPanel centralPanel = new JPanel(cardLayout);
        centralPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Border to differentiate from sidebar

        // Add Home and Profile panels
        HomePanel homePanel = new HomePanel();
        ProfilePanel profilePanel = new ProfilePanel();
        FriendsPanel friendsPanel = new FriendsPanel();
        SearchPanel searchPanel = new SearchPanel(clientSocket, outputStream, inputStream);
        centralPanel.add(homePanel, "Home");
        centralPanel.add(profilePanel, "Profile");
        centralPanel.add(friendsPanel, "Friends");
        centralPanel.add(searchPanel, "Search");

        // Add spacer at the top with weight
        gbc.weighty = 1;
        sidebarPanel.add(Box.createGlue(), gbc);

        // Reset weighty before adding buttons
        gbc.weighty = 0;
        addButton(sidebarPanel, "Home", centralPanel, "Home", gbc);
        addButton(sidebarPanel, "Profile", centralPanel, "Profile", gbc);
        addButton(sidebarPanel, "Friends", centralPanel, "Friends", gbc);
        addButton(sidebarPanel, "Search", centralPanel, "Search", gbc);

        // Add spacer at the bottom with weight
        gbc.weighty = 1;
        sidebarPanel.add(Box.createGlue(), gbc);

        // Add sidebar and central panel to the frame
        add(sidebarPanel, BorderLayout.WEST);
        add(centralPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void addButton(JPanel panel, String text, JPanel cardPanel, String cardName, GridBagConstraints gbc) {
        JButton button = new JButton(text);
        button.addActionListener(e -> cardLayout.show(cardPanel, cardName));
        panel.add(button, gbc);
    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(() -> {
    // new MainFrame().setVisible(true);
    // });
    // }
}
