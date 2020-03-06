package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBConnectManager {
    private static DBConnectManager dbManager;
    private Connection connection;
    //Parallel array: keySet - dataFrame - Hashmap<S,O>
    private ArrayList<String> keySet;
    private ArrayList<String> dataFrame;
    private ArrayList<HashMap<String,Object>> structs;

    private DBConnectManager() {
        keySet = new ArrayList<>();
        dataFrame = new ArrayList<>();
        structs = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:derby:res/pcdb"); //PCDB: Parallel Coordinates Database
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Singleton structure. Keep the database manager instance one.
     * @return database manager instance.
     */
    public static DBConnectManager getInstance() {
        if(dbManager == null) {
            dbManager = new DBConnectManager();
        }
        return dbManager;
    }

    /**
     * The metadata of the table (Table column name, and data type) is saved in keySet and dataFrame field as a list.
     * Each values in the dataset is grouped by lines, encapsulated as one hashmap object.
     * For example: one hashmap object is {colname1: value, colname2: value, colname3: value ...}
     * The index of structs represents the line in the dataset and the column name in keySet is the key of each hashmap object.
     * @param tableName name of the table in the database
     */
    public void loadTable(String tableName) {
        keySet.clear();
        dataFrame.clear();
        structs.clear();
        if(connection != null) {
            try {
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    keySet.add(rsmd.getColumnName(i));
                    dataFrame.add(rsmd.getColumnTypeName(i));
                }
                while (rs.next()) {
                    HashMap<String,Object> entity = new HashMap<>();
                    for(int i = 1; i <= keySet.size(); i++) {
                        if(dataFrame.get(i-1).toLowerCase().equals("double")) {
                            entity.put(keySet.get(i-1), rs.getDouble(i));
                        } else if (dataFrame.get(i-1).toLowerCase().contains("int")) {
                            entity.put(keySet.get(i-1), rs.getInt(i));
                        } else {
                            entity.put(keySet.get(i-1), rs.getString(i));
                        }
                    }
                    structs.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the number of rows in the database.
     * @return the size of struct
     */
    public int getEntityCount() {
        return structs.size();
    }

    /**
     * Returns one 'struct', single line in the dataset.
     * @param i number of the row of dataset
     * @return hashmap object stored in the corresponding hashmap object
     */
    public HashMap<String, Object> getEntity(int i) {
        return structs.get(i);
    }

    /**
     * Returns the number of keySet
     * @return the size of keySet
     */
    public int getKeySetCount() {
        return keySet.size();
    }

    /**
     * Key getter for hashmap.
     * @param i index of keySet
     * @return key String for a hashmap
     */
    public String getKeySetValueIn(int i) {
        return keySet.get(i);
    }

    /**
     * Data type getter for hashmap
     * @param i index of dataFrame
     * @return data type string for a hashmap
     */
    public String getDataType(int i) {
        return dataFrame.get(i);
    }
    // TODO Create a method returns its own type using generic: public <T> List<T> getValuesList(int i)

    /**
     * Returns a value list of a column
     * @param i index of KeySet
     * @return an object arrayList of values. (needs to be type casted)
     */
    public List<Object> getValueList(int i) {
        List<Object> valueSet = new ArrayList<>();
        for(HashMap<String, Object> entity : structs) {
            valueSet.add(entity.get(getKeySetValueIn(i)));
        }
        return valueSet;
    }
}
