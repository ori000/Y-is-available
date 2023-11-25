import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInfoPanel extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JTextArea bioArea;
    private JButton saveButton;

    public UserInfoPanel() {
        setLayout(new GridBagLayout());
        initializeComponents();
        layoutComponents();
        addEventListeners();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        bioArea = new JTextArea(5, 20);
        saveButton = new JButton("Save Changes");

        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        add(emailField, gbc);

        gbc.gridy++;
        add(new JLabel("Bio:"), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(bioArea), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(saveButton, gbc);
    }

    private void addEventListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement saving logic here
                // For example: save the entered information to a database or a file
            }
        });
    }

    // Methods to set and get user information (can be linked to a database or data
    // source)
    public void setUserInfo(String name, String email, String bio) {
        nameField.setText(name);
        emailField.setText(email);
        bioArea.setText(bio);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getBio() {
        return bioArea.getText();
    }
}