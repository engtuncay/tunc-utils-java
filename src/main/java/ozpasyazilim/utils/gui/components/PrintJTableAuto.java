package ozpasyazilim.utils.gui.components;

import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class PrintJTableAuto {

	// print configurations
	private boolean fitWidth = true;
	private boolean showPrintDialog = true;
	private boolean interactive = true;
	private boolean showHeader = true;
	private boolean showFooter = true;

	public PrintJTableAuto() {
	}

	public void printTable(JTable jtablecomp, String strheader, String strfooter) {

		MessageFormat header = null;
		/* if we should print a header */
		if (showHeader) { /* create a MessageFormat around the header text */
			header = new MessageFormat(strheader);
		}

		MessageFormat footer = null;
		/* if we should print a footer */
		if (showFooter) { /* create a MessageFormat around the footer text */
			footer = new MessageFormat(strfooter);
		}

		/* determine the print mode */
		JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
		try {

			/* print the table */
			boolean complete = jtablecomp.print(mode, header, footer, showPrintDialog, null,
				interactive, null);
			/* if printing completes */
			if (complete) { /* show a success message */
				JOptionPane.showMessageDialog(null, "Yazdırma Tamamlandı", "Yazdırma İşlemi",
					JOptionPane.INFORMATION_MESSAGE);
			} else { /* show a message indicating that printing was cancelled */
				JOptionPane.showMessageDialog(null, "Yazdırma iptal edildi.", "Yazdırma İşlemi",
					JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			/* Printing failed, report to the user */
			JOptionPane.showMessageDialog(null, "Yazdırma Başarısız !!! Sorun :" + pe.getMessage(),
				"Printing Result", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isFitWidth() {
		return fitWidth;
	}

	public void setFitWidth(boolean fitWidth) {
		this.fitWidth = fitWidth;
	}

	public boolean isShowPrintDialog() {
		return showPrintDialog;
	}

	public void setShowPrintDialog(boolean showPrintDialog) {
		this.showPrintDialog = showPrintDialog;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}
}