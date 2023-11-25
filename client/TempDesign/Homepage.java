import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage extends JPanel {
    private MainApplicationFrame mainFrame;
    private JButton newPostButton;
    private JButton refreshButton;
    private JButton homeButton;
    private JButton profileButton;
    private JButton searchButton;
    private JButton settingsButton;
    private JLabel statusLabel;
    private JLabel userLabel;

    public Homepage(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        styleComponents();
        layoutComponents();
        addEventListeners();
    }

    private void initializeComponents() {
        newPostButton = new JButton("New Post");
        refreshButton = new JButton("Refresh");
        homeButton = new JButton("Home");
        profileButton = new JButton("Profile");
        searchButton = new JButton("Search");
        settingsButton = new JButton("Settings");
        statusLabel = new JLabel("Ready");
        userLabel = new JLabel("User Name");
    }

    private void styleComponents() {
        setBackground(new Color(32, 32, 32));
        Font font = new Font("Arial", Font.BOLD, 14);
        Color buttonColor = new Color(70, 70, 70);
        Color textColor = Color.WHITE;

        styleButton(homeButton, font, buttonColor, textColor);
        styleButton(profileButton, font, buttonColor, textColor);
        styleButton(searchButton, font, buttonColor, textColor);
        styleButton(settingsButton, font, buttonColor, textColor);
        styleButton(newPostButton, font, buttonColor, textColor);
        styleButton(refreshButton, font, buttonColor, textColor);

        statusLabel.setFont(font);
        statusLabel.setForeground(textColor);
        userLabel.setFont(font);
        userLabel.setForeground(textColor);
    }

    private void layoutComponents() {
        add(createTopPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
        add(createInteractivePanel(), BorderLayout.SOUTH);
        add(createStatusBarPanel(), BorderLayout.PAGE_END);
    }

    private void addEventListeners() {
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToProfilePage();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToSearchPage();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToSettingsPage();
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchToHomePage();
            }
        });
        // Additional listeners for other buttons as needed
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

    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setBackground(new Color(100, 100, 100));
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        // Add main content here
        return mainContentPanel;
    }

    private JPanel createInteractivePanel() {
        JPanel interactivePanel = new JPanel(new FlowLayout());
        interactivePanel.add(newPostButton);
        interactivePanel.add(refreshButton);
        return interactivePanel;
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
