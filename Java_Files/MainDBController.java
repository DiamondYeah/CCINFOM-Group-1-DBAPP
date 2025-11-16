import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*; // Import for FileInputStream
import java.util.ArrayList;
import java.util.Properties; // Import for Properties class


/*
 *
 * HOW TO CONNECT TO SQL:
 * below you will see 3 attributes called DB_URL, USER, and PASSWORD. Change the password to your local version assuming
 * URL and USER are the same as mine.
 * NOTE 1: Table should have been made in MYSQL to make this work. So create the database first in MYSQL before connecting
 * NOTE 2: JDBC is needed to connect Java to MYSQL Tutorial for VSCODE -> (https://www.youtube.com/watch?v=MtME-ERufu0)
 * 
 * 
 * HOW TO CONNECT YOUR MVC TO THIS CONTROLLER:
 * This class will be the main controller of the DB Application
 * I have already made sample Controllers for you guys to use, and they are already connected to this main controller.
 * Please implement your MVC seperately of the Main Controller and Viewer as it is only meant to connect the record together.
 * If you wish to use another controller to connect your MVC from my sample,
 * go to the actionPerformed method in the very button, and add the code that will show the viewer of your records in your respective case. 
 * Also uncomment the setVisible before you show your own viewer -> ( appDBViewer.setVisible(false))
 * NOTE: Add a back button on your viewer so that it will go back to the MainDBViewer.
 * 
 * 
    * HOW TO USE CARD VIEWER
    *
    * In your viewer class, create a viewer class with a constructor that accepts **JPanel cardPanel**,
    * Afterwards, extend your class to JFrame and add the class itself as a parameter to the cardPanel JPanel with the appropriate link
    * EX:
    * 
    * public class FeedbackRecordViewer extends JPanel{ <- Extends JPanel
    * 
    *      public FeedbackRecordViewer(JPanel cardPanel){ <- Add JPanel parameter
    *
    *
    *
    *          cardPanel.add(this, MainDBViewer.FEEDBACK_LINK); <- Add Class directly to cardPanel, and use the appropriate link
    *
    *       }
    *
    *  }
    * 
    * Afterwards, in your controller, add the cardPanel when creating the object instance of your viewer.
    * EX:
    * view = new FeedbackRecordViewer(cardPanel);
    * 
    * If both are implemented properly, when you run the program and click the button, it should now open your JPanel
    * 
    * NOTE: Add a back button on your JPanel that will return the Panel back to main via an ActionListener on your own controller
    * 
    * 
    */


// This class connects to the database and connects to the other controllers in the project
public class MainDBController implements ActionListener{

    // NOTE: To make the connection work, please change your password to your own password

    // Attributes define SQL table connection
    private static String DB_URL;
    private static String USER;
    private static String PASSWORD; // Change password to your own local one

    private static Connection conn = null; 
    
    // Currently authenticated user (null if not logged in)
    private User currentUser = null;

    // Stringbuilder to build queries
    StringBuilder query = new StringBuilder();

    // DB App Viewer
    private MainDBViewer appDBViewer;
    private LoginPageViewer loginViewer;

    // Mainly for LoginPageViewer, provides a list of nationalities to select from
    private ArrayList<String> nationalityOptions; 

    // Report Controllers
    public UserRecordController userRecord;
    public TravelRecordController travelRecord;
    public FeedbackRecordController feedbackRecord;
    public BookingRecordController bookingRecord;



    // Constructor
    public MainDBController(){

        loadProperties();

        connectDB();
        
        // Creates the GUI Viewer and references itself
        appDBViewer = new MainDBViewer(this);
        appDBViewer.setActionListener(this);

        // Creates the GUI login and references itself
        loginViewer = new LoginPageViewer(appDBViewer.getCardPanel(), this);
        loginViewer.setActionListener(this);
        appDBViewer.showPanel(LoginPageViewer.LOGIN_LINK); //Force view on login page
        appDBViewer.setVisible(true); //Makes the JFrame visible

    

        // Initialize record controllers and pass conn and MainDBController to them
        userRecord = new UserRecordController(conn, this, appDBViewer.getCardPanel());
        travelRecord = new TravelRecordController(conn, this, appDBViewer.getCardPanel());
        feedbackRecord = new FeedbackRecordController(conn, this, appDBViewer.getCardPanel());
        bookingRecord = new BookingRecordController(conn, this, appDBViewer.getCardPanel());

    }


    // Getter methods

    public MainDBViewer getMainDBViewer(){

        return appDBViewer;
        
    }

