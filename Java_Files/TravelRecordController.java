import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Class is the main controller for the Travel Records
public class TravelRecordController {

    // ADD viewer and models here
    private TravelRecordModel model;
    private TravelRecordView view;
    private MainDBController maincon;
    private JPanel cardPane;
  
    public TravelRecordController(Connection conn, MainDBController maincon, JPanel cardPane){
        this.model = new TravelRecordModel(conn);
        this.maincon = maincon;
        this.cardPane = cardPane;
        this.view = new TravelRecordView(cardPane);
        addListeners();
    }

    // Add
    private void addListeners() {

        view.getAddB().addActionListener(e -> {
        try {
            TravelRecord newRecord = new TravelRecord(
                Integer.parseInt(view.getUserId()),
                view.getArea(),
                view.getAvailability(),
                java.sql.Date.valueOf(view.getDateShared()),
                Integer.parseInt(view.getCityId()),
                Double.parseDouble(view.getBasePrice()),
                Integer.parseInt(view.getMaxCap())
            );
        
            model.addTravelSpot(newRecord);
            JOptionPane.showMessageDialog(null, "Travel spot added.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error");
        }
        });

        // Update
        view.getUpdateB().addActionListener(e -> {
        try {
            TravelRecord updated = new TravelRecord(
                Integer.parseInt(view.getLocationId()),
                Integer.parseInt(view.getUserId()),
                view.getArea(),
                view.getAvailability(),
                java.sql.Date.valueOf(view.getDateShared()),
                Integer.parseInt(view.getCityId()),
                Double.parseDouble(view.getBasePrice()),
                Integer.parseInt(view.getMaxCap())
            );
        
            model.updTravelSpot(updated);
            JOptionPane.showMessageDialog(null, "Travel spot updated.");
            refreshTable();

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error");
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
            JOptionPane.showMessageDialog(null, "Error");
        }
        });

        // Refresh
        view.getRefreshB().addActionListener(e -> refreshTable());

        // Back
        view.getBackB().addActionListener(e -> {
            maincon.getAppDBViewer().showPanel(MainDBViewer.MAIN_LINK);
        });
    }

    private void refreshTable() {
        try {
            DefaultTableModel tm = view.getTableModel();
            tm.setRowCount(0);

            for (TravelRecord tr : model.getAllTravelSpots()) {
                tm.addRow(new Object[]{
                    tr.getLocationId(),
                    tr.getUserId(),
                    tr.getArea(),
                    tr.getAvailability(),
                    tr.getDateShared(),
                    tr.getCityId(),
                    tr.getBasePrice(),
                    tr.getMaxCap()
                });
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }
}
