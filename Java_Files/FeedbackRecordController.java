import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;

// Class is the main controller for the Feedback Records
public class FeedbackRecordController implements ActionListener{

    private MainDBController mainController;

    // ADD viewer and models here
    private FeedbackRecord feedback;
    private FeedbackRecordViewer view;
    
    // Boolean checks if current user is an admin or not
    private boolean isAdmin;

    // Public constructor
    public FeedbackRecordController(Connection conn, MainDBController mainController, JPanel cardPanel){


        isAdmin = false;
        this.mainController = mainController;

        feedback = new FeedbackRecord(conn, this);
        view = new FeedbackRecordViewer(cardPanel, this);


        view.setActionListener(this);

        // Set intial value to false and hide buttons
        setIsAdmin(false);

    }


    // Getter method for feedback record
    public FeedbackRecord getFeedbackRecord(){

        return feedback;

    }


    public FeedbackRecordViewer getFeedbackRecordViewer(){

        return view;
    
    }


    // Setter method updates if current user who is logged in is an admin or user
    public void setIsAdmin(boolean isAdmin){

        this.isAdmin = isAdmin;

        displayAdminButtons(this.isAdmin);
         
    }


    // Method to check whether to display view of admin only buttons for non-admin users
    private void displayAdminButtons(boolean isAdmin){

        // Enables or disables based on boolean
        if(isAdmin){

            view.getFeedbackViewButton().setVisible(true);
            view.getUserReactViewButton().setVisible(true);
            view.getReactionViewButton().setVisible(true);
            view.getReportViewButton().setVisible(true);


        }
        else{

            view.getFeedbackViewButton().setVisible(false);
            view.getUserReactViewButton().setVisible(false);
            view.getReactionViewButton().setVisible(false);
            view.getReportViewButton().setVisible(false);
            view.getDisplayPanel().setVisible(false);

        }

    }



    // Method gets information for feedback info, checks for any errors, and then calls model to store to database
    private void storeFeedbackInfo(){

        String userID = view.getUserFeedbackPanelViewer().getUserFeedbackIDField().getText().trim();
        String locationID = view.getUserFeedbackPanelViewer().getLocationIDField().getText().trim();
        float rating = view.getUserFeedbackPanelViewer().getRatingsSlider().getValue();
        

        if(!checkFeedbackErrors(new String[] {userID, locationID}, new String[] {"userID", "locationID"})){

            System.out.println(userID + locationID + rating);
            feedback.addUserFeedback(Integer.valueOf(userID), Integer.valueOf(locationID), rating);

        }


    }

    // Method gets information for reaction info, checks for any errors, and then calls model to store to database
    private void storeUserReactionInfo(){

        String userID = view.getUserReactionPanelViewer().getUserReactionIDField().getText().trim();
        String reviewID = view.getUserReactionPanelViewer().getReviewIDField().getText().trim();
        String reactionType = (String) view.getUserReactionPanelViewer().getReactionTypeBox().getSelectedItem();
        

        if(!checkFeedbackErrors(new String[] {userID, reviewID}, new String[] {"userID", "reviewID"})){

            System.out.println(userID + reviewID + reactionType);
            feedback.addUserReaction(Integer.valueOf(userID), Integer.valueOf(reviewID), reactionType);

        }



    }


    // Method gets information for reaction info, checks for any errors, and then calls model to store to database
    private void storeReactionInfo(){

        String reactionName = view.getReactionPanelViewer().getReactionNameField().getText().trim();

        if(!checkFeedbackErrors(new String[] {reactionName}, new String[] {"reactionName"}))
            feedback.addReaction(reactionName, view.getUserReactionPanelViewer().getReactionTypeBox());

        
        
    }


    // Method gets information from component fields and updates the display in the feedback edit panel
    private void loadFeedbackEditUser(){

        String feedbackUser = (String) view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox().getSelectedItem();

        // Get the needed componenets to update display
        JComboBox<String> userBox = view.getUserFeedbackPanelViewer().getEditFeedbackUserIDBox();
        JComboBox<String> locationBox = view.getUserFeedbackPanelViewer().getEditFeedbackLocationIDBox();
        
        JSlider ratingSlider = view.getUserFeedbackPanelViewer().getEditFeedbackRatingsSlider();

        JSpinner reactionSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackReactionSpinner();
        JSpinner commentSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackCommentSpinner();
        JSpinner dateSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackDateSpinner();

        // Enable all components
       userBox.setEnabled(true);
       locationBox.setEnabled(true);
       ratingSlider.setEnabled(true);
       commentSpinner.setEnabled(true);
       dateSpinner.setEnabled(true);

        feedback.loadUserFeedback(feedbackUser, userBox, locationBox, ratingSlider, reactionSpinner, commentSpinner,
                                  dateSpinner);

    }   


