package GUI;

import org.apache.derby.iapi.services.loader.InstanceGetter;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Contents extends JPanel {
    private String retrieve;
    private String[] options;
    private String genderString;
    private String ageString;
    private String timeString;
    private Connection connection;
    public Contents() {
        genderString = "";
        ageString = "";
        timeString = "";
        //0: Gender, 1: Age, 2: Hours
        options = new String[3];
        try {
            connection = DriverManager.getConnection("jdbc:derby:src/cs490R");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        //If no query, just show the size of the database.
        if(options[0] == null && options[1] == null && options[2] == null) {
            try {
                retrieve = "Total of " + askDB("SELECT COUNT(*) FROM marathon");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            retrieve += "\n" + "Ask Query using menubar options";
        } else {
            String query = "SELECT COUNT(*) FROM marathon WHERE ";
            if(options[0] != null) {
                query += options[0] + " AND ";
            }
            if(options[1] != null) {
                query += options[1] + " AND ";
            }
            if(options[2] != null) {
                query += options[2] + " AND ";
            }
            //Construct query as the menu get selected.
            query = query.substring(0, query.length() - 5);
            try {
                retrieve = "Total of " + askDB(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            retrieve += "\n" + genderString + ageString + timeString;
        }
        drawString(g, retrieve, 20,20);
    }

    //Draw String with new lines
    private void drawString(Graphics g, String s, int x, int y) {
        String[] lines = s.split("\n");
        int line = 1;
        for(String ln : lines) {
            g.drawString(ln, x, y * line);
            line++;
        }
    }

    //Create Statement, send a Query
    private String askDB(String query) throws SQLException {
        String out = "null";
        if(connection != null) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            rs.next();
            out = rs.getString(1);
        }
        return out;
    }

    /**
     * Create Query option and gender string for a query statement and panel message.
     * @param gen Gender String, either "M" or "F". When null, no query will be made
     */
    public void queryGender(String gen) {
        if(gen == null) {
            options[0] = null;
            genderString = "";
        } else {
            options[0] = "gender = '" + gen + "'";
            genderString = "Gender = '" + gen + "'; ";
        }
        repaint();
    }

    /**
     * Create Query option and age string for a query statement and panel message.
     * When lowbound = highbound = 0, there will be no query.
     * @param lowbound
     * @param highbound
     */
    public void queryAge(int lowbound, int highbound) {
        if(lowbound == 0 && highbound == 0) {
            options[1] = null;
            ageString = "";
        } else {
            options[1] = "age >= " + Integer.toString(lowbound) + " AND " + "age <= " + Integer.toString(highbound);
            ageString = "Age = " + lowbound + " <= age <= " + highbound + "; ";
        }
        repaint();
    }

    /**
     * Create Query option and time(hours) string for a query statement and panel message.
     * When lowbound = highbound = 0, there will be no query.
     * @param lowbound
     * @param highbound
     */
    public void queryTime(int lowbound, int highbound) {
        if(lowbound == 0 && highbound == 0) {
            options[2] = null;
            timeString = "";
        } else {
            options[2] = "hours >= " + Integer.toString(lowbound) + " AND " + "hours < " + Integer.toString(highbound);
            timeString = "Hours = " + lowbound + " <= time < " + highbound + "; ";
        }
        repaint();
    }

    /**
     * Clear all Query options and panel message strings.
     */
    public void clearRoster() {
        for(int i = 0; i < options.length; i++) {
            options[i] = null;
        }
        genderString = "";
        timeString = "";
        ageString = "";
        repaint();
    }
}
