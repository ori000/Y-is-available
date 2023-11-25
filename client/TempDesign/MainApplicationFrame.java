import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplicationFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainApplicationFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize pages
        JPanel homePage = new Homepage(this);
        JPanel settingsPage = new SettingsPage(this);
        JPanel searchPage = new SearchPage(this);
        JPanel profilePage = new ProfilePage(this);

        // Add pages to card panel with a string identifier
        cardPanel.add(homePage, "HomePage");
        cardPanel.add(settingsPage, "SettingsPage");
        cardPanel.add(searchPage, "SearchPage");
        cardPanel.add(profilePage, "ProfilePage");

        // Add the card panel to the frame
        add(cardPanel);

        // Show the initial card
        cardLayout.show(cardPanel, "HomePage");
    }

    // Methods to switch to different pages
    public void switchToHomePage() {
        cardLayout.show(cardPanel, "HomePage");
    }

    public void switchToSettingsPage() {
        cardLayout.show(cardPanel, "SettingsPage");
    }

    public void switchToSearchPage() {
        cardLayout.show(cardPanel, "SearchPage");
    }

    public void switchToProfilePage() {
        cardLayout.show(cardPanel, "ProfilePage");
    }

    // Main method to start the application
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }
}