    // Method gets information from user feedback component fields and calls model to update the row chosen
    private void updateFeedbackEditUser(){

        // Get the needed componenets to update database
        int review_ID = Integer.parseInt(((String) view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox().getSelectedItem()).split(" ")[0]);
        int userID = Integer.parseInt(((String) view.getUserFeedbackPanelViewer().getEditFeedbackUserIDBox().getSelectedItem()).split(" ")[0]);
        int locationID = Integer.parseInt(((String) view.getUserFeedbackPanelViewer().getEditFeedbackLocationIDBox().getSelectedItem()).split(" ")[0]);
        float rating = view.getUserFeedbackPanelViewer().getEditFeedbackRatingsSlider().getValue();
        int commentCount = (int) view.getUserFeedbackPanelViewer().getEditFeedbackCommentSpinner().getValue();
        java.sql.Timestamp reviewDate = new java.sql.Timestamp(((java.util.Date) view.getUserFeedbackPanelViewer().getEditFeedbackDateSpinner().getValue()).getTime());
            

        feedback.updateUserFeedbackDatabase(review_ID, userID, locationID, rating, commentCount, reviewDate);

        // Gets components to reset display
        ArrayList<String> userFeedbackSelectOptions = feedback.getUserFeedbackOptions();

        JComboBox<String> userFeedbackSelectBox = view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox();

        // Remove all selections
        userFeedbackSelectBox.removeAllItems();

        // Section loops and adds updated user feedback options and updates selected display
        for(String userReaction : userFeedbackSelectOptions)
            userFeedbackSelectBox.addItem(userReaction);

        for(String userReaction : userFeedbackSelectOptions)
            if(userReaction.startsWith(review_ID + " - "))
                userFeedbackSelectBox.setSelectedItem(userReaction);

    }


    // Method deletes information from user feedback database depending on feedback id chosen
    private void deleteFeedbackEditUser(){

        // Get the needed componenets to update database
        int review_ID = Integer.parseInt(((String) view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox().getSelectedItem()).split(" ")[0]);

        feedback.deleteUserFeedbackDatabase(review_ID);

        

        
        // Gets components to reset display
        ArrayList<String> userFeedbackSelectOptions = feedback.getUserFeedbackOptions();
        ArrayList<String> userIDOptions = feedback.getFeedbackUserIDOptions();
        ArrayList<String> locationIDOptions = feedback.getFeedbackLocationIDOptions();

        JComboBox<String> userFeedbackSelectBox = view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox();
        JComboBox<String> userIDBox = view.getUserFeedbackPanelViewer().getEditFeedbackUserIDBox();
        JComboBox<String> locationIDBox = view.getUserFeedbackPanelViewer().getEditFeedbackLocationIDBox();
        JSlider ratingSlider = view.getUserFeedbackPanelViewer().getEditFeedbackRatingsSlider();
        JSpinner reactionSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackReactionSpinner();
        JSpinner commentSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackCommentSpinner();
        JSpinner dateSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackDateSpinner();


        // Remove all selections
        userFeedbackSelectBox.removeAllItems();
        userIDBox.removeAllItems();
        locationIDBox.removeAllItems();


        // Section loops and adds updated user reaction options
        for(String userReaction : userFeedbackSelectOptions)
            userFeedbackSelectBox.addItem(userReaction);

        // Section loops and adds updated user options
        for(String user : userIDOptions)
            userIDBox.addItem(user);

        // Section loops and adds updated location options 
        for(String location : locationIDOptions)
            locationIDBox.addItem(location);


        if(userFeedbackSelectOptions.size() > 0){

            userFeedbackSelectBox.setSelectedIndex(0);

            feedback.loadUserFeedback(userFeedbackSelectOptions.get(0), userIDBox, locationIDBox, 
                                      ratingSlider, reactionSpinner, commentSpinner, dateSpinner);

        }
        else
            dateSpinner.setValue(new java.util.Date());

    }


