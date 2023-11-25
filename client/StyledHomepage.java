import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import org.w3c.dom.events.MouseEvent;
import java.awt.*;

public class StyledHomepage extends JFrame {
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font TEXT_FIELD_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color NAV_BAR_COLOR = new Color(30, 144, 255);
    private static final Color SIDEBAR_COLOR = new Color(230, 230, 250);
    private static final Color FOOTER_COLOR = new Color(192, 192, 192);
    private static final Color BUTTON_COLOR = new Color(100, 149, 237);
    private static final Color BUTTON_HOVER_COLOR = new Color(65, 105, 225);
    private static final Color TEXT_COLOR = Color.WHITE;

    public StyledHomepage() {
        setTitle("Y Application - Home");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing between components
        getContentPane().setBackground(BACKGROUND_COLOR);
        initializeUI();
    }

    private void initializeUI() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLeftSidebar(), BorderLayout.WEST);
        add(createCentralPanel(), BorderLayout.CENTER);
        add(createRightSidebar(), BorderLayout.EAST);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(NAV_BAR_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(NAV_BAR_COLOR);
        buttonPanel.add(createStyledButton("Home"));
        buttonPanel.add(createStyledButton("Profile"));
        buttonPanel.add(createStyledButton("Messages"));
        buttonPanel.add(createStyledButton("Notifications"));

        headerPanel.add(buttonPanel, BorderLayout.CENTER);

        JTextField searchField = new JTextField(20);
        styleTextField(searchField);
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
        searchPanel.add(searchField);

        headerPanel.add(searchPanel, BorderLayout.NORTH);

        return headerPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    g.setColor(getBackground());
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
                super.paintComponent(g);
            }
        };
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(TEXT_FIELD_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    }

    private JPanel createLeftSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(0, 1, 0, 10));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        sidebar.add(createStyledButton("Groups"));
        sidebar.add(createStyledButton("Events"));
        sidebar.add(createStyledButton("Saved Posts"));

        return sidebar;
    }

    private JScrollPane createCentralPanel() {
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(BACKGROUND_COLOR);
        centralPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(centralPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return scrollPane;
    }

    private JPanel createRightSidebar() {
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setBackground(SIDEBAR_COLOR);
        rightSidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        rightSidebar.add(new JLabel("User Profile Summary"));
        rightSidebar.add(createStyledButton("New Connections"));

        return rightSidebar;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(FOOTER_COLOR);
        footerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        footerPanel.add(new JLabel("About | Contact | Privacy Policy | Terms of Service"));

        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StyledHomepage homePage = new StyledHomepage();
            homePage.setVisible(true);
        });
    }
}
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;

// public class StyledHomepage extends Application {

// @Override
// public void start(Stage primaryStage) {
// primaryStage.setTitle("Y Application - Home");

// BorderPane mainLayout = new BorderPane();

// // Header
// HBox headerPanel = createHeaderPanel();
// mainLayout.setTop(headerPanel);

// // Sidebar
// VBox sidebar = createLeftSidebar();
// mainLayout.setLeft(sidebar);

// // Main content area
// ScrollPane centralPanel = createCentralPanel();
// mainLayout.setCenter(centralPanel);

// // Right sidebar
// VBox rightSidebar = createRightSidebar();
// mainLayout.setRight(rightSidebar);

// // Footer
// HBox footerPanel = createFooterPanel();
// mainLayout.setBottom(footerPanel);

// Scene scene = new Scene(mainLayout, 1024, 768);
// primaryStage.setScene(scene);
// primaryStage.show();
// }

// private HBox createHeaderPanel() {
// HBox headerPanel = new HBox(10);
// headerPanel.setPadding(new Insets(10));
// headerPanel.setBackground(
// new Background(new BackgroundFill(Color.rgb(30, 144, 255), CornerRadii.EMPTY,
// Insets.EMPTY)));

// Button homeButton = createStyledButton("Home");
// Button profileButton = createStyledButton("Profile");
// Button messagesButton = createStyledButton("Messages");
// Button notificationsButton = createStyledButton("Notifications");

// TextField searchField = new TextField();
// searchField.setPromptText("Search");
// searchField.setFont(Font.font("SansSerif", 14));
// HBox.setHgrow(searchField, Priority.ALWAYS); // Make search field grow
// horizontally

// headerPanel.getChildren().addAll(homeButton, profileButton, messagesButton,
// notificationsButton, searchField);

// return headerPanel;
// }

// private Button createStyledButton(String text) {
// Button button = new Button(text);
// // button.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
// button.setBackground(
// new Background(new BackgroundFill(Color.rgb(100, 149, 237), new
// CornerRadii(5), Insets.EMPTY)));
// button.setTextFill(Color.WHITE);
// button.setBorder(Border.EMPTY);

// // Set button hover behavior
// button.setOnMouseEntered(e -> button.setBackground(
// new Background(new BackgroundFill(Color.rgb(65, 105, 225), new
// CornerRadii(5), Insets.EMPTY))));
// button.setOnMouseExited(e -> button.setBackground(
// new Background(new BackgroundFill(Color.rgb(100, 149, 237), new
// CornerRadii(5), Insets.EMPTY))));

// return button;
// }

// private VBox createLeftSidebar() {
// VBox sidebar = new VBox(10);
// sidebar.setPadding(new Insets(10));
// sidebar.setBackground(
// new Background(new BackgroundFill(Color.rgb(230, 230, 250),
// CornerRadii.EMPTY, Insets.EMPTY)));

// Button groupsButton = createStyledButton("Groups");
// Button eventsButton = createStyledButton("Events");
// Button savedPostsButton = createStyledButton("Saved Posts");

// sidebar.getChildren().addAll(groupsButton, eventsButton, savedPostsButton);

// return sidebar;
// }

// private ScrollPane createCentralPanel() {
// VBox centralPanel = new VBox(10);
// centralPanel.setPadding(new Insets(10));
// centralPanel.setBackground(new Background(new BackgroundFill(Color.WHITE,
// CornerRadii.EMPTY, Insets.EMPTY)));

// // Here you would add your actual content nodes to the centralPanel

// ScrollPane scrollPane = new ScrollPane(centralPanel);
// scrollPane.setFitToWidth(true);

// return scrollPane;
// }

// private VBox createRightSidebar() {
// VBox rightSidebar = new VBox(10);
// rightSidebar.setBackground(
// new Background(new BackgroundFill(Color.rgb(230, 230, 250),
// CornerRadii.EMPTY, Insets.EMPTY)));
// rightSidebar.setPadding(new Insets(10));

// Label userProfileSummary = new Label("User Profile Summary");
// Button newConnectionsButton = createStyledButton("New Connections");

// rightSidebar.getChildren().addAll(userProfileSummary, newConnectionsButton);

// return rightSidebar;
// }

// private HBox createFooterPanel() {
// HBox footerPanel = new HBox();
// footerPanel.setPadding(new Insets(10));
// footerPanel.setBackground(
// new Background(new BackgroundFill(Color.rgb(192, 192, 192),
// CornerRadii.EMPTY, Insets.EMPTY)));

// Label footerLabel = new Label("About | Contact | Privacy Policy | Terms of
// Service");
// footerPanel.getChildren().add(footerLabel);

// return footerPanel;
// }

// public static void main(String[] args) {
// launch(args);
// }
// }