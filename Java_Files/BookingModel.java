import java.util.ArrayList;
import java.util.List;

public class BookingModel {
    private List<Book> bookings; // list to store Book objects

    public BookingModel() {
        bookings = new ArrayList<>(); // initialize the booking list
    }

    // Add a new booking
    public void addBooking(Book booking) {
        bookings.add(booking);
    }

    // Get all bookings
    public List<Book> getBookings() {
        return bookings;
    }

    //Find a booking by booking ID
    public Book getBookingById(int bookingID) {
        for (Book b : bookings) {
            if (b.getBookingID() == bookingID) {
                return b;
            }
        }
        return null; // return null if not found
    }

    //Remove a booking by ID
    public boolean removeBooking(int bookingID) {
        Book b = getBookingById(bookingID);
        if (b != null) {
            bookings.remove(b);
            return true;
        }
        return false;
    }
}
