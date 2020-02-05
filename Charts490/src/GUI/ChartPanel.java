package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ChartPanel extends JPanel {
    //Singleton for Chart Panel
    private static ChartPanel chartPanel;

    //db related global fields
    private Connection connection;
    private ArrayList<String> x_values;
    private ArrayList<Double> y_values;
    private ArrayList<Double> y_values_sum;
    private ArrayList<Double> y_values_max;
    private String columnName;
    private String measurement;
    private String orderBy;
    private static final String countAll = "COUNT (*)";
    private static final String avgGPA = "AVG (gpa)";

    //chart related global fields
    private double[] chartResolution;
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double barwidth;

    //user interaction fields
    private boolean isTotalBased;
    private boolean redraw;
    private boolean showDetail;
    private boolean showBar;
    private boolean showLine;
    private int yPoints;
    private Color color;

    //ToolBar Interaction Field
    private double lowBound;
    private double highBound;

    //Other fields
    private static final double[] gpaBins = {2.25, 2.5, 2.75, 3.0, 3.25, 3.5, 3.75, 4.0};
    private static final DecimalFormat df = new DecimalFormat("#.#");
    private boolean isXLabel;

    private ChartPanel() {
        barwidth = 0.7;
        chartResolution = new double[2];
        x_values = new ArrayList<>();
        y_values = new ArrayList<>();
        y_values_max = new ArrayList<>();
        y_values_sum = new ArrayList<>();
        showDetail = false;
        isTotalBased = false;
        showBar = true;
        showLine = false;
        columnName = "homecountry";
        measurement = "COUNT (*)";
        isXLabel = true;
        yPoints = 10;
        color = new Color(109, 195, 209);

        String query = (orderBy!=null)? "SELECT " + columnName +" , " + measurement + " FROM students GROUP BY " + columnName + orderBy:"SELECT " + columnName +" , " + measurement + " FROM students GROUP BY " + columnName;
        try {
            connection = DriverManager.getConnection("jdbc:derby:res/roster2012");
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                x_values.add(rs.getString(1));
                y_values.add(rs.getDouble(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lowBound = 0;
        highBound = Collections.max(y_values);
        redraw = true;
    }

    public static ChartPanel getInstance(){
        if(chartPanel == null) {
            chartPanel = new ChartPanel();
        }
        return chartPanel;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        double h = getHeight();
        double w = getWidth();
        if(redraw) {
            y_values_sum.clear();
            y_values_max.clear();
            double y_sum = y_values.stream().mapToDouble(Double::valueOf).sum();
            double y_max = Collections.max(y_values) * 1.1;
            y_values.forEach(x->y_values_sum.add(y_values.indexOf(x), x/y_sum));
            y_values.forEach(x->y_values_max.add(y_values.indexOf(x), x/y_max));
        }
        redraw = false;
        if(isTotalBased) {
            double[] datapoints = drawAxis(g, w, h, x_values.size(), yPoints);
            drawBars(g, y_values_sum.stream().mapToDouble(Double::valueOf).toArray(), datapoints);
            drawLines(g, y_values_sum.stream().mapToDouble(Double::valueOf).toArray(), datapoints);
        } else {
            double[] datapoints = drawAxis(g, w, h, x_values.size(), yPoints);
            drawBars(g, y_values_max.stream().mapToDouble(Double::valueOf).toArray(), datapoints);
            drawLines(g, y_values_max.stream().mapToDouble(Double::valueOf).toArray(), datapoints);
        }
    }

    private void drawBar(Graphics2D g, double value, double centerx, int barcnt, String title){
        double w = (chartResolution[0]*barwidth)/barcnt;
        g.setColor(color);
        g.fillRect((int)(centerx-w/2), (int)(bottom-value), (int)w, (int)value);
        g.setColor(Color.BLACK);
        g.drawRect((int)(centerx-w/2), (int)(bottom-value), (int)w, (int)value);
        g.drawLine((int)centerx, (int)bottom - 3, (int)centerx, (int)bottom + 3);
        g.drawString(title, (int)(centerx-title.length()*3.5), (int)bottom + 15);
    }

    private void drawPlot(Graphics2D g, double cx, double cy, int r, String title) {
        g.setColor(Color.GREEN);
        g.fillOval((int)(cx-r/2), (int)(cy-r/2), r, r);
        if(!isXLabel) {
            g.setColor(Color.BLACK);
            g.drawLine((int)cx, (int)bottom - 3, (int)cx, (int)bottom + 3);
            g.drawString(title, (int)(cx-title.length()*3.5), (int)bottom + 15);
        }
    }

    //TODO add color parameter
    private void drawBars(Graphics2D g, double[] values, double[] datapoints) {
        //Scale Factor by toolbar range
        double ybtm = YScaleToolbar.getInstance().getScaleRatio()[0];
        double ytop = YScaleToolbar.getInstance().getScaleRatio()[1];
        for(int i = 0; i < datapoints.length; i++) {
            if(showBar) {
                drawBar(g, chartResolution[1]*((values[i]-ybtm)/(ytop-ybtm)), datapoints[i], datapoints.length, x_values.get(i));
            }
            if(showDetail) {
                g.drawString(df.format(isTotalBased?values[i]*100:y_values.get(i))+(isTotalBased?"%":""), (int)datapoints[i]-(isTotalBased?15:5), (int)(bottom-chartResolution[1]*((values[i]-ybtm)/(ytop-ybtm)))-10);
            }
        }
    }

    private void drawLines(Graphics2D g, double[] values, double[] datapoints) {
        //Scale Factor by toolbar range
        double ybtm = YScaleToolbar.getInstance().getScaleRatio()[0];
        double ytop = YScaleToolbar.getInstance().getScaleRatio()[1];
        if(showLine) {
            g.setColor(Color.GREEN);
            g.setStroke(new BasicStroke(2));
            for(int i = 0; i < datapoints.length-1; i++) {
                g.drawLine((int)datapoints[i], (int)(bottom-chartResolution[1]*((values[i]-ybtm)/(ytop-ybtm))), (int)datapoints[i+1], (int)(bottom-chartResolution[1]*((values[i+1]-ybtm)/(ytop-ybtm))));
            }
            for(int i = 0; i < datapoints.length; i++) {
                drawPlot(g, datapoints[i], bottom-chartResolution[1]*((values[i]-ybtm)/(ytop-ybtm)), 10, x_values.get(i));
            }
        }
    }

    private double[] drawAxis(Graphics g, double w, double h, int xps, int yps) {
        left = (w*0.1<1)?10:(w*0.1);
        right = w - left;
        top = (h*0.1<1)?10:(h*0.1);
        bottom = h - top;
        chartResolution[0] = right-left;
        chartResolution[1] = bottom-top;
        double left_index = (w*0.05<1)?5:(w*0.05);
        double[] xpoints = new double[xps];
        g.setColor(Color.BLACK);
        g.drawLine((int)left, (int)top, (int)left, (int)bottom);
        g.drawLine((int)left, (int)bottom, (int)right, (int)bottom);
        for(int i = 0; i < xps; i++) {
            xpoints[i] = chartResolution[0]*(1-barwidth)/(xps+1)+chartResolution[0]*(barwidth)/(xps*2)+i*(chartResolution[0]*(1-barwidth)/(xps+1)+chartResolution[0]*(barwidth)/xps)+left;
        }
        for(int i = 1; i <= yps; i++) {
            double ymark = bottom-i*(chartResolution[1]/yps);
            g.drawLine((int)left - 3, (int)ymark, (int)left+3, (int)ymark);
            g.drawString(df.format(((highBound - lowBound)/yps)*i+lowBound)+(isTotalBased?"%":""),(int)left_index, (int)ymark+5);
        }
        return xpoints;
    }

    //Create Statement, send a Query
    private void askDB_GroupBy() {
        x_values.clear();
        y_values.clear();
        String query = (orderBy!=null)? "SELECT " + columnName +" , " + measurement + " FROM students GROUP BY " + columnName + orderBy:"SELECT " + columnName +" , " + measurement + " FROM students GROUP BY " + columnName;
        if(connection != null) {
            try{
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery(query);
                while(rs.next()) {
                    x_values.add(rs.getString(1));
                    y_values.add(rs.getDouble(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        redraw = true;
        if(isTotalBased) {
            YScaleToolbar.getInstance().updateScale(0.0,100.0);
        } else {
            YScaleToolbar.getInstance().updateScale(0.0, Collections.max(y_values)*1.1);
        }
    }

    private void askDB_ScaleGroup(double[] bounds) {
        x_values.clear();
        y_values.clear();
        double lowBound = 0;
        for(int i = 0; i < bounds.length; i++) {
            String query = "SELECT " + measurement + "  FROM students WHERE " + columnName + " >= " + Double.toString(lowBound) + " AND " + columnName + " < " + Double.toString(bounds[i]);
            if(connection != null) {
                try{
                    Statement s = connection.createStatement();
                    ResultSet rs = s.executeQuery(query);
                    while(rs.next()) {
                        x_values.add(df.format(lowBound) + " - " + df.format(bounds[i]));
                        y_values.add(rs.getDouble(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            lowBound = bounds[i];
        }
        redraw = true;
        if(isTotalBased) {
            YScaleToolbar.getInstance().updateScale(0.0,100.0);
        } else {
            YScaleToolbar.getInstance().updateScale(0.0, Collections.max(y_values)*1.1);
        }
    }

    public void getStudentByHomeCountry(){
        columnName = "homecountry";
        measurement = "COUNT(*)";
        askDB_GroupBy();
        repaint();
    }

    public void getStudentByMajor(){
        columnName = "major";
        measurement = "COUNT(*)";
        askDB_GroupBy();
        repaint();

    }
    public void getStudentByGraduation(){
        columnName = "graduation";
        measurement = "COUNT(*)";
        askDB_GroupBy();
        repaint();
    }

    public void getAverageGPAByMajor() {
        columnName = "major";
        measurement = "AVG(gpa)";
        askDB_GroupBy();
        repaint();
    }

    public void getAverageCreditByGraduation() {
        columnName = "graduation";
        measurement = "AVG(credit_applied)";
        askDB_GroupBy();
        repaint();
    }

    public void getStudentsByGPA() {
        columnName = "gpa";
        measurement = "COUNT(*)";
        askDB_ScaleGroup(gpaBins);
        repaint();
    }

    public boolean getIsTotalBased(){
        return this.isTotalBased;
    }

    public double getYMax(){
        return Collections.max(y_values);
    }

    public void setTotalBased(boolean toggle){
        isTotalBased = toggle;
        if(isTotalBased) {
            YScaleToolbar.getInstance().updateScale(0.0,100.0);
        } else {
            YScaleToolbar.getInstance().updateScale(0.0, Collections.max(y_values)*1.1);
        }
        repaint();
    }

    public void setyPoints(int i){
        this.yPoints = i;
        repaint();
    }

    public void setLow_HighBounds(double low, double high) {
        this.lowBound = low;
        this.highBound = high;
        repaint();
    }

    public void showExactValue(boolean toggle) {
        this.showDetail = toggle;
        repaint();
    }

    public void showBarChart(boolean toggle) {
        this.showBar = toggle;
        this.isXLabel = toggle;
        repaint();
    }

    public void showLineChart(boolean toggle) {
        this.showLine = toggle;
        repaint();
    }

    public void setOrderBy(OrderMode order) {
        switch(order) {
            case ASCD:
                orderBy = " ORDER BY " + measurement;
                break;
            case DESC:
                orderBy = " ORDER BY " + measurement + " DESC";
                break;
            case NORM:
                orderBy = null;
                break;
        }
        repaint();
    }
}
