import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JPanel {
    private MainApplicationFrame mainFrame;
    private JButton homeButton;
    private JButton searchButton;
    private JButton settingsButton;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JButton profileButton;
    private JTabbedPane tabbedPane;
    private JList<String> userPostsList;
    private JList<String> likedPostsList;

    public ProfilePage(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        styleComponents();
        layoutComponents();
        addEventListeners();
    }

    private void initializeComponents() {
        homeButton = new JButton("Home");
        searchButton = new JButton("Search");
        settingsButton = new JButton("Settings");
        statusLabel = new JLabel("Ready");
        userLabel = new JLabel("User Name");
        profileButton = new JButton("Profile");
        tabbedPane = new JTabbedPane();
        userPostsList = new JList<>();
        JScrollPane userPostsScrollPane = new JScrollPane(userPostsList);
        likedPostsList = new JList<>();
        JScrollPane likedPostsScrollPane = new JScrollPane(likedPostsList);

    }

    private void styleComponents() {
        setBackground(new Color(32, 32, 32));
        Font font = new Font("Arial", Font.BOLD, 14);
        styleButton(homeButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(searchButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(settingsButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(profileButton, font, new Color(70, 70, 70), Color.WHITE);

        statusLabel.setFont(font);
        statusLabel.setForeground(Color.WHITE);
        userLabel.setForeground(Color.WHITE);
    }

    private void layoutComponents() {
        add(createTopPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
        add(createStatusBarPanel(), BorderLayout.PAGE_END);
        tabbedPane.addTab("My Posts", userPostsList);
        tabbedPane.addTab("Liked Posts", likedPostsList);
    }

    private void addEventListeners() {
        homeButton.addActionListener(e -> mainFrame.switchToHomePage());
        searchButton.addActionListener(e -> mainFrame.switchToSearchPage());
        settingsButton.addActionListener(e -> mainFrame.switchToSettingsPage());
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(userLabel);
        topPanel.add(userPanel, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        sidebarPanel.add(homeButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(profileButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(searchButton, gbc);
        gbc.gridy++;
        sidebarPanel.add(settingsButton, gbc);
        return sidebarPanel;
    }

    // Method to update the user's posts
    public void setUserPosts(String[] posts) {
        userPostsList.setListData(posts);
    }

    // Method to update the liked posts
    public void setLikedPosts(String[] posts) {
        likedPostsList.setListData(posts);
    }

    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(100, 100, 100));
        // Add your profile related components here
        mainContentPanel.add(tabbedPane);

        return mainContentPanel;
    }

    private JPanel createStatusBarPanel() {
        JPanel statusBarPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(statusLabel, BorderLayout.WEST);
        return statusBarPanel;
    }

    private void styleButton(JButton button, Font font, Color bgColor, Color fgColor) {
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
