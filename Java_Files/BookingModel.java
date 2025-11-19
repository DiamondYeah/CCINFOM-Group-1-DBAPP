import java.sql.*;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class BookingModel {

    private Connection conn;
    private TravelRecordModel travelModel;

    public BookingModel(Connection conn) {
        this.conn = conn;
        this.travelModel = new TravelRecordModel(conn);
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
                    rs.getDouble("Price"),
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
                    rs.getString("Status"),
                    rs.getDouble("Gem_price"),
                    rs.getDouble("Tax"),
                    rs.getDouble("Gem_price") + rs.getDouble("Tax")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** Get Travel Spot details by Location ID */
    public TravelSpotDetails getTravelSpotDetails(int locationId) {
        String sql = "SELECT max_capacity, base_price FROM Travel_Spot WHERE location_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TravelSpotDetails(
                    rs.getInt("max_capacity"),
                    rs.getDouble("base_price")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** Check if location has conflicting bookings in the date range */
    public boolean hasConflictingBooking(int locationId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE Location_ID = ? AND " +
                     "((Start_date <= ? AND End_date >= ?) OR " +
                     "(Start_date <= ? AND End_date >= ?) OR " +
                     "(Start_date >= ? AND End_date <= ?))";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.setDate(2, endDate);
            stmt.setDate(3, startDate);
            stmt.setDate(4, startDate);
            stmt.setDate(5, startDate);
            stmt.setDate(6, startDate);
            stmt.setDate(7, endDate);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Create a new booking with automatic price calculation */
    public boolean createBooking(int organizerId, int locationId, int currentCapacity, 
                                 Date startDate, Date endDate) {
        // Get travel spot details
        TravelSpotDetails spotDetails = getTravelSpotDetails(locationId);
        if (spotDetails == null) {
            System.out.println("Error: Travel spot not found for location ID: " + locationId);
            return false;
        }

        // Validate capacity
        if (currentCapacity > spotDetails.getMaxCapacity()) {
            System.out.println("Error: Requested capacity exceeds maximum capacity");
            return false;
        }

        // Calculate number of days
        long diffInMillis = endDate.getTime() - startDate.getTime();
        int numberOfDays = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        
        if (numberOfDays <= 0) {
            System.out.println("Error: Invalid date range");
            return false;
        }

        // Calculate prices
        double gemPrice = spotDetails.getBasePrice() * numberOfDays;
        double tax = gemPrice * 0.10; // 10% tax

        String sql = "INSERT INTO Booking (Organizer_ID, Location_ID, Current_Capacity, Max_Capacity, " +
                     "Start_date, End_date, Gem_price, Tax, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizerId);
            stmt.setInt(2, locationId);
            stmt.setInt(3, currentCapacity);
            stmt.setInt(4, spotDetails.getMaxCapacity());
            stmt.setDate(5, startDate);
            stmt.setDate(6, endDate);
            stmt.setDouble(7, gemPrice);
            stmt.setDouble(8, tax);
            stmt.setString(9, "Booked");
            
            stmt.executeUpdate();

            try {
                travelModel.updateAvailability(locationId);
            } catch (SQLException ex) {
                System.err.println("Warning: Failed to update availability: " + ex.getMessage());
            }
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

            try {
                travelModel.updateAvailability(b.getLocationID());
            } catch (SQLException ex) {
                System.err.println("Warning: Failed to update availability: " + ex.getMessage());
            }

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
        String sql = "SELECT User_Booking_ID, Booking_ID, User_ID, Role FROM User_Booking ORDER BY Booking_ID";

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

    /** Get parties for a specific user - shows all members of bookings the user is in */
    public DefaultTableModel getPartiesForUser(int userId) {
        String[] columns = { "User Booking ID", "Booking ID", "User ID", "Role" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        // First get all booking IDs where the user is a participant
        String sql = "SELECT DISTINCT ub.User_Booking_ID, ub.Booking_ID, ub.User_ID, ub.Role " +
                     "FROM User_Booking ub " +
                     "WHERE ub.Booking_ID IN " +
                     "(SELECT Booking_ID FROM User_Booking WHERE User_ID = ?) " +
                     "ORDER BY ub.Booking_ID, ub.Role DESC, ub.User_ID";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
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

    /** Get bookings for a specific user with price information and auto-update status */
    public DefaultTableModel getBookingsForUser(int userId) {
        updateBookingStatuses(); // Auto-update statuses first
        
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Booking ID", "Location ID", "Start Date", "End Date", 
                                                "Pax", "Base Price", "Tax", "Total Price", "Status"});

        String query = "SELECT Booking_ID, Location_ID, Start_Date, End_Date, Current_Capacity, " +
                       "Gem_price, Tax, (Gem_price + Tax) as Total_Price, Status " +
                       "FROM Booking WHERE Organizer_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("Booking_ID"),
                    rs.getInt("Location_ID"),
                    rs.getDate("Start_Date"),
                    rs.getDate("End_Date"),
                    rs.getInt("Current_Capacity"),
                    rs.getDouble("Gem_price"),
                    rs.getDouble("Tax"),
                    rs.getDouble("Total_Price"),
                    rs.getString("Status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public int getLastInsertedBookingID() {
        String sql = "SELECT LAST_INSERT_ID() AS last_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("last_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
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

    /** Get all most visited locations regardless of date */
    public DefaultTableModel getAllMostVisitedLocations() {
        String[] columns = { "Booking ID", "User ID", "Location ID", "Availability", "Capacity" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String sql = "SELECT Booking_ID, Organizer_ID AS User_ID, Location_ID, Status AS Availability, Max_Capacity AS Capacity " +
                     "FROM Booking ORDER BY Max_Capacity DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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

    /** Update booking statuses based on end date */
    private void updateBookingStatuses() {
        LocalDate today = LocalDate.now();
        String sql = "UPDATE Booking SET Status = 'Completed' WHERE End_date < ? AND Status = 'Booked'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(today));
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Updated " + updated + " bookings to 'Completed' status.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** Booking records with optional date filter and auto-update status */
    public DefaultTableModel getBookingRecords(String dateInput, String filterType, String filterValue) {
        updateBookingStatuses(); // Auto-update statuses first
        
        String[] columns = { "Booking ID", "Organizer ID", "Location ID", "Price", "Current Capacity", "Max Capacity", "Start Date", "End Date", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT Booking_ID, Organizer_ID, Location_ID, Price, Current_Capacity, Max_Capacity, Start_date, End_date, Status FROM Booking WHERE 1=1"
        );

        // Add date filter
        if (dateInput != null && !dateInput.equals("ALL")) {
            if (dateInput.contains("-")) {
                sqlBuilder.append(" AND DATE_FORMAT(Start_date, '%Y-%m') = ?");
            } else {
                sqlBuilder.append(" AND YEAR(Start_date) = ?");
            }
        }

        // Add location filter
        if ("LOCATION".equals(filterType) && filterValue != null && !filterValue.trim().isEmpty()) {
            sqlBuilder.append(" AND Location_ID = ?");
        }

        // Add user filter
        if ("USER".equals(filterType) && filterValue != null && !filterValue.trim().isEmpty()) {
            sqlBuilder.append(" AND Organizer_ID = ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            
            // Set date parameter
            if (dateInput != null && !dateInput.equals("ALL")) {
                stmt.setString(paramIndex++, dateInput);
            }

            // Set filter parameter
            if (filterValue != null && !filterValue.trim().isEmpty()) {
                if ("LOCATION".equals(filterType) || "USER".equals(filterType)) {
                    stmt.setInt(paramIndex++, Integer.parseInt(filterValue.trim()));
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("Booking_ID"),
                    rs.getInt("Organizer_ID"),
                    rs.getInt("Location_ID"),
                    rs.getDouble("Price"),
                    rs.getInt("Current_Capacity"),
                    rs.getInt("Max_Capacity"),
                    rs.getDate("Start_date"),
                    rs.getDate("End_date"),
                    rs.getString("Status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            System.out.println("Invalid filter value: " + filterValue);
        }

        return model;
    }

    /** Get parties filtered by location ID */
    public DefaultTableModel getPartiesByLocation(int locationId) {
        String[] columns = { "User Booking ID", "Booking ID", "User ID", "Role" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        String sql = "SELECT ub.User_Booking_ID, ub.Booking_ID, ub.User_ID, ub.Role " +
                     "FROM User_Booking ub " +
                     "INNER JOIN Booking b ON ub.Booking_ID = b.Booking_ID " +
                     "WHERE b.Location_ID = ? " +
                     "ORDER BY ub.Booking_ID, ub.Role DESC, ub.User_ID";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            ResultSet rs = stmt.executeQuery();
            
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

    /** Assign a user to a booking with a role */
    public boolean assignUserToBooking(int userID, int bookingID, String role) {
        if ("Organizer".equalsIgnoreCase(role)) {
            String checkSql = "SELECT COUNT(*) FROM User_Booking WHERE Booking_ID = ? AND Role = 'Organizer'";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, bookingID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        int locationId = -1;
        String getLocSql = "SELECT Location_ID FROM Booking WHERE Booking_ID = ?";
        try (PreparedStatement getStmt = conn.prepareStatement(getLocSql)) {
            getStmt.setInt(1, bookingID);
            ResultSet rs = getStmt.executeQuery();
            if (rs.next())
                locationId = rs.getInt("Location_ID");

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        String insertSql = "INSERT INTO User_Booking (Booking_ID, User_ID, Role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setInt(1, bookingID);
            stmt.setInt(2, userID);
            stmt.setString(3, role);
            stmt.executeUpdate();

            if (locationId != -1) {
                try {
                    travelModel.updateAvailability(locationId);
                } catch (SQLException ex) {
                    System.err.println("Warning: Failed to update availability");
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Delete a booking by ID - now deletes related User_Booking entries first */
    public boolean deleteBooking(int bookingID) {
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // First delete all User_Booking entries for this booking
            String deleteUserBookingsSql = "DELETE FROM User_Booking WHERE Booking_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserBookingsSql)) {
                stmt.setInt(1, bookingID);
                stmt.executeUpdate();
            }
            
            // Then delete the booking itself
            String deleteBookingSql = "DELETE FROM Booking WHERE Booking_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteBookingSql)) {
                stmt.setInt(1, bookingID);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    conn.setAutoCommit(true);
                    return true;
                } else {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return false;
                }
            }
        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return false;
        }
    }

    /** Cancel a booking by ID */
    public boolean cancelBooking(int bookingID) {
        String sql = "UPDATE Booking SET Status = 'Cancelled' WHERE Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getUserRoleInBooking(int userId, int bookingId) {
        String sql = "SELECT Role FROM User_Booking WHERE User_ID = ? AND Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean removeUserFromBooking(int userId, int bookingId) {
        int locationId = -1;
        String getLocSql = "SELECT b.Location_ID FROM Booking b JOIN User_Booking ub ON b.Booking_ID = ub.Booking_ID WHERE ub.User_ID = ? AND ub.Booking_ID = ?";
        try (PreparedStatement getStmt = conn.prepareStatement(getLocSql)) {
            getStmt.setInt(1, userId);
            getStmt.setInt(2, bookingId);
            ResultSet rs = getStmt.executeQuery();
            if (rs.next()) {
                locationId = rs.getInt("Location_ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String sql = "DELETE FROM User_Booking WHERE User_ID = ? AND Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            int affected = stmt.executeUpdate();

            if(affected > 0 && locationId != -1) {
                try {
                    travelModel.updateAvailability(locationId);
                } catch (SQLException ex) {
                    System.err.println("Warning: Failed to update availability: " + ex.getMessage());
                }
            }
            return affected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isUserInBooking(int userId, int bookingId) {
        String sql = "SELECT 1 FROM User_Booking WHERE User_ID = ? AND Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Get organizer ID by booking ID */
    public int getOrganizerIdByBookingId(int bookingId) {
        String sql = "SELECT Organizer_ID FROM Booking WHERE Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Organizer_ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /** Verify user password */
    public boolean verifyUserPassword(int userId, String password) {
        String sql = "SELECT User_ID FROM User WHERE User_ID = ? AND Password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Update party record */
    public boolean updatePartyRecord(int userBookingId, int bookingId, int userId, String role) {
        // Validate organizer constraint
        if ("Organizer".equalsIgnoreCase(role)) {
            String checkSql = "SELECT COUNT(*) FROM User_Booking WHERE Booking_ID = ? AND Role = 'Organizer' AND User_Booking_ID != ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, userBookingId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Cannot have multiple organizers for one booking");
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        
        String sql = "UPDATE User_Booking SET Booking_ID = ?, User_ID = ?, Role = ? WHERE User_Booking_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);
            stmt.setString(3, role);
            stmt.setInt(4, userBookingId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Inner class to hold Travel Spot details */
    public static class TravelSpotDetails {
        private int maxCapacity;
        private double basePrice;

        public TravelSpotDetails(int maxCapacity, double basePrice) {
            this.maxCapacity = maxCapacity;
            this.basePrice = basePrice;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public double getBasePrice() {
            return basePrice;
        }
    }
}