import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;  
import java.text.SimpleDateFormat;

public class UserRecordController implements ActionListener {
    
    private Connection conn;
    private MainDBController mainController;
    private UserRecordViewer view;
    private UserRecordModel model;
    
    private User currentUser;
    private boolean isAdminMode;
    
    public UserRecordController(Connection conn, MainDBController mainController, JPanel cardPanel) {
        this.conn = conn;
        this.mainController = mainController;
        this.model = new UserRecordModel(conn);
        this.view = new UserRecordViewer(cardPanel);
        
        view.getViewUserButton().addActionListener(this);
        view.getViewRecommendedButton().addActionListener(this);
        view.getEditInfoButton().addActionListener(this);
        view.getBackButton().addActionListener(this);
        view.getRefreshTableButton().addActionListener(this);
        view.getRefreshRecommendationsButton().addActionListener(this);
        // Removed getFilterRecommendationsButton - no longer exists in view
        
        view.getUpdateUserButton().addActionListener(this);
        view.getLoadUserButton().addActionListener(this);
        view.getAddEmailButton().addActionListener(this);
        view.getRemoveEmailButton().addActionListener(this);
        view.getAddPhoneButton().addActionListener(this);
        view.getRemovePhoneButton().addActionListener(this);
        view.getDeleteAccountButton().addActionListener(this);
        
        view.getUpdateUserButtonUser().addActionListener(this);
        view.getAddEmailButtonUser().addActionListener(this);
        view.getRemoveEmailButtonUser().addActionListener(this);
        view.getAddPhoneButtonUser().addActionListener(this);
        view.getRemovePhoneButtonUser().addActionListener(this);
        view.getDeleteAccountButtonUser().addActionListener(this);
        
        view.getRefreshTiersButton().addActionListener(this);
        view.getRefreshEmailsButton().addActionListener(this);
        view.getRefreshPhonesButton().addActionListener(this);
        view.getInsertUserButton().addActionListener(this);
        view.getDeleteUserButton().addActionListener(this);
        view.getInsertTierButton().addActionListener(this);
        view.getDeleteTierButton().addActionListener(this);
        view.getInsertEmailButton().addActionListener(this);
        view.getDeleteEmailButton().addActionListener(this);
        view.getInsertPhoneButton().addActionListener(this);
        view.getDeletePhoneButton().addActionListener(this);
    }

