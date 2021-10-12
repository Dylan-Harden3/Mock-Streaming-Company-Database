import guis.analystGUI;
import guis.customerGUI;
import javax.swing.*;

import java.sql.*;
import java.util.regex.Pattern;
public class login {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        //Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        boolean loggedIn = false;
    
        while(!loggedIn) {
            String input = JOptionPane.showInputDialog(null, "Enter \"admin\" for admin login or customerid for user login");
            
            String message = "SELECT customerid FROM customerratings WHERE (customerid = " + input + ") LIMIT 1";

            if(Pattern.matches("[a-zA-Z]+", input) == true) {
                if(input.toLowerCase().equals("admin")) {
                    loggedIn = true;
                    new analystGUI();
                }else {
                    JOptionPane.showMessageDialog(null,"You must enter either \"admin\" or a customerid");
                }
            }
            else if(input.matches("[0-9]+")) {
                    // SEND MESSAGE TO DATABASE
                try{
                    Statement stmt = conn.createStatement();
                    ResultSet query = stmt.executeQuery(message);
                    // CHECK IF VALID CUSTOMER ID
                    if (!query.next()) {
                        // NO USER WITH ID
                        //throw new IllegalArgumentException("NO USER WITH ID: " + input);
                        JOptionPane.showMessageDialog(null,"Invalid customerid");
                    }else {
                        loggedIn = true;
                        new customerGUI(Integer.parseInt(input));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            }else {
                JOptionPane.showMessageDialog(null,"You must enter either \"admin\" or a customerid");
            }
        }

        //closing the connection
        try {
            conn.close();
        } catch(Exception e) {
        } 
    }
}