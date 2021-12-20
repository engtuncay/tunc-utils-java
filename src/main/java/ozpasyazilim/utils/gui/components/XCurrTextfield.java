package ozpasyazilim.utils.gui.components;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JTextField;

public class XCurrTextfield extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Locale locale = new Locale("tr", "TR");

	public XCurrTextfield() {
		this.setHorizontalAlignment(RIGHT);
	}

	@Override
	public void setText(String l) {
		super.setText(l);
	}

	public void setText(Double dblval) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
		String strnumber = decimalpattern.format(dblval);
		super.setText(strnumber);
	}

	public void setValue(Double dblval) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
		String strnumber = decimalpattern.format(dblval);
		super.setText(strnumber);
	}

}
