package mark.utils.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

import mark.utils.bean.Formatter;

public class DateFormatterTbl implements Formatter {

	// table a bu formatta dönüş yapıyor - model, entitydeki alanı format üzerinde çekerek alıyor - parametre Object entityden geliyor
	public Date format(Object obj) {
		//String ss=Utilview.formatla((Number)obj);
		if(obj==null)return null;
		return (Date)obj;
	}

	public String getName() {
		return "date";
	}

	// table editable ise set ederken kullanıyor
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	// tabledan String formatında mi gelir ? - editorpane jextfield old için String döner
	public Date parse(Object s) {
		if(s==null||s.equals(""))return null;
		
		SimpleDateFormat formatterdate = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
		//String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
		Date date = new Date();
		try {
			date = formatterdate.parse((String)s);
			//System.out.println(date);
			//System.out.println(formatter.format(date));
			
			// sql date çevirmek istenirse
		    //java.sql.Date date2 = new java.sql.Date(date.getTime());

	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

}
