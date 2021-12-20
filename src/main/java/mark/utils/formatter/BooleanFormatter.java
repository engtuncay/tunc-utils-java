package mark.utils.formatter;

import mark.utils.bean.Formatter;

public class BooleanFormatter implements Formatter {

	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object entityden geliyor
	public Boolean format(Object obj) {
		//String ss=Utilview.formatla((Number)obj);
		if(obj==null)return false;
		return (Boolean)obj;
	}

	public String getName() {
		return "double";
	}

	// table editable ise set ederken - parse edip değeri kullanıyor - fieldhandler.setvalue()
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	public Boolean parse(Object s) {
		if(s==null||s.equals(""))return null;
		if(s.getClass().getName().equals("String")) return Boolean.parseBoolean((String)s);
		return (Boolean)s;
	}

}
