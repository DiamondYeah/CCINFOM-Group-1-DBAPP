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
    private static final String EDIT_CARD = "Edit";
    
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JScrollPane userScrollPane;

    private JTable recommendationsTable;
    private DefaultTableModel recommendationsTableModel;
    private JScrollPane recommendationsScrollPane;

    private JTextField userIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField nationalityField;
    private JTextField pointsField;
    private JLabel tierLabel;
    
    private JTextField emailField;
    private JTextField emailIdToRemoveField;

    private JTextField phoneField;
    private JTextField phoneIdToRemoveField;
    
    private JTextArea emailDisplayArea;
    private JTextArea phoneDisplayArea;

    private JButton viewUserButton;
    private JButton viewRecommendedButton;
    private JButton editInfoButton;
    private JButton backButton;
    private JButton refreshTableButton;
    private JButton refreshRecommendationsButton;
    private JButton updateUserButton;
    private JButton loadUserButton;
    private JButton addEmailButton;
    private JButton removeEmailButton;
    private JButton addPhoneButton;
    private JButton removePhoneButton;

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
        rightPanel.add(createEditPanel(), EDIT_CARD);
        
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

        JLabel titleLabel = new JLabel("User Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"User ID", "First Name", "Last Name", "Nationality", "Points", "Tier"};
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

        JLabel titleLabel = new JLabel("Recommended Travel Spots Rating Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Location ID", "Area", "City", "Region", "Country", "Date Shared", "Reviews", "Avg Rating"};
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

    private JPanel createEditPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Edit User Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userIdLabel = new JLabel("Enter User ID:");
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(userIdLabel, gbc);
        
        gbc.gridx = 1;
        userIdField = new JTextField(15);
        formPanel.add(userIdField, gbc);
        
        gbc.gridx = 2;
        loadUserButton = new JButton("Load User");
        loadUserButton.setActionCommand("LoadUser");
        loadUserButton.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(loadUserButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        firstNameField = new JTextField(20);
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        lastNameField = new JTextField(20);
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nationalityField = new JTextField(20);
        formPanel.add(nationalityField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Points:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        pointsField = new JTextField(20);
        formPanel.add(pointsField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Current Tier:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        tierLabel = new JLabel("N/A");
        tierLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tierLabel.setForeground(new Color(0, 102, 204));
        formPanel.add(tierLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 3;
        JLabel emailSectionLabel = new JLabel("Emails:");
        emailSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(emailSectionLabel, gbc);

        gbc.gridy = 9;
        emailDisplayArea = new JTextArea(3, 30);
        emailDisplayArea.setEditable(false);
        emailDisplayArea.setBackground(new Color(240, 240, 240));
        JScrollPane emailScroll = new JScrollPane(emailDisplayArea);
        formPanel.add(emailScroll, gbc);

        gbc.gridy = 10; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        gbc.gridx = 2;
        addEmailButton = new JButton("Add");
        addEmailButton.setActionCommand("AddEmail");
        formPanel.add(addEmailButton, gbc);

        gbc.gridy = 11;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Email ID to Remove:"), gbc);
        gbc.gridx = 1;
        emailIdToRemoveField = new JTextField(15);
        formPanel.add(emailIdToRemoveField, gbc);
        gbc.gridx = 2;
        removeEmailButton = new JButton("Remove");
        removeEmailButton.setActionCommand("RemoveEmail");
        formPanel.add(removeEmailButton, gbc);

        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 3;
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 3;
        JLabel phoneSectionLabel = new JLabel("Phone Numbers:");
        phoneSectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(phoneSectionLabel, gbc);

        gbc.gridy = 14;
        phoneDisplayArea = new JTextArea(3, 30);
        phoneDisplayArea.setEditable(false);
        phoneDisplayArea.setBackground(new Color(240, 240, 240));
        JScrollPane phoneScroll = new JScrollPane(phoneDisplayArea);
        formPanel.add(phoneScroll, gbc);

        gbc.gridy = 15; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        gbc.gridx = 2;
        addPhoneButton = new JButton("Add");
        addPhoneButton.setActionCommand("AddPhone");
        formPanel.add(addPhoneButton, gbc);

        gbc.gridy = 16;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Phone ID to Remove:"), gbc);
        gbc.gridx = 1;
        phoneIdToRemoveField = new JTextField(15);
        formPanel.add(phoneIdToRemoveField, gbc);
        gbc.gridx = 2;
        removePhoneButton = new JButton("Remove");
        removePhoneButton.setActionCommand("RemovePhone");
        formPanel.add(removePhoneButton, gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        panel.add(formScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateUserButton = new JButton("Update User");
        updateUserButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateUserButton.setActionCommand("UpdateUser");
        buttonPanel.add(updateUserButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JButton getViewUserButton() { return viewUserButton; }
    public JButton getViewRecommendedButton() { return viewRecommendedButton; }
    public JButton getEditInfoButton() { return editInfoButton; }
    public JButton getBackButton() { return backButton; }
    public JButton getRefreshTableButton() { return refreshTableButton; }
    public JButton getRefreshRecommendationsButton() { return refreshRecommendationsButton; }
    public JButton getUpdateUserButton() { return updateUserButton; }
    public JButton getLoadUserButton() { return loadUserButton; }
    public JButton getAddEmailButton() { return addEmailButton; }
    public JButton getRemoveEmailButton() { return removeEmailButton; }
    public JButton getAddPhoneButton() { return addPhoneButton; }
    public JButton getRemovePhoneButton() { return removePhoneButton; }

    public JTextField getUserIdField() { return userIdField; }
    public JTextField getFirstNameField() { return firstNameField; }
    public JTextField getLastNameField() { return lastNameField; }
    public JTextField getNationalityField() { return nationalityField; }
    public JTextField getPointsField() { return pointsField; }
    public JLabel getTierLabel() { return tierLabel; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getEmailIdToRemoveField() { return emailIdToRemoveField; }
    public JTextField getPhoneField() { return phoneField; }
    public JTextField getPhoneIdToRemoveField() { return phoneIdToRemoveField; }
    public JTextArea getEmailDisplayArea() { return emailDisplayArea; }
    public JTextArea getPhoneDisplayArea() { return phoneDisplayArea; }

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
    public String getEditCard() { return EDIT_CARD; }
}