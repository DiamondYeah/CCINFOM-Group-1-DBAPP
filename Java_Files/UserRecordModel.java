import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRecordModel {
    private Connection conn;

    public UserRecordModel(Connection conn) {
        this.conn = conn;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, u.Is_Admin, u.Password, " +
                        "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points, " +
                        "COUNT(DISTINCT ts.Location_ID) as Locations_Shared, " +
                        "COUNT(DISTINCT uf.Review_ID) as Reviews_Made, " +
                        "COUNT(DISTINCT b.Booking_ID) as Bookings_Count " +
                        "FROM User u " +
                        "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
                        "LEFT JOIN Travel_Spot ts ON u.User_ID = ts.User_ID " +
                        "LEFT JOIN User_Feedback uf ON u.User_ID = uf.User_ID " +
                        "LEFT JOIN Booking b ON u.User_ID = b.Organizer_ID " +
                        "GROUP BY u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, u.Is_Admin, u.Password, " +
                        "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points " +
                        "ORDER BY u.User_ID";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                boolean isAdmin = rs.getBoolean("Is_Admin");
                PointsTier tier = null;
            
                if (!isAdmin && rs.getObject("Tier_ID") != null) {
                    tier = new PointsTier(
                        rs.getInt("Tier_ID"),
                        rs.getString("Tier_Name"),
                        rs.getInt("Min_Points"),
                        rs.getInt("Max_Points")
                        );
                }

                User user = new User(
                    rs.getInt("User_ID"),
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Nationality"),
                    rs.getInt("Points"),
                    tier, isAdmin);
            
                user.setPassword(rs.getString("Password"));
            
                user.setLocationsShared(rs.getInt("Locations_Shared"));
                user.setReviewsMade(rs.getInt("Reviews_Made"));
                user.setBookingsCount(rs.getInt("Bookings_Count"));

                user.setEmails(getUserEmails(user.getUserId()));
                user.setPhones(getUserPhones(user.getUserId()));

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching users.\n");
            e.printStackTrace();
        }

        return users;
    }

    public List<UserEmail> getUserEmails(int userId) {
        List<UserEmail> emails = new ArrayList<>();
        String query = "SELECT Email_ID, User_ID, Email FROM User_Email WHERE User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                emails.add(new UserEmail(
                    rs.getInt("Email_ID"),
                    rs.getInt("User_ID"),
                    rs.getString("Email")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching emails.\n");
        }

        return emails;
    }

    public List<UserPhone> getUserPhones(int userId) {
        List<UserPhone> phones = new ArrayList<>();
        String query = "SELECT Phone_ID, User_ID, Phone_Number FROM User_Phone WHERE User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                phones.add(new UserPhone(
                    rs.getInt("Phone_ID"),
                    rs.getInt("User_ID"),
                    rs.getString("Phone_Number")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching phones.\n");
        }

        return phones;
    }
    
    public List<UserEmail> getAllEmails() {
        List<UserEmail> emails = new ArrayList<>();
        String query = "SELECT Email_ID, User_ID, Email FROM User_Email ORDER BY User_ID, Email_ID";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                emails.add(new UserEmail(
                    rs.getInt("Email_ID"),
                    rs.getInt("User_ID"),
                    rs.getString("Email")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching all emails.\n");
        }

        return emails;
    }

    public List<UserPhone> getAllPhones() {
        List<UserPhone> phones = new ArrayList<>();
        String query = "SELECT Phone_ID, User_ID, Phone_Number FROM User_Phone ORDER BY User_ID, Phone_ID";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                phones.add(new UserPhone(
                    rs.getInt("Phone_ID"),
                    rs.getInt("User_ID"),
                    rs.getString("Phone_Number")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching all phones.\n");
        }

        return phones;
    }

    public User getUserById(int userId) {
        String query = "SELECT u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, u.Is_Admin, " +
                      "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points " +
                      "FROM User u " +
                      "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
                      "WHERE u.User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = rs.getBoolean("Is_Admin");
                PointsTier tier = null;
                
                if (!isAdmin && rs.getObject("Tier_ID") != null) {
                    tier = new PointsTier(
                        rs.getInt("Tier_ID"),
                        rs.getString("Tier_Name"),
                        rs.getInt("Min_Points"),
                        rs.getInt("Max_Points")
                    );
                }

                User user = new User(
                    rs.getInt("User_ID"),
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Nationality"),
                    rs.getInt("Points"),
                    tier,
                    isAdmin
                );

                user.setEmails(getUserEmails(user.getUserId()));
                user.setPhones(getUserPhones(user.getUserId()));

                return user;
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching user.\n");
        }

        return null;
    }

    public boolean updateUser(int userId, String firstName, String lastName, String nationality, int points) {
        String query = "UPDATE User SET First_Name = ?, Last_Name = ?, Nationality = ?, Points = ? WHERE User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, nationality);
            pstmt.setInt(4, points);
            pstmt.setInt(5, userId);

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                if (!isUserAdmin(userId)) {
                    updateUserTier(userId, points);
                }
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.printf("Error updating user.\n");
            return false;
        }
    }

    public boolean updateUserPassword(int userId, String newPassword) {
        String query = "UPDATE User SET Password = ? WHERE User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("Error updating password.\n");
            return false;
        }
    }

    private boolean isUserAdmin(int userId) {
        String query = "SELECT Is_Admin FROM User WHERE User_ID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("Is_Admin");
            }
        } catch (SQLException e) {
            System.out.printf("Error checking admin status.\n");
        }
        
        return false;
    }

    private void updateUserTier(int userId, int points) {
        String query = "UPDATE User SET Tier_ID = " +
                      "(SELECT Tier_ID FROM Points_Tier WHERE ? BETWEEN Min_Points AND Max_Points) " +
                      "WHERE User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, points);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("Error updating tier.\n");
        }
    }

    public boolean addUserEmail(int userId, String email) {
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format: " + email);
            return false;
        }

        String query = "INSERT INTO User_Email (User_ID, Email) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("Error adding email.\n");
            return false;
        }
    }

    public boolean deleteUserEmail(int emailId) {
        String query = "DELETE FROM User_Email WHERE Email_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, emailId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Email deleted successfully: " + emailId);
                return true;
            } else {
                System.out.println("No email found with ID: " + emailId);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error deleting email: %s\n", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUserPhone(int userId, String phoneNumber) {
        if (!isValidPhone(phoneNumber)) {
            System.out.println("Invalid phone format: " + phoneNumber);
            return false;
        }

        String query = "INSERT INTO User_Phone (User_ID, Phone_Number) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, phoneNumber);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("Error adding phone.\n");
            return false;
        }
    }

    public boolean deleteUserPhone(int phoneId) {
        String query = "DELETE FROM User_Phone WHERE Phone_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, phoneId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("Error deleting phone.\n");
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        email = email.trim();
        
        if (!email.contains("@") || !email.contains(".com")) {
            return false;
        }
        
        int atIndex = email.indexOf("@");
        int comIndex = email.indexOf(".com");
        
        return atIndex < comIndex && atIndex > 0;
    }

    private boolean isValidPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        
        return digitsOnly.length() >= 7 && digitsOnly.length() <= 15;
    }

    public List<PointsTier> getAllTiers() {
        List<PointsTier> tiers = new ArrayList<>();
        String query = "SELECT Tier_ID, Tier_Name, Min_Points, Max_Points FROM Points_Tier ORDER BY Min_Points";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tiers.add(new PointsTier(
                    rs.getInt("Tier_ID"),
                    rs.getString("Tier_Name"),
                    rs.getInt("Min_Points"),
                    rs.getInt("Max_Points")
                ));
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching tiers.\n");
        }

        return tiers;
    }

    public List<Object[]> getRecommendationsByTierAndDate(int month, int year) {
        List<Object[]> recommendations = new ArrayList<>();
        String query = "SELECT ts.location_id, ts.area, c.city_name, r.region_name, co.country_name, " +
                        "pt.Tier_Name as tier_name, " +
                        "COUNT(DISTINCT uf.Review_ID) as recommendation_count " +
                        "FROM User_Feedback uf " +
                        "JOIN Travel_Spot ts ON uf.Location_ID = ts.location_id " +
                        "JOIN City c ON ts.city_id = c.city_id " +
                        "JOIN Region r ON c.region_id = r.region_id " +
                        "JOIN Country co ON r.country_id = co.country_id " +
                        "JOIN User u ON uf.User_ID = u.User_ID " +
                        "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
                        "WHERE uf.is_recommendation = TRUE " +
                        "AND MONTH(uf.Review_Date) = ? " +
                        "AND YEAR(uf.Review_Date) = ? " +
                        "AND u.Is_Admin = FALSE " +
                        "GROUP BY ts.location_id, ts.area, c.city_name, r.region_name, co.country_name, pt.Tier_Name " +
                        "ORDER BY recommendation_count DESC, ts.location_id, tier_name";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] rec = new Object[7];
                rec[0] = rs.getInt("location_id");
                rec[1] = rs.getString("area");
                rec[2] = rs.getString("city_name");
                rec[3] = rs.getString("region_name");
                rec[4] = rs.getString("country_name");
            
                String tier = rs.getString("tier_name");
                if (tier != null) {
                    rec[5] = tier;
                } else {
                    rec[5] = "No Tier";
                }
                
                rec[6] = rs.getInt("recommendation_count");
                recommendations.add(rec);
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching recommendations by tier and date.\n");
            e.printStackTrace();
        }

        return recommendations;
    }

    public boolean addPointsToUser(int userId, int pointsToAdd) {
        String query = "UPDATE User SET Points = Points + ? WHERE User_ID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, pointsToAdd);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0 && !isUserAdmin(userId)) {
                String getPointsQuery = "SELECT Points FROM User WHERE User_ID = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(getPointsQuery)) {
                    ps2.setInt(1, userId);
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) {
                        updateUserTier(userId, rs.getInt("Points"));
                    }
                }
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.printf("Error adding points.\n");
            return false;
        }
    }
    
    public int getLocationsSharedByUser(int userId) {
        String query = "SELECT COUNT(Location_ID) as count FROM Travel_Spot WHERE User_ID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching locations shared count.\n");
        }
        
        return 0;
    }
    
    public boolean insertUser(String firstName, String lastName, String nationality, int points, String password, boolean isAdmin) {
        String query = "INSERT INTO User (First_Name, Last_Name, Nationality, Points, Password, Is_Admin) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, nationality);
            pstmt.setInt(4, points);
            pstmt.setString(5, password);
            pstmt.setBoolean(6, isAdmin);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0 && !isAdmin && points > 0) {
                String getIdQuery = "SELECT LAST_INSERT_ID() as id";
                try (PreparedStatement ps2 = conn.prepareStatement(getIdQuery)) {
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) {
                        updateUserTier(rs.getInt("id"), points);
                    }
                }
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.printf("Error inserting user.\n");
            return false;
        }
    }
    
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM User WHERE User_ID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User deleted successfully: " + userId);
                return true;
            } else {
                System.out.println("No user found with ID: " + userId);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error deleting user: %s\n", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertTier(String tierName, int minPoints, int maxPoints) {
        String query = "INSERT INTO Points_Tier (Tier_Name, Min_Points, Max_Points) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tierName);
            pstmt.setInt(2, minPoints);
            pstmt.setInt(3, maxPoints);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Tier inserted successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.printf("Error inserting tier: %s\n", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteTier(int tierId) {
        String updateQuery = "UPDATE User SET Tier_ID = NULL WHERE Tier_ID = ?";
        String deleteQuery = "DELETE FROM Points_Tier WHERE Tier_ID = ?";
        
        try {
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, tierId);
                pstmt.executeUpdate();
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, tierId);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Tier deleted successfully: " + tierId);
                    return true;
                } else {
                    System.out.println("No tier found with ID: " + tierId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.printf("Error deleting tier: %s\n", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}