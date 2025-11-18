import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

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

        // clearBookingFields();

        if (src == view.getCreateBookingButton()) {
            resetCreateFields();
            view.showCreateBooking();

        } else if (src == view.getViewBookingButton()) {
            loadBookingsForCurrentUser();

        } else if (src == view.getSaveButton()) {
            if (editingBookingID != -1) {
                saveEditedBooking();
            } else {
                saveNewBooking();
            }

        } else if (src == view.getEditBookingBtn()) {
            editBooking();

        } else if (src == view.getBackButton()) {
            CardLayout cl = (CardLayout) cardPane.getLayout();
            cl.show(cardPane, MainDBViewer.MAIN_LINK);

        } else if (src == view.getPartyButton()) {
            handlePartyAssignment();

        } else if (src == view.getViewPartiesButton()) {
            view.showViewBooking();
            view.setTableModel(model.getUserBookingTableModel());

        } else if (src == view.getLeavePartyButton()) {
            handleLeaveParty();
        } else if (src == view.getMostVisitedButton()) {
            String dateInput = JOptionPane.showInputDialog(null,
                    "Enter Year (YYYY) or Month (YYYY-MM):",
                    "Most Visited Locations", JOptionPane.QUESTION_MESSAGE);
            if (dateInput != null && !dateInput.isEmpty()) {
                view.showViewBooking();
                view.setTableModel(model.getMostVisitedLocations(dateInput));
            }

        } else if (src == view.getViewBookingRecordsButton()) {
            String dateInput = view.promptBookingRecordsDate();
            if (dateInput != null) {
                view.showViewBooking();
                view.setTableModel(model.getBookingRecords(dateInput));
            }

        } else if (src == view.getDeleteBookingButton()) {
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
                "Are you sure you want to delete Booking ID " + bookingID + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = model.deleteBooking(bookingID);
                if (success) {
                    view.showMessage("Booking deleted successfully!");
                    loadBookingsForCurrentUser(); // refresh only current user's bookings
                } else {
                    view.showMessage("Failed to delete booking. It may not exist.");
                }
            }
        }
    }

    /** Load bookings only for the currently logged-in user */
    private void loadBookingsForCurrentUser() {
        view.showViewBooking();
        int currentUserId = mainController.getCurrentUser().getUserId();
        view.setTableModel(model.getBookingsForUser(currentUserId));
    }

    /** Create new booking via Model */
    private void saveNewBooking() {
    try {
        int organizerID = mainController.getCurrentUser().getUserId();

        String locText = view.getLocIDField().getText().trim();
        String paxText = view.getPaxField().getText().trim();

        if (locText.isEmpty() || paxText.isEmpty()) {
            view.showMessage("Location ID and Pax cannot be empty.");
            return;
        }

        int locationID = Integer.parseInt(locText);
        int pax = Integer.parseInt(paxText);

        java.sql.Date startDate = java.sql.Date.valueOf(view.getSDateField().getText().trim());
        java.sql.Date endDate = java.sql.Date.valueOf(view.getEDateField().getText().trim());

        if (endDate.before(startDate)) {
            view.showMessage("End date must be after start date.");
            return;
        }

        Book booking = new Book(0, organizerID, locationID, pax, pax, startDate, endDate, "Booked");
        if (model.createBooking(booking)) {
            int bookingID = model.getLastInsertedBookingID();
            model.assignUserToBooking(organizerID, bookingID, "Organizer");

            view.showMessage("Booking created successfully!");
            clearBookingFields();
            loadBookingsForCurrentUser();
        } else {
            view.showMessage("Failed to create booking.");
        }

    } catch (NumberFormatException ex) {
        view.showMessage("Location ID and Pax must be numbers.");
    } catch (IllegalArgumentException ex) {
        view.showMessage("Dates must be in YYYY-MM-DD format.");
    }
}


    /** Begin editing a booking */
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

            // Organizer ID field removed → do not set it
            // view.getOrgIDField().setText(String.valueOf(booking.getOrganizerID()));
            // view.getOrgIDField().setEditable(false);

            view.getLocIDField().setText(String.valueOf(booking.getLocationID()));
            view.getLocIDField().setEditable(false);

            view.getPaxField().setText(String.valueOf(booking.getCurrentCapacity()));
            view.getSDateField().setText(booking.getStartDate().toString());
            view.getEDateField().setText(booking.getEndDate().toString());

            editingBookingID = bookingID;
            view.showCreateBooking();

        } catch (NumberFormatException ex) {
            view.showMessage("Invalid Booking ID.");
        }
    }


    /** Save edited booking via Model */
private void saveEditedBooking() {
    try {
        int currentCapacity = Integer.parseInt(view.getPaxField().getText().trim());
        java.sql.Date startDate = java.sql.Date.valueOf(view.getSDateField().getText().trim());
        java.sql.Date endDate = java.sql.Date.valueOf(view.getEDateField().getText().trim());

        Book booking = model.getBookingById(editingBookingID);
        if (booking == null) {
            view.showMessage("Booking not found.");
            editingBookingID = -1;
            return;
        }

        if (endDate.before(startDate)) {
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
            clearBookingFields();
            loadBookingsForCurrentUser(); // refresh only current user's bookings
            view.showCreateBooking();
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
                view.showMessage("User added to booking successfully!");
            } else {
                view.showMessage("Failed to add user. Organizer may already exist.");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("Booking ID must be a number.");
        }
    }


    /** Clear all input fields */
    private void clearBookingFields() {
        // orgIDField removed because organizer is automatic
        view.getLocIDField().setText("");
        view.getPaxField().setText("");
        view.getSDateField().setText("");
        view.getEDateField().setText("");
    }

    private void resetCreateFields() {
        clearBookingFields();
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
            view.showMessage("You cannot leave as you are the organizer. You must delete the booking.");
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
