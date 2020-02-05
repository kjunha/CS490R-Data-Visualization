package GUI;

import javax.swing.*;
import java.text.DecimalFormat;

public class YScaleToolbar extends JToolBar {
    private static YScaleToolbar yScaleToolbar;
    private double currentHigh;
    private double currentLow;
    private JPanel panel;
    private ChartPanel chartPanel;
    private DecimalFormat df;
    private JSpinner minSetter;
    private JSpinner maxSetter;
    private YScaleToolbar() {
        currentLow = 0.0;
        currentHigh = 0.0;
        chartPanel = ChartPanel.getInstance();
        panel = new JPanel();
        panel.add(new JLabel("min: "));
        panel.add(minSetter = new JSpinner());
        panel.add(new JLabel("Max: "));
        panel.add(maxSetter = new JSpinner());

        minSetter.addChangeListener(e -> {
            currentLow = (Double)minSetter.getValue();
            chartPanel.setLow_HighBounds(currentLow, currentHigh);
        });
        maxSetter.addChangeListener(e -> {
            currentHigh = (Double)maxSetter.getValue();
            chartPanel.setLow_HighBounds(currentLow, currentHigh);
        });

        add(panel);
    }

    public static YScaleToolbar getInstance() {
        if(yScaleToolbar == null) {
            yScaleToolbar = new YScaleToolbar();
        }
        return yScaleToolbar;
    }

    public double[] getScaleRatio(){
        if(chartPanel.getIsTotalBased()) {
            return new double[] {currentLow/100, currentHigh/100};
        }
        return new double[] {currentLow/(chartPanel.getYMax()*1.1), currentHigh/(chartPanel.getYMax()*1.1)};
    }

    public void updateScale(double low, double high) {
        this.currentLow = low;
        this.currentHigh = high;
        minSetter.setModel(new SpinnerNumberModel(currentLow, currentLow, currentHigh, 1.0));
        maxSetter.setModel(new SpinnerNumberModel(currentHigh, currentLow, currentHigh, 1.0));
        chartPanel.setLow_HighBounds(currentLow, currentHigh);
        panel.repaint();
        //TODO minSetter, maxSetter, panel repaint
    }
}
