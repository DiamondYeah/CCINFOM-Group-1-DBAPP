import java.sql.Timestamp;

public class UserEmail {
    private int emailId;
    private int userId;
    private String email;
    private Timestamp dateAdded;

    public UserEmail(int emailId, int userId, String email, Timestamp dateAdded) {
        this.emailId = emailId;
        this.userId = userId;
        this.email = email;
        this.dateAdded = dateAdded;
    }

    public int getEmailId() { 
        return emailId; 
    }

    public void setEmailId(int emailId) { 
        this.emailId = emailId; 
    }

    public int getUserId() { 
        return userId; 
    }

    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }
}