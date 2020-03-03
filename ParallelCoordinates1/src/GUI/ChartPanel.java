package GUI;

import model.DBConnectManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener {
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
    private ArrayList<GeneralPath> graphs;
    private int[] xPoints;
    private double[] maxPolynomialYs;
    private double[] minPolynomialYs;
    private ArrayList<ArrayList<String>> uniqueSetList;

    //mouse listener related field

    //tooltip related field
    private boolean showTooltip;
    private ToolTipManager ttm;

    //user interaction fields

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");

    private ChartPanel() {
        dbManager = DBConnectManager.getInstance();
        ttm = ToolTipManager.sharedInstance();
        graphs = new ArrayList<>();
        ttm.setInitialDelay(0);
        ttm.setDismissDelay(10000);
        showTooltip = false;
        uniqueSetList = new ArrayList<>();

        updateTable("roster2019");
        addMouseListener(this);
        addMouseMotionListener(this);
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
        drawAxis(g, w, h, dbManager.getKeySetCount());
        updateView();
        drawLabels(g);
        g.setColor(Color.BLACK);
        for(GeneralPath gp : graphs) {
            g.draw(gp);
        }
    }

    //TODO add color parameter

    private void drawAxis(Graphics2D g, double w, double h, int pts) {
        left = (w*0.1<1)?10:(w*0.1);
        right = w - left;
        top = (h*0.1<1)?10:(h*0.1);
        bottom = h - top;
        chartWidthResolution = right-left;
        chartHeightResolution = bottom-top;
        g.setColor(Color.BLACK);
        xPoints = new int[pts];
        for(int i = 0; i < pts; i++) {
            xPoints[i] = (int)(left+(chartWidthResolution/(pts-1))*i);
            g.drawLine(xPoints[i], (int)top, xPoints[i], (int)bottom);
            String label = dbManager.getKeySetValueIn(i);
            g.drawString(label, xPoints[i] - label.length()/2*8, (int)bottom+25);
        }
    }
    private void updateView() {
        graphs.clear();
        for(int i = 0; i < dbManager.getEntityCount(); i++) {
            GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
            double[] yRatio = new double[dbManager.getKeySetCount()];
            int[] yPoints = new int[dbManager.getKeySetCount()];
            int binomialCounter = 0;
            for(int j = 0; j < dbManager.getKeySetCount(); j++) {
                if(dbManager.getDataType(j).toLowerCase().equals("double")) {
                    yRatio[j] = (((Double)dbManager.getEntity(i).get(dbManager.getKeySetValueIn(j))-minPolynomialYs[j])/(maxPolynomialYs[j]-minPolynomialYs[j]));
                } else if(dbManager.getDataType(j).toLowerCase().contains("int")) {
                    yRatio[j] = ((Double.parseDouble(Integer.toString((Integer)dbManager.getEntity(i).get(dbManager.getKeySetValueIn(j))))-minPolynomialYs[j])/(maxPolynomialYs[j]-minPolynomialYs[j]));
                } else {
                    for(String item : uniqueSetList.get(binomialCounter)) {
                        if(item.equals(dbManager.getEntity(i).get(dbManager.getKeySetValueIn(j)))) {
                            yRatio[j] = ((double)(uniqueSetList.get(binomialCounter).indexOf(item)+1)/(uniqueSetList.get(binomialCounter).size()+1));
                        }
                    }
                    binomialCounter = (binomialCounter+1 < uniqueSetList.size())?binomialCounter+1:binomialCounter;
                }
                yPoints[j] = (int)(bottom-yRatio[j]*chartHeightResolution);
            }
            gp.moveTo(xPoints[0], yPoints[0]);
            for(int j = 1; j < xPoints.length; j++) {
                gp.lineTo(xPoints[j], yPoints[j]);
            }
            graphs.add(gp);
        }
    }

    private void drawLabels(Graphics2D g) {
        g.setColor(Color.RED);
        int binomialCounter = 0;
        for(int i = 0; i < dbManager.getKeySetCount(); i++) {
            if(dbManager.getDataType(i).toLowerCase().equals("double") || dbManager.getDataType(i).toLowerCase().contains("int")) {
                for(int j = 0; j <= 5; j++) {
                    double polynomialLabel = minPolynomialYs[i] + ((maxPolynomialYs[i]-minPolynomialYs[i])/5)*j;
                    if(dbManager.getDataType(i).toLowerCase().equals("double")) {
                        g.drawString(df.format(polynomialLabel), xPoints[i] + 10, (int)(bottom - chartHeightResolution/5*j));
                    } else {
                        g.drawString(Integer.toString((int)polynomialLabel), xPoints[i] + 10, (int)(bottom - chartHeightResolution/5*j));
                    }
                }
            } else {
                for(int j = 0; j < uniqueSetList.get(binomialCounter).size(); j++) {
                    String binomialLabel = uniqueSetList.get(binomialCounter).get(j);
                    g.drawString(binomialLabel, xPoints[i] + 10, (int)(bottom - chartHeightResolution/(uniqueSetList.get(binomialCounter).size()+1)*(j+1)));
                }
                binomialCounter = (binomialCounter+1 < uniqueSetList.size())?binomialCounter+1:binomialCounter;
            }
        }
    }

    //Public methods section
    public void updateTable(String tableName) {
        dbManager.loadTable(tableName);
        maxPolynomialYs = new double[dbManager.getKeySetCount()];
        minPolynomialYs = new double[dbManager.getKeySetCount()];
        for(int i = 0; i < dbManager.getKeySetCount(); i++) {
            if(dbManager.getDataType(i).toLowerCase().equals("double")) {
                minPolynomialYs[i] = Collections.min(dbManager.getValueList(i).stream().map(entity -> (Double)entity).collect(Collectors.toList()));
                maxPolynomialYs[i] = Collections.max(dbManager.getValueList(i).stream().map(entity -> (Double)entity).collect(Collectors.toList()));
            } else if(dbManager.getDataType(i).toLowerCase().contains("int")) {
                minPolynomialYs[i] = Collections.min(dbManager.getValueList(i).stream().map(entity -> (Integer)entity).collect(Collectors.toList()));
                maxPolynomialYs[i] = Collections.max(dbManager.getValueList(i).stream().map(entity -> (Integer)entity).collect(Collectors.toList()));
            } else {
                Set<String> uniqueSet = dbManager.getValueList(i).stream().map(entity -> (String) entity).collect(Collectors.toSet());
                uniqueSetList.add(new ArrayList<>(uniqueSet));
                maxPolynomialYs[i] = uniqueSet.size();
            }
        }
        repaint();
    }

    //Mouse Listener Interface methods
    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
