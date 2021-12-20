package ozpasyazilim.utils.gui.components;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

public class DTimeField extends JFormattedTextField {
	boolean pEditable = true;
	Format currency = NumberFormat.getCurrencyInstance(Locale.UK);
	DecimalFormat currencyFormat;
	String[] digits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	Locale locale = new Locale("tr", "TR", "");
	String originalText = "";
	GregorianCalendar cal;

	public DTimeField() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		// this.setDocument(new CurrencyPlainDocument());
		MaskFormatter mf = new MaskFormatter("##:##");
		this.setFormatterFactory(new DefaultFormatterFactory(mf));
		this.setSize(30, 20);
	}

	private boolean isDigitChar(String str) {
		boolean chk = false;
		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < digits.length; j++) {
				if (str.substring(i, i + 1).equals(digits[j])) chk = true;
			}
			if (!chk) return false;
		}
		return chk;
	}

	public void setNow() {
		// cal= new GregorianCalendar();
		/*
		 * setText( ( (cal.get(GregorianCalendar.HOUR_OF_DAY)<10)?"0":"")+cal.get(GregorianCalendar.HOUR_OF_DAY)+":"+
		 * ((cal.get(GregorianCalendar.MINUTE)<10)?"0":"")+cal.get(GregorianCalendar.MINUTE));
		 */
		SimpleDateFormat sdfOnlyTimeFormat = new SimpleDateFormat("HH:mm:ss");
		setText(sdfOnlyTimeFormat.format(new Date()));
	}


	class CurrencyPlainDocument extends PlainDocument {
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (isDigitChar(str)) super.insertString(offs, str, a);
		}

		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (isDigitChar(text)) super.replace(offset, length, text, attrs);
		}

		public void remove(int offs, int len) throws BadLocationException {
			if (pEditable) super.remove(offs, len);
		}
	}

}
