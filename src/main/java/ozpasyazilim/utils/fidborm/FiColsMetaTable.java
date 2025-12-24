package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.table.OzColType;

public class FiColsMetaTable {

    public static FiCol ofcTxFieldType() {
        FiCol fiCol = new FiCol("ofcTxFieldType", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcTxFieldName() {
        FiCol fiCol = new FiCol("ofcTxFieldName", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }

    public static FiCol ofcTxEntityName() {
        FiCol fiCol = new FiCol("ofcTxEntityName", "");
        fiCol.buiColType(OzColType.String);
        return fiCol;
    }

    public static FiCol ofcTxHeader() {
        FiCol fiCol = new FiCol("ofcTxHeader", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcTxFieldDesc() {
        FiCol fiCol = new FiCol("ofcTxFieldDesc", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofiTxIdType() {
        FiCol fiCol = new FiCol("ofiTxIdType", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoUniqGro1() {
        FiCol fiCol = new FiCol("ofcBoUniqGro1", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoNullable() {
        FiCol fiCol = new FiCol("ofcBoNullable", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoUnique() {
        FiCol fiCol = new FiCol("ofcBoUnique", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoUtfSupport() {
        FiCol fiCol = new FiCol("ofcBoUtfSupport", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcTxDefValue() {
        FiCol fiCol = new FiCol("ofcTxDefValue", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcTxCollation() {
        FiCol fiCol = new FiCol("ofcTxCollation", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcTxTypeName() {
        FiCol fiCol = new FiCol("ofcTxTypeName", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcLnLength() {
        FiCol fiCol = new FiCol("ofcLnLength", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcLnPrecision() {
        FiCol fiCol = new FiCol("ofcLnPrecision", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcLnScale() {
        FiCol fiCol = new FiCol("ofcLnScale", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoFilterLike() {
        FiCol fiCol = new FiCol("ofcBoFilterLike", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }

    public static FiCol ofcBoTransient() {
        FiCol fiCol = new FiCol("ofcBoTransient", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }


    public static FicList genTableCols() {

        FicList ficList = new FicList();

        ficList.add(ofcTxFieldType());
        ficList.add(ofcTxFieldName());
        ficList.add(ofcTxHeader());
        ficList.add(ofcTxFieldDesc());
        ficList.add(ofiTxIdType());
        ficList.add(ofcBoUniqGro1());
        ficList.add(ofcBoNullable());
        ficList.add(ofcBoUnique());
        ficList.add(ofcBoUtfSupport());
        ficList.add(ofcTxDefValue());
        ficList.add(ofcTxCollation());
        ficList.add(ofcTxTypeName());
        ficList.add(ofcLnLength());
        ficList.add(ofcLnPrecision());
        ficList.add(ofcLnScale());
        ficList.add(ofcBoFilterLike());
        ficList.add(ofcBoTransient());

        return ficList;
    }
}