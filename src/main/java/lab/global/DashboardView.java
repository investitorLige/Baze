package lab.global;

import lab.core.nav.NavigationController;
import lab.core.session.UserSession;
import lab.features.experiments.views.ExperimentsView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class DashboardView extends JPanel {
    private final JPanel contentPanel;

    public DashboardView() {
        contentPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        showContent(new ExperimentsView());
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel userLabel = new JLabel("Ulogovani istrazivac: " + UserSession.getDisplayName());
        JButton logoutButton = new JButton("Odjava");
        logoutButton.addActionListener(event -> {
            UserSession.logout();
            NavigationController.getInstance().showLoginView();
        });

        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    private void showContent(JPanel view) {
        contentPanel.removeAll();
        contentPanel.add(view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
