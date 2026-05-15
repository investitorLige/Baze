package lab.features.auth.views;

import lab.features.auth.controllers.AuthController;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class RegisterView extends JPanel {
    private final AuthController controller;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField usernameField;
    private final JComboBox<String> roleComboBox;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton registerButton;
    private final JButton backToLoginButton;

    public RegisterView() {
        controller = AuthController.getInstance();
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        usernameField = new JTextField(20);
        roleComboBox = new JComboBox<>(new String[] {"Researcher", "Admin", "External user"});
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        registerButton = new JButton("Register");
        registerButton.addActionListener(event -> controller.registerUser(
                getFirstName(),
                getLastName(),
                getUsername(),
                getRole(),
                getPassword(),
                getConfirmedPassword()
        ));
        backToLoginButton = new JButton("Back to login");
        backToLoginButton.addActionListener(event -> controller.showLoginView());

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
        formPanel.add(new JLabel("First name:"), constraints);

        constraints.gridx = 1;
        formPanel.add(firstNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Last name:"), constraints);

        constraints.gridx = 1;
        formPanel.add(lastNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(new JLabel("Username:"), constraints);

        constraints.gridx = 1;
        formPanel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(new JLabel("Role:"), constraints);

        constraints.gridx = 1;
        formPanel.add(roleComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        formPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        formPanel.add(new JLabel("Confirm password:"), constraints);

        constraints.gridx = 1;
        formPanel.add(confirmPasswordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        formPanel.add(registerButton, constraints);

        constraints.gridx = 1;
        formPanel.add(backToLoginButton, constraints);

        return formPanel;
    }

    public AuthController getController() {
        return controller;
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getRole() {
        return (String) roleComboBox.getSelectedItem();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public char[] getConfirmedPassword() {
        return confirmPasswordField.getPassword();
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getBackToLoginButton() {
        return backToLoginButton;
    }
}
