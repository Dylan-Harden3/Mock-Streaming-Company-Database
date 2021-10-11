package guis;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class analystGUI extends JFrame implements ActionListener,ItemListener {
    JTextField txtarea;
    JTextArea mostWatchedMovies;
    public JRadioButton button1,button2,button3,button4;
    public JButton rad_pan_button,text_area_button;
    public JComboBox<String> comb_box;
    public JFrame frame;

    public analystGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JPanel b3=new JPanel();



        text_area_button = new JButton("Search");
        text_area_button.addActionListener(this);
        txtarea = new JTextField(20);
        b3.add(txtarea);
        b3.add(text_area_button);

        JLabel companyName = new JLabel("Watchorama",SwingConstants.CENTER);
        mostWatchedMovies = new JTextArea("Select a Time Interval");
        mostWatchedMovies.setEditable(false);
        JTextArea watchHistory = new JTextArea("Watch History");
        watchHistory.setEditable(false);
        JTextArea customerRecomendations = new JTextArea("Customer Recomendations");
        customerRecomendations.setEditable(false);

        JPanel b1=new JPanel();    
        JPanel genre = new JPanel();
        String[] s1 = {"Genre 1", "Genre 2", "Genre 3", "Genre 4"};
        comb_box = new JComboBox<String>(s1);
        comb_box.addItemListener(this);
        genre.add(comb_box);

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

        JLabel filterTime = new JLabel("Filter By Time Perioid",SwingConstants.CENTER);
        JLabel names = new JLabel("Title name, Number of views",SwingConstants.CENTER);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50,50,50,50);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(companyName,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(filterTime,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(names,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(b1,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(mostWatchedMovies,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(b3,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(watchHistory,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(genre,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(customerRecomendations,gbc);

        frame.add(panel,BorderLayout.CENTER);
        //frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent event){
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
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        String sqlStmt = "";
        Object src = event.getSource();
        String startStmt = "Most watched titles ";
        if(src == rad_pan_button){
            if(button1.isSelected()){
                startStmt += "from 2000 - 2001";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2001-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if(button2.isSelected()){
                startStmt += "from 2002 - 2003";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2002-01-01' AND date <= '2003-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if(button3.isSelected()){
                startStmt += "from 2004 - 2005";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2004-01-01' AND date <= '2005-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if(button4.isSelected()){
                startStmt += "All Time";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2005-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            startStmt += "\n";
            try{
                Statement statement = conn.createStatement();
                ResultSet set = statement.executeQuery(sqlStmt);
                String result = "";
                while(set.next()){
                    result += set.getString("titleid")+ " " + set.getString("most_watched") + " ";
                }
                
                String[] titles = result.split(" ");
                
                String output = "";
                Vector<String> names = new Vector<String>();
                for(String s : titles){
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
                        output += names.get(i) + " ";
                        i++;
                    }else {
                        output += s + "\n";
                    }
                }
                output = startStmt + output;
                mostWatchedMovies.setText(output);
            }catch(Exception exc){
                JOptionPane.showMessageDialog(null,"Error accessing Database");
            }
        }
        else if(src == text_area_button){
            String s = event.getActionCommand();
            if (s.equals("Search")) {
                // set the text of the label to the text of the field
                JOptionPane.showMessageDialog(this,txtarea.getText());
                txtarea.setText("");
     
                // set the text of field to blank

            }
        }
        
    }
    public void itemStateChanged(ItemEvent e){
        // if the state combobox is changed
        if (e.getSource() == comb_box) {
        }
    }

    public static void main(String[] args) throws Exception {
        new analystGUI();
    }
}
