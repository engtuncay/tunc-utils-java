package ozpasyazilim.utils.core;

public class FiDebug {

	public static String getCurrentClassAndMethodNames() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		final String s = e.getClassName();
		return s.substring(s.lastIndexOf('.') + 1, s.length()) + "." + e.getMethodName();
	}

}
