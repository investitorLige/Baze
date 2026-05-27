package lab.global;

import lab.core.nav.NavigationController;
import lab.core.session.UserSession;
import lab.features.experiments.views.ExperimentsView;
import lab.features.luka.views.LukaReportView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class DashboardView extends JPanel {
    private final JPanel contentPanel;

    public DashboardView() {
        contentPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

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

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 10, 12));

        JButton lukaQueryButton = new JButton("Luka upit");
        lukaQueryButton.addActionListener(event -> LukaReportView.showDialog(this));
        footerPanel.add(lukaQueryButton);

        return footerPanel;
    }

    private void showContent(JPanel view) {
        contentPanel.removeAll();
        contentPanel.add(view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
