package gui;

import helper.CalenderToolBar;
import helper.DetailViewMode;
import helper.LineGraph;
import model.WebAPIManager;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class DetailChart extends JPanel {
    private static DetailChart detailChart;
    private WebAPIManager webAPIManager;
    private CalenderToolBar calenderToolBar;
    private DetailViewMode mode;
    private String country_1;
    private String country_2;
    private boolean isNextTargetTwo;

    //Chart Related Field
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double chartWidth;
    private double chartHeight;
    private LineGraph graph_1;
    private LineGraph graph_2;

    //Other Fields
    private static final DecimalFormat df = new DecimalFormat("#.##");

    private DetailChart() {
        mode = DetailViewMode.DUAL;
        webAPIManager = WebAPIManager.getInstance();
        calenderToolBar = CalenderToolBar.getInstance();
        isNextTargetTwo = true;

    }

    public static DetailChart getInstance() {
        if(detailChart == null) {
            detailChart = new DetailChart();
        }
        return detailChart;
    }

    @Override
    public void paintComponent(Graphics g1) {
        double w = getWidth();
        double h = getHeight();
        Graphics2D g = (Graphics2D) g1;
        g.setColor(new Color(255,255,245));
        g.fillRect(0, 0, (int)w, (int)h);
        g.setColor(Color.BLACK);
        left = (w*0.08<1)?10:(w*0.08);
        right = w - left;
        top = (h*0.05<1)?10:(h*0.05);
        bottom = h - top;
        chartWidth = right-left;
        chartHeight = bottom-top;
        drawDetailChart(g);

    }

    private void drawDetailChart(Graphics2D g) {
        //Show One Selected Chart
        if(mode == DetailViewMode.SINGLE) {
            if(country_1 != null) {
                updateGraphs(left, top, chartWidth, chartHeight, country_1, 1);
                updateChart(left, top, chartWidth, chartHeight, country_1, 1, g);
                g.setStroke(new BasicStroke(1.5f));
                g.setColor(graph_1.getColor());
                g.draw(graph_1.getGraph());
            } else {
                g.setColor(Color.BLACK);
                g.drawLine((int)left, (int)top, (int)left, (int)bottom);
                g.drawLine((int)left, (int)bottom, (int)right, (int)bottom);
            }
        } else {
            double dualChartWidth = chartWidth/2-20;
            double left2 = left + chartWidth/2+20;
            //Chart 1
            if(country_1 != null) {
                updateGraphs(left, top, dualChartWidth, chartHeight, country_1, 1);
                updateChart(left, top, dualChartWidth, chartHeight, country_1, 1, g);
                g.setStroke(new BasicStroke(1.5f));
                g.setColor(graph_1.getColor());
                g.draw(graph_1.getGraph());
            } else {
                g.setColor(Color.BLACK);
                g.drawLine((int)left, (int)top, (int)left, (int)bottom);
                g.drawLine((int)left, (int)bottom, (int)(left+dualChartWidth), (int)bottom);
            }
            //Chart 2
            if(country_2 != null) {
                updateGraphs(left2, top, dualChartWidth, chartHeight, country_2, 2);
                updateChart(left2, top, dualChartWidth, chartHeight, country_2, 2, g);
                g.setStroke(new BasicStroke(1.5f));
                g.setColor(graph_2.getColor());
                g.draw(graph_2.getGraph());
            } else {
                g.setColor(Color.BLACK);
                g.drawLine((int)left2, (int)top, (int)left2, (int)bottom);
                g.drawLine((int)left2, (int)bottom, (int)right, (int)bottom);
            }
        }
    }

    private void updateChart(double l, double t, double w, double h, String cname, int target, Graphics2D g) {
        g.setColor(Color.BLACK);
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), f.getStyle(), 8));
        g.drawLine((int)l, (int)t, (int)l, (int)(t+h));
        g.drawLine((int)l, (int)(t+h), (int)(l+w), (int)(t+h));
        double min = (target == 1)?graph_1.getMin():graph_2.getMin();
        double max = (target == 1)?graph_1.getMax():graph_2.getMax();
        double heap = (max-min)/5;
        //Draw Y scale bar
        int y_gap = (int)(h/5);
        for(int i = 0; i < 5; i++) {
            g.drawLine((int)l - 3, (int)t + y_gap * i, (int)l + 3, (int)t + y_gap * i);
            String s = df.format(max-(heap*i));
            g.drawString(s, (int)l - s.length() * 7, (int)t + 3 + y_gap * i);
        }
        String s = df.format(min);
        g.drawString(df.format(min), (int)l - s.length() * 7, (int)(t+h));

        //Draw X scale bar
        String[] byDates = webAPIManager.getDates(calenderToolBar.getFromDate(), calenderToolBar.getToDate());
        int x_gap = (int)(w/(byDates.length-1));
        for(int i = 0; i < byDates.length-1; i++) {
            g.drawLine((int)l + x_gap * (i+1), (int)(t+h) - 3, (int)l + x_gap * (i+1), (int)(t+h) + 3);
            g.drawString(byDates[byDates.length-1-i].substring(5), (int)l - 10 + x_gap * i, (int)(t+h) + 13);
        }
        g.drawString(byDates[0].substring(5), (int)(l+w) - 10, (int)(t+h) + 13);
    }

    private void updateGraphs(double l, double t, double w, double h, String cname, int target) {
        LineGraph graph = new LineGraph(cname);
        List<Double> dataset = webAPIManager.getAllRatesByCountry(cname);
        String[] byDates = webAPIManager.getDates(calenderToolBar.getFromDate(), calenderToolBar.getToDate());
        double min = Collections.min(dataset);
        double max = Collections.max(dataset);
        double gap = w/dataset.size();
        for(int i = 0; i < byDates.length; i++) {
            Double v = dataset.get(webAPIManager.getIndexOfDates(byDates[byDates.length - 1 - i]));
            var v1 = (v - min) / (max - min) * h;
            graph.addPoints(l+(gap*i), t+h-v1);
        }
        if(target == 1) {
            graph_1 = graph;
        } else if(target == 2) {
            graph_2 = graph;
        }
    }

    public void setMode(DetailViewMode dvm) {
        mode = dvm;
        repaint();
    }

    public void updateCountry(String cname, Color color) {
        if(mode == DetailViewMode.SINGLE) {
            country_1 = cname;
        } else {
            if(isNextTargetTwo) {
                country_1 = cname;
                isNextTargetTwo = false;
            } else {
                country_2 = cname;
                isNextTargetTwo = true;
            }
        }
        repaint();
    }

    public void resetView() {
        country_1 = null;
        country_2 = null;
        graph_1 = null;
        graph_2 = null;
        repaint();
    }
}
