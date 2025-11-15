import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TravelRecordModel {
    private final Connection connection;

    public TravelRecordModel(Connection connection){ this.connection = connection; }

    // Create a Travel Spot
    public boolean addTravelSpot(TravelRecord spot) throws SQLException {
        String sql = "INSERT INTO Travel_spot (user_id, area, availability, date_shared, city_id, base_price, max_capacity)"
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, spot.getUserId());
        ps.setString(2, spot.getArea());
        ps.setString(3, spot.getAvailability());
        ps.setDate(4, spot.getDateShared());
        ps.setInt(5, spot.getCityId());
        ps.setDouble(6, spot.getBasePrice());
        ps.setInt(7, spot.getMaxCap());
        return ps.executeUpdate() > 0;
    }

    // Read a Travel Spot
    public TravelRecord getTravelSpot(int location_id) throws SQLException {
        String sql = "SELECT * FROM travel_spot WHERE location_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, location_id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new TravelRecord (
                rs.getInt("location_id"),
                rs.getInt("user_id"),
                rs.getString("area"),
                rs.getString("availability"),
                rs.getDate("date_shared"),
                rs.getInt("city_id"),
                rs.getDouble("base_price"),
                rs.getInt("max_capacity")
            );
        }
        return null;
    }

    // Update a Travel Spot
    public boolean updTravelSpot(TravelRecord spot) throws SQLException {
        String sql = "UPDATE travel_spot SET user_id=?, area=?, availability=?, date_shared=?, city_id=?, base_price=?, max_capacity=? WHERE location_id=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, spot.getUserId());
        ps.setString(2, spot.getArea());
        ps.setString(3, spot.getAvailability());
        ps.setDate(4, spot.getDateShared());
        ps.setInt(5, spot.getCityId());
        ps.setDouble(6, spot.getBasePrice());
        ps.setInt(7, spot.getMaxCap());
        ps.setInt(8, spot.getLocationId());

        return ps.executeUpdate() > 0;
    }

    // Delete a Travel Spot
    public boolean deleteTravelSpot(int location_id) throws SQLException {
        String sql = "DELETE FROM travel_spot WHERE location_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, location_id);

        return ps.executeUpdate() > 0;
    }

    // Display or List all spots
    public List<TravelRecord> getAllTravelSpots() throws SQLException {
        List<TravelRecord> tsList = new ArrayList<>();

        String sql = "SELECT * FROM travel_spot";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            tsList.add(new TravelRecord (
                rs.getInt("location_id"),
                rs.getInt("user_id"),
                rs.getString("area"),
                rs.getString("availability"),
                rs.getDate("date_shared"),
                rs.getInt("city_id"),
                rs.getDouble("base_price"),
                rs.getInt("max_capacity")
            ));
        }
        return tsList;
    }

}
