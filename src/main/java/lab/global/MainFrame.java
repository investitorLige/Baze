package lab.global;

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
    }

    public void showView(JPanel view) {
        contentPanel.removeAll();
        contentPanel.add(view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
