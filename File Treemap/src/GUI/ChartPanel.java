package GUI;

import model.TreeMap;
import model.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    private List<String> fileExtentions;
    private HashMap<String, Color> colorMap;

    //Draw box field
    boolean isHorizontalSplit; //true when the rectangle splits horizontally.

    //Color Scheme indicator
    private ColorSchemeType colorSchemeType;

    //tooltip related field
    private boolean showTooltip;
    private ToolTipManager ttm;
    private List<String> tooltipInfo;

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");
    private Timer boxAction;
    private JFileChooser chooser;


    private ChartPanel() {
        treeMap = TreeMap.getInstance();
        boxStructure = new ArrayList<>();
        fileExtentions = new ArrayList<>();
        colorMap = new HashMap<>();
        tooltipInfo = new ArrayList<>();
        isHorizontalSplit = true;
        colorSchemeType = ColorSchemeType.BY_EXTENSION;
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
            switch (colorSchemeType) {
                case BY_EXTENSION:
                    g.setColor(colorMap.get(fileExtentions.get(i)));
                    g.fill(boxStructure.get(i));
                    break;
                case RANDOM:
                    g.setColor(Color.getHSBColor((float)Math.random(),0.7f,0.7f));
                    g.fill(boxStructure.get(i));
                    break;
                case NONE:
                    break;
                default:
                    g.setColor(colorMap.get(fileExtentions.get(i)));
                    g.fill(boxStructure.get(i));
                    break;
            }
            g.setColor(Color.LIGHT_GRAY);
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
        if(tn.getFile().isFile()) {
            boxStructure.add(new Rectangle2D.Double(left,top,width,height));
            fileExtentions.add(tn.getExtension());
            tooltipInfo.add(tn.getFile().toString() + " (Size: " + df.format(tn.getFileSize()/1024) + " kb)");
            if(!colorMap.keySet().contains(tn.getExtension())) {
                colorMap.put(tn.getExtension(), Color.getHSBColor((float)Math.random(), 0.7f, 0.7f));
            }
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

    public void chooseAFile() {
        boxStructure.clear();
        fileExtentions.clear();
        tooltipInfo.clear();
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose a file");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            treeMap.resetRootDir(chooser.getSelectedFile().toString());
        }
        repaint();
    }

    public void setColorSchemeType(ColorSchemeType ct) {
        this.colorSchemeType = ct;
        repaint();
    }




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
        double x = e.getX();
        double y = e.getY();
        for(int i = 0; i < boxStructure.size(); i++) {
            if(boxStructure.get(i).contains(x,y)) {
                setToolTipText(tooltipInfo.get(i));
                break;
            } else {
                setToolTipText(null);
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
