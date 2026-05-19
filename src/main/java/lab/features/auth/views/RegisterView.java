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

public class RegisterView extends JPanel {
    private final AuthController controller;
    private final JTextField imeField;
    private final JTextField prezimeField;
    private final JTextField datumRodjenjaField;
    private final JTextField emailField;
    private final JTextField telField;
    private final JTextField korisnickoImeField;
    private final JTextField titulaField;
    private final JTextField specijalizacijaField;
    private final JTextField godineIskustvaField;
    private final JTextField institucijaField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton registerButton;
    private final JButton backToLoginButton;

    public RegisterView() {
        controller = AuthController.getInstance();
        imeField = new JTextField(20);
        prezimeField = new JTextField(20);
        datumRodjenjaField = new JTextField(20);
        emailField = new JTextField(20);
        telField = new JTextField(20);
        korisnickoImeField = new JTextField(20);
        titulaField = new JTextField(20);
        specijalizacijaField = new JTextField(20);
        godineIskustvaField = new JTextField(20);
        institucijaField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        registerButton = new JButton("Registruj se");
        registerButton.addActionListener(event -> controller.registerUser(
                getIme(),
                getPrezime(),
                getDatumRodjenja(),
                getEmail(),
                getTel(),
                getKorisnickoIme(),
                getTitula(),
                getSpecijalizacija(),
                getGodineIskustva(),
                getInstitucija(),
                getPassword(),
                getConfirmedPassword()
        ));
        backToLoginButton = new JButton("Nazad na prijavu");
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
        formPanel.add(new JLabel("Ime:"), constraints);

        constraints.gridx = 1;
        formPanel.add(imeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Prezime:"), constraints);

        constraints.gridx = 1;
        formPanel.add(prezimeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(new JLabel("Datum rodjenja:"), constraints);

        constraints.gridx = 1;
        formPanel.add(datumRodjenjaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(new JLabel("Email:"), constraints);

        constraints.gridx = 1;
        formPanel.add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(new JLabel("Tel:"), constraints);

        constraints.gridx = 1;
        formPanel.add(telField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        formPanel.add(new JLabel("Korisnicko ime:"), constraints);

        constraints.gridx = 1;
        formPanel.add(korisnickoImeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        formPanel.add(new JLabel("Titula:"), constraints);

        constraints.gridx = 1;
        formPanel.add(titulaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        formPanel.add(new JLabel("Specijalizacija:"), constraints);

        constraints.gridx = 1;
        formPanel.add(specijalizacijaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 8;
        formPanel.add(new JLabel("Godine iskustva:"), constraints);

        constraints.gridx = 1;
        formPanel.add(godineIskustvaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        formPanel.add(new JLabel("Institucija:"), constraints);

        constraints.gridx = 1;
        formPanel.add(institucijaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 10;
        formPanel.add(new JLabel("Lozinka:"), constraints);

        constraints.gridx = 1;
        formPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 11;
        formPanel.add(new JLabel("Potvrda lozinke:"), constraints);

        constraints.gridx = 1;
        formPanel.add(confirmPasswordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 12;
        formPanel.add(registerButton, constraints);

        constraints.gridx = 1;
        formPanel.add(backToLoginButton, constraints);

        return formPanel;
    }

    public AuthController getController() {
        return controller;
    }

    public String getIme() {
        return imeField.getText();
    }

    public String getPrezime() {
        return prezimeField.getText();
    }

    public String getDatumRodjenja() {
        return datumRodjenjaField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getTel() {
        return telField.getText();
    }

    public String getKorisnickoIme() {
        return korisnickoImeField.getText();
    }

    public String getTitula() {
        return titulaField.getText();
    }

    public String getSpecijalizacija() {
        return specijalizacijaField.getText();
    }

    public String getGodineIskustva() {
        return godineIskustvaField.getText();
    }

    public String getInstitucija() {
        return institucijaField.getText();
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
