public class UserEmail {
    private int emailId;
    private int userId;
    private String email;

    public UserEmail(int emailId, int userId, String email) {
        this.emailId = emailId;
        this.userId = userId;
        this.email = email;
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
}