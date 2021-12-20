package mark.utils.formatter;

import mark.utils.bean.Formatter;

public class StringtoIntegerFormatter implements Formatter {

	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object entityden geliyor
	public Integer format(Object obj) {
		String ss=(String)obj;
		if(obj==null)return null;
		return Integer.parseInt(ss);
	}

	public String getName() {
		return "integer";
	}

	// table editable ise set ederken kullanıyor
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	public String parse(Object s) {
		if(s==null||s.equals(""))return null;
		return (String)s;
	}

}
