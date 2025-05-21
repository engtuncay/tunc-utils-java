package ozpasyazilim.utils.ficols;// FiCol Class Generation v1

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiColList;
import ozpasyazilim.utils.fidbanno.FiIdGenerationType;
import ozpasyazilim.utils.fidborm.IFiTableMeta;

public class FicOksCoding implements IFiTableMeta {

    public static String getTxTableName() {
        return "OksCoding";
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
        return "oks";
    }

    public String getITxPrefix() {
        return getTxPrefix();
    }


    public static FiColList genTableCols() {
        FiColList ficList = new FiColList();

        ficList.add(okTableName());
        ficList.add(okTableFields());
        ficList.add(okCsvFields());
        ficList.add(okTxWhere());


        return ficList;
    }

    public static FiColList genTableColsTrans() {
        FiColList ficList = new FiColList();


        return ficList;
    }

    public static FiCol okTableName() {
        FiCol fiCol = new FiCol("okTableName");

        return fiCol;
    }

    public static FiCol okTableFields() {
        FiCol fiCol = new FiCol("okTableFields");

        return fiCol;
    }

    public static FiCol okCsvFields() {
        FiCol fiCol = new FiCol("okCsvFields");

        return fiCol;
    }

    public static FiCol okTxWhere() {
        FiCol fiCol = new FiCol("okTxWhere");

        return fiCol;
    }


}
