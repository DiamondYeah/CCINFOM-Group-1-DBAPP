import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

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
    
    private JTable tiersTable;
    private DefaultTableModel tiersTableModel;
    private JScrollPane tiersScrollPane;
    
    private JTable emailsTable;
    private DefaultTableModel emailsTableModel;
    private JScrollPane emailsScrollPane;
    
    private JTable phonesTable;
    private DefaultTableModel phonesTableModel;
    private JScrollPane phonesScrollPane;

    private JTable recommendationsTable;
    private DefaultTableModel recommendationsTableModel;
    private JScrollPane recommendationsScrollPane;

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
    private JButton deleteAccountButtonAdmin;

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
    private JButton deleteAccountButtonUser;

    private JButton viewUserButton;
    private JButton viewRecommendedButton;
    private JButton editInfoButton;
    private JButton backButton;
    
    private JButton refreshTableButton;
    private JButton refreshRecommendationsButton;
    private JButton refreshTiersButton;
    private JButton refreshEmailsButton;
    private JButton refreshPhonesButton;
    private JButton insertUserButton;
    private JButton deleteUserButton;
    private JButton insertTierButton;
    private JButton deleteTierButton;
    private JButton insertEmailButton;
    private JButton deleteEmailButton;
    private JButton insertPhoneButton;
    private JButton deletePhoneButton;

    public UserRecordViewer() {}

    public UserRecordViewer(JPanel cardPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#bfbfb2"));

        initialization();

        cardPanel.add(this, MainDBViewer.USER_LINK);
    }
    
    private void initialization() {
        JPanel panelWest = new JPanel(new GridBagLayout());
        panelWest.setBackground(Color.GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        viewUserButton = new JButton("View User Table");
        viewRecommendedButton = new JButton("View Recommendations");
        editInfoButton = new JButton("Edit User Information");
        backButton = new JButton("Back");

        viewUserButton.setActionCommand("ViewUserTable");
    viewRecommendedButton.setActionCommand("ViewRecommendations");
    editInfoButton.setActionCommand("EditUserInfo");
    backButton.setActionCommand("BackToMain");

        int row = 0;
        gbc.gridy = row++;
        panelWest.add(viewUserButton, gbc);
        gbc.gridy = row++;
        panelWest.add(viewRecommendedButton, gbc);
        gbc.gridy = row++;
        panelWest.add(editInfoButton, gbc);
        
        gbc.insets = new Insets(50, 10, 10, 10);
        gbc.gridy = row++;
        panelWest.add(backButton, gbc);

        this.add(panelWest, BorderLayout.WEST);

        rightCardLayout = new CardLayout();
        rightPanel = new JPanel(rightCardLayout);
        rightPanel.setBackground(Color.decode("#bfbfb2"));
        
        rightPanel.add(createEmptyPanel(), EMPTY_CARD);
        rightPanel.add(createTablePanel(), TABLE_CARD);
        rightPanel.add(createRecommendationsPanel(), RECOMMENDATIONS_CARD);
        rightPanel.add(createEditPanelAdmin(), EDIT_CARD_ADMIN);
        rightPanel.add(createEditPanelUser(), EDIT_CARD_USER);
        
        this.add(rightPanel, BorderLayout.CENTER);
    }   

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#bfbfb2"));
        JLabel label = new JLabel("Select an option from the left menu");
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        label.setForeground(Color.GRAY);
        panel.add(label);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#bfbfb2"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("User Records (Admin Only)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel userTablePanel = new JPanel(new BorderLayout(5, 5));
        userTablePanel.setBackground(Color.decode("#bfbfb2"));
        String[] userColumns = {"User ID", "First Name", "Last Name", "Nationality", "Points", "Tier", 
                                "Password", "Is Admin", "Locations Shared", "Reviews Made", "Bookings Count"};
        userTableModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        userScrollPane = new JScrollPane(userTable);
        userTablePanel.add(userScrollPane, BorderLayout.CENTER);
        
        JPanel userButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userButtonPanel.setBackground(Color.decode("#bfbfb2"));
        refreshTableButton = new JButton("Refresh");
        refreshTableButton.setActionCommand("RefreshUserTable");
        insertUserButton = new JButton("Insert User");
        insertUserButton.setActionCommand("InsertUser");
        deleteUserButton = new JButton("Delete User");
        deleteUserButton.setActionCommand("DeleteUser");
        userButtonPanel.add(refreshTableButton);
        userButtonPanel.add(insertUserButton);
        userButtonPanel.add(deleteUserButton);
        userTablePanel.add(userButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Users", userTablePanel);
        
        JPanel tiersTablePanel = new JPanel(new BorderLayout(5, 5));
        tiersTablePanel.setBackground(Color.decode("#bfbfb2"));
        String[] tierColumns = {"Tier ID", "Tier Name", "Min Points", "Max Points"};
        tiersTableModel = new DefaultTableModel(tierColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tiersTable = new JTable(tiersTableModel);
        tiersTable.setFont(new Font("Arial", Font.PLAIN, 12));
        tiersTable.setRowHeight(25);
        tiersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tiersScrollPane = new JScrollPane(tiersTable);
        tiersTablePanel.add(tiersScrollPane, BorderLayout.CENTER);
        
        JPanel tiersButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tiersButtonPanel.setBackground(Color.decode("#bfbfb2"));
        refreshTiersButton = new JButton("Refresh");
        refreshTiersButton.setActionCommand("RefreshTiers");
        insertTierButton = new JButton("Insert Tier");
        insertTierButton.setActionCommand("InsertTier");
        deleteTierButton = new JButton("Delete Tier");
        deleteTierButton.setActionCommand("DeleteTier");
        tiersButtonPanel.add(refreshTiersButton);
        tiersButtonPanel.add(insertTierButton);
        tiersButtonPanel.add(deleteTierButton);
        tiersTablePanel.add(tiersButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Points Tiers", tiersTablePanel);
        
        JPanel emailsTablePanel = new JPanel(new BorderLayout(5, 5));
        emailsTablePanel.setBackground(Color.decode("#bfbfb2"));
        String[] emailColumns = {"Email ID", "User ID", "Email", "Date Added"};
        emailsTableModel = new DefaultTableModel(emailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        emailsTable = new JTable(emailsTableModel);
        emailsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        emailsTable.setRowHeight(25);
        emailsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        emailsScrollPane = new JScrollPane(emailsTable);
        emailsTablePanel.add(emailsScrollPane, BorderLayout.CENTER);
        
        JPanel emailsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        emailsButtonPanel.setBackground(Color.decode("#bfbfb2"));
        refreshEmailsButton = new JButton("Refresh");
        refreshEmailsButton.setActionCommand("RefreshEmails");
        insertEmailButton = new JButton("Insert Email");
        insertEmailButton.setActionCommand("InsertEmail");
        deleteEmailButton = new JButton("Delete Email");
        deleteEmailButton.setActionCommand("DeleteEmail");
        emailsButtonPanel.add(refreshEmailsButton);
        emailsButtonPanel.add(insertEmailButton);
        emailsButtonPanel.add(deleteEmailButton);
        emailsTablePanel.add(emailsButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("User Emails", emailsTablePanel);
        
        JPanel phonesTablePanel = new JPanel(new BorderLayout(5, 5));
        phonesTablePanel.setBackground(Color.decode("#bfbfb2"));
        String[] phoneColumns = {"Phone ID", "User ID", "Phone Number", "Date Added"};
        phonesTableModel = new DefaultTableModel(phoneColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        phonesTable = new JTable(phonesTableModel);
        phonesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        phonesTable.setRowHeight(25);
        phonesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        phonesScrollPane = new JScrollPane(phonesTable);
        phonesTablePanel.add(phonesScrollPane, BorderLayout.CENTER);
        
        JPanel phonesButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        phonesButtonPanel.setBackground(Color.decode("#bfbfb2"));
        refreshPhonesButton = new JButton("Refresh");
        refreshPhonesButton.setActionCommand("RefreshPhones");
        insertPhoneButton = new JButton("Insert Phone");
        insertPhoneButton.setActionCommand("InsertPhone");
        deletePhoneButton = new JButton("Delete Phone");
        deletePhoneButton.setActionCommand("DeletePhone");
        phonesButtonPanel.add(refreshPhonesButton);
        phonesButtonPanel.add(insertPhoneButton);
        phonesButtonPanel.add(deletePhoneButton);
        phonesTablePanel.add(phonesButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("User Phones", phonesTablePanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#bfbfb2"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Recommendations Rating Report by Points Tier");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Location ID", "Spot Name", "City", "Region", "Country", 
                           "Points Tier", "Recommendations Count", "Date", "Time"};
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
        buttonPanel.setBackground(Color.decode("#bfbfb2"));
        refreshRecommendationsButton = new JButton("Refresh");
        refreshRecommendationsButton.setActionCommand("RefreshRecommendations");
        buttonPanel.add(refreshRecommendationsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEditPanelAdmin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#bfbfb2"));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
    
        int row = 0;
    
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Edit User Information (Admin)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);
    
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(userIdLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel userIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        userIdPanel.setBackground(Color.decode("#bfbfb2"));
        userIdFieldAdmin = new JTextField(15);
        loadUserButtonAdmin = new JButton("Load User");
        loadUserButtonAdmin.setActionCommand("LoadUser");
        userIdPanel.add(userIdFieldAdmin);
        userIdPanel.add(loadUserButtonAdmin);
        panel.add(userIdPanel, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(firstNameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        firstNameFieldAdmin = new JTextField(20);
        panel.add(firstNameFieldAdmin, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lastNameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        lastNameFieldAdmin = new JTextField(20);
        panel.add(lastNameFieldAdmin, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nationalityLabel = new JLabel("Nationality:");
        nationalityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nationalityLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        nationalityFieldAdmin = new JTextField(20);
        panel.add(nationalityFieldAdmin, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel pointsLabel = new JLabel("Points:");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(pointsLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pointsFieldAdmin = new JTextField(20);
        panel.add(pointsFieldAdmin, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel tierTextLabel = new JLabel("Current Tier:");
        tierTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(tierTextLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        tierLabelAdmin = new JLabel("N/A");
        tierLabelAdmin.setFont(new Font("Arial", Font.BOLD, 14));
        tierLabelAdmin.setForeground(Color.decode("#0066cc"));
        panel.add(tierLabelAdmin, gbc);
    
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordLabel, gbc);
    
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        passwordFieldAdmin = new JTextField(20);
        panel.add(passwordFieldAdmin, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailSectionLabel = new JLabel("Emails:");
        emailSectionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(emailSectionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        emailDisplayAreaAdmin = new JTextArea(3, 20);
        emailDisplayAreaAdmin.setEditable(false);
        emailDisplayAreaAdmin.setBackground(Color.WHITE);
        JScrollPane emailScroll = new JScrollPane(emailDisplayAreaAdmin);
        panel.add(emailScroll, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel newEmailLabel = new JLabel("New Email:");
        newEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(newEmailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        emailPanel.setBackground(Color.decode("#bfbfb2"));
        emailFieldAdmin = new JTextField(15);
        addEmailButtonAdmin = new JButton("Add");
        addEmailButtonAdmin.setActionCommand("AddEmail");
        emailPanel.add(emailFieldAdmin);
        emailPanel.add(addEmailButtonAdmin);
        panel.add(emailPanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel removeEmailLabel = new JLabel("Email ID to Remove:");
        removeEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(removeEmailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel removeEmailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        removeEmailPanel.setBackground(Color.decode("#bfbfb2"));
        emailIdToRemoveFieldAdmin = new JTextField(15);
        removeEmailButtonAdmin = new JButton("Remove");
        removeEmailButtonAdmin.setActionCommand("RemoveEmail");
        removeEmailPanel.add(emailIdToRemoveFieldAdmin);
        removeEmailPanel.add(removeEmailButtonAdmin);
        panel.add(removeEmailPanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel phoneSectionLabel = new JLabel("Phone Numbers:");
        phoneSectionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(phoneSectionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        phoneDisplayAreaAdmin = new JTextArea(3, 20);
        phoneDisplayAreaAdmin.setEditable(false);
        phoneDisplayAreaAdmin.setBackground(Color.WHITE);
        JScrollPane phoneScroll = new JScrollPane(phoneDisplayAreaAdmin);
        panel.add(phoneScroll, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel newPhoneLabel = new JLabel("New Phone:");
        newPhoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(newPhoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        phonePanel.setBackground(Color.decode("#bfbfb2"));
        phoneFieldAdmin = new JTextField(15);
        addPhoneButtonAdmin = new JButton("Add");
        addPhoneButtonAdmin.setActionCommand("AddPhone");
        phonePanel.add(phoneFieldAdmin);
        phonePanel.add(addPhoneButtonAdmin);
        panel.add(phonePanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel removePhoneLabel = new JLabel("Phone ID to Remove:");
        removePhoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(removePhoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel removePhonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        removePhonePanel.setBackground(Color.decode("#bfbfb2"));
        phoneIdToRemoveFieldAdmin = new JTextField(15);
        removePhoneButtonAdmin = new JButton("Remove");
        removePhoneButtonAdmin.setActionCommand("RemovePhone");
        removePhonePanel.add(phoneIdToRemoveFieldAdmin);
        removePhonePanel.add(removePhoneButtonAdmin);
        panel.add(removePhonePanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.decode("#bfbfb2"));
        
        updateUserButtonAdmin = new JButton("Update User");
        updateUserButtonAdmin.setActionCommand("UpdateUser");
        updateUserButtonAdmin.setFont(new Font("Arial", Font.PLAIN, 14));
        
        deleteAccountButtonAdmin = new JButton("Delete Account");
        deleteAccountButtonAdmin.setActionCommand("DeleteAccount");
        deleteAccountButtonAdmin.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteAccountButtonAdmin.setForeground(Color.RED);
        
        buttonPanel.add(updateUserButtonAdmin);
        buttonPanel.add(deleteAccountButtonAdmin);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }

    private JPanel createEditPanelUser() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#bfbfb2"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Edit My Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(firstNameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        firstNameFieldUser = new JTextField(20);
        panel.add(firstNameFieldUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lastNameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        lastNameFieldUser = new JTextField(20);
        panel.add(lastNameFieldUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nationalityLabel = new JLabel("Nationality:");
        nationalityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nationalityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        nationalityFieldUser = new JTextField(20);
        panel.add(nationalityFieldUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel pointsLabel = new JLabel("Points:");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(pointsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pointsFieldUser = new JTextField(20);
        pointsFieldUser.setEditable(false);
        pointsFieldUser.setBackground(Color.LIGHT_GRAY);
        panel.add(pointsFieldUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel tierTextLabel = new JLabel("Current Tier:");
        tierTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(tierTextLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        tierLabelUser = new JLabel("N/A");
        tierLabelUser.setFont(new Font("Arial", Font.BOLD, 14));
        tierLabelUser.setForeground(Color.decode("#0066cc"));
        panel.add(tierLabelUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        passwordFieldUser = new JTextField(20);
        panel.add(passwordFieldUser, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailSectionLabel = new JLabel("Emails:");
        emailSectionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(emailSectionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        emailDisplayAreaUser = new JTextArea(3, 20);
        emailDisplayAreaUser.setEditable(false);
        emailDisplayAreaUser.setBackground(Color.WHITE);
        JScrollPane emailScroll = new JScrollPane(emailDisplayAreaUser);
        panel.add(emailScroll, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel newEmailLabel = new JLabel("New Email:");
        newEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(newEmailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        emailPanel.setBackground(Color.decode("#bfbfb2"));
        emailFieldUser = new JTextField(15);
        addEmailButtonUser = new JButton("Add");
        addEmailButtonUser.setActionCommand("AddEmail");
        emailPanel.add(emailFieldUser);
        emailPanel.add(addEmailButtonUser);
        panel.add(emailPanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel removeEmailLabel = new JLabel("Email ID to Remove:");
        removeEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(removeEmailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel removeEmailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        removeEmailPanel.setBackground(Color.decode("#bfbfb2"));
        emailIdToRemoveFieldUser = new JTextField(15);
        removeEmailButtonUser = new JButton("Remove");
        removeEmailButtonUser.setActionCommand("RemoveEmail");
        removeEmailPanel.add(emailIdToRemoveFieldUser);
        removeEmailPanel.add(removeEmailButtonUser);
        panel.add(removeEmailPanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel phoneSectionLabel = new JLabel("Phone Numbers:");
        phoneSectionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(phoneSectionLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        phoneDisplayAreaUser = new JTextArea(3, 20);
        phoneDisplayAreaUser.setEditable(false);
        phoneDisplayAreaUser.setBackground(Color.WHITE);
        JScrollPane phoneScroll = new JScrollPane(phoneDisplayAreaUser);
        panel.add(phoneScroll, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel newPhoneLabel = new JLabel("New Phone:");
        newPhoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(newPhoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        phonePanel.setBackground(Color.decode("#bfbfb2"));
        phoneFieldUser = new JTextField(15);
        addPhoneButtonUser = new JButton("Add");
        addPhoneButtonUser.setActionCommand("AddPhone");
        phonePanel.add(phoneFieldUser);
        phonePanel.add(addPhoneButtonUser);
        panel.add(phonePanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel removePhoneLabel = new JLabel("Phone ID to Remove:");
        removePhoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(removePhoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel removePhonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        removePhonePanel.setBackground(Color.decode("#bfbfb2"));
        phoneIdToRemoveFieldUser = new JTextField(15);
        removePhoneButtonUser = new JButton("Remove");
        removePhoneButtonUser.setActionCommand("RemovePhone");
        removePhonePanel.add(phoneIdToRemoveFieldUser);
        removePhonePanel.add(removePhoneButtonUser);
        panel.add(removePhonePanel, gbc);
        
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.decode("#bfbfb2"));
        
        updateUserButtonUser = new JButton("Update User");
        updateUserButtonUser.setActionCommand("UpdateUser");
        updateUserButtonUser.setFont(new Font("Arial", Font.PLAIN, 14));
        
        deleteAccountButtonUser = new JButton("Delete Account");
        deleteAccountButtonUser.setActionCommand("DeleteAccount");
        deleteAccountButtonUser.setFont(new Font("Arial", Font.PLAIN, 14));
        deleteAccountButtonUser.setForeground(Color.RED);
        
        buttonPanel.add(updateUserButtonUser);
        buttonPanel.add(deleteAccountButtonUser);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }  

    public JButton getViewUserButton() { return viewUserButton; }
    public JButton getViewRecommendedButton() { return viewRecommendedButton; }
    public JButton getEditInfoButton() { return editInfoButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getRefreshTableButton() { return refreshTableButton; }
    public JButton getRefreshRecommendationsButton() { return refreshRecommendationsButton; }
    public JButton getRefreshTiersButton() { return refreshTiersButton; }
    public JButton getRefreshEmailsButton() { return refreshEmailsButton; }
    public JButton getRefreshPhonesButton() { return refreshPhonesButton; }
    
    public JButton getInsertUserButton() { return insertUserButton; }
    public JButton getDeleteUserButton() { return deleteUserButton; }
    public JButton getInsertTierButton() { return insertTierButton; }
    public JButton getDeleteTierButton() { return deleteTierButton; }
    public JButton getInsertEmailButton() { return insertEmailButton; }
    public JButton getDeleteEmailButton() { return deleteEmailButton; }
    public JButton getInsertPhoneButton() { return insertPhoneButton; }
    public JButton getDeletePhoneButton() { return deletePhoneButton; }

    public JButton getUpdateUserButton() { return updateUserButtonAdmin; }
    public JButton getLoadUserButton() { return loadUserButtonAdmin; }
    public JButton getAddEmailButton() { return addEmailButtonAdmin; }
    public JButton getRemoveEmailButton() { return removeEmailButtonAdmin; }
    public JButton getAddPhoneButton() { return addPhoneButtonAdmin; }
    public JButton getRemovePhoneButton() { return removePhoneButtonAdmin; }
    public JButton getDeleteAccountButton() { return deleteAccountButtonAdmin; }

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
    public JButton getDeleteAccountButtonUser() { return deleteAccountButtonUser; }
    
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

    public void updateUserTable(List<User> users) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getNationality(),
                user.getPoints(),
                user.isAdmin() ? "ADMIN" : (user.getPointsTier() != null ? user.getPointsTier().getTierName() : "No Tier"),
                user.getPassword(),
                user.isAdmin() ? "TRUE" : "FALSE",
                user.getLocationsShared(),
                user.getReviewsMade(),
                user.getBookingsCount()
            };
            userTableModel.addRow(row);
        }
    }
    
    public void updateTiersTable(List<PointsTier> tiers) {
        tiersTableModel.setRowCount(0);
        for (PointsTier tier : tiers) {
            Object[] row = {
                tier.getTierId(),
                tier.getTierName(),
                tier.getMinPoints(),
                tier.getMaxPoints()
            };
            tiersTableModel.addRow(row);
        }
    }
    
    public void updateEmailsTable(List<UserEmail> emails) {
        emailsTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserEmail email : emails) {
            Object[] row = {
                email.getEmailId(),
                email.getUserId(),
                email.getEmail(),
                email.getDateAdded() != null ? dateFormat.format(email.getDateAdded()) : "N/A"
            };
            emailsTableModel.addRow(row);
        }
    }
    
    public void updatePhonesTable(List<UserPhone> phones) {
        phonesTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserPhone phone : phones) {
            Object[] row = {
                phone.getPhoneId(),
                phone.getUserId(),
                phone.getPhoneNumber(),
                phone.getDateAdded() != null ? dateFormat.format(phone.getDateAdded()) : "N/A"
            };
            phonesTableModel.addRow(row);
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