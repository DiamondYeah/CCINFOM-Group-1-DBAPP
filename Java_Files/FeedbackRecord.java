import java.sql.*;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class FeedbackRecord {

    public StringBuilder query;


    public Connection conn;
    public PreparedStatement stmt; // Gets the statements
    public ResultSet set; // Prepares for multiple results


    // Private attributes containing information of different attributes from the dataset
    private ArrayList<String> reactionOptions;

    // Public Constructor
    public FeedbackRecord(Connection conn){

        this.conn = conn;

        // Initialize StringBuilder
        query = new StringBuilder();

        reactionOptions = new ArrayList<>();
        updateReactionOptions();


    }


    // Getter Methods

    public ArrayList<String> getReactionOptions(){

        return reactionOptions;

    }



    // Method adds details of user feedback into the database
    public void addUserFeedback(int userID, int locationID, float rating){

        // String builder query to create the INSERT statement
        query.setLength(0);
        query.append("INSERT INTO USER_FEEDBACK(user_id, location_id, rating, reaction_count, comment_count, review_date) ");
        query.append("VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, userID);
            stmt.setInt(2, locationID);
            stmt.setFloat(3, rating);
            stmt.setInt(4, 0);
            stmt.setInt(5, 0);

            // Insert the data to the dataset
            stmt.executeUpdate();

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the feedback model
    public void loadUserFeedback(DefaultTableModel model){

        model.setRowCount(0);

        try{

            query.setLength(0);
            query.append("SELECT * FROM User_Feedback");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reviewID = set.getInt("Review_ID");
                int userID = set.getInt("User_ID");
                int locationID = set.getInt("Location_ID");
                double rating = set.getDouble("Rating");
                int reactionCount = getReviewCount(reviewID);
                int commentCount = set.getInt("Comment_Count");
                Timestamp reviewDate = set.getTimestamp("Review_Date");

                model.addRow(new Object[]{
                    reviewID, userID, locationID, rating, reactionCount, commentCount, reviewDate
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method adds details of user reaction into the database
    public void addUserReaction(int userID, int reviewID, String reactionType){

        int reactionID = 0; // Stores the reaction ID that will be passed to the database

        // Obtain the Type ID based on the given reaction name
        query.setLength(0);
        query.append("SELECT ReactionType_ID FROM Reaction WHERE Reaction_Name = ? ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            stmt.setString(1, reactionType); // Pass reactionType to obtain ID

            set = stmt.executeQuery(); // Execute Query

            // Checks if ID exists
            if(set.next())
                reactionID = set.getInt("ReactionType_ID");

        } catch(SQLException e){

            e.printStackTrace();

        }


        // String builder query to create the INSERT statement
        query.setLength(0);
        query.append("INSERT INTO USER_REACTION(user_id, review_id, reactiontype_id, reaction_date) ");
        query.append("VALUES (?, ?, ?, CURRENT_TIMESTAMP)");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, userID);
            stmt.setInt(2, reviewID);
            stmt.setInt(3, reactionID);

            // Insert the data to the dataset
            stmt.executeUpdate();

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the report model
    public void loadUserReaction(DefaultTableModel model){

        model.setRowCount(0);

        try{

            query.setLength(0);
            query.append("SELECT * FROM User_Reaction");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reactionID = set.getInt("Reaction_ID");
                int reviewID = set.getInt("Review_ID");
                int userID = set.getInt("User_ID");
                int reactionTypeID = set.getInt("ReactionType_ID");
                Timestamp reactionDate = set.getTimestamp("Reaction_Date");

                model.addRow(new Object[]{
                    reactionID, reviewID, userID, reactionTypeID, reactionDate
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method adds details of reaction into the database
    public void addReaction(String reactionType, JComboBox<String> reactionTypeBox){

        // String builder query to create the INSERT statement
        query.setLength(0);
        query.append("INSERT INTO REACTION (reaction_name) ");
        query.append("VALUES (?)");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setString(1, reactionType);

            // Insert the data to the dataset
            stmt.executeUpdate();

        } catch(SQLException e){

            e.printStackTrace();

        }

        updateReactionOptions();

        reactionTypeBox.removeAllItems();
        
        for(int i = 0; i < reactionOptions.size(); i++)
            reactionTypeBox.addItem(reactionOptions.get(i));

    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the review/user_reaction model
    public void loadReaction(DefaultTableModel model){

        model.setRowCount(0);

        try{

            query.setLength(0);
            query.append("SELECT * FROM Reaction");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reactionTypeID = set.getInt("ReactionType_ID");
                String reactionName = set.getString("Reaction_Name");

                model.addRow(new Object[]{
                    reactionTypeID, reactionName
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }





    // Method obtains the infromation from stmt and gets the needed information and adds them to the feedback model
    public void loadReport(DefaultTableModel model){

        model.setRowCount(0);

        try{

            query.setLength(0);

            // Query that will generate the report
            query.append("SELECT u.user_id, CONCAT(u.first_name, ' ' , u.last_name) AS full_name, pt.tier_name, ts.area, uf.rating, ");
            query.append("COUNT(CASE WHEN r.Reaction_Name = 'LIKE' THEN 1 END) AS likes, ");
            query.append("COUNT(CASE WHEN r.Reaction_Name = 'DISLIKE' THEN 1 END) AS dislikes, ");
            query.append("DATE_FORMAT(uf.Review_Date, '%M %Y') AS Review_Month_Year ");
            query.append("FROM User_feedback uf JOIN User u ON uf.User_ID = u.User_ID ");
            query.append("JOIN Points_tier pt ON u.Tier_ID = pt.Tier_ID ");
            query.append("JOIN Travel_spot ts ON uf.Location_ID = ts.Location_ID ");
            query.append("LEFT JOIN User_reaction ur ON uf.Review_ID = ur.Review_ID ");
            query.append("LEFT JOIN Reaction r ON ur.ReactionType_ID = r.ReactionType_ID ");
            query.append("GROUP BY user_id, full_name, tier_name, ts.area, uf.rating, Review_Month_Year ");
            query.append("ORDER BY u.points DESC ");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int userID = set.getInt("User_ID");
                String fullName = set.getString("Full_Name");
                String tierName = set.getString("Tier_Name");
                String area = set.getString("Area");
                double rating = set.getDouble("Rating");
                int likes = set.getInt("Likes");
                int dislikes = set.getInt("Dislikes");
                String reviewMonthYear = set.getString("Review_Month_Year");
                
                model.addRow(new Object[]{
                    userID, fullName, tierName, area, rating, likes, dislikes, reviewMonthYear
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Methods to obtain fields and update the database

    // Methods obtains the different reactions types from the reactions table
    private void updateReactionOptions(){

        reactionOptions.clear(); // Resets arrayList in case of updates in Reaction Options
        
        try{

            query.setLength(0);
            query.append("SELECT * FROM Reaction");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String reactionType = set.getString("Reaction_Name");

                System.out.println(reactionType);

                reactionOptions.add(reactionType); // Add reaction type to array list



            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }



    private int getReviewCount(int reviewID){

        int reviewCount = 0;

        PreparedStatement reviewSTMT; // Seperate PreparedStatement to avoid interfering with the global PreparedStatement
        ResultSet reviewSet; // Seperate ResultSet to avoid interfering with the global Resultset

        try{

            query.setLength(0);
            query.append("SELECT COUNT(*) AS review_count FROM User_Reaction WHERE Review_ID = ?");

            reviewSTMT = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            reviewSTMT.setInt(1, reviewID);

            reviewSet = reviewSTMT.executeQuery();

            // Checks if ID exists
            if(reviewSet.next())
                reviewCount = reviewSet.getInt("review_count");


        } catch(SQLException e){

            e.printStackTrace();

        }  
        

        return reviewCount;

    }

    
}
