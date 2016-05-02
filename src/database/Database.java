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
    
    static Record record;
    
    static public LoginWindow loginWindow;
    static public MainMenu menuWindow;
    static public RetrieveWindow retrieveWindow;
    static public AddWindow addWindow;
    static public UpdateWindow updateWindow;
    static public DeleteWindow deleteWindow;
    static public AdminDeleteWindow adminDeleteWindow;
    static public ItemSubWindow itemSubWindow;
    static public ItemQuantityWindow itemQuantityWindow;
    
    public Database() {
        record = new Record(this);
        loginWindow = new LoginWindow(this);
        menuWindow = new MainMenu(this);
        retrieveWindow = new RetrieveWindow(this);
        addWindow = new AddWindow(this);
        updateWindow = new UpdateWindow(this);
        deleteWindow = new DeleteWindow(this);
        adminDeleteWindow = new AdminDeleteWindow(this);
        
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setVisible(true);
        menuWindow.setVisible(false);
        retrieveWindow.setVisible(false);
        addWindow.setVisible(false);
        updateWindow.setVisible(false);
        adminDeleteWindow.setVisible(false);
    }
    
    public static void main(String[] args) {
        new Database();
    }
    
    public void connect(String u, String p) {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(
//                    "jdbc:mysql://triton.towson.edu:3360/swanga1db", u, p);
//            queryStatement = conn.createStatement();
//            JOptionPane.showMessageDialog(login, "Connected to Database");

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/swanga1db", "root", "usa21213");
            queryStatement = conn.createStatement();
            JOptionPane.showMessageDialog(loginWindow, "Connected to Database");
            record.getData();
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
    
    static void deleteWindowOpen() {
        deleteWindow.setVisible(true);
        deleteWindow.setLocationRelativeTo(menuWindow);
        deleteWindow.setLocation(menuWindow.getX()+menuWindow.getWidth(),
                menuWindow.getY());
    }
    
    void itemSubWindowOpen(String s) throws SQLException {
        itemSubWindow = new ItemSubWindow(this, s);
        itemQuantityWindow = new ItemQuantityWindow(this);
        itemSubWindow.setLocationRelativeTo(addWindow);
        itemSubWindow.setVisible(true);
        itemQuantityWindow.setVisible(false);
    }
    
    static void itemQuantityWindowOpen(String orderNo, String itemNo) {
        itemQuantityWindow.setLocationRelativeTo(itemSubWindow);
        itemQuantityWindow.setVisible(true);
        itemQuantityWindow.init(orderNo, itemNo);
    }
    
    static void adminDeleteWindowOpen() {
        adminDeleteWindow.setVisible(true);
        adminDeleteWindow.setLocationRelativeTo(menuWindow);
        adminDeleteWindow.setLocation(menuWindow.getX()+menuWindow.getWidth(),
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
    
    static void createSelectAllStatement(String t) throws SQLException {
        query = "SELECT * FROM " + t;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createBasicStatement(String t, String p, String c) throws SQLException {
        c = quote(c);
        
        query = "SELECT * FROM " + t + " WHERE " + p + " = " + c;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createBasicStatement(String t1, String p1, String c1, 
            String p2, String c2) throws SQLException {        
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT * FROM " + t1 + " WHERE " + p1 + " = " + c1
                + " AND " + p2 + " = " + c2;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createTwoTableStatement(String t1, String t2, String j, 
            String p1, String c1) throws SQLException {
        c1 = quote(c1);
        
        query = "SELECT a.* FROM " + t1 + " a, " + t2 + " b" + " WHERE "
                + "a." + j + " = b." + j + " AND b." + p1 + " = " + c1;
        printQuery();
        rs = queryStatement.executeQuery(query);        
    }
    
    static void createTwoTableStatement(String t1, String t2, String p1, String c1,
            String p2, String c2) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT a.* FROM " + t1 + " WHERE " + p1 + " = " + c1
                + " AND " + p2 + " = " + c2;
        printQuery();
        rs = queryStatement.executeQuery(query); 
        
    }
    
    static void createStatementOperator(String t, String p1, String c1, String p2,
            String c2, String o1) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT * FROM " + t + " WHERE " + p1 + " = " + c1
                + " AND " + p2 + " " + o1 +  " " + c2;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createTwoTableStatementOperator(String t1, String t2, String j, String p1, 
            String c1, String p2, String c2, String o1) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT a.* FROM " + t1 + " a, " + t2 + " b" + " WHERE "
                + "a." + j + " = b." + j + " AND b." + p1 + " = " + c1 + 
                " AND " + p2 + " " + o1 + " " + c2;
        printQuery();
        rs = queryStatement.executeQuery(query); 
    }
    
    static void createTwoTableStatementOperator(String t1, String t2, String j, 
            String p1, String c1, String p2, String c2, String o1, String p3, 
            String c3, String o2) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT a.* FROM " + t1 + " a, " + t2 + " b" + " WHERE "
                + "a." + j + " = b." + j + " AND b." + p1 + " = " + c1 + 
                " AND " + p2 + " " + o1 + " " + c2 + " AND " + p3 + " " + o2 + 
                " " + c3;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createBasicStatementOperator(String t, String p1, String c1, String o1) 
            throws SQLException {
        c1 = quote(c1);
        
        query = "SELECT * FROM " + t + " WHERE " + p1 + " " +  o1 + " " + c1;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createBasicStatementOperator(String t, String p1, String c1, String o1,
            String p2, String c2, String o2) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        
        query = "SELECT * FROM " + t + " WHERE " + p1 + " " +  o1 + " " + c1 +
                " AND " + p2 + " " + o2 + " " + c2;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createStatementOperator(String t, String p1, String c1, String p2,
            String c2, String o1, String p3, String c3, String o2) throws SQLException {
        c1 = quote(c1);
        c2 = quote(c2);
        query = "SELECT * FROM " + t + " WHERE " + p1 + " = " + c1
                + " AND " + p2 + " " + o1 +  " " + c2 + " AND " + p3 + " " + o2 +
                " " + c3;
        printQuery();
        rs = queryStatement.executeQuery(query);
    }
    
    static void createCustomerByOrderIdStatement(String c1) throws SQLException {
 
        query = "SELECT a.* FROM CUSTOMER a,ORDERS b "
                + "WHERE a.CUSTOMER_NO = b.CUSTOMER_NO AND ORDER_ID" + " = " + c1;
        printQuery();
        
        rs = queryStatement.executeQuery(query);
    }
    
    static void createInvoice(String s) throws SQLException {
        query = "SELECT c.item_type as Item_Type, Description, a.QTY, Price FROM consist_of a, "
                + "orders b, item c WHERE b.order_id = " + s + " AND c.item_no = a.item_no";
        printQuery();
        rs = queryStatement.executeQuery(query);
    }

    static void statementDateSwitch (String t, String p, String c, String d1, String d2)
            throws SQLException {
        int flag1 = 0;
        int flag2 = 0;
        String s;
        
        if (!d1.isEmpty())
            flag1 = 1;
        if (!d2.isEmpty())
            flag2 = 1;
        
        s = concatenateDigits(flag1, flag2);
        
        switch (s) {
            case "00":
                createBasicStatement(t, p, c);
                break;
            case "10":
                createStatementOperator(t, p, c, "date", d1, ">=");
                break;
            case "01":
                createStatementOperator(t, p, c, "date", d2, "<=");
                break;
            case "11":
                createStatementOperator(t, p, c, "date", d1, ">=", "date", d2, "<=");
                break;
        }
    }
    
    static void statementDateSwitch (String t1, String t2, String j, 
            String p, String c, String d1, String d2) throws SQLException {
        int flag1 = 0;
        int flag2 = 0;
        String s;
        
        if (!d1.isEmpty())
            flag1 = 1;
        if (!d2.isEmpty())
            flag2 = 1;
        
        s = concatenateDigits(flag1, flag2);
        
        if (!c.isEmpty()) {
            switch (s) {
            case "00":
                createTwoTableStatement(t1, t2, j, p, c);
                break;
            case "10":
                createTwoTableStatementOperator(t1, t2, j, p, c, "date", d1, ">=");
                break;
            case "01":
                createTwoTableStatementOperator(t1, t2, j, p, c, "date", d2, "<=");
                break;
            case "11":
                createTwoTableStatementOperator(t1, t2, j, p, c, "date", 
                        d1, ">=", "date", d2, "<=");
                break;
            }
        }
        if (c.isEmpty()) {
            switch (s) {
            case "00":
                break;
            case "10":
                createBasicStatementOperator(t1, "date", d1, ">=");
                break;
            case "01":
                createBasicStatementOperator(t1, "date", d1, "<=");;
                break;
            case "11":
                createTwoTableStatementOperator(t1, t2, j, p, c, "date", 
                        d1, ">=", "date", d2, "<=");
                break;
            }
        }
        
    }
    
    static void statementSwitch (String t, String p1, String c1, String p2, 
            String c2) throws SQLException {
        int flag1 = 0;
        int flag2 = 0;
        String s;
        
        if (!c1.isEmpty())
            flag1 = 1;
        if (!c2.isEmpty())
            flag2 = 1;
        
        s = concatenateDigits(flag1, flag2);
        switch (s) {
            case "00":
                break;
            case "10":
                createBasicStatementOperator(t, p1, c1, ">=");
                break;
            case "01":
                createBasicStatementOperator(t, p1, c1, "<=");
                break;
            case "11":
                createBasicStatementOperator(t, p1, c1, ">=", p2, c2, "<=");
                break;
        }
    }

    static void addCustomer(String a, String b, String c, String d, String e) throws SQLException {
        String id = Integer.toString(record.getCustomerID()+1);
        query = "INSERT INTO `customer` "
                + "(`Customer_No`, `First_Name`, `Last_Name`, `Address`, `Phone`, `Email`) "
                + "VALUES ('"
                + "" + id + "', "
                + "'" + a + "', "
                + "'" + b + "', "
                + "'" + c + "', "
                + "'" + d + "', "
                + "'" + e + "')";
        printQuery();
        queryStatement.executeUpdate(query);
    }
    
    void addOrder(String customerNo, String shippingAddress) throws SQLException {
        String id = Integer.toString(record.getOrderID() + 1);
        query = "INSERT INTO `swanga1db`.`orders` "
                + "(`ORDER_ID`, `SHIPPING_ADDRESS`, `DATE`, `CUSTOMER_NO`) "
                + "VALUES ("
                + "'" + id + "', "
                + "'" + shippingAddress + "', "
                + "CURDATE(), "
                + "'" + customerNo + "')";
        printQuery();
        queryStatement.executeUpdate(query);
    }
    
    void addOrder(String customerNo, String shippingAddress, String creditCard) 
            throws SQLException {
        String id = Integer.toString(record.getOrderID() + 1);
        query = "INSERT INTO .`orders` "
                + "(`ORDER_ID`, `SHIPPING_ADDRESS`, `DATE`, `CUSTOMER_NO`, `CREDIT_CARD_NO`) "
                + "VALUES ("
                + "'" + id + "', "
                + "'" + shippingAddress + "', "
                + "CURDATE(), "
                + "'" + customerNo + "',"
                + "'" + creditCard + "')";
        printQuery();
        queryStatement.executeUpdate(query);
    }

    void addOrder(String customerNo, String shippingAddress, String creditCard, 
            String ccFirstName, String ccLastName, String ccExpiration) 
            throws SQLException {
        if (!check("payment", "credit_card_no", creditCard)) {
            addPayment(creditCard, ccFirstName, ccLastName, ccExpiration);
        }
        addOrder(customerNo, shippingAddress, creditCard);
    }
    
    void addItemToOrder(String orderNo, String itemNo, String quantity) 
            throws SQLException {
        query = "INSERT INTO `consist_of` "
                + "(`ORDER_ID`, `ITEM_NO`, `QTY`) "
                + "VALUES ("
                + "'" + orderNo + "', "
                + "'" + itemNo + "', "
                + "'" + quantity + "')";
        printQuery();
        queryStatement.executeUpdate(query);
    }
    
    void addPayment(String creditCard, String ccFirstName, 
            String ccLastName, String ccExpiration) throws SQLException {
        query = "INSERT INTO `payment` "
                + "(`CREDIT_CARD_NO`, `EXPIRATIONDATE`, `FIRST_NAME`, `LAST_NAME`, `PAID`) "
                + "VALUES ("
                + "'" + creditCard + "', "
                + "'" + ccExpiration +"', "
                + "'" + ccFirstName + "', "
                + "'" + ccLastName + "', "
                + "'Y');";
        printQuery();
        queryStatement.executeUpdate(query);
    }
    
    void addService(String customerNo, String address, String date, String time, int itemNo) 
            throws SQLException {
        String id = Integer.toString(record.getServiceID() + 1);
        query = "INSERT INTO `swanga1db`.`services` "
                + "(`SERVICE_ID`, `ADDRESS`, `ITEM_NO`, `DATE`, `TIME`, `CUSTOMER_NO`) "
                + "VALUES ("
                + "'" + id + "', "
                + "'" + address + "', "
                + "'" + itemNo + "', "
                + "'" + date + "', "
                + "'" + time + "', "
                + "'" + customerNo + "')";
        printQuery();
        queryStatement.executeUpdate(query);
    }
    
    boolean check(String t, String p, String c) throws SQLException {
        c = quote(c);
        query = "SELECT " + p + " FROM " + t + " WHERE " + p + " = " + c;
        printQuery();
        rs = queryStatement.executeQuery(query);
        return rs.next();
    }
    
    static String quote(String s) {
        String a = s;
        if (!s.matches("[0-9]+")) {
            a = "\"" + s + "\"";
        }
        return a;
    }
    
    static void printQuery() {
        System.out.println(query);
    }
    
    public static String concatenateDigits(int... digits) {
        StringBuilder sb = new StringBuilder(digits.length);
        for (int digit : digits) {
          sb.append(digit);
        }
        return sb.toString();
     }

    void createItemListStatement() throws SQLException {
        query = "SELECT ITEM_NO, DESCRIPTION, PRICE FROM item WHERE qty > 0";
        printQuery();
        rs = queryStatement.executeQuery(query);
    }





    

}
