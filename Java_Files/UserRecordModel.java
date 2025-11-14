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
        String query = "SELECT u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, " +
                      "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points " +
                      "FROM User u " +
                      "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
                      "ORDER BY u.User_ID";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PointsTier tier = null;
                if (rs.getObject("Tier_ID") != null) {
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
                    tier
                );

                user.setEmails(getUserEmails(user.getUserId()));
                user.setPhones(getUserPhones(user.getUserId()));

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching users.\n");
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

    public User getUserById(int userId) {
        String query = "SELECT u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, " +
                      "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points " +
                      "FROM User u " +
                      "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
                      "WHERE u.User_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                PointsTier tier = null;
                if (rs.getObject("Tier_ID") != null) {
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
                    tier
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
                updateUserTier(userId, points);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.printf("Error updating user.\n");
            return false;
        }
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
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("Error deleting email.\n");
            return false;
        }
    }

    public boolean addUserPhone(int userId, String phoneNumber) {
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

    public List<Object[]> getRecommendedSpots() {
        List<Object[]> spots = new ArrayList<>();
        String query = "SELECT ts.location_id, ts.area, c.city_name, r.region_name, co.country_name, " +
                      "ts.date_shared, COUNT(DISTINCT uf.Review_ID) as review_count, " +
                      "COALESCE(AVG(uf.Rating), 0) as avg_rating " +
                      "FROM Travel_Spot ts " +
                      "JOIN City c ON ts.city_id = c.city_id " +
                      "JOIN Region r ON c.region_id = r.region_id " +
                      "JOIN Country co ON r.country_id = co.country_id " +
                      "LEFT JOIN User_Feedback uf ON ts.location_id = uf.Location_ID " +
                      "WHERE ts.is_recommended = TRUE " +
                      "GROUP BY ts.location_id, ts.area, c.city_name, r.region_name, co.country_name, ts.date_shared " +
                      "ORDER BY avg_rating DESC, review_count DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] spot = new Object[8];
                spot[0] = rs.getInt("location_id");
                spot[1] = rs.getString("area");
                spot[2] = rs.getString("city_name");
                spot[3] = rs.getString("region_name");
                spot[4] = rs.getString("country_name");
                spot[5] = rs.getDate("date_shared");
                spot[6] = rs.getInt("review_count");
                spot[7] = rs.getDouble("avg_rating");
                spots.add(spot);
            }
        } catch (SQLException e) {
            System.out.printf("Error fetching recommended spots.\n");
        }

        return spots;
    }

    public boolean addPointsToUser(int userId, int pointsToAdd) {
    String query = "UPDATE User SET Points = Points + ? WHERE User_ID = ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, pointsToAdd);
        pstmt.setInt(2, userId);
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
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
}