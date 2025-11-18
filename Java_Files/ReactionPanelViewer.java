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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ReactionPanelViewer {
    
    // Constants to link the different transactions and reports found in buttons
    public static final String REACTION_VIEW_LINK = "Reaction View";

    // Constants to link the different panels for INSERT, DELETE, and UPDATE
    public static final String REACTION_CREATE_LINK = "Reaction Create";
    public static final String REACTION_EDIT_LINK = "Reaction Edit";

    // Constants for the action commands of the buttons inside of the transactions and reports
    public static final String LOAD_REACTION_USER_LINK = "Load Reaction";
    public static final String CREATE_REACTION_USER_LINK = "Create Reaction Info";
    public static final String EDIT_REACTION_USER_LINK = "Edit Reaction Info";
    public static final String DELETE_REACTION_USER_LINK = "Delete Reaction Info";

    // Reference to the Record Controller
    private FeedbackRecordController controller;

    //Panels to add diff components to mainPanel
    private JPanel mainPanel;

    // Panels to store the different displays for the table
    private JPanel editAndDeleteReactionPanel;
    private JPanel createReactionPanel; 
    private JPanel viewReactionsPanel;

    // Default Table Model to craete the framework and store data; 
    private DefaultTableModel reactionModel;

    // Components for Buttons for create reaction tables
    private JButton createReactionLinkButton; // Button links to the reaction creation view
    private JButton editReactionLinkButton; // Button links to the reaction edit view


    // Components for Buttons for create reaction
    private JButton enterReactionButton;


    // Input fields for create reaction
    private JTextField reactionNameField;

    // Components and Input fields for edit reaction
    private JButton reactionConfirmEditButton;
    private JButton reactionConfirmDeleteButton;
    private JButton loadReactionButton;

    private JTextField currentReactionNameField;
    private JTextField newReactionNameField;

    private JComboBox<String> reactionSelectComboBox;



    public ReactionPanelViewer(FeedbackRecordController controller, JPanel mainPanel){

        this.controller = controller;
        this.mainPanel = mainPanel;

        createInputComponents(); // Create feedback input commands

        // Calls methods to create the diff panels related to feedback table
        initializeViewReactionPanel();
        intializeCreateReactionPanel();
        intializeEditAdminReactionPanel();

    }


    public JComboBox<String> getReactionSelectComboBox(){

        return reactionSelectComboBox;

    }

    
    public JTextField getReactionNameField(){

        return reactionNameField;

    }


    public JTextField getCurrentReactionNameField(){

        return currentReactionNameField;
        
    }


    public JTextField getNewReactionNameField(){

        return newReactionNameField;

    }


    public DefaultTableModel getReactionModel(){

        return reactionModel;

    }


    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        enterReactionButton.addActionListener(listener);

        loadReactionButton.addActionListener(listener);
        createReactionLinkButton.addActionListener(listener);
        editReactionLinkButton.addActionListener(listener);
        reactionConfirmEditButton.addActionListener(listener);
        reactionConfirmDeleteButton.addActionListener(listener);

    }


    // Method creates the input components for the main record table
    private void createInputComponents(){

        // Create a Reaction Name Field 
        reactionNameField = new JTextField(25);
        reactionNameField.setMaximumSize(new Dimension(250, 50));
        reactionNameField.setPreferredSize(new Dimension(250, 50));
        reactionNameField.setFont(new Font("Arial", Font.PLAIN, 20));


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

        // Create panel wrapper to combine all 3 buttons together
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        buttonWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        // Create Enter Button and add components
        createReactionLinkButton = new JButton("Add");
        createReactionLinkButton.setPreferredSize(new Dimension(250, 50));
        createReactionLinkButton.setMaximumSize(new Dimension(250, 50));
        createReactionLinkButton.setFont(new Font("Arial", Font.BOLD, 30));
        createReactionLinkButton.setFocusPainted(false);
        createReactionLinkButton.setActionCommand(REACTION_CREATE_LINK);

        // Create edit Button and add components
        editReactionLinkButton = new JButton("Edit and Delete");
        editReactionLinkButton.setPreferredSize(new Dimension(250, 50));
        editReactionLinkButton.setMaximumSize(new Dimension(250, 50));
        editReactionLinkButton.setFont(new Font("Arial", Font.BOLD, 20));
        editReactionLinkButton.setFocusPainted(false);
        editReactionLinkButton.setActionCommand(REACTION_EDIT_LINK);


        // Add buttons to wrapper
        buttonWrapper.add(createReactionLinkButton);
        buttonWrapper.add(editReactionLinkButton);

        // Create the table model with the different fields and Override a method to avoid making it editable
        reactionModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        // Add columns
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
        viewReactionsPanel.add(buttonWrapper);
        viewReactionsPanel.add(Box.createVerticalStrut(40));
        viewReactionsPanel.add(reactionTitle);
        viewReactionsPanel.add(Box.createVerticalStrut(20));
        viewReactionsPanel.add(scroll);


        // Add createFeedbackPanel to panelRight
        mainPanel.add(viewReactionsPanel, REACTION_VIEW_LINK);


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
        mainPanel.add(createReactionPanel, REACTION_CREATE_LINK);

    }


    // Method creates the edit reaction panel where admins can edit reaction
    private void intializeEditAdminReactionPanel(){

        editAndDeleteReactionPanel = new JPanel();
        editAndDeleteReactionPanel.setLayout(new BoxLayout(editAndDeleteReactionPanel, BoxLayout.Y_AXIS));


        JLabel editUserReactionTitle = new JLabel("Edit Reaction Table");
        editUserReactionTitle.setFont(new Font("Arial", Font.BOLD, 50));


        reactionSelectComboBox = new JComboBox<>(controller.getFeedbackRecord().getReactionWithIDOptions().toArray(new String[0]));
        reactionSelectComboBox.setMaximumSize(new Dimension(250, 50));
        reactionSelectComboBox.setPreferredSize(new Dimension(250, 50));
        reactionSelectComboBox.setFont(new Font("Arial", Font.PLAIN, 20));

        loadReactionButton = new JButton("Load");
        loadReactionButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loadReactionButton.setMaximumSize(new Dimension(200, 50));
        loadReactionButton.setPreferredSize(new Dimension(200, 50));
        loadReactionButton.setActionCommand(LOAD_REACTION_USER_LINK);

        JPanel selectWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        selectWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        selectWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        selectWrapper.add(reactionSelectComboBox);
        selectWrapper.add(loadReactionButton);



        JLabel currentReactionLabel = new JLabel("Current Label:");
        currentReactionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        currentReactionNameField = new JTextField(25);
        currentReactionNameField.setMaximumSize(new Dimension(250, 50));
        currentReactionNameField.setPreferredSize(new Dimension(250, 50));
        currentReactionNameField.setFont(new Font("Arial", Font.PLAIN, 20));
        currentReactionNameField.setEditable(false);

        JPanel currentReactionWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        currentReactionWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        currentReactionWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        currentReactionWrapper.add(currentReactionLabel);
        currentReactionWrapper.add(currentReactionNameField);



        JLabel newReactionLabel = new JLabel("New Label:");
        newReactionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        newReactionNameField = new JTextField(25);
        newReactionNameField.setMaximumSize(new Dimension(250, 50));
        newReactionNameField.setPreferredSize(new Dimension(250, 50));
        newReactionNameField.setFont(new Font("Arial", Font.PLAIN, 20));
        newReactionNameField.setEnabled(false);

        JPanel newReactionWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        newReactionWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        newReactionWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        newReactionWrapper.add(newReactionLabel);
        newReactionWrapper.add(newReactionNameField);



        // Creates the edit and deletee buttons for the panel and use a wrapper to combine them together
        reactionConfirmEditButton = new JButton("Edit");
        reactionConfirmEditButton.setPreferredSize(new Dimension(250, 50));
        reactionConfirmEditButton.setMaximumSize(new Dimension(250, 50));
        reactionConfirmEditButton.setFont(new Font("Arial", Font.BOLD, 20));
        reactionConfirmEditButton.setFocusPainted(false);
        reactionConfirmEditButton.setActionCommand(EDIT_REACTION_USER_LINK);

        reactionConfirmDeleteButton = new JButton("Delete");
        reactionConfirmDeleteButton.setPreferredSize(new Dimension(250, 50));
        reactionConfirmDeleteButton.setMaximumSize(new Dimension(250, 50));
        reactionConfirmDeleteButton.setFont(new Font("Arial", Font.BOLD, 20));
        reactionConfirmDeleteButton.setFocusPainted(false);
        reactionConfirmDeleteButton.setActionCommand(DELETE_REACTION_USER_LINK);

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20 , 20));
        buttonWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        buttonWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        buttonWrapper.add(reactionConfirmEditButton);
        buttonWrapper.add(reactionConfirmDeleteButton);

        // Center all components
        editUserReactionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentReactionWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        newReactionWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add all components to panel with padding
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(40));
        editAndDeleteReactionPanel.add(editUserReactionTitle);
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(15));
        editAndDeleteReactionPanel.add(selectWrapper);
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(15));
        editAndDeleteReactionPanel.add(currentReactionWrapper);
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(15));
        editAndDeleteReactionPanel.add(newReactionWrapper);
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(15));
        editAndDeleteReactionPanel.add(Box.createVerticalGlue());
        editAndDeleteReactionPanel.add(buttonWrapper);
        editAndDeleteReactionPanel.add(Box.createVerticalStrut(40));


        mainPanel.add(editAndDeleteReactionPanel, REACTION_EDIT_LINK);

        
    }

}
