package database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Record {
    
    int CustomerID = 0;
    int OrderID;
    int ServiceID;
    
    Database database;
    
    public Record(Database d) {
        database = d;
    }
    
    public void getData() throws SQLException {
        database.createSelectAllStatement("customer");
        while (database.rs.next()) {
            CustomerID = database.rs.getInt(1);
        }
        database.createSelectAllStatement("orders");
        while (database.rs.next()) {
            OrderID = database.rs.getInt(1);
        }
        database.createSelectAllStatement("services");
        while (database.rs.next()) {
            ServiceID = database.rs.getInt(1);
        }
    }
    
    public int getCustomerID() throws SQLException {
        database.createSelectAllStatement("customer");
        while (database.rs.next()) {
            CustomerID = database.rs.getInt(1);
        }
        return CustomerID;
    }
    
    public int getOrderID() throws SQLException {
        database.createSelectAllStatement("orders");
        while (database.rs.next()) {
            CustomerID = database.rs.getInt(1);
        }
        return OrderID;
    }
    
    public int getServiceID() throws SQLException {
        database.createSelectAllStatement("services");
        while (database.rs.next()) {
            CustomerID = database.rs.getInt(1);
        }
        return ServiceID;
    }
    
}
