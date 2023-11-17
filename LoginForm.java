import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class LoginForm extends JFrame {

    private JTextField emailTextField;
    private JPasswordField passwordField;

    public LoginForm(RegistrationForm registrationForm) {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background

        JLabel titleLabel = new JLabel("Login Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font to bold and larger size
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridwidth = 2;
        titleConstraints.insets = new Insets(10, 10, 50, 10);
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        add(titleLabel, titleConstraints);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(30, 10, 10, 10);

        // Customizing the text fields and buttons
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color buttonColor = new Color(100, 150, 220); // Custom button color

        emailTextField = new JTextField(20);
        passwordField = new JPasswordField(20);

        emailTextField.setFont(font);
        passwordField.setFont(font);

        addComponent(new JLabel("E-mail:"), constraints, 0, 0);
        addComponent(emailTextField, constraints, 1, 0);
        addComponent(new JLabel("Password:"), constraints, 0, 1);
        addComponent(passwordField, constraints, 1, 1);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, buttonColor, font);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (DatabaseConnector.validateLogin(email, password)) {
                    // Grant access
                    JOptionPane.showMessageDialog(LoginForm.this, "Access Granted");
                } else {
                    // Wrong input information
                    JOptionPane.showMessageDialog(LoginForm.this, "Wrong Input Information", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        styleButton(backButton, buttonColor, font);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm.this.setVisible(false); // Hide LoginForm
                registrationForm.setVisible(true); // Show RegistrationForm
            }
        });

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(loginButton, constraints);

        constraints.gridy = 3;
        add(backButton, constraints);

        pack();
        setLocationRelativeTo(null); // Center on screen

        // Add the back button to the layout
        GridBagConstraints backConstraints = new GridBagConstraints();
        backConstraints.fill = GridBagConstraints.HORIZONTAL;
        backConstraints.gridwidth = 2;
        backConstraints.gridx = 0;
        backConstraints.gridy = 4; // Adjust the row index as per your layout
        add(backButton, backConstraints);

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(loginButton, constraints);

        JButton forgotPasswordButton = new JButton("Forgot Password");
        styleButton(forgotPasswordButton, buttonColor, font);
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ForgotPasswordForm forgotPasswordForm = new ForgotPasswordForm();
                forgotPasswordForm.setVisible(true);
            }
        });
        JPanel forgotPanel = new JPanel();
        forgotPanel.add(new JLabel("Forgot password?"));
        forgotPanel.add(forgotPasswordButton);

        constraints.gridy = 3;
        addComponent(forgotPanel, constraints, 0, 3);

        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private void addComponent(Component component, GridBagConstraints constraints, int x, int y) {
        if (component instanceof JLabel) {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
        }
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
    }

    private void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }
}
