import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserProfilePage extends JFrame {

    // Define the color and style constants similar to the homepage for consistency
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color PANEL_COLOR = new Color(230, 230, 250);
    private static final Color BORDER_COLOR = new Color(192, 192, 192);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 14);

    public UserProfilePage() {
        setTitle("Y Application - User Profile");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Top section for Personal Information
        add(createPersonalInfoPanel(), BorderLayout.NORTH);

        // Center section for User Posts
        add(createPostsPanel(), BorderLayout.CENTER);

        // Right sidebar for Friends/Followers List
        add(createFriendsPanel(), BorderLayout.EAST);
    }

    private JPanel createPersonalInfoPanel() {
        JPanel personalInfoPanel = new JPanel();
        personalInfoPanel.setLayout(new BoxLayout(personalInfoPanel, BoxLayout.Y_AXIS));
        personalInfoPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Personal Information"));
        personalInfoPanel.setBackground(PANEL_COLOR);

        // Example components for personal information
        JLabel nameLabel = new JLabel("User Name");
        nameLabel.setFont(HEADER_FONT);
        JLabel photoLabel = new JLabel(new ImageIcon("path/to/user/photo.jpg")); // Replace with actual path
        JTextArea bioArea = new JTextArea("Bio...");
        bioArea.setFont(TEXT_FONT);
        bioArea.setEditable(false);
        bioArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        personalInfoPanel.add(photoLabel);
        personalInfoPanel.add(nameLabel);
        personalInfoPanel.add(bioArea);

        return personalInfoPanel;
    }

    private JScrollPane createPostsPanel() {
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Posts"));
        postsPanel.setBackground(PANEL_COLOR);

        // Dynamically add user posts here

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setBorder(null); // Removes the border around the scroll pane if desired
        return scrollPane;
    }

    private JPanel createFriendsPanel() {
        JPanel friendsPanel = new JPanel();
        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS));
        friendsPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Friends/Followers"));
        friendsPanel.setBackground(PANEL_COLOR);

        // Dynamically add friends/followers here

        return friendsPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserProfilePage userProfilePage = new UserProfilePage();
            userProfilePage.setVisible(true);
        });
    }
}
