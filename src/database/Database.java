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
    static public LoginWindow login;
    static public MainMenu menu;
    static public RetrieveWindow retrieve;
    
    public Database() {
        login = new LoginWindow(this);
        menu = new MainMenu(this);
        retrieve = new RetrieveWindow(this);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        menu.setVisible(false);
        retrieve.setVisible(false);
    }
    
    public void connect(String u, String p) {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(
//                    "jdbc:mysql://triton.towson.edu:3360/swanga1db", u, p);
//            queryStatement = conn.createStatement();
//            JOptionPane.showMessageDialog(login, "Connected to Database");
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
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }
    
    static void retrieveOpen() {
        retrieve.setVisible(true);
        retrieve.setLocationRelativeTo(menu);
        retrieve.setLocation(menu.getX()+menu.getWidth(), menu.getY());
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
