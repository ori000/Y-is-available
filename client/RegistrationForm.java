import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Shared.Requests.*;

public class RegistrationForm extends JFrame {

    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField emailTextField;
    private JTextField phoneNumberTextField;
    private JTextField regionTextField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegistrationForm(ClientSocket client_socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        setTitle("Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background

        JLabel titleLabel = new JLabel("Registration Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font to bold and larger size
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridwidth = 20;
        titleConstraints.insets = new Insets(20, 10, 60, 10);
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        add(titleLabel, titleConstraints);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(30, 10, 10, 10);

        Font font = new Font("Arial", Font.BOLD, 14);
        Color buttonColor = new Color(100, 150, 220); // Custom button color

        firstNameTextField = new JTextField(20);
        lastNameTextField = new JTextField(20);
        emailTextField = new JTextField(20);
        phoneNumberTextField = new JTextField(20);
        regionTextField = new JTextField(20);
        passwordField = new JPasswordField(20);
        usernameField = new JTextField(20);


        addComponent(new JLabel("First Name:"), constraints, 0, 1);
        addComponent(firstNameTextField, constraints, 1, 1);
        addComponent(new JLabel("Last Name:"), constraints, 0, 2);
        addComponent(lastNameTextField, constraints, 1, 2);
        addComponent(new JLabel("Email:"), constraints, 0, 3);
        addComponent(emailTextField, constraints, 1, 3);
        addComponent(new JLabel("Phone Number:"), constraints, 0, 4);
        addComponent(phoneNumberTextField, constraints, 1, 4);
        addComponent(new JLabel("Region:"), constraints, 0, 5);
        addComponent(regionTextField, constraints, 1, 5);
        addComponent(new JLabel("Password:"), constraints, 0, 6);
        addComponent(passwordField, constraints, 1, 6);
        addComponent(new JLabel("Username:"), constraints, 0, 7);
        addComponent(usernameField, constraints, 1, 7);


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
                String password = new String(passwordField.getPassword());
                String username = usernameField.getText();
                RegisterationRequest user = new RegisterationRequest(firstName, lastName, username, email, password, region, phoneNumber);

                // Call the method to register the user (You need to implement this method)
                // User Registration
                try {
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address + " and sending to port number: " + ClientSocket.client_port_number);

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("REGISTER");
                    outputStream.writeUnshared(user);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    boolean isRegistered = inputStream.readBoolean();
                    
                    if (isRegistered) {
                        JOptionPane.showMessageDialog(null, "Registration Successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);                
                    }
                } 
                catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }
        });

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, buttonColor, font);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm(RegistrationForm.this, client_socket, outputStream, inputStream);
                loginForm.setVisible(true);
                RegistrationForm.this.setVisible(false);
            }
        });

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 8;
        add(signUpButton, constraints);

        constraints.gridy = 9;
        add(loginButton, constraints);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addComponent(Component component, GridBagConstraints constraints, int x, int y) {
        // if (component instanceof JLabel) {
        //     ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
        //     component.setFont(new Font("Arial", Font.BOLD, (int)(component.getFont().getSize()*1.5)));
        // }
        if (component instanceof JTextField) {
            ((JTextField) component).setHorizontalAlignment(SwingConstants.LEFT);

            component.setFont(new Font("Arial", Font.ITALIC, (int)(component.getFont().getSize())));

            // set border to dashed
            ((JTextField) component).setBorder(BorderFactory.createBevelBorder(1));

        }
        // component.setFont(new Font("Arial", Font.PLAIN, 14));
        constraints.gridx = x;
        constraints.gridy = y;
        if(component != null){
            add(component, constraints);
        }
    }

    private void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

}
