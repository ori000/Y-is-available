import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class RegistrationForm extends JFrame {

    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField emailTextField;
    private JTextField phoneNumberTextField;
    private JTextField regionTextField;
    private JPasswordField passwordField; // Added password field

    public RegistrationForm() {
        setTitle("Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background

        JLabel titleLabel = new JLabel("Registration Form");
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

        Font font = new Font("Arial", Font.PLAIN, 14);
        Color buttonColor = new Color(100, 150, 220); // Custom button color

        firstNameTextField = new JTextField(20);
        lastNameTextField = new JTextField(20);
        emailTextField = new JTextField(20);
        phoneNumberTextField = new JTextField(20);
        regionTextField = new JTextField(20);
        passwordField = new JPasswordField(20); // Initialize password field

        addComponent(new JLabel("First Name:"), constraints, 0, 0);
        addComponent(firstNameTextField, constraints, 1, 0);
        addComponent(new JLabel("Last Name:"), constraints, 0, 1);
        addComponent(lastNameTextField, constraints, 1, 1);
        addComponent(new JLabel("Email:"), constraints, 0, 2);
        addComponent(emailTextField, constraints, 1, 2);
        addComponent(new JLabel("Phone Number:"), constraints, 0, 3);
        addComponent(phoneNumberTextField, constraints, 1, 3);
        addComponent(new JLabel("Region:"), constraints, 0, 4);
        addComponent(regionTextField, constraints, 1, 4);
        addComponent(new JLabel("Password:"), constraints, 0, 5); // Add password label
        addComponent(passwordField, constraints, 1, 5); // Add password field

        JButton signUpButton = new JButton("Sign Up");
        styleButton(signUpButton, buttonColor, font);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch data from text fields including the password
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String email = emailTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();
                String region = regionTextField.getText();
                String password = new String(passwordField.getPassword()); // Get password

                // Call the method to register the user (You need to implement this method)
                boolean isRegistered = false;
                try {
                    isRegistered = DatabaseConnector.registerUser(firstName, lastName, email, phoneNumber, region, password);
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }

                if (isRegistered) {
                    JOptionPane.showMessageDialog(RegistrationForm.this, "Registration Successful");
                } else {
                    JOptionPane.showMessageDialog(RegistrationForm.this, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, buttonColor, font);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm(RegistrationForm.this);
                loginForm.setVisible(true);
                RegistrationForm.this.setVisible(false);
            }
        });

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 6;
        add(signUpButton, constraints);

        constraints.gridy = 7;
        add(loginButton, constraints);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationForm::new);
    }
}
