package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.table.OzColType;

public class FiColsMetaTable {

    public static FiCol fcTxFieldType() {
        FiCol fiCol = new FiCol("fcTxFieldType", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcTxFieldName() {
        FiCol fiCol = new FiCol("fcTxFieldName", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }

    public static FiCol fcTxEntityName() {
        FiCol fiCol = new FiCol("fcTxEntityName", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }

    public static FiCol fcTxHeader() {
        FiCol fiCol = new FiCol("fcTxHeader", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcTxFieldDesc() {
        FiCol fiCol = new FiCol("fcTxFieldDesc", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofiTxIdType() {
        FiCol fiCol = new FiCol("ofiTxIdType", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoUniqGro1() {
        FiCol fiCol = new FiCol("fcBoUniqGro1", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoNullable() {
        FiCol fiCol = new FiCol("fcBoNullable", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoUnique() {
        FiCol fiCol = new FiCol("fcBoUnique", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoUtfSupport() {
        FiCol fiCol = new FiCol("fcBoUtfSupport", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcTxDefValue() {
        FiCol fiCol = new FiCol("fcTxDefValue", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcTxCollation() {
        FiCol fiCol = new FiCol("fcTxCollation", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcTxTypeName() {
        FiCol fiCol = new FiCol("fcTxTypeName", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcLnLength() {
        FiCol fiCol = new FiCol("fcLnLength", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcLnPrecision() {
        FiCol fiCol = new FiCol("fcLnPrecision", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcLnScale() {
        FiCol fiCol = new FiCol("fcLnScale", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoFilterLike() {
        FiCol fiCol = new FiCol("fcBoFilterLike", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol fcBoTransient() {
        FiCol fiCol = new FiCol("fcBoTransient", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }


    public static FicList genTableCols() {

        FicList ficList = new FicList();

        ficList.add(fcTxFieldType());
        ficList.add(fcTxFieldName());
        ficList.add(fcTxHeader());
        ficList.add(fcTxFieldDesc());
        ficList.add(ofiTxIdType());
        ficList.add(fcBoUniqGro1());
        ficList.add(fcBoNullable());
        ficList.add(fcBoUnique());
        ficList.add(fcBoUtfSupport());
        ficList.add(fcTxDefValue());
        ficList.add(fcTxCollation());
        ficList.add(fcTxTypeName());
        ficList.add(fcLnLength());
        ficList.add(fcLnPrecision());
        ficList.add(fcLnScale());
        ficList.add(fcBoFilterLike());
        ficList.add(fcBoTransient());

        return ficList;
    }
}