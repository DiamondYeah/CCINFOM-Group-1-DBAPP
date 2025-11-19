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
        //view.setUserId(String.valueOf(maincon.getCurrentUser().getUserId()));
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

    // Action Listeners =========================
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
            int userId = maincon.getCurrentUser().getUserId(); //Integer.parseInt(view.getUserId()); //maincon.getCurrentUser().getUserId();
            String spotname = view.getSpotname();

            java.sql.Date dateShared = new java.sql.Date(System.currentTimeMillis());
            view.setDateShared(dateShared.toString());

            IdName city = (IdName) view.getCitiesCB().getSelectedItem();
            if (city == null)
                throw new IllegalArgumentException("Please select a city.");
            int cityId = city.getId();
            double basePrice = Double.parseDouble(view.getBasePrice());
            int maxCap = Integer.parseInt(view.getMaxCap());

            String availability = "Available";

            TravelRecord tr = new TravelRecord(userId, spotname, dateShared, cityId, basePrice, maxCap, availability);

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

            String availability = exists.getAvailability();

            TravelRecord tr = new TravelRecord(locationId, userId, spotname, dateShared, cityId, basePrice, maxCap, availability);
            model.updTravelSpot(tr);

            // updating categories
            List<IdName> selected = view.getCategoriesList().getSelectedValuesList();
            List<Integer> catIds = new ArrayList<>();
            for (IdName c : selected)
                catIds.add(c.getId());
            model.updTravelSpotCategories(locationId, catIds);

            model.updateAvailability(locationId);
            JOptionPane.showMessageDialog(null, "Travel spot updated.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error updating travel spot: " + ex.getMessage());
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
            JOptionPane.showMessageDialog(null, "Error deleting travel spot: " + ex.getMessage());
        }
        });

        // Refresh
        view.getRefreshB().addActionListener(e -> refreshTable());

        // gem popularity report
        view.getPopularityReportB().addActionListener(e -> showPopularityReport());

        // Your Posts
        view.getYourPostsB().addActionListener(e -> showYourPosts());
        
        // View Poster Info
        view.getViewPosterB().addActionListener(e -> showPosterInfo());
        
        // View Spot Ratings
        view.getViewSpotRatingsB().addActionListener(e -> showSpotRatings());

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
                    view.setAvailability(view.getTable().getValueAt(row, 9).toString());

                    int locationId = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
                    try {
                        List<Integer> catIds = model.getCategoryOfSpot(locationId);
                        view.selectCategories(catIds);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error loading categories: : " + ex.getMessage());
                    }
                }
            }
        });

        // Back
        view.getBackB().addActionListener(e -> {
            maincon.getMainDBViewer().showPanel(MainDBViewer.MAIN_LINK);
        });
    }

    // Report methods
    private void showPopularityReport() {
        JPanel panel = new JPanel(new java.awt.GridLayout(3, 2, 5, 5));
        JTextField tfMonth = new JTextField();
        JTextField tfYear = new JTextField();
        
        panel.add(new JLabel("Month (1-12):"));
        panel.add(tfMonth);
        panel.add(new JLabel("Year (e.g., 2025):"));
        panel.add(tfYear);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Month and Year", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int month = Integer.parseInt(tfMonth.getText());
                int year = Integer.parseInt(tfYear.getText());
                
                if (month < 1 || month > 12) {
                    JOptionPane.showMessageDialog(null, "Month must be between 1 and 12.");
                    return;
                }
                
                List<Object[]> reportData = model.getPopularityReport(month, year);
                
                // Create popup window with report
                JFrame reportFrame = new JFrame("Hidden Gem Popularity Report - " + month + "/" + year);
                reportFrame.setSize(900, 500);
                reportFrame.setLocationRelativeTo(null);
                
                String[] columns = {"Location ID", "Spot Name", "Total Bookings", "Avg Rating", "Likes", "Dislikes", "Popularity Score"};
                DefaultTableModel reportModel = new DefaultTableModel(columns, 0);
                
                for (Object[] row : reportData) {
                    reportModel.addRow(row);
                }
                
                JTable reportTable = new JTable(reportModel);
                JScrollPane scrollPane = new JScrollPane(reportTable);
                reportFrame.add(scrollPane);
                reportFrame.setVisible(true);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for month and year.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error loading popularity report: " + ex.getMessage());
            }
        }
    }
    
    private void showYourPosts() {
        try {
            int userId = maincon.getCurrentUser().getUserId();
            List<Object[]> userSpots = model.getUserPostedSpots(userId);
            
            DefaultTableModel tm = view.getTableModel();
            tm.setRowCount(0);
            
            for (Object[] r : userSpots) {
                tm.addRow(new Object[] {
                    r[0], r[1], r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[9], r[10]
                });
            }
            
            JOptionPane.showMessageDialog(null, "Showing only your posted spots. Click REFRESH to see all spots.");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading your posts: " + ex.getMessage());
        }
    }
    
    private void showPosterInfo() {
        try {
            int row = view.getTable().getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Please select a travel spot first.");
                return;
            }
            
            int userId = Integer.parseInt(view.getTable().getValueAt(row, 1).toString());
            Object[] posterInfo = model.getPosterInfo(userId);
            
            if (posterInfo != null) {
                String message = String.format(
                    "Poster Information:\n\n" +
                    "User ID: %d\n" +
                    "Name: %s %s\n" +
                    "Points: %d",
                    posterInfo[0], posterInfo[1], posterInfo[2], posterInfo[3]
                );
                
                JOptionPane.showMessageDialog(null, message, "Poster Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "User information not found.");
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading poster info: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please select a valid travel spot.");
        }
    }
    
    private void showSpotRatings() {
        try {
            int row = view.getTable().getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Please select a travel spot first.");
                return;
            }
            
            int locationId = Integer.parseInt(view.getTable().getValueAt(row, 0).toString());
            String spotName = view.getTable().getValueAt(row, 2).toString();
            
            // Get ratings
            List<Object[]> ratings = model.getSpotRatings(locationId);
            
            // popup window
            JFrame ratingsFrame = new JFrame("Ratings for: " + spotName);
            ratingsFrame.setSize(900, 400);
            ratingsFrame.setLocationRelativeTo(null);
            ratingsFrame.setLayout(new java.awt.BorderLayout());
            
            JTabbedPane tabbedPane = new JTabbedPane();
            
            // Ratings tab
            String[] ratingColumns = {"Review ID", "Reviewer", "Rating", "Recommended", "Review Date", "Likes", "Dislikes"};
            DefaultTableModel ratingModel = new DefaultTableModel(ratingColumns, 0);
            for (Object[] r : ratings) {
                ratingModel.addRow(r);
            }
            JTable ratingTable = new JTable(ratingModel);
            JScrollPane ratingScroll = new JScrollPane(ratingTable);
            tabbedPane.addTab("Ratings & Reviews", ratingScroll);
            
            ratingsFrame.add(ratingScroll, java.awt.BorderLayout.CENTER);
            ratingsFrame.setVisible(true);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading spot ratings: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please select a valid travel spot.");
        }
    }


    // Refreshing the table
    private void refreshTable() {
        try {
            DefaultTableModel tm = view.getTableModel();
            tm.setRowCount(0);
            List<Object[]> rows = model.getAllTravelSpotsDetailed();
            for (Object[] r : rows) {
                tm.addRow(new Object[] {
                    r[0], r[1], r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[9], r[10]
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
