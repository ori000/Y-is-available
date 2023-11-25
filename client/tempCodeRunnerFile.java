
// import javax.swing.*;
// import javax.swing.border.EmptyBorder;
// import java.awt.*;

// public class StyledHomepage extends JFrame {

//     private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
//     private static final Color NAV_BAR_COLOR = new Color(30, 144, 255);
//     private static final Color SIDEBAR_COLOR = new Color(230, 230, 250);
//     private static final Color FOOTER_COLOR = new Color(192, 192, 192);
//     private static final Color BUTTON_COLOR = new Color(100, 149, 237);
//     private static final Color BUTTON_HOVER_COLOR = new Color(65, 105, 225);
//     private static final Color TEXT_COLOR = Color.WHITE;

//     public StyledHomepage() {
//         setTitle("Y Application - Home");
//         setSize(1024, 768);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new BorderLayout(10, 10)); // Add spacing between components
//         getContentPane().setBackground(BACKGROUND_COLOR);
//         initializeUI();
//     }

//     private void initializeUI() {
//         add(createHeaderPanel(), BorderLayout.NORTH);
//         add(createLeftSidebar(), BorderLayout.WEST);
//         add(createCentralPanel(), BorderLayout.CENTER);
//         add(createRightSidebar(), BorderLayout.EAST);
//         add(createFooterPanel(), BorderLayout.SOUTH);
//     }

//     private JPanel createHeaderPanel() {
//         JPanel headerPanel = new JPanel(new BorderLayout());
//         headerPanel.setBackground(NAV_BAR_COLOR);

//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         buttonPanel.setBackground(NAV_BAR_COLOR);
//         buttonPanel.add(createStyledButton("Home"));
//         buttonPanel.add(createStyledButton("Profile"));
//         buttonPanel.add(createStyledButton("Messages"));
//         buttonPanel.add(createStyledButton("Notifications"));

//         headerPanel.add(buttonPanel, BorderLayout.CENTER);

//         JTextField searchField = new JTextField(20);
//         styleTextField(searchField);
//         JPanel searchPanel = new JPanel(new BorderLayout());
//         searchPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
//         searchPanel.add(searchField);

//         headerPanel.add(searchPanel, BorderLayout.NORTH);

//         return headerPanel;
//     }

//     private JButton createStyledButton(String text) {
//         JButton button = new JButton(text);
//         button.setBackground(BUTTON_COLOR);
//         button.setForeground(TEXT_COLOR);
//         button.setBorderPainted(false);
//         button.setFocusPainted(false);
//         button.setFont(new Font("Tahoma", Font.BOLD, 12));
//         button.setBorder(new EmptyBorder(5, 10, 5, 10));
//         button.addMouseListener(new java.awt.event.MouseAdapter() {
//             public void mouseEntered(java.awt.event.MouseEvent evt) {
//                 button.setBackground(BUTTON_HOVER_COLOR);
//             }

//             public void mouseExited(java.awt.event.MouseEvent evt) {
//                 button.setBackground(BUTTON_COLOR);
//             }
//         });
//         return button;
//     }

//     private void styleTextField(JTextField field) {
//         field.setBorder(BorderFactory.createCompoundBorder(
//                 field.getBorder(),
//                 BorderFactory.createEmptyBorder(5, 10, 5, 10)));
//         field.setFont(new Font("Tahoma", Font.PLAIN, 12));
//     }

//     private JPanel createLeftSidebar() {
//         JPanel sidebar = new JPanel(new GridLayout(0, 1, 0, 10));
//         sidebar.setBackground(SIDEBAR_COLOR);
//         sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

//         sidebar.add(createStyledButton("Groups"));
//         sidebar.add(createStyledButton("Events"));
//         sidebar.add(createStyledButton("Saved Posts"));

//         return sidebar;
//     }

//     private JScrollPane createCentralPanel() {
//         JPanel centralPanel = new JPanel();
//         centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
//         centralPanel.setBackground(BACKGROUND_COLOR);
//         centralPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

//         JScrollPane scrollPane = new JScrollPane(centralPanel);
//         scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//         return scrollPane;
//     }

//     private JPanel createRightSidebar() {
//         JPanel rightSidebar = new JPanel();
//         rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
//         rightSidebar.setBackground(SIDEBAR_COLOR);
//         rightSidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

//         rightSidebar.add(new JLabel("User Profile Summary"));
//         rightSidebar.add(createStyledButton("New Connections"));

//         return rightSidebar;
//     }

//     private JPanel createFooterPanel() {
//         JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         footerPanel.setBackground(FOOTER_COLOR);
//         footerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

//         footerPanel.add(new JLabel("About | Contact | Privacy Policy | Terms of Service"));

//         return footerPanel;
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             StyledHomepage homePage = new StyledHomepage();
//             homePage.setVisible(true);
//         });
//     }
// }