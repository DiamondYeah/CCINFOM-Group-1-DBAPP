public class UserPhone {
    private int phoneId;
    private int userId;
    private String phoneNumber;

    public UserPhone(int phoneId, int userId, String phoneNumber) {
        this.phoneId = phoneId;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }

    public int getPhoneId() { 
        return phoneId; 
    }

    public void setPhoneId(int phoneId) { 
        this.phoneId = phoneId; 
    }

    public int getUserId() { 
        return userId; 
    }

    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
}