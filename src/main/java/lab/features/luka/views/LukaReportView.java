package lab.features.luka.views;

import lab.features.luka.controllers.LukaController;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

public final class LukaReportView {
    private static final String[] COLUMNS = {
            "ID istrazivaca",
            "Ime",
            "Prezime",
            "Institucija",
            "Indeks opterecenja",
            "Broj izvodjenja",
            "Zakazane sesije",
            "Zavrsene sesije",
            "Broj prisutnih",
            "Broj rezultata"
    };

    private LukaReportView() {
    }

    public static void showDialog(Component parent) {
        LukaController controller = new LukaController();
        List<Object[]> rows = controller.getResearcherLoadReport();

        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nema podataka za Luka upit.");
            return;
        }

        DefaultTableModel model = createTableModel();
        for (Object[] row : rows) {
            model.addRow(row);
        }

        Frame owner = JOptionPane.getFrameForComponent(parent);
        JDialog dialog = new JDialog(owner, "Luka upit - opterecenje istrazivaca", true);
        JTable table = new JTable(model);
        configureTable(table);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Zatvori");
        closeButton.addActionListener(event -> dialog.dispose());

        actions.add(closeButton);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setSize(1000, 460);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void configureTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] widths = {90, 110, 130, 180, 140, 120, 120, 120, 120, 120};
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private static DefaultTableModel createTableModel() {
        return new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
