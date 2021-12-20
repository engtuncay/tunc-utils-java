package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class LabeledRectangle extends Rectangle {

	public LabeledRectangle(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		this.text = text;

	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(new Rectangle2D.Double(x, y, width, height));
		Font font = g2.getFont();
		FontRenderContext context = g2.getFontRenderContext();
		g2.setFont(font);
		int textWidth = (int) font.getStringBounds(text, context).getWidth();
		LineMetrics ln = font.getLineMetrics(text, context);
		int textHeight = (int) (ln.getAscent() + ln.getDescent());
		int x1 = x + (width - textWidth) / 2;
		int y1 = (int) (y + (height + textHeight) / 2 - ln.getDescent());

		g2.setColor(Color.red);

		g2.drawString(text, (int) x1, (int) y1);
	}

	private String text;
}