    // Method gets information from user reaction component fields and calls model to update the row chosen
    private void loadUserReactionEditUser(){

        String userReviewUser = (String) view.getUserReactionPanelViewer().getUserReactionSelectComboBox().getSelectedItem();

        // Get the needed componenets to update display
        JComboBox<String> reviewBox = view.getUserReactionPanelViewer().getEditUserReactionReviewIDBox();
        JComboBox<String> userBox = view.getUserReactionPanelViewer().getEditUserReactionUserIDBox();
        JComboBox<String> reactionTypeBox = view.getUserReactionPanelViewer().getEditUserReactionReactionTypeIDBox();       

        JSpinner dateSpinner = view.getUserReactionPanelViewer().getEditUserReactionDateSpinner();

        // Enable all components
       userBox.setEnabled(true);
       reviewBox.setEnabled(true);
       userBox.setEnabled(true);
       reactionTypeBox.setEnabled(true);
       dateSpinner.setEnabled(true);

       feedback.loadUserReaction(userReviewUser, reviewBox, userBox, reactionTypeBox, dateSpinner);


    }


    // Method gets information from user reaction component fields and calls model to update the row chosen
    private void updateUserReactionEditUser(){

        // Get the needed componenets to update database
        int reactionID = Integer.parseInt(((String) view.getUserReactionPanelViewer().getUserReactionSelectComboBox().getSelectedItem()).split(" ")[0]);
        int reviewID = Integer.parseInt(((String) view.getUserReactionPanelViewer().getEditUserReactionReviewIDBox().getSelectedItem()).split(" ")[0]);
        int userID = Integer.parseInt(((String) view.getUserReactionPanelViewer().getEditUserReactionUserIDBox().getSelectedItem()).split(" ")[0]);
        int reactionTypeID =Integer.parseInt(((String) view.getUserReactionPanelViewer().getEditUserReactionReactionTypeIDBox().getSelectedItem()).split(" ")[0]);
        java.sql.Timestamp reviewDate = new java.sql.Timestamp(((java.util.Date) view.getUserReactionPanelViewer().getEditUserReactionDateSpinner().getValue()).getTime());
            

        // Calls method to update selected item in database
        feedback.updateUserReactionDatabase(reactionID, reviewID, userID, reactionTypeID, reviewDate);


        // Gets components to reset display
        ArrayList<String> userReactionSelectOptions = feedback.getUserReactionOptions();

        JComboBox<String> userReactionSelectBox = view.getUserReactionPanelViewer().getUserReactionSelectComboBox();

        // Remove all selections
        userReactionSelectBox.removeAllItems();

        // Section loops and adds updated user reaction options and updates selected display
        for(String userReaction : userReactionSelectOptions)
            userReactionSelectBox.addItem(userReaction);

        for(String userReaction : userReactionSelectOptions)
            if(userReaction.startsWith(reactionID + " - "))
                userReactionSelectBox.setSelectedItem(userReaction);

    }


    // Method deletes information from user reaction database depending on reaction id chosen
    private void deleteUserReactionEditUser(){

        // Get the needed componenets to update database
        int reactionID = Integer.parseInt(((String) view.getUserReactionPanelViewer().getUserReactionSelectComboBox().getSelectedItem()).split(" ")[0]);

        // Calls method to delete selected item in database
        feedback.deleteUserReactionDatabase(reactionID);


        // Gets components to reset display
        ArrayList<String> userReactionSelectOptions = feedback.getUserReactionOptions();
        ArrayList<String> reviewIDOptions = feedback.getUserReactionReviewIDOptions();
        ArrayList<String> userIDOptions = feedback.getUserReactionUserIDOptions();
        ArrayList<String> reactionTypeIDOptions = feedback.getUserReactionReactionTypeIDOptions();

        JComboBox<String> userReactionSelectBox = view.getUserReactionPanelViewer().getUserReactionSelectComboBox();
        JComboBox<String> reviewIDBox = view.getUserReactionPanelViewer().getEditUserReactionReviewIDBox();
        JComboBox<String> userIDBox = view.getUserReactionPanelViewer().getEditUserReactionUserIDBox();
        JComboBox<String> reactionTypeIDBox = view.getUserReactionPanelViewer().getEditUserReactionReactionTypeIDBox();
        JSpinner dateSpinner = view.getUserFeedbackPanelViewer().getEditFeedbackDateSpinner();


        // Remove all selections
        userReactionSelectBox.removeAllItems();
        reviewIDBox.removeAllItems();
        userIDBox.removeAllItems();
        reactionTypeIDBox.removeAllItems();

        // Section loops and adds updated user reaction options
        for(String userReaction : userReactionSelectOptions)
            userReactionSelectBox.addItem(userReaction);

        // Section loops and adds updated review options
        for(String review : reviewIDOptions)
            reviewIDBox.addItem(review);

        // Section loops and adds updated user options 
        for(String user : userIDOptions)
            userIDBox.addItem(user);

        // Section loops and adds updated reaction type options
        for(String reactionType : reactionTypeIDOptions)
            reactionTypeIDBox.addItem(reactionType);


        if(userReactionSelectOptions.size() > 0){

            userReactionSelectBox.setSelectedIndex(0);

            feedback.loadUserReaction(userReactionSelectOptions.get(0), reviewIDBox, userIDBox, reactionTypeIDBox, dateSpinner);
        }
        else
            dateSpinner.setValue(new java.util.Date());

    }


