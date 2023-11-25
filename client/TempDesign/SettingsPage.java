import javax.swing.*;
import java.awt.*;

public class SettingsPage extends JPanel {
    private MainApplicationFrame mainFrame;
    private JButton homeButton;
    private JButton searchButton;
    private JButton profileButton;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JButton settingsButton;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton refreshButton;

    public SettingsPage(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initializeComponents(); // Initialize common components
        initializeSettingsComponents(); // Initialize settings-specific components
        styleComponents(); // Apply styles
        layoutComponents(); // Lay out the components on the panel
        addEventListeners(); // Add event listeners
    }

    private void initializeSettingsComponents() {
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        refreshButton = new JButton("Load Data");

        // Set fields to be non-editable initially
        usernameField.setEditable(false);
        emailField.setEditable(false);
        passwordField.setEditable(false);
    }

    private void initializeComponents() {
        homeButton = new JButton("Home");
        searchButton = new JButton("Search");
        profileButton = new JButton("Profile");
        statusLabel = new JLabel("Ready");
        userLabel = new JLabel("User Name");
        settingsButton = new JButton("Settings");

    }

    private void styleComponents() {
        setBackground(new Color(32, 32, 32));
        Font font = new Font("Arial", Font.BOLD, 14);
        styleButton(homeButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(searchButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(profileButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(settingsButton, font, new Color(70, 70, 70), Color.WHITE);
        statusLabel.setFont(font);
        statusLabel.setForeground(Color.WHITE);
        userLabel.setForeground(Color.WHITE);
    }

    private void layoutComponents() {
        add(createTopPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
        add(createStatusBarPanel(), BorderLayout.PAGE_END);
    }

    public void loadDataFromBackend() {
        // TODO: Implement loading data from backend
        // For now, populate with placeholder data
        usernameField.setText("JohnDoe");
        emailField.setText("johndoe@example.com");
        passwordField.setText("password123");
    }

    private void addEventListeners() {
        homeButton.addActionListener(e -> mainFrame.switchToHomePage());
        searchButton.addActionListener(e -> mainFrame.switchToSearchPage());
        profileButton.addActionListener(e -> mainFrame.switchToProfilePage());
        refreshButton.addActionListener(e -> loadDataFromBackend());
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
        // gbc.gridx = 0;
        gbc.gridy = 0;
        sidebarPanel.add(homeButton, gbc);
        gbc.gridy = 1;
        sidebarPanel.add(profileButton, gbc);
        gbc.gridy = 2;
        sidebarPanel.add(searchButton, gbc);
        gbc.gridy = 3;
        sidebarPanel.add(settingsButton, gbc);
        return sidebarPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(100, 100, 100));
        mainContentPanel.add(new JLabel("Username:"));
        mainContentPanel.add(usernameField);
        mainContentPanel.add(new JLabel("Email:"));
        mainContentPanel.add(emailField);
        mainContentPanel.add(new JLabel("Password:"));
        mainContentPanel.add(passwordField);

        mainContentPanel.add(refreshButton); // Button to load data

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
