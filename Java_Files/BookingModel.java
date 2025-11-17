import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BookingModel {

    private Connection conn;

    public BookingModel(Connection conn) {
        this.conn = conn;
    }

    /** Fetch all bookings as table model */
    public DefaultTableModel getBookingTableModel() {
        String[] columns = {
            "Booking ID", "Organizer ID", "Location ID", "Price",
            "Current Capacity", "Max Capacity", "Start Date", "End Date", "Status"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String sql = "SELECT Booking_ID, Organizer_ID, Location_ID, Price, Current_Capacity, Max_Capacity, Start_date, End_date, Status FROM Booking";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("Booking_ID"),
                    rs.getInt("Organizer_ID"),
                    rs.getInt("Location_ID"),
                    rs.getInt("Price"),
                    rs.getInt("Current_Capacity"),
                    rs.getInt("Max_Capacity"),
                    rs.getDate("Start_date"),
                    rs.getDate("End_date"),
                    rs.getString("Status")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return model;
    }

    /** Fetch single booking by ID */
    public Book getBookingById(int bookingID) {
        String sql = "SELECT * FROM Booking WHERE Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                    rs.getInt("Booking_ID"),
                    rs.getInt("Organizer_ID"),
                    rs.getInt("Location_ID"),
                    rs.getInt("Current_Capacity"),
                    rs.getInt("Max_Capacity"),
                    rs.getDate("Start_date"),
                    rs.getDate("End_date"),
                    rs.getString("Status")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** Create a new booking */
    public boolean createBooking(Book b) {
        String sql = "INSERT INTO Booking (Organizer_ID, Location_ID, Current_Capacity, Max_Capacity, Start_date, End_date, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getOrganizerID());
            stmt.setInt(2, b.getLocationID());
            stmt.setInt(3, b.getCurrentCapacity());
            stmt.setInt(4, b.getMaxCapacity());
            stmt.setDate(5, b.getStartDate());
            stmt.setDate(6, b.getEndDate());
            stmt.setString(7, b.getStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Update existing booking */
    public boolean updateBooking(Book b) {
        String sql = "UPDATE Booking SET Current_Capacity = ?, Start_date = ?, End_date = ? WHERE Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getCurrentCapacity());
            stmt.setDate(2, b.getStartDate());
            stmt.setDate(3, b.getEndDate());
            stmt.setInt(4, b.getBookingID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /** User_Booking table for parties */
    public DefaultTableModel getUserBookingTableModel() {
        String[] columns = { "User Booking ID", "Booking ID", "User ID", "Role" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        String sql = "SELECT User_Booking_ID, Booking_ID, User_ID, Role FROM User_Booking";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("User_Booking_ID"),
                    rs.getInt("Booking_ID"),
                    rs.getInt("User_ID"),
                    rs.getString("Role")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return model;
    }

    /** Most visited locations by year/month */
    public DefaultTableModel getMostVisitedLocations(String dateInput) {
        String[] columns = { "Booking ID", "User ID", "Location ID", "Availability", "Capacity" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String sql;
        boolean isMonth = dateInput.contains("-");

        if (isMonth) {
            sql = "SELECT Booking_ID, Organizer_ID AS User_ID, Location_ID, Status AS Availability, Max_Capacity AS Capacity " +
                  "FROM Booking WHERE DATE_FORMAT(Start_date, '%Y-%m') = ? ORDER BY Max_Capacity DESC";
        } else {
            sql = "SELECT Booking_ID, Organizer_ID AS User_ID, Location_ID, Status AS Availability, Max_Capacity AS Capacity " +
                  "FROM Booking WHERE YEAR(Start_date) = ? ORDER BY Max_Capacity DESC";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateInput);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("Booking_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Location_ID"),
                    rs.getString("Availability"),
                    rs.getInt("Capacity")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return model;
    }

    /** Booking records with optional date filter */
    public DefaultTableModel getBookingRecords(String dateInput) {
        String[] columns = { "Booking ID", "Organizer ID", "Location ID", "Price", "Current Capacity", "Max Capacity", "Start Date", "End Date", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String sql;
        if (dateInput == null || dateInput.equals("ALL")) {
            sql = "SELECT Booking_ID, Organizer_ID, Location_ID, Price, Current_Capacity, Max_Capacity, Start_date, End_date, Status FROM Booking";
        } else if (dateInput.contains("-")) {
            sql = "SELECT Booking_ID, Organizer_ID, Location_ID, Price, Current_Capacity, Max_Capacity, Start_date, End_date, Status " +
                  "FROM Booking WHERE DATE_FORMAT(Start_date, '%Y-%m') = ?";
        } else {
            sql = "SELECT Booking_ID, Organizer_ID, Location_ID, Price, Current_Capacity, Max_Capacity, Start_date, End_date, Status " +
                  "FROM Booking WHERE YEAR(Start_date) = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!"ALL".equals(dateInput)) stmt.setString(1, dateInput);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("Booking_ID"),
                    rs.getInt("Organizer_ID"),
                    rs.getInt("Location_ID"),
                    rs.getInt("Price"),
                    rs.getInt("Current_Capacity"),
                    rs.getInt("Max_Capacity"),
                    rs.getDate("Start_date"),
                    rs.getDate("End_date"),
                    rs.getString("Status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return model;
    }

    /**
     * Assign a user to a booking with a role ("Organizer" or "Participant")
     */
    public boolean assignUserToBooking(int userID, int bookingID, String role) {
        // Check if an organizer already exists if role is "Organizer"
        if ("Organizer".equalsIgnoreCase(role)) {
            String checkSql = "SELECT COUNT(*) FROM User_Booking WHERE Booking_ID = ? AND Role = 'Organizer'";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, bookingID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // organizer already exists
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        // Insert into User_Booking table
        String insertSql = "INSERT INTO User_Booking (Booking_ID, User_ID, Role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setInt(1, bookingID);
            stmt.setInt(2, userID);
            stmt.setString(3, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a booking by ID
     */
    public boolean deleteBooking(int bookingID) {
        // Check if users are assigned
        String checkSql = "SELECT COUNT(*) FROM User_Booking WHERE Booking_ID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, bookingID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Cannot delete booking with assigned users
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        // Proceed to delete
        String deleteSql = "DELETE FROM Booking WHERE Booking_ID = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, bookingID);
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
