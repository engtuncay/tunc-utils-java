package ozpasyazilim.utils.returntypes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FdrContext {

	private StringProperty txRxMessage;

	// Getter and Setter

	public String getTxRxMessage() {
		return txRxMessageProperty().get();
	}

	public StringProperty txRxMessageProperty() {
		if (txRxMessage == null) {
			txRxMessage = new SimpleStringProperty();
		}
		return txRxMessage;
	}

	public void setTxRxMessage(String txRxMessage) {
		this.txRxMessageProperty().set(txRxMessage);
	}

}
