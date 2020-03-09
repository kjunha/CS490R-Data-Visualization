package GUI;

import model.DBConnectManager;

import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    private ArrayList<ParallelGraph> graphs_total;
    private ArrayList<ParallelGraph> graphs_selected;
    private ArrayList<ParallelGraph> graphs_inScope;
    private boolean scopeRenewRequested;
    private ParallelGraph highlighted;
    private int[] xPoints; //Canvas x coordinate numbers of the bar
    private double[] maxPolynomialYs; //min-max values of each column
    private double[] minPolynomialYs;
    private ArrayList<ArrayList<String>> uniqueSetList; //All unique string values for binomial data values

    //Drag Box
    private boolean showBox;
    private Point mouseDown;
    private Point mouseUp;
    private Rectangle selectionBox;

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
        graphs_total = new ArrayList<>();
        graphs_selected = new ArrayList<>();
        graphs_inScope = new ArrayList<>();
        highlighted = null;
        ttm.setInitialDelay(0);
        ttm.setDismissDelay(10000);
        showTooltip = false;
        uniqueSetList = new ArrayList<>();
        selectionBox = new Rectangle();
        showBox = false;
        scopeRenewRequested = true;

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
        g.setStroke(new BasicStroke());
        for(ParallelGraph pg : graphs_total) {
            if(graphs_inScope.indexOf(pg) == -1) {
                g.setColor(new Color(120,120,255, 20));
                g.draw(pg.getPolyLine());
            } else {
                g.setColor(Color.BLUE);
                g.draw(pg.getPolyLine());
            }
        }
        if(highlighted != null) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(1.5f));
            g.draw(highlighted.getPolyLine());
        }
        for(ParallelGraph pg : graphs_selected) {
            g.setColor(Color.CYAN);
            g.setStroke(new BasicStroke(1.3f));
            g.draw(pg.getPolyLine());
        }
        if(showBox) {
            g.setColor(new Color(139, 176, 194, 70));
            g.fill(selectionBox);
            g.setColor(Color.WHITE);
            g.draw(selectionBox);
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
        graphs_total.clear();
        for(int i = 0; i < dbManager.getEntityCount(); i++) {
            double[] yRatio = new double[dbManager.getKeySetCount()];
            int[] yPoints = new int[dbManager.getKeySetCount()];
            //index counter for binomial(String) values.
            int binomialCounter = 0;
            //(value-min) / (max-min) = ratio ->> ways to put min values at the bottom of the bar
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
            int id = graphs_total.size();
            graphs_total.add(new ParallelGraph(xPoints, yPoints, id));
        }
        if(scopeRenewRequested) {
            graphs_inScope.clear();
            graphs_inScope.addAll(graphs_total);
            scopeRenewRequested = false;
        } else {
            for(ParallelGraph pg : graphs_inScope) {
                graphs_inScope.set(graphs_inScope.indexOf(pg),graphs_total.get(graphs_total.indexOf(pg)));
            }
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

    private String buildToolTipText(int index) {
        String tooltipText = "";
        for(int j = 0; j < dbManager.getKeySetCount(); j++) {
            String key = dbManager.getKeySetValueIn(j);
            tooltipText = tooltipText + key + ": " + dbManager.getEntity(index).get(key);
            if(j < dbManager.getKeySetCount() - 1) {
                tooltipText += ", ";
            }
        }
        return tooltipText;
    }

    //Public methods section

    /**
     * Update target table to display by user request.
     * @param tableName a name of the table in the database
     */
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
        resetScope();
        repaint();
    }

    public void resetScope() {
        scopeRenewRequested = true;
        repaint();
    }

    //Mouse Listener Interface methods
    @Override
    public void mouseMoved(MouseEvent e) {
        for(ParallelGraph pg : graphs_inScope) {
            if(pg.onLine(e.getPoint())) {
                highlighted = pg;
                setToolTipText(buildToolTipText(graphs_total.indexOf(pg)));
                break;
            }
        }
        if(highlighted != null && !highlighted.onLine(e.getPoint())) {
            highlighted = null;
            setToolTipText(null);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = e.getPoint();
        showBox = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseUp = e.getPoint();
        selectionBox.setFrameFromDiagonal(mouseDown, mouseUp);
        for(ParallelGraph pg:graphs_inScope) {
            if(graphs_selected.indexOf(pg) == -1 && pg.isContainedIn(selectionBox)){
                graphs_selected.add(pg);
            } else if(graphs_selected.indexOf(pg) != -1 && !pg.isContainedIn(selectionBox)) {
                    graphs_selected.remove(graphs_selected.indexOf(pg));

            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showBox = false;
        if(graphs_selected.size() > 0) {
            graphs_inScope.clear();
            graphs_inScope.addAll(graphs_selected);
        }
        graphs_selected.clear();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
