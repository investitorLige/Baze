package lab.features.auth.views;

import lab.features.auth.controllers.AuthController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LoginView extends JPanel {
    private final AuthController controller;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton goToRegisterButton;

    public LoginView() {
        controller = AuthController.getInstance();
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Prijavi se");
        loginButton.addActionListener(event -> controller.loginUser(getUsername(), getPassword()));
        goToRegisterButton = new JButton("Napravi nalog");
        goToRegisterButton.addActionListener(event -> controller.showRegisterView());

        setLayout(new BorderLayout());
        add(createFormPanel(), BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Korisnicko ime:"), constraints);

        constraints.gridx = 1;
        formPanel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Lozinka:"), constraints);

        constraints.gridx = 1;
        formPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(loginButton, constraints);

        constraints.gridx = 1;
        formPanel.add(goToRegisterButton, constraints);

        return formPanel;
    }

    public AuthController getController() {
        return controller;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getGoToRegisterButton() {
        return goToRegisterButton;
    }
}
