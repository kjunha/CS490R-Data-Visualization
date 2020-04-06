package gui;

import helper.CalenderToolBar;
import model.WebAPIManager;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DetailTable extends JScrollPane {
    private static DetailTable detailTable;
    private CalenderToolBar calenderToolBar;
    private JTable table;
    private WebAPIManager webAPIManager;
    private String[] columnName;
    private Object[][] rowData;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    private DetailTable() {
        webAPIManager = WebAPIManager.getInstance();
        calenderToolBar = CalenderToolBar.getInstance();
        columnName = new String[]{"Currency", "Base", "Today", "Max", "Min"};
        String[] countries = webAPIManager.getCountries();
        rowData = new Object[countries.length][columnName.length];
        for(int i = 0; i < countries.length; i++) {
            ArrayList<Double> currencyValues = new ArrayList<>(webAPIManager.getAllRatesByCountry(countries[i]));
            rowData[i][0] = countries[i];
            rowData[i][1] = webAPIManager.getBaseCurrency();
            rowData[i][2] = df.format(currencyValues.get(0));
            rowData[i][3] = df.format(Collections.max(currencyValues));
            rowData[i][4] = df.format(Collections.min(currencyValues));
        }
        table = new JTable(rowData, columnName);
        table.getModel().addTableModelListener(e -> {
            viewport.revalidate();
        });
        setViewportView(table);
        table.setFillsViewportHeight(true);
    }

    public static DetailTable getInstance() {
        if(detailTable == null) {
            detailTable = new DetailTable();
        }
        return detailTable;
    }

    public String[] getColumnName() { return columnName; }
    public Object[][] getRowData() { return rowData; }

    public void updateData() {
        String[] countries = webAPIManager.getCountries();
        int begin = webAPIManager.getIndexOfDates(calenderToolBar.getToDate().toString());
        int end = webAPIManager.getIndexOfDates(calenderToolBar.getFromDate().toString());
        for(int i = 0; i < countries.length; i++) {
            ArrayList<Double> currencyValues = new ArrayList<>(webAPIManager.getAllRatesByCountry(countries[i]));
            ArrayList<Double> currencyValuesInWindow = IntStream.range(begin, end).mapToObj(currencyValues::get).collect(Collectors.toCollection(ArrayList::new));
            table.setValueAt(countries[i],i,0);
            table.setValueAt(webAPIManager.getBaseCurrency(),i,1);
            table.setValueAt(df.format(currencyValues.get(0)),i,2);
            table.setValueAt(df.format(Collections.max(currencyValuesInWindow)),i,3);
            table.setValueAt(df.format(Collections.min(currencyValuesInWindow)),i,4);
        }
    }

}
