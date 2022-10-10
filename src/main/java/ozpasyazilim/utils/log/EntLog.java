package ozpasyazilim.utils.log;

public class EntLog {

	private String txMessage;

	private String txLogType;

	public EntLog(String txMessage) {
		setTxMessage(txMessage);
		setTxLogType(MetaLogType.INFO.toString());
	}

	public EntLog(String txMessage, MetaLogType metaLogType) {
		setTxMessage(txMessage);
		setTxLogType(metaLogType.toString());
	}

	// Getter and Setter

	public String getTxMessage() {
		return txMessage;
	}

	public void setTxMessage(String txMessage) {
		this.txMessage = txMessage;
	}

	public String getTxLogType() {
		return txLogType;
	}

	public void setTxLogType(String txLogType) {
		this.txLogType = txLogType;
	}

}