    // Method gets information from reaction component fields and calls model to update the row chosen
    private void loadReactionEditUser(){

        String reactonInfo = (String) view.getReactionPanelViewer().getReactionSelectComboBox().getSelectedItem();

        // Get the needed componenets to update display
        JTextField currentNameBox = view.getReactionPanelViewer().getCurrentReactionNameField();
        JTextField newNameBox = view.getReactionPanelViewer().getNewReactionNameField();
      
        // Enable newNameBox component
         newNameBox.setEnabled(true);


        feedback.loadReaction(reactonInfo, currentNameBox);


    }


    // Method gets information from reaction component fields and calls model to update the row chosen
    private void updateReactionEditUser(){

        // Get the needed componenets to update database
        int reactionTypeID = Integer.parseInt(((String) view.getReactionPanelViewer().getReactionSelectComboBox().getSelectedItem()).split(" ")[0]);
        String newName = view.getReactionPanelViewer().getNewReactionNameField().getText();
      

        if(!checkFeedbackErrors(new String[] {newName}, new String[] {"reactionName"})){

            feedback.updateReactionDatabase(reactionTypeID, newName);


            // Gets components to reset display
            ArrayList<String> reactionSelectOptions = feedback.getReactionWithIDOptions();
            JComboBox<String> selectBox = view.getReactionPanelViewer().getReactionSelectComboBox();
            JTextField currentNameField = view.getReactionPanelViewer().getCurrentReactionNameField();
            JTextField newNameField = view.getReactionPanelViewer().getNewReactionNameField();

            selectBox.removeAllItems();

            // Loop adds back new choices
            for(String reaction : reactionSelectOptions)
                selectBox.addItem(reaction);

            
            String updatedItem = reactionTypeID + " - " + newName;
            selectBox.setSelectedItem(updatedItem);

            currentNameField.setText(newName);
            newNameField.setText("");

        }

    }


    // Method deletes information from reaction database depending on reaction id chosen
    private void deleteReactionEditUser(){

        // Get the needed componenets to update database
        int reactionTypeID = Integer.parseInt(((String) view.getReactionPanelViewer().getReactionSelectComboBox().getSelectedItem()).split(" ")[0]);

        feedback.deleteReactionDatabase(reactionTypeID);

        // Gets components to reset display
        ArrayList<String> reactionSelectOptions = feedback.getReactionWithIDOptions();
        JComboBox<String> selectBox = view.getReactionPanelViewer().getReactionSelectComboBox();
        JTextField currentNameField = view.getReactionPanelViewer().getCurrentReactionNameField();
        JTextField newNameField = view.getReactionPanelViewer().getNewReactionNameField();

            selectBox.removeAllItems();

            // Loop adds back new choices
            for(String reaction : reactionSelectOptions)
                selectBox.addItem(reaction);

            
            // Checks if theres still any options left, any updates display to the first choice
            if(reactionSelectOptions.size() > 0){
                
                selectBox.setSelectedIndex(0);
                feedback.loadReaction(reactionSelectOptions.get(0), currentNameField);

            }
            else
                currentNameField.setText("");

            newNameField.setText("");


    }

