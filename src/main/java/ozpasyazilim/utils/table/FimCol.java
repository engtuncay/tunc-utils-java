package ozpasyazilim.utils.table;

public class FimCol extends FiCol<Object> {

    public FimCol() {
    }

    public FimCol(String ofcTxFieldName, String ofcTxHeader) {
        super(ofcTxFieldName, ofcTxHeader);
    }

    public FimCol(String ofcTxFieldName) {
        super(ofcTxFieldName);
    }

    public FimCol(String ofcTxFieldName, String ofcTxHeader, String colComment) {
        super(ofcTxFieldName, ofcTxHeader, colComment);
    }

    public FimCol(String ofcTxHeader, Object ofcTxFieldName) {
        super(ofcTxHeader, ofcTxFieldName);
    }

    public FimCol(Object ofcTxFieldName, String ofcTxHeader, OzColType colType) {
        super(ofcTxFieldName, ofcTxHeader, colType);
    }

    public FimCol(Object ofcTxFieldName, String ofcTxHeader, OzColType colType, String colComment) {
        super(ofcTxFieldName, ofcTxHeader, colType, colComment);
    }

}
