package ozpasyazilim.utils.ficols;// FiCol Class Generation v1

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.fidborm.IFiTableMeta;

public class FicOqfQuery implements IFiTableMeta {

    public static String getTxTableName() {
        return "OqfQuery";
    }

    public String getITxTableName() {
        return getTxTableName();
    }

    public FicList genITableCols() {
        return genTableCols();
    }

    public FicList genITableColsTrans() {
        return genTableColsTrans();
    }

    public static String getTxPrefix() {
        return "oqf";
    }

    public String getITxPrefix() {
        return getTxPrefix();
    }


    public static FicList genTableCols() {
        FicList ficList = new FicList();

        ficList.add(oqfTxTableName());
        ficList.add(oqfTxTableFields());
        ficList.add(oqfCsvFields());
        ficList.add(oqfTxWhere());


        return ficList;
    }

    public static FicList genTableColsTrans() {
        FicList ficList = new FicList();

        return ficList;
    }

    public static FiCol oqfTxTableName() {
        FiCol fiCol = new FiCol("oqfTxTableName");

        return fiCol;
    }

    public static FiCol oqfTxTableFields() {
        FiCol fiCol = new FiCol("oqfTxTableFields");

        return fiCol;
    }

    public static FiCol oqfCsvFields() {
        FiCol fiCol = new FiCol("oqfCsvFields");

        return fiCol;
    }

    public static FiCol oqfTxWhere() {
        FiCol fiCol = new FiCol("oqfTxWhere");

        return fiCol;
    }


}
