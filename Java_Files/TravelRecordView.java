import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TravelRecordView extends JPanel{
    // Inputs
    private JTextField tfLocationId, tfUserId, tfSpotname, tfDateShared, tfBasePrice, tfMaxCap, tfStatus;
    private JComboBox<IdName> cbCountry, cbRegion, cbCity;
    private JList<IdName> listCategory;

    // Buttons
    private JButton bAdd, bUpdate, bDelete, bRefresh, bBack;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Main View
    public TravelRecordView(JPanel cardPanel) {
        cardPanel.add(this, MainDBViewer.TRAVEL_LINK);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        formPanel.setBorder(BorderFactory.createTitledBorder("Travel Spot Form"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;


        tfLocationId = new JTextField();
        tfLocationId.setEditable(false); // locationId should never be edited

        tfUserId = new JTextField();
        tfUserId.setEditable(false); // UserID comes frm login, no need to be edited

        tfSpotname = new JTextField();

        tfDateShared = new JTextField();
        tfDateShared.setEditable(false); // Date shared should never be edited

        tfBasePrice = new JTextField();
        tfMaxCap = new JTextField();
        
        tfStatus = new JTextField();
        tfStatus.setEditable(false); // automatic to be available or not

        cbCountry = new JComboBox<>();
        cbRegion = new JComboBox<>();
        cbCity = new JComboBox<>();

        listCategory = new JList<>();
        listCategory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane catdropdown = new JScrollPane(listCategory);

        addRow(formPanel, gbc, "Location ID:", tfLocationId);
        addRow(formPanel, gbc, "User ID:", tfUserId);
        addRow(formPanel, gbc, "Spot Name:", tfSpotname);
        addRow(formPanel, gbc, "Date Shared [YYYY-MM-DD]:", tfDateShared);
        addRow(formPanel, gbc, "Country:", cbCountry);
        addRow(formPanel, gbc, "Region:", cbRegion);
        addRow(formPanel, gbc, "City:", cbCity);
        addRow(formPanel, gbc, "<html>Categories <font size='3'>(Ctrl+Click)</font>:</html>", catdropdown);
        addRow(formPanel, gbc, "Base Price:", tfBasePrice);
        addRow(formPanel, gbc, "Max Capacity:", tfMaxCap);
        addRow(formPanel, gbc, "Status:", tfStatus);

        JScrollPane leftScroll = new JScrollPane(formPanel);
        leftScroll.setPreferredSize(new Dimension(350, 400)); //400, 600
        formPanel.setBackground(Color.GRAY);
        
        add(leftScroll, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[] {
            "ID", "User", "Spotname", "Date", "City", "Region", "Country", "Base Price", "Max Cap", "Status", "Categories"
        }, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.getViewport().setBackground(Color.decode("#bfbfb2"));
        add(tableScroll, BorderLayout.CENTER);

        table.getTableHeader().setBackground(Color.decode("#bfbfb2"));
        table.getTableHeader().setOpaque(true);


        // Buttons
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

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

    private void addRow(JPanel panel, GridBagConstraints gbc, String labelText, Component comp) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        comp.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(comp, gbc);

        gbc.gridy++;
    }

    public void selectCategories(List<Integer> categoryIds) {
        JList<IdName> list = getCategoriesList();
        ListModel<IdName> lm = list.getModel();

        list.clearSelection();

        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < lm.getSize(); i++) {
            IdName item = lm.getElementAt(i);
            if (categoryIds.contains(item.getId())) {
                idx.add(i);
            }
        }

        int[] arr = idx.stream().mapToInt(i -> i).toArray();
        list.setSelectedIndices(arr);
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
    public String getAvailability() {return tfStatus.getText();}

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
    public void setAvailability(String s) {tfStatus.setText(s);}

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
