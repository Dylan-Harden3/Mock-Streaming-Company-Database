package guis;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class customerGUI extends JFrame implements ActionListener,ItemListener {
    int customerid;
    public JRadioButton button1, button2, button3, button4;
    public JButton rad_pan_button;
    public JFrame frame;
    public JComboBox<String> comb_box;
    public JTextArea watchHistory;

    public customerGUI(int id) {
        customerid = id;
        frame =  new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JLabel cName = new JLabel("Watchorama", SwingConstants.CENTER);
        JTextArea trending = new JTextArea("Trending titles here");
        trending.setEditable(false);
        JTextArea custom = new JTextArea("Customer Recomendations here");
        custom.setEditable(false);;
        watchHistory = new JTextArea("Select time interval");
        watchHistory.setEditable(false);
        JPanel b1=new JPanel();    
        JPanel genre = new JPanel();
        String[] s1 = {"Genre 1", "Genre 2", "Genre 3", "Genre 4"};
        comb_box = new JComboBox<String>(s1);
        comb_box.addItemListener(this);
        genre.add(comb_box);

        JLabel filterTime = new JLabel("Filter By Time Perioid", SwingConstants.CENTER);
        JLabel name = new JLabel("Title name", SwingConstants.CENTER);

        //make RadioButtons
        button1 = new JRadioButton("2000 - 2001");
        button2 = new JRadioButton("2002 - 2003");
        button3 = new JRadioButton("2004 - 2005");
        button4 = new JRadioButton("All Time");
        //Add RadioButtons to Button Group so they work in sync
        ButtonGroup rad_button_group = new ButtonGroup();
        rad_button_group.add(button1);
        rad_button_group.add(button2);
        rad_button_group.add(button3);
        rad_button_group.add(button4);
        //make button for bottom of RadioButton Panel
        rad_pan_button = new JButton("submit");
        rad_pan_button.addActionListener(this);
        //add to the action listener
        //add RadioButtons
        b1.add(button1);
        b1.add(button2);
        b1.add(button3);
        b1.add(button4);
        b1.add(rad_pan_button);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(cName,gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(genre,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(trending,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(custom,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(filterTime,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(name,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(b1,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(watchHistory,gbc);
        
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        //Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            System.exit(0);
        }
        String sqlStmt = "";
        String startStmt = "Your watch history ";
        if(button1.isSelected()) {
            startStmt += "from 2000 - 2001";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2001-12-31' AND customerid = " + String.valueOf(customerid) + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if(button2.isSelected()) {
            startStmt += "from 2002 - 2003";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2002-01-01' AND date <= '2003-12-31' AND customerid = " + String.valueOf(customerid) + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if(button3.isSelected()) {
            startStmt = "from 2004 - 2005";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2004-01-01' AND date <= '2005-12-31' AND customerid = " + String.valueOf(customerid) + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if(button4.isSelected()) {
            startStmt += "All Time";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2005-12-31' AND customerid = " + String.valueOf(customerid) + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        startStmt += "\n";
        try{
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(sqlStmt);
            String result = "";
            while(set.next()) {
                result += set.getString("titleid")+ "\n";
            }
            String output = "";
            if(result.length() == 0) {
                output = "No watch history data from selected time interval";
            }else {
                String[] titles = result.split("\n");
                
                Vector<String> names = new Vector<String>();
                for(String s : titles) {
                    if(s.charAt(0) == 't'){
                        sqlStmt = "SELECT titlename FROM mediainfo WHERE titleid = '" + s + "';";
                        set = statement.executeQuery(sqlStmt);
                        while(set.next()){
                            names.add(set.getString("titlename"));
                        }
                    }
                }
                int i = 0;
                for(String s : titles){
                    if(s.charAt(0) == 't'){
                        output += names.get(i) + "\n";
                        i++;
                    }else {
                        output += s + "\n";
                    }
                }
            }
            output = startStmt + output;
            watchHistory.setText(output);
        }catch(Exception exc) {
            JOptionPane.showMessageDialog(null,"Error accessing Database");
        }
    }
    public void itemStateChanged(ItemEvent e) {
        // if the state combobox is changed
        if (e.getSource() == comb_box) {
        }
    }

    public static void main(String[] args) throws Exception {
    
    }

}