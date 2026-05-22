package lab.features.auth.controllers;

import lab.core.nav.NavigationController;
import lab.core.session.UserSession;
import lab.features.auth.models.LoginModel;
import lab.features.auth.models.RegisterModel;
import lab.features.researchers.models.ResearcherModel;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            JOptionPane.showMessageDialog(null, "Korisnicko ime i lozinka su obavezni.");
            return false;
        }

        ResearcherModel researcher = loginModel.login(normalizedUsername, passwordText);
        if (researcher != null) {
            UserSession.login(researcher);
            JOptionPane.showMessageDialog(null, "Prijava uspesna.");
            NavigationController.getInstance().showDashboardView();
            return true;
        }

        JOptionPane.showMessageDialog(null, "Neispravno korisnicko ime ili lozinka.");
        return false;
    }

    public boolean registerUser(
            String ime,
            String prezime,
            String datumRodjenja,
            String email,
            String tel,
            String korisnickoIme,
            String titula,
            String specijalizacija,
            String godineIskustva,
            String institucija,
            char[] password,
            char[] confirmedPassword
    ) {
        String normalizedIme = ime.trim();
        String normalizedPrezime = prezime.trim();
        String normalizedDatumRodjenja = datumRodjenja.trim();
        String normalizedEmail = email.trim();
        String normalizedTel = tel.trim();
        String normalizedKorisnickoIme = korisnickoIme.trim();
        String normalizedTitula = titula.trim();
        String normalizedSpecijalizacija = specijalizacija.trim();
        String normalizedGodineIskustva = godineIskustva.trim();
        String normalizedInstitucija = institucija.trim();
        String passwordText = String.valueOf(password);
        String confirmedPasswordText = String.valueOf(confirmedPassword);
        Arrays.fill(password, '\0');
        Arrays.fill(confirmedPassword, '\0');

        if (
                normalizedIme.isBlank()
                        || normalizedPrezime.isBlank()
                        || normalizedDatumRodjenja.isBlank()
                        || normalizedEmail.isBlank()
                        || normalizedKorisnickoIme.isBlank()
                        || passwordText.isBlank()
                        || confirmedPasswordText.isBlank()
        ) {
            JOptionPane.showMessageDialog(null, "Sva polja su obavezna.");
            return false;
        }

        Integer parsedGodineIskustva = null;
        if (!normalizedGodineIskustva.isBlank()) {
            try {
                parsedGodineIskustva = Integer.parseInt(normalizedGodineIskustva);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Godine iskustva moraju biti ceo broj.");
                return false;
            }
        }

        try {
            LocalDate.parse(normalizedDatumRodjenja);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Datum rodjenja mora biti u formatu yyyy-MM-dd.");
            return false;
        }

        if (!passwordText.equals(confirmedPasswordText)) {
            JOptionPane.showMessageDialog(null, "Lozinke se ne poklapaju.");
            return false;
        }

        boolean registered = registerModel.register(
                normalizedIme,
                normalizedPrezime,
                normalizedDatumRodjenja,
                normalizedEmail,
                blankToNull(normalizedTel),
                normalizedKorisnickoIme,
                blankToNull(normalizedTitula),
                blankToNull(normalizedSpecijalizacija),
                parsedGodineIskustva,
                blankToNull(normalizedInstitucija),
                passwordText
        );

        if (registered) {
            JOptionPane.showMessageDialog(null, "Registracija uspesna.");
            showLoginView();
            return true;
        }

        JOptionPane.showMessageDialog(null, "Registracija nije uspela.");
        return false;
    }

    private String blankToNull(String value) {
        return value.isBlank() ? null : value;
    }
}
