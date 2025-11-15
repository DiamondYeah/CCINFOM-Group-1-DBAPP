import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TravelRecordView extends JPanel{
    // Inputs
    private JTextField tfLocationId, tfUserId, tfArea, tfAvailability, tfDateShared, tfCityId, tfBasePrice, tfMaxCap;

    // Buttons
    private JButton bAdd, bUpdate, bDelete, bRefresh, bBack;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    public static final String TravelRecordLink = "TRAVEL RECORD";

    // Main View
    public TravelRecordView(JPanel cardPanel) {
        cardPanel.add(this, MainDBViewer.TRAVEL_LINK);

        setLayout(new BorderLayout());

        // Input/Top
        JPanel topPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Travel Spots"));

        tfLocationId = new JTextField();
        tfUserId = new JTextField();
        tfArea = new JTextField();
        tfAvailability = new JTextField();
        tfDateShared = new JTextField();
        tfCityId = new JTextField();
        tfBasePrice = new JTextField();
        tfMaxCap = new JTextField();

        topPanel.add(new JLabel("Location ID:"));
        topPanel.add(tfLocationId);

        topPanel.add(new JLabel("User ID:"));
        topPanel.add(tfUserId);

        topPanel.add(new JLabel("Area:"));
        topPanel.add(tfArea);

        topPanel.add(new JLabel("Status:"));
        topPanel.add(tfAvailability);

        topPanel.add(new JLabel("Date Shared [YYYY-MM-DD]:"));
        topPanel.add(tfDateShared);

        topPanel.add(new JLabel("City ID:"));
        topPanel.add(tfCityId);
        
        topPanel.add(new JLabel("Base Price:"));
        topPanel.add(tfBasePrice);

        topPanel.add(new JLabel("Max Capacity:"));
        topPanel.add(tfMaxCap);

        add(topPanel, BorderLayout.NORTH);


        // Table
        tableModel = new DefaultTableModel(new String[] {
            "ID", "User", "Area", "Status", "Date", "City", "Base Price", "Max Cap"
        }, 0);

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);


        // Buttons
        JPanel botPanel = new JPanel();

        bAdd = new JButton("ADD");
        bUpdate = new JButton("UPDATE");
        bDelete = new JButton("DELETE");
        bRefresh = new JButton("REFRESH");
        bBack = new JButton("BACK");

        botPanel.add(bAdd);
        botPanel.add(bUpdate);
        botPanel.add(bDelete);
        botPanel.add(bRefresh);
        botPanel.add(bBack);

        add(botPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JButton getAddB() { return bAdd;}
    public JButton getUpdateB() { return bUpdate;}
    public JButton getDeleteB() { return bDelete;}
    public JButton getRefreshB() {return bRefresh;}
    public JButton getBackB() {return bBack;}

    // User Input
    public String getLocationId() {return tfLocationId.getText();}
    public String getUserId() {return tfUserId.getText();}
    public String getArea() {return tfArea.getText();}
    public String getAvailability() {return tfAvailability.getText();}
    public String getDateShared() {return tfDateShared.getText();}
    public String getCityId() {return tfCityId.getText();}
    public String getBasePrice() {return tfBasePrice.getText();}
    public String getMaxCap() {return tfMaxCap.getText();}

    public DefaultTableModel getTableModel() {return tableModel;}
}
