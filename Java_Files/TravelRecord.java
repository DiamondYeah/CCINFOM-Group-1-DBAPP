public class TravelRecord {
    private int location_id;
    private int user_id;
    private String area;
    private String availability;
    private java.sql.Date dateShared;
    private int city_id;
    private double basePrice;
    private int maxCap;

    // Constructors
    public TravelRecord(int location_id, int user_id, String area, String availability, 
                        java.sql.Date dateShared, int city_id, 
                        double basePrice, int MaxCap) {
        this.location_id = location_id;
        this.user_id = user_id;
        this.area = area;
        this.availability = availability;
        this.dateShared = dateShared;
        this.city_id = city_id;
        this.basePrice = basePrice;
        this.maxCap = maxCap;
        }
    
    // Adding a travel spot, as location_id will increment by itself
    public TravelRecord(int user_id, String area, String availability, 
                        java.sql.Date dateShared, int city_id, 
                        double basePrice, int MaxCap) {
        this.user_id = user_id;
        this.area = area;
        this.availability = availability;
        this.dateShared = dateShared;
        this.city_id = city_id;
        this.basePrice = basePrice;
        this.maxCap = maxCap;
        }

    // Getter n Setter
    public int getLocationId() { return location_id;}
    public void setLocationId(int location_id) { this.location_id = location_id; }

    public int getUserId() { return user_id;}
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getArea() { return area;}
    public void setArea(String area) { this.area = area; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public java.sql.Date getDateShared() { return dateShared; }
    public void setDateShared(java.sql.Date dateShared) { this.dateShared = dateShared; }

    public int getCityId() {return city_id;}
    public void setCityId(int city_id) {this.city_id = city_id;}

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public int getMaxCap() { return maxCap; }
    public void setMaxCap(int maxCap) { this.maxCap = maxCap; }
}
