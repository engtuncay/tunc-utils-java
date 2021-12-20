package mark.utils.formatter;

import mark.utils.bean.Formatter;

public class FormatterInteger implements Formatter {

	public Integer format(Object obj) {
		if (obj == null) return null;
		return (Integer) obj;
	}


	public String getName() {
		return "int";
	}


	public Integer parse(Object s) {
		// Loghelper.getInstance(this.getClass()).info("object parse");
		if (s == null || s.equals("")) return null;
		// if(s.getClass().getName().equals("String")) return Integer.parseInt((String) s);
		return (Integer) s;
	}

	public Integer parse(String s) {
		// Loghelper.getInstance(this.getClass()).info("String parse");
		if (s == null || s.equals("")) return null;
		return Integer.parseInt((String) s);
	}

}
