package gui;

import helper.CalenderToolBar;
import helper.DateTimeUnit;
import helper.LineGraph;
import model.WebAPIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class OverallChart extends JPanel implements MouseListener, MouseMotionListener {
    //My Self
    private static OverallChart overallChart;
    private HashMap<String, ArrayList<Point2D.Double>> chartMap;
    private DateTimeUnit unit;

    //Model & Other Panel
    private WebAPIManager webAPIManager;
    private DetailChart detailChart;
    private CalenderToolBar calenderToolBar;

    //Chart Related Field
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double chartWidth;
    private double chartHeight;
    private ArrayList<LineGraph> graphs;
    private LineGraph highlited;

    //Mouse Action Related Field
    private boolean showBox;
    private Point mouseUp;
    private Point mouseDown;
    private Rectangle selectionBox;

    //tooltip related field
    private boolean showTooltip;
    private ToolTipManager ttm;

    private OverallChart() {
        webAPIManager = WebAPIManager.getInstance();
        detailChart = DetailChart.getInstance();
        calenderToolBar = CalenderToolBar.getInstance();
        chartMap = new HashMap<>();
        graphs = new ArrayList<>();
        unit = DateTimeUnit.DAY;
        showBox = false;
        selectionBox = new Rectangle();
        ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(0);
        ttm.setDismissDelay(10000);
        showTooltip = false;
        String[] countries = webAPIManager.getCountries();
        for(int i = 0; i < countries.length; i++) {
            chartMap.put(countries[i], new ArrayList<>());
            LineGraph.setColorScheme(countries[i], Color.getHSBColor((float)i/countries.length, 0.8f, 0.7f));
        }

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public static OverallChart getInstance() {
        if(overallChart == null) {
            overallChart = new OverallChart();
        }
        return overallChart;
    }

    @Override
    public void paintComponent(Graphics g1) {
        double w = getWidth();
        double h = getHeight();
        Graphics2D g = (Graphics2D)g1;
        g.setColor(new Color(245,255,255));
        g.fillRect(0,0,(int)w,(int)h);
        left = (w*0.08<1)?10:(w*0.08);
        right = w - left;
        top = (h*0.08<1)?10:(h*0.08);
        bottom = h - top;
        chartWidth = right-left;
        chartHeight = bottom-top;
        drawLineChart(g);
        updateGraphs();
        String[] countries = webAPIManager.getCountries();
        for(int i = 0; i < graphs.size(); i++) {
            g.setColor(graphs.get(i).getColor());
            g.draw(graphs.get(i).getGraph());
            g.drawString(countries[i], (int)left-18, (int)chartMap.get(countries[i]).get(0).getY()+6);
        }
        if(highlited != null) {
            g.setStroke(new BasicStroke(1.5f));
            g.setColor(highlited.getColor());
            g.draw(highlited.getGraph());
            g.setStroke(new BasicStroke(1.0f));
        }
        if(showBox) {
            g.setColor(new Color(139, 176, 194, 70));
            g.fill(selectionBox);
            g.setColor(Color.WHITE);
            g.draw(selectionBox);
        }
    }

    private void drawLineChart(Graphics2D g) {
        chartMap.forEach((key,value) -> value.clear());
        String[] byDates = webAPIManager.getDates(calenderToolBar.getFromDate(), calenderToolBar.getToDate());
        System.out.printf("Start: %s, End: %s\n", byDates[0], byDates[byDates.length-1]);
        g.setColor(Color.BLACK);
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), f.getStyle(), 8));
        g.drawLine((int)left, (int)top, (int)left, (int)bottom);
        g.drawLine((int)left, (int)bottom, (int)right, (int)bottom);
        for(int i = 0; i < byDates.length; i++) {
            int x_gap = (int)(chartWidth/byDates.length);
            updateChartPoints(left + x_gap * i, webAPIManager.getIndexOfDates(byDates[byDates.length - 1 - i]));
            g.drawLine((int)left + x_gap * (i+1), (int)bottom-3, (int)left + x_gap * (i+1), (int)bottom + 3);
            g.drawString(byDates[byDates.length - 1 - i].substring(5), (int)left - 10 + x_gap * i,(int)bottom + 15);
        }
        g.drawString(byDates[0].substring(5), (int)right-10, (int)bottom + 15);
        updateChartPoints(right, 0);
    }

    private void updateChartPoints(double xValue, int index) {
        String[] countries = webAPIManager.getCountries();
        for(String s : countries) {
            List<Double> rates = webAPIManager.getAllRatesByCountry(s);
            double min = Collections.min(rates);
            double max = Collections.max(rates);
            if(min == max) {
                chartMap.get(s).add(new Point2D.Double(xValue, bottom-(rates.get(index)/max*chartHeight)));
            } else {
                chartMap.get(s).add(new Point2D.Double(xValue, bottom-((rates.get(index)-min)/(max-min)*chartHeight)));
            }
        }
    }

    private void updateGraphs() {
        graphs.clear();
        chartMap.forEach((k,v) -> {
            LineGraph lg = new LineGraph(k);
            for(int i = 0; i < v.size(); i++) {
                lg.addPoints(v.get(i).getX(), v.get(i).getY());
            }
            graphs.add(lg);
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = e.getPoint();
        showBox = true;
        if(highlited != null) {
            detailChart.updateCountry(highlited.getCountryName(), highlited.getColor());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseUp = e.getPoint();
        selectionBox.setFrameFromDiagonal(mouseDown, mouseUp);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showBox = false;

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(LineGraph lg : graphs) {
            if(lg.onLine(e.getPoint())) {
                highlited = lg;
                setToolTipText(lg.getTooltipString());
                break;
            }
        }
        if(highlited != null && !highlited.onLine(e.getPoint())) {
            highlited = null;
            setToolTipText(null);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
