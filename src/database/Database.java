package database;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Database {
    
    static Connection conn = null;
    static PreparedStatement pst = null;
    static Statement queryStatement;
    static String query;

    static ResultSet rs = null;
    static public LoginWindow loginWindow;
    static public MainMenu menuWindow;
    static public RetrieveWindow retrieveWindow;
    static public AddWindow addWindow;
    static public UpdateWindow updateWindow;
    
    public Database() {
        loginWindow = new LoginWindow(this);
        menuWindow = new MainMenu(this);
        retrieveWindow = new RetrieveWindow(this);
        addWindow = new AddWindow(this);
        updateWindow = new UpdateWindow(this);
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setVisible(true);
        menuWindow.setVisible(false);
        retrieveWindow.setVisible(false);
        addWindow.setVisible(false);
        updateWindow.setVisible(false);
    }
    
    public void connect(String u, String p) {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(
//                    "jdbc:mysql://triton.towson.edu:3360/swanga1db", u, p);
//            queryStatement = conn.createStatement();
//            JOptionPane.showMessageDialog(login, "Connected to Database");
            loginWindowClose();
            menuWindowOpen();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginWindow, "Failed to Connect");
        }
    }
    
    static void loginWindowClose() {
        loginWindow.dispose();
    }
    
    static void menuWindowOpen() {
        menuWindow.setLocationRelativeTo(null);
        menuWindow.setVisible(true);
    }
    
    static void retrieveWindowOpen() {
        retrieveWindow.setVisible(true);
        retrieveWindow.setLocationRelativeTo(menuWindow);
        retrieveWindow.setLocation(menuWindow.getX()+menuWindow.getWidth(), 
                menuWindow.getY());
    }
    
    static void addWindowOpen() {
        addWindow.setVisible(true);
        addWindow.setLocationRelativeTo(menuWindow);
        addWindow.setLocation(menuWindow.getX()-menuWindow.getWidth()*3, 
                menuWindow.getY());
    }
    
    static void updateWindowOpen() {
        updateWindow.setVisible(true);
        updateWindow.setLocationRelativeTo(menuWindow);
        updateWindow.setLocation(menuWindow.getX()+menuWindow.getWidth(),
                menuWindow.getY());
    }
    
    public DefaultTableModel buildTableModel() throws SQLException {
        
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
    
    static void createSelectAllStatement(String t) {
        query = "SELECT * FROM " + t;
    }
    
    static void createBasicStatement(String t, String p, String c) {
        
        query = "SELECT * FROM " + t + " WHERE " + p + " = " + c;
    }
    
    static void createBasicStatement(String t1, String p1, String c1, 
            String p2, String c2) {
        
        query = "SELECT * FROM " + t1 + ", " + " WHERE " + p1 + " = " + c1
                + " AND " + p2 + " = " + c2;
    }
    
    static void createCustomerStatement(String p, String c) {
        query = "SELECT * FROM CUSTOMER,ORDER_HISTORY WHERE"
                + " CUSTOMER_NO = CUSTOMER_ONO AND " + p + " = " + c;
//        switch (p) {
//            case "Order ID":
//                query = "SELECT * FROM CUSTOMER,ORDER_HISTORY WHERE CUSTOMER_NO = CUSTOMER_ONO AND " + p + " = " + c;
//                break;
//        }
    }
    
    static void createCustomerStatement(String p1, String c1, String p2, String c2) {
        query = "SELECT * FROM CUSTOMER,ORDER_HISTORY "
                + "WHERE CUSTOMER_NO = CUSTOMER_ONO AND "
                + p1 + " = " + c1 + " AND " + p2 + " = " + c2;
    }

    public static void main(String[] args) {
        new Database();
    }

}
