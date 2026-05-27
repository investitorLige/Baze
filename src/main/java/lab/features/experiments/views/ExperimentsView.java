package lab.features.experiments.views;

import lab.features.experiments.controllers.ExperimentsController;
import lab.features.sessions.views.SessionsView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

public class ExperimentsView extends JPanel {
    private static final String[] LAB_COLUMNS = {
            "ID laboratorije", "Naziv", "Opis lokacije", "Max kapacitet"
    };
    private static final String[] EXPERIMENT_COLUMNS = {
            "ID eksperimenta", "Naziv", "Teorija", "Hipoteza", "Ciljna populacija", "Trajanje min"
    };
    private static final String[] EXECUTION_COLUMNS = {
            "ID izvodjenja", "Eksperiment", "Laboratorija", "Datum", "Status", "Napomena"
    };
    private static final String[] REPORT_COLUMNS = {
            "ID izvodjenja",
            "Eksperiment",
            "Laboratorija",
            "Datum",
            "Status",
            "Broj sesija",
            "Broj prisutnih",
            "Broj rezultata",
            "Ukupno resursa",
            "Broj koriscenih alata"
    };
    private static final String[] EXECUTION_STATUSES = {
            "planirano", "zapoceto", "otkazano", "zavrseno_uspesno", "zavrseno_neuspesno"
    };
    private static final String[] THEORY_COLUMNS = {
            "ID teorije", "Naziv", "Oblast", "Glavni autor", "Godina", "Opis"
    };
    private static final String[] PROTOCOL_COLUMNS = {
            "Redosled", "Naziv faze", "Trajanje min", "Uputstvo"
    };
    private static final String[] INVENTORY_COLUMNS = {
            "ID resursa", "Resurs", "Jedinica mere", "Kolicina", "Rezervisano", "Dostupno"
    };
    private static final String[] TOOL_COLUMNS = {
            "ID alata", "Tip alata", "Nabavljen", "Proizveden"
    };
    private static final String[] QUESTIONNAIRE_COLUMNS = {
            "ID upitnika", "Naziv", "Autor", "Godina", "Tip skale", "Broj pitanja", "Konstrukt", "Jezik"
    };
    private static final String[] REQUIRED_RESOURCE_COLUMNS = {
            "ID resursa", "Resurs", "Potrebna kolicina", "Jedinica mere", "Opis"
    };
    private static final String[] REQUIRED_TOOL_COLUMNS = {
            "ID tipa", "Tip alata", "Opis"
    };
    private static final String[] APPROVAL_COLUMNS = {
            "Eticki odbor", "Institucija", "Datum podnosenja", "Datum odluke", "Status", "Napomena"
    };
    private static final String[] DESIGNER_COLUMNS = {
            "ID istrazivaca", "Ime", "Prezime", "Titula", "Specijalizacija", "Institucija"
    };
    private static final String[] TEAM_COLUMNS = {
            "ID istrazivaca", "Ime", "Prezime", "Uloga", "Beleske"
    };
    private final ExperimentsController controller;
    private final DefaultTableModel executionTableModel;
    private final JTable executionTable;
    private final JComboBox<FilterItem> labFilter;
    private final JComboBox<FilterItem> experimentFilter;
    private boolean loadingFilters;

