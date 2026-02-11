package ozpasyazilim.utils.table;

public class FimCol extends FiCol<Object> {

    public FimCol() {
    }

    public FimCol(String fcTxFieldName, String fcTxHeader) {
        super(fcTxFieldName, fcTxHeader);
    }

    public FimCol(String fcTxFieldName) {
        super(fcTxFieldName);
    }

    public FimCol(String fcTxFieldName, String fcTxHeader, String colComment) {
        super(fcTxFieldName, fcTxHeader, colComment);
    }

    public FimCol(String fcTxHeader, Object fcTxFieldName) {
        super(fcTxHeader, fcTxFieldName);
    }

    public FimCol(Object fcTxFieldName, String fcTxHeader, OzColType colType) {
        super(fcTxFieldName, fcTxHeader, colType);
    }

    public FimCol(Object fcTxFieldName, String fcTxHeader, OzColType colType, String colComment) {
        super(fcTxFieldName, fcTxHeader, colType, colComment);
    }

}
