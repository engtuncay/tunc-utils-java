package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import ozpasyazilim.utils.gui.utils.GraphicsUtil;
import ozpasyazilim.utils.gui.utils.GraphicsUtil.Align;
import ozpasyazilim.utils.gui.utils.Graphrowcol;




public class MyGraphics extends JComponent {

	private static final long serialVersionUID = 1L;

	public MyGraphics() {
		setPreferredSize(new Dimension(500, 500));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.fillRect(200, 62, 30, 10);
		// LabeledRectangle deneme = new LabeledRectangle(10, 10, 100, 100,
		// "Deneme");
		// deneme.draw(g);
		Graphics2D g2d = (Graphics2D) g;

		Integer lastpositiony = 50; // hclip.height + clip.height; // +
		// hclip.height;
		Graphrowcol rowcol = new Graphrowcol();

		rowcol.setBoxheight(30);
		rowcol.setBoxwidth(100);
		rowcol.setGapy(5);
		rowcol.setLastrowy(lastpositiony);
		rowcol.setLocation(1, 1);

		Rectangle textrectangle = new Rectangle(rowcol.getCurrposx(), rowcol.getCurrposy(), rowcol.getBoxwidth(), rowcol
			.getBoxheight());

		g2d.setColor(Color.red);
		g2d.draw(textrectangle);
		GraphicsUtil.drawString(g2d, "Rut Toplami", textrectangle, Align.West);

		rowcol.setLocation(2, 1);
		textrectangle.setBounds(rowcol.getCurrposx(), rowcol.getCurrposy(), rowcol.getBoxwidth(), rowcol
			.getBoxheight());

		g2d.draw(textrectangle);
		GraphicsUtil.drawString(g2d, "100,00", textrectangle, Align.East);

		rowcol.setGapx(10);

		rowcol.setLocation(3, 1);
		textrectangle.setBounds(rowcol.getCurrposx(), rowcol.getCurrposy(), rowcol.getBoxwidth(), rowcol
			.getBoxheight());

		g2d.draw(textrectangle);
		GraphicsUtil.drawString(g2d, "Ort. Gecikme", textrectangle, Align.West);

		rowcol.setLocation(4, 1);
		textrectangle.setBounds(rowcol.getCurrposx(), rowcol.getCurrposy(), rowcol.getBoxwidth(), rowcol
			.getBoxheight());

		g2d.draw(textrectangle);
		GraphicsUtil.drawString(g2d, "100", textrectangle, Align.East);
	}
}