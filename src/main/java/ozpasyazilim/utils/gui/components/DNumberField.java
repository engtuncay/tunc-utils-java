package ozpasyazilim.utils.gui.components;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Numeric JTextField
 * 
 * @author TUNC
 *
 */
public class DNumberField extends JTextField {

	// this can only be a characters of '#' '.' '-' at the first index
	private String npb = null;
	private int digitNum;
	private int decimalNum;
	private int maxLength;

	public DNumberField(Integer digitNum, Integer decimalNum, String negativeorpositivekeyword_np) {
		super();

		if (digitNum == null) digitNum = 12;
		if (decimalNum == null) decimalNum = 0;
		if (negativeorpositivekeyword_np == null) negativeorpositivekeyword_np = "p";

		this.digitNum = digitNum;
		this.decimalNum = decimalNum;
		this.npb = negativeorpositivekeyword_np;
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
				OzPanel.getMessagePane(this).showInfoPane("Bu alana sadece pozitif sayı girebilirsiniz");
				return false;
			} else if (nch[0] != '-' && npb.equals("n")) {
				OzPanel.getMessagePane(this).showInfoPane("Bu alana sadece negatif sayı girebilirsiniz");
				return false;
			} else if (nch[0] == '.') return false;

			if (nch[0] == '-') {
				digit++;
				maxL++;
				digNum++;
			}
		}

		if (ns.length() > maxL) {
			// Loghelper.getInstance(this.getClass()).info("ns:" + ns + "ns:" + ns.length() + " > max=" + maxL);

			OzPanel.getMessagePane(this).showInfoPane("Bu alana daha fazla girdi yapamazsınız");
			return false;
		}

		// digit=0
		for (; digit < ns.length(); digit++) {
			if (nch[digit] < '0' || nch[digit] > '9') {
				if (nch[digit] == '.') {
					if (decimalNum == 0) {
						OzPanel.getMessagePane(this).showInfoPane("Bu alana sadece Tamsayı girilebilir");
						return false;
					} else
						break;
				}
				OzPanel.getMessagePane(this).showInfoPane("Bu alana sadece sayısal değer girebilirsiniz :"
					+ nch[digit]);

				return false;
			}
		}

		// System.out.println(digit+" "+digitNum);
		if (digit > digNum) {
			if (decimalNum == 0) {
				OzPanel.getMessagePane(this).showInfoPane("Bu alana en fazla "
					+ digitNum
					+ " basamaklı tamsayı girebilirsiniz");
			} else
				OzPanel.getMessagePane(this).showInfoPane("Bu alanın tamsayı kısmı enfazla "
					+ digitNum
					+ " basamaklı olabilir");
			return false;
		}

		digit++;
		int rate = 0;
		for (; digit < ns.length(); digit++) {
			rate++;
			// System.out.println(nch[digit]);
			if (nch[digit] < '0' || nch[digit] > '9') {
				OzPanel.getMessagePane(this).showInfoPane("Bu alana sadece sayısal değer girebilirsiniz ("
					+ nch[digit]
					+ " girilemez)");
				return false;
			}
		}

		if (rate > decimalNum) {
			OzPanel.getMessagePane(this).showInfoPane("Bu alanın ondalık kısmı en fazla "
				+ decimalNum
				+ " basamak olabilir");
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
		if (getText().trim().equals("")) return new Long(0);
		else
			return new Long(getText());
	}

	public Double getValuedouble() {
		if (getText().trim().equals("")) return null;
		return Double.parseDouble(getText());
	}

	class NumberPlainDocument extends PlainDocument {

		private String myInsert(String num, int offs, String str) {
			// System.out.println("num="+num+"l:"+num.length()+" offs= "+offs+"
			// str="+str);
			if (num.length() == offs) {
				return num.concat(str);
			} else {
				if (offs == 0) return str + num.substring(0, num.length());
				else
					return num.substring(0, offs) + str + num.substring(offs, num.length());
			}
		}

		private String myRemove(int offs, int len, String num) {
			if (offs == 0) {
				if (len == num.length()) return "";
				else
					return num.substring(offs + len + 1);
			} else {
				String ret = num.substring(0, offs);
				if (offs + len == num.length()) return ret;
				else
					return ret + num.substring(offs + len, num.length());
			}
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

			if ((str != null) && (str.indexOf('\n') >= 0)) {
				StringBuilder filtered = new StringBuilder(str);
				int n = filtered.length();
				for (int i = 0; i < n; i++) {
					if (filtered.charAt(i) == ',') {
						filtered.setCharAt(i, '.');
					}
				}
				str = filtered.toString();
			}


			// karakterden önceki durum - num stringi
			String num = super.getText(0, super.getLength());

			// eklenen karakter ise str String
			// Loghelper.getInstance(this.getClass()).info("str:" + str + " -- num:" + num);


			if (isValid(myInsert(num, offs, str))) {
				super.insertString(offs, str, a);
			}

		}

		@Override
		public void replace(int offs, int len, String text, AttributeSet attrs) throws BadLocationException {

			if (text.equals(",")) text = ".";

			// Loghelper.getInstance(this.getClass()).info("replace girdi" + text);

			String num = super.getText(0, super.getLength());
			if (isValid(myInsert(myRemove(offs, len, num), offs, text))) super.replace(offs, len, text, attrs);
		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {
			String num = super.getText(0, super.getLength());
			String rem = myRemove(offs, len, num);
			// System.out.println("rem="+rem);
			if (isValid(rem)) super.remove(offs, len);
		}
	}

}
