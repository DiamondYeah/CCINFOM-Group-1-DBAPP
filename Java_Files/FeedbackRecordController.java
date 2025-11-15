import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JPanel;

// Class is the main controller for the Feedback Records
public class FeedbackRecordController implements ActionListener{

    private MainDBController mainController;

    // ADD viewer and models here
    private FeedbackRecord feedback;
    private FeedbackRecordViewer view;


    // Public constructor
    public FeedbackRecordController(Connection conn, MainDBController mainController, JPanel cardPanel){



        this.mainController = mainController;

        feedback = new FeedbackRecord(conn);

        view = new FeedbackRecordViewer(cardPanel, this);
        view.setActionListener(this);



    }


    // Getter methods (w/ Access to both model and viewer)
    public ArrayList<String> getModelReactionOption(){

        return feedback.getReactionOptions();

    }

    // Method gets information for feedback info, checks for any errors, and then calls model to store to database
    private void storeFeedbackInfo(){

        String userID = view.getUserFeedbackIDField().getText().trim();
        String locationID = view.getLocationIDField().getText().trim();
        float rating = view.getRatingsSlider().getValue();
        

        if(!checkFeedbackErrors(new String[] {userID, locationID}, new String[] {"userID", "locationID"})){

            System.out.println(userID + locationID + rating);
            feedback.addUserFeedback(Integer.valueOf(userID), Integer.valueOf(locationID), rating);

        }


    }

    // Method gets information for reaction info, checks for any errors, and then calls model to store to database
    private void storeUserReactionInfo(){

        String userID = view.getUserReactionIDField().getText().trim();
        String reviewID = view.getReviewIdField().getText().trim();
        String reactionType = (String) view.getReactionTypeBox().getSelectedItem();
        

        if(!checkFeedbackErrors(new String[] {userID, reviewID}, new String[] {"userID", "reviewID"})){

            System.out.println(userID + reviewID + reactionType);
            feedback.addUserReaction(Integer.valueOf(userID), Integer.valueOf(reviewID), reactionType);

        }



    }


    // Method gets information for reaction info, checks for any errors, and then calls model to store to database
    private void storeReactionInfo(){

        String reactionName = view.getReactionNameField().getText().trim();

        if(!checkFeedbackErrors(new String[] {reactionName}, new String[] {"reactionName"})){

            System.out.println(reactionName);
            feedback.addReaction(reactionName, view.getReactionTypeBox());

        }



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

            case "View Feedback Table":

                view.showPanel(FeedbackRecordViewer.FEEDBACK_VIEW_LINK);
                feedback.loadUserFeedback(view.getFeedbackModel());
                break;

            case "View User Reaction Table":

                view.showPanel(FeedbackRecordViewer.USER_REACTION_VIEW_LINK);
                feedback.loadUserReaction(view.getReviewModel());
                break;

            case "View Feedback Report":

                view.showPanel(FeedbackRecordViewer.REPORT_VIEW_LINK);
                feedback.loadReport(view.getReportModel());
                break;

            case "View Reaction Table":

                view.showPanel(FeedbackRecordViewer.REACTION_VIEW_LINK);
                feedback.loadReaction(view.getReactionModel());
                break;


            case "Create User Feedback":

                view.showPanel(FeedbackRecordViewer.FEEDBACK_CREATE_LINK);
                break;

            case "Create User Reaction":

                view.showPanel(FeedbackRecordViewer.USER_REACTION_CREATE_LINK);
                break;

            case "Create Reaction":

                view.showPanel(FeedbackRecordViewer.REACTION_CREATE_LINK);
                break;
  

            case FeedbackRecordViewer.CREATE_FEEDBACK_USER_LINK:


                System.out.println("GET INFO!");
                storeFeedbackInfo();
                break;


            case FeedbackRecordViewer.CREATE_USERREACTION_USER_LINK:

                System.out.println("GET INFO!");
                storeUserReactionInfo();
                break;


            case FeedbackRecordViewer.CREATE_REACTION_USER_LINK:

                System.out.println("GET INFO!");
                storeReactionInfo();
                break;       




        }

    }


    
}
