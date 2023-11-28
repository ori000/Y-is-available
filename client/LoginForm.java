
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

import Shared.Requests.LoginRequest;

import java.awt.*;

class LoginForm extends JFrame {

    private JTextField emailTextField;
    private JPasswordField passwordField;

    public LoginForm(RegistrationForm registrationForm, ClientSocket client_socket, ObjectOutputStream outputStream,
            ObjectInputStream inputStream) {
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
        constraints.insets = new Insets(10, 0, 10, 0);

        emailTextField = new JTextField(20);
        passwordField = new JPasswordField(20);

        Styles.styleTextField(emailTextField);
        Styles.styleTextField(passwordField);

        addComponent(new JLabel("E-mail:"), constraints, 0, 0);
        addComponent(emailTextField, constraints, 1, 0);
        addComponent(new JLabel("Password:"), constraints, 0, 1);
        addComponent(passwordField, constraints, 1, 1);

        JButton loginButton = new JButton("Login");
        Styles.styleButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch data from text fields including the password
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                LoginRequest loginRequest = new LoginRequest(email, password);

                // User Registration
                try {
                    // 1 - Create a socket and connect to the server
                    System.out.println("Client socket created with IP: " + client_socket.client_ip_address
                            + " and sending to port number: " + ClientSocket.client_port_number);

                    // 3 - Send the User object to the server and tell the server to register
                    outputStream.writeObject("LOGIN");
                    outputStream.writeObject(loginRequest);
                    System.out.println("Info sent...");

                    // Read the response from the server
                    String token = (String) inputStream.readObject();

                    if (token.length() > 1) {
                        client_socket.setUserToken(token);
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        new MainFrame(client_socket, outputStream, inputStream);
                        LoginForm.this.dispose();

                        //If user logged in successfully, set a server to get other user's posts
                        int port_number = DatabaseConnector.getUser(token).getPort();
                        ServerSocket serv = new ServerSocket(port_number);
                        System.out.println("Server socket for posts created with port number: " + port_number);
                        
                        new Thread(() -> {
                            while (true) {
                                try {
                                    Socket socket = serv.accept();
                                    System.out.println("Client connected to server socket for posts");
                        
                                    new Thread(() -> {
                                        try {
                                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                            String post;
                                            while ((post = (String) in.readObject()) != null) {
                                                //Handle the post I received from other users
                                                System.out.println("Received post: " + post);
                                            }
                                        } catch (EOFException eof) { System.out.println("reading finished");}
                                        catch (Exception ex) {
                                            ex.printStackTrace();
                                        } finally {
                                            try {
                                                socket.close();
                                            } catch (IOException exe) {
                                                exe.printStackTrace();
                                            }
                                        }
                                    }).start();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }).start();

                        /* while (true){
                                Socket socket = serv.accept();
                                System.out.println("Client connected to server socket for posts");
                                new Thread(() -> {
                                    try {
                                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        String post;
                                        while ((post = in.readLine()) != null) {
                                            //Handle the post I received from other users
                                            System.out.println("Received post: " + post);
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    } finally {
                                        try {
                                            socket.close();
                                        } catch (IOException exe) {
                                            exe.printStackTrace();
                                        }
                                    }
                                }).start();

                        }
                        */

                    } else {
                        JOptionPane.showMessageDialog(null, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        Styles.styleButton(backButton);
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
        Styles.styleButton(forgotPasswordButton);
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
}
