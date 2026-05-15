package lab;

import lab.global.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    private static final String MOTIF_LOOK_AND_FEEL = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setLook();

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private static void setLook() {
        try {
            UIManager.setLookAndFeel(MOTIF_LOOK_AND_FEEL);
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException e) {
            System.err.println("Could not apply CDE/Motif look and feel: " + e.getMessage());
        }
    }
}
