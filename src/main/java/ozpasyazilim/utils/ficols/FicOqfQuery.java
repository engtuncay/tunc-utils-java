package ozpasyazilim.utils.ficols;// FiCol Class Generation v1

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiColList;
import ozpasyazilim.utils.fidbanno.FiIdGenerationType;
import ozpasyazilim.utils.fidborm.IFiTableMeta;

public class FicOqfQuery implements IFiTableMeta {

    public static String getTxTableName() {
        return "OqfQuery";
    }

    public String getITxTableName() {
        return getTxTableName();
    }

    public FiColList genITableCols() {
        return genTableCols();
    }

    public FiColList genITableColsTrans() {
        return genTableColsTrans();
    }

    public static String getTxPrefix() {
        return "oqf";
    }

    public String getITxPrefix() {
        return getTxPrefix();
    }


    public static FiColList genTableCols() {
        FiColList ficList = new FiColList();

        ficList.add(oqfTxTableName());
        ficList.add(oqfTxTableFields());
        ficList.add(oqfCsvFields());
        ficList.add(oqfTxWhere());


        return ficList;
    }

    public static FiColList genTableColsTrans() {
        FiColList ficList = new FiColList();

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
