package GUI;

import model.DBConnectManager;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class ChartPanel extends JPanel {
    //Singleton for Chart Panel
    private static ChartPanel chartPanel;
    private DBConnectManager dbManager;

    //chart related global fields
    private double chartWidthResolution;
    private double chartHeightResolution;
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double barWidth;
    private String x_name;
    private String y_name;

    //user interaction fields
    private int scaleCount;
    private Color color;

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");

    private ChartPanel() {
        dbManager = DBConnectManager.getInstance();
        x_name = "CREDITS_APPLIED";
        y_name = "CREDITS_PASSED";
        dbManager.requestDB(x_name, y_name);
        barWidth = 0.7;
        scaleCount = 10;
        color = new Color(109, 195, 209);
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
        drawAxis(g, w, h, scaleCount);
        for(int i = 0; i < dbManager.getSize(); i++) {
            drawPlot(g, chartWidthResolution*(dbManager.getXin(i)/dbManager.getXMax())+top, chartHeightResolution*(dbManager.getYin(i)/dbManager.getYMax())+left, 5);
        }
    }

    private void drawPlot(Graphics2D g, double cx, double cy, int r) {
        g.setColor(Color.GREEN);
        g.fillOval((int)(cx-r/2), (int)(cy-r/2), r, r);
    }

    //TODO add color parameter

    private void drawAxis(Graphics g, double w, double h, int pts) {
        left = (w*0.1<1)?10:(w*0.1);
        right = w - left;
        top = (h*0.1<1)?10:(h*0.1);
        bottom = h - top;
        chartWidthResolution = right-left;
        chartHeightResolution = bottom-top;
        double left_index = (w*0.05<1)?5:(w*0.05);
        double bottom_index = (h*0.05<1)?5:(h*0.05);
        g.setColor(Color.BLACK);
        g.drawLine((int)left, (int)top, (int)left, (int)bottom);
        g.drawLine((int)left, (int)bottom, (int)right, (int)bottom);
        g.drawString(x_name, (int)(right-x_name.length()*3), (int)(bottom+bottom_index)+10);
        g.drawString(y_name, (int)(left_index-(y_name.length()/2)*3), (int)top-10);
        for(int i = 1; i <= pts; i++) {
            double xmark = left+i*(chartWidthResolution/pts);
            double ymark = bottom-i*(chartHeightResolution/pts);
            g.drawLine((int)xmark, (int)bottom - 3, (int)xmark, (int)bottom + 3);
            g.drawLine((int)left - 3, (int)ymark, (int)left+3, (int)ymark);
            g.drawString(df.format((dbManager.getYMax()/pts)*i),(int)left_index, (int)ymark+5);
            g.drawString(df.format((dbManager.getXMax()/pts)*i), (int)xmark+5, (int)(bottom+bottom_index));
        }
    }


    public void getCreditsAttempted_CreditsPassed(){
        x_name = "credits_applied";
        y_name = "credits_passed";
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getCreditsAttempted_GPA(){
        x_name = "credits_applied";
        y_name = "gpa";
        dbManager.requestDB(x_name,y_name);
        repaint();

    }
    public void getCreditsPassed_GPA(){
        x_name = "credits_passed";
        y_name = "gpa";
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getAge_GPA() {
        x_name = "age";
        y_name = "gpa";
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getAge_CurrentCredits() {
        x_name = "age";
        y_name = "current_credits";
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void setScaleCount(int i){
        this.scaleCount = i;
        repaint();
    }

    public void showExactValue(boolean toggle) {

        repaint();
    }
}
