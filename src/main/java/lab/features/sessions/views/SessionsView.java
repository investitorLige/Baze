package lab.features.sessions.views;

import lab.features.sessions.controllers.SessionsController;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

public final class SessionsView {
    private static final String[] SESSION_COLUMNS = {
            "ID sesije", "Datum", "Pocetak", "Zavrsetak", "Status", "Napomena"
    };
    private static final String[] SESSION_STATUSES = {
            "zakazana", "u_toku", "zavrsena", "otkazana"
    };
    private static final String[] PARTICIPATION_COLUMNS = {
            "ID ispitanika", "Ispitanik", "Pol", "Obrazovanje", "Prisutan", "Ostvario nagradu"
    };
    private static final String[] RESULT_COLUMNS = {
            "Ispitanik", "Upitnik", "Rezultat", "Datum vreme", "Napomena"
    };
    private static final String[] USED_RESOURCE_COLUMNS = {
            "ID resursa", "Resurs", "Kolicina iskoriscenog", "Jedinica mere"
    };
    private static final String[] USED_TOOL_COLUMNS = {
            "ID alata", "Tip alata", "Ispravan", "Napomena"
    };

    private final Component parent;
    private final SessionsController controller;

    private SessionsView(Component parent) {
        this.parent = parent;
        controller = new SessionsController();
    }

    public static void showForExecution(Component parent, int executionId) {
        new SessionsView(parent).showSessionsForExecution(executionId);
    }

    private void showSessionsForExecution(int executionId) {
        DefaultTableModel sessionModel = createTableModel(SESSION_COLUMNS);
        loadRows(sessionModel, controller.findSessionsByExecution(executionId));
        JTable sessionTable = new JTable(sessionModel);

        JDialog dialog = createDialog("Sesije za izvodjenje " + executionId);
        dialog.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton statusButton = new JButton("Promeni status");
        statusButton.addActionListener(event -> changeSelectedSessionStatus(sessionTable, sessionModel));

        JButton deleteButton = new JButton("Obrisi sesiju");
        deleteButton.addActionListener(event -> deleteSelectedSession(sessionTable, sessionModel));

        JButton participationButton = new JButton("Prikazi ucesca");
        participationButton.addActionListener(event -> showParticipationForSelectedSession(sessionTable));

        JButton resultsButton = new JButton("Prikazi rezultate");
        resultsButton.addActionListener(event -> showResultsForSelectedSession(sessionTable));

        JButton usedResourcesButton = new JButton("Prikazi resurse");
        usedResourcesButton.addActionListener(event -> showUsedResourcesForSelectedSession(sessionTable));

        JButton usedToolsButton = new JButton("Prikazi alate");
        usedToolsButton.addActionListener(event -> showUsedToolsForSelectedSession(sessionTable));

        JButton closeButton = new JButton("Zatvori");
        closeButton.addActionListener(event -> dialog.dispose());
        actions.add(statusButton);
        actions.add(deleteButton);
        actions.add(participationButton);
        actions.add(resultsButton);
        actions.add(usedResourcesButton);
        actions.add(usedToolsButton);
        actions.add(closeButton);

        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showParticipationForSelectedSession(JTable sessionTable) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        showRowsDialog(
                "Ucesca za sesiju " + sessionId,
                PARTICIPATION_COLUMNS,
                controller.findParticipationBySession(sessionId)
        );
    }

    private void showResultsForSelectedSession(JTable sessionTable) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        showRowsDialog(
                "Rezultati upitnika za sesiju " + sessionId,
                RESULT_COLUMNS,
                controller.findResultsBySession(sessionId)
        );
    }

    private void showUsedResourcesForSelectedSession(JTable sessionTable) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        showRowsDialog(
                "Upotreba resursa za sesiju " + sessionId,
                USED_RESOURCE_COLUMNS,
                controller.findUsedResourcesBySession(sessionId)
        );
    }

    private void showUsedToolsForSelectedSession(JTable sessionTable) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        showRowsDialog(
                "Upotreba alata za sesiju " + sessionId,
                USED_TOOL_COLUMNS,
                controller.findUsedToolsBySession(sessionId)
        );
    }

    private void deleteSelectedSession(JTable sessionTable, DefaultTableModel sessionModel) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                parent,
                "Obrisati sesiju " + sessionId + "?",
                "Potvrda",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted = controller.deleteSession(sessionId);
        if (!deleted) {
            JOptionPane.showMessageDialog(parent, "Sesija nije obrisana. Proverite da li ste u timu izvodjaca.");
            return;
        }

        removeSelectedRow(sessionTable, sessionModel);
        JOptionPane.showMessageDialog(parent, "Sesija je obrisana.");
    }

    private void changeSelectedSessionStatus(JTable sessionTable, DefaultTableModel sessionModel) {
        Integer sessionId = getSelectedId(sessionTable, "Izaberite sesiju.");
        if (sessionId == null) {
            return;
        }

        JComboBox<String> statusComboBox = new JComboBox<>(SESSION_STATUSES);
        int result = JOptionPane.showConfirmDialog(
                parent,
                statusComboBox,
                "Novi status za sesiju " + sessionId,
                JOptionPane.OK_CANCEL_OPTION
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String status = (String) statusComboBox.getSelectedItem();
        boolean updated = controller.updateSessionStatus(sessionId, status);
        if (!updated) {
            JOptionPane.showMessageDialog(parent, "Status nije promenjen. Proverite da li ste u timu izvodjaca.");
            return;
        }

        int selectedRow = sessionTable.getSelectedRow();
        sessionModel.setValueAt(status, selectedRow, 4);
        JOptionPane.showMessageDialog(parent, "Status sesije je promenjen.");
    }

    private void showRowsDialog(String title, String[] columns, List<Object[]> rows) {
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nema podataka za prikaz.");
            return;
        }

        DefaultTableModel model = createTableModel(columns);
        loadRows(model, rows);
        JDialog dialog = createDialog(title);
        dialog.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton closeButton = new JButton("Zatvori");
        closeButton.addActionListener(event -> dialog.dispose());
        actions.add(closeButton);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadRows(DefaultTableModel model, List<Object[]> rows) {
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    private JDialog createDialog(String title) {
        Frame owner = JOptionPane.getFrameForComponent(parent);
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(900, 460);
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    private Integer getSelectedId(JTable table, String message) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parent, message);
            return null;
        }

        Object value = table.getValueAt(selectedRow, 0);
        return toInteger(value);
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.parseInt(value.toString());
    }

    private void removeSelectedRow(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            model.removeRow(selectedRow);
        }
    }
}
