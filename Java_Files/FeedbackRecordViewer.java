import java.sql.*;
import java.util.ArrayList;

import javax.security.auth.kerberos.KerberosKey;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

public class FeedbackRecordViewer extends JPanel{

    // Constants to link the different transactions and reports found in buttons
    public static final String FEEDBACK_VIEW_LINK = "Feedback View";
    public static final String USER_REACTION_VIEW_LINK = "Reaction View";
    public static final String REACTION_VIEW_LINK = "User Reaction View";
    public static final String REPORT_VIEW_LINK = "Feedback Report";
    public static final String FEEDBACK_CREATE_LINK = "Feedback Create";
    public static final String USER_REACTION_CREATE_LINK = "User Reaction Create";
    public static final String REACTION_CREATE_LINK = "Reaction Create";

    // Constants for the action commands of the buttons inside of the transactions and reports
    public static final String CREATE_FEEDBACK_USER_LINK = "Create Feedback Info";
    public static final String CREATE_USERREACTION_USER_LINK = "Create User Reaction Info";
    public static final String CREATE_REACTION_USER_LINK = "Create Reaction Info";

    // Constant for different error codes
    public static final String LENGTH_EXCEEDED = "Input Exceeded";
    public static final String LENGTH_INSUFFICIENT = "Input Insufficient";
    public static final String NO_INPUT = "Input Blank";
    public static final String INVALID_INPUT = "Input Invalid";
    public static final String EXISTING_RECORD = "Given Already Exists in Database";


    // Reference to the Record Controller
    private FeedbackRecordController controller;

    //Main Layout that combines everything
    private JPanel mainPanel;

    //Panels to add diff components to mainPanel
    private JPanel panelLeft;
    private JPanel panelRight;
    private CardLayout cardLayout;

    // Panels to store the different displays for the table
    private JPanel createFeedbackPanel;
    private JPanel createUserReactionPanel;
    private JPanel createReactionPanel;
    private JPanel viewFeedbackPanel;
    private JPanel viewUserReactionPanel;
    private JPanel viewReportPanel;
    private JPanel viewReactionsPanel;


    // Default Table Models to craete the framework and store data; 
    private DefaultTableModel feedbackModel;
    private DefaultTableModel reviewModel;
    private DefaultTableModel reactionModel;
    private DefaultTableModel reportModel;

    

    // Components for DB APP Buttons
    private JButton feedbackViewButton; // Button links to the feedback table view
    private JButton userReactViewButton; // Button links to the reaction table view
    private JButton reactionViewButton; // Button links to the reaction table view
    private JButton reportViewButton; // Button links to the report table view
    private JButton createFeedbackButton; // Button links to the feedback creation view
    private JButton createUserReactionButton; // Button links to the user_reaction creation view
    private JButton createReactionButton; // Button links to the reaction creation view
    private JButton backButton; // Button returns user to main menu


    // Components for Buttons for create feedback
    private JButton enterFeedbackButton;
    private JButton enterUserReactionButton;
    private JButton enterReactionButton;


    // Input fields for create feedback
    private JTextField userFeedbackIDField;
    private JTextField userReactionIDField;
    private JTextField locationIDField;
    private JTextField reviewIDField;
    private JTextField reactionNameField;
    private JSlider ratingsSlider;
    private JComboBox<String> reactionTypeBox;


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

    public JTextField getUserFeedbackIDField(){

        return userFeedbackIDField;

    }


    public JTextField getUserReactionIDField(){

        return userReactionIDField;

    }


    public JTextField getLocationIDField(){

        return locationIDField;

    }


    public JSlider getRatingsSlider(){

        return ratingsSlider;

    }


    public JTextField getReviewIdField(){

        return reviewIDField;

    }

    public JTextField getReactionNameField(){

        return reactionNameField;

    }

    public JComboBox<String> getReactionTypeBox(){

        return reactionTypeBox;

    }
    

    public DefaultTableModel getFeedbackModel(){

        return feedbackModel;

    }


    public DefaultTableModel getReviewModel(){

        return reviewModel;

    }


    public DefaultTableModel getReactionModel(){

        return reactionModel;

    }


    public DefaultTableModel getReportModel(){

        return reportModel;
        
    }

