
import javax.swing.*;
import java.awt.*;

public class PostsPage extends JFrame {

    public PostsPage() {
        setTitle("Y Application - Posts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addPosts();
        this.setVisible(true);
    }

    private void addPosts() {
        // This is where you'd normally loop through a list of post objects
        // For demonstration, we'll just add one post panel

        // Create the post panel with a border layout
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        postPanel.setPreferredSize(new Dimension(800, 100)); // Set the preferred size for the post panel

        // Add the post content in the center
        JTextArea postContent = new JTextArea("This is an example post content.");
        postContent.setWrapStyleWord(true);
        postContent.setLineWrap(true);
        postContent.setEditable(false);
        postContent.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Add some padding around the text

        // Add the post content to the post panel
        postPanel.add(postContent);

        // Add the post panel to the frame
        add(postPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PostsPage postsPage = new PostsPage();
            postsPage.setVisible(true);
        });
    }
}