    // Getter method
    public UserRecordModel getUserRecordModel(){
        return model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "ViewUserTable":
                showUserTable();
                break;
                
            case "ViewRecommendations":
                showRecommendations();
                break;
                
            case "EditUserInfo":
                showEditPanel();
                break;
                
            case "BackToMain":
                backToMain();
                break;
                
            case "RefreshUserTable":
                refreshUserTable();
                break;
                
            case "RefreshRecommendations":
                refreshRecommendations();
                break;
                
            case "LoadUser":
                loadUser();
                break;
                
            case "UpdateUser":
                updateUser();
                break;
                
            case "AddEmail":
                addEmail();
                break;
                
            case "RemoveEmail":
                removeEmail();
                break;
                
            case "AddPhone":
                addPhone();
                break;
                
            case "RemovePhone":
                removePhone();
                break;
                
            case "DeleteAccount":
                deleteAccount();
                break;
                
            case "RefreshTiers":
                refreshTiersTable();
                break;
                
            case "RefreshEmails":
                refreshEmailsTable();
                break;
                
            case "RefreshPhones":
                refreshPhonesTable();
                break;
                
            case "InsertUser":
                insertUser();
                break;
                
            case "DeleteUser":
                deleteUser();
                break;
                
            case "InsertTier":
                insertTier();
                break;
                
            case "DeleteTier":
                deleteTier();
                break;
                
            case "InsertEmail":
                insertEmail();
                break;
                
            case "DeleteEmail":
                deleteEmail();
                break;
                
            case "InsertPhone":
                insertPhone();
                break;
                
            case "DeletePhone":
                deletePhone();
                break;
        }
    }
    
    private void showUserTable() {
        if (!mainController.isLoggedIn()) {
            JOptionPane.showMessageDialog(view, "Please login first.", 
                "Authentication Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!mainController.isCurrentUserAdmin()) {
            JOptionPane.showMessageDialog(view, "This feature is only available to administrators.", 
                "Admin Access Required", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to view user table:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            
            if (mainController.verifyPassword(password)) {
                view.showCard(view.getTableCard());
                refreshUserTable();
                refreshTiersTable();
                refreshEmailsTable();
                refreshPhonesTable();
            } else {
                JOptionPane.showMessageDialog(view, "Incorrect password.", 
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshUserTable() {
        List<User> users = model.getAllUsers();
        view.updateUserTable(users);
    }
    
    private void refreshTiersTable() {
        List<PointsTier> tiers = model.getAllTiers();
        view.updateTiersTable(tiers);
    }
    
    private void refreshEmailsTable() {
        List<UserEmail> emails = model.getAllEmails();
        view.updateEmailsTable(emails);
    }
    
    private void refreshPhonesTable() {
        List<UserPhone> phones = model.getAllPhones();
        view.updatePhonesTable(phones);
    }
    
    private void showRecommendations() {
        view.showCard(view.getRecommendationsCard());
        refreshRecommendations();
    }
    
    private void refreshRecommendations() {
        List<Object[]> recommendations = model.getRecommendationsByTierAndDate();
        view.updateRecommendationsTable(recommendations);
        
        if (recommendations.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "No recommendations found.", 
                "No Data", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showEditPanel() {
        if (!mainController.isLoggedIn()) {
            JOptionPane.showMessageDialog(view, "Please login first.", 
                "Authentication Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (mainController.isCurrentUserAdmin()) {
            isAdminMode = true;
            JPasswordField passwordField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to edit information:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
            if (option == JOptionPane.OK_OPTION) {
                String password = new String(passwordField.getPassword());
            
                if (mainController.verifyPassword(password)) {
                    view.showCard(view.getEditCardAdmin());
                    clearEditFields();
                } else {
                JOptionPane.showMessageDialog(view, "Incorrect password.", 
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            isAdminMode = false;
            view.showCard(view.getEditCardUser());
            clearEditFields();
            autoLoadCurrentUser();
        }
    }
    
    private void clearEditFields() {
        if (isAdminMode) {
            view.getUserIdField().setText("");
            view.getFirstNameField().setText("");
            view.getLastNameField().setText("");
            view.getNationalityField().setText("");
            view.getPointsField().setText("");
            view.getTierLabel().setText("N/A");
            view.getPasswordField().setText("");
            view.getEmailDisplayArea().setText("");
            view.getPhoneDisplayArea().setText("");
            view.getEmailField().setText("");
            view.getPhoneField().setText("");
            view.getEmailIdToRemoveField().setText("");
            view.getPhoneIdToRemoveField().setText("");
        } else {
            view.getFirstNameFieldUser().setText("");
            view.getLastNameFieldUser().setText("");
            view.getNationalityFieldUser().setText("");
            view.getPointsFieldUser().setText("");
            view.getTierLabelUser().setText("N/A");
            view.getPasswordFieldUser().setText("");
            view.getEmailDisplayAreaUser().setText("");
            view.getPhoneDisplayAreaUser().setText("");
            view.getEmailFieldUser().setText("");
            view.getPhoneFieldUser().setText("");
            view.getEmailIdToRemoveFieldUser().setText("");
            view.getPhoneIdToRemoveFieldUser().setText("");
        }
        currentUser = null;
    }
    
    private void autoLoadCurrentUser() {
        User authUser = mainController.getCurrentUser();
        if (authUser != null) {
            currentUser = model.getUserById(authUser.getUserId());
            
            if (currentUser != null) {
                displayUserInfo();
            }
        }
    }
    
    private void loadUser() {
        if (!mainController.isCurrentUserAdmin()) {
            JOptionPane.showMessageDialog(view, "This feature is only available to administrators.", 
                "Admin Access Required", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String userIdText = view.getUserIdField().getText().trim();
        
        if (userIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a User ID.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdText);
            currentUser = model.getUserById(userId);
            
            if (currentUser != null) {
                isAdminMode = true;
                displayUserInfo();
            } else {
                JOptionPane.showMessageDialog(view, "User ID not found.", 
                    "Not Found", JOptionPane.ERROR_MESSAGE);
                clearEditFields();
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid User ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displayUserInfo() {
        boolean isDisplayedUserAdmin = currentUser.isAdmin();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if (isAdminMode) {
            view.getFirstNameField().setText(currentUser.getFirstName());
            view.getLastNameField().setText(currentUser.getLastName());
            view.getNationalityField().setText(currentUser.getNationality());
            view.getPointsField().setText(String.valueOf(currentUser.getPoints()));
            view.getPasswordField().setText("");
            
            if (isDisplayedUserAdmin) {
                view.getTierLabel().setText("ADMIN");
                view.getTierLabel().setForeground(Color.RED);
            } else if (currentUser.getPointsTier() != null) {
                view.getTierLabel().setText(currentUser.getPointsTier().getTierName());
                view.getTierLabel().setForeground(Color.decode("#0066cc"));
            } else {
                view.getTierLabel().setText("No Tier");
                view.getTierLabel().setForeground(Color.decode("#0066cc"));
            }
            
            StringBuilder emailText = new StringBuilder();
            if (currentUser.getEmails() != null && !currentUser.getEmails().isEmpty()) {
                for (UserEmail email : currentUser.getEmails()) {
                    emailText.append("ID: ").append(email.getEmailId())
                            .append(" - ").append(email.getEmail())
                            .append(" (Added: ").append(email.getDateAdded() != null ? 
                                dateFormat.format(email.getDateAdded()) : "N/A")
                            .append(")\n");
                }
            } else {
                emailText.append("No emails found");
            }
            view.getEmailDisplayArea().setText(emailText.toString());
            
            StringBuilder phoneText = new StringBuilder();
            if (currentUser.getPhones() != null && !currentUser.getPhones().isEmpty()) {
                for (UserPhone phone : currentUser.getPhones()) {
                    phoneText.append("ID: ").append(phone.getPhoneId())
                            .append(" - ").append(phone.getPhoneNumber())
                            .append(" (Added: ").append(phone.getDateAdded() != null ? 
                                dateFormat.format(phone.getDateAdded()) : "N/A")
                            .append(")\n");
                }
            } else {
                phoneText.append("No phone numbers found");
            }
            view.getPhoneDisplayArea().setText(phoneText.toString());
        } else {
            view.getFirstNameFieldUser().setText(currentUser.getFirstName());
            view.getLastNameFieldUser().setText(currentUser.getLastName());
            view.getNationalityFieldUser().setText(currentUser.getNationality());
            view.getPointsFieldUser().setText(String.valueOf(currentUser.getPoints()));
            view.getPasswordFieldUser().setText("");
            
            if (isDisplayedUserAdmin) {
                view.getTierLabelUser().setText("ADMIN");
                view.getTierLabelUser().setForeground(Color.RED);
            } else if (currentUser.getPointsTier() != null) {
                view.getTierLabelUser().setText(currentUser.getPointsTier().getTierName());
                view.getTierLabelUser().setForeground(Color.decode("#0066cc"));
            } else {
                view.getTierLabelUser().setText("No Tier");
                view.getTierLabelUser().setForeground(Color.decode("#0066cc"));
            }
            
            StringBuilder emailText = new StringBuilder();
            if (currentUser.getEmails() != null && !currentUser.getEmails().isEmpty()) {
                for (UserEmail email : currentUser.getEmails()) {
                    emailText.append("ID: ").append(email.getEmailId())
                            .append(" - ").append(email.getEmail())
                            .append(" (Added: ").append(email.getDateAdded() != null ? 
                                dateFormat.format(email.getDateAdded()) : "N/A")
                            .append(")\n");
                }
            } else {
                emailText.append("No emails found");
            }
            view.getEmailDisplayAreaUser().setText(emailText.toString());
            
            StringBuilder phoneText = new StringBuilder();
            if (currentUser.getPhones() != null && !currentUser.getPhones().isEmpty()) {
                for (UserPhone phone : currentUser.getPhones()) {
                    phoneText.append("ID: ").append(phone.getPhoneId())
                            .append(" - ").append(phone.getPhoneNumber())
                            .append(" (Added: ").append(phone.getDateAdded() != null ? 
                                dateFormat.format(phone.getDateAdded()) : "N/A")
                            .append(")\n");
                }
            } else {
                phoneText.append("No phone numbers found");
            }
            view.getPhoneDisplayAreaUser().setText(phoneText.toString());
        }
    }
    
    
    private void updateUser() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm changes:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password. Changes not saved.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String firstName, lastName, nationality, newPassword;
            int points;
            
            if (isAdminMode) {
                firstName = view.getFirstNameField().getText().trim();
                lastName = view.getLastNameField().getText().trim();
                nationality = view.getNationalityField().getText().trim();
                points = Integer.parseInt(view.getPointsField().getText().trim());
                newPassword = view.getPasswordField().getText().trim();
            } else {
                firstName = view.getFirstNameFieldUser().getText().trim();
                lastName = view.getLastNameFieldUser().getText().trim();
                nationality = view.getNationalityFieldUser().getText().trim();
                points = currentUser.getPoints();
                newPassword = view.getPasswordFieldUser().getText().trim();
                
                try {
                    int fieldPoints = Integer.parseInt(view.getPointsFieldUser().getText().trim());
                    if (fieldPoints != currentUser.getPoints()) {
                        JOptionPane.showMessageDialog(view, 
                            "Points cannot be manually modified by users.\nYour current points will be preserved.", 
                            "Points Not Editable", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                }
            }
            
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(view, "First Name and Last Name cannot be empty.", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (points < 0) {
                JOptionPane.showMessageDialog(view, "Points cannot be negative.", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = model.updateUser(currentUser.getUserId(), firstName, lastName, nationality, points);
            
            if (!newPassword.isEmpty()) {
                if (newPassword.length() < 4) {
                    JOptionPane.showMessageDialog(view, "Password must be at least 4 characters long.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean passwordSuccess = model.updateUserPassword(currentUser.getUserId(), newPassword);
                if (!passwordSuccess) {
                    JOptionPane.showMessageDialog(view, "User info updated but password update failed.", 
                        "Partial Success", JOptionPane.WARNING_MESSAGE);
                }
            }
            
            if (success) {
                JOptionPane.showMessageDialog(view, "User updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                if (isAdminMode) {
                    loadUser();
                } else {
                    autoLoadCurrentUser();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to update user.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Points must be a valid number.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addEmail() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String email = isAdminMode ? view.getEmailField().getText().trim() : view.getEmailFieldUser().getText().trim();
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Email cannot be empty.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".com")) {
            JOptionPane.showMessageDialog(view, "Email must contain '@' and '.com'", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (email.indexOf("@") > email.indexOf(".com")) {
            JOptionPane.showMessageDialog(view, "Invalid email format. '@' must come before '.com'", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean success = model.addUserEmail(currentUser.getUserId(), email);
        
        if (success) {
            if (isAdminMode) {
                view.getEmailField().setText("");
                loadUser();
            } else {
                view.getEmailFieldUser().setText("");
                autoLoadCurrentUser();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Failed to add email.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeEmail() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String emailIdText = isAdminMode ? view.getEmailIdToRemoveField().getText().trim() : view.getEmailIdToRemoveFieldUser().getText().trim();
        
        if (emailIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter an Email ID to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int emailId = Integer.parseInt(emailIdText);
            
            boolean success = model.deleteUserEmail(emailId);
            
            if (success) {
                if (isAdminMode) {
                    view.getEmailIdToRemoveField().setText("");
                    loadUser();
                } else {
                    view.getEmailIdToRemoveFieldUser().setText("");
                    autoLoadCurrentUser();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to remove email. Check if Email ID is correct.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid Email ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addPhone() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String phone = isAdminMode ? view.getPhoneField().getText().trim() : view.getPhoneFieldUser().getText().trim();
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Phone number cannot be empty.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        
        if (digitsOnly.length() < 7 || digitsOnly.length() > 15) {
            JOptionPane.showMessageDialog(view, "Phone number must contain between 7 and 15 digits.\nCurrent digits: " + digitsOnly.length(), 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean success = model.addUserPhone(currentUser.getUserId(), phone);
        
        if (success) {
            if (isAdminMode) {
                view.getPhoneField().setText("");
                loadUser();
            } else {
                view.getPhoneFieldUser().setText("");
                autoLoadCurrentUser();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Failed to add phone.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removePhone() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String phoneIdText = isAdminMode ? view.getPhoneIdToRemoveField().getText().trim() : view.getPhoneIdToRemoveFieldUser().getText().trim();
        
        if (phoneIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a Phone ID to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int phoneId = Integer.parseInt(phoneIdText);
            
            boolean success = model.deleteUserPhone(phoneId);
            
            if (success) {
                if (isAdminMode) {
                    view.getPhoneIdToRemoveField().setText("");
                    loadUser();
                } else {
                    view.getPhoneIdToRemoveFieldUser().setText("");
                    autoLoadCurrentUser();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Failed to remove phone. Check if Phone ID is correct.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid Phone ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAccount() {
        if (currentUser == null && !isAdminMode) {
            JOptionPane.showMessageDialog(view, "No user loaded.", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userToDelete;
        if (isAdminMode) {
            String userIdText = view.getUserIdField().getText().trim();
            if (userIdText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please load a user first.", 
                    "No User Loaded", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                userToDelete = Integer.parseInt(userIdText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid User ID.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            userToDelete = currentUser.getUserId();
        }
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Are you sure you want to DELETE this account?\nThis action CANNOT be undone!", 
            "Confirm Account Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(view, passwordField, 
            "Enter your password to confirm deletion:", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        
        String password = new String(passwordField.getPassword());
        
        if (!mainController.verifyPassword(password)) {
            JOptionPane.showMessageDialog(view, "Incorrect password. Account not deleted.", 
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = model.deleteUser(userToDelete);
        
        if (success) {
            JOptionPane.showMessageDialog(view, "Account deleted successfully.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if (!isAdminMode && userToDelete == mainController.getCurrentUser().getUserId()) {
                mainController.logout();
                backToMain();
            } else {
                clearEditFields();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Failed to delete account.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertUser() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField nationalityField = new JTextField();
        JTextField pointsField = new JTextField("0");
        JTextField passwordField = new JTextField();
        JCheckBox isAdminCheck = new JCheckBox();
        
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Nationality:"));
        panel.add(nationalityField);
        panel.add(new JLabel("Points:"));
        panel.add(pointsField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Is Admin:"));
        panel.add(isAdminCheck);
        
        int result = JOptionPane.showConfirmDialog(view, panel, "Insert New User", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String nationality = nationalityField.getText().trim();
                int points = Integer.parseInt(pointsField.getText().trim());
                String password = passwordField.getText().trim();
                boolean isAdmin = isAdminCheck.isSelected();
                
                if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "First Name, Last Name, and Password are required.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (password.length() < 4) {
                    JOptionPane.showMessageDialog(view, "Password must be at least 4 characters long.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean success = model.insertUser(firstName, lastName, nationality, points, password, isAdmin);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "User inserted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to insert user.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Points must be a valid number.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteUser() {
        String userIdText = JOptionPane.showInputDialog(view, "Enter User ID to delete:", 
            "Delete User", JOptionPane.QUESTION_MESSAGE);
        
        if (userIdText == null || userIdText.trim().isEmpty()) {
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdText.trim());
            
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Are you sure you want to delete User ID " + userId + "?\nThis action CANNOT be undone!\n\nThis will also delete:\n- All user emails\n- All user phone numbers\n- All travel spots shared by this user\n- All bookings by this user\n- All feedback from this user", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Attempting to delete user ID: " + userId);
                boolean success = model.deleteUser(userId);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "User deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserTable();
                    refreshEmailsTable();
                    refreshPhonesTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to delete user. User ID " + userId + " may not exist.\nCheck the console for more details.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid User ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertTier() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField tierNameField = new JTextField();
        JTextField minPointsField = new JTextField();
        JTextField maxPointsField = new JTextField();
        
        panel.add(new JLabel("Tier Name:"));
        panel.add(tierNameField);
        panel.add(new JLabel("Min Points:"));
        panel.add(minPointsField);
        panel.add(new JLabel("Max Points:"));
        panel.add(maxPointsField);
        
        int result = JOptionPane.showConfirmDialog(view, panel, "Insert New Tier", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String tierName = tierNameField.getText().trim();
                int minPoints = Integer.parseInt(minPointsField.getText().trim());
                int maxPoints = Integer.parseInt(maxPointsField.getText().trim());
                
                if (tierName.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Tier Name is required.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (minPoints > maxPoints) {
                    JOptionPane.showMessageDialog(view, "Min Points cannot be greater than Max Points.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean success = model.insertTier(tierName, minPoints, maxPoints);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Tier inserted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTiersTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to insert tier.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Points must be valid numbers.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteTier() {
        String tierIdText = JOptionPane.showInputDialog(view, "Enter Tier ID to delete:", 
            "Delete Tier", JOptionPane.QUESTION_MESSAGE);
        
        if (tierIdText == null || tierIdText.trim().isEmpty()) {
            return;
        }
        
        try {
            int tierId = Integer.parseInt(tierIdText.trim());
            
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Are you sure you want to delete Tier ID " + tierId + "?\nUsers in this tier will have their Tier_ID set to NULL.", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Attempting to delete tier ID: " + tierId);
                boolean success = model.deleteTier(tierId);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Tier deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTiersTable();
                    refreshUserTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to delete tier. Tier ID " + tierId + " may not exist.\nCheck the console for more details.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid Tier ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertEmail() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField userIdField = new JTextField();
        JTextField emailField = new JTextField();
        
        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        
        int result = JOptionPane.showConfirmDialog(view, panel, "Insert New Email", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String email = emailField.getText().trim();
                
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Email is required.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!email.contains("@") || !email.contains(".com")) {
                    JOptionPane.showMessageDialog(view, "Email must contain '@' and '.com'", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (email.indexOf("@") > email.indexOf(".com")) {
                    JOptionPane.showMessageDialog(view, "Invalid email format. '@' must come before '.com'", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean success = model.addUserEmail(userId, email);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Email inserted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshEmailsTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to insert email. Check if User ID exists.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "User ID must be a valid number.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteEmail() {
        String emailIdText = JOptionPane.showInputDialog(view, "Enter Email ID to delete:", 
            "Delete Email", JOptionPane.QUESTION_MESSAGE);
        
        if (emailIdText == null || emailIdText.trim().isEmpty()) {
            return;
        }
        
        try {
            int emailId = Integer.parseInt(emailIdText.trim());
            
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Are you sure you want to delete Email ID " + emailId + "?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = model.deleteUserEmail(emailId);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Email deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshEmailsTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to delete email. Email ID may not exist.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid Email ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertPhone() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField userIdField = new JTextField();
        JTextField phoneField = new JTextField();
        
        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        
        int result = JOptionPane.showConfirmDialog(view, panel, "Insert New Phone", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String phone = phoneField.getText().trim();
                
                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Phone number is required.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String digitsOnly = phone.replaceAll("[^0-9]", "");
                
                if (digitsOnly.length() < 7 || digitsOnly.length() > 15) {
                    JOptionPane.showMessageDialog(view, "Phone number must contain between 7 and 15 digits.\nCurrent digits: " + digitsOnly.length(), 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean success = model.addUserPhone(userId, phone);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Phone inserted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshPhonesTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to insert phone. Check if User ID exists.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "User ID must be a valid number.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deletePhone() {
        String phoneIdText = JOptionPane.showInputDialog(view, "Enter Phone ID to delete:", 
            "Delete Phone", JOptionPane.QUESTION_MESSAGE);
        
        if (phoneIdText == null || phoneIdText.trim().isEmpty()) {
            return;
        }
        
        try {
            int phoneId = Integer.parseInt(phoneIdText.trim());
            
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Are you sure you want to delete Phone ID " + phoneId + "?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = model.deleteUserPhone(phoneId);
                
                if (success) {
                    JOptionPane.showMessageDialog(view, "Phone deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshPhonesTable();
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to delete phone. Phone ID may not exist.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid Phone ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToMain() {
        view.showCard(view.getEmptyCard());
        
        CardLayout layout = (CardLayout) view.getParent().getLayout();
        layout.show(view.getParent(), "Main");
    }
}