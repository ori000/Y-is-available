import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class Styles {
    public static void styleFrame(JFrame frame) {
        // Set the background color
        frame.getContentPane().setBackground(Color.WHITE);

        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be non-resizable
        frame.setResizable(false);

        // Set the frame's location to the center of the screen
        frame.setLocationRelativeTo(null);
    }

    //style panel
    public static void stylePanel(JPanel panel) {
        // Set the background color
        panel.setBackground(Color.WHITE);

        // Set the border
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));
    }

    //style label
    public static void styleLabel(JLabel label) {
        // Twitter's text color
        Color textColor = new Color(20, 23, 26);

        // Set the foreground color
        label.setForeground(textColor);

        // Set the font
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    //style text area
    public static void styleTextArea(JTextArea textArea) {
        // Twitter's text field border color
        Color borderColor = new Color(230, 236, 240);

        // Set the background color
        textArea.setBackground(Color.WHITE);

        // Set the border
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));

        // Set the font
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    public static void styleTextField(JTextField textField) {
        // Twitter's text field border color
        Color borderColor = new Color(230, 236, 240);

        // Set the background color
        textField.setBackground(Color.WHITE);

        // Set the border
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));

        // Set the font
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    public static void styleButton(JButton button) {
        Color twitterBlue = new Color(29, 161, 242);

        // Set the background and foreground colors
        button.setBackground(twitterBlue);
        button.setForeground(Color.WHITE);

        // Set the font
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a rounded border
        button.setBorder(new RoundedBorder(10)); // 10 is the radius of the border

        // Remove the focus paint
        button.setFocusPainted(false);

        // Set the button to be opaque
        button.setOpaque(true);

        // Set the border paint
        button.setBorderPainted(false);
    }

    //style a button but smaller font
    public static void styleButtonSmall(JButton button) {
        Color twitterBlue = new Color(29, 161, 242);

        // Set the background and foreground colors
        button.setBackground(twitterBlue);
        button.setForeground(Color.WHITE);

        // Set the font
        button.setFont(new Font("Arial", Font.BOLD, 10));

        // Create a rounded border
        button.setBorder(new RoundedBorder(10)); // 10 is the radius of the border

        // Remove the focus paint
        button.setFocusPainted(false);

        // Set the button to be opaque
        button.setOpaque(true);

        // Set the border paint
        button.setBorderPainted(false);
    }

    //style navigation bar button
    public static void styleNavBarButton(JButton button) {
        // Set the background and foreground colors
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        // Set the font
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Remove the focus paint
        button.setFocusPainted(false);

        // Set the button to be opaque
        button.setOpaque(true);

        // Set the border paint
        button.setBorderPainted(false);
    }
    
}

class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
