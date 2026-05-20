package edu.cmu.hcii.paint;

import java.awt.*;

public class LinePaint extends PaintObject {

    Point[] points;

    public LinePaint() {

    }

    public double getStartX() { return points[0].getX(); }
    public double getStartY() { return points[0].getY(); }
    public double getEndX() { return points[points.length - 1].getX(); }
    public double getEndY() { return points[points.length - 1].getY(); }

    public void define(Point[] points) {

        this.points = points;

    }

    public Rectangle getBoundingBox() {

		int startX = (int)Math.min(getStartX(), getEndX()) - thickness / 2;
		int startY = (int)Math.min(getStartY(), getEndY()) - thickness / 2;
		int endX = (int)Math.max(getStartX(), getEndX()) + thickness / 2;
		int endY = (int)Math.max(getStartY(), getEndY()) + thickness / 2;

		return new Rectangle(startX, startY, endX - startX, endY - startY);

    }

    public void paint(Graphics2D g) {

        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.drawLine((int)getStartX(), (int)getStartY(), (int)getEndX(), (int)getEndY());
        g.setStroke(oldStroke);

    }

}