import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class UserRecordViewer extends JPanel {
    private JPanel rightPanel;
    private CardLayout rightCardLayout;
    
    private static final String EMPTY_CARD = "Empty";
    private static final String TABLE_CARD = "Table";
    private static final String RECOMMENDATIONS_CARD = "Recommendations";
    private static final String EDIT_CARD_ADMIN = "EditAdmin";
    private static final String EDIT_CARD_USER = "EditUser";
    
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JScrollPane userScrollPane;

    private JTable recommendationsTable;
    private DefaultTableModel recommendationsTableModel;
    private JScrollPane recommendationsScrollPane;
    
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JButton filterRecommendationsButton;

    private JTextField userIdFieldAdmin;
    private JTextField firstNameFieldAdmin;
    private JTextField lastNameFieldAdmin;
    private JTextField nationalityFieldAdmin;
    private JTextField pointsFieldAdmin;
    private JLabel tierLabelAdmin;
    private JTextField passwordFieldAdmin;
    private JTextField emailFieldAdmin;
    private JTextField emailIdToRemoveFieldAdmin;
    private JTextField phoneFieldAdmin;
    private JTextField phoneIdToRemoveFieldAdmin;
    private JTextArea emailDisplayAreaAdmin;
    private JTextArea phoneDisplayAreaAdmin;
    private JButton updateUserButtonAdmin;
    private JButton loadUserButtonAdmin;
    private JButton addEmailButtonAdmin;
    private JButton removeEmailButtonAdmin;
    private JButton addPhoneButtonAdmin;
    private JButton removePhoneButtonAdmin;

    private JTextField firstNameFieldUser;
    private JTextField lastNameFieldUser;
    private JTextField nationalityFieldUser;
    private JTextField pointsFieldUser;
    private JLabel tierLabelUser;
    private JTextField passwordFieldUser;
    private JTextField emailFieldUser;
    private JTextField emailIdToRemoveFieldUser;
    private JTextField phoneFieldUser;
    private JTextField phoneIdToRemoveFieldUser;
    private JTextArea emailDisplayAreaUser;
    private JTextArea phoneDisplayAreaUser;
    private JButton updateUserButtonUser;
    private JButton addEmailButtonUser;
    private JButton removeEmailButtonUser;
    private JButton addPhoneButtonUser;
    private JButton removePhoneButtonUser;

    private JButton viewUserButton;
    private JButton viewRecommendedButton;
    private JButton editInfoButton;
    private JButton backButton;
    private JButton refreshTableButton;
    private JButton refreshRecommendationsButton;

    public UserRecordViewer() {}

    public UserRecordViewer(JPanel cardPanel) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1500, 920));
        setMaximumSize(new Dimension(1500, 920));
        setMinimumSize(new Dimension(1500, 920));

        initialization();

        cardPanel.add(this, MainDBViewer.USER_LINK);
    }
    
    private void initialization() {
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; 
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        Dimension buttonSize = new Dimension(200, 100);

        viewUserButton = new JButton("View User Table");
        viewUserButton.setPreferredSize(buttonSize);
        viewUserButton.setFont(new Font("Arial", Font.PLAIN, 15));
        viewUserButton.setActionCommand("ViewUserTable");
        panelLeft.add(viewUserButton, gbc);
        
        viewRecommendedButton = new JButton("View Recommendations");
        viewRecommendedButton.setPreferredSize(buttonSize);
        viewRecommendedButton.setFont(new Font("Arial", Font.PLAIN, 15));
        viewRecommendedButton.setActionCommand("ViewRecommendations");
        panelLeft.add(viewRecommendedButton, gbc);

        editInfoButton = new JButton("Edit User Information");
        editInfoButton.setPreferredSize(buttonSize);
        editInfoButton.setFont(new Font("Arial", Font.PLAIN, 15));
        editInfoButton.setActionCommand("EditUserInfo");
        panelLeft.add(editInfoButton, gbc);

        gbc.insets = new Insets(50, 10, 10, 10);

        backButton = new JButton("Back");
        backButton.setPreferredSize(buttonSize);
        backButton.setFont(new Font("Arial", Font.PLAIN, 30));
        backButton.setActionCommand("BackToMain");
        panelLeft.add(backButton, gbc);

        this.add(panelLeft, BorderLayout.WEST);

        rightCardLayout = new CardLayout();
        rightPanel = new JPanel(rightCardLayout);
        
        rightPanel.add(createEmptyPanel(), EMPTY_CARD);
        rightPanel.add(createTablePanel(), TABLE_CARD);
        rightPanel.add(createRecommendationsPanel(), RECOMMENDATIONS_CARD);
        rightPanel.add(createEditPanelAdmin(), EDIT_CARD_ADMIN);
        rightPanel.add(createEditPanelUser(), EDIT_CARD_USER);
        
        this.add(rightPanel, BorderLayout.CENTER);
    }   

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Select an option from the left menu");
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        label.setForeground(Color.GRAY);
        panel.add(label);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("User Records (Admin Only)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"User ID", "First Name", "Last Name", "Nationality", "Points", "Tier", "Locations Shared"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        userScrollPane = new JScrollPane(userTable);
        panel.add(userScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshTableButton = new JButton("Refresh");
        refreshTableButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshTableButton.setActionCommand("RefreshUserTable");
        buttonPanel.add(refreshTableButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Recommendations Rating Report by Points Tier");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter by Date"));
        
        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filterPanel.add(monthLabel);
        
        String[] months = {"01 - January", "02 - February", "03 - March", "04 - April", 
                          "05 - May", "06 - June", "07 - July", "08 - August", 
                          "09 - September", "10 - October", "11 - November", "12 - December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(monthComboBox);
        
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filterPanel.add(yearLabel);
        
        String[] years = new String[10];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(yearComboBox);
        
        filterRecommendationsButton = new JButton("Apply Filter");
        filterRecommendationsButton.setFont(new Font("Arial", Font.BOLD, 14));
        filterRecommendationsButton.setActionCommand("FilterRecommendations");
        filterPanel.add(filterRecommendationsButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Location ID", "Area", "City", "Region", "Country", 
                           "Points Tier", "Recommendations Count"};
        recommendationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recommendationsTable = new JTable(recommendationsTableModel);
        recommendationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        recommendationsTable.setRowHeight(25);
        recommendationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        recommendationsScrollPane = new JScrollPane(recommendationsTable);
        panel.add(recommendationsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshRecommendationsButton = new JButton("Refresh");
        refreshRecommendationsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshRecommendationsButton.setActionCommand("RefreshRecommendations");
        buttonPanel.add(refreshRecommendationsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEditPanelAdmin() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Edit User Information (Admin Mode)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int currentRow = 0;

        gbc.gridx = 0; gbc.gridy = currentRow;
        JLabel userIdLabel = new JLabel("Enter User ID:");
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(userIdLabel, gbc);
        
        gbc.gridx = 1;
        userIdFieldAdmin = new JTextField(15);
        formPanel.add(userIdFieldAdmin, gbc);
        
        gbc.gridx = 2;
        loadUserButtonAdmin = new JButton("Load User");
        loadUserButtonAdmin.setActionCommand("LoadUser");
        loadUserButtonAdmin.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(loadUserButtonAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        currentRow++;
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = currentRow;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        firstNameFieldAdmin = new JTextField(20);
        formPanel.add(firstNameFieldAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        lastNameFieldAdmin = new JTextField(20);
        formPanel.add(lastNameFieldAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nationalityFieldAdmin = new JTextField(20);
        formPanel.add(nationalityFieldAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Points:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        pointsFieldAdmin = new JTextField(20);
        formPanel.add(pointsFieldAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Current Tier:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        tierLabelAdmin = new JLabel("N/A");
        tierLabelAdmin.setFont(new Font("Arial", Font.BOLD, 14));
        tierLabelAdmin.setForeground(Color.decode("#0066cc"));
        formPanel.add(tierLabelAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        passwordFieldAdmin = new JTextField(20);
        formPanel.add(passwordFieldAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        JLabel passwordHint = new JLabel("(Leave blank to keep current password)");
        passwordHint.setFont(new Font("Arial", Font.ITALIC, 11));
        passwordHint.setForeground(Color.GRAY);
        formPanel.add(passwordHint, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        currentRow++;

        gbc.gridy = currentRow;
        JLabel emailSectionLabel = new JLabel("Emails:");
        emailSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(emailSectionLabel, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        emailDisplayAreaAdmin = new JTextArea(3, 30);
        emailDisplayAreaAdmin.setEditable(false);
        emailDisplayAreaAdmin.setBackground(Color.WHITE);
        JScrollPane emailScroll = new JScrollPane(emailDisplayAreaAdmin);
        formPanel.add(emailScroll, gbc);

        currentRow++;
        gbc.gridy = currentRow; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Email:"), gbc);
        gbc.gridx = 1;
        emailFieldAdmin = new JTextField(15);
        formPanel.add(emailFieldAdmin, gbc);
        gbc.gridx = 2;
        addEmailButtonAdmin = new JButton("Add");
        addEmailButtonAdmin.setActionCommand("AddEmail");
        formPanel.add(addEmailButtonAdmin, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Email ID to Remove:"), gbc);
        gbc.gridx = 1;
        emailIdToRemoveFieldAdmin = new JTextField(15);
        formPanel.add(emailIdToRemoveFieldAdmin, gbc);
        gbc.gridx = 2;
        removeEmailButtonAdmin = new JButton("Remove");
        removeEmailButtonAdmin.setActionCommand("RemoveEmail");
        formPanel.add(removeEmailButtonAdmin, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        currentRow++;

        gbc.gridy = currentRow;
        JLabel phoneSectionLabel = new JLabel("Phone Numbers:");
        phoneSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(phoneSectionLabel, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        phoneDisplayAreaAdmin = new JTextArea(3, 30);
        phoneDisplayAreaAdmin.setEditable(false);
        phoneDisplayAreaAdmin.setBackground(Color.WHITE);
        JScrollPane phoneScroll = new JScrollPane(phoneDisplayAreaAdmin);
        formPanel.add(phoneScroll, gbc);

        currentRow++;
        gbc.gridy = currentRow; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Phone:"), gbc);
        gbc.gridx = 1;
        phoneFieldAdmin = new JTextField(15);
        formPanel.add(phoneFieldAdmin, gbc);
        gbc.gridx = 2;
        addPhoneButtonAdmin = new JButton("Add");
        addPhoneButtonAdmin.setActionCommand("AddPhone");
        formPanel.add(addPhoneButtonAdmin, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Phone ID to Remove:"), gbc);
        gbc.gridx = 1;
        phoneIdToRemoveFieldAdmin = new JTextField(15);
        formPanel.add(phoneIdToRemoveFieldAdmin, gbc);
        gbc.gridx = 2;
        removePhoneButtonAdmin = new JButton("Remove");
        removePhoneButtonAdmin.setActionCommand("RemovePhone");
        formPanel.add(removePhoneButtonAdmin, gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        panel.add(formScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateUserButtonAdmin = new JButton("Update User");
        updateUserButtonAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        updateUserButtonAdmin.setActionCommand("UpdateUser");
        buttonPanel.add(updateUserButtonAdmin);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEditPanelUser() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Edit My Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int currentRow = 0;

        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        JLabel infoLabel = new JLabel("Editing your own information");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        infoLabel.setForeground(Color.decode("#0066cc"));
        formPanel.add(infoLabel, gbc);
        currentRow++;
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = currentRow;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        firstNameFieldUser = new JTextField(20);
        formPanel.add(firstNameFieldUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        lastNameFieldUser = new JTextField(20);
        formPanel.add(lastNameFieldUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nationalityFieldUser = new JTextField(20);
        formPanel.add(nationalityFieldUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Points:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        pointsFieldUser = new JTextField(20);
        pointsFieldUser.setEditable(false);
        pointsFieldUser.setBackground(Color.LIGHT_GRAY);
        formPanel.add(pointsFieldUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        JLabel pointsHint = new JLabel("(Points are automatically awarded and cannot be manually edited)");
        pointsHint.setFont(new Font("Arial", Font.ITALIC, 11));
        pointsHint.setForeground(Color.GRAY);
        formPanel.add(pointsHint, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Current Tier:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        tierLabelUser = new JLabel("N/A");
        tierLabelUser.setFont(new Font("Arial", Font.BOLD, 14));
        tierLabelUser.setForeground(Color.decode("#0066cc"));
        formPanel.add(tierLabelUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 1;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        passwordFieldUser = new JTextField(20);
        formPanel.add(passwordFieldUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        JLabel passwordHint = new JLabel("(Leave blank to keep current password)");
        passwordHint.setFont(new Font("Arial", Font.ITALIC, 11));
        passwordHint.setForeground(Color.GRAY);
        formPanel.add(passwordHint, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        currentRow++;

        gbc.gridy = currentRow;
        JLabel emailSectionLabel = new JLabel("Emails:");
        emailSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(emailSectionLabel, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        emailDisplayAreaUser = new JTextArea(3, 30);
        emailDisplayAreaUser.setEditable(false);
        emailDisplayAreaUser.setBackground(Color.WHITE);
        JScrollPane emailScroll = new JScrollPane(emailDisplayAreaUser);
        formPanel.add(emailScroll, gbc);

        currentRow++;
        gbc.gridy = currentRow; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Email:"), gbc);
        gbc.gridx = 1;
        emailFieldUser = new JTextField(15);
        formPanel.add(emailFieldUser, gbc);
        gbc.gridx = 2;
        addEmailButtonUser = new JButton("Add");
        addEmailButtonUser.setActionCommand("AddEmail");
        formPanel.add(addEmailButtonUser, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Email ID to Remove:"), gbc);
        gbc.gridx = 1;
        emailIdToRemoveFieldUser = new JTextField(15);
        formPanel.add(emailIdToRemoveFieldUser, gbc);
        gbc.gridx = 2;
        removeEmailButtonUser = new JButton("Remove");
        removeEmailButtonUser.setActionCommand("RemoveEmail");
        formPanel.add(removeEmailButtonUser, gbc);

        currentRow++;
        gbc.gridx = 0; gbc.gridy = currentRow; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        currentRow++;

        gbc.gridy = currentRow;
        JLabel phoneSectionLabel = new JLabel("Phone Numbers:");
        phoneSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(phoneSectionLabel, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        phoneDisplayAreaUser = new JTextArea(3, 30);
        phoneDisplayAreaUser.setEditable(false);
        phoneDisplayAreaUser.setBackground(Color.WHITE);
        JScrollPane phoneScroll = new JScrollPane(phoneDisplayAreaUser);
        formPanel.add(phoneScroll, gbc);

        currentRow++;
        gbc.gridy = currentRow; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Phone:"), gbc);
        gbc.gridx = 1;
        phoneFieldUser = new JTextField(15);
        formPanel.add(phoneFieldUser, gbc);
        gbc.gridx = 2;
        addPhoneButtonUser = new JButton("Add");
        addPhoneButtonUser.setActionCommand("AddPhone");
        formPanel.add(addPhoneButtonUser, gbc);

        currentRow++;
        gbc.gridy = currentRow;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Phone ID to Remove:"), gbc);
        gbc.gridx = 1;
        phoneIdToRemoveFieldUser = new JTextField(15);
        formPanel.add(phoneIdToRemoveFieldUser, gbc);
        gbc.gridx = 2;
        removePhoneButtonUser = new JButton("Remove");
        removePhoneButtonUser.setActionCommand("RemovePhone");
        formPanel.add(removePhoneButtonUser, gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        panel.add(formScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateUserButtonUser = new JButton("Update User");
        updateUserButtonUser.setFont(new Font("Arial", Font.BOLD, 16));
        updateUserButtonUser.setActionCommand("UpdateUser");
        buttonPanel.add(updateUserButtonUser);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }  

    public JButton getViewUserButton() { return viewUserButton; }
    public JButton getViewRecommendedButton() { return viewRecommendedButton; }
    public JButton getFilterRecommendationsButton() { return filterRecommendationsButton; }
    public JButton getEditInfoButton() { return editInfoButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getRefreshTableButton() { return refreshTableButton; }
    public JButton getRefreshRecommendationsButton() { return refreshRecommendationsButton; }

    public JButton getUpdateUserButton() { return updateUserButtonAdmin; }
    public JButton getLoadUserButton() { return loadUserButtonAdmin; }
    public JButton getAddEmailButton() { return addEmailButtonAdmin; }
    public JButton getRemoveEmailButton() { return removeEmailButtonAdmin; }
    public JButton getAddPhoneButton() { return addPhoneButtonAdmin; }
    public JButton getRemovePhoneButton() { return removePhoneButtonAdmin; }

    public JTextField getUserIdField() { return userIdFieldAdmin; }
    public JTextField getFirstNameField() { return firstNameFieldAdmin; }
    public JTextField getLastNameField() { return lastNameFieldAdmin; }
    public JTextField getNationalityField() { return nationalityFieldAdmin; }
    public JTextField getPointsField() { return pointsFieldAdmin; }
    public JLabel getTierLabel() { return tierLabelAdmin; }
    public JTextField getPasswordField() { return passwordFieldAdmin; }
    public JTextField getEmailField() { return emailFieldAdmin; }
    public JTextField getEmailIdToRemoveField() { return emailIdToRemoveFieldAdmin; }
    public JTextField getPhoneField() { return phoneFieldAdmin; }
    public JTextField getPhoneIdToRemoveField() { return phoneIdToRemoveFieldAdmin; }
    public JTextArea getEmailDisplayArea() { return emailDisplayAreaAdmin; }
    public JTextArea getPhoneDisplayArea() { return phoneDisplayAreaAdmin; }

    public JButton getUpdateUserButtonUser() { return updateUserButtonUser; }
    public JButton getAddEmailButtonUser() { return addEmailButtonUser; }
    public JButton getRemoveEmailButtonUser() { return removeEmailButtonUser; }
    public JButton getAddPhoneButtonUser() { return addPhoneButtonUser; }
    public JButton getRemovePhoneButtonUser() { return removePhoneButtonUser; }
    
    public JTextField getFirstNameFieldUser() { return firstNameFieldUser; }
    public JTextField getLastNameFieldUser() { return lastNameFieldUser; }
    public JTextField getNationalityFieldUser() { return nationalityFieldUser; }
    public JTextField getPointsFieldUser() { return pointsFieldUser; }
    public JLabel getTierLabelUser() { return tierLabelUser; }
    public JTextField getPasswordFieldUser() { return passwordFieldUser; }
    public JTextField getEmailFieldUser() { return emailFieldUser; }
    public JTextField getEmailIdToRemoveFieldUser() { return emailIdToRemoveFieldUser; }
    public JTextField getPhoneFieldUser() { return phoneFieldUser; }
    public JTextField getPhoneIdToRemoveFieldUser() { return phoneIdToRemoveFieldUser; }
    public JTextArea getEmailDisplayAreaUser() { return emailDisplayAreaUser; }
    public JTextArea getPhoneDisplayAreaUser() { return phoneDisplayAreaUser; }
    
    public JComboBox<String> getMonthComboBox() { return monthComboBox; }
    public JComboBox<String> getYearComboBox() { return yearComboBox; }

    public void updateUserTable(List<User> users) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getNationality(),
                user.getPoints(),
                user.getPointsTier() != null ? user.getPointsTier().getTierName() : "No Tier"
            };
            userTableModel.addRow(row);
        }
    }

    public void updateRecommendationsTable(List<Object[]> recommendations) {
        recommendationsTableModel.setRowCount(0);
        for (Object[] rec : recommendations) {
            recommendationsTableModel.addRow(rec);
        }
    }

    public void showCard(String cardName) {
        rightCardLayout.show(rightPanel, cardName);
    }

    public String getEmptyCard() { return EMPTY_CARD; }
    public String getTableCard() { return TABLE_CARD; }
    public String getRecommendationsCard() { return RECOMMENDATIONS_CARD; }
    public String getEditCardAdmin() { return EDIT_CARD_ADMIN; }
    public String getEditCardUser() { return EDIT_CARD_USER; }
}