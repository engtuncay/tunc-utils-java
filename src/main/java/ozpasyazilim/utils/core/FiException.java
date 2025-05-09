package ozpasyazilim.utils.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class FiException {

	public static String exTosMain(Exception e) {
		if (e == null) return "";
		return getStackTraceString(e, "");
	}

	public static String exceptiontostring1(Exception e) {
		String[] ss = ExceptionUtils.getRootCauseStackTrace(e);
		return StringUtils.join(ss, "\n");
	}

	public static String exceptionStackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static String exceptionStackTraceStringFull(Throwable e) {
		return getStackTraceString(e, "");
	}

	public static String getStackTraceString(Throwable e, String indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString());
		sb.append("\n");
		sb.append("\n");

		StackTraceElement[] stack = e.getStackTrace();
		if (stack != null) {
			for (StackTraceElement stackTraceElement : stack) {
				sb.append(indent);
				sb.append("\tat ");
				sb.append(stackTraceElement.toString());
				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append("\n");

		Throwable[] suppressedExceptions = e.getSuppressed();
		// Print suppressed exceptions indented one level deeper.
		if (suppressedExceptions != null) {
			for (Throwable throwable : suppressedExceptions) {
				sb.append(indent);
				sb.append("\tSuppressed: ");
				sb.append(getStackTraceString(throwable, indent + "\t"));
			}
		}

		sb.append("\n");
		sb.append("\n");

		Throwable cause = e.getCause();
		if (cause != null) {
			sb.append(indent);
			sb.append("Caused by: ");
			sb.append(getStackTraceString(cause, indent));
		}

		return sb.toString();
	}

	public static String exceptionIfToString(Exception exception) {
		if (exception == null) return "Tanımsız Exception";
		return exception.toString();
	}

	/**
	 * Error Log için Exception String çevirir
	 *
	 * @param ex
	 * @return
	 */
	public static String exToErrorLog(Exception ex) {
		return exTosMain(ex);
	}

	public static String TosSummary(Exception exception) {
		if (exception == null) return "Exception Tanımı yok";
		return FiString.orEmpty(exception.getMessage());
	}
}
