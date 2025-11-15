import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.List;

public class UserRecordController implements ActionListener {
    
    private Connection conn;
    private MainDBController mainController;
    private UserRecordViewer view;
    private UserRecordModel model;
    
    private User currentUser;
    
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
        view.getUpdateUserButton().addActionListener(this);
        view.getLoadUserButton().addActionListener(this);
        view.getAddEmailButton().addActionListener(this);
        view.getRemoveEmailButton().addActionListener(this);
        view.getAddPhoneButton().addActionListener(this);
        view.getRemovePhoneButton().addActionListener(this);
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
        }
    }
    
    private void showUserTable() {
        view.showCard(view.getTableCard());
        refreshUserTable();
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
        List<Object[]> recommendations = model.getRecommendedSpots();
        view.updateRecommendationsTable(recommendations);
    }
    
    private void showEditPanel() {
        view.showCard(view.getEditCard());
        clearEditFields();
    }
    
    private void clearEditFields() {
        view.getUserIdField().setText("");
        view.getFirstNameField().setText("");
        view.getLastNameField().setText("");
        view.getNationalityField().setText("");
        view.getPointsField().setText("");
        view.getTierLabel().setText("N/A");
        view.getEmailDisplayArea().setText("");
        view.getPhoneDisplayArea().setText("");
        view.getEmailField().setText("");
        view.getPhoneField().setText("");
        view.getEmailIdToRemoveField().setText("");
        view.getPhoneIdToRemoveField().setText("");
        currentUser = null;
    }
    
    private void loadUser() {
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

                view.getFirstNameField().setText(currentUser.getFirstName());
                view.getLastNameField().setText(currentUser.getLastName());
                view.getNationalityField().setText(currentUser.getNationality());
                view.getPointsField().setText(String.valueOf(currentUser.getPoints()));
                
                if (currentUser.getPointsTier() != null) {
                    view.getTierLabel().setText(currentUser.getPointsTier().getTierName());
                } else {
                    view.getTierLabel().setText("No Tier");
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
                JOptionPane.showMessageDialog(view, "User ID not found.", 
                    "Not Found", JOptionPane.ERROR_MESSAGE);
                clearEditFields();
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Please enter a valid User ID number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateUser() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(view, "Please load a user first.", 
                "No User Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String firstName = view.getFirstNameField().getText().trim();
            String lastName = view.getLastNameField().getText().trim();
            String nationality = view.getNationalityField().getText().trim();
            int points = Integer.parseInt(view.getPointsField().getText().trim());
            
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
            
            if (success) {
                JOptionPane.showMessageDialog(view, "User updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                // Reload user to show updated tier
                loadUser();
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
        
        String email = view.getEmailField().getText().trim();
        
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
            view.getEmailField().setText("");
            loadUser(); // Reload to show new email
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
        
        String emailIdText = view.getEmailIdToRemoveField().getText().trim();
        
        if (emailIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter an Email ID to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int emailId = Integer.parseInt(emailIdText);
            
            boolean success = model.deleteUserEmail(emailId);
            
            if (success) {
                view.getEmailIdToRemoveField().setText("");
                loadUser(); // Reload to update list
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
        
        String phone = view.getPhoneField().getText().trim();
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Phone number cannot be empty.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean success = model.addUserPhone(currentUser.getUserId(), phone);
        
        if (success) {
            view.getPhoneField().setText("");
            loadUser();
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
        
        String phoneIdText = view.getPhoneIdToRemoveField().getText().trim();
        
        if (phoneIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a Phone ID to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int phoneId = Integer.parseInt(phoneIdText);
            
            boolean success = model.deleteUserPhone(phoneId);
            
            if (success) {
                view.getPhoneIdToRemoveField().setText("");
                loadUser();
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