    public ExperimentsView() {
        controller = new ExperimentsController();
        executionTableModel = createTableModel(EXECUTION_COLUMNS);
        executionTable = new JTable(executionTableModel);
        labFilter = new JComboBox<>();
        experimentFilter = new JComboBox<>();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(new JScrollPane(executionTable), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);

        prepareFilters();
        loadExecutions();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel titleLabel = new JLabel("Izvodjenja");
        JButton refreshButton = new JButton("Osvezi sve");
        refreshButton.addActionListener(event -> {
            resetFilters();
            loadExecutions();
        });

        labFilter.addActionListener(event -> {
            if (!loadingFilters) {
                loadExecutions();
            }
        });
        experimentFilter.addActionListener(event -> {
            if (!loadingFilters) {
                loadExecutions();
            }
        });

        filtersPanel.add(titleLabel);
        filtersPanel.add(refreshButton);
        filtersPanel.add(new JLabel("Laboratorija:"));
        filtersPanel.add(labFilter);
        filtersPanel.add(new JLabel("Eksperiment:"));
        filtersPanel.add(experimentFilter);

        JButton experimentsButton = new JButton("Eksperimenti");
        experimentsButton.addActionListener(event -> showExperimentsDialog());

        JButton laboratoriesButton = new JButton("Laboratorije");
        laboratoriesButton.addActionListener(event -> showLaboratoriesDialog());

        rightButtonsPanel.add(experimentsButton);
        rightButtonsPanel.add(laboratoriesButton);

        headerPanel.add(filtersPanel, BorderLayout.WEST);
        headerPanel.add(rightButtonsPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createActionsPanel() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton sessionsButton = new JButton("Prikazi sesije");
        sessionsButton.addActionListener(event -> showSessionsForSelectedExecution());

        JButton statusButton = new JButton("Promeni status");
        statusButton.addActionListener(event -> changeSelectedExecutionStatus());

        JButton reportButton = new JButton("Prikazi izvestaj");
        reportButton.addActionListener(event -> showReportForSelectedExecution());

        JButton teamButton = new JButton("Prikazi tim");
        teamButton.addActionListener(event -> showTeamForSelectedExecution());

        actions.add(sessionsButton);
        actions.add(statusButton);
        actions.add(reportButton);
        actions.add(teamButton);
        return actions;
    }

    private void prepareFilters() {
        addLazyLoad(labFilter, this::loadLabFilter);
        addLazyLoad(experimentFilter, this::loadExperimentFilter);
        resetFilters();
    }

    private void addLazyLoad(JComboBox<FilterItem> comboBox, Runnable loader) {
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
                loader.run();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent event) {
            }
        });
    }

    private void resetFilters() {
        loadingFilters = true;
        labFilter.removeAllItems();
        labFilter.addItem(FilterItem.all("Sve laboratorije"));
        experimentFilter.removeAllItems();
        experimentFilter.addItem(FilterItem.all("Svi eksperimenti"));
        loadingFilters = false;
    }

    private void loadLabFilter() {
        FilterItem selected = selectedItem(labFilter);
        loadingFilters = true;
        labFilter.removeAllItems();
        labFilter.addItem(FilterItem.all("Sve laboratorije"));
        for (Object[] row : controller.findLaboratories()) {
            labFilter.addItem(new FilterItem(toInteger(row[0]), String.valueOf(row[1])));
        }
        selectById(labFilter, selected);
        loadingFilters = false;
    }

    private void loadExperimentFilter() {
        FilterItem selected = selectedItem(experimentFilter);
        loadingFilters = true;
        experimentFilter.removeAllItems();
        experimentFilter.addItem(FilterItem.all("Svi eksperimenti"));
        for (Object[] row : controller.findExperiments()) {
            experimentFilter.addItem(new FilterItem(toInteger(row[0]), String.valueOf(row[1])));
        }
        selectById(experimentFilter, selected);
        loadingFilters = false;
    }

    private void selectById(JComboBox<FilterItem> comboBox, FilterItem selected) {
        if (selected == null) {
            comboBox.setSelectedIndex(0);
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            FilterItem item = comboBox.getItemAt(i);
            if (item.matches(selected.id())) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }

        comboBox.setSelectedIndex(0);
    }

    private void loadExecutions() {
        FilterItem selectedLab = selectedItem(labFilter);
        FilterItem selectedExperiment = selectedItem(experimentFilter);
        List<Object[]> rows = controller.findExecutionsByFilters(
                selectedLab == null ? null : selectedLab.id(),
                selectedExperiment == null ? null : selectedExperiment.id()
        );
        loadRows(executionTableModel, rows);
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

    private void showSessionsForSelectedExecution() {
        Integer executionId = getSelectedId(executionTable, "Izaberite izvodjenje.");
        if (executionId == null) {
            return;
        }

        SessionsView.showForExecution(this, executionId);
    }

    private void showExperimentsDialog() {
        DefaultTableModel experimentModel = createTableModel(EXPERIMENT_COLUMNS);
        loadRows(experimentModel, controller.findExperiments());
        JTable experimentTable = new JTable(experimentModel);

        JDialog dialog = createDialog("Eksperimenti", 1100, 520);
        dialog.add(new JScrollPane(experimentTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton theoryButton = new JButton("Prikazi teoriju");
        theoryButton.addActionListener(event -> showTheoryForSelectedExperiment(experimentTable));

        JButton protocolButton = new JButton("Prikazi protokol");
        protocolButton.addActionListener(event -> showProtocolForSelectedExperiment(experimentTable));

        JButton questionnairesButton = new JButton("Prikazi upitnike");
        questionnairesButton.addActionListener(event -> showQuestionnairesForSelectedExperiment(experimentTable));

        JButton resourcesButton = new JButton("Prikazi potrebne resurse");
        resourcesButton.addActionListener(event -> showRequiredResourcesForSelectedExperiment(experimentTable));

        JButton toolsButton = new JButton("Prikazi potrebne alate");
        toolsButton.addActionListener(event -> showRequiredToolsForSelectedExperiment(experimentTable));

        JButton approvalsButton = new JButton("Prikazi odobrenja");
        approvalsButton.addActionListener(event -> showApprovalsForSelectedExperiment(experimentTable));

        JButton designersButton = new JButton("Prikazi dizajnere");
        designersButton.addActionListener(event -> showDesignersForSelectedExperiment(experimentTable));

        JButton closeButton = new JButton("Zatvori");
        closeButton.addActionListener(event -> dialog.dispose());

        actions.add(theoryButton);
        actions.add(protocolButton);
        actions.add(questionnairesButton);
        actions.add(resourcesButton);
        actions.add(toolsButton);
        actions.add(approvalsButton);
        actions.add(designersButton);
        actions.add(closeButton);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showLaboratoriesDialog() {
        DefaultTableModel labModel = createTableModel(LAB_COLUMNS);
        loadRows(labModel, controller.findLaboratories());
        JTable labTable = new JTable(labModel);

        JDialog dialog = createDialog("Laboratorije", 1000, 500);
        dialog.add(new JScrollPane(labTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton inventoryButton = new JButton("Prikazi inventar");
        inventoryButton.addActionListener(event -> showInventoryForSelectedLab(labTable));

        JButton toolsButton = new JButton("Prikazi alate");
        toolsButton.addActionListener(event -> showToolsForSelectedLab(labTable));

        JButton executionsButton = new JButton("Prikazi izvodjenja");
        executionsButton.addActionListener(event -> showExecutionsForSelectedLab(labTable));

        JButton closeButton = new JButton("Zatvori");
        closeButton.addActionListener(event -> dialog.dispose());

        actions.add(inventoryButton);
        actions.add(toolsButton);
        actions.add(executionsButton);
        actions.add(closeButton);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showTheoryForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Teorija za eksperiment " + experimentId,
                THEORY_COLUMNS,
                controller.findTheoryByExperiment(experimentId)
        );
    }

    private void showProtocolForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Protokol za eksperiment " + experimentId,
                PROTOCOL_COLUMNS,
                controller.findProtocolByExperiment(experimentId)
        );
    }

    private void showQuestionnairesForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Upitnici za eksperiment " + experimentId,
                QUESTIONNAIRE_COLUMNS,
                controller.findQuestionnairesByExperiment(experimentId)
        );
    }

    private void showRequiredResourcesForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Potrebni resursi za eksperiment " + experimentId,
                REQUIRED_RESOURCE_COLUMNS,
                controller.findRequiredResourcesByExperiment(experimentId)
        );
    }

    private void showRequiredToolsForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Potrebni alati za eksperiment " + experimentId,
                REQUIRED_TOOL_COLUMNS,
                controller.findRequiredToolsByExperiment(experimentId)
        );
    }

    private void showApprovalsForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Odobrenja za eksperiment " + experimentId,
                APPROVAL_COLUMNS,
                controller.findApprovalsByExperiment(experimentId)
        );
    }

    private void showDesignersForSelectedExperiment(JTable experimentTable) {
        Integer experimentId = getSelectedId(experimentTable, "Izaberite eksperiment.");
        if (experimentId == null) {
            return;
        }

        showRowsDialog(
                "Dizajneri eksperimenta " + experimentId,
                DESIGNER_COLUMNS,
                controller.findDesignersByExperiment(experimentId)
        );
    }

    private void showInventoryForSelectedLab(JTable labTable) {
        Integer labId = getSelectedId(labTable, "Izaberite laboratoriju.");
        if (labId == null) {
            return;
        }

        showRowsDialog(
                "Inventar laboratorije " + labId,
                INVENTORY_COLUMNS,
                controller.findInventoryByLab(labId)
        );
    }

    private void showToolsForSelectedLab(JTable labTable) {
        Integer labId = getSelectedId(labTable, "Izaberite laboratoriju.");
        if (labId == null) {
            return;
        }

        showRowsDialog(
                "Alati laboratorije " + labId,
                TOOL_COLUMNS,
                controller.findToolsByLab(labId)
        );
    }

    private void showExecutionsForSelectedLab(JTable labTable) {
        Integer labId = getSelectedId(labTable, "Izaberite laboratoriju.");
        if (labId == null) {
            return;
        }

        showRowsDialog(
                "Izvodjenja laboratorije " + labId,
                EXECUTION_COLUMNS,
                controller.findExecutionsByLab(labId)
        );
    }

    private void showTeamForSelectedExecution() {
        Integer executionId = getSelectedId(executionTable, "Izaberite izvodjenje.");
        if (executionId == null) {
            return;
        }

        showRowsDialog(
                "Tim izvodjaca za izvodjenje " + executionId,
                TEAM_COLUMNS,
                controller.findTeamByExecution(executionId)
        );
    }

    private void showRowsDialog(String title, String[] columns, List<Object[]> rows) {
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nema podataka za prikaz.");
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

    private void changeSelectedExecutionStatus() {
        Integer executionId = getSelectedId(executionTable, "Izaberite izvodjenje.");
        if (executionId == null) {
            return;
        }

        JComboBox<String> statusComboBox = new JComboBox<>(EXECUTION_STATUSES);
        int result = JOptionPane.showConfirmDialog(
                this,
                statusComboBox,
                "Novi status za izvodjenje " + executionId,
                JOptionPane.OK_CANCEL_OPTION
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String status = (String) statusComboBox.getSelectedItem();
        boolean updated = controller.updateExecutionStatus(executionId, status);
        if (!updated) {
            JOptionPane.showMessageDialog(this, "Status nije promenjen. Proverite da li ste vezani za izvodjenje.");
            return;
        }

        int selectedRow = executionTable.getSelectedRow();
        executionTableModel.setValueAt(status, selectedRow, 4);
        JOptionPane.showMessageDialog(this, "Status je promenjen.");
    }

    private void showReportForSelectedExecution() {
        Integer executionId = getSelectedId(executionTable, "Izaberite izvodjenje.");
        if (executionId == null) {
            return;
        }

        List<Object[]> rows = controller.getExecutionReport(executionId);
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nema podataka za izvestaj.");
            return;
        }

        Object[] row = rows.get(0);
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < REPORT_COLUMNS.length; i++) {
            report.append(REPORT_COLUMNS[i]).append(": ").append(row[i]).append(System.lineSeparator());
        }

        JTextArea reportArea = new JTextArea(report.toString(), 14, 60);
        reportArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(reportArea), "Izvestaj izvodjenja", JOptionPane.INFORMATION_MESSAGE);
    }

    private JDialog createDialog(String title) {
        return createDialog(title, 900, 460);
    }

    private JDialog createDialog(String title, int width, int height) {
        Frame owner = JOptionPane.getFrameForComponent(this);
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        return dialog;
    }

    private Integer getSelectedId(JTable table, String message) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, message);
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

    private FilterItem selectedItem(JComboBox<FilterItem> comboBox) {
        Object selected = comboBox.getSelectedItem();
        if (selected instanceof FilterItem item && item.id() != null) {
            return item;
        }
        return null;
    }

    private record FilterItem(Integer id, String label) {
        static FilterItem all(String label) {
            return new FilterItem(null, label);
        }

        boolean matches(Integer otherId) {
            return id == null ? otherId == null : id.equals(otherId);
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
