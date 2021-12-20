package ozpasyazilim.utils.gui.components;

import java.awt.TextField;
import java.awt.event.FocusEvent;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.PlainDocument;

public class DMoneyField extends JFormattedTextField {
	private DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private String trCurrency = "TL";
	private int maxFractionDigits = 3; // Virgül'den sonra girilebilir basamak
										// sayısı
	private int denominationLength; // Birim uzunlugu
	private int minFract = 2;

	public DMoneyField(int frac) {
		super();
		this.minFract = frac;
		String pattern = "#.";
		for (int i = 0; i < frac; i++) {
			pattern += "#";
		}
		decimalFormat = new DecimalFormat(pattern);

		setFormatterFactory(new DefaultFormatterFactory(new CurrencyFormatter(),
				new CurrencyFormatter(), new EditCurrencyFormatter()));
		this.setDocument(new CurrencyPlainDocument());
		setValue(new Long(0));
		setHorizontalAlignment(JFormattedTextField.RIGHT);
	}

	public DMoneyField() {
		super();
		setFormatterFactory(new DefaultFormatterFactory(new CurrencyFormatter(),
				new CurrencyFormatter(), new EditCurrencyFormatter()));
		this.setDocument(new CurrencyPlainDocument());
		setValue(new Long(0));
		setHorizontalAlignment(JFormattedTextField.RIGHT);
	}

	public void setValue(Object value) {
		if (value == null)
			value = new Double(0.0);
		if (value instanceof BigDecimal) {
			super.setValue(((BigDecimal) value).doubleValue());
		} else
			super.setValue(value);
	}

	public Object getValue() {
		Object o = super.getValue();
		if (o == null)
			return null;

		double dMoney;
		if (super.getValue() instanceof Long) {
			dMoney = ((Long) o).doubleValue();
		} else {
			dMoney = ((Double) o).doubleValue();
		}
		return new Double(decimalFormat.format(dMoney).replaceAll(",", "."));
	}

	public Double getDoubleValue() {
		return (Double) getValue();
	}

	protected void processFocusEvent(FocusEvent e) {
		super.processFocusEvent(e);
		if (e.getID() == FocusEvent.FOCUS_GAINED)
			select(0, getText().length() - denominationLength);
	}

	private class EditCurrencyFormatter extends CurrencyFormatter {
		public String valueToString(Object object) {
			if (object == null) {
				return "";
			}
			return getNumberFormat().format(object);
		}
	}

	private NumberFormat getNumberFormat() {
		NumberFormat curFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
		if (Locale.getDefault().getCountry().equals("TR")) {
			
			DecimalFormatSymbols decimalFormatSymbol = ((DecimalFormat) curFormat)
					.getDecimalFormatSymbols();
			
			//< added to
//			decimalFormatSymbol.setDecimalSeparator('.');
//			decimalFormatSymbol.setGroupingSeparator(',');
			//> added to
			
			
			decimalFormatSymbol.setCurrencySymbol(trCurrency);
			((DecimalFormat) curFormat).setDecimalFormatSymbols(decimalFormatSymbol);
		} else {
			trCurrency = ((DecimalFormat) curFormat).getDecimalFormatSymbols().getCurrencySymbol();
		}
		denominationLength = trCurrency.length() + 1;

		curFormat.setMaximumFractionDigits(14);
		curFormat.setMinimumFractionDigits(minFract);
		return curFormat;
	}

	private class CurrencyFormatter extends JFormattedTextField.AbstractFormatter {
		public String valueToString(Object object) {
			if (object == null) {
				return "";
			}
			NumberFormat curFormat = getNumberFormat();
			return curFormat.format(object);
		}

		public Object stringToValue(String string) throws ParseException {
			if (string == null || string.trim().length() == 0) {
				return null;
			}
			Number number = null;
			NumberFormat curFormat = NumberFormat.getCurrencyInstance();
			NumberFormat numFormat = NumberFormat.getNumberInstance();
			try {
				number = curFormat.parse(string);
			} catch (ParseException ex) {
				try {
					number = numFormat.parse(string);
				} catch (ParseException ex2) {
					throw ex2;
				}
			}
			return number;
		}
	}

	class CurrencyPlainDocument extends PlainDocument {
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (characterControl(str))
				super.insertString(offs, str, a);
		}

		public void replace(int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			if (characterControl(text))
				super.replace(offset, length, text, attrs);
		}

		public void remove(int offs, int len) throws BadLocationException {
			super.remove(offs, len);
		}
	}

	private boolean characterControl(String str) {
		if (str.length() > 1)
			return true;

		int cursorPosition = getCaretPosition();
		int commaPosition = getText().indexOf(",");

		// Sadece rakam ve "," girilebilir
		if (!(Character.isDigit(str.charAt(0)) || str.equals(",")))
			return false;

		// 1'den fazla "," girilemez
		if (str.equals(",") && getText().indexOf(",") >= 0)
			return false;

		// Virgül'den sonra 3 basamakdan fazla girilmesini engeller
		if (commaPosition > -1 && cursorPosition > commaPosition
				&& getText().length() - commaPosition > maxFractionDigits + denominationLength
				&& getSelectedText() == null)
			return false;

		// Birim k�sm�na deger girilmesini engeller
		if (getText().length() - cursorPosition < denominationLength)
			return false;

		return true;
	}

	public static void main(String[] args) {
		// Locale.setDefault(new Locale("tr","TR"));

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setLayout(null);

		DMoneyField field = new DMoneyField();
		frame.add(field);
		field.setBounds(20, 20, 200, 25);

		TextField tf = new TextField();
		frame.add(tf);
		tf.setBounds(20, 50, 200, 25);
		frame.setVisible(true);
	}
}
