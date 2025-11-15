import java.sql.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class FeedbackRecordViewer extends JPanel{

    //Main Layout that combines everything
    private JPanel mainPanel;

    //Panels to add diff components to mainPanel
    private JPanel panelLeft;
    private JPanel panelRight;
    

    //Components for DB APP Buttons
    private JButton recordViewButton; // Button links to the feedback table view
    private JButton reportViewButton; // Button links to the report table view
    private JButton createFeedbackButton; // Button links to the feedback creation view
    private JButton quitButton; // Button returns user to main menu


    public FeedbackRecordViewer(JPanel cardPanel){

        setLayout(new BorderLayout()); //Layout
        setPreferredSize(new Dimension(1500, 920));
        setMaximumSize(new Dimension(1500, 920));
        setMinimumSize(new Dimension(1500, 920));



        cardPanel.add(this, MainDBViewer.FEEDBACK_LINK);


    }

    
}
