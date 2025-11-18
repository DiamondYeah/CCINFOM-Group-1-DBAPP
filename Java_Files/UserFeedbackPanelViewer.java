import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class UserFeedbackPanelViewer{
    
   // Constants to link the different transactions and reports found in buttons
    public static final String FEEDBACK_VIEW_LINK = "Feedback View";

    // Constants to link the different panels for INSERT, DELETE, and UPDATE
    public static final String FEEDBACK_CREATE_LINK = "Feedback Create";
    public static final String FEEDBACK_EDIT_LINK = "Feedback Edit";

    // Constants for the action commands of the buttons inside of the transactions and reports
    public static final String LOAD_FEEDBACK_EDIT_LINK = "Load User Feedback";
    public static final String CREATE_FEEDBACK_USER_LINK = "Create Feedback Info";
    public static final String EDIT_FEEDBACK_USER_LINK = "Edit Feedback Info";
    public static final String DELETE_FEEDBACK_USER_LINK = "Delete Feedback Info";

    // Constant for different error codes
    public static final String EXISTING_RECORD = "Given Already Exists in Database";


    // Reference to the Record Controller
    private FeedbackRecordController controller;

    //Panels to add diff components to mainPanel
    private JPanel mainPanel;

    // Panels to store the different displays for the table
    private JPanel createFeedbackPanel;
    private JPanel editAndDeleteFeedbackPanel;
    private JPanel viewFeedbackPanel;

    // Default Table Models to craete the framework and store data; 
    private DefaultTableModel feedbackModel;

    // Components for DB APP Buttons
    private JButton createFeedbackLinkButton; // Button links to the feedback creation view
    private JButton editAndDeleteFeedbackLinkButton; // Button links to the feedback edit and delete view


    // Components for Buttons for create feedback
    private JButton enterFeedbackButton;

    // Input fields for create feedback
    private JTextField userFeedbackIDField;
    private JTextField locationIDField;
    private JSlider ratingsSlider;

    // Components and Input fields for edit feedback
    private JButton feedbackConfirmEditButton;
    private JButton feedbackConfirmDeleteButton;
    private JButton loadFeedbackButton;

    private SpinnerNumberModel editFeedbackReactionModel;
    private JSpinner editFeedbackReactionSpinner;
    
    private SpinnerDateModel editFeedbackDateModel;
    private JSpinner editFeedbackDateSpinner;

    private SpinnerNumberModel editFeedbackCommentModel;
    private JSpinner editFeedbackCommentSpinner;

    private JSlider editFeedbackRatingsSlider;

    private JComboBox<String> feedbackSelectComboBox;
    private JComboBox<String> editFeedbackUserIDBox;
    private JComboBox<String> editFeedbackLocationIDBox;




    public UserFeedbackPanelViewer(FeedbackRecordController controller, JPanel mainPanel){

        this.controller = controller;
        this.mainPanel = mainPanel;

        createInputComponents(); // Create feedback input commands

        // Calls methods to create the diff panels related to feedback table
        intializeViewFeedbackPanel();
        intializeCreateFeedbackPanel();
        intializeEditAdminFeedbackPanel();

    }

    // Getter methods;
    public JTextField getUserFeedbackIDField(){

        return userFeedbackIDField;

    }


    public JTextField getLocationIDField(){

        return locationIDField;

    }


    public JSlider getRatingsSlider(){

        return ratingsSlider;

    }


    public DefaultTableModel getFeedbackModel(){

        return feedbackModel;

    }


    public JComboBox<String> getFeedbackSelectComboBox(){

        return feedbackSelectComboBox;

    }


    public JComboBox<String> getEditFeedbackUserIDBox(){

        return editFeedbackUserIDBox;

    }

    public JComboBox<String> getEditFeedbackLocationIDBox(){

        return editFeedbackLocationIDBox;

    }

    public JSlider getEditFeedbackRatingsSlider(){

        return editFeedbackRatingsSlider;

    }

    public JSpinner getEditFeedbackReactionSpinner(){

        return editFeedbackReactionSpinner;

    }

    public JSpinner getEditFeedbackCommentSpinner(){

        return editFeedbackCommentSpinner;

    }

    public JSpinner getEditFeedbackDateSpinner(){

        return editFeedbackDateSpinner;

    }


    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        createFeedbackLinkButton.addActionListener(listener);
        editAndDeleteFeedbackLinkButton.addActionListener(listener);
        enterFeedbackButton.addActionListener(listener);

        loadFeedbackButton.addActionListener(listener);
        feedbackConfirmEditButton.addActionListener(listener);
        feedbackConfirmDeleteButton.addActionListener(listener);



    }


    // Method creates the input components for the main record table and is called whenever an update in database occurs
    private void createInputComponents(){

        // Create User ID Text Field for Feedback
        userFeedbackIDField = new JTextField(25);
        userFeedbackIDField.setMaximumSize(new Dimension(250, 50));
        userFeedbackIDField.setPreferredSize(new Dimension(250, 50));
        userFeedbackIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Location ID Text Field
        locationIDField = new JTextField(25);
        locationIDField.setMaximumSize(new Dimension(250, 50));
        locationIDField.setPreferredSize(new Dimension(250, 50));
        locationIDField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Ratings Slider
        ratingsSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        ratingsSlider.setMaximumSize(new Dimension(250, 50));
        ratingsSlider.setPreferredSize(new Dimension(250, 50));
        ratingsSlider.setMajorTickSpacing(1);
        ratingsSlider.setSnapToTicks(true);
        ratingsSlider.setPaintTicks(true);
        ratingsSlider.setPaintLabels(true);

    }


    // Method creates the show table for feedback  where it shows the table for user_feedback
    private void intializeViewFeedbackPanel(){


        // Create create feedback panel
        viewFeedbackPanel = new JPanel();
        viewFeedbackPanel.setLayout(new BoxLayout(viewFeedbackPanel, BoxLayout.Y_AXIS));

        // Create title label
        JLabel feedbackTitle = new JLabel("User_Feedback Table");
        feedbackTitle.setFont(new Font("Arial", Font.BOLD, 40));
        feedbackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create panel wrapper to combine all 3 buttons together
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        buttonWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        // Create Enter Button and add components
        createFeedbackLinkButton = new JButton("Add");
        createFeedbackLinkButton.setPreferredSize(new Dimension(250, 50));
        createFeedbackLinkButton.setMaximumSize(new Dimension(250, 50));
        createFeedbackLinkButton.setFont(new Font("Arial", Font.BOLD, 30));
        createFeedbackLinkButton.setFocusPainted(false);
        createFeedbackLinkButton.setActionCommand(FEEDBACK_CREATE_LINK);

        // Create Enter Button and add components
        editAndDeleteFeedbackLinkButton = new JButton("Edit and Delete");
        editAndDeleteFeedbackLinkButton.setPreferredSize(new Dimension(250, 50));
        editAndDeleteFeedbackLinkButton.setMaximumSize(new Dimension(250, 50));
        editAndDeleteFeedbackLinkButton.setFont(new Font("Arial", Font.BOLD, 20));
        editAndDeleteFeedbackLinkButton.setFocusPainted(false);
        editAndDeleteFeedbackLinkButton.setActionCommand(FEEDBACK_EDIT_LINK);

        // Add all 3 buttons to wrapper
        buttonWrapper.add(createFeedbackLinkButton);
        buttonWrapper.add(editAndDeleteFeedbackLinkButton);

        // Create the table model with the different fields and Override a method to avoid making it editable
        feedbackModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        // Add columns
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
        viewFeedbackPanel.add(buttonWrapper);
        viewFeedbackPanel.add(Box.createVerticalStrut(40));
        viewFeedbackPanel.add(feedbackTitle);
        viewFeedbackPanel.add(Box.createVerticalStrut(20));
        viewFeedbackPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        mainPanel.add(viewFeedbackPanel, FEEDBACK_VIEW_LINK);


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
        mainPanel.add(createFeedbackPanel, FEEDBACK_CREATE_LINK);


    }


    // Method creates the edit feedback panel where admins can edit user_feedback
    private void intializeEditAdminFeedbackPanel(){

        editAndDeleteFeedbackPanel = new JPanel();
        editAndDeleteFeedbackPanel.setLayout(new BoxLayout(editAndDeleteFeedbackPanel, BoxLayout.Y_AXIS));

        JLabel editFeedbackTitle = new JLabel("Edit Feedback Table");
        editFeedbackTitle.setFont(new Font("Arial", Font.BOLD, 50));



        feedbackSelectComboBox = new JComboBox<>(controller.getFeedbackRecord().getUserFeedbackOptions().toArray(new String[0]));
        feedbackSelectComboBox.setMaximumSize(new Dimension(250, 50));
        feedbackSelectComboBox.setPreferredSize(new Dimension(250, 50));
        feedbackSelectComboBox.setFont(new Font("Arial", Font.PLAIN, 20));

        loadFeedbackButton = new JButton("Load");
        loadFeedbackButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loadFeedbackButton.setMaximumSize(new Dimension(200, 50));
        loadFeedbackButton.setPreferredSize(new Dimension(200, 50));
        loadFeedbackButton.setActionCommand(LOAD_FEEDBACK_EDIT_LINK);

        JPanel selectWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        selectWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        selectWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        selectWrapper.add(feedbackSelectComboBox);
        selectWrapper.add(loadFeedbackButton);



        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackUserIDBox = new JComboBox<>(controller.getFeedbackRecord().getFeedbackUserIDOptions().toArray(new String[0]));
        editFeedbackUserIDBox.setMaximumSize(new Dimension(250, 50));
        editFeedbackUserIDBox.setPreferredSize(new Dimension(250, 50));
        editFeedbackUserIDBox.setFont(new Font("Arial", Font.PLAIN, 20));
        editFeedbackUserIDBox.setEnabled(false);

        JPanel userIDWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        userIDWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        userIDWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        userIDWrapper.add(userIDLabel);
        userIDWrapper.add(editFeedbackUserIDBox);



        JLabel locationIDLabel = new JLabel("Location ID:");
        locationIDLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackLocationIDBox = new JComboBox<>(controller.getFeedbackRecord().getFeedbackLocationIDOptions().toArray(new String[0]));
        editFeedbackLocationIDBox.setMaximumSize(new Dimension(250, 50));
        editFeedbackLocationIDBox.setPreferredSize(new Dimension(250, 50));
        editFeedbackLocationIDBox.setFont(new Font("Arial", Font.PLAIN, 20));
        editFeedbackLocationIDBox.setEnabled(false);

        JPanel locationIDWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        locationIDWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        locationIDWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        locationIDWrapper.add(locationIDLabel);
        locationIDWrapper.add(editFeedbackLocationIDBox);



        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackRatingsSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        editFeedbackRatingsSlider.setMaximumSize(new Dimension(250, 50));
        editFeedbackRatingsSlider.setPreferredSize(new Dimension(250, 50));
        editFeedbackRatingsSlider.setPaintTicks(true);
        editFeedbackRatingsSlider.setPaintLabels(true);
        editFeedbackRatingsSlider.setEnabled(false);

        JPanel ratingWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        ratingWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        ratingWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        ratingWrapper.add(ratingLabel);
        ratingWrapper.add(editFeedbackRatingsSlider);



        JLabel reactionCountLabel = new JLabel("Reaction Count:");
        reactionCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackReactionModel = new SpinnerNumberModel(0, 0, 999 ,1);
        editFeedbackReactionSpinner = new JSpinner(editFeedbackReactionModel);
        editFeedbackReactionSpinner.setMaximumSize(new Dimension(250, 50));
        editFeedbackReactionSpinner.setPreferredSize(new Dimension(250, 50));
        editFeedbackReactionSpinner.setFont(new Font("Arial", Font.BOLD, 20));
        editFeedbackReactionSpinner.setEnabled(false); // Disable at start
        editFeedbackReactionSpinner.setEnabled(false);

        JPanel reactionCountWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        reactionCountWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        reactionCountWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        reactionCountWrapper.add(reactionCountLabel);
        reactionCountWrapper.add(editFeedbackReactionSpinner);



        JLabel commentCountLabel = new JLabel("Comment Count:");
        commentCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackCommentModel = new SpinnerNumberModel(0, 0, 999 ,1);
        editFeedbackCommentSpinner = new JSpinner(editFeedbackCommentModel);
        editFeedbackCommentSpinner.setMaximumSize(new Dimension(250, 50));
        editFeedbackCommentSpinner.setPreferredSize(new Dimension(250, 50));
        editFeedbackCommentSpinner.setFont(new Font("Arial", Font.BOLD, 20));
        editFeedbackCommentSpinner.setEnabled(false); 

        JPanel commentCountWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        commentCountWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        commentCountWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        commentCountWrapper.add(commentCountLabel);
        commentCountWrapper.add(editFeedbackCommentSpinner);


        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        editFeedbackDateModel = new SpinnerDateModel(); // Create a date model to be used
        editFeedbackDateSpinner = new JSpinner(editFeedbackDateModel);
        // Create a date editor on the spinner
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(editFeedbackDateSpinner, "dd-MMM-yyyy HH:mm:ss");
        editFeedbackDateSpinner.setEditor(dateEditor);
        editFeedbackDateSpinner.setMaximumSize(new Dimension(250, 50));
        editFeedbackDateSpinner.setPreferredSize(new Dimension(250, 50));
        editFeedbackDateSpinner.setFont(new Font("Arial", Font.BOLD, 20));
        editFeedbackDateSpinner.setEnabled(false); // Disable at start

        JPanel dateWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        dateWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        dateWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        dateWrapper.add(dateLabel);
        dateWrapper.add(editFeedbackDateSpinner);

        // Creates the edit and deletee buttons for the panel and use a wrapper to combine them together
        feedbackConfirmEditButton = new JButton("Edit");
        feedbackConfirmEditButton.setPreferredSize(new Dimension(250, 50));
        feedbackConfirmEditButton.setMaximumSize(new Dimension(250, 50));
        feedbackConfirmEditButton.setFont(new Font("Arial", Font.BOLD, 20));
        feedbackConfirmEditButton.setFocusPainted(false);
        feedbackConfirmEditButton.setActionCommand(EDIT_FEEDBACK_USER_LINK);

        feedbackConfirmDeleteButton = new JButton("Delete");
        feedbackConfirmDeleteButton.setPreferredSize(new Dimension(250, 50));
        feedbackConfirmDeleteButton.setMaximumSize(new Dimension(250, 50));
        feedbackConfirmDeleteButton.setFont(new Font("Arial", Font.BOLD, 20));
        feedbackConfirmDeleteButton.setFocusPainted(false);
        feedbackConfirmDeleteButton.setActionCommand(DELETE_FEEDBACK_USER_LINK);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        buttonWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        buttonWrapper.add(feedbackConfirmEditButton);
        buttonWrapper.add(feedbackConfirmDeleteButton);

        editFeedbackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationIDWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        reactionCountWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        commentCountWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(40));
        editAndDeleteFeedbackPanel.add(editFeedbackTitle);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(selectWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(userIDWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(locationIDWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(ratingWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(reactionCountWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(commentCountWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(15));
        editAndDeleteFeedbackPanel.add(dateWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalGlue());
        editAndDeleteFeedbackPanel.add(buttonWrapper);
        editAndDeleteFeedbackPanel.add(Box.createVerticalStrut(40));


        mainPanel.add(editAndDeleteFeedbackPanel, FEEDBACK_EDIT_LINK);



    }

    
}
