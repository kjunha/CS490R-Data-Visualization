package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DBConnectManager {
    //Scatter Plot Database Model - Feb 13,2020
    private static DBConnectManager dbManager;
    private Connection connection;
    private ArrayList<Double> x_values;
    private ArrayList<Double> y_values;

    private DBConnectManager() {
        x_values = new ArrayList<>();
        y_values = new ArrayList<>();
        try {
          connection = DriverManager.getConnection("jdbc:derby:res/withage2012");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnectManager getInstance() {
        if(dbManager == null) {
            dbManager = new DBConnectManager();
        }
        return dbManager;
    }

    public void requestDB(String col_name1, String col_name2) {
        x_values.clear();
        y_values.clear();
        String query = "SELECT " + col_name1 + ", " + col_name2 + " FROM withage";
        if(connection != null) {
            try{
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery(query);
                while(rs.next()) {
                    x_values.add(rs.getDouble(1));
                    y_values.add(rs.getDouble(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double getXin(int i) {
        return x_values.get(i);
    }
    public double getYin(int i) {
        return y_values.get(i);
    }
    public double getXMax() {
        return Collections.max(x_values);
    }
    public double getYMax() {
        return Collections.max(y_values);
    }
    public int getSize(){
        return x_values.size();
    }

}
