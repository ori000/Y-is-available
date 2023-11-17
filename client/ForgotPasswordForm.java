
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ForgotPasswordForm extends JFrame {

    private JTextField emailTextField;

    public ForgotPasswordForm() {
        setTitle("Forgot Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Consistent Background Color

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Customizing the text fields and buttons
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color buttonColor = new Color(100, 150, 220); // Custom button color

        emailTextField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, buttonColor, font);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String token = PasswordResetTokenManager.generateToken();
                processPasswordResetRequest(email);
                PasswordResetTokenManager.saveToken(email, token);
                EmailSender.sendResetEmail(email, token);
                JOptionPane.showMessageDialog(ForgotPasswordForm.this, "If the email is registered, a reset link will be sent.");
            }
        });
        

        addComponent(new JLabel("Enter your email:"), constraints, 0, 0);
        addComponent(emailTextField, constraints, 1, 0);
        constraints.gridwidth = 2;
        constraints.gridy = 1;
        add(submitButton, constraints);

        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private void addComponent(Component component, GridBagConstraints constraints, int x, int y) {
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

    private void processPasswordResetRequest(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Generate a token
        String token = PasswordResetTokenManager.generateToken();
    
        // Save the token in the database
        PasswordResetTokenManager.saveToken(email, token);
    
        // Send an email with the reset link
        EmailSender.sendResetEmail(email, token);
    
        // Inform the user
        JOptionPane.showMessageDialog(this, "If the email is registered, a reset link will be sent to: " + email);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ForgotPasswordForm::new);
    }
}