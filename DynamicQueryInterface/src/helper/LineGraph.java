package helper;

import model.WebAPIManager;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LineGraph {
    private WebAPIManager webAPIManager;
    private List<Double> xPoints;
    private List<Double> yPoints;
    private String countryName;
    private Color color;
    private double minValue;
    private double maxValue;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    public static HashMap<String, Color> colorScheme;

    public LineGraph(String cname) {
        webAPIManager = WebAPIManager.getInstance();
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();
        countryName = cname;
        minValue = Collections.min(webAPIManager.getAllRatesByCountry(countryName));
        maxValue = Collections.max(webAPIManager.getAllRatesByCountry(countryName));
    }

    public GeneralPath getGraph() {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.size());
        gp.moveTo(xPoints.get(0), yPoints.get(0));
        for(int i = 1; i < xPoints.size(); i++) {
            gp.lineTo(xPoints.get(i), yPoints.get(i));
        }
        return gp;
    }

    public void addPoints(double x, double y) {
        xPoints.add(x);
        yPoints.add(y);
    }

    public boolean onLine(Point p) {
        int index = -1;
        for(int i = 0; i < xPoints.size()-1; i++) {
            if(p.getX() >= xPoints.get(i) && p.getX() <= xPoints.get(i+1)) {
                index = i;
                break;
            }
        }
        return index != -1 && Math.abs(Math.sin(Math.atan2(yPoints.get(index + 1) - p.y, xPoints.get(index + 1) - p.x) - Math.atan2(yPoints.get(index) - p.y, xPoints.get(index) - p.x))) < 0.07;
    }

    public Color getColor() { return colorScheme.get(countryName); }
    public double getMin() {return minValue;}
    public double getMax() {return maxValue;}
    public String getTooltipString() { return countryName + " || Max: " + df.format(maxValue) + " min: " + df.format(minValue); }
    public String getCountryName() { return countryName; }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LineGraph)) {
            return false;
        }
        LineGraph lg = (LineGraph) obj;
        return lg.countryName.equals(this.countryName);
    }

    public static void setColorScheme(String cname, Color col) {
        if(colorScheme == null) {
            colorScheme = new HashMap<>();
        }
        colorScheme.put(cname, col);
    }
}
