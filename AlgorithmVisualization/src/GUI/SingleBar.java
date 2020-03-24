package GUI;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SingleBar {
    private Rectangle.Double rectangle;
    private double centerX;
    private int value;

    public SingleBar(double center, double bottom, double width, double height, int value) {
        this.rectangle = new Rectangle.Double(center-width/2, bottom-height, width, height);
        this.centerX = center;
        this.value = value;
    }

    public SingleBar(SingleBar sb) {
        this.rectangle = new Rectangle2D.Double(sb.rectangle.x, sb.rectangle.y, sb.rectangle.width, sb.rectangle.height);
        this.centerX = sb.centerX;
        this.value = sb.value;
    }

    public void updateHorizontalPosition(double offset) {
        this.centerX = offset;
        rectangle.setRect(centerX-rectangle.width/2, rectangle.y, rectangle.width, rectangle.height);
    }

    public double getCenterX() { return this.centerX; }
    public Rectangle2D getBarGraph() {
        return rectangle;
    }
}
