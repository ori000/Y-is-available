import javax.swing.*;
import java.awt.*;

public class InteractionViewPage extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public InteractionViewPage() {
        setTitle("Y Application - Interactions");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new GridLayout(1, 2, 10, 10)); // Add horizontal and vertical gaps
        initializeUI();
    }

    private void initializeUI() {
        add(createListPanel("Likers", new String[] { "User1", "User2", "User3", "User4", "User5" }));
        add(createListPanel("Commenters", new String[] { "UserA", "UserB", "UserC", "UserD", "UserE" }));
    }

    private JPanel createListPanel(String title, String[] data) {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(title));
        listPanel.setBackground(BACKGROUND_COLOR);

        JList<String> list = new JList<>(data);
        list.setFont(MAIN_FONT);
        list.setBackground(BACKGROUND_COLOR);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND_COLOR);

        listPanel.add(scrollPane);

        return listPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InteractionViewPage interactionViewPage = new InteractionViewPage();
            interactionViewPage.setVisible(true);
        });
    }
}
