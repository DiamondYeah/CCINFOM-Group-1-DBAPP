public class Book {
    private int bookingID;
    private int organizerID;
    private int locationID;
    private int pax;
    private String startDate;
    private String endDate;
    private String bookDate;

    public Book(int bookingID, int organizerID, int locationID, int pax, String startDate, String endDate){
         this.bookingID = bookingID;
         this.organizerID = organizerID;
         this.pax = pax;
         this.startDate = startDate;
         this.endDate = endDate;
    }

    public int getBookingID(){
        return bookingID;
    }

    public int getOrganizerID(){
        return organizerID;
    }

    public int getLocationID(){
        return locationID;
    }

    public int getPax(){
        return pax;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getBookDate(){
        bookDate = startDate + " until " + endDate;
        return bookDate;
    }
    
}


