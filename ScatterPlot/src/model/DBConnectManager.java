package model;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class DBConnectManager {
    //Scatter Plot Database Model - Feb 13,2020
    private static DBConnectManager dbManager;
    private Connection connection;
    private ArrayList<Double> x_raw;
    private ArrayList<Double> y_raw;
    private ArrayList<String> g_raw;
    private ArrayList<Point.Double> points;
    private ArrayList<String> gender;

    private DBConnectManager() {
        x_raw = new ArrayList<>();
        y_raw = new ArrayList<>();
        points = new ArrayList<>();
        g_raw = new ArrayList<>();
        gender = new ArrayList<>();
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
        x_raw.clear();
        y_raw.clear();
        String query = "SELECT " + col_name1 + ", " + col_name2 + ", gender FROM withage";
        if(connection != null) {
            try{
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery(query);
                while(rs.next()) {
                    x_raw.add(rs.getDouble(1));
                    y_raw.add(rs.getDouble(2));
                    g_raw.add(rs.getString(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resetFrame();
        }
    }

    public void updateFrame(double minX, double minY, double maxX, double maxY) {
        ArrayList<Point.Double> mappedPoints = new ArrayList<>();
        ArrayList<String> mappedGender = new ArrayList<>();
        for(int i = 0; i < getSize(); i++) {
            if((minX <= getXin(i) && maxX >= getXin(i))&(minY <= getYin(i) && maxY >= getYin(i))) {
                mappedPoints.add(points.get(i));
                mappedGender.add(gender.get(i));
            }
        }
        points.clear();
        gender.clear();
        points = (ArrayList<Point.Double>)mappedPoints.clone();
        gender = (ArrayList<String>)mappedGender.clone();
    }

    public void resetFrame() {
        points.clear();
        gender.clear();
        for(int i = 0; i < x_raw.size(); i++) {
            points.add(new Point.Double(x_raw.get(i), y_raw.get(i)));
        }
        gender = (ArrayList<String>) g_raw.clone();
    }

    public double getXin(int i) {
        return points.get(i).x;
    }
    public double getYin(int i) {
        return points.get(i).y;
    }
    public String getGenderin(int i) { return gender.get(i); }
    public double getXMax(){ return (getSize()==0)?1.0:Collections.max(points.stream().map(x->x.getX()).collect(Collectors.toList())); }
    public double getYMax(){ return (getSize()==0)?1.0:Collections.max(points.stream().map(y->y.getY()).collect(Collectors.toList())); }
    public double getXMin(){ return (getSize()==0)?0.0:Collections.min(points.stream().map(x->x.getX()).collect(Collectors.toList())); }
    public double getYMin(){ return (getSize()==0)?0.0:Collections.min(points.stream().map(y->y.getY()).collect(Collectors.toList())); }
    public int getSize(){ return points.size(); }
    public int getDBSize(){ return x_raw.size(); }

}
