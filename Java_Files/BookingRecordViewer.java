import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class BookingRecordViewer extends JPanel {

    // --- Buttons ---
    private JButton viewBookingBtn, createBookingBtn, backBtn, saveCreateBtn, saveEditBtn,
            partyBtn, viewPartiesBtn, viewYourPartiesBtn, mostVisitedBtn, viewBookingRecordsBtn, 
            editBookingBtn, deleteBookingBtn, leavePartyBtn, cancelBookingBtn, editPartyRecordBtn;

    // --- Text Fields for CREATE panel ---
    private JTextField createLocIDField, createPaxField, createSDateField, createEDateField;

    // --- Text Fields for EDIT panel ---
    private JTextField editLocIDField, editPaxField, editSDateField, editEDateField;

    // --- Panels ---
    private JPanel panelCenter, createPanel, editPanel, viewPanel, savePanelCreate, savePanelEdit, bookingTableContainer;

    // --- Header label for view panel ---
    private JLabel viewHeaderLabel;

    public BookingRecordViewer(JPanel cardPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#bfbfb2"));

        // Build left navigation panel
        initLeftButtonPanel();

        // Center container
        panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(Color.decode("#bfbfb2"));
        add(panelCenter, BorderLayout.CENTER);

        // Build panels
        buildCreateBookingPanel();
        buildEditBookingPanel();
        buildViewBookingPanel();
        buildSavePanels();

        // Table container
        bookingTableContainer = new JPanel(new BorderLayout());
        bookingTableContainer.setBackground(Color.decode("#bfbfb2"));
    }

    private void initLeftButtonPanel() {
        JPanel panelWest = new JPanel(new GridBagLayout());
        panelWest.setBackground(Color.GRAY);

        partyBtn = new JButton("Join a Party");
        leavePartyBtn = new JButton("Leave Party");
        viewYourPartiesBtn = new JButton("View Your Parties");
        viewPartiesBtn = new JButton("View All Parties");
        editPartyRecordBtn = new JButton("Edit Party Record");
        viewBookingBtn = new JButton("View Your Bookings");
        createBookingBtn = new JButton("Create Booking");
        mostVisitedBtn = new JButton("See Most Visited Locations");
        editBookingBtn = new JButton("Edit Booking");
        cancelBookingBtn = new JButton("Cancel Booking");
        viewBookingRecordsBtn = new JButton("View Booking Records");
        deleteBookingBtn = new JButton("Delete Booking");

        backBtn = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        int row = 0;
        panelWest.add(mostVisitedBtn, gbc); gbc.gridy = ++row;
        panelWest.add(partyBtn, gbc); gbc.gridy = ++row;
        panelWest.add(leavePartyBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewYourPartiesBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewPartiesBtn, gbc); gbc.gridy = ++row;
        panelWest.add(editPartyRecordBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(createBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewBookingRecordsBtn, gbc); gbc.gridy = ++row;
        panelWest.add(editBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(cancelBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(deleteBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(backBtn, gbc);

        add(panelWest, BorderLayout.WEST);
    }

    private void buildCreateBookingPanel() {
        createPanel = new JPanel(new GridBagLayout());
        createPanel.setBackground(Color.decode("#bfbfb2"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fonts
        Font headerFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 20);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        JLabel header = new JLabel("Create Booking");
        header.setFont(headerFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        createPanel.add(header, gbc);
        gbc.gridwidth = 1;

        int row = 1;
        createLocIDField = createInputRow(createPanel, gbc, row++, "Location ID:", labelFont, fieldFont);
        createPaxField = createInputRow(createPanel, gbc, row++, "Number of People:", labelFont, fieldFont);
        createSDateField = createInputRow(createPanel, gbc, row++, "Start Date (YYYY-MM-DD):", labelFont, fieldFont);
        createEDateField = createInputRow(createPanel, gbc, row++, "End Date (YYYY-MM-DD):", labelFont, fieldFont);
    }

    private void buildEditBookingPanel() {
        editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.decode("#bfbfb2"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fonts
        Font headerFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 20);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        JLabel header = new JLabel("Edit Booking");
        header.setFont(headerFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        editPanel.add(header, gbc);
        gbc.gridwidth = 1;

        int row = 1;
        editLocIDField = createInputRow(editPanel, gbc, row++, "Location ID:", labelFont, fieldFont);
        editPaxField = createInputRow(editPanel, gbc, row++, "Number of People:", labelFont, fieldFont);
        editSDateField = createInputRow(editPanel, gbc, row++, "Start Date (YYYY-MM-DD):", labelFont, fieldFont);
        editEDateField = createInputRow(editPanel, gbc, row++, "End Date (YYYY-MM-DD):", labelFont, fieldFont);
    }

    private JTextField createInputRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Font labelFont, Font fieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        JTextField field = new JTextField(18);
        field.setFont(fieldFont);
        field.setPreferredSize(new Dimension(250, 35));

        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);

        return field;
    }

    private void buildViewBookingPanel() {
        viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBackground(Color.decode("#bfbfb2"));
        viewHeaderLabel = new JLabel("Viewing Bookings");
        viewHeaderLabel.setFont(new Font("Arial", Font.BOLD, 22));
        viewHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        viewPanel.add(viewHeaderLabel, BorderLayout.NORTH);
    }

    private void buildSavePanels() {
        // Create separate buttons for Create and Edit
        savePanelCreate = new JPanel(new FlowLayout());
        savePanelCreate.setBackground(Color.decode("#bfbfb2"));
        saveCreateBtn = new JButton("Save Booking");
        savePanelCreate.add(saveCreateBtn);

        savePanelEdit = new JPanel(new FlowLayout());
        savePanelEdit.setBackground(Color.decode("#bfbfb2"));
        saveEditBtn = new JButton("Save Changes");
        savePanelEdit.add(saveEditBtn);
    }

    // --- Panel switches ---
    public void showCreateBooking() {
        panelCenter.removeAll();
        panelCenter.add(createPanel, BorderLayout.CENTER);
        panelCenter.add(savePanelCreate, BorderLayout.SOUTH);
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public void showEditBooking() {
        panelCenter.removeAll();
        panelCenter.add(editPanel, BorderLayout.CENTER);
        panelCenter.add(savePanelEdit, BorderLayout.SOUTH);
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public void showViewBooking() {
        viewHeaderLabel.setText("Viewing Bookings");
        panelCenter.removeAll();
        panelCenter.add(viewPanel, BorderLayout.NORTH);
        panelCenter.add(bookingTableContainer, BorderLayout.CENTER);
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public void showViewParties() {
        viewHeaderLabel.setText("Viewing Parties");
        panelCenter.removeAll();
        panelCenter.add(viewPanel, BorderLayout.NORTH);
        panelCenter.add(bookingTableContainer, BorderLayout.CENTER);
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    // --- Table management ---
    public void setTableModel(DefaultTableModel model) {
        bookingTableContainer.removeAll();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        bookingTableContainer.add(scrollPane, BorderLayout.CENTER);
        bookingTableContainer.revalidate();
        bookingTableContainer.repaint();
    }

    // --- Dialogs ---
    public int promptRoleSelection() {
        String[] options = {"Organizer", "Participant"};
        return JOptionPane.showOptionDialog(
                this, "Select your role:", "Role Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    public String promptBookingID() {
        return JOptionPane.showInputDialog(this, "Enter Booking ID:");
    }

    public boolean showOrganizerExistsDialog() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Organizer is already present. Go back?", "Duplicate Organizer",
                JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public String promptBookingRecordsDate() {
        String[] options = {"Enter Year/Month", "Show All"};
        int choice = JOptionPane.showOptionDialog(
                this, "Filter bookings by date or show all?", "Booking Records Filter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) return "ALL";
        if (choice == 0) {
            while (true) {
                String input = JOptionPane.showInputDialog(this, "Enter Year (YYYY) or Month (YYYY-MM):");
                if (input == null) return null;
                if (input.matches("\\d{4}") || input.matches("\\d{4}-\\d{2}")) return input;
                JOptionPane.showMessageDialog(this, "Invalid format. Enter YYYY or YYYY-MM.");
            }
        }
        return null;
    }

    /** Prompt for password verification */
    public String promptPassword() {
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(new JLabel("Enter the organizer's password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Password Verification",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    // --- Action Listener ---
    public void setActionListener(ActionListener listener) {
        viewBookingBtn.addActionListener(listener);
        createBookingBtn.addActionListener(listener);
        backBtn.addActionListener(listener);
        saveCreateBtn.addActionListener(listener);
        saveEditBtn.addActionListener(listener);
        partyBtn.addActionListener(listener);
        viewPartiesBtn.addActionListener(listener);
        viewYourPartiesBtn.addActionListener(listener);
        mostVisitedBtn.addActionListener(listener);
        viewBookingRecordsBtn.addActionListener(listener);
        editBookingBtn.addActionListener(listener);
        deleteBookingBtn.addActionListener(listener);
        leavePartyBtn.addActionListener(listener);
        cancelBookingBtn.addActionListener(listener);
        editPartyRecordBtn.addActionListener(listener);
    }

    // --- Getters ---
    public JButton getViewBookingButton() { return viewBookingBtn; }
    public JButton getCreateBookingButton() { return createBookingBtn; }
    public JButton getBackButton() { return backBtn; }
    public JButton getSaveCreateButton() { return saveCreateBtn; }
    public JButton getSaveEditButton() { return saveEditBtn; }
    public JButton getPartyButton() { return partyBtn; }
    public JButton getViewPartiesButton() { return viewPartiesBtn; }
    public JButton getViewYourPartiesButton() { return viewYourPartiesBtn; }
    public JButton getMostVisitedButton() { return mostVisitedBtn; }
    public JButton getViewBookingRecordsButton() { return viewBookingRecordsBtn; }
    public JButton getEditBookingBtn() { return editBookingBtn; }
    public JButton getDeleteBookingButton() { return deleteBookingBtn; }
    public JButton getLeavePartyButton() { return leavePartyBtn; }
    public JButton getCancelBookingButton() { return cancelBookingBtn; }
    public JButton getEditPartyRecordButton() { return editPartyRecordBtn; }

    // Getters for CREATE fields
    public JTextField getLocIDField() { return createLocIDField; }
    public JTextField getPaxField() { return createPaxField; }
    public JTextField getSDateField() { return createSDateField; }
    public JTextField getEDateField() { return createEDateField; }

    // Getters for EDIT fields
    public JTextField getEditLocIDField() { return editLocIDField; }
    public JTextField getEditPaxField() { return editPaxField; }
    public JTextField getEditSDateField() { return editSDateField; }
    public JTextField getEditEDateField() { return editEDateField; }
}