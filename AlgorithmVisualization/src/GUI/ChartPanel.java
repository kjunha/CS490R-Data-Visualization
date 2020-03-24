package GUI;

import logic.BubbleSort;
import model.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartPanel extends JPanel {
    //Singleton for Chart Panel
    private static ChartPanel chartPanel;

    //db related global fields
    private ResourceManager resourceManager;
    private int highBound;
    private int lowBound;

    //chart related global fields
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double chartWidth;
    private double chartHeight;
    private List<SingleBar> barGraphs;
    private boolean isInit;

    //user interaction fields

    //ToolBar Interaction Field
    private AnimationSpeedToolbar animationSpeedToolbar;

    //Other fields
    private static final DecimalFormat df = new DecimalFormat("#.#");

    private ChartPanel() {
        resourceManager = ResourceManager.getInstance();
        barGraphs = new ArrayList<>();
        highBound = Collections.max(resourceManager.getDataset()) + 10;
        lowBound = Collections.min(resourceManager.getDataset())/5;
        isInit = false;
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
        System.out.println("Paint Requested!");
        if(!isInit){
            drawAxis(g, w, h, resourceManager.getDatasetSize(), 10);
            updateGraph();
        }
        g.setColor(Color.GREEN);
        for(SingleBar sb : barGraphs) {
            g.fill(sb.getBarGraph());
        }

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(10,10,100,100);
    }

    private void drawAxis(Graphics g, double w, double h, int xps, int yps) {
        isInit = true;
        left = (w*0.1<1)?10:(w*0.1);
        right = w - left;
        top = (h*0.1<1)?10:(h*0.1);
        bottom = h - top;
        chartWidth = right-left;
        chartHeight = bottom-top;
        double left_index = (w*0.05<1)?5:(w*0.05);
        g.setColor(Color.BLACK);
        g.drawLine((int)left, (int)top, (int)left, (int)bottom);
        g.drawLine((int)left, (int)bottom, (int)right, (int)bottom);
        for(int i = 1; i <= yps; i++) {
            double ymark = bottom-i*(chartHeight/yps);
            g.drawLine((int)left - 3, (int)ymark, (int)left+3, (int)ymark);
            g.drawString(df.format(((highBound - lowBound)/yps)*i+lowBound),(int)left_index, (int)ymark+5);
        }
    }

    private void updateGraph() {
        barGraphs.clear();
        double barwidth = chartWidth/resourceManager.getDatasetSize() * 0.7;
        double centerPointX = left + barwidth/2 + chartWidth/resourceManager.getDatasetSize() * 0.3;
        for(int i = 0; i < resourceManager.getDatasetSize(); i++) {
            barGraphs.add(new SingleBar(centerPointX, bottom, barwidth, chartHeight * (resourceManager.getDataset().get(i)-lowBound)/(highBound - lowBound), resourceManager.getDataset().get(i)));
            centerPointX += (barwidth + chartWidth/resourceManager.getDatasetSize() * 0.3);
        }
    }

    public void swapBarGraphs(int i, int j) {
        SingleBar copyOf_i = new SingleBar(barGraphs.get(i));
        SingleBar copyOf_j = new SingleBar(barGraphs.get(j));
        //System.out.printf("Before| i: %f, j: %f\n", copyOf_i.getCenterX(), copyOf_j.getCenterX());
        barGraphs.set(i, new SingleBar(copyOf_j));
        barGraphs.get(i).updateHorizontalPosition(copyOf_j.getCenterX());
        barGraphs.set(j, new SingleBar(copyOf_i));
        barGraphs.get(j).updateHorizontalPosition(copyOf_i.getCenterX());
        //System.out.printf("After| i: %f, j: %f\n", barGraphs.get(i).getCenterX(), barGraphs.get(j).getCenterX());
        
    }

    public void updateResource() {
        isInit = false;
        //TODO clear array lists
    }

    public void doBubbleSort() { BubbleSort.sort(resourceManager.getDataset());}
}
