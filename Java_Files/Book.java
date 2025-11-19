import java.sql.Date;
import java.sql.Timestamp;

public class Book {
    private int bookingID;
    private int organizerID;
    private int locationID;
    private int currentCapacity;
    private int maxCapacity;
    private Date startDate;
    private Date endDate;
    private String status;
    private double gemPrice;
    private double tax;
    private double totalPrice;
    private Timestamp dateBook;

    // Constructor with pricing information and Date_Book
    public Book(int bookingID, int organizerID, int locationID, int currentCapacity, int maxCapacity, 
                Date startDate, Date endDate, String status, double gemPrice, double tax, double totalPrice, Timestamp dateBook) {
        this.bookingID = bookingID;
        this.organizerID = organizerID;
        this.locationID = locationID;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.gemPrice = gemPrice;
        this.tax = tax;
        this.totalPrice = totalPrice;
        this.dateBook = dateBook;
    }

    // Constructor with pricing information (backward compatibility)
    public Book(int bookingID, int organizerID, int locationID, int currentCapacity, int maxCapacity, 
                Date startDate, Date endDate, String status, double gemPrice, double tax, double totalPrice) {
        this(bookingID, organizerID, locationID, currentCapacity, maxCapacity, startDate, endDate, status, gemPrice, tax, totalPrice, null);
    }

    // Original constructor for backward compatibility
    public Book(int bookingID, int organizerID, int locationID, int currentCapacity, int maxCapacity, 
                Date startDate, Date endDate, String status) {
        this(bookingID, organizerID, locationID, currentCapacity, maxCapacity, startDate, endDate, status, 0.0, 0.0, 0.0, null);
    }

    // Getters and setters
    public int getBookingID() { return bookingID; }
    public int getOrganizerID() { return organizerID; }
    public int getLocationID() { return locationID; }
    public int getCurrentCapacity() { return currentCapacity; }
    public int getMaxCapacity() { return maxCapacity; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public double getGemPrice() { return gemPrice; }
    public double getTax() { return tax; }
    public double getTotalPrice() { return totalPrice; }
    public Timestamp getDateBook() { return dateBook; }

    public void setCurrentCapacity(int currentCapacity) { this.currentCapacity = currentCapacity; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setGemPrice(double gemPrice) { this.gemPrice = gemPrice; }
    public void setTax(double tax) { this.tax = tax; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setDateBook(Timestamp dateBook) { this.dateBook = dateBook; }
}