import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;


// This class serves as the main hub to display the starting screen of the DB App
public class MainDBViewer extends JFrame{


    //Main Layout that combines everything
    private JPanel mainPanel;

    //Panels to add diff components to mainPanel
    private JPanel panelNorth;
    private JPanel panelCenter;
    private JPanel leftButtonPanel; // Stores buttons to the left of panelCenter
    private JPanel rightButtonPanel; // Stores buttons to the right of panelCenter


    //JLabel for title screen
    private JLabel titleLabel;

    //Components for DB APP Buttons
    private JButton UserRecordButton; // Button links to the user record transactions and reports
    private JButton TravelSpotsButton; // Button links to the travel spots transactions and reports
    private JButton UserFeedbackButton; // Button links to the user feedback transactions and reports
    private JButton BookingRecordButton; // Button links to the booking record transactions and reports
    private JButton quitButton; // Button closes the program

    
    // Constructor
    public MainDBViewer(MainDBController controller){

        super("Hidden Gems APP"); //Title
        setLayout(new BorderLayout()); //Layout
        setPreferredSize(new Dimension(1500, 920));
        setMaximumSize(new Dimension(1500, 920));
        setMinimumSize(new Dimension(1500, 920));
        pack(); // sizes the frame to fit preferred sizes of components
        setLocationRelativeTo(null); // centers on screen

        setResizable(false); //Prevent resizing to avoid breaking any components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Allows to close the program when clicking the X in the program window

        initialization(); //Initializes the basic components

        setVisible(true); //Makes the JFrame visible

    }


    // Action Listener Methods

    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        UserRecordButton.addActionListener(listener);
        TravelSpotsButton.addActionListener(listener);
        UserFeedbackButton.addActionListener(listener);
        BookingRecordButton.addActionListener(listener);
        quitButton.addActionListener(listener);

    }

    // GUI Methods

    //Initializes the base components for the program
    private void initialization(){

        // Create the 2 panels that will add components in
        panelCenter = new JPanel(new BorderLayout(25, 25)); // Add Buttons
        panelNorth = new JPanel(new FlowLayout()); // Add Title

        //Create the menu panel and add both the title and names into it.
        mainPanel = new JPanel(new BorderLayout());

        // Create JLabel and add components
        titleLabel = new JLabel("Hidden Gems");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        panelNorth.add(titleLabel); // Add to panelNorth


        // Call method to create the buttons
        createMainButtons();


        // Add the 2 panels to mainPanel
        mainPanel.add(panelNorth, BorderLayout.NORTH);
        mainPanel.add(panelCenter, BorderLayout.CENTER);


        // Add Mainpanel to JFrame in the Center
        this.add(mainPanel, BorderLayout.CENTER);


    }


    // Creates the main 4 button reports
    private void createMainButtons(){


        // Create Panels to store the buttons with borders
        leftButtonPanel = new JPanel();
        leftButtonPanel.setLayout(new BoxLayout(leftButtonPanel, BoxLayout.Y_AXIS));
        leftButtonPanel.setBorder(new EmptyBorder(100, 300, 0, 0));

        rightButtonPanel = new JPanel();
        rightButtonPanel.setLayout(new BoxLayout(rightButtonPanel, BoxLayout.Y_AXIS));
        rightButtonPanel.setBorder(new EmptyBorder(100, 0, 0, 300));

        // Create a wrapper panel to store quit button to avoid it spanning the entire panel
        JPanel quitButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        quitButtonWrapper.setBorder(new EmptyBorder(0, 0, 250, 0));

        // Create User Record Button and add components
        UserRecordButton = new JButton("User Record");
        UserRecordButton.setPreferredSize(new Dimension(300, 100));
        UserRecordButton.setMaximumSize(new Dimension(300, 100));
        UserRecordButton.setFont(new Font("Arial", Font.PLAIN, 30));
        UserRecordButton.setFocusPainted(false);

        // Create User Record Button and add components
        TravelSpotsButton = new JButton("Travel Record");
        TravelSpotsButton.setPreferredSize(new Dimension(300, 100));
        TravelSpotsButton.setMaximumSize(new Dimension(300, 100));
        TravelSpotsButton.setFont(new Font("Arial", Font.PLAIN, 30));
        TravelSpotsButton.setFocusPainted(false);

         // Create User Record Button and add components
        UserFeedbackButton = new JButton("Feedback Record");
        UserFeedbackButton.setPreferredSize(new Dimension(300, 100));
        UserFeedbackButton.setMaximumSize(new Dimension(300, 100));
        UserFeedbackButton.setFont(new Font("Arial", Font.PLAIN, 30));
        UserFeedbackButton.setFocusPainted(false);

        // Create User Record Button and add components
        BookingRecordButton = new JButton("Booking Record");
        BookingRecordButton.setPreferredSize(new Dimension(300, 100));
        BookingRecordButton.setMaximumSize(new Dimension(300, 100));
        BookingRecordButton.setFont(new Font("Arial", Font.PLAIN, 30));
        BookingRecordButton.setFocusPainted(false);

        // Create Quit Record Button and add components
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(300, 100));
        quitButton.setMaximumSize(new Dimension(300, 100));
        quitButton.setFont(new Font("Arial", Font.PLAIN, 30));
        quitButton.setFocusPainted(false);


        // Add Buttons to Left Button Panel
        leftButtonPanel.add(UserRecordButton);
        leftButtonPanel.add(Box.createVerticalStrut(20));
        leftButtonPanel.add(TravelSpotsButton);  

        // Add Buttons to Right Button Panel
        rightButtonPanel.add(UserFeedbackButton);
        rightButtonPanel.add(Box.createVerticalStrut(20));
        rightButtonPanel.add(BookingRecordButton); 

        // Add Quit Button to Wrapper
        quitButtonWrapper.add(quitButton);

        // Add all components to Center Panel
        panelCenter.add(leftButtonPanel, BorderLayout.WEST);
        panelCenter.add(rightButtonPanel, BorderLayout.EAST);
        panelCenter.add(quitButtonWrapper, BorderLayout.SOUTH);

    }


    

}
