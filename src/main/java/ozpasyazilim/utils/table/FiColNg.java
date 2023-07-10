package ozpasyazilim.utils.table;

public class FiColNg extends FiCol<Object> {

    public FiColNg() {
        super();
    }

    public FiColNg(String fieldName, String headerName) {
        super(fieldName, headerName);
    }

    public FiColNg(String fieldName) {
        super(fieldName);
    }

    public FiColNg(String fieldName, String headerName, String colComment) {
        super(fieldName, headerName, colComment);
    }

    public FiColNg(String headerName, Object fieldName) {
        super(headerName, fieldName);
    }

    public FiColNg(Object fieldName, String headerName, OzColType colType) {
        super(fieldName, headerName, colType);
    }

    public FiColNg(Object fieldName, String headerName, OzColType colType, String colComment) {
        super(fieldName, headerName, colType, colComment);
    }


}
