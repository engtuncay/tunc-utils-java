package ozpasyazilim.utils.log;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.FiException;

// comment
public class LogListener {

	public static StringProperty logMessage = new SimpleStringProperty();
	public static String logDetailMessage;

	public static String getLogMessage() {
		return logMessage.get();
	}

	public static StringProperty logMessageProperty() {
		return logMessage;
	}

	// FIXME seti bir yerden kullanıp synchronized olmalı
	public static void setLogMessage(String logMessage) {
		setLogDetailMessage("");
		LogListener.logMessage.set(logMessage);
	}

	public static void setLogMessageAndDetail(String logMessage,String detail) {
		LogListener.setLogDetailMessage(detail);
		LogListener.logMessage.set(logMessage);
	}

	public static void setLogMessageAndDetail(String logMessage, Exception exception) {
		setLogDetailMessage(FiException.exceptiontostring(exception));
		LogListener.logMessage.set(logMessage);

	}

	public static String getLogDetailMessage() {
		return logDetailMessage;
	}

	public static void setLogDetailMessage(String logDetailMessage) {
		LogListener.logDetailMessage = logDetailMessage;
	}
}
