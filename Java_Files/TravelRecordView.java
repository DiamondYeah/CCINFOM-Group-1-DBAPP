import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TravelRecordView extends JPanel{
    // Inputs
    private JTextField tfLocationId, tfUserId, tfSpotname, tfDateShared, tfBasePrice, tfMaxCap;
    private JComboBox<IdName> cbCountry, cbRegion, cbCity;
    private JList<IdName> listCategory;

    // Buttons
    private JButton bAdd, bUpdate, bDelete, bRefresh, bBack;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    //public static final String TravelRecordLink = "TRAVEL RECORD";

    // Main View
    public TravelRecordView(JPanel cardPanel) {
        cardPanel.add(this, MainDBViewer.TRAVEL_LINK);

        setLayout(new BorderLayout());

        // Input/Top
        JPanel topPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Travel Spots"));

        tfLocationId = new JTextField();
        tfLocationId.setEditable(false); // locationId should never be edited
        tfUserId = new JTextField();
        tfSpotname = new JTextField();
        tfDateShared = new JTextField();
        tfDateShared.setEditable(false); // Date shared should never be edited
        tfBasePrice = new JTextField();
        tfMaxCap = new JTextField();

        cbCountry = new JComboBox<>();
        cbRegion = new JComboBox<>();
        cbCity = new JComboBox<>();

        listCategory = new JList<>();
        listCategory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane catdropdown = new JScrollPane(listCategory);
        catdropdown.setPreferredSize(new Dimension(200, 80));

        topPanel.add(new JLabel("Location ID (for upd/del):"));
        topPanel.add(tfLocationId);

        topPanel.add(new JLabel("User ID:"));
        topPanel.add(tfUserId);

        topPanel.add(new JLabel("Spot name:"));
        topPanel.add(tfSpotname);

        topPanel.add(new JLabel("Date Shared [YYYY-MM-DD]:"));
        topPanel.add(tfDateShared);

        topPanel.add(new JLabel("City:"));
        topPanel.add(cbCity);

        topPanel.add(new JLabel("Region:"));
        topPanel.add(cbRegion);

        topPanel.add(new JLabel("Country:"));
        topPanel.add(cbCountry);

        topPanel.add(new JLabel("Categories:"));
        topPanel.add(catdropdown);
        
        topPanel.add(new JLabel("Base Price:"));
        topPanel.add(tfBasePrice);

        topPanel.add(new JLabel("Max Capacity:"));
        topPanel.add(tfMaxCap);

        add(topPanel, BorderLayout.NORTH);


        // Table
        tableModel = new DefaultTableModel(new String[] {
            "ID", "User", "Spotname", "Date", "City", "Region", "Country", "Base Price", "Max Cap", "Categories"
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
    public JTable getTable() {return table;}

    // User Input
    public String getLocationId() {return tfLocationId.getText();}
    public String getUserId() {return tfUserId.getText();}
    public String getSpotname() {return tfSpotname.getText();}
    public String getDateShared() {return tfDateShared.getText();}
    public String getBasePrice() {return tfBasePrice.getText();}
    public String getMaxCap() {return tfMaxCap.getText();}

    public JComboBox<IdName> getCountriesCB() {return cbCountry;}
    public JComboBox<IdName> getRegionsCB() {return cbRegion;}
    public JComboBox<IdName> getCitiesCB() {return cbCity;}
    public JList<IdName> getCategoriesList() {return listCategory;}

    public DefaultTableModel getTableModel() {return tableModel;}

    // Setters
    public void setLocationId(String s) {tfLocationId.setText(s);}
    public void setUserId(String s) {tfUserId.setText(s);}
    public void setSpotname(String s) {tfSpotname.setText(s);}
    public void setDateShared(String s) {tfDateShared.setText(s);}
    public void setBasePrice(String s) {tfBasePrice.setText(s);}
    public void setMaxCap(String s) {tfMaxCap.setText(s);}

    public void selectCity(String name) {
        for (int i = 0; i < cbCity.getItemCount(); i++) {
            if (cbCity.getItemAt(i).getName().equals(name)) {
                cbCity.setSelectedIndex(i);
                break;
            }
        }
    }

    // helpers
    public void setCountries(List<IdName> countries) {
        DefaultComboBoxModel<IdName> m = new DefaultComboBoxModel<>();
        for (IdName co : countries)
            m.addElement(co);
        cbCountry.setModel(m);
    }

    public void setRegions(List<IdName> regions) {
        DefaultComboBoxModel<IdName> m = new DefaultComboBoxModel<>();
        for (IdName r : regions)
            m.addElement(r);
        cbRegion.setModel(m);
    }

    public void setCities(List<IdName> cities) {
        DefaultComboBoxModel<IdName> m = new DefaultComboBoxModel<>();
        for (IdName ci : cities)
            m.addElement(ci);
        cbCity.setModel(m);
    }

    public void setCategories(List<IdName> categories) {
        DefaultComboBoxModel<IdName> m = new DefaultComboBoxModel<>();
        for (IdName cat : categories)
            m.addElement(cat);
        listCategory.setModel(m);
    }
    
}
