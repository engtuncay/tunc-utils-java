package mark.utils.formatter;

import mark.utils.bean.Formatter;

public class DoubleFormatter implements Formatter {

	public String getName() {
		return "double";
	}

	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object entityden geliyor
	public Double format(Object obj) {
		//String ss=Utilview.formatla((Number)obj);
		if(obj==null)return null;
		return (Double)obj;
	}

	// table editable ise set ederken kullanıyor - fieldhandler.setvalue()
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	// tabledan String formatında mi gelir ? - editorpane jextfield old için String döner
	public Double parse(Object s) {
		if(s==null||s.equals(""))return null;
		if(s.getClass().getName().equals("String")) return Double.parseDouble((String)s);
		return (Double)s;
		
	}

}
