import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;  

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
        view.getFilterRecommendationsButton().addActionListener(this);
        
        view.getUpdateUserButton().addActionListener(this);
        view.getLoadUserButton().addActionListener(this);
        view.getAddEmailButton().addActionListener(this);
        view.getRemoveEmailButton().addActionListener(this);
        view.getAddPhoneButton().addActionListener(this);
        view.getRemovePhoneButton().addActionListener(this);
        
        view.getUpdateUserButtonUser().addActionListener(this);
        view.getAddEmailButtonUser().addActionListener(this);
        view.getRemoveEmailButtonUser().addActionListener(this);
        view.getAddPhoneButtonUser().addActionListener(this);
        view.getRemovePhoneButtonUser().addActionListener(this);
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
                
            case "FilterRecommendations":
                filterRecommendations();
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
    
    private void showRecommendations() {
        view.showCard(view.getRecommendationsCard());
        refreshRecommendations();
    }
    
    private void refreshRecommendations() {
        String monthStr = (String) view.getMonthComboBox().getSelectedItem();
        String yearStr = (String) view.getYearComboBox().getSelectedItem();
        
        if (monthStr != null && yearStr != null) {
            int month = Integer.parseInt(monthStr.substring(0, 2));
            int year = Integer.parseInt(yearStr);
            
            List<Object[]> recommendations = model.getRecommendationsByTierAndDate(month, year);
            view.updateRecommendationsTable(recommendations);
            
            if (recommendations.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "No recommendations found for " + monthStr + " " + year, 
                    "No Data", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void filterRecommendations() {
        refreshRecommendations();
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
        
        if (isAdminMode) {
            view.getFirstNameField().setText(currentUser.getFirstName());
            view.getLastNameField().setText(currentUser.getLastName());
            view.getNationalityField().setText(currentUser.getNationality());
            view.getPointsField().setText(String.valueOf(currentUser.getPoints()));
            view.getPasswordField().setText("");
            
            if (isDisplayedUserAdmin) {
                view.getTierLabel().setText("ADMIN");
                view.getTierLabel().setForeground(Color.decode("#cc0000"));
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
                            .append(" - ").append(email.getEmail()).append("\n");
                }
            } else {
                emailText.append("No emails found");
            }
            view.getEmailDisplayArea().setText(emailText.toString());
            
            StringBuilder phoneText = new StringBuilder();
            if (currentUser.getPhones() != null && !currentUser.getPhones().isEmpty()) {
                for (UserPhone phone : currentUser.getPhones()) {
                    phoneText.append("ID: ").append(phone.getPhoneId())
                            .append(" - ").append(phone.getPhoneNumber()).append("\n");
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
                view.getTierLabelUser().setForeground(Color.decode("#cc0000"));
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
                            .append(" - ").append(email.getEmail()).append("\n");
                }
            } else {
                emailText.append("No emails found");
            }
            view.getEmailDisplayAreaUser().setText(emailText.toString());
            
            StringBuilder phoneText = new StringBuilder();
            if (currentUser.getPhones() != null && !currentUser.getPhones().isEmpty()) {
                for (UserPhone phone : currentUser.getPhones()) {
                    phoneText.append("ID: ").append(phone.getPhoneId())
                            .append(" - ").append(phone.getPhoneNumber()).append("\n");
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
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(view, "Please enter a valid email address.", 
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
    
    private void backToMain() {
        view.showCard(view.getEmptyCard());
        
        CardLayout layout = (CardLayout) view.getParent().getLayout();
        layout.show(view.getParent(), "Main");
    }
}