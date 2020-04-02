package helper;

import gui.DetailChart;
import gui.OverallChart;
import model.WebAPIManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class CalenderToolBar extends JToolBar {
    private static CalenderToolBar calenderToolBar;
    private WebAPIManager webAPIManager;
    private LocalDate fromDate;
    private LocalDate toDate;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JSpinner baseCurrencySpinner;
    private static final LocalDate today = LocalDate.now();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private CalenderToolBar() {
        webAPIManager = WebAPIManager.getInstance();
        toDate = today;
        fromDate = webAPIManager.getEndDate();
        fromDateSpinner = new JSpinner(new SpinnerDateModel(
                java.sql.Date.valueOf(fromDate),
                null,
                java.sql.Date.valueOf(today),
                Calendar.DAY_OF_MONTH
        ));
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
        toDateSpinner = new JSpinner(new SpinnerDateModel(
                java.sql.Date.valueOf(toDate),
                null,
                java.sql.Date.valueOf(today),
                Calendar.DAY_OF_MONTH
        ));
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
        String[] countries = webAPIManager.getAllCountries();
        int index = -1;
        for(int i = 0; i < countries.length; i++) {
            if(countries[i].equals(webAPIManager.getBaseCurrency())) {
                index = i;
            }
        }
        baseCurrencySpinner = new JSpinner(new SpinnerListModel(countries));
        baseCurrencySpinner.setValue(countries[index]);


        fromDateSpinner.addChangeListener(e -> {
            Date spinnerDate = (Date)fromDateSpinner.getValue();
            if(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(sdf.format(spinnerDate)), toDate))< 5) {
                JOptionPane.showConfirmDialog(
                        new JFrame(),
                        "From date and to date must have more then 5 days difference in between. FROM",
                        "System Alert",
                        JOptionPane.WARNING_MESSAGE
                );
                fromDateSpinner.setValue(java.sql.Date.valueOf(toDate.minusDays(7)));
            } else {
                fromDate = LocalDate.parse(sdf.format(spinnerDate));
                webAPIManager.expendDataSet(fromDate);
                DetailChart.getInstance().resetView();
                OverallChart.getInstance().repaint();
            }
        });

        toDateSpinner.addChangeListener(e -> {
            Date spinnerDate = (Date)toDateSpinner.getValue();
            if(Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(sdf.format(spinnerDate)), fromDate)) < 5) {
                JOptionPane.showConfirmDialog(
                        new JFrame(),
                        "From date and to date must have more then 5 days difference in between. TO",
                        "System Alert",
                        JOptionPane.WARNING_MESSAGE
                );
                toDateSpinner.setValue(java.sql.Date.valueOf(fromDate.plusDays(7)));
                System.out.println("New Value for to: " + toDateSpinner.getValue().toString());
            } else {
                toDate = LocalDate.parse(sdf.format(spinnerDate));
                DetailChart.getInstance().resetView();
                OverallChart.getInstance().repaint();
            }
        });

        baseCurrencySpinner.addChangeListener(e -> {
            webAPIManager.switchBaseCurrency((String)baseCurrencySpinner.getValue());
            //TODO Update Panels
        });

        Container container = new Container();
        container.setLayout(new FlowLayout());
        container.add(new JLabel("From: "));
        container.add(fromDateSpinner);
        container.add(new JLabel("To: "));
        container.add(toDateSpinner);
        container.add(new JLabel("Base Currency: "));
        container.add(baseCurrencySpinner);
        add(container);
    }
    public static CalenderToolBar getInstance() {
        if(calenderToolBar == null) {
            calenderToolBar = new CalenderToolBar();
        }
        return calenderToolBar;
    }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return  toDate; }
}
