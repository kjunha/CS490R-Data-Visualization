package GUI;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ParallelGraph {
    private int[] xPoints;
    private int[] yPoints;
    private int id; //index position in the arrayList of Chart Panel

    public ParallelGraph(int[] xps, int[] yps, int i) {
        this.xPoints = xps;
        this.yPoints = yps;
        this.id = i;
    }

    public GeneralPath getPolyLine() {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        gp.moveTo(xPoints[0], yPoints[0]);
        for(int i = 1; i < xPoints.length; i++) {
            gp.lineTo(xPoints[i], yPoints[i]);
        }
        return gp;
    }

    public boolean onLine(Point p) {
        int index = -1;
        for(int i = 0; i < xPoints.length-1; i++) {
            if(p.getX() >= xPoints[i] && p.getX() <= xPoints[i+1]) {
                index = i;
                break;
            }
        }
        if(index != -1 && Math.abs(Math.sin(Math.atan2(yPoints[index+1]-p.y,xPoints[index+1]-p.x)-Math.atan2(yPoints[index]-p.y,xPoints[index]-p.x))) < 0.07) {
            return true;
        }
        return false;
    }

    public boolean isContainedIn(Rectangle2D rect) {
        double xInterval = (double)(xPoints[xPoints.length-1]-xPoints[0])/100;
        for(int i = 0; i < xPoints.length-1; i++) {
            double slope = (double)(yPoints[i+1]-yPoints[i])/(xPoints[i+1]-xPoints[i]);
            for(double xcor = xPoints[i]; xcor < xPoints[i+1]; xcor += xInterval) {
                Point2D linePoint = new Point2D.Double(xcor, yPoints[i]+slope*(xcor-xPoints[i]));
                if(rect.contains(linePoint)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ParallelGraph)) {
            return false;
        }
        ParallelGraph pg = (ParallelGraph) obj;
        return pg.id == this.id;
    }
}
