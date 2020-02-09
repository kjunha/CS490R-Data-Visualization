package GUI;

import model.DBConnectManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

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
    private String x_name;
    private String y_name;
    private Point min;
    private boolean showGender;

    //mouse listener related field
    private Rectangle2D.Double box;
    private Point mouseDownPoint;
    private Point mouseUpPoint;
    private boolean showBox;
    private boolean isZoom;

    //tooltip related field
    private boolean showTooltip;
    private ToolTipManager ttm;

    //user interaction fields
    private int scaleCount;
    private Color color;

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");
    private Timer boxAction;

    private ChartPanel() {
        dbManager = DBConnectManager.getInstance();
        x_name = "CREDITS_APPLIED";
        y_name = "CREDITS_PASSED";
        dbManager.requestDB(x_name, y_name);
        scaleCount = 10;
        color = new Color(109, 195, 209);
        box = new Rectangle2D.Double();
        isZoom = false;
        min = new Point(0,0);
        ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(0);
        ttm.setDismissDelay(10000);
        showTooltip = false;
        showGender = false;


        addMouseListener(this);
        addMouseMotionListener(this);

        boxAction = new Timer(50, e -> {
            double maxX = (mouseDownPoint.x < mouseUpPoint.x)?mouseUpPoint.x:mouseDownPoint.x;
            double minX = (mouseDownPoint.x < mouseUpPoint.x)?mouseDownPoint.x:mouseUpPoint.x;
            double maxY = (mouseDownPoint.y < mouseUpPoint.y)?mouseUpPoint.y:mouseDownPoint.y;
            double minY = (mouseDownPoint.y < mouseUpPoint.y)?mouseDownPoint.y:mouseUpPoint.y;

        });
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
            if(showGender) {
                if(dbManager.getGenderin(i).equals("F")) {
                    drawPlot(g,
                            left + chartWidthResolution*((dbManager.getXin(i)-min.x)/(dbManager.getXMax()-min.x)),
                            bottom - chartHeightResolution*((dbManager.getYin(i)-min.y)/(dbManager.getYMax()-min.y)),
                            6,
                            Color.RED);
                } else {
                    drawPlot(g,
                            left + chartWidthResolution*((dbManager.getXin(i)-min.x)/(dbManager.getXMax()-min.x)),
                            bottom - chartHeightResolution*((dbManager.getYin(i)-min.y)/(dbManager.getYMax()-min.y)),
                            6,
                            Color.BLUE);
                }
            } else {
                drawPlot(g,
                        left + chartWidthResolution*((dbManager.getXin(i)-min.x)/(dbManager.getXMax()-min.x)),
                        bottom - chartHeightResolution*((dbManager.getYin(i)-min.y)/(dbManager.getYMax()-min.y)),
                        6,
                        Color.GREEN);
            }
        }
        g.setColor(Color.BLUE);
        if(showBox) {
            g.draw(box);
        }

    }

    private void drawPlot(Graphics2D g, double cx, double cy, int r, Color c) {
        g.setColor(c);
        g.fillOval((int)(cx-r/2), (int)(cy-r/2), r, r);
    }

    //TODO add color parameter

    private void drawAxis(Graphics2D g, double w, double h, int pts) {
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
        g.drawString(x_name, (int)(right-x_name.length()*7), (int)(bottom+bottom_index)+13);
        g.rotate(-Math.PI/2, (left-left_index)-10, (int)(top+y_name.length()*7));
        g.drawString(y_name, (int)(left-left_index)-10, (int)(top+y_name.length()*7));
        g.rotate(Math.PI/2,(left-left_index)-10, (int)(top+y_name.length()*7));
        for(int i = 1; i <= pts; i++) {
            double xmark = left+i*(chartWidthResolution/pts);
            double ymark = bottom-i*(chartHeightResolution/pts);
            g.drawLine((int)xmark, (int)bottom - 3, (int)xmark, (int)bottom + 3);
            g.drawLine((int)left - 3, (int)ymark, (int)left+3, (int)ymark);
            g.drawString(df.format(min.y+((dbManager.getYMax()-min.y)/pts)*i),(int)left_index, (int)ymark+5);
            String xs;
            g.drawString(xs = df.format(min.x+((dbManager.getXMax()-min.x)/pts)*i), (int)xmark-(xs.length()/2)*7, (int)(bottom+bottom_index));
        }
    }


    public void getCreditsAttempted_CreditsPassed(){
        x_name = "credits_applied";
        y_name = "credits_passed";
        min.setLocation(0,0);
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getCreditsAttempted_GPA(){
        x_name = "credits_applied";
        y_name = "gpa";
        min.setLocation(0,0);
        dbManager.requestDB(x_name,y_name);
        repaint();

    }
    public void getCreditsPassed_GPA(){
        x_name = "credits_passed";
        y_name = "gpa";
        min.setLocation(0,0);
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getAge_GPA() {
        x_name = "age";
        y_name = "gpa";
        min.setLocation(0,0);
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void getAge_CurrentCredits() {
        x_name = "age";
        y_name = "current_credits";
        min.setLocation(0,0);
        dbManager.requestDB(x_name,y_name);
        repaint();
    }

    public void setScaleCount(int i){
        this.scaleCount = i;
        repaint();
    }

    public void showExactValue(boolean toggle) {
        this.showTooltip = toggle;
        repaint();
    }

    public void showGenderInfo(boolean toggle) {
        this.showGender = toggle;
        repaint();
    }

    public void resetToOriginal() {
        dbManager.resetFrame();
        min.setLocation(0,0);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseDownPoint = new Point(x,y);
        showBox = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isZoom) {
            double maxX = (mouseDownPoint.x < mouseUpPoint.x)?mouseUpPoint.x:mouseDownPoint.x;
            double minX = (mouseDownPoint.x < mouseUpPoint.x)?mouseDownPoint.x:mouseUpPoint.x;
            double maxY = (mouseDownPoint.y < mouseUpPoint.y)?mouseUpPoint.y:mouseDownPoint.y;
            double minY = (mouseDownPoint.y < mouseUpPoint.y)?mouseDownPoint.y:mouseUpPoint.y;
            for(int i = 0; i < 30; i++) {
                box.setFrameFromDiagonal(minX-((minX-left)/30*i),minY-((minY-top)/30*i),maxX+((right-maxX)/30*i),maxY+((bottom-maxY)/30*i));
                paintImmediately((int)(top*0.8), (int)(left*0.8), (int)(chartWidthResolution*1.2), (int)(chartHeightResolution*1.2));
                try { Thread.sleep(3); } catch (InterruptedException ex) { ex.printStackTrace(); }
            }
            dbManager.updateFrame(
                    (minX - left)/chartWidthResolution*(dbManager.getXMax()-min.x)+min.x,
                    (bottom - maxY)/chartHeightResolution*(dbManager.getYMax()-min.y)+min.y,
                    (maxX - left)/chartWidthResolution*(dbManager.getXMax()-min.x)+min.x,
                    (bottom - minY)/chartHeightResolution*(dbManager.getYMax()-min.y)+min.y
                    );
        }
        showBox = false;
        min.setLocation(dbManager.getXMin(), dbManager.getYMin());
        isZoom = false;
        boxAction.stop();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseUpPoint = new Point();
        isZoom = true;
        mouseUpPoint.x = e.getX();
        mouseUpPoint.y = e.getY();
        box.setFrameFromDiagonal(mouseDownPoint.x, mouseDownPoint.y, mouseUpPoint.x, mouseUpPoint.y);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(showTooltip) {
            int x = e.getX();
            int y = e.getY();
            for(int i = 0; i < dbManager.getSize(); i++) {
                int xval = (int)(left + chartWidthResolution*((dbManager.getXin(i)-min.x)/(dbManager.getXMax()-min.x)));
                int yval = (int)(bottom - chartHeightResolution*((dbManager.getYin(i)-min.y)/(dbManager.getYMax()-min.y)));
                if((xval-4 < x & x < xval+4)&&(yval-4 < y & y < yval+4)) {
                    setToolTipText(x_name + ": " + dbManager.getXin(i) + ", "+ y_name + ": " + dbManager.getYin(i) + ((showGender)?", Gender: " + dbManager.getGenderin(i):""));
                    break;
                } else {
                    setToolTipText(null);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
