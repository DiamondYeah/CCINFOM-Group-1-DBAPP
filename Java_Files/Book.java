import java.sql.Date;

public class Book {
    private int bookingID;
    private int organizerID;
    private int locationID;
    private int currentCapacity;
    private int maxCapacity;
    private Date startDate;
    private Date endDate;
    private String status;

    public Book(int bookingID, int organizerID, int locationID, int currentCapacity, int maxCapacity, Date startDate, Date endDate, String status) {
        this.bookingID = bookingID;
        this.organizerID = organizerID;
        this.locationID = locationID;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
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

    public void setCurrentCapacity(int currentCapacity) { this.currentCapacity = currentCapacity; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    //
}
