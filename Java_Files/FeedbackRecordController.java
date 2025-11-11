import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.Action;
import javax.swing.JPanel;

// Class is the main controller for the Feedback Records
public class FeedbackRecordController {

    private MainDBController mainController;

    // ADD viewer and models here
    private FeedbackRecord feedback;
    private FeedbackRecordViewer view;


    // Public constructor
    public FeedbackRecordController(Connection conn, MainDBController mainController, JPanel cardPanel){


        this.mainController = mainController;

        feedback = new FeedbackRecord(conn);
        view = new FeedbackRecordViewer(cardPanel);

    }


    
}
