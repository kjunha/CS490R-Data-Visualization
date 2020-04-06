package helper;

import model.WebAPIManager;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class TableStructure extends AbstractTableModel {
    private static TableStructure tableStructure;
    private WebAPIManager webAPIManager;
    private String[] columnName;
    private Object[][] rowData;

    private static final DecimalFormat df = new DecimalFormat("#.##");
    private TableStructure() {
        webAPIManager = WebAPIManager.getInstance();
        columnName = new String[]{"Currency", "Base", "Today", "Max", "Min"};
        String[] countries = webAPIManager.getCountries();
        rowData = new Object[countries.length][columnName.length];
        this.addTableModelListener(e -> {
            
        });
    }
    public static TableStructure getInstance() {
        if(tableStructure == null) {
            tableStructure = new TableStructure();
        }
        return tableStructure;
    }
    public String[] getColumnName() { return columnName; }
    public Object[][] getRowData() { return rowData; }

    @Override
    public int getRowCount() {
        return rowData.length;
    }

    @Override
    public int getColumnCount() {
        return columnName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }

    public void updateData() {
        String[] countries = webAPIManager.getCountries();
        for(int i = 0; i < countries.length; i++) {
            ArrayList<Double> currencyValues = new ArrayList<>(webAPIManager.getAllRatesByCountry(countries[i]));
            rowData[i][0] = countries[i];
            rowData[i][1] = webAPIManager.getBaseCurrency();
            rowData[i][2] = df.format(currencyValues.get(0));
            rowData[i][3] = df.format(Collections.max(currencyValues));
            rowData[i][4] = df.format(Collections.min(currencyValues));
        }

    }
}
