package mark.utils.formatter;

import mark.utils.bean.Formatter;

public class ObjectFormatter implements Formatter {

	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object entityden geliyor
	public Object format(Object obj) {
		return obj;
	}

	public String getName() {
		return "double";
	}

	// table editable ise set ederken kullanıyor
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	public Object parse(Object s) {
		return s;
	}

}
