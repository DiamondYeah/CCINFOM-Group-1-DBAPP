import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class BookingRecordViewer extends JPanel {

    // --- Buttons ---
    private JButton viewBookingBtn, createBookingBtn, backBtn, saveBtn,
            partyBtn, viewPartiesBtn, mostVisitedBtn, viewBookingRecordsBtn, 
            editBookingBtn, deleteBookingBtn;

    // --- Text Fields ---
    private JTextField orgIDField, locIDField, paxField, sDateField, eDateField;

    // --- Panels ---
    private JPanel panelCenter;
    private JPanel createPanel;
    private JPanel viewPanel;
    private JPanel savePanel;
    private JPanel bookingTableContainer;

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
        buildViewBookingPanel();
        buildSavePanel();

        // Table container
        bookingTableContainer = new JPanel(new BorderLayout());
        bookingTableContainer.setBackground(Color.decode("#bfbfb2"));
    }

    private void initLeftButtonPanel() {
        JPanel panelWest = new JPanel(new GridBagLayout());
        panelWest.setBackground(Color.GRAY);

        

        partyBtn = new JButton("Join/Create a Party");
        viewPartiesBtn = new JButton("View Parties");
        viewBookingBtn = new JButton("View Your Bookings");
        createBookingBtn = new JButton("Create Booking");
        mostVisitedBtn = new JButton("See Most Visited Locations");
        editBookingBtn = new JButton("Edit Booking");
        viewBookingRecordsBtn = new JButton("View Booking Records");
        deleteBookingBtn = new JButton("Delete Booking");


        backBtn = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        int row = 0;
        panelWest.add(partyBtn, gbc); gbc.gridy = ++row;
        panelWest.add(mostVisitedBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewPartiesBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(createBookingBtn, gbc); gbc.gridy = ++row;
        panelWest.add(viewBookingRecordsBtn, gbc); gbc.gridy = ++row;
        panelWest.add(editBookingBtn, gbc); gbc.gridy = ++row;
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

        JLabel header = new JLabel("Create / Edit Booking");
        header.setFont(headerFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        createPanel.add(header, gbc);
        gbc.gridwidth = 1;

        orgIDField = createInputRow(createPanel, gbc, 1, "Organizer ID:", labelFont, fieldFont);
        locIDField = createInputRow(createPanel, gbc, 2, "Location ID:", labelFont, fieldFont);
        paxField = createInputRow(createPanel, gbc, 3, "Number of People:", labelFont, fieldFont);
        sDateField = createInputRow(createPanel, gbc, 4, "Start Date (YYYY-MM-DD):", labelFont, fieldFont);
        eDateField = createInputRow(createPanel, gbc, 5, "End Date (YYYY-MM-DD):", labelFont, fieldFont);
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
        JLabel header = new JLabel("Viewing Bookings");
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        viewPanel.add(header, BorderLayout.NORTH);
    }

    private void buildSavePanel() {
        savePanel = new JPanel(new FlowLayout());
        savePanel.setBackground(Color.decode("#bfbfb2"));
        saveBtn = new JButton("Save Booking");
        savePanel.add(saveBtn);
    }

    // --- Panel switches ---
    public void showCreateBooking() {
        panelCenter.removeAll();
        panelCenter.add(createPanel, BorderLayout.CENTER);
        panelCenter.add(savePanel, BorderLayout.SOUTH);
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public void showViewBooking() {
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

    // --- Action Listener ---
    public void setActionListener(ActionListener listener) {
        viewBookingBtn.addActionListener(listener);
        createBookingBtn.addActionListener(listener);
        backBtn.addActionListener(listener);
        saveBtn.addActionListener(listener);
        partyBtn.addActionListener(listener);
        viewPartiesBtn.addActionListener(listener);
        mostVisitedBtn.addActionListener(listener);
        viewBookingRecordsBtn.addActionListener(listener);
        editBookingBtn.addActionListener(listener);
        deleteBookingBtn.addActionListener(listener);
    }


    // --- Getters ---
    public JButton getViewBookingButton() { return viewBookingBtn; }
    public JButton getCreateBookingButton() { return createBookingBtn; }
    public JButton getBackButton() { return backBtn; }
    public JButton getSaveButton() { return saveBtn; }
    public JButton getPartyButton() { return partyBtn; }
    public JButton getViewPartiesButton() { return viewPartiesBtn; }
    public JButton getMostVisitedButton() { return mostVisitedBtn; }
    public JButton getViewBookingRecordsButton() { return viewBookingRecordsBtn; }
    public JButton getEditBookingBtn() { return editBookingBtn; }
    public JButton getDeleteBookingButton() { return deleteBookingBtn; }

    public JTextField getOrgIDField() { return orgIDField; }
    public JTextField getLocIDField() { return locIDField; }
    public JTextField getPaxField() { return paxField; }
    public JTextField getSDateField() { return sDateField; }
    public JTextField getEDateField() { return eDateField; }
}
