package netbeansdatabase;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Database {
    
    static Connection conn = null;
    static PreparedStatement pst = null;
    Statement queryStatement;
    String query;

    static ResultSet rs = null;
    static public LoginWindow login;
    static public MainMenu menu;
    static public RetrieveWindow retrieve;
    
    public Database() {
        login = new LoginWindow(this);
        menu = new MainMenu(this);
        retrieve = new RetrieveWindow(this);
        login.setVisible(true);
        menu.setVisible(false);
        retrieve.setVisible(false);
    }
    
    public void connect(String u, String p) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://triton.towson.edu:3360/bdang1db", u, p);
            queryStatement = conn.createStatement();
            JOptionPane.showMessageDialog(login, "Connected to Database");
            loginClose();
            menuOpen();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(login, "Failed to Connect");
        }
    }
    
    static void loginClose() {
        login.dispose();
    }
    
    static void menuOpen() {
        menu.setVisible(true);
    }
    
    static void retrieveOpen() {
        retrieve.setVisible(true);
        retrieve.setLocation(menu.getHeight()/2, menu.getWidth()/2);
    }
    
    public static DefaultTableModel buildTableModel()
        throws SQLException {
        
        
      
        ResultSetMetaData metaData = rs.getMetaData();
        
        
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
    }
        return new DefaultTableModel(data, columnNames);
    }
    
    static void makeStatement() {
        
    }

    public static void main(String[] args) {
        new Database();
    }
    
}
