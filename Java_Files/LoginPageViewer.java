import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginPageViewer extends JPanel{
    
    // Constants to link the different views in viewer
    public static final String LOGIN_LINK = "Login View";
    public static final String REGISTER_LINK = "Register View";

    // Serves as the links of the different Panels in the Card Layout for login page specifically
    public static final String LOGIN_PERFORMED = "Login";
    public static final String REGISTERED_PERFORMED = "Register";

    // Constant for different error codes
    public static final String USER_DOES_NOT_EXIST = "User Not In Database";


    // Reference to the Main Controller
    private MainDBController controller;

    //Main Layout that combines everything
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Panels for the Login/Register screen
    private JPanel loginPanel;
    private JPanel registerPanel;

    //Components for DB APP Buttons for Login/Register
    private JButton loginButton; // Button links to the user record transactions and reports
    private JButton goToRegisterButton; // Button links to the travel spots transactions and reports
    private JButton returnToLoginButton; // Button links to the user record transactions and reports
    private JButton registerButton; // Button links to the travel spots transactions and reports

    // Input fields for create feedback
    private JTextField userNameLoginField;
    private JPasswordField passwordLoginField;

    private JTextField firstNameRegisterField;
    private JTextField lastNameRegisterField;
    private JTextField emailRegisterField;
    private JComboBox<String> nationalityRegisterBox;
    private JPasswordField passwordRegisterField;



    public LoginPageViewer(JPanel cardPanel, MainDBController controller){

        // Initialize controller from parameter
        this.controller = controller;

        // Change components of JPanel child class
        setLayout(new BorderLayout()); //Layout
        setPreferredSize(new Dimension(1500, 920));
        setMaximumSize(new Dimension(1500, 920));
        setMinimumSize(new Dimension(1500, 920));


        initialization();

        this.add(mainPanel, BorderLayout.CENTER);
        cardPanel.add(this, LOGIN_LINK);


    }


    // Getter Methods

    public JTextField getUserNameLoginField(){

        return userNameLoginField;

    }


    public JPasswordField getPasswordLoginField(){

        return passwordLoginField;

    }


    // Add an action listener to provide button interactivity (CONNECTED TO MainDBController)
    public void setActionListener (ActionListener listener){

        loginButton.addActionListener(listener);
        goToRegisterButton.addActionListener(listener);
        returnToLoginButton.addActionListener(listener);
        registerButton.addActionListener(listener);

    }

        //Initializes the base components for the program
    private void initialization(){

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize components that obtain input
        createInputComponents();

        // Call methods to create the left panel and right panel 
        createLoginPanel();
        createRegisterPanel();


        // Call methods to create the panels for the different transactions and reports

    }


    // Method creates the input components for the main record table and is called whenever an update in database occurs
    private void createInputComponents(){

        // Create Username Login Text Field for Feedback
        userNameLoginField = new JTextField(25);
        userNameLoginField.setMaximumSize(new Dimension(250, 50));
        userNameLoginField.setPreferredSize(new Dimension(250, 50));
        userNameLoginField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Password Login Text Field for Reaction
        passwordLoginField = new JPasswordField(25);
        passwordLoginField.setMaximumSize(new Dimension(250, 50));
        passwordLoginField.setPreferredSize(new Dimension(250, 50));
        passwordLoginField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create First Name Register Text Field for Feedback
        firstNameRegisterField = new JTextField(25);
        firstNameRegisterField.setMaximumSize(new Dimension(250, 50));
        firstNameRegisterField.setPreferredSize(new Dimension(250, 50));
        firstNameRegisterField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Last Name Register Text Field for Feedback
        lastNameRegisterField = new JTextField(25);
        lastNameRegisterField.setMaximumSize(new Dimension(250, 50));
        lastNameRegisterField.setPreferredSize(new Dimension(250, 50));
        lastNameRegisterField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Email Register Text Field for Feedback
        emailRegisterField = new JTextField(25);
        emailRegisterField.setMaximumSize(new Dimension(250, 50));
        emailRegisterField.setPreferredSize(new Dimension(250, 50));
        emailRegisterField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Email Register Text Field for Feedback
        nationalityRegisterBox = new JComboBox<>(controller.getNationalityOptions().toArray(new String[0]));
        nationalityRegisterBox.setMaximumSize(new Dimension(250, 50));
        nationalityRegisterBox.setPreferredSize(new Dimension(250, 50));
        nationalityRegisterBox.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create Password Register Text Field for Reaction
        passwordRegisterField = new JPasswordField(25);
        passwordRegisterField.setMaximumSize(new Dimension(250, 50));
        passwordRegisterField.setPreferredSize(new Dimension(250, 50));
        passwordRegisterField.setFont(new Font("Arial", Font.PLAIN, 20));        

    }


    // Creates the panel view for the login page
    private void createLoginPanel(){

         // Create JPanel and its different componeents
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));


        // Create title page
        JLabel loginTitleLabel = new JLabel("Login");
        loginTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));    


        // Create label, username field and Wrapper to combine them in
        JLabel userNameLabel = new JLabel("Name:");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel userNameWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userNameWrapper.setMaximumSize(new Dimension(800, 70));
        userNameWrapper.setPreferredSize(new Dimension(800, 70));
        userNameWrapper.add(userNameLabel);
        userNameWrapper.add(userNameLoginField);


        // Create label, password field and Wrapper to combine them in
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel passwordWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordWrapper.setMaximumSize(new Dimension(800, 70));
        passwordWrapper.setPreferredSize(new Dimension(800, 70));
        passwordWrapper.add(passwordLabel);
        passwordWrapper.add(passwordLoginField);

        // Create Login Button and add components
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(250, 50));
        loginButton.setMaximumSize(new Dimension(250, 50));
        loginButton.setFont(new Font("Arial", Font.BOLD, 30));
        loginButton.setFocusPainted(false);
        loginButton.setActionCommand(LOGIN_PERFORMED);

        // Create Register Button and add components
        goToRegisterButton = new JButton("Register");
        goToRegisterButton.setPreferredSize(new Dimension(250, 50));
        goToRegisterButton.setMaximumSize(new Dimension(250, 50));
        goToRegisterButton.setFont(new Font("Arial", Font.BOLD, 30));
        goToRegisterButton.setFocusPainted(false);
        goToRegisterButton.setActionCommand(REGISTER_LINK);

        // Wrapper to combine both login and register
        JPanel buttonWrappers = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrappers.setMaximumSize(new Dimension(800, 70));
        buttonWrappers.setPreferredSize(new Dimension(800, 70));
        buttonWrappers.add(loginButton);
        buttonWrappers.add(goToRegisterButton);

        loginTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userNameWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrappers.setAlignmentX(Component.CENTER_ALIGNMENT);
    



        // Add componenets to panelLeft including Structs to add padding between buttons
        loginPanel.add(Box.createVerticalStrut(40));
        loginPanel.add(loginTitleLabel);
        loginPanel.add(Box.createVerticalStrut(40));
        loginPanel.add(userNameWrapper);
        loginPanel.add(Box.createVerticalStrut(40));
        loginPanel.add(passwordWrapper);
        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(buttonWrappers);
        loginPanel.add(Box.createVerticalStrut(100));

        mainPanel.add(loginPanel, LOGIN_LINK);

    }


    // Creates the panel view for the register page
    private void createRegisterPanel(){

        // Create JPanel and its different componeents
        registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));


        // Create title page
        JLabel registerTitleLabel = new JLabel("Register");
        registerTitleLabel.setFont(new Font("Arial", Font.BOLD, 50));    


        // Create label, first name register field and Wrapper to combine them in
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel firstNameWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        firstNameWrapper.setMaximumSize(new Dimension(800, 70));
        firstNameWrapper.setPreferredSize(new Dimension(800, 70));
        firstNameWrapper.add(firstNameLabel);
        firstNameWrapper.add(firstNameRegisterField);

        // Create label, last name register field and Wrapper to combine them in
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel lastNameWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lastNameWrapper.setMaximumSize(new Dimension(800, 70));
        lastNameWrapper.setPreferredSize(new Dimension(800, 70));
        lastNameWrapper.add(lastNameLabel);
        lastNameWrapper.add(lastNameRegisterField);

        // Create label, email register field and Wrapper to combine them in
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel emailWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        emailWrapper.setMaximumSize(new Dimension(800, 70));
        emailWrapper.setPreferredSize(new Dimension(800, 70));
        emailWrapper.add(emailLabel);
        emailWrapper.add(emailRegisterField);

        // Create label, nationality register box and Wrapper to combine them in
        JLabel nationalityLabel = new JLabel("Nationality:");
        nationalityLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel nationalityWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nationalityWrapper.setMaximumSize(new Dimension(800, 70));
        nationalityWrapper.setPreferredSize(new Dimension(800, 70));
        nationalityWrapper.add(nationalityLabel);
        nationalityWrapper.add(nationalityRegisterBox);

        // Create label, password field and Wrapper to combine them in
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 35));
        
        JPanel passwordWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordWrapper.setMaximumSize(new Dimension(800, 70));
        passwordWrapper.setPreferredSize(new Dimension(800, 70));
        passwordWrapper.add(passwordLabel);
        passwordWrapper.add(passwordRegisterField);

        // Create Login Button and add components
        returnToLoginButton = new JButton("Return to Login");
        returnToLoginButton.setPreferredSize(new Dimension(250, 50));
        returnToLoginButton.setMaximumSize(new Dimension(250, 50));
        returnToLoginButton.setFont(new Font("Arial", Font.BOLD, 25));
        returnToLoginButton.setFocusPainted(false);
        returnToLoginButton.setActionCommand(LOGIN_LINK);

        // Create Register Button and add components
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(250, 50));
        registerButton.setMaximumSize(new Dimension(250, 50));
        registerButton.setFont(new Font("Arial", Font.BOLD, 30));
        registerButton.setFocusPainted(false);
        registerButton.setActionCommand(REGISTERED_PERFORMED);

        // Wrapper to combine both login and register
        JPanel buttonWrappers = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrappers.setMaximumSize(new Dimension(800, 70));
        buttonWrappers.setPreferredSize(new Dimension(800, 70));
        buttonWrappers.add(returnToLoginButton);
        buttonWrappers.add(registerButton);

        // Align all components to center
        registerTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstNameWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastNameWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        nationalityWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonWrappers.setAlignmentX(Component.CENTER_ALIGNMENT);
    

        // Add componenets to panelLeft including Structs to add padding between buttons
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(registerTitleLabel);
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(firstNameWrapper);
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(lastNameWrapper);
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(emailWrapper);
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(nationalityWrapper);
        registerPanel.add(Box.createVerticalStrut(40));
        registerPanel.add(passwordWrapper);
        registerPanel.add(Box.createVerticalGlue());
        registerPanel.add(buttonWrappers);
        registerPanel.add(Box.createVerticalStrut(100));

        mainPanel.add(registerPanel, REGISTER_LINK);

    }

    // Helper method that clears all input components in that page depending on given parameter string
    public void clearPageInput(String pageType){

        if(pageType.equals("Login")){

            userNameLoginField.setText("");
            passwordLoginField.setText("");

        }
        else if(pageType.equals("Register")){

            firstNameRegisterField.setText("");
            lastNameRegisterField.setText("");
            emailRegisterField.setText("");
            passwordRegisterField.setText("");

        }

    }


    // Method displays JOptionPane errors depending on passed String
    public void showError(String errorCode){

        String displayError = null;

        switch(errorCode){

            case USER_DOES_NOT_EXIST:

                displayError = "Cannot login as user does not exist with given information.";
                break;


        }

        JOptionPane.showMessageDialog(this, displayError,"Login Error",JOptionPane.WARNING_MESSAGE);

    }


    // Method shows panels via Card Layout
    public void showPanel(String link){


        // Switch statement determines which panel to open based on link provided in parameter
        switch(link){


            case LOGIN_LINK:

                mainPanel.setVisible(true);
                cardLayout.show(mainPanel, LOGIN_LINK);
                break;

            case REGISTER_LINK:

                mainPanel.setVisible(true);
                cardLayout.show(mainPanel, REGISTER_LINK);
                break;

            
        }

    }

}
