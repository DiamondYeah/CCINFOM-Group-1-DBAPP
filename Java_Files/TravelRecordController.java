import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

// Class is the main controller for the Travel Records
public class TravelRecordController {

    // ADD viewer and models here
    private TravelRecordModel model;
    private TravelRecordView view;
    private MainDBController maincon;
  
    public TravelRecordController(java.sql.Connection conn, MainDBController maincon, javax.swing.JPanel cardPanel){
        this.model = new TravelRecordModel(conn);
        this.maincon = maincon;
        this.view = new TravelRecordView(cardPanel);
        addListeners();
        loadInitData();
        refreshTable();
    }

    private void loadInitData() {
        try {
            List<IdName> countries = model.getCountries();
            view.setCountries(countries);

            List<IdName> cats = model.getCategories();
            view.setCategories(cats);

            if (!countries.isEmpty()) {
                IdName firstCountry = countries.get(0);
                List<IdName> regions = model.getRegions(firstCountry.getId());
                view.setRegions(regions);
                if (!regions.isEmpty()) {
                    List<IdName> cities = model.getCities(regions.get(0).getId());
                    view.setCities(cities);
                }
                else {
                    view.setCities(new ArrayList<>());
                }
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading initial data: " + ex.getMessage());
        }
    }

    private void loadCategoriesForSpot(int locationId) {
        try {
            List<Integer> catIds = model.getCategoryOfSpot(locationId);
            
            JList<IdName> list = view.getCategoriesList();
            ListModel<IdName> lm = list.getModel();

            // unselecting
            list.clearSelection();

            java.util.List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < lm.getSize(); i++) {
                IdName item = lm.getElementAt(i);
                if (catIds.contains(item.getId()))
                    idx.add(i);
            }

            int[] arr = idx.stream().mapToInt(i -> i).toArray();
            list.setSelectedIndices(arr);
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading categories.");
        }
    }


    private void addListeners() {
        // country to regions
        view.getCountriesCB().addActionListener(e -> {
            IdName selected = (IdName) view.getCountriesCB().getSelectedItem();
            if (selected != null) {
                try {
                    List<IdName> regions = model.getRegions(selected.getId());
                    view.setRegions(regions);
                    if (!regions.isEmpty()) {
                        List<IdName> cities = model.getCities(regions.get(0).getId());
                        view.setCities(cities);
                    }
                    else {
                        view.setCities(new ArrayList<>());
                    }
                }
                catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading regions: " + ex.getMessage());
                }
            }
        });

        // region to cities
        view.getRegionsCB().addActionListener(e -> {
            IdName selected = (IdName) view.getRegionsCB().getSelectedItem();
            if (selected != null) {
                try {
                    List<IdName> cities = model.getCities(selected.getId());
                    view.setCities(cities);
                }
                catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading cities: " + ex.getMessage());
                }
            }
        });

        // ADD
        view.getAddB().addActionListener(e -> {
        try {
            int userId = maincon.getCurrentUser().getUserId();
            String spotname = view.getSpotname();

            java.sql.Date dateShared = new java.sql.Date(System.currentTimeMillis());
            view.setDateShared(dateShared.toString());

            IdName city = (IdName) view.getCitiesCB().getSelectedItem();
            if (city == null)
                throw new IllegalArgumentException("Please select a city.");
            int cityId = city.getId();
            double basePrice = Double.parseDouble(view.getBasePrice());
            int maxCap = Integer.parseInt(view.getMaxCap());

            TravelRecord tr = new TravelRecord(userId, spotname, dateShared, cityId, basePrice, maxCap);

            // categories
            List<IdName> selected = view.getCategoriesList().getSelectedValuesList();
            List<Integer> catIds = new ArrayList<>();
            for (IdName in : selected)
                catIds.add(in.getId());
            int newId = model.addTravelSpot(tr, catIds);

            // Confirm travel spot was added
            JOptionPane.showMessageDialog(null, "Travel spot added.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error adding travel spot: " + ex.getMessage());
        }
        });

        // UPDATE
        view.getUpdateB().addActionListener(e -> {
        try {
            int locationId = Integer.parseInt(view.getLocationId());
            int userId = maincon.getCurrentUser().getUserId();
            String spotname = view.getSpotname();

            // date shared
            TravelRecord exists = model.getTravelSpot(locationId);
            if (exists == null)
                throw new IllegalArgumentException("Record not found");
            java.sql.Date dateShared = exists.getDateShared();

            IdName city = (IdName) view.getCitiesCB().getSelectedItem();
            if (city == null)
                throw new IllegalArgumentException("Please select a city.");
            int cityId = city.getId();
            double basePrice = Double.parseDouble(view.getBasePrice());
            int maxCap = Integer.parseInt(view.getMaxCap());

            TravelRecord tr = new TravelRecord(locationId, userId, spotname, dateShared, cityId, basePrice, maxCap);
            model.updTravelSpot(tr);

            // updating categories
            List<IdName> selected = view.getCategoriesList().getSelectedValuesList();
            List<Integer> catIds = new ArrayList<>();
            for (IdName c : selected)
                catIds.add(c.getId());
            model.updTravelSpotCategories(locationId, catIds);

            JOptionPane.showMessageDialog(null, "Travel spot updated.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error updating travel spot.");
        }
        });

        // Delete
        view.getDeleteB().addActionListener(e -> {
        try {
            int id = Integer.parseInt(view.getLocationId());
            model.deleteTravelSpot(id);
            JOptionPane.showMessageDialog(null, "Travel spot deleted.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error deleting travel spot.");
        }
        });

        // Refresh
        view.getRefreshB().addActionListener(e -> refreshTable());

        // auto fill fields
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    view.setLocationId(view.getTable().getValueAt(row, 0).toString());
                    view.setUserId(view.getTable().getValueAt(row, 1).toString());
                    view.setSpotname(view.getTable().getValueAt(row, 2).toString());
                    view.setDateShared(view.getTable().getValueAt(row, 3).toString());

                    String cityName = view.getTable().getValueAt(row, 4).toString();
                    view.selectCity(cityName);

                    view.setBasePrice(view.getTable().getValueAt(row, 7).toString());
                    view.setMaxCap(view.getTable().getValueAt(row, 8).toString());

                    int locationId = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
                    loadCategoriesForSpot(locationId);
                }
            }
        });

        // Back
        view.getBackB().addActionListener(e -> {
            maincon.getMainDBViewer().showPanel(MainDBViewer.MAIN_LINK);
        });
    }

    // Refreshing the table
    private void refreshTable() {
        try {
            DefaultTableModel tm = view.getTableModel();
            tm.setRowCount(0);
            List<Object[]> rows = model.getAllTravelSpotsDetailed();
            for (Object[] r : rows) {
                tm.addRow(new Object[] {
                    r[0], r[1], r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[9]
                });
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading travel spots.");
        }
    }

    // Getting the userId after a successful login
    public void updateLoggedUser(int userId) {
        view.setUserId(String.valueOf(userId));
    }
}
