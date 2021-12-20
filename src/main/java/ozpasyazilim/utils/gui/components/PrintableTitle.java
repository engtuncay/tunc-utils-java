package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class PrintableTitle implements Printable {

	Font font = new Font("SansSerif", Font.PLAIN, 12);

	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {

		// if (pageIndex > 0) {
		// return Printable.NO_SUCH_PAGE;
		// }
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		g2d.setFont(font);
		g2d.setColor(Color.black);
		g2d.drawString("Rut Raporu", 50, 200);
		return Printable.PAGE_EXISTS;

	}
}