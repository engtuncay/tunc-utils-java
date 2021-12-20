package ozpasyazilim.utils.gui.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.ImageObserver;

public class GraphicsUtil {
	protected Graphics g;
	protected ImageObserver observer;

	public GraphicsUtil(Graphics g, ImageObserver observer) {
		this.g = g;
		this.observer = observer;
	}

	public enum Align {
		North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest, Center
	}

	public void drawString(String text, RectangularShape bounds, Align align) {
		drawString(g, text, bounds, align, 0.0);
	}

	public void drawString(String text, RectangularShape bounds, Align align, double angle) {
		drawString(g, text, bounds, align, angle);
	}

	public static void drawString(Graphics g, String text, RectangularShape bounds, Align align) {
		drawString(g, text, bounds, align, 0.0);
	}

	public static void drawString(Graphics g, String text, RectangularShape bounds, Align align,
			double angle) {
		Graphics2D g2 = (Graphics2D) g;
		Font font = g2.getFont();
		if (angle != 0) g2.setFont(font.deriveFont(AffineTransform.getRotateInstance(Math.toRadians(angle))));

		Rectangle2D sSize = g2.getFontMetrics().getStringBounds(text, g2);
		Point2D pos = getPoint(bounds, align);
		double x = pos.getX();
		double y = pos.getY() + sSize.getHeight();

		switch (align) {
		case North:
		case South:
		case Center:
			x -= (sSize.getWidth() / 2);
			break;
		case NorthEast:
		case East:
		case SouthEast:
			x -= (sSize.getWidth());
			break;
		case SouthWest:
		case West:
		case NorthWest:
			break;
		}

		g2.drawString(text, (float) x, (float) y);
		g2.setFont(font);
	}

	public static Point2D getPoint(RectangularShape bounds, Align align) {
		double x = 0.0;
		double y = 0.0;

		switch (align) {
		case North:
			x = bounds.getCenterX();
			y = bounds.getMinY();
			break;
		case NorthEast:
			x = bounds.getMaxX();
			y = bounds.getMinY();
			break;
		case East:
			x = bounds.getMaxX() - 5;
			y = bounds.getCenterY() - 5;
			break;
		case SouthEast:
			x = bounds.getMaxX();
			y = bounds.getMaxY();
			break;
		case South:
			x = bounds.getCenterX();
			y = bounds.getMaxY();
			break;
		case SouthWest:
			x = bounds.getMinX();
			y = bounds.getMaxY();
			break;
		case West:
			x = bounds.getMinX() + 2;
			y = bounds.getCenterY() - 5;
			break;
		case NorthWest:
			x = bounds.getMinX();
			y = bounds.getMinY();
			break;
		case Center:
			x = bounds.getCenterX();
			y = bounds.getCenterY();
			break;
		}

		return new Point2D.Double(x, y);
	}

}
