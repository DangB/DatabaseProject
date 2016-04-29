package netbeansdatabase;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Database {
    
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    static public LoginWindow login;
    static public MainMenu menu;
    
    public Database() {
        login = new LoginWindow(this);
        menu = new MainMenu();
        login.setVisible(true);
        menu.setVisible(false);
    }
    
    public void connect(String u, String p) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://triton.towson.edu:3360/bdang1db", u, p);
            JOptionPane.showMessageDialog(login, "Connected to Database");
            login.dispose();
            menu.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(login, "Failed to Connect");
        }
    }

    public static void main(String[] args) {
        new Database();
    }
    
}
