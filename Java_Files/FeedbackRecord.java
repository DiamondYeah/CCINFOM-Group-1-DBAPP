import java.sql.*;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FeedbackRecord {

    public StringBuilder query;


    // Main controller
    private FeedbackRecordController controller;

    public Connection conn;
    public PreparedStatement stmt; // Gets the statements
    public ResultSet set; // Prepares for multiple results


    // Private attributes containing information of different attributes from the dataset
    private ArrayList<String> reactionOptions;

    private ArrayList<String> userFeedbackOptions;
    private ArrayList<String> feedbackUserIDOptions;
    private ArrayList<String> feedbackLocationIDOptions;

    private ArrayList<String> userReactionOptions;
    private ArrayList<String> userReactionReviewIDOptions;
    private ArrayList<String> userReactionUserIDOptions;
    private ArrayList<String> userReactionReactionTypeIDOptions;

    private ArrayList<String> reactionWithIDOptions;

    // Public Constructor
    public FeedbackRecord(Connection conn, FeedbackRecordController controller){

        this.conn = conn;
        this.controller = controller;

        // Initialize StringBuilder
        query = new StringBuilder();

        
        // Initialize the User Feedback Panel Arraylist Options
        userFeedbackOptions = new ArrayList<>();
        feedbackUserIDOptions = new ArrayList<>();
        feedbackLocationIDOptions = new ArrayList<>();

        // Initialize the User Reaction Panel Arraylist Options
        userReactionOptions = new ArrayList<>();
        userReactionReviewIDOptions = new ArrayList<>();
        userReactionUserIDOptions = new ArrayList<>();
        userReactionReactionTypeIDOptions = new ArrayList<>();

        // Initialize the Reaction Options and Reaction Panel Arraylist Options
        reactionOptions = new ArrayList<>();
        reactionWithIDOptions = new ArrayList<>();

    }

    // -------------------------------------------------------

    // Getter Methods

    public ArrayList<String> getReactionOptions(){

        return reactionOptions;

    }


    // Getters for User Feedback Panel
    public ArrayList<String> getUserFeedbackOptions(){

        return userFeedbackOptions;

    }


    public ArrayList<String> getFeedbackUserIDOptions(){

        return feedbackUserIDOptions;

    }


    public ArrayList<String> getFeedbackLocationIDOptions(){

        return feedbackLocationIDOptions;

    }


    // Getters for User Reaction Panel
    public ArrayList<String> getUserReactionOptions(){

        return userReactionOptions;

    }


    public ArrayList<String> getUserReactionReviewIDOptions(){

        return userReactionReviewIDOptions;

    }

  
    public ArrayList<String> getUserReactionUserIDOptions(){

        return userReactionUserIDOptions;

    }


    public ArrayList<String> getUserReactionReactionTypeIDOptions(){

        return userReactionReactionTypeIDOptions;

    }


    // Getters for Reaction Panel
    public ArrayList<String> getReactionWithIDOptions(){

        return reactionWithIDOptions;

    }

    // -------------------------------------------------------


    
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

            // Update choices
            updateUserFeedbackOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the feedback model
    public void loadUserFeedbackTable(DefaultTableModel model){

        model.setRowCount(0);

        try{

            // Modified query fields and create extra fields for positive and negative counts
            query.setLength(0);
            query.append("SELECT uf.*, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = TRUE THEN 1 END) AS positive_count, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = FALSE THEN 1 END) AS negative_count ");
            query.append("FROM User_Feedback uf ");
            query.append("LEFT JOIN User_Reaction ur ON ur.review_id = uf.review_id ");
            query.append("LEFT JOIN Reaction r ON r.reactiontype_id = ur.reactiontype_id ");
            query.append("GROUP BY uf.review_id; ");


            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reviewID = set.getInt("Review_ID");
                int userID = set.getInt("User_ID");
                int locationID = set.getInt("Location_ID");
                float rating = set.getFloat("Rating");
                int positiveCount = set.getInt("Positive_Count");
                int negativeCount = set.getInt("Negative_Count");
                int commentCount = set.getInt("Comment_Count");
                Timestamp reviewDate = set.getTimestamp("Review_Date");

                model.addRow(new Object[]{
                    reviewID, userID, locationID, rating, positiveCount, negativeCount, commentCount, reviewDate
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method loads the user chosen in the edit feedback table and shows information of them
    public void loadUserFeedback(String feedbackUser, JComboBox<String> userBox, JComboBox<String> locationBox, JSlider ratingSlider,
                                JSpinner negativeSpinner, JSpinner positiveSpinner, JSpinner commentSpinner, JSpinner dateSpinner){

        int reviewID = Integer.parseInt(feedbackUser.split(" ")[0]);
            query.setLength(0);

        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("SELECT uf.*, CONCAT(u.first_name, ' ' , u.last_name) AS full_name, ts.spotname, ");
        query.append("COUNT(CASE WHEN r.Is_Positive = TRUE THEN 1 END) AS positive_count, ");
        query.append("COUNT(CASE WHEN r.Is_Positive = FALSE THEN 1 END) AS negative_count ");
        query.append("FROM USER_FEEDBACK uf JOIN user u ON u.user_id = uf.user_id ");
        query.append("JOIN travel_spot ts ON ts.location_id = uf.location_id ");
        query.append("LEFT JOIN User_Reaction ur ON ur.review_id = uf.review_id ");
        query.append("LEFT JOIN Reaction r ON r.reactiontype_id = ur.reactiontype_id ");
        query.append("WHERE uf.review_id = ? ");
        query.append("GROUP BY uf.review_id; ");
        
        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reviewID);

            set = stmt.executeQuery();

            if(set.next()){

                // Get information
                int userID = set.getInt("User_ID");
                int locationID = set.getInt("Location_ID");
                float rating = set.getFloat("Rating");
                int positiveCount = set.getInt("Positive_Count");
                int negativeCount = set.getInt("Negative_Count");
                int commentCount = set.getInt("Comment_Count");
                Timestamp reviewDate = set.getTimestamp("Review_Date");

                // Information mainly for making comboBox work
                String fullname = set.getString("full_name");
                String spotName = set.getString("spotname");

                // Display the results of the query to the input components
                
                userBox.setSelectedItem(userID + " - " + fullname);
                locationBox.setSelectedItem(locationID + " - " + spotName);
                ratingSlider.setValue(Math.round((rating - 1.0f) * 10));
                positiveSpinner.setValue(positiveCount);
                negativeSpinner.setValue(negativeCount);
                commentSpinner.setValue(commentCount);
                dateSpinner.setValue(reviewDate);

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method updates the user chosen in the edit feedback table and updates it in the database
    public void updateUserFeedbackDatabase(int reviewID, int userID, int locationID, float rating, int commentCount, 
                                           java.sql.Timestamp reviewDate){


        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("UPDATE USER_FEEDBACK SET user_id = ?, location_id = ?, rating = ?, comment_count = ?, review_date = ? ");
        query.append("WHERE review_id = ?; ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, userID);
            stmt.setInt(2, locationID);
            stmt.setFloat(3, rating);
            stmt.setInt(4, commentCount);
            stmt.setTimestamp(5, reviewDate);

            stmt.setInt(6, reviewID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateUserFeedbackOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method deletes the user chosen in the edit feedback table and removes it from the database
    public void deleteUserFeedbackDatabase(int reviewID){

        // String builder query to create the DELETE statement
        query.setLength(0);
        query.append("DELETE FROM USER_FEEDBACK WHERE review_id = ?; ");


        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reviewID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateUserFeedbackOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // -------------------------------------------------------


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

            // Update choices
            updateUserReactionOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the report model
    public void loadUserReactionTable(DefaultTableModel model){

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


    // Method loads the user chosen in the edit feedback table and shows information of them
    public void loadUserReaction(String userReviewUser, JComboBox<String> reviewBox, JComboBox<String> userBox,
                                 JComboBox<String> reactionTypeBox, JSpinner dateSpinner){

                                    
        int userReviewID = Integer.parseInt(userReviewUser.split(" ")[0]);

        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("SELECT ur.*, CONCAT(u.first_name, ' ' , u.last_name) AS full_name, r.reaction_name ");
        query.append("FROM USER_REACTION ur JOIN user u ON u.user_id = ur.user_id ");
        query.append("JOIN reaction r ON r.reactiontype_id = ur.reactiontype_id ");
        query.append("WHERE reaction_id = ?; ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, userReviewID);

            set = stmt.executeQuery();

            if(set.next()){

                // Get information
                int reviewID = set.getInt("review_id");
                int userID = set.getInt("user_id");
                int reactionTypeID = set.getInt("reactiontype_id");
                Timestamp reviewDate = set.getTimestamp("reaction_date");

                // Information mainly for making comboBox work
                String fullname = set.getString("full_name");
                String reactionName = set.getString("reaction_name");

                // Display the results of the query to the input components
                reviewBox.setSelectedItem(reviewID + " - " + fullname); 
                userBox.setSelectedItem(userID + " - " + fullname);
                reactionTypeBox.setSelectedItem(reactionTypeID + " - " + reactionName);
                dateSpinner.setValue(reviewDate);

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // Method updates the user reaction chosen in the edit user reaction table and updates it in the database
    public void updateUserReactionDatabase(int reactionID, int reviewID, int userID, int reactionTypeID, java.sql.Timestamp reviewDate){

        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("UPDATE USER_REACTION SET review_id = ?, user_id = ?, reactiontype_id = ?, reaction_date = ? WHERE reaction_id = ?; ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reviewID);
            stmt.setInt(2, userID);
            stmt.setInt(3, reactionTypeID);
            stmt.setTimestamp(4, reviewDate);

            stmt.setInt(5, reactionID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateUserReactionOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method deletes the user chosen in the edit feedback table and removes it from the database
    public void deleteUserReactionDatabase(int reactionID){

        // String builder query to create the DELETE statement
        query.setLength(0);
        query.append("DELETE FROM USER_REACTION WHERE reaction_id = ?; ");


        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reactionID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateUserReactionOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }

    }
     // -------------------------------------------------------

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


        // Update choices
        updateReactionOptions();

        reactionTypeBox.removeAllItems();
        
        for(int i = 0; i < reactionOptions.size(); i++)
            reactionTypeBox.addItem(reactionOptions.get(i));

    }


    // Method obtains the infromation from stmt and gets the needed information and adds them to the review/user_reaction model
    public void loadReactionTable(DefaultTableModel model){

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


    // Method loads the user chosen in the reaction table and shows information of them
    public void loadReaction(String reactonInfo, JTextField currentNameField){


        int reactionTypeID = Integer.parseInt(reactonInfo.split(" ")[0]);

        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("SELECT * FROM REACTION WHERE reactiontype_id = ?; ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reactionTypeID);

            set = stmt.executeQuery();

            if(set.next()){

                // Get information
                String reactionName = set.getString("reaction_name");
    
                // Display the results of the query to the input components
                currentNameField.setText(reactionName);

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method updates the reaction chosen in the edit reation table and updates it in the database
    public void updateReactionDatabase(int reactionTypeID, String newName){

        // String builder query to create the SELECT statement
        query.setLength(0);
        query.append("UPDATE REACTION SET reaction_name = ? WHERE reactiontype_id = ?; ");

        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setString(1, newName);

            stmt.setInt(2, reactionTypeID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateReactionOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method deletes the reaction chosen in the edit reaction table and removes it from the database
    public void deleteReactionDatabase(int reactionTypeID){

        // String builder query to create the DELETE statement
        query.setLength(0);
        query.append("DELETE FROM REACTION WHERE reactiontype_id = ?; ");


        // Try-catch block to catch any errors with SQL
        try{

            stmt = conn.prepareStatement(query.toString());

            // Set the values in the stmt with the parameters and starting values for each of the ? in the query
            stmt.setInt(1, reactionTypeID);

            // Update the data to the dataset
            stmt.executeUpdate();

            System.out.println("DATABASE UPDATED");

            // Update choices
            updateReactionOptions();

        } catch(SQLException e){

            e.printStackTrace();

        }

    }

    // -------------------------------------------------------


    // Method obtains the infromation from stmt and gets the needed information and adds them to the feedback model
    public void loadReport(DefaultTableModel model){

        model.setRowCount(0);

        try{

            query.setLength(0);

            // Query that will generate the report
            query.append("SELECT uf.review_id, CONCAT(u.first_name, ' ' , u.last_name) AS full_name, pt.tier_name, ts.spotname, uf.rating, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = TRUE THEN 1 END) AS positive_count, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = FALSE THEN 1 END) AS negative_count, ");
            query.append("DATE_FORMAT(uf.Review_Date, '%M %Y') AS Review_Month_Year ");
            query.append("FROM User_feedback uf JOIN User u ON uf.User_ID = u.User_ID ");
            query.append("LEFT JOIN Points_tier pt ON u.Tier_ID = pt.Tier_ID ");
            query.append("JOIN Travel_spot ts ON uf.Location_ID = ts.Location_ID ");
            query.append("LEFT JOIN User_reaction ur ON uf.Review_ID = ur.Review_ID ");
            query.append("LEFT JOIN Reaction r ON ur.ReactionType_ID = r.ReactionType_ID ");
            query.append("GROUP BY review_id, full_name, tier_name, ts.spotname, uf.rating, Review_Month_Year ");
            query.append("ORDER BY COALESCE(pt.tier_id, 0) DESC, uf.rating DESC;  ");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reviewID = set.getInt("Review_ID");
                String fullName = set.getString("Full_Name");
                String tierName = set.getString("Tier_Name");
                String spotName = set.getString("Spotname");
                double rating = set.getDouble("Rating");
                int positiveCount = set.getInt("positive_count");
                int negativeCount = set.getInt("negative_count");
                String reviewMonthYear = set.getString("Review_Month_Year");
                
                model.addRow(new Object[]{
                    reviewID, fullName, tierName, spotName, rating, positiveCount, negativeCount, reviewMonthYear
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Method filters the exisiting report based on the given month and year
    public void loadFilteredReport(DefaultTableModel model, String month, String year){

        model.setRowCount(0);

        try{

            query.setLength(0);

            // Query that will generate the report
            query.append("SELECT uf.review_id, CONCAT(u.first_name, ' ' , u.last_name) AS full_name, pt.tier_name, ts.spotname, uf.rating, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = TRUE THEN 1 END) AS positive_count, ");
            query.append("COUNT(CASE WHEN r.Is_Positive = FALSE THEN 1 END) AS negative_count, ");
            query.append("DATE_FORMAT(uf.Review_Date, '%M %Y') AS Review_Month_Year ");
            query.append("FROM User_feedback uf JOIN User u ON uf.User_ID = u.User_ID ");
            query.append("LEFT JOIN Points_tier pt ON u.Tier_ID = pt.Tier_ID ");
            query.append("JOIN Travel_spot ts ON uf.Location_ID = ts.Location_ID ");
            query.append("LEFT JOIN User_reaction ur ON uf.Review_ID = ur.Review_ID ");
            query.append("LEFT JOIN Reaction r ON ur.ReactionType_ID = r.ReactionType_ID ");

            // Append WHERE clause depending on the given month and year

            // Checks if a filter has been applied to either month or year
            if(!month.equals("All") || !year.equals("All")){
                
                query.append("WHERE ");

                // Checks for month and year and sees if they have a filter. If so, append to query with condition
                if(!month.equals("All") && !year.equals("All"))
                    query.append("MONTHNAME(uf.Review_Date) = ? AND YEAR(uf.Review_Date) = ? ");
                else if(!month.equals("All"))
                    query.append("MONTHNAME(uf.Review_Date) = ? ");
                else if(!year.equals("All"))
                    query.append("YEAR(uf.Review_Date) = ? ");

            }

            // Add the rest of the query
            query.append(" GROUP BY review_id, full_name, tier_name, ts.spotname, uf.rating, Review_Month_Year ");
            query.append(" ORDER BY COALESCE(pt.tier_id, 0) DESC, uf.rating DESC;  ");

            stmt = conn.prepareStatement(query.toString());

            // Set parameters to filter query

            // Conditional checking whether month or year has a filter, and adds corresponding values to parameters
            if(!month.equals("All") && !year.equals("All")){

                stmt.setString(1, month);
                stmt.setInt(2, Integer.parseInt(year));

            }
            else if(!month.equals("All"))
                stmt.setString(1, month); 
            else if(!year.equals("All"))
                stmt.setInt(1, Integer.parseInt(year));


            set = stmt.executeQuery();

            while(set.next()){

                int reviewID = set.getInt("Review_ID");
                String fullName = set.getString("Full_Name");
                String tierName = set.getString("Tier_Name");
                String spotName = set.getString("spotname");
                double rating = set.getDouble("Rating");
                int positiveCount = set.getInt("Positive_Count");
                int negativeCount = set.getInt("Negative_count");
                String reviewMonthYear = set.getString("Review_Month_Year");
                
                model.addRow(new Object[]{
                    reviewID, fullName, tierName, spotName, rating, positiveCount, negativeCount, reviewMonthYear
                });

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }

    // -------------------------------------------------------

    // Methods to obtain fields and update the database

    // Methods obtains the different reactions types from the reactions table
    public void updateUserFeedbackOptions(){

        // Resets arrayList in case of updates 
        userFeedbackOptions.clear(); 
        feedbackLocationIDOptions.clear();
        feedbackUserIDOptions.clear();
        
        try{

            query.setLength(0);
            
            // Checks if current user is admin, if not, only show feedback associated with them
            if(controller.getIsAdmin()){

                query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, uf.review_id ");
                query.append("FROM   USER_FEEDBACK uf ");
                query.append("JOIN   USER u ON u.user_id = uf.user_id; ");

            }
            else{

                query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, uf.review_id ");
                query.append("FROM   USER_FEEDBACK uf ");
                query.append("JOIN   USER u ON u.user_id = uf.user_id ");  
                query.append("WHERE  uf.user_id = ?; ");  

            }

            
            stmt = conn.prepareStatement(query.toString());

            // Supplies user ID if not admin
            if(!controller.getIsAdmin())
                stmt.setInt(1, controller.getCurrentUserID());

            set = stmt.executeQuery();

            while(set.next()){

                String userFeedbackOption = set.getInt("uf.review_id") + " - " + set.getString("full_name");

                System.out.println(userFeedbackOption);

                userFeedbackOptions.add(userFeedbackOption); // Add reaction type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


        try{

            query.setLength(0);
            
            query.append("SELECT * FROM TRAVEL_SPOT;");
            
            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String getLocationOption = set.getString("location_id") + " - " + set.getString("spotname");
                feedbackLocationIDOptions.add(getLocationOption); // Add reaction type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


        try{

            query.setLength(0);
            
            query.append("SELECT u.user_id , CONCAT(u.first_name, ' ' , u.last_name) AS full_name FROM USER u; ");
            
            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String userOption = set.getString("u.user_id") + " - " + set.getString("full_name");
                feedbackUserIDOptions.add(userOption); // Add reaction type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }

    }


    // Methods obtains the different reactions types from the reactions table
    public void updateUserReactionOptions(){


        // Resets arrayList in case of updates 
        userReactionOptions.clear();
        userReactionReviewIDOptions.clear();
        userReactionUserIDOptions.clear();
        userReactionReactionTypeIDOptions.clear();

        // Create/Update userReactionOptions
        try{

            query.setLength(0);
            
            // Checks if current user is admin, if not, only show feedback associated with them

            if(controller.getIsAdmin()){

                query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, ur.reaction_id ");
                query.append("FROM   USER_REACTION ur ");
                query.append("JOIN   USER u ON u.user_id = ur.user_id; ");

            }else{

                query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, ur.reaction_id ");
                query.append("FROM   USER_REACTION ur ");
                query.append("JOIN   USER u ON u.user_id = ur.user_id ");
                query.append("WHERE  ur.user_id = ?; ");

            }

            
            stmt = conn.prepareStatement(query.toString());

            // Supplies user ID if not admin
            if(!controller.getIsAdmin())
                stmt.setInt(1, controller.getCurrentUserID());

            set = stmt.executeQuery();

            while(set.next()){

                String userReactionOption = set.getInt("ur.reaction_id") + " - " + set.getString("full_name");

                System.out.println(userReactionOption);

                userReactionOptions.add(userReactionOption); 

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


        // Create/Update userReactionReviewIDOptions
        try{

            query.setLength(0);
            
            query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, uf.review_id ");
            query.append("FROM   USER_FEEDBACK uf ");
            query.append("JOIN   USER u ON u.user_id = uf.user_id; ");
            
            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String userReactionReviewIDOption = set.getString("uf.review_id") + " - " + set.getString("full_name");
                userReactionReviewIDOptions.add(userReactionReviewIDOption); 

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


        // Create/Update userReactionUserIDOptions
        try{

            query.setLength(0);
            
            query.append("SELECT CONCAT(u.first_name, ' ' , u.last_name) AS full_name, u.* FROM USER u; ");
            
            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String userReactionUserIDOption = set.getString("user_id") + " - " + set.getString("full_name");
                userReactionUserIDOptions.add(userReactionUserIDOption); // Add reaction type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


        // Create/Update userReactionReactionTypeIDOptions
        try{

            query.setLength(0);
            
            query.append("SELECT * FROM REACTION; ");
            
            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                String userReactionReactionTypeIDOption = set.getString("reactiontype_id") + " - " + set.getString("reaction_name");
                userReactionReactionTypeIDOptions.add(userReactionReactionTypeIDOption); // Add reaction type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


    }




    // Methods obtains the different available user feedbacks
    public void updateReactionOptions(){

        // Resets arrayLists in case of updates
        reactionOptions.clear(); 
        reactionWithIDOptions.clear();
        
        try{

            query.setLength(0);
            query.append("SELECT * FROM Reaction");

            stmt = conn.prepareStatement(query.toString());
            set = stmt.executeQuery();

            while(set.next()){

                int reactionTypeID = set.getInt("ReactionType_ID");
                String reactionType = set.getString("Reaction_Name");

                reactionOptions.add(reactionType); // Add reaction type to array list

                reactionWithIDOptions.add(reactionTypeID + " - " + reactionType); // Add id and type to array list

            }

        } catch(SQLException e){

            e.printStackTrace();

        }


    }


    // -------------------------------------------------------

   
}

