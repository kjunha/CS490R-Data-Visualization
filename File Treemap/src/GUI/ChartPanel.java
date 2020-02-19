package GUI;

import model.TreeMap;
import model.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartPanel extends JPanel implements MouseListener, MouseMotionListener {
    //Singleton for Chart Panel
    private static ChartPanel chartPanel;

    //TreeMap object related field
    private TreeMap treeMap;
    private List<Rectangle2D> boxStructure;
    private HashMap<String, Color> colorMap;

    //Draw box field
    boolean isHorizontalSplit; //true when the rectangle splits horizontally.

    //tooltip related field
    private boolean showTooltip;
    private ToolTipManager ttm;

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");
    private Timer boxAction;

    private ChartPanel() {
        treeMap = TreeMap.getInstance();
        boxStructure = new ArrayList<>();
        isHorizontalSplit = true;
        ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(0);
        ttm.setDismissDelay(10000);
        showTooltip = false;

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
        buildStructure(0,0,w,h,true,treeMap.getRoot());
        for(int i = 0; i < boxStructure.size(); i++) {
            g.setColor(Color.BLUE);
            g.fill(boxStructure.get(i));
            g.setColor(Color.GREEN);
            g.draw(boxStructure.get(i));
        }
    }

    /**
     *
     * @param left left x
     * @param top top y
     * @param width width of rectangle
     * @param height height of rectangle
     * @param h_or_v true for horizontal, false for vertical
     * @param tn tree node to start with
     */
    public void buildStructure(double left, double top, double width, double height, boolean h_or_v, TreeNode tn) {
        if(tn.getIsFile()) {
            boxStructure.add(new Rectangle2D.Double(left,top,width,height));
        } else {
            if (h_or_v) {
                double x_position = left;
                for(int i = 0; i < tn.getSubSize(); i++) {
                    buildStructure(x_position, top, width*(tn.getSubInstanceIn(i).getFileSize()/tn.getFileSize()), height, false, tn.getSubInstanceIn(i));
                    x_position += width*(tn.getSubInstanceIn(i).getFileSize()/tn.getFileSize());
                }
            } else {
                double y_position = top;
                for(int i = 0; i < tn.getSubSize(); i++) {
                    buildStructure(left, y_position, width, height*(tn.getSubInstanceIn(i).getFileSize()/tn.getFileSize()), true, tn.getSubInstanceIn(i));
                    y_position += height*(tn.getSubInstanceIn(i).getFileSize()/tn.getFileSize());
                }
            }
        }

    }

    public void build




    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