    private boolean checkFeedbackErrors(String[] givenInputs, String[] givenInputNames){

        boolean errorFound = false; // Stores whether an error occured or not

        String[] inputs = givenInputs;
        String[] inputNames = givenInputNames;
        ArrayList<String> errorCodes = new ArrayList<>(); 
        ArrayList<String> errorInputs = new ArrayList<>(); 

        for(int i = 0; i < inputs.length; i++){

            // Different validity checks whether given is ID or not
            if(!givenInputNames[i].equals("reactionName")){

                // Conditional checking for input at current index
                if(inputs[i].isEmpty()){

                    errorInputs.add(inputNames[i]);
                    errorCodes.add(FeedbackRecordViewer.NO_INPUT);
                    errorFound = true;

                }
                else if(inputs[i].length() < 1){

                    errorInputs.add(inputNames[i]);
                    errorCodes.add(FeedbackRecordViewer.LENGTH_INSUFFICIENT);
                    errorFound = true;

                }    
                else if(inputs[i].length() > 4){

                    errorInputs.add(inputNames[i]);
                    errorCodes.add(FeedbackRecordViewer.LENGTH_EXCEEDED);
                    errorFound = true;

                }
                else if(inputs[i].matches(".*(?i)[a-z].*")){

                    errorInputs.add(inputNames[i]);
                    errorCodes.add(FeedbackRecordViewer.INVALID_INPUT);
                    errorFound = true;

                }   

            }
            else{

                // Conditional checking for input at current index

                for(int j = 0; j < feedback.getReactionOptions().size(); j++)
                    if(inputs[i].equals(feedback.getReactionOptions().get(j))){

                        errorInputs.add(inputNames[i]);
                        errorCodes.add(FeedbackRecordViewer.EXISTING_RECORD);   
                        errorFound = true;

                    }
                    
            }
        }


        if(errorFound)
            view.showError(errorCodes, errorInputs);

        return errorFound;

    }


 


    // Method provides what the buttons will do when pressed.
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String command = e.getActionCommand(); //Get String equivalent of the action command


