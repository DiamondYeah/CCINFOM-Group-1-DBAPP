
public class Driver {
    

    /*
     * Main Driver of Program
     * NOTE: TO KNOW HOW TO CONNECT YOUR MVC TO THE MAIN CONTROLLER, GO TO MainDBController
     */
    public static void main(String[] args){

        MainDBController mainController = new MainDBController();

        //Remove test cases after implementing login feature

        //Test Case 1: Admin User Login
        if (mainController.login(1, "admin1234")) {
            System.out.println("Admin User (ID 1) logged in successfully.");
        } else {
            System.out.println("Admin User (ID 1) login failed.");
        }
        

        // Test Case 2: Regular User
        // if (mainController.login(2, "user1234")) {
        //     System.out.println("Regular User (ID 2) logged in successfully.");
        // } else {
        //     System.out.println("Regular User (ID 2) login failed.");
        // }

    }


}
