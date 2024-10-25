package ozpasyazilim.utils.log;

import ozpasyazilim.utils.fidbanno.FiTable;

/**
 * Ore: Orak - Entity (Util Library)
 * <p>
 * Orl : Orak Log
 */
@FiTable
public class OreLog {

    private String orlTxMessage;

    private String orlTxLogType;

    public OreLog(String orlTxMessage) {
        setOrlTxMessage(orlTxMessage);
        setOrlTxLogType(MetaLogType.INFO.toString());
    }

    public OreLog(String orlTxMessage, MetaLogType metaLogType) {
        setOrlTxMessage(orlTxMessage);
        setOrlTxLogType(metaLogType.toString());
    }

    // Getter and Setter

    public String getOrlTxMessage() {
        return orlTxMessage;
    }

    public String getTxMessageNtn() {
        if (orlTxMessage == null) {
            return "";
        }
        return orlTxMessage;
    }

    public void setOrlTxMessage(String orlTxMessage) {
        this.orlTxMessage = orlTxMessage;
    }

    public String getOrlTxLogType() {
        return orlTxLogType;
    }

    public String getTxLogTypeNtn() {
        if (orlTxLogType == null) {
            return "";
        }
        return orlTxLogType;
    }

    public void setOrlTxLogType(String orlTxLogType) {
        this.orlTxLogType = orlTxLogType;
    }

    @Override
    public String toString() {
        return "EntLog{" +
                "txMessage='" + orlTxMessage + '\'' +
                ", txLogType='" + orlTxLogType + '\'' +
                '}';
    }
}
