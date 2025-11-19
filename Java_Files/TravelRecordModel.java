import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// helper for list
class IdName {
    private int id;
    private String name;

    public IdName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    @Override public String toString() {return name;}
}

public class TravelRecordModel {
    private final Connection connection;

    public TravelRecordModel(Connection connection){ this.connection = connection; }

    // dropdowns
    // All Countries
    public List<IdName> getCountries() throws SQLException {
        List<IdName> out = new ArrayList<>();
        String sql = "SELECT country_id, country_name FROM Country ORDER BY country_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new IdName(rs.getInt(1), rs.getString(2)));
        }
         return out;
    }

    // All Regions
    public List<IdName> getRegions(int countryId) throws SQLException {
        List<IdName> out = new ArrayList<>();
        String sql = "SELECT region_id, region_name FROM Region WHERE country_id = ? ORDER BY region_name";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, countryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new IdName(rs.getInt(1), rs.getString(2)));
            }
        }
        return out;
    }

    // All Cities
    public List<IdName> getCities(int regionId) throws SQLException {
        List<IdName> out = new ArrayList<>();
        String sql = "SELECT city_id, city_name FROM City WHERE region_id = ? ORDER BY city_name";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, regionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new IdName(rs.getInt(1), rs.getString(2)));
            }
        }
        return out;
    }

    // All Categories
    public List<IdName> getCategories() throws SQLException {
        List<IdName> out = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM Category ORDER BY category_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new IdName(rs.getInt(1), rs.getString(2)));
        }
         return out;
    }

    // Return generated location_id
    public int addTravelSpot(TravelRecord spot, List<Integer> categoryId) throws SQLException {
        String newSpot = "INSERT INTO Travel_Spot (user_id, spotname, date_shared, city_id, base_price, max_capacity, availability) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean auto = connection.getAutoCommit();
        try (PreparedStatement ps = connection.prepareStatement(newSpot, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            
            ps.setInt(1, spot.getUserId());
            ps.setString(2, spot.getSpotname());
            ps.setDate(3, spot.getDateShared());
            ps.setInt(4, spot.getCityId());
            ps.setDouble(5, spot.getBasePrice());
            ps.setInt(6, spot.getMaxCap());
            ps.setString(7, spot.getAvailability());

            int result = ps.executeUpdate();
            if (result == 0)
                throw new SQLException("insert travel_spot failed.");
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next())
                    throw new SQLException("No generated id.");
                int location_id = rs.getInt(1);

                if (categoryId != null && !categoryId.isEmpty()) {
                    String link = "INSERT INTO TS_Category (location_id, category_id) VALUES (?, ?)";
                    try (PreparedStatement psLink = connection.prepareStatement(link)) {
                        for (int catId : categoryId) {
                            psLink.setInt(1, location_id);
                            psLink.setInt(2, catId);
                            psLink.addBatch();
                        }
                        psLink.executeBatch();
                    }
                }

                connection.commit();
                return location_id; // or change location_id to locationId ???
            }
        }
        catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
        finally {
            connection.setAutoCommit(auto);
        }
    }

    public TravelRecord getTravelSpot(int locationId) throws SQLException {
        String sql = "SELECT * FROM Travel_Spot WHERE location_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TravelRecord(
                        rs.getInt("location_id"),
                        rs.getInt("user_id"),
                        rs.getString("spotname"),
                        rs.getDate("date_shared"),
                        rs.getInt("city_id"),
                        rs.getDouble("base_price"),
                        rs.getInt("max_capacity"),
                        rs.getString("availability")
                    );
                }
            }
        }
        return null;
    }

    // Update a Travel Spot
    public boolean updTravelSpot(TravelRecord spot) throws SQLException {
        String sql = "UPDATE travel_spot SET user_id=?, spotname=?, date_shared=?, city_id=?, base_price=?, max_capacity=?, availability=? WHERE location_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, spot.getUserId());
        ps.setString(2, spot.getSpotname());
        ps.setDate(3, spot.getDateShared());
        ps.setInt(4, spot.getCityId());
        ps.setDouble(5, spot.getBasePrice());
        ps.setInt(6, spot.getMaxCap());
        ps.setString(7, spot.getAvailability());
        ps.setInt(8, spot.getLocationId());

        return ps.executeUpdate() > 0;
        }
    }

    // Update Category / Categories
    public void updTravelSpotCategories(int location_id, List<Integer> categoryId) throws SQLException {
        boolean auto = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            // delete existing category / categories
            try (PreparedStatement psDel = connection.prepareStatement("DELETE FROM TS_Category WHERE location_id=?")) {
                psDel.setInt(1, location_id);
                psDel.executeUpdate();
            }
            // inserting new
            if (categoryId != null && !categoryId.isEmpty()) {
                try (PreparedStatement psIns = connection.prepareStatement("INSERT INTO TS_Category (location_id, category_id) VALUES (?, ?)")) {
                    for (int catId : categoryId) {
                        psIns.setInt(1, location_id);
                        psIns.setInt(2, catId);
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
            }
            connection.commit();
        }
        catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
        finally {
            connection.setAutoCommit(auto);
        }
    }

    // Delete a Travel Spot
    public boolean deleteTravelSpot(int location_id) throws SQLException {
        String sql = "DELETE FROM travel_spot WHERE location_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setInt(1, location_id);

        return ps.executeUpdate() > 0;
        }
    }

    //list details
    public List<Object[]> getAllTravelSpotsDetailed() throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = 
            "SELECT ts.location_id, ts.user_id, ts.spotname, ts.date_shared, c.city_name, r.region_name, co.country_name, ts.base_price, ts.max_capacity, ts.availability, " +
            "GROUP_CONCAT(DISTINCT cat.category_name ORDER BY cat.category_name SEPARATOR ', ') AS categories FROM Travel_Spot AS ts " +
            "JOIN City AS c ON ts.city_id = c.city_id " +
            "JOIN Region AS r ON c.region_id = r.region_id " +
            "JOIN Country AS co ON r.country_id = co.country_id " +
            "LEFT JOIN TS_Category AS tsc ON ts.location_id = tsc.location_id " +
            "LEFT JOIN Category AS cat ON tsc.category_id = cat.category_id " +
            "GROUP BY ts.location_id ORDER BY ts.location_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] r = new Object[11];
                    r[0] = rs.getInt("location_id");
                    r[1] = rs.getInt("user_id");
                    r[2] = rs.getString("spotname");
                    r[3] = rs.getDate("date_shared");
                    r[4] = rs.getString("city_name");
                    r[5] = rs.getString("region_name");
                    r[6] = rs.getString("country_name");
                    r[7] = rs.getDouble("base_price");
                    r[8] = rs.getInt("max_capacity");
                    r[9] = rs.getString("availability");
                    r[10] = rs.getString("categories");
                    rows.add(r);
                }
            }
            return rows;
    }


    // Display or List all spots
    public List<TravelRecord> getAllTravelSpots() throws SQLException {
        List<TravelRecord> tsList = new ArrayList<>();

        String sql = "SELECT * FROM travel_spot";
        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tsList.add(new TravelRecord (
                    rs.getInt("location_id"),
                    rs.getInt("user_id"),
                    rs.getString("spotname"),
                    rs.getDate("date_shared"),
                    rs.getInt("city_id"),
                    rs.getDouble("base_price"),
                    rs.getInt("max_capacity"),
                    rs.getString("availability")
            ));
            }
        }
        return tsList;
    }

    public List<Integer> getCategoryOfSpot(int locationId) throws SQLException {
        List<Integer> out = new ArrayList<>();
        String sql = "SELECT category_id FROM TS_Category WHERE location_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(rs.getInt(1));
                }
            }
        }
        return out;
    }

    // helper for getting the number booked in a spot
    public int getTotalBooked(int locationId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(Current_Capacity), 0) AS total FROM Booking WHERE Location_ID=? AND Status IN ('Booked', 'Confirmed')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("total");
            }
        }
        return 0;
    }

    // helper to get the max capacity of a travel spot
    public int getMaxCapacity(int locationId) throws SQLException {
        String sql = "SELECT max_capacity FROM Travel_Spot WHERE location_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("max_capacity");
            }
        }
        return 0;
    }

    // helper to compare the number of people booked and maxCapacity to automatically update status
    public void updateAvailability(int locationId) throws SQLException {
        int totalBooked = getTotalBooked(locationId);
        int maxCap = getMaxCapacity(locationId);

        String newStatus = (totalBooked >= maxCap) ? "Unavailable" : "Available";

        String sql = "UPDATE Travel_Spot SET availability = ? WHERE location_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, locationId);
            ps.executeUpdate();
        }
    }

    // NEW REPORTS
    // Hidden Gem Popularity
    public List<Object[]> getPopularityReport(int month, int year) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql =
            "SELECT ts.location_id, ts.spotname, COUNT(DISTINCT b.Booking_ID) AS total_bookings, COALESCE(AVG(uf.Rating), 0) AS avg_rating, " +
            "COALESCE(SUM(CASE WHEN ur.ReactionType_ID = 1 THEN 1 ELSE 0 END), 0) AS total_likes, COALESCE(SUM(CASE WHEN ur.ReactionType_ID = 2 THEN 1 ELSE 0 END), 0) AS total_dislikes, " +
            "(COUNT(DISTINCT b.Booking_ID) * 0.6 + COALESCE(AVG(uf.Rating), 0) * 0.4) AS popularity_score FROM Travel_Spot AS ts " +
            "LEFT JOIN Booking b ON ts.location_id = b.Location_ID AND MONTH(b.Start_date) = ? AND YEAR(b.Start_date) = ? " +
            "LEFT JOIN User_Feedback uf ON ts.location_id = uf.Location_ID AND MONTH(uf.Review_Date) = ? AND YEAR(uf.Review_Date) = ? " +
            "LEFT JOIN User_Reaction ur ON uf.Review_ID = ur.Review_ID " +
            "GROUP BY ts.location_id, ts.spotname " +
            "ORDER BY popularity_score DESC, total_bookings DESC, avg_rating DESC";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setInt(4, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("location_id");
                    row[1] = rs.getString("spotname");
                    row[2] = rs.getInt("total_bookings");
                    row[3] = String.format("%.2f", rs.getDouble("avg_rating"));
                    row[4] = rs.getInt("total_likes");
                    row[5] = rs.getInt("total_dislikes");
                    row[6] = String.format("%.2f", rs.getDouble("popularity_score"));
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    // View user's own posted spots
    public List<Object[]> getUserPostedSpots(int userId) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql =
            "SELECT ts.location_id, ts.user_id, ts.spotname, ts.date_shared, c.city_name, r.region_name, co.country_name, ts.base_price, ts.max_capacity, ts.availability, " +
            "GROUP_CONCAT(DISTINCT cat.category_name ORDER BY cat.category_name SEPARATOR ', ') AS categories FROM Travel_Spot AS ts " +
            "JOIN City AS c ON ts.city_id = c.city_id JOIN Region AS r ON c.region_id = r.region_id JOIN Country AS co ON r.country_id = co.country_id " +
            "LEFT JOIN TS_Category AS tsc ON ts.location_id = tsc.location_id LEFT JOIN Category AS cat ON tsc.category_id = cat.category_id " +
            "WHERE ts.user_id = ? GROUP BY ts.location_id ORDER BY ts.date_shared DESC";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Object[] r = new Object[11];
                        r[0] = rs.getInt("location_id");
                        r[1] = rs.getInt("user_id");
                        r[2] = rs.getString("spotname");
                        r[3] = rs.getDate("date_shared");
                        r[4] = rs.getString("city_name");
                        r[5] = rs.getString("region_name");
                        r[6] = rs.getString("country_name");
                        r[7] = rs.getDouble("base_price");
                        r[8] = rs.getInt("max_capacity");
                        r[9] = rs.getString("availability");
                        r[10] = rs.getString("categories");
                        rows.add(r);
                    }
                }
            }
            return rows;
    }

    // Get poster's info (name + points)
    public Object[] getPosterInfo(int userId) throws SQLException {
        String sql = "SELECT User_ID, First_Name, Last_Name, Points FROM User WHERE User_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] info = new Object[4];
                    info[0] = rs.getInt("User_ID");
                    info[1] = rs.getString("First_Name");
                    info[2] = rs.getString("Last_Name");
                    info[3] = rs.getInt("Points");
                    return info;
                }
            }
        }
        return null;
    }

    // Get ratings for a spot
    public List<Object[]> getSpotRatings(int locationId) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = 
            "SELECT " +
            "uf.Review_ID, " +
            "CONCAT(u.First_Name, ' ', u.Last_Name) AS reviewer_name, " +
            "uf.Rating, " +
            "uf.is_recommendation, " +
            "uf.Review_Date, " +
            "COALESCE(SUM(CASE WHEN ur.ReactionType_ID = 1 THEN 1 ELSE 0 END), 0) AS likes, " +
            "COALESCE(SUM(CASE WHEN ur.ReactionType_ID = 2 THEN 1 ELSE 0 END), 0) AS dislikes " +
            "FROM User_Feedback uf " +
            "JOIN User u ON uf.User_ID = u.User_ID " +
            "LEFT JOIN User_Reaction ur ON uf.Review_ID = ur.Review_ID " +
            "WHERE uf.Location_ID = ? " +
            "GROUP BY uf.Review_ID, reviewer_name, uf.Rating, uf.is_recommendation, uf.Review_Date " +
            "ORDER BY uf.Review_Date DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("Review_ID");
                    row[1] = rs.getString("reviewer_name");
                    row[2] = rs.getDouble("Rating");
                    row[3] = rs.getBoolean("is_recommendation") ? "Yes" : "No";
                    row[4] = rs.getTimestamp("Review_Date");
                    row[5] = rs.getInt("likes");
                    row[6] = rs.getInt("dislikes");
                    rows.add(row);
                }
            }
        }
        return rows;
    }
}
