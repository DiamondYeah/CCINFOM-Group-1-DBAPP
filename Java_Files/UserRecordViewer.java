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

        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel userTablePanel = new JPanel(new BorderLayout(5, 5));
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
        refreshTableButton = new JButton("Refresh");
        refreshTableButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshTableButton.setActionCommand("RefreshUserTable");
        insertUserButton = new JButton("Insert User");
        insertUserButton.setFont(new Font("Arial", Font.PLAIN, 12));
        insertUserButton.setActionCommand("InsertUser");
        deleteUserButton = new JButton("Delete User");
        deleteUserButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteUserButton.setActionCommand("DeleteUser");
        userButtonPanel.add(refreshTableButton);
        userButtonPanel.add(insertUserButton);
        userButtonPanel.add(deleteUserButton);
        userTablePanel.add(userButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Users", userTablePanel);
        
        JPanel tiersTablePanel = new JPanel(new BorderLayout(5, 5));
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
        refreshTiersButton = new JButton("Refresh");
        refreshTiersButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshTiersButton.setActionCommand("RefreshTiers");
        insertTierButton = new JButton("Insert Tier");
        insertTierButton.setFont(new Font("Arial", Font.PLAIN, 12));
        insertTierButton.setActionCommand("InsertTier");
        deleteTierButton = new JButton("Delete Tier");
        deleteTierButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteTierButton.setActionCommand("DeleteTier");
        tiersButtonPanel.add(refreshTiersButton);
        tiersButtonPanel.add(insertTierButton);
        tiersButtonPanel.add(deleteTierButton);
        tiersTablePanel.add(tiersButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Points Tiers", tiersTablePanel);
        
        JPanel emailsTablePanel = new JPanel(new BorderLayout(5, 5));
        String[] emailColumns = {"Email ID", "User ID", "Email"};
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
        refreshEmailsButton = new JButton("Refresh");
        refreshEmailsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshEmailsButton.setActionCommand("RefreshEmails");
        insertEmailButton = new JButton("Insert Email");
        insertEmailButton.setFont(new Font("Arial", Font.PLAIN, 12));
        insertEmailButton.setActionCommand("InsertEmail");
        deleteEmailButton = new JButton("Delete Email");
        deleteEmailButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteEmailButton.setActionCommand("DeleteEmail");
        emailsButtonPanel.add(refreshEmailsButton);
        emailsButtonPanel.add(insertEmailButton);
        emailsButtonPanel.add(deleteEmailButton);
        emailsTablePanel.add(emailsButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("User Emails", emailsTablePanel);
        
        JPanel phonesTablePanel = new JPanel(new BorderLayout(5, 5));
        String[] phoneColumns = {"Phone ID", "User ID", "Phone Number"};
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
        refreshPhonesButton = new JButton("Refresh");
        refreshPhonesButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshPhonesButton.setActionCommand("RefreshPhones");
        insertPhoneButton = new JButton("Insert Phone");
        insertPhoneButton.setFont(new Font("Arial", Font.PLAIN, 12));
        insertPhoneButton.setActionCommand("InsertPhone");
        deletePhoneButton = new JButton("Delete Phone");
        deletePhoneButton.setFont(new Font("Arial", Font.PLAIN, 12));
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
        
        deleteAccountButtonAdmin = new JButton("Delete Account");
        deleteAccountButtonAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        deleteAccountButtonAdmin.setForeground(Color.RED);
        deleteAccountButtonAdmin.setActionCommand("DeleteAccount");
        buttonPanel.add(deleteAccountButtonAdmin);
        
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
        
        deleteAccountButtonUser = new JButton("Delete Account");
        deleteAccountButtonUser.setFont(new Font("Arial", Font.BOLD, 16));
        deleteAccountButtonUser.setForeground(Color.RED);
        deleteAccountButtonUser.setActionCommand("DeleteAccount");
        buttonPanel.add(deleteAccountButtonUser);
        
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
        for (UserEmail email : emails) {
            Object[] row = {
                email.getEmailId(),
                email.getUserId(),
                email.getEmail()
            };
            emailsTableModel.addRow(row);
        }
    }
    
    public void updatePhonesTable(List<UserPhone> phones) {
        phonesTableModel.setRowCount(0);
        for (UserPhone phone : phones) {
            Object[] row = {
                phone.getPhoneId(),
                phone.getUserId(),
                phone.getPhoneNumber()
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