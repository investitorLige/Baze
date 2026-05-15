package lab.core.nav;

import lab.features.auth.views.LoginView;
import lab.features.auth.views.RegisterView;
import lab.global.MainFrame;

public class NavigationController {
    private static final NavigationController INSTANCE = new NavigationController();

    private MainFrame mainFrame;

    private NavigationController() {
    }

    public static NavigationController getInstance() {
        return INSTANCE;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void showLoginView() {
        mainFrame.showView(new LoginView());
    }

    public void showRegisterView() {
        mainFrame.showView(new RegisterView());
    }
}
