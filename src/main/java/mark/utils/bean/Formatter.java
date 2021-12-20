package mark.utils.bean;

/**
 * Simple interface to convert a object to String and a String to a object.
 *
 *@author Marcos Vasconcelos
 */
public interface Formatter {

	/**
	 * Convert a object to String.
	 */
	public abstract Object format(Object obj);

	/**
	 * Convert the String to the Object.
	 */
	public abstract Object parse(Object s); 	//String s : Object s e çevrildi - ttn

	// table editable ise set ederken kullanıyor
	// tabledan alanı String s olarak alıp, parse ile entityde tanımlandığı türe çevirerek entity e set ediyor.
	// tabledan String formatında mi gelir ? - editorpane jextfield old için String döner


	/**
	 * Naming proposes only
	 */
	public abstract String getName();
}