package ozpasyazilim.utils.gui.components;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/*
 * author : ttn
 */

public class DMoneyFormattedField extends JTextField {

	// this can only be a characters of '#' '.' '-' at the first index
	private String npb = null;
	private int digitNum;
	private int decimalNum;
	private int maxLength;
	
	
	public DMoneyFormattedField(int digitNum, int decimalNum, String npb) {
		super();

		this.digitNum = digitNum;
		this.decimalNum = decimalNum;
		this.npb = npb;
		if (decimalNum == 0) {
			maxLength = digitNum;
		} else {
			maxLength = digitNum + decimalNum + 1;
		}

		this.setDocument(new NumberPlainDocument());
		this.setHorizontalAlignment(JTextField.RIGHT);
	}

	public boolean isValid(String ns) {

		int maxL = this.maxLength;
		int digNum = digitNum;
		// System.out.println(ns + " "+ maxLength );
		int digit = 0;
		char[] nch = ns.toCharArray();
		if (nch.length > 0) {
			if (nch[0] == '-' && npb.equals("p")) {
				OzPanel.getMessagePane(this)
						.showInfoPane("Bu alana sadece pozitif sayı girebilirsiniz");
				return false;
			} else if (nch[0] != '-' && npb.equals("n")) {
				OzPanel.getMessagePane(this)
						.showInfoPane("Bu alana sadece negatif sayı girebilirsiniz");
				return false;
			} else if (nch[0] == '.')
				return false;

			if (nch[0] == '-') {
				digit++;
				maxL++;
				digNum++;
			}
		}

		if (ns.length() > maxL) {
			OzPanel.getMessagePane(this).showInfoPane("Bu alana daha fazla girdi yapamazsınız");
			return false;
		}

		// digit=0
		for (; digit < ns.length(); digit++) {
			if (nch[digit] < '0' || nch[digit] > '9') {
				if (nch[digit] == '.') {
					if (decimalNum == 0) {
						OzPanel.getMessagePane(this)
								.showInfoPane("Bu alana sadece Tamsayı girilebilir");
						return false;
					} else
						break;
				}
				OzPanel.getMessagePane(this).showInfoPane(
						"Bu alana sadece sayısal değer girebilirsiniz :" + nch[digit]);
				
				return false;
			}
		}

		// System.out.println(digit+" "+digitNum);
		if (digit > digNum) {
			if (decimalNum == 0) {
				OzPanel.getMessagePane(this).showInfoPane(
						"Bu alana en fazla " + digitNum + " basamaklı tamsayı girebilirsiniz");
			} else
				OzPanel.getMessagePane(this).showInfoPane(
						"Bu alanın tamsayı kısmı enfazla " + digitNum + " basamaklı olabilir");
			return false;
		}

		digit++;
		int rate = 0;
		for (; digit < ns.length(); digit++) {
			rate++;
			// System.out.println(nch[digit]);
			if (nch[digit] < '0' || nch[digit] > '9') {
				OzPanel.getMessagePane(this)
						.showInfoPane("Bu alana sadece sayısal değer girebilirsiniz (" + nch[digit]
								+ " girilemez)");
				return false;
			}
		}

		if (rate > decimalNum) {
			OzPanel.getMessagePane(this).showInfoPane(
					"Bu alanın ondalık kısmı en fazla " + decimalNum + " basamak olabilir");
			return false;
		}

		return true;
	}

	public void setText(Long l) {
		super.setText(l + "");
	}

	public void setText(String l) {
		super.setText(l);
	}

	public Long getLongValue() {
		if (getText().trim().equals(""))
			return new Long(0);
		else
			return new Long(getText());
	}

	class NumberPlainDocument extends PlainDocument {
		private String myInsert(String num, int offs, String str) {
			// System.out.println("num="+num+"l:"+num.length()+" offs= "+offs+"
			// str="+str);
			if (num.length() == offs) {
				return num.concat(str);
			} else {
				if (offs == 0)
					return str + num.substring(0, num.length());
				else
					return num.substring(0, offs) + str + num.substring(offs, num.length());
			}
		}

		private String myRemove(int offs, int len, String num) {
			if (offs == 0) {
				if (len == num.length())
					return "";
				else
					return num.substring(offs + len + 1);
			} else {
				String ret = num.substring(0, offs);
				if (offs + len == num.length())
					return ret;
				else
					return ret + num.substring(offs + len, num.length());
			}
		}

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			String num = super.getText(0, super.getLength());
			if (isValid(myInsert(num, offs, str))) {
				super.insertString(offs, str, a);
			}

		}

		public void replace(int offs, int len, String text, AttributeSet attrs)
				throws BadLocationException {
			String num = super.getText(0, super.getLength());
			if (isValid(myInsert(myRemove(offs, len, num), offs, text)))
				super.replace(offs, len, text, attrs);
		}

		public void remove(int offs, int len) throws BadLocationException {
			String num = super.getText(0, super.getLength());
			String rem = myRemove(offs, len, num);
			// System.out.println("rem="+rem);
			if (isValid(rem))
				super.remove(offs, len);
		}
	}
	
	public double getValuedouble() {
		return Double.parseDouble(getText());
	}
}
