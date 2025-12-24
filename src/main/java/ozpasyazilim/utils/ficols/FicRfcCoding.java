package ozpasyazilim.utils.ficols;

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FicList;
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

    public FicList genITableCols() {
        return genTableCols();
    }

    public FicList genITableColsTrans() {
        return genTableColsTrans();
    }


    public static FicList genTableCols() {

        FicList ficList = new FicList();

        ficList.add(rfcTxClassName());
        ficList.add(rfcTxClassBody());

        return ficList;
    }

    public static FicList genTableColsTrans() {

        FicList ficList = new FicList();


        return ficList;
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

    public static FiCol rfcTxSelectFields() {
        FiCol fiCol = new FiCol("rfcTxSelectFields", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }


}