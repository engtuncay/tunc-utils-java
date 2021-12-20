package mark.utils.formatter;



import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import mark.utils.bean.Formatter;

public class DoubletoStringFormatter implements Formatter {

	static Locale locale = new Locale("tr", "TR");

	public String formatNumberParagosterimi(Number number) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
		String strnumber = decimalpattern.format(number);
		return strnumber;
	}


	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object
	// entityden geliyor
	public String format(Object obj) {
		String ss = formatNumberParagosterimi((Number) obj);
		if (obj == null) return null;
		return ss;
	}

	public String getName() {
		return "double";
	}

	// table editable ise set ederken kullanıyor
	// tabledan alanı(editorden düzenlerken) String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity
	// e set ediyor.
	public Double parse(Object s) {
		if (s == null || s.equals("")) return null;
		return Double.parseDouble((String) s);
	}

}
