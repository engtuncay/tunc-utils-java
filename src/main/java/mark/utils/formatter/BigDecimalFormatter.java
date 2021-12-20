package mark.utils.formatter;

import java.math.BigDecimal;

import mark.utils.bean.Formatter;

public class BigDecimalFormatter implements Formatter {

	public BigDecimal format(Object obj) {
		// String ss=Utilview.formatla((Number)obj);
		if (obj == null)
			return null;
		return (BigDecimal) obj;
	}

	public String getName() {
		return "Bigdecimal";
	}

	public Object parse(Object s) {
		if (s == null)
			return null;
		if (s.equals(""))
			return null;
		BigDecimal value = new BigDecimal((String)s);
		return value;
	}

}
