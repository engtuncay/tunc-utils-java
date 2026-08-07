package ozpasyazilim.utils.log;

import ozpasyazilim.utils.fidbanno.FiTable;

/**
 * Ore: Orak - Entity (Util Library)
 * <p>
 * Orl : Orak Log
 */
@FiTable
public class FieLog {

    private String fieTxMessage;

    private String fieTxLogType;

    public FieLog(String fieTxMessage) {
        setFieTxMessage(fieTxMessage);
        setFieTxLogType(MetaLogType.INFO.toString());
    }

    public FieLog(String fieTxMessage, MetaLogType metaLogType) {
        setFieTxMessage(fieTxMessage);
        setFieTxLogType(metaLogType.toString());
    }

    // Getter and Setter

    public String getFieTxMessage() {
        return fieTxMessage;
    }

    public String getTxMessageNtn() {
        if (fieTxMessage == null) {
            return "";
        }
        return fieTxMessage;
    }

    public void setFieTxMessage(String fieTxMessage) {
        this.fieTxMessage = fieTxMessage;
    }

    public String getFieTxLogType() {
        return fieTxLogType;
    }

    public String getTxLogTypeNtn() {
        if (fieTxLogType == null) {
            return "";
        }
        return fieTxLogType;
    }

    public void setFieTxLogType(String fieTxLogType) {
        this.fieTxLogType = fieTxLogType;
    }

    @Override
    public String toString() {
        return "EntLog{" +
                "txMessage='" + fieTxMessage + '\'' +
                ", txLogType='" + fieTxLogType + '\'' +
                '}';
    }
}
