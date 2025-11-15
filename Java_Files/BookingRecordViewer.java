import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BookingRecordViewer extends JPanel {

    // Buttons
    private JButton viewBookingBtn, createBookingBtn, backBtn, saveBtn;

    // Text fields (only created ONCE)
    private JTextField orgIDField, locIDField, paxField, sDateField, eDateField;

    // Panels
    private JPanel panelCenter;
    private JPanel createPanel;
    private JPanel viewPanel;
    private JPanel savePanel;
    private JPanel bookingTableContainer; // where JTable will be inserted

    public BookingRecordViewer(JPanel cardPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#bfbfb2"));

        // Left side navigation buttons
        initLeftButtonPanel();

        // Center panel where create/view panels will swap
        panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(Color.decode("#bfbfb2"));
        add(panelCenter, BorderLayout.CENTER);

        // Build ALL UI pieces ONCE
        buildCreateBookingPanel();
        buildViewBookingPanel();
        buildSavePanel();

        // Container for View Booking table
        bookingTableContainer = new JPanel(new BorderLayout());
        bookingTableContainer.setBackground(Color.decode("#bfbfb2"));
    }

    // -------------------------------------------------------------
    // LEFT BUTTON PANEL
    // -------------------------------------------------------------
    private void initLeftButtonPanel() {
        JPanel panelWest = new JPanel(new GridBagLayout());
        panelWest.setBackground(Color.GRAY);

        viewBookingBtn = new JButton("View Booking");
        createBookingBtn = new JButton("Create Booking");
        backBtn = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        panelWest.add(viewBookingBtn, gbc);
        gbc.gridy++;
        panelWest.add(createBookingBtn, gbc);
        gbc.gridy++;
        panelWest.add(backBtn, gbc);

        add(panelWest, BorderLayout.WEST);
    }

    // -------------------------------------------------------------
    // CREATE BOOKING PANEL (Built ONCE)
    // -------------------------------------------------------------
    private void buildCreateBookingPanel() {
        createPanel = new JPanel(null);
        createPanel.setPreferredSize(new Dimension(600, 400));
        createPanel.setBackground(Color.decode("#bfbfb2"));

        JLabel header = new JLabel("Creating a booking");
        header.setBounds(400, 0, 300, 30);
        createPanel.add(header);

        JLabel orgLabel = new JLabel("Enter Organizer ID");
        JLabel locLabel = new JLabel("Enter Location ID");
        JLabel paxLabel = new JLabel("Number of People");
        JLabel sdateLabel = new JLabel("Start Date");
        JLabel edateLabel = new JLabel("End Date");

        orgLabel.setBounds(350, 80, 250, 20);
        locLabel.setBounds(350, 120, 250, 20);
        paxLabel.setBounds(350, 160, 250, 20);
        sdateLabel.setBounds(350, 200, 250, 20);
        edateLabel.setBounds(350, 240, 250, 20);

        createPanel.add(orgLabel);
        createPanel.add(locLabel);
        createPanel.add(paxLabel);
        createPanel.add(sdateLabel);
        createPanel.add(edateLabel);

        orgIDField = new JTextField();
        locIDField = new JTextField();
        paxField = new JTextField();
        sDateField = new JTextField();
        eDateField = new JTextField();

        orgIDField.setBounds(500, 80, 200, 20);
        locIDField.setBounds(500, 120, 200, 20);
        paxField.setBounds(500, 160, 200, 20);
        sDateField.setBounds(500, 200, 200, 20);
        eDateField.setBounds(500, 240, 200, 20);

        createPanel.add(orgIDField);
        createPanel.add(locIDField);
        createPanel.add(paxField);
        createPanel.add(sDateField);
        createPanel.add(eDateField);
    }

    // -------------------------------------------------------------
    // VIEW BOOKING PANEL (Built ONCE)
    // -------------------------------------------------------------
    private void buildViewBookingPanel() {
        viewPanel = new JPanel(null);
        viewPanel.setPreferredSize(new Dimension(600, 70));
        viewPanel.setBackground(Color.decode("#bfbfb2"));

        JLabel header = new JLabel("Viewing Bookings");
        header.setBounds(400, 10, 300, 30);
        viewPanel.add(header);
    }

    // -------------------------------------------------------------
    // SAVE BUTTON PANEL (Built ONCE)
    // -------------------------------------------------------------
    private void buildSavePanel() {
        savePanel = new JPanel(new FlowLayout());
        savePanel.setBackground(Color.decode("#bfbfb2"));
        saveBtn = new JButton("Save Booking");
        savePanel.add(saveBtn);
    }

    // -------------------------------------------------------------
    // SWITCH PANELS
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // ALLOW CONTROLLER TO INSERT A JTable
    // -------------------------------------------------------------
    public JPanel getTableContainer() {
        return bookingTableContainer;
    }

    // -------------------------------------------------------------
    // Getters for controller
    // -------------------------------------------------------------
    public JButton getViewBookingButton() { return viewBookingBtn; }
    public JButton getCreateBookingButton() { return createBookingBtn; }
    public JButton getBackButton() { return backBtn; }
    public JButton getSaveButton() { return saveBtn; }

    public JTextField getOrgIDField() { return orgIDField; }
    public JTextField getLocIDField() { return locIDField; }
    public JTextField getPaxField() { return paxField; }
    public JTextField getSDateField() { return sDateField; }
    public JTextField getEDateField() { return eDateField; }

    public JPanel getPanelCenter() { return panelCenter; }

    public void setActionListener(ActionListener listener) {
        viewBookingBtn.addActionListener(listener);
        createBookingBtn.addActionListener(listener);
        backBtn.addActionListener(listener);
        saveBtn.addActionListener(listener);
    }
}
