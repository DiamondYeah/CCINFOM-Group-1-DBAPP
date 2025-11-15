import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BookingRecordController implements ActionListener {

    private BookingRecordViewer view;
    private MainDBController mainController;
    private JPanel cardPane;
    private Connection conn;  // use the real DB connection
    private Scanner sc;

    public BookingRecordController(Connection conn, MainDBController mainController, JPanel cardPane) {
        this.conn = conn;
        this.mainController = mainController;
        this.cardPane = cardPane;

        // Initialize VIEW
        this.view = new BookingRecordViewer(cardPane);

        // Attach action listeners
        this.view.setActionListener(this);

        // Add viewer to card layout
        cardPane.add(view, MainDBViewer.BOOKING_LINK);

        // Start on Create Booking screen
        view.showCreateBooking();

        // Scanner for console input
        sc = new Scanner(System.in);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == view.getCreateBookingButton()) {
            view.showCreateBooking();
        } else if (src == view.getViewBookingButton()) {
            showBookingsTable();
        } else if (src == view.getSaveButton()) {
            saveBooking();
        } else if (src == view.getBackButton()) {
            CardLayout cl = (CardLayout) cardPane.getLayout();
            cl.show(cardPane, MainDBViewer.MAIN_LINK);
        }
    }

    /** ------------------ VIEW BOOKINGS TABLE ------------------ **/
    public void showBookingsTable() {
        view.showViewBooking();

        String sql = "SELECT Booking_ID, Organizer_ID, Location_ID, " +
                     "Current_Capacity, Start_date, End_date, Booking_Dates " +
                     "FROM Booking";

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Organizer ID");
        tableModel.addColumn("Location ID");
        tableModel.addColumn("Current Capacity");
        tableModel.addColumn("Start Date");
        tableModel.addColumn("End Date");
        tableModel.addColumn("Booking Period");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("Booking_ID"),
                        rs.getInt("Organizer_ID"),
                        rs.getInt("Location_ID"),
                        rs.getInt("Current_Capacity"),
                        rs.getDate("Start_date"),
                        rs.getDate("End_date"),
                        rs.getString("Booking_Dates")
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load bookings from database.");
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel center = view.getPanelCenter();
        center.removeAll();
        center.add(scrollPane, BorderLayout.CENTER);
        center.revalidate();
        center.repaint();
    }

    /** ------------------ SAVE BOOKING ------------------ **/
    private void saveBooking() {
        try {
            int organizerID = Integer.parseInt(view.getOrgIDField().getText());
            int locationID = Integer.parseInt(view.getLocIDField().getText());
            int pax = Integer.parseInt(view.getPaxField().getText());
            String startDate = view.getSDateField().getText();
            String endDate = view.getEDateField().getText();

            String sql = "INSERT INTO Booking (Organizer_ID, Location_ID, Current_Capacity, Start_date, End_date, Max_Capacity, Status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, organizerID);
                stmt.setInt(2, locationID);
                stmt.setInt(3, pax); // current capacity
                stmt.setString(4, startDate);
                stmt.setString(5, endDate);
                stmt.setInt(6, pax); // max capacity same as pax initially
                stmt.setString(7, "Confirmed"); // default status
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Booking saved successfully!");

            // Clear input fields
            view.getOrgIDField().setText("");
            view.getLocIDField().setText("");
            view.getPaxField().setText("");
            view.getSDateField().setText("");
            view.getEDateField().setText("");

            // Refresh table
            showBookingsTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Please enter valid numbers for Organizer ID, Location ID, and Pax.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save booking to database.");
        }
    }

    /** ------------------ CONSOLE VIEW ------------------ **/
    public void runConsole() {
        while (true) {
            System.out.println("\n--- Booking Console ---");
            System.out.println("1. Create Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            String input = sc.nextLine();

            switch (input) {
                case "1":
                    consoleCreateBooking();
                    break;
                case "2":
                    consoleViewBookings();
                    break;
                case "3":
                    System.out.println("Exiting console...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void consoleCreateBooking() {
        try {
            System.out.print("Organizer ID: ");
            int organizerID = Integer.parseInt(sc.nextLine());

            System.out.print("Location ID: ");
            int locationID = Integer.parseInt(sc.nextLine());

            System.out.print("Number of people (Pax): ");
            int pax = Integer.parseInt(sc.nextLine());

            System.out.print("Start Date (YYYY-MM-DD): ");
            String startDate = sc.nextLine();

            System.out.print("End Date (YYYY-MM-DD): ");
            String endDate = sc.nextLine();

            String sql = "INSERT INTO Booking (Organizer_ID, Location_ID, Current_Capacity, Start_date, End_date, Max_Capacity, Status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, organizerID);
                stmt.setInt(2, locationID);
                stmt.setInt(3, pax);
                stmt.setString(4, startDate);
                stmt.setString(5, endDate);
                stmt.setInt(6, pax);
                stmt.setString(7, "Confirmed");
                stmt.executeUpdate();
            }

            System.out.println("Booking saved successfully!");

        } catch (NumberFormatException ex) {
            System.out.println("Invalid input. Organizer ID, Location ID, and Pax must be numbers.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to save booking to database.");
        }
    }

    private void consoleViewBookings() {
        String sql = "SELECT Booking_ID, Organizer_ID, Location_ID, Current_Capacity, Start_date, End_date, Booking_Dates FROM Booking";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%-10s %-12s %-12s %-5s %-12s %-12s %-20s%n",
                    "BookingID", "OrganizerID", "LocationID", "Pax",
                    "StartDate", "EndDate", "BookingPeriod");

            while (rs.next()) {
                System.out.printf("%-10d %-12d %-12d %-5d %-12s %-12s %-20s%n",
                        rs.getInt("Booking_ID"),
                        rs.getInt("Organizer_ID"),
                        rs.getInt("Location_ID"),
                        rs.getInt("Current_Capacity"),
                        rs.getDate("Start_date"),
                        rs.getDate("End_date"),
                        rs.getString("Booking_Dates"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load bookings from database.");
        }
    }
}
