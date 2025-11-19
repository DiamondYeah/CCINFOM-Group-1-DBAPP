import java.util.List;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String nationality;
    private int points;
    private PointsTier pointsTier;
    private String password;
    private boolean isAdmin;
    private List<UserEmail> emails;
    private List<UserPhone> phones;
    private int locationsShared;
    private int reviewsMade;
    private int bookingsCount;

    public User(int userId, String firstName, String lastName, String nationality, int points, PointsTier pointsTier, boolean isAdmin) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.points = points;
        this.pointsTier = pointsTier;
        this.isAdmin = isAdmin;
    }

    public int getUserId() { 
        return userId; 
    }

    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getFirstName() { 
        return firstName; 
    }

    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }

    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public String getNationality() { 
        return nationality; 
    }

    public void setNationality(String nationality) { 
        this.nationality = nationality; 
    }

    public int getPoints() { 
        return points; 
    }

    public void setPoints(int points) { 
        this.points = points; 
    }

    public PointsTier getPointsTier() { 
        return pointsTier; 
    }

    public void setPointsTier(PointsTier pointsTier) { 
        this.pointsTier = pointsTier; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<UserEmail> getEmails() { 
        return emails; 
    }

    public void setEmails(List<UserEmail> emails) { 
        this.emails = emails; 
    }

    public List<UserPhone> getPhones() { 
        return phones; 
    }

    public void setPhones(List<UserPhone> phones) { 
        this.phones = phones; 
    }

    public int getLocationsShared() { 
        return locationsShared; 
    }

    public void setLocationsShared(int locationsShared) { 
        this.locationsShared = locationsShared; 
    }

    public int getReviewsMade() { 
        return reviewsMade; 
    }

    public void setReviewsMade(int reviewsMade) { 
        this.reviewsMade = reviewsMade; 
    }

    public int getBookingsCount() { 
        return bookingsCount; 
    }

    public void setBookingsCount(int bookingsCount) { 
        this.bookingsCount = bookingsCount; 
    }
}