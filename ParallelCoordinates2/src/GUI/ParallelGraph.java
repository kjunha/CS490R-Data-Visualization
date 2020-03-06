package GUI;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class ParallelGraph {
    private int[] xPoints;
    private int[] yPoints;

    public ParallelGraph(int[] xps, int[] yps) {
        this.xPoints = xps;
        this.yPoints = yps;
    }

    public void updatePoints(int[] xps, int[] yps) {
        this.xPoints = xps;
        this.yPoints = yps;
    }

    public GeneralPath getPolyLine(Graphics2D g) {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        gp.moveTo(xPoints[0], yPoints[0]);
        for(int i = 1; i < xPoints.length; i++) {
            gp.lineTo(xPoints[i], yPoints[i]);
        }
        return gp;
    }
}
