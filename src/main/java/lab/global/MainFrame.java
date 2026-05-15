package lab.global;

import lab.core.nav.NavigationController;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private final JPanel contentPanel;

    public MainFrame() {
        contentPanel = new JPanel(new BorderLayout());

        setTitle("Lab for Psychology Experiments");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setContentPane(contentPanel);

        NavigationController.getInstance().setMainFrame(this);
        NavigationController.getInstance().showLoginView();
    }

    public void showView(JPanel view) {
        contentPanel.removeAll();
        contentPanel.add(view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