    // Action Listener Methods

    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        feedbackViewButton.addActionListener(listener);
        userReactViewButton.addActionListener(listener);
        reportViewButton.addActionListener(listener);
        createFeedbackButton.addActionListener(listener);
        createUserReactionButton.addActionListener(listener);
        backButton.addActionListener(listener);
        reactionViewButton.addActionListener(listener);
        createReactionButton.addActionListener(listener);

        enterFeedbackButton.addActionListener(listener);
        enterUserReactionButton.addActionListener(listener);
        enterReactionButton.addActionListener(listener);

    }


    // GUI Methods

    //Initializes the base components for the program
    private void initialization(){


        mainPanel = new JPanel(new BorderLayout());

        // Initialize components that obtain input
        createInputComponents();

        // Call methods to create the left panel and right panel 
        createLeftPanel();
        createRightPanel();


        // Call methods to create the panels for the different transactions and reports

    }


    // Method creates the input components for the main record table and is called whenever an update in database occurs
    private void createInputComponents(){

        // Create User ID Text Field for Feedback
        userFeedbackIDField = new JTextField(25);
        userFeedbackIDField.setMaximumSize(new Dimension(250, 50));
        userFeedbackIDField.setPreferredSize(new Dimension(250, 50));
        userFeedbackIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create User ID Text Field for Reaction
        userReactionIDField = new JTextField(25);
        userReactionIDField.setMaximumSize(new Dimension(250, 50));
        userReactionIDField.setPreferredSize(new Dimension(250, 50));
        userReactionIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Location ID Text Field
        locationIDField = new JTextField(25);
        locationIDField.setMaximumSize(new Dimension(250, 50));
        locationIDField.setPreferredSize(new Dimension(250, 50));
        locationIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create a Review ID Text Field
        reviewIDField = new JTextField(25);
        reviewIDField.setMaximumSize(new Dimension(250, 50));
        reviewIDField.setPreferredSize(new Dimension(250, 50));
        reviewIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create a Reaction Name Text Field
        reactionNameField = new JTextField(25);
        reactionNameField.setMaximumSize(new Dimension(250, 50));
        reactionNameField.setPreferredSize(new Dimension(250, 50));
        reactionNameField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Ratings Slider
        ratingsSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        ratingsSlider.setMaximumSize(new Dimension(250, 50));
        ratingsSlider.setPreferredSize(new Dimension(250, 50));
        ratingsSlider.setMajorTickSpacing(1);
        ratingsSlider.setSnapToTicks(true);
        ratingsSlider.setPaintTicks(true);
        ratingsSlider.setPaintLabels(true);

        // Create a Reaction Type Dropdown 
        reactionTypeBox = new JComboBox<>(controller.getModelReactionOption().toArray(new String[0]));
        reactionTypeBox.setMaximumSize(new Dimension(250, 50));
        reactionTypeBox.setPreferredSize(new Dimension(250, 50));
        reactionTypeBox.setFont(new Font("Arial", Font.PLAIN, 20));

        



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
        feedbackViewButton = new JButton("View Feedback Table");
        feedbackViewButton.setPreferredSize(new Dimension(250, 50));
        feedbackViewButton.setMaximumSize(new Dimension(250, 50));
        feedbackViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        feedbackViewButton.setAlignmentX(CENTER_ALIGNMENT);
        feedbackViewButton.setFocusPainted(false);

        // Create User Record Button and add components
        userReactViewButton = new JButton("View User Reaction Table");
        userReactViewButton.setPreferredSize(new Dimension(250, 50));
        userReactViewButton.setMaximumSize(new Dimension(250, 50));
        userReactViewButton.setFont(new Font("Arial", Font.PLAIN, 18));
        userReactViewButton.setAlignmentX(CENTER_ALIGNMENT);
        userReactViewButton.setFocusPainted(false);

        // Create View Reaction Button and add components
        reactionViewButton = new JButton("View Reaction Table");
        reactionViewButton.setPreferredSize(new Dimension(250, 50));
        reactionViewButton.setMaximumSize(new Dimension(250, 50));
        reactionViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        reactionViewButton.setAlignmentX(CENTER_ALIGNMENT);
        reactionViewButton.setFocusPainted(false);

        // Create User Record Button and add components
        reportViewButton = new JButton("View Feedback Report");
        reportViewButton.setPreferredSize(new Dimension(250, 50));
        reportViewButton.setMaximumSize(new Dimension(250, 50));
        reportViewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        reportViewButton.setAlignmentX(CENTER_ALIGNMENT);
        reportViewButton.setFocusPainted(false);

         // Create User Record Button and add components
        createFeedbackButton = new JButton("Create User Feedback");
        createFeedbackButton.setPreferredSize(new Dimension(250, 50));
        createFeedbackButton.setMaximumSize(new Dimension(250, 50));
        createFeedbackButton.setFont(new Font("Arial", Font.PLAIN, 20));
        createFeedbackButton.setAlignmentX(CENTER_ALIGNMENT);
        createFeedbackButton.setFocusPainted(false);

        // Create User Record Button and add components
        createUserReactionButton = new JButton("Create User Reaction");
        createUserReactionButton.setPreferredSize(new Dimension(250, 50));
        createUserReactionButton.setMaximumSize(new Dimension(250, 50));
        createUserReactionButton.setFont(new Font("Arial", Font.PLAIN, 20));
        createUserReactionButton.setAlignmentX(CENTER_ALIGNMENT);
        createUserReactionButton.setFocusPainted(false);

        // Create User Record Button and add components
        createReactionButton = new JButton("Create Reaction");
        createReactionButton.setPreferredSize(new Dimension(250, 50));
        createReactionButton.setMaximumSize(new Dimension(250, 50));
        createReactionButton.setFont(new Font("Arial", Font.PLAIN, 20));
        createReactionButton.setAlignmentX(CENTER_ALIGNMENT);
        createReactionButton.setFocusPainted(false);

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
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(createFeedbackButton);
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(createUserReactionButton);
        panelLeft.add(Box.createVerticalStrut(40));
        panelLeft.add(createReactionButton);
        panelLeft.add(Box.createVerticalGlue());
        panelLeft.add(backButton);
        panelLeft.add(Box.createVerticalStrut(60));

        mainPanel.add(panelLeft, BorderLayout.WEST);

    }



    // Method creates the base of the right panel
    private void createRightPanel(){


        cardLayout = new CardLayout();
        panelRight = new JPanel(cardLayout);

        // Call emthods to create the different panels for transactions and reports
        intializeViewFeedbackPanel();
        initializeViewReactionPanel();
        intializeViewUserReactionPanel();
        intializeViewReportPanel();
        intializeCreateFeedbackPanel();
        intializeUserCreateReactionPanel();
        intializeCreateReactionPanel();




        panelRight.setVisible(false); // Start as non-visible
        mainPanel.add(panelRight, BorderLayout.CENTER);


    }



    // Method creates the show table for feedback  where it shows the table for user_feedback
    private void intializeViewFeedbackPanel(){


        // Create create feedback panel
        viewFeedbackPanel = new JPanel();
        viewFeedbackPanel.setLayout(new BoxLayout(viewFeedbackPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel feedbackTitle = new JLabel("User_Feedback Table");
        feedbackTitle.setFont(new Font("Arial", Font.BOLD, 50));
        feedbackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the table model with the different fields
        feedbackModel = new DefaultTableModel();
        feedbackModel.addColumn("Review_ID");
        feedbackModel.addColumn("User_ID");
        feedbackModel.addColumn("Location_ID");
        feedbackModel.addColumn("Rating");
        feedbackModel.addColumn("Reaction_Count");
        feedbackModel.addColumn("Comment_Count");
        feedbackModel.addColumn("Review_Date");

        // Create actual visual studio and change its components
        JTable feedbackTable =  new JTable(feedbackModel);
        feedbackTable.setFont(new Font("Arial", Font.PLAIN, 20));
        feedbackTable.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(feedbackTable); // Scroll to allow navigation vertically

        // Adjusts the column sizes of the fields in default table model
        TableColumnModel columnModel = feedbackTable.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(25);
        columnModel.getColumn(3).setPreferredWidth(25);
        columnModel.getColumn(4).setPreferredWidth(25);
        columnModel.getColumn(5).setPreferredWidth(25);
        columnModel.getColumn(6).setPreferredWidth(250);

        // Adjusts the content inside of the table so that it aligns to the center of each cell
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < feedbackTable.getColumnCount(); i++)
            feedbackTable.getColumnModel().getColumn(i).setCellRenderer(cellCenter);


        // Add componenets to panelLeft including Structs to add padding between buttons
        viewFeedbackPanel.add(Box.createVerticalStrut(40));
        viewFeedbackPanel.add(feedbackTitle);
        viewFeedbackPanel.add(Box.createVerticalStrut(20));
        viewFeedbackPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        panelRight.add(viewFeedbackPanel, FEEDBACK_VIEW_LINK);


    }


    // Method creates the show table for user_reaction where it shows the table for user_reaction
    private void intializeViewUserReactionPanel(){

        // Create create feedback panel
        viewUserReactionPanel = new JPanel();
        viewUserReactionPanel.setLayout(new BoxLayout(viewUserReactionPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel reactionTitle = new JLabel("User_Reaction Table");
        reactionTitle.setFont(new Font("Arial", Font.BOLD, 50));
        reactionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the table model with the different fields
        reviewModel = new DefaultTableModel();
        reviewModel.addColumn("Reaction_ID");
        reviewModel.addColumn("Review_ID");
        reviewModel.addColumn("User_ID");
        reviewModel.addColumn("ReactionType_ID");
        reviewModel.addColumn("Reaction_Date");

        // Create actual visual studio and change its components
        JTable userReactionTable =  new JTable(reviewModel);
        userReactionTable.setFont(new Font("Arial", Font.PLAIN, 20));
        userReactionTable.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(userReactionTable); // Scroll to allow navigation vertically

        // Adjusts the column sizes of the fields in default table model
        TableColumnModel columnModel = userReactionTable.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(25);
        columnModel.getColumn(2).setPreferredWidth(25);
        columnModel.getColumn(3).setPreferredWidth(25);
        columnModel.getColumn(4).setPreferredWidth(250);

        // Adjusts the content inside of the table so that it aligns to the center of each cell
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < userReactionTable.getColumnCount(); i++)
            userReactionTable.getColumnModel().getColumn(i).setCellRenderer(cellCenter);


        // Add componenets to panelLeft including Structs to add padding between buttons
        viewUserReactionPanel.add(Box.createVerticalStrut(40));
        viewUserReactionPanel.add(reactionTitle);
        viewUserReactionPanel.add(Box.createVerticalStrut(20));
        viewUserReactionPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        panelRight.add(viewUserReactionPanel, USER_REACTION_VIEW_LINK);


    }


    // Method creates the show table for reaction where it shows the table for reaction
    private void initializeViewReactionPanel(){

        // Create create feedback panel
        viewReactionsPanel = new JPanel();
        viewReactionsPanel.setLayout(new BoxLayout(viewReactionsPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel reactionTitle = new JLabel("Reaction Table");
        reactionTitle.setFont(new Font("Arial", Font.BOLD, 50));
        reactionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the table model with the different fields
        reactionModel = new DefaultTableModel();
        reactionModel.addColumn("ReactionType_ID");
        reactionModel.addColumn("Reaction_Name");
        
        // Create actual visual studio and change its components
        JTable reactionTable =  new JTable(reactionModel);
        reactionTable.setFont(new Font("Arial", Font.PLAIN, 20));
        reactionTable.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(reactionTable); // Scroll to allow navigation vertically

        scroll.setPreferredSize(new Dimension(400, 600));
        scroll.setMaximumSize(new Dimension(400, 600));
        scroll.setMinimumSize(new Dimension(400, 600));

        // Adjusts the column sizes of the fields in default table model
        TableColumnModel columnModel = reactionTable.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(25);
        columnModel.getColumn(1).setPreferredWidth(150);

        // Adjusts the content inside of the table so that it aligns to the center of each cell
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < reactionTable.getColumnCount(); i++)
            reactionTable.getColumnModel().getColumn(i).setCellRenderer(cellCenter);


        // Add componenets to panelLeft including Structs to add padding between buttons
        viewReactionsPanel.add(Box.createVerticalStrut(40));
        viewReactionsPanel.add(reactionTitle);
        viewReactionsPanel.add(Box.createVerticalStrut(20));
        viewReactionsPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        panelRight.add(viewReactionsPanel, REACTION_VIEW_LINK);


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


    // Method creates the create feedback panel where users can create their own feedback
    private void intializeCreateFeedbackPanel(){


        // Create create feedback panel
        createFeedbackPanel = new JPanel();
        createFeedbackPanel.setLayout(new BoxLayout(createFeedbackPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel feedbackTitle = new JLabel("Create Feedback");
        feedbackTitle.setFont(new Font("Arial", Font.BOLD, 80));


        // Create label, ID Text Field and Wrapper to combine them in
        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel userIDWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userIDWrapper.setMaximumSize(new Dimension(800, 70));
        userIDWrapper.setPreferredSize(new Dimension(800, 70));
        userIDWrapper.add(userIDLabel);
        userIDWrapper.add(userFeedbackIDField);
        

        // Create label, Location Text Field and Wrapper to combine them in
        JLabel locationIDLabel = new JLabel("Location ID:");
        locationIDLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel locationIDWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        locationIDWrapper.setMaximumSize(new Dimension(800, 70));
        locationIDWrapper.setPreferredSize(new Dimension(800, 70));
        locationIDWrapper.add(locationIDLabel);
        locationIDWrapper.add(locationIDField);


        // Create label, Ratings Slider and Wrapper to combine them in
        JLabel ratingsLabel = new JLabel("Rating:");
        ratingsLabel.setFont(new Font("Arial", Font.BOLD, 35));

        JPanel ratingsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ratingsWrapper.setMaximumSize(new Dimension(800, 70));
        ratingsWrapper.setPreferredSize(new Dimension(800, 70));
        ratingsWrapper.add(ratingsLabel);
        ratingsWrapper.add(ratingsSlider);


        // Create Enter Button and add components
        enterFeedbackButton = new JButton("Enter");
        enterFeedbackButton.setPreferredSize(new Dimension(250, 50));
        enterFeedbackButton.setMaximumSize(new Dimension(250, 50));
        enterFeedbackButton.setFont(new Font("Arial", Font.BOLD, 30));
        enterFeedbackButton.setFocusPainted(false);
        enterFeedbackButton.setActionCommand(CREATE_FEEDBACK_USER_LINK);

        // Align everything to center
        feedbackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationIDWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingsWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterFeedbackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add componenets to panelLeft including Structs to add padding between buttons
        createFeedbackPanel.add(Box.createVerticalStrut(40));
        createFeedbackPanel.add(feedbackTitle);
        createFeedbackPanel.add(Box.createVerticalStrut(40));
        createFeedbackPanel.add(userIDWrapper);
        createFeedbackPanel.add(Box.createVerticalStrut(40));
        createFeedbackPanel.add(locationIDWrapper);
        createFeedbackPanel.add(Box.createVerticalStrut(40));
        createFeedbackPanel.add(ratingsWrapper);
        createFeedbackPanel.add(Box.createVerticalStrut(250));
        createFeedbackPanel.add(enterFeedbackButton);
        createFeedbackPanel.add(Box.createVerticalGlue());


        // Add createFeedbackPanel to panelRight
        panelRight.add(createFeedbackPanel, FEEDBACK_CREATE_LINK);


    }


    // Method creates the view reaction panel where users can view the reaction table
    private void intializeUserCreateReactionPanel(){

        
        // Create create feedback panel
        createUserReactionPanel = new JPanel();
        createUserReactionPanel.setLayout(new BoxLayout(createUserReactionPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel createReactionTitle = new JLabel("Create User Reaction");
        createReactionTitle.setFont(new Font("Arial", Font.BOLD, 80));


        // Create label, and Wrapper to combine label and user text field in
        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel userIDWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userIDWrapper.setMaximumSize(new Dimension(800, 70));
        userIDWrapper.setPreferredSize(new Dimension(800, 70));
        userIDWrapper.add(userIDLabel);
        userIDWrapper.add(userReactionIDField);


        // Create label, ID Text Field and Wrapper to combine them in
        JLabel reviewIDLabel = new JLabel("Review ID:");
        reviewIDLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel reviewIDPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reviewIDPanel.setMaximumSize(new Dimension(800, 70));
        reviewIDPanel.setPreferredSize(new Dimension(800, 70));
        reviewIDPanel.add(reviewIDLabel);
        reviewIDPanel.add(reviewIDField);


        // Create label, Ratings Slider and Wrapper to combine them in
        JLabel reactionLabel = new JLabel("Reaction:");
        reactionLabel.setFont(new Font("Arial", Font.BOLD, 35));

        JPanel reactionWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reactionWrapper.setMaximumSize(new Dimension(800, 70));
        reactionWrapper.setPreferredSize(new Dimension(800, 70));
        reactionWrapper.add(reactionLabel);
        reactionWrapper.add(reactionTypeBox);


        // Create Enter Button and add components
        enterUserReactionButton = new JButton("Enter");
        enterUserReactionButton.setPreferredSize(new Dimension(250, 50));
        enterUserReactionButton.setMaximumSize(new Dimension(250, 50));
        enterUserReactionButton.setFont(new Font("Arial", Font.BOLD, 30));
        enterUserReactionButton.setFocusPainted(false);
        enterUserReactionButton.setActionCommand(CREATE_USERREACTION_USER_LINK);

        // Align everything to center
        createReactionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        reviewIDPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reactionWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterUserReactionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add componenets to panelLeft including Structs to add padding between buttons
        createUserReactionPanel.add(Box.createVerticalStrut(40));
        createUserReactionPanel.add(createReactionTitle);
        createUserReactionPanel.add(Box.createVerticalStrut(40));
        createUserReactionPanel.add(userIDWrapper);
        createUserReactionPanel.add(Box.createVerticalStrut(40));
        createUserReactionPanel.add(reviewIDPanel);
        createUserReactionPanel.add(Box.createVerticalStrut(40));
        createUserReactionPanel.add(reactionWrapper);
        createUserReactionPanel.add(Box.createVerticalStrut(250));
        createUserReactionPanel.add(enterUserReactionButton);
        createUserReactionPanel.add(Box.createVerticalGlue());


        // Add createFeedbackPanel to panelRight
        panelRight.add(createUserReactionPanel, USER_REACTION_CREATE_LINK);

    }


    // Method creates the view reaction panel where users can view the reaction table
    private void intializeCreateReactionPanel(){

        // Create create feedback panel
        createReactionPanel = new JPanel();
        createReactionPanel.setLayout(new BoxLayout(createReactionPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel reactionTitle = new JLabel("Create Reaction");
        reactionTitle.setFont(new Font("Arial", Font.BOLD, 80));


        // Create label, and Wrapper to combine label and user text field in
        JLabel reactionNameLabel = new JLabel("Reaction Name:");
        reactionNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel reactionNameWrapper= new JPanel(new FlowLayout(FlowLayout.CENTER));
        reactionNameWrapper.setMaximumSize(new Dimension(800, 70));
        reactionNameWrapper.setPreferredSize(new Dimension(800, 70));
        reactionNameWrapper.add(reactionNameLabel);
        reactionNameWrapper.add(reactionNameField);


        // Create Enter Button and add components
        enterReactionButton = new JButton("Enter");
        enterReactionButton.setPreferredSize(new Dimension(250, 50));
        enterReactionButton.setMaximumSize(new Dimension(250, 50));
        enterReactionButton.setFont(new Font("Arial", Font.BOLD, 30));
        enterReactionButton.setFocusPainted(false);
        enterReactionButton.setActionCommand(CREATE_REACTION_USER_LINK);

        // Align everything to center
        reactionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        reactionNameWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterReactionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add componenets to panelLeft including Structs to add padding between buttons
        createReactionPanel.add(Box.createVerticalStrut(40));
        createReactionPanel.add(reactionTitle);
        createReactionPanel.add(Box.createVerticalStrut(40));
        createReactionPanel.add(reactionNameWrapper);
        createReactionPanel.add(Box.createVerticalStrut(250));
        createReactionPanel.add(enterReactionButton);
        createReactionPanel.add(Box.createVerticalGlue());


        // Add createFeedbackPanel to panelRight
        panelRight.add(createReactionPanel, REACTION_CREATE_LINK);

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

            }

        }

        JOptionPane.showMessageDialog(this, String.join("\n", errorsToDisplay),"Input Error",JOptionPane.WARNING_MESSAGE);

    }


    // Method shows panels via Card Layout
    public void showPanel(String link){


        // Switch statement determines which panel to open based on link provided in parameter
        switch(link){


            case FEEDBACK_VIEW_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, FEEDBACK_VIEW_LINK);
                break;

                
            case USER_REACTION_VIEW_LINK:
        
                panelRight.setVisible(true);
                cardLayout.show(panelRight, USER_REACTION_VIEW_LINK);
                break;

            case REACTION_VIEW_LINK:
        
                panelRight.setVisible(true);
                cardLayout.show(panelRight, REACTION_VIEW_LINK);
                break;

            case REPORT_VIEW_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, REPORT_VIEW_LINK);
                break;

            case FEEDBACK_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, FEEDBACK_CREATE_LINK);
                break;

            case USER_REACTION_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, USER_REACTION_CREATE_LINK);
                break;

            case REACTION_CREATE_LINK:

                panelRight.setVisible(true);
                cardLayout.show(panelRight, REACTION_CREATE_LINK);

            
        }

    }


    

}





    

