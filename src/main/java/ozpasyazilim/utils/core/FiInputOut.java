package ozpasyazilim.utils.core;

import javax.swing.ImageIcon;

public class FiInputOut {

	public static ImageIcon createImage(String path) {

		java.net.URL imgURL = FiInputOut.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}

	}

	public static void println(String message) {

		Throwable t = new Throwable();
		StackTraceElement elements[] = t.getStackTrace();

		System.out.println("-["
			+ elements[2].getClassName()
			+ "::"
			+ elements[2].getMethodName()
			+ "]("
			+ elements[2].getLineNumber()
			+ ")");

		System.out.println("---["
			+ elements[1].getClassName()
			+ "::"
			+ elements[1].getMethodName()
			+ "]("
			+ elements[1].getLineNumber()
			+ ") \n\t"
			+ message
			+ "\n");
	}
}
