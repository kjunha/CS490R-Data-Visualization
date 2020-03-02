package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBConnectManager {
    private static DBConnectManager dbManager;
    private Connection connection;
    private ArrayList<String> keyset;
    private ArrayList<String> dataframe;
    private ArrayList<HashMap<String,Object>> structs;
    private DBConnectManager() {
        keyset = new ArrayList<>();
        dataframe = new ArrayList<>();
        structs = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:derby:res/pcdb"); //PCDB: Parallel Coordinates Database
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadTable("roster");
    }

    public static DBConnectManager getInstance() {
        if(dbManager == null) {
            dbManager = new DBConnectManager();
        }
        return dbManager;
    }

    public void loadTable(String tableName) {
        keyset.clear();
        dataframe.clear();
        structs.clear();
        if(connection != null) {
            try {
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    keyset.add(rsmd.getColumnName(i));
                    dataframe.add(rsmd.getColumnTypeName(i));
                }
                while (rs.next()) {
                    HashMap<String,Object> entity = new HashMap<>();
                    for(int i = 1; i <= keyset.size(); i++) {
                        if(dataframe.get(i-1).toLowerCase().equals("double")) {
                            entity.put(keyset.get(i-1), rs.getDouble(i));
                        } else if (dataframe.get(i-1).toLowerCase().contains("int")) {
                            entity.put(keyset.get(i-1), rs.getInt(i));
                        } else {
                            entity.put(keyset.get(i-1), rs.getString(i));
                        }
                    }
                    structs.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int getEntityCount() {
        return structs.size();
    }
    public HashMap<String, Object> getEntity(int i) {
        return structs.get(i);
    }

    public int getKeySetCount() {
        return keyset.size();
    }
    public String getKeySetValueIn(int i) {
        return keyset.get(i);
    }
    public String getDataType(int i) {
        return dataframe.get(i);
    }
    // TODO Create a method returns its own type using generic: public <T> List<T> getValuesList(int i)
    public List<Object> getValueList(int i) {
        List<Object> valueSet = new ArrayList<>();
        for(HashMap<String, Object> entity : structs) {
            valueSet.add(entity.get(getKeySetValueIn(i)));
        }
        return valueSet;
    }
}