        //Switch statement to check the command and do the corresponding action
        switch(command){

            case MainDBViewer.BACK_LINK:

                mainController.getMainDBViewer().showPanel(MainDBViewer.BACK_LINK); //Closes program
                break;

            case "Feedback Table":

                view.showPanel(UserFeedbackPanelViewer.FEEDBACK_VIEW_LINK);
                feedback.loadUserFeedbackTable(view.getUserFeedbackPanelViewer().getFeedbackModel());
                break;

            case UserFeedbackPanelViewer.FEEDBACK_CREATE_LINK:

                view.showPanel(UserFeedbackPanelViewer.FEEDBACK_CREATE_LINK);
                break;

            case UserFeedbackPanelViewer.FEEDBACK_EDIT_LINK:

                // Refresh the feedback select combo box
                ArrayList<String> feedbackSelectOptions = feedback.getUserFeedbackOptions();
                JComboBox<String> feedbackSelectBox = view.getUserFeedbackPanelViewer().getFeedbackSelectComboBox();
                
                feedbackSelectBox.removeAllItems(); // Removes all content to update
                
                // For loop to add updates options
                for(String feedback : feedbackSelectOptions)
                    feedbackSelectBox.addItem(feedback);
                
                if(feedbackSelectOptions.size() > 0)
                    feedbackSelectBox.setSelectedIndex(0);


                // Show panel after
                view.showPanel(UserFeedbackPanelViewer.FEEDBACK_EDIT_LINK);

                break;

            case UserFeedbackPanelViewer.CREATE_FEEDBACK_USER_LINK:

                storeFeedbackInfo();
                view.showMessage(FeedbackRecordViewer.DATA_ADDED); // Show message

                break;

            case UserFeedbackPanelViewer.LOAD_FEEDBACK_EDIT_LINK:

                loadFeedbackEditUser();
                break;

            case UserFeedbackPanelViewer.EDIT_FEEDBACK_USER_LINK:

                if(view.getUserFeedbackPanelViewer().getEditFeedbackLocationIDBox().isEnabled())
                    updateFeedbackEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("reviewID")));

                view.showMessage(FeedbackRecordViewer.DATA_EDITED); // Show message

                break;

            case UserFeedbackPanelViewer.DELETE_FEEDBACK_USER_LINK:

                if(view.getUserFeedbackPanelViewer().getEditFeedbackLocationIDBox().isEnabled())
                    deleteFeedbackEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("reviewID")));

                view.showMessage(FeedbackRecordViewer.DATA_REMOVED); // Show message

                break;

            case "User Reaction Table":

                view.showPanel(UserReactionPanelViewer.USER_REACTION_VIEW_LINK);
                feedback.loadUserReactionTable(view.getUserReactionPanelViewer().getReviewModel());
                break;

            case UserReactionPanelViewer.USER_REACTION_CREATE_LINK:

                view.showPanel(UserReactionPanelViewer.USER_REACTION_CREATE_LINK);
                break;

            case UserReactionPanelViewer.USER_REACTION_EDIT_LINK:

                // Refresh the user reaction select combo box
                ArrayList<String> userReactionSelectOptions = feedback.getUserReactionOptions();
                JComboBox<String> userReactionSelectBox = view.getUserReactionPanelViewer().getUserReactionSelectComboBox();
                
                userReactionSelectBox.removeAllItems(); // Removes all content to update
                
                // For loop to add updates options
                for(String userReaction : userReactionSelectOptions)
                    userReactionSelectBox.addItem(userReaction);
                
                // Displays first option if there is content to display
                if(userReactionSelectOptions.size() > 0)
                    userReactionSelectBox.setSelectedIndex(0);


                // Show panel after
                view.showPanel(UserReactionPanelViewer.USER_REACTION_EDIT_LINK);

                break;

            case UserReactionPanelViewer.CREATE_USERREACTION_USER_LINK:

                storeUserReactionInfo();
                view.showMessage(FeedbackRecordViewer.DATA_ADDED); // Show message

                break;

            case UserReactionPanelViewer.LOAD_USERREACTION_USER_LINK:

                loadUserReactionEditUser();
                break;

            case UserReactionPanelViewer.EDIT_USERREACTION_USER_LINK:

            
                if(view.getUserReactionPanelViewer().getEditUserReactionDateSpinner().isEnabled())
                    updateUserReactionEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("userReactionID")));

                view.showMessage(FeedbackRecordViewer.DATA_EDITED); // Show message
            
                break;

            case UserReactionPanelViewer.DELETE_USERREACTION_USER_LINK:

                if(view.getUserReactionPanelViewer().getEditUserReactionDateSpinner().isEnabled())
                    deleteUserReactionEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("userReactionID")));

                view.showMessage(FeedbackRecordViewer.DATA_REMOVED); // Show message

                break;

            case "Feedback Report":

                view.showPanel(FeedbackRecordViewer.REPORT_VIEW_LINK);
                feedback.loadReport(view.getReportModel());
                break;


            case ReactionPanelViewer.REACTION_CREATE_LINK:

                view.showPanel(ReactionPanelViewer.REACTION_CREATE_LINK);
                break;
  

            case ReactionPanelViewer.REACTION_EDIT_LINK:

                // Refresh the reaction select combo box
                ArrayList<String> reactionSelectOptions = feedback.getReactionWithIDOptions();
                JComboBox<String> reactionSelectBox = view.getReactionPanelViewer().getReactionSelectComboBox();
                
                reactionSelectBox.removeAllItems(); // Removes all content to update
                
                // For loop to add updates options
                for(String reaction : reactionSelectOptions)
                    reactionSelectBox.addItem(reaction);
    
                // Displays first option if there is content to display
                if(reactionSelectOptions.size() > 0)
                    reactionSelectBox.setSelectedIndex(0);


                // Show panel after
                view.showPanel(ReactionPanelViewer.REACTION_EDIT_LINK);

                break;

            case ReactionPanelViewer.LOAD_REACTION_USER_LINK:

                loadReactionEditUser();
                
                break;

            case ReactionPanelViewer.CREATE_REACTION_USER_LINK:

                storeReactionInfo();
                view.showMessage(FeedbackRecordViewer.DATA_ADDED); // Show message

                break;     


            case ReactionPanelViewer.EDIT_REACTION_USER_LINK:

                if(view.getReactionPanelViewer().getNewReactionNameField().isEnabled())
                    updateReactionEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("userReactionID")));

                view.showMessage(FeedbackRecordViewer.DATA_EDITED); // Show message

                break;
                
            case ReactionPanelViewer.DELETE_REACTION_USER_LINK:

                if(view.getReactionPanelViewer().getNewReactionNameField().isEnabled())
                    deleteReactionEditUser();
                else
                    view.showError(new ArrayList<String>(Arrays.asList(FeedbackRecordViewer.USER_NOT_LOADED)), 
                                   new ArrayList<String>(Arrays.asList("userReactionID")));

                view.showMessage(FeedbackRecordViewer.DATA_REMOVED); // Show message

                break;

            case "Reaction Table":

                view.showPanel(ReactionPanelViewer.REACTION_VIEW_LINK);
                feedback.loadReactionTable(view.getReactionPanelViewer().getReactionModel());
                break;

        }

    }


    
}
