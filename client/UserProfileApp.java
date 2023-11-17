
import javax.swing.*;
import java.awt.*;

public class UserProfileApp extends JFrame {

    private static final Color LIGHT_BLUE = new Color(210, 224, 239);
    private static final Color DARK_BLUE = new Color(25, 83, 105);
    private static final Color WHITE = Color.WHITE;
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public UserProfileApp() {
        setTitle("Y Application - User Profile");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(WHITE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI() {
        add(createSettingsPanel(), BorderLayout.WEST);
        add(createSearchResultsPanel(), BorderLayout.CENTER);
        add(createCreatePostPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(LIGHT_BLUE);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton changePasswordButton = new StyledButton("Change Password");
        JCheckBox privacyCheckbox = new JCheckBox("Make profile private");
        privacyCheckbox.setFont(MAIN_FONT);
        privacyCheckbox.setBackground(LIGHT_BLUE);

        settingsPanel.add(changePasswordButton);
        settingsPanel.add(privacyCheckbox);

        return settingsPanel;
    }

    private JPanel createSearchResultsPanel() {
        JPanel searchResultsPanel = new JPanel(new BorderLayout());
        searchResultsPanel.setBackground(WHITE);
        searchResultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JList<String> resultList = new JList<>(); // Populate with search results
        resultList.setFont(MAIN_FONT);
        JComboBox<String> categoriesComboBox = new JComboBox<>(new String[] { "People", "Pages", "Posts" });
        categoriesComboBox.setFont(MAIN_FONT);

        searchResultsPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);
        searchResultsPanel.add(categoriesComboBox, BorderLayout.NORTH);

        return searchResultsPanel;
    }

    private JPanel createCreatePostPanel() {
        JPanel createPostPanel = new JPanel(new BorderLayout());
        createPostPanel.setBackground(LIGHT_BLUE);
        createPostPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea postEditor = new JTextArea();
        postEditor.setFont(MAIN_FONT);
        postEditor.setLineWrap(true);
        postEditor.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(postEditor);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Width of 400 and height of 200

        JButton addMediaButton = new StyledButton("Add Media");
        JComboBox<String> privacyOptionsComboBox = new JComboBox<>(new String[] { "Public", "Friends", "Only Me" });
        privacyOptionsComboBox.setFont(MAIN_FONT);

        createPostPanel.add(new JScrollPane(postEditor), BorderLayout.CENTER);
        createPostPanel.add(addMediaButton, BorderLayout.NORTH);
        createPostPanel.add(privacyOptionsComboBox, BorderLayout.SOUTH);

        return createPostPanel;
    }

    // Helper method to create styled buttons
    private class StyledButton extends JButton {
        public StyledButton(String text) {
            super(text);
            setBackground(DARK_BLUE);
            setForeground(WHITE);
            setFont(MAIN_FONT);
            setBorderPainted(false);
            setFocusPainted(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserProfileApp userProfileApp = new UserProfileApp();
            userProfileApp.setVisible(true);
        });
    }
}