    // Special getter method that creates an ArrayList of nationality and returns it. Mainly for login page
    public ArrayList<String> getNationalityOptions(){

        nationalityOptions = new ArrayList<>();

        nationalityOptions.add("Filipino");
        nationalityOptions.add("American");
        nationalityOptions.add("Chinese");
        nationalityOptions.add("Indian");
        nationalityOptions.add("German");
        nationalityOptions.add("French");
        nationalityOptions.add("Russian");
        nationalityOptions.add("Brazilian");
        nationalityOptions.add("Japanese");   

        return nationalityOptions;


    }

    // NEW: Method to load credentials from the external file
    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("db.properties")) {
            
            // Load the properties file
            props.load(input);

            // Assign values from the file to the attributes
            DB_URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            
        } catch (IOException ex) {
            // Handle case where file is missing (crucial for groupmates!)
            System.err.println("Error: Could not find or read db.properties file.");
            System.err.println("Please create this file and add your local credentials.");
            // You might want to exit the program here or set dummy values
            // System.exit(1);
        }

        System.out.println(DB_URL + USER + PASSWORD);
    }


    // Method performs the connection to the SQL Database via DriverManager
    public static void connectDB(){

        // Try-catch block to determine if connection was succesful or not(Display message if unsuccessful)
        try{
            
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.printf("Connection to %s database is successful \n", DB_URL);


        } catch(SQLException e){

            System.out.printf("Error in connecting database occured \n");
            
        }

    }
    
    // Login implementation
    public boolean login() {

        String username = loginViewer.getUserNameLoginField().getText().trim();
        String password = String.valueOf(loginViewer.getPasswordLoginField().getPassword()).trim();


        boolean inputValid = checkInputValidity(username, password); // Checks for input validity
        int userID = findUserID(username); // Calls method to find userID

        // Checks for invalid inputs, and shows error if found
        if(!inputValid){

            loginViewer.showError(LoginPageViewer.EMPTY_INPUT);
            return false;

        }
        else if(userID == 0){

            loginViewer.showError(LoginPageViewer.USER_DOES_NOT_EXIST);   
            return false; 
        }     


        // Checks if ID has been assigned anything, return false if not.
        if(inputValid && userID != 0){

            String query = "SELECT u.User_ID, u.First_Name, u.Last_Name, u.Nationality, u.Points, u.Is_Admin, " +
            "pt.Tier_ID, pt.Tier_Name, pt.Min_Points, pt.Max_Points " +
            "FROM User u " +
            "LEFT JOIN Points_Tier pt ON u.Tier_ID = pt.Tier_ID " +
            "WHERE u.User_ID = ? AND u.Password = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userID);
                pstmt.setString(2, password);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    boolean isAdmin = rs.getBoolean("Is_Admin");
                    PointsTier tier = null;
                    
                    if (!isAdmin && rs.getObject("Tier_ID") != null) {
                        tier = new PointsTier(
                            rs.getInt("Tier_ID"),
                            rs.getString("Tier_Name"),
                            rs.getInt("Min_Points"),
                            rs.getInt("Max_Points")
                        );
                    }
                    
                    currentUser = new User(
                        rs.getInt("User_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Nationality"),
                        rs.getInt("Points"),
                        tier,
                        isAdmin
                    );
                    
                    System.out.println("Login successful: " + currentUser.getFirstName() + " " + currentUser.getLastName() + 
                                    (currentUser.isAdmin() ? " (Admin)" : " (User)"));

                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error during login: " + e.getMessage());
            }
        } 
        
        return false;

    }


    public boolean register(){

        // Will add email depending on Jovere's repsponse
        String firstName = loginViewer.getFirstNameRegisterField().getText().trim();
        String lastName = loginViewer.getLastNameRegisterField().getText().trim();
        //String email = loginViewer.getEmailRegisterField().getText().trim() 
        String nationality = (String) loginViewer.getNationalityRegisterBox().getSelectedItem();
        String password = String.valueOf(loginViewer.getPasswordRegisterField().getPassword()).trim();

        boolean inputValid = checkInputValidity(firstName, lastName, password); // Checks for input validity
        int checkIfUserExists = findUserID(firstName + " " + lastName);

        // Checks for invalid inputs, and shows error if found
        if(!inputValid){

            loginViewer.showError(LoginPageViewer.EMPTY_INPUT);
            return false;

        }
        else if(checkIfUserExists != 0){

            loginViewer.showError(LoginPageViewer.USER_ALREADY_EXISTS);   
            return false; 

        }  


        // Checks if boolean is true
        if(inputValid && checkIfUserExists == 0){

            // Gets the full name of users stored in database
            query.setLength(0);
            query.append("INSERT INTO USER(first_name, last_name, nationality, points, tier_id, password, is_admin) ");
            query.append("VALUES (?, ?, ?, 0, 1, ?, ?)");

            // Try catch tries to check if username given is valid, if so, obtain the userID
            try (PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
                    
                // Set the values in the stmt with the parameters and starting values for each of the ? in the query
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, nationality);
                pstmt.setString(4, password);

                boolean isAdmin = false;

                // If first name equals to admin, set isAdmin boolean to true
                if(firstName.toLowerCase().equals("admin"))
                    isAdmin = true;

                pstmt.setBoolean(5, isAdmin);

                // Insert the data to the dataset
                pstmt.executeUpdate();

                System.out.println("Register successful: " + firstName + " " + lastName + 
                (isAdmin ? " (Admin)" : " (User)"));

                return true;

                    
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        // Display error depending on type found
        if(!inputValid) // Empty input found
            loginViewer.showPanel(LoginPageViewer.EMPTY_INPUT);
        else if(checkIfUserExists != 0) // User does not exist
            loginViewer.showPanel(LoginPageViewer.USER_ALREADY_EXISTS);    

        return false;

    }


    // Checks by scanning same usernames and getting returns a non-zero value if found
    private int findUserID(String username){

        int userID = 0;

        // Gets the full name of users stored in database
        String query = "SELECT u.user_id , CONCAT(u.first_name, ' ' , u.last_name) AS full_name FROM USER u ";

        // Try catch tries to check if username given is valid, if so, obtain the userID
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            // While loop checks database if username is equal to fullname. If it exists, get the user id of its
            while(rs.next())
                if(rs.getString("full_name").equals(username))
                    userID = rs.getInt("user_id");

        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return userID;

    }


    // Input validity for login user
    private boolean checkInputValidity(String completeName, String password){

        // Checks for empty inputs
        if(completeName.isEmpty() || password.isEmpty())
            return false;

        return true;

    }

    // Input validity for register user
    private boolean checkInputValidity(String firstName, String lastName, String password){

         // Checks for empty inputs
        if(firstName.isEmpty() || lastName.isEmpty() || password.isEmpty())
            return false;

        return true;

    }

    
    public boolean verifyPassword(String password) {
        if (currentUser == null) {
            return false;
        }
        
        String query = "SELECT User_ID FROM User WHERE User_ID = ? AND Password = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, currentUser.getUserId());
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error verifying password: " + e.getMessage());
        }
        
        return false;
    }
    
    public void logout() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getFirstName() + " " + currentUser.getLastName() + " logged out");
        }
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }


    // Method provides what the buttons will do when pressed.
    // NOTE: Add your implementation here to connect your MVC inside of your case block
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String command = e.getActionCommand(); //Get String equivalent of the action command


        //Switch statement to check the command and do the corresponding action
        switch(command){

            case "Quit":

                System.exit(0); //Closes program
                break;
            
            case LoginPageViewer.REGISTER_LINK:
            
                loginViewer.clearPageInput("Login"); // Clear login page input
                loginViewer.showPanel(LoginPageViewer.REGISTER_LINK);

                break;

            case "Return to Login":

                appDBViewer.showPanel(LoginPageViewer.LOGIN_LINK);

                break;

            case LoginPageViewer.LOGIN_LINK:

                loginViewer.showPanel(LoginPageViewer.LOGIN_LINK);

                break;

            case LoginPageViewer.LOGIN_PERFORMED:

                // Accesses login() method if given username and password exists in database
                if(login()){

                    loginViewer.clearPageInput("Login"); // Clear login page input
                    appDBViewer.showPanel(MainDBViewer.MAIN_LINK); // Show main page

                }                    

                break;

            case LoginPageViewer.REGISTERED_PERFORMED: //TO BE ADDED
            
                //appDBViewer.showPanel(LoginPageViewer.REGISTER_LINK);

                if(register()){

                    loginViewer.clearPageInput("Register"); // Clear login page input
                    loginViewer.showMessage(LoginPageViewer.REGISTER_SUCCESSFUL); // Show pop-up that message was successful

                }
                
                break;

            case "User Record":

                appDBViewer.showPanel(MainDBViewer.USER_LINK);

                break;

            case "Travel Record":

                // Display checking if button works (Remove once you implemented your MVC)
                System.out.println("Travel Record Button was pressed");

                appDBViewer.showPanel(MainDBViewer.TRAVEL_LINK);

                break;

            case "Feedback Record":

                // Display checking if button works (Remove once you implemented your MVC)
                System.out.println("Feedback Record Button was pressed");

                appDBViewer.showPanel(MainDBViewer.FEEDBACK_LINK);

                break;

            case "Booking Record":

                 // Display checking if button works (Remove once you implemented your MVC)
                System.out.println("Booking Record Button was pressed");

                appDBViewer.showPanel(MainDBViewer.BOOKING_LINK);

                break;





        }

    }
    
}
