import javax.swing.*;
import java.awt.*;

public class SearchPage extends JPanel {
    private MainApplicationFrame mainFrame;
    private JTextField searchField;
    private JButton searchButton;
    private JButton topsearchButton;
    private JButton homeButton;
    private JButton profileButton;
    private JButton settingsButton;
    private JLabel statusLabel;
    private JLabel userLabel;

    public SearchPage(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        styleComponents();
        layoutComponents();
        addEventListeners();
    }

    private void initializeComponents() {
        searchField = new JTextField(50);
        homeButton = new JButton("Home");
        profileButton = new JButton("Profile");
        settingsButton = new JButton("Settings");
        searchButton = new JButton("Search");
        statusLabel = new JLabel("Ready");
        userLabel = new JLabel("User Name");
    }

    private void styleComponents() {
        setBackground(new Color(32, 32, 32));
        searchField.setBackground(new Color(70, 70, 70));
        searchField.setForeground(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 14);
        styleButton(homeButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(profileButton, font, new Color(70, 70, 70), Color.WHITE);
        styleButton(searchButton, font, new Color(70, 70, 70), Color.WHITE);
        // styleButton(searchButton, font, new Color(70, 70, 70), Color.WHITE);
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

    private void addEventListeners() {
        homeButton.addActionListener(e -> mainFrame.switchToHomePage());
        profileButton.addActionListener(e -> mainFrame.switchToProfilePage());
        settingsButton.addActionListener(e -> mainFrame.switchToSettingsPage());
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(userLabel);
        topPanel.add(userPanel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(); // Using default FlowLayout
        topsearchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(topsearchButton);
        topPanel.add(searchPanel, BorderLayout.CENTER); // Centering the search panel

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
        gbc.gridy = 1;
        sidebarPanel.add(profileButton, gbc);
        gbc.gridy = 2;
        sidebarPanel.add(searchButton, gbc);
        gbc.gridy = 3;
        sidebarPanel.add(settingsButton, gbc);
        return sidebarPanel;
    }

    private JPanel createMainContentPanel() {
        setBackground(new Color(32, 32, 32));
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setBackground(new Color(100, 100, 100));
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
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
