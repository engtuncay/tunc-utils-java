package ozpasyazilim.utils.ficols;

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiColList;
import ozpasyazilim.utils.fidbanno.FiIdGenerationType;
import ozpasyazilim.utils.fidborm.IFiTableMeta;

/**
 * Orak Code Generatorda kullanılıyor
 */
public class FicRfcCoding implements IFiTableMeta {

    public String getITxTableName() {
        return getTxTableName();
    }

    public static String getTxTableName() {
        return "RfcCoding";
    }

    public FiColList genITableCols() {
        return genTableCols();
    }

    public FiColList genITableColsTrans() {
        return genTableColsTrans();
    }


    public static FiColList genTableCols() {

        FiColList fiColList = new FiColList();

        fiColList.add(rfcTxClassName());
        fiColList.add(rfcTxClassBody());

        return fiColList;
    }

    public static FiColList genTableColsTrans() {

        FiColList fiColList = new FiColList();


        return fiColList;
    }

    public static FiCol rfcTxClassName() {
        FiCol fiCol = new FiCol("rfcTxClassName", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }

    public static FiCol rfcTxClassBody() {
        FiCol fiCol = new FiCol("rfcTxClassBody", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }


}