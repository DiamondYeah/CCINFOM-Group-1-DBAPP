import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.time.LocalDate;

public class BookingRecordController implements ActionListener {

    private BookingRecordViewer view;
    private BookingModel model;
    private JPanel cardPane;
    private int editingBookingID = -1;
    private MainDBController mainController;

    public BookingRecordController(Connection conn, MainDBController mainController, JPanel cardPane) {
        this.cardPane = cardPane;
        this.mainController = mainController;

        // Initialize Model and View
        this.model = new BookingModel(conn);
        this.view = new BookingRecordViewer(cardPane);

        // Attach ActionListeners
        view.setActionListener(this);

        // Add view to card layout
        cardPane.add(view, MainDBViewer.BOOKING_LINK);

        // Start with Create Booking screen
        view.showCreateBooking();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == view.getCreateBookingButton()) {
            resetCreateFields();
            view.showCreateBooking();

        } else if (src == view.getViewBookingButton()) {
            loadBookingsForCurrentUser();

        } else if (src == view.getSaveCreateButton()) {
            saveNewBooking();

        } else if (src == view.getSaveEditButton()) {
            saveEditedBooking();

        } else if (src == view.getEditBookingBtn()) {
            editBooking();

        } else if (src == view.getBackButton()) {
            CardLayout cl = (CardLayout) cardPane.getLayout();
            cl.show(cardPane, MainDBViewer.MAIN_LINK);

        } else if (src == view.getPartyButton()) {
            handlePartyAssignment();

        } else if (src == view.getViewPartiesButton()) {
            handleViewAllParties();

        } else if (src == view.getViewYourPartiesButton()) {
            handleViewYourParties();

        } else if (src == view.getLeavePartyButton()) {
            handleLeaveParty();

        } else if (src == view.getMostVisitedButton()) {
            handleMostVisitedLocations();

        } else if (src == view.getViewBookingRecordsButton()) {
            handleViewBookingRecords();

        } else if (src == view.getDeleteBookingButton()) {
            handleDeleteBooking();

        } else if (src == view.getCancelBookingButton()) {
            handleCancelBooking();

        } else if (src == view.getEditPartyRecordButton()) {
            handleEditPartyRecord();
        }
    }

    /**
     * Handle most visited locations with option to show all
     */
    private void handleMostVisitedLocations() {
        String[] options = {"Enter Date", "Show All"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Filter by date or show all most visited locations?",
            "Most Visited Locations",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {
            // Enter date
            String dateInput = JOptionPane.showInputDialog(null,
                "Enter Year (YYYY) or Month (YYYY-MM):",
                "Most Visited Locations", JOptionPane.QUESTION_MESSAGE);
            if (dateInput != null && !dateInput.isEmpty()) {
                view.showViewBooking();
                view.setTableModel(model.getMostVisitedLocations(dateInput));
            }
        } else if (choice == 1) {
            // Show all
            view.showViewBooking();
            view.setTableModel(model.getAllMostVisitedLocations());
        }
    }

    /**
     * Handle View All Parties with admin verification and location filter
     */
    private void handleViewAllParties() {
        if (mainController.isCurrentUserAdmin()) {
            if (verifyAdminPassword()) {
                // Ask if user wants to filter by location
                String[] options = {"View All Parties", "Filter by Location ID"};
                int choice = JOptionPane.showOptionDialog(
                    null,
                    "How would you like to view parties?",
                    "View Parties",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                if (choice == 0) {
                    // View all parties
                    view.showViewParties();
                    view.setTableModel(model.getUserBookingTableModel());
                } else if (choice == 1) {
                    // Filter by location
                    String locationInput = JOptionPane.showInputDialog(null,
                        "Enter Location ID:",
                        "Filter Parties by Location", JOptionPane.QUESTION_MESSAGE);
                    
                    if (locationInput != null && !locationInput.trim().isEmpty()) {
                        try {
                            int locationId = Integer.parseInt(locationInput.trim());
                            view.showViewParties();
                            view.setTableModel(model.getPartiesByLocation(locationId));
                        } catch (NumberFormatException ex) {
                            view.showMessage("Invalid Location ID. Please enter a valid number.");
                        }
                    }
                }
            }
        } else {
            view.showMessage("This function is for admins only.");
        }
    }

    /**
     * Handle View Booking Records with admin verification and filters
     */
    private void handleViewBookingRecords() {
        if (mainController.isCurrentUserAdmin()) {
            if (verifyAdminPassword()) {
                // Ask for date filter
                String[] dateOptions = {"Enter Year/Month", "Show All"};
                int dateChoice = JOptionPane.showOptionDialog(
                    null, 
                    "Filter bookings by date or show all?", 
                    "Booking Records Date Filter",
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE,
                    null, 
                    dateOptions, 
                    dateOptions[0]
                );

                String dateInput = "ALL";
                if (dateChoice == 0) {
                    while (true) {
                        String input = JOptionPane.showInputDialog(null, "Enter Year (YYYY) or Month (YYYY-MM):");
                        if (input == null) return; // User cancelled
                        if (input.matches("\\d{4}") || input.matches("\\d{4}-\\d{2}")) {
                            dateInput = input;
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Invalid format. Enter YYYY or YYYY-MM.");
                    }
                }

                // Ask for additional filter
                String[] filterOptions = {"No Additional Filter", "Filter by Location ID", "Filter by User ID"};
                int filterChoice = JOptionPane.showOptionDialog(
                    null,
                    "Apply additional filter?",
                    "Additional Filter",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    filterOptions,
                    filterOptions[0]
                );

                String filterType = null;
                String filterValue = null;

                if (filterChoice == 1) {
                    // Filter by location
                    filterType = "LOCATION";
                    filterValue = JOptionPane.showInputDialog(null, "Enter Location ID:");
                } else if (filterChoice == 2) {
                    // Filter by user
                    filterType = "USER";
                    filterValue = JOptionPane.showInputDialog(null, "Enter User ID:");
                }

                if (filterValue != null && filterValue.trim().isEmpty()) {
                    filterType = null;
                    filterValue = null;
                }

                view.showViewBooking();
                view.setTableModel(model.getBookingRecords(dateInput, filterType, filterValue));
            }
        } else {
            view.showMessage("This function is for admins only.");
        }
    }

    /**
     * Handle Delete Booking with admin verification
     */
    private void handleDeleteBooking() {
        if (!mainController.isCurrentUserAdmin()) {
            view.showMessage("This function is for admins only.");
            return;
        }

        if (!verifyAdminPassword()) {
            return;
        }

        String input = view.promptBookingID();
        if (input == null || input.isEmpty()) return;

        int bookingID;
        try {
            bookingID = Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            view.showMessage("Invalid Booking ID.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete Booking ID " + bookingID + "?\nThis will also delete all associated party records.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = model.deleteBooking(bookingID);
            if (success) {
                view.showMessage("Booking and all associated party records deleted successfully!");
                loadBookingsForCurrentUser();
            } else {
                view.showMessage("Failed to delete booking. It may not exist.");
            }
        }
    }

    /**
     * Handle Cancel Booking
     */
    private void handleCancelBooking() {
        String bookingIdInput = JOptionPane.showInputDialog(
            null,
            "Enter Booking ID to cancel:",
            "Cancel Booking",
            JOptionPane.QUESTION_MESSAGE
        );

        if (bookingIdInput == null || bookingIdInput.trim().isEmpty()) {
            return;
        }

        int bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdInput.trim());
        } catch (NumberFormatException ex) {
            view.showMessage("Invalid Booking ID.");
            return;
        }

        // Get organizer ID for this booking
        int organizerId = model.getOrganizerIdByBookingId(bookingId);
        
        if (organizerId == -1) {
            view.showMessage("Booking not found.");
            return;
        }

        // Prompt for organizer's password
        String password = view.promptPassword();
        
        if (password == null || password.isEmpty()) {
            view.showMessage("Password verification cancelled.");
            return;
        }

        // Verify the password
        if (!model.verifyUserPassword(organizerId, password)) {
            view.showMessage("Incorrect Password. Cancellation failed.");
            return;
        }

        // Cancel the booking
        boolean success = model.cancelBooking(bookingId);
        
        if (success) {
            view.showMessage("Booking cancelled successfully!");
            loadBookingsForCurrentUser();
        } else {
            view.showMessage("Failed to cancel booking.");
        }
    }

    /**
     * Handle Edit Party Record (Admin only)
     */
    private void handleEditPartyRecord() {
        if (!mainController.isCurrentUserAdmin()) {
            view.showMessage("This function is for admins only.");
            return;
        }

        if (!verifyAdminPassword()) {
            return;
        }

        // Create input panel
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField userBookingIdField = new JTextField();
        JTextField bookingIdField = new JTextField();
        JTextField userIdField = new JTextField();
        JTextField roleField = new JTextField();

        panel.add(new JLabel("User Booking ID:"));
        panel.add(userBookingIdField);
        panel.add(new JLabel("New Booking ID:"));
        panel.add(bookingIdField);
        panel.add(new JLabel("New User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("New Role:"));
        panel.add(roleField);

        int result = JOptionPane.showConfirmDialog(
            null,
            panel,
            "Edit Party Record",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int userBookingId = Integer.parseInt(userBookingIdField.getText().trim());
                int bookingId = Integer.parseInt(bookingIdField.getText().trim());
                int userId = Integer.parseInt(userIdField.getText().trim());
                String role = roleField.getText().trim();

                if (role.isEmpty()) {
                    view.showMessage("Role cannot be empty.");
                    return;
                }

                boolean success = model.updatePartyRecord(userBookingId, bookingId, userId, role);
                
                if (success) {
                    view.showMessage("Party record updated successfully!");
                    view.setTableModel(model.getUserBookingTableModel());
                } else {
                    view.showMessage("Failed to update party record. Check if values are valid and that there isn't already an organizer for this booking.");
                }
            } catch (NumberFormatException ex) {
                view.showMessage("All ID fields must be valid numbers.");
            }
        }
    }

    /**
     * Verify admin password before allowing access to admin-only functions
     * @return true if password is correct, false otherwise
     */
    private boolean verifyAdminPassword() {
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(new JLabel("Please re-enter your password to continue:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
            null,
            panel,
            "Admin Verification",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            
            if (password.isEmpty()) {
                view.showMessage("Password cannot be empty.");
                return false;
            }

            if (mainController.verifyPassword(password)) {
                return true;
            } else {
                view.showMessage("Incorrect password. Access denied.");
                return false;
            }
        }
        
        return false;
    }

    /** Load bookings only for the currently logged-in user */
    private void loadBookingsForCurrentUser() {
        view.showViewBooking();
        int currentUserId = mainController.getCurrentUser().getUserId();
        view.setTableModel(model.getBookingsForUser(currentUserId));
    }

    /** View parties where current user belongs */
    private void handleViewYourParties() {
        view.showViewParties();
        int currentUserId = mainController.getCurrentUser().getUserId();
        view.setTableModel(model.getPartiesForUser(currentUserId));
    }

    /** Create new booking with automatic price calculation and points reward */
    private void saveNewBooking() {
        try {
            int organizerID = mainController.getCurrentUser().getUserId();

            String locText = view.getLocIDField().getText().trim();
            String paxText = view.getPaxField().getText().trim();
            String startDateText = view.getSDateField().getText().trim();
            String endDateText = view.getEDateField().getText().trim();

            // Validate inputs
            if (locText.isEmpty() || paxText.isEmpty() || startDateText.isEmpty() || endDateText.isEmpty()) {
                view.showMessage("All fields must be filled.");
                return;
            }

            int locationID = Integer.parseInt(locText);
            int currentCapacity = Integer.parseInt(paxText);

            // Validate dates
            java.sql.Date startDate = java.sql.Date.valueOf(startDateText);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateText);

            // Check if dates are in the past or today
            LocalDate today = LocalDate.now();
            LocalDate startLocalDate = startDate.toLocalDate();
            LocalDate endLocalDate = endDate.toLocalDate();

            if (!startLocalDate.isAfter(today)) {
                view.showMessage("Start date must be at least 1 day after today (" + today + ").");
                return;
            }

            if (!endLocalDate.isAfter(startLocalDate)) {
                view.showMessage("End date must be after start date.");
                return;
            }

            // Check for conflicting bookings
            if (model.hasConflictingBooking(locationID, startDate, endDate)) {
                view.showMessage("This location already has a booking during the selected dates. Please choose different dates.");
                return;
            }

            // Get travel spot details to validate capacity
            BookingModel.TravelSpotDetails spotDetails = model.getTravelSpotDetails(locationID);
            
            if (spotDetails == null) {
                view.showMessage("Location ID not found. Please enter a valid Location ID.");
                return;
            }

            if (currentCapacity > spotDetails.getMaxCapacity()) {
                view.showMessage("Number of people (" + currentCapacity + ") exceeds the maximum capacity (" + 
                               spotDetails.getMaxCapacity() + ") for this location.");
                return;
            }

            // Create booking with automatic price calculation
            if (model.createBooking(organizerID, locationID, currentCapacity, startDate, endDate)) {
                int bookingID = model.getLastInsertedBookingID();
                model.assignUserToBooking(organizerID, bookingID, "Organizer");

                // Add 50 points to user for creating booking
                boolean pointsAdded = mainController.getUserRecord().getUserRecordModel().addPointsToUser(organizerID, 50);
                
                // Calculate price info for display
                long diffInMillis = endDate.getTime() - startDate.getTime();
                int numberOfDays = (int) (diffInMillis / (1000 * 60 * 60 * 24));
                double gemPrice = spotDetails.getBasePrice() * numberOfDays;
                double tax = gemPrice * 0.10;
                double totalPrice = gemPrice + tax;

                String pointsMessage = pointsAdded ? "\n\n+50 Points Added!" : "";

                view.showMessage(String.format(
                    "Booking created successfully!\n\n" +
                    "Booking ID: %d\n" +
                    "Number of Days: %d\n" +
                    "Base Price per Day: %.2f\n" +
                    "Gem Price: %.2f\n" +
                    "Tax (10%%): %.2f\n" +
                    "Total Price: %.2f\n" +
                    "Max Capacity: %d%s",
                    bookingID, numberOfDays, spotDetails.getBasePrice(), gemPrice, tax, totalPrice, spotDetails.getMaxCapacity(), pointsMessage
                ));

                clearCreateFields();
                loadBookingsForCurrentUser();
            } else {
                view.showMessage("Failed to create booking. Please check your inputs.");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("Location ID and Number of People must be valid numbers.");
        } catch (IllegalArgumentException ex) {
            view.showMessage("Dates must be in YYYY-MM-DD format.");
        } catch (Exception ex) {
            view.showMessage("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /** Begin editing a booking - Loads data into EDIT panel fields */
    private void editBooking() {
        String input = view.promptBookingID();
        if (input == null || input.isEmpty()) return;

        try {
            int bookingID = Integer.parseInt(input.trim());
            Book booking = model.getBookingById(bookingID);

            if (booking == null) {
                view.showMessage("Booking not found.");
                return;
            }

            // Get organizer ID for this booking
            int organizerID = model.getOrganizerIdByBookingId(bookingID);
            
            if (organizerID == -1) {
                view.showMessage("Unable to retrieve organizer information.");
                return;
            }

            // Prompt for organizer's password
            String password = view.promptPassword();
            
            if (password == null || password.isEmpty()) {
                view.showMessage("Password verification cancelled.");
                return;
            }

            // Verify the password
            if (!model.verifyUserPassword(organizerID, password)) {
                view.showMessage("Incorrect Password. Please try Again.");
                return;
            }

            // Load data into EDIT panel fields
            view.getEditLocIDField().setText(String.valueOf(booking.getLocationID()));
            view.getEditLocIDField().setEditable(false);

            view.getEditPaxField().setText(String.valueOf(booking.getCurrentCapacity()));
            view.getEditSDateField().setText(booking.getStartDate().toString());
            view.getEditEDateField().setText(booking.getEndDate().toString());

            editingBookingID = bookingID;
            view.showEditBooking();

        } catch (NumberFormatException ex) {
            view.showMessage("Invalid Booking ID.");
        }
    }

    /** Save edited booking via Model - Uses EDIT panel fields */
    private void saveEditedBooking() {
        try {
            int currentCapacity = Integer.parseInt(view.getEditPaxField().getText().trim());
            java.sql.Date startDate = java.sql.Date.valueOf(view.getEditSDateField().getText().trim());
            java.sql.Date endDate = java.sql.Date.valueOf(view.getEditEDateField().getText().trim());

            Book booking = model.getBookingById(editingBookingID);
            if (booking == null) {
                view.showMessage("Booking not found.");
                editingBookingID = -1;
                return;
            }

            if (endDate.before(startDate) || endDate.equals(startDate)) {
                view.showMessage("End date must be after start date.");
                return;
            }

            if (currentCapacity > booking.getMaxCapacity()) {
                view.showMessage("Current capacity cannot exceed max capacity (" + booking.getMaxCapacity() + ").");
                return;
            }

            // Update editable fields only
            booking.setCurrentCapacity(currentCapacity);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);

            if (model.updateBooking(booking)) {
                view.showMessage("Booking updated successfully!");
                editingBookingID = -1;
                clearEditFields();
                loadBookingsForCurrentUser();
            } else {
                view.showMessage("Failed to update booking.");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("Current Capacity must be a number.");
        } catch (IllegalArgumentException ex) {
            view.showMessage("Dates must be in YYYY-MM-DD format.");
        }
    }

    /** Assign user to a booking (Organizer/Participant) */
    private void handlePartyAssignment() {
        int userID = mainController.getCurrentUser().getUserId();

        String bookingIdStr = view.promptBookingID();
        if (bookingIdStr == null) return;

        try {
            int bookingID = Integer.parseInt(bookingIdStr.trim());

            // Check if user is already in this booking
            if (model.isUserInBooking(userID, bookingID)) {
                view.showMessage("You are already part of this booking!");
                return;
            }

            boolean success = model.assignUserToBooking(userID, bookingID, "Participant");
            if (success) {
                view.showMessage("Successfully joined the party!");
            } else {
                view.showMessage("Failed to join party. The booking may not exist.");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("Booking ID must be a number.");
        }
    }

    /** Clear CREATE panel input fields */
    private void clearCreateFields() {
        view.getLocIDField().setText("");
        view.getPaxField().setText("");
        view.getSDateField().setText("");
        view.getEDateField().setText("");
    }

    /** Clear EDIT panel input fields */
    private void clearEditFields() {
        view.getEditLocIDField().setText("");
        view.getEditPaxField().setText("");
        view.getEditSDateField().setText("");
        view.getEditEDateField().setText("");
        view.getEditLocIDField().setEditable(true);
    }

    private void resetCreateFields() {
        clearCreateFields();
        view.getLocIDField().setEditable(true);
    }

    private void handleLeaveParty() {
        int userId = mainController.getCurrentUser().getUserId();
        String bookingIdStr = JOptionPane.showInputDialog(
            null, "Enter Booking ID to leave:", "Leave Party", JOptionPane.QUESTION_MESSAGE
        );

        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) return;

        try {
            int bookingId = Integer.parseInt(bookingIdStr.trim());

            // Check if user is part of the booking
            String role = model.getUserRoleInBooking(userId, bookingId);
            if (role == null) {
                view.showMessage("You are not part of this booking.");
                return;
            }

            if (role.equalsIgnoreCase("Organizer")) {
                view.showMessage("You cannot leave as you are the organizer. You must delete the booking instead.");
                return;
            }

            // Remove participant
            boolean success = model.removeUserFromBooking(userId, bookingId);
            if (success) {
                view.showMessage("You have successfully left the party.");
            } else {
                view.showMessage("Failed to leave the party.");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("Booking ID must be a number.");
        }
    }
}