
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class FeedbackRecordViewer extends JPanel{

    
    // Constants to link the different transactions and reports found in buttons
    public static final String REPORT_VIEW_LINK = "Feedback Report";

    // Constant for different error codes
    public static final String LENGTH_EXCEEDED = "Input Exceeded";
    public static final String LENGTH_INSUFFICIENT = "Input Insufficient";
    public static final String NO_INPUT = "Input Blank";
    public static final String INVALID_INPUT = "Input Invalid";
    public static final String EXISTING_RECORD = "Given Already Exists in Database";
    public static final String USER_NOT_LOADED = "User Has Not Been Loaded";

    // Constant for different messsage codes
    public static final String DATA_ADDED = "Input has been Added to Database";
    public static final String DATA_EDITED = "Chosen Data has been  Edited in Database";
    public static final String DATA_REMOVED = "Chosen Data has been  Deleted from Database";

    // Reference to the Record Controller
    private FeedbackRecordController controller;

    //Main Layout that combines everything
    private JPanel mainPanel;

    //Panels to add diff components to mainPanel
    private JPanel panelLeft;
    private JPanel panelRight;
    private CardLayout cardLayout;

    // Objects calling other panels classes to initialize display
    UserFeedbackPanelViewer feedbackPanel;
    UserReactionPanelViewer userReactionPanel;
    ReactionPanelViewer reactionPanel;

    private JPanel viewReportPanel;

    // Default Table Models to craete the framework and store data; 
    private DefaultTableModel reportModel;

    // Components for DB APP Buttons
    private JButton feedbackViewButton; // Button links to the feedback table view
    private JButton userReactViewButton; // Button links to the reaction table view
    private JButton reactionViewButton; // Button links to the reaction table view
    private JButton reportViewButton; // Button links to the report table view
    private JButton backButton; // Button returns user to main menu


    public FeedbackRecordViewer(JPanel cardPanel, FeedbackRecordController controller){

        this.controller = controller;

        // Change components of JPanel child class
        setLayout(new BorderLayout()); //Layout
        setPreferredSize(new Dimension(1500, 920));
        setMaximumSize(new Dimension(1500, 920));
        setMinimumSize(new Dimension(1500, 920));


        // Call method to initialize the starting display of FeedbackRecord
        initialization();


        this.add(mainPanel, BorderLayout.CENTER);
        cardPanel.add(this, MainDBViewer.FEEDBACK_LINK);


    }


    // Getter Methods

    public JPanel getDisplayPanel(){

        return panelRight;
    }

    public UserFeedbackPanelViewer getUserFeedbackPanelViewer(){

        return feedbackPanel;

    }

    public UserReactionPanelViewer getUserReactionPanelViewer(){

        return userReactionPanel;

    }

    public ReactionPanelViewer getReactionPanelViewer(){

        return reactionPanel;

    }


    public DefaultTableModel getReportModel(){

        return reportModel;
        
    }


    public JButton getFeedbackViewButton(){

        return feedbackViewButton;
    }


    public JButton getUserReactViewButton(){

        return userReactViewButton;
    }


    public JButton getReactionViewButton(){

        return reactionViewButton;
    }


    public JButton getReportViewButton(){

        return reportViewButton;
    }


    // Action Listener Methods

    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        feedbackViewButton.addActionListener(listener);
        userReactViewButton.addActionListener(listener);
        reportViewButton.addActionListener(listener);
        backButton.addActionListener(listener);
        reactionViewButton.addActionListener(listener);

    }


    // GUI Methods

    //Initializes the base components for the program
    private void initialization(){


        mainPanel = new JPanel(new BorderLayout());

        // Call methods to create the left panel and right panel 
        createLeftPanel();
        createRightPanel();


    }


    // Creates the main buttons for the feedback table
    private void createLeftPanel(){

         // Create JPanel and its different componeents
        panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setMaximumSize(new Dimension(375, 920));
        panelLeft.setPreferredSize(new Dimension(375, 920));
        panelLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK, 8));
        panelLeft.setBackground(new Color(211, 211, 211));


        // Create User Record Button and add components
        feedbackViewButton = new JButton("Feedback Table");
        feedbackViewButton.setPreferredSize(new Dimension(250, 50));
        feedbackViewButton.setMaximumSize(new Dimension(250, 50));
        feedbackViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        feedbackViewButton.setAlignmentX(CENTER_ALIGNMENT);
        feedbackViewButton.setFocusPainted(false);

        // Create User Record Button and add components
        userReactViewButton = new JButton("User Reaction Table");
        userReactViewButton.setPreferredSize(new Dimension(250, 50));
        userReactViewButton.setMaximumSize(new Dimension(250, 50));
        userReactViewButton.setFont(new Font("Arial", Font.PLAIN, 18));
        userReactViewButton.setAlignmentX(CENTER_ALIGNMENT);
        userReactViewButton.setFocusPainted(false);

        // Create View Reaction Button and add components
        reactionViewButton = new JButton("Reaction Table");
        reactionViewButton.setPreferredSize(new Dimension(250, 50));
        reactionViewButton.setMaximumSize(new Dimension(250, 50));
        reactionViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        reactionViewButton.setAlignmentX(CENTER_ALIGNMENT);
        reactionViewButton.setFocusPainted(false);

        // Create User Record Button and add components
        reportViewButton = new JButton("Feedback Report");
        reportViewButton.setPreferredSize(new Dimension(250, 50));
        reportViewButton.setMaximumSize(new Dimension(250, 50));
        reportViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        reportViewButton.setAlignmentX(CENTER_ALIGNMENT);
        reportViewButton.setFocusPainted(false);

        // Create Back Record Button and add components
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(250, 50));
        backButton.setMaximumSize(new Dimension(250, 50));
        backButton.setFont(new Font("Arial", Font.BOLD, 30));
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setFocusPainted(false);

        // Add componenets to panelLeft including Structs to add padding between buttons
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(feedbackViewButton);
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(userReactViewButton);
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(reactionViewButton);
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(reportViewButton);
        panelLeft.add(Box.createVerticalGlue());
        panelLeft.add(backButton);
        panelLeft.add(Box.createVerticalStrut(60));

        mainPanel.add(panelLeft, BorderLayout.WEST);

    }



    // Method creates the base of the right panel
    private void createRightPanel(){


        cardLayout = new CardLayout();
        panelRight = new JPanel(cardLayout);

        // Initialize objects and call methods to create the different panels for transactions and reports

        intializeViewReportPanel(); // Creates report

        feedbackPanel = new UserFeedbackPanelViewer(controller, panelRight);
        feedbackPanel.setActionListener(controller);

        userReactionPanel = new UserReactionPanelViewer(controller, panelRight);
        userReactionPanel.setActionListener(controller);

        reactionPanel = new ReactionPanelViewer(controller, panelRight);
        reactionPanel.setActionListener(controller);

        panelRight.setVisible(false); // Start as non-visible
        mainPanel.add(panelRight, BorderLayout.CENTER);


    }


    // Method creates the show table for report
    private void intializeViewReportPanel(){

        // Create create feedback panel
        viewReportPanel = new JPanel();
        viewReportPanel.setLayout(new BoxLayout(viewReportPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel reportTitle = new JLabel("Report Table");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 50));
        reportTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the table model with the different fields
        reportModel = new DefaultTableModel();
        reportModel.addColumn("User_ID");
        reportModel.addColumn("Full_Name");
        reportModel.addColumn("Tier_Name");
        reportModel.addColumn("Area");
        reportModel.addColumn("Rating");
        reportModel.addColumn("Likes");
        reportModel.addColumn("Dislikes");
        reportModel.addColumn("Review_Month_Year");

        // Create actual visual studio and change its components
        JTable reportTable =  new JTable(reportModel);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 20));
        reportTable.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(reportTable); // Scroll to allow navigation vertically

        // Adjusts the column sizes of the fields in default table model
        TableColumnModel columnModel = reportTable.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(5);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(125);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(5).setPreferredWidth(5);
        columnModel.getColumn(6).setPreferredWidth(5);
        columnModel.getColumn(7).setPreferredWidth(150);

        // Adjusts the content inside of the table so that it aligns to the center of each cell
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < reportTable.getColumnCount(); i++)
            reportTable.getColumnModel().getColumn(i).setCellRenderer(cellCenter);


        // Add componenets to panelLeft including Structs to add padding between buttons
        viewReportPanel.add(Box.createVerticalStrut(40));
        viewReportPanel.add(reportTitle);
        viewReportPanel.add(Box.createVerticalStrut(20));
        viewReportPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        panelRight.add(viewReportPanel, REPORT_VIEW_LINK);


    }







    // Method displays JOptionPane errors depending on passed String
    public void showError(ArrayList<String> errorCodes, ArrayList<String> errorInputs){

        String[] errorsToDisplay = new String[errorInputs.size()];

        for(int i = 0; i < errorInputs.size(); i++){

            switch(errorCodes.get(i)){

                case LENGTH_EXCEEDED:

                    errorsToDisplay[i] = errorInputs.get(i) + " has exceeded maximum input.";
                    break;

                case LENGTH_INSUFFICIENT:
                
                    errorsToDisplay[i] = errorInputs.get(i) + " has not reached minimum input.";
                    break;

                case NO_INPUT:
                
                    errorsToDisplay[i] = errorInputs.get(i) + " has no input.";
                    break;

                case INVALID_INPUT:
                
                    errorsToDisplay[i] = errorInputs.get(i) + " has invalid input.";
                    break;

                case EXISTING_RECORD:
                
                    errorsToDisplay[i] = errorInputs.get(i) + " already exists in database.";
                    break;

                case USER_NOT_LOADED:
                
                    errorsToDisplay[i] = errorInputs.get(i) + " has not been loaded yet";
                    break;

            }

        }

        JOptionPane.showMessageDialog(this, String.join("\n", errorsToDisplay),"Input Error",JOptionPane.WARNING_MESSAGE);

    }


    // Method displays non JOptionPane messages depending on passed String
    public void showMessage(String messageCode){

        String displayMessage = null;

        switch(messageCode){

            case DATA_ADDED:

                displayMessage = "Data has been added to the database. Display has been updated.";
                break;


            case DATA_EDITED:

                displayMessage = "Chosen data has been updated in the database. Display has been updated.";
                break;


            case DATA_REMOVED:

                displayMessage = "Chosen data has been deleted from the database. Display has been updated.";
                break;

        }

        JOptionPane.showMessageDialog(this, displayMessage,"Notice",JOptionPane.PLAIN_MESSAGE);

    }

    // Method shows panels via Card Layout
    public void showPanel(String link){


        // Switch statement determines which panel to open based on link provided in parameter
        switch(link){

            // Cases for UserFeedbackPanelViewer
            case UserFeedbackPanelViewer.FEEDBACK_VIEW_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserFeedbackPanelViewer.FEEDBACK_VIEW_LINK);
                break;

            case UserFeedbackPanelViewer.FEEDBACK_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserFeedbackPanelViewer.FEEDBACK_CREATE_LINK);
                break;

            case UserFeedbackPanelViewer.FEEDBACK_EDIT_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserFeedbackPanelViewer.FEEDBACK_EDIT_LINK);
                break;   

            // Cases for UserReactionPanelViewer
            case UserReactionPanelViewer.USER_REACTION_VIEW_LINK:
        
                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserReactionPanelViewer.USER_REACTION_VIEW_LINK);
                break;

            case UserReactionPanelViewer.USER_REACTION_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserReactionPanelViewer.USER_REACTION_CREATE_LINK);
                break;

            case UserReactionPanelViewer.USER_REACTION_EDIT_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, UserReactionPanelViewer.USER_REACTION_EDIT_LINK);
                break;

            case ReactionPanelViewer.REACTION_VIEW_LINK:
        
                panelRight.setVisible(true);
                cardLayout.show(panelRight, ReactionPanelViewer.REACTION_VIEW_LINK);
                break;


            case ReactionPanelViewer.REACTION_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, ReactionPanelViewer.REACTION_CREATE_LINK);
                break;


            case ReactionPanelViewer.REACTION_EDIT_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, ReactionPanelViewer.REACTION_EDIT_LINK);
                break;

            case REPORT_VIEW_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, REPORT_VIEW_LINK);
                break;



        }

    }


    

}





    

