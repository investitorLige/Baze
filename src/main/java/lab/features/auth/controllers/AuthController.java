package lab.features.auth.controllers;

import lab.core.nav.NavigationController;
import lab.features.auth.models.LoginModel;
import lab.features.auth.models.RegisterModel;

import javax.swing.JOptionPane;
import java.util.Arrays;

public class AuthController {
    private static final AuthController INSTANCE = new AuthController();

    private final LoginModel loginModel;
    private final RegisterModel registerModel;

    private AuthController() {
        loginModel = new LoginModel();
        registerModel = new RegisterModel();
    }

    public static AuthController getInstance() {
        return INSTANCE;
    }

    public void showLoginView() {
        NavigationController.getInstance().showLoginView();
    }

    public void showRegisterView() {
        NavigationController.getInstance().showRegisterView();
    }

    public boolean loginUser(String username, char[] password) {
        String normalizedUsername = username.trim();
        String passwordText = String.valueOf(password);
        Arrays.fill(password, '\0');

        if (normalizedUsername.isBlank() || passwordText.isBlank()) {
            JOptionPane.showMessageDialog(null, "Username and password are required.");
            return false;
        }

        boolean loggedIn = loginModel.login(normalizedUsername, passwordText);
        if (loggedIn) {
            JOptionPane.showMessageDialog(null, "Login successful.");
            return true;
        }

        JOptionPane.showMessageDialog(null, "Invalid username or password.");
        return false;
    }

    public boolean registerUser(
            String firstName,
            String lastName,
            String username,
            String role,
            char[] password,
            char[] confirmedPassword
    ) {
        String normalizedFirstName = firstName.trim();
        String normalizedLastName = lastName.trim();
        String normalizedUsername = username.trim();
        String passwordText = String.valueOf(password);
        String confirmedPasswordText = String.valueOf(confirmedPassword);
        Arrays.fill(password, '\0');
        Arrays.fill(confirmedPassword, '\0');

        if (
                normalizedFirstName.isBlank()
                        || normalizedLastName.isBlank()
                        || normalizedUsername.isBlank()
                        || role == null
                        || role.isBlank()
                        || passwordText.isBlank()
                        || confirmedPasswordText.isBlank()
        ) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return false;
        }

        if (!passwordText.equals(confirmedPasswordText)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match.");
            return false;
        }

        boolean registered = registerModel.register(
                normalizedFirstName,
                normalizedLastName,
                normalizedUsername,
                role,
                passwordText
        );

        if (registered) {
            JOptionPane.showMessageDialog(null, "Registration successful.");
            showLoginView();
            return true;
        }

        JOptionPane.showMessageDialog(null, "Registration failed.");
        return false;
    }
}
