package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColList;
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

    public static FiCol field_desc() {
        FiCol fiCol = new FiCol("field_desc", "");
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

    public static FiCol oftBoTransient() {
        FiCol fiCol = new FiCol("oftBoTransient", "");
// fiCol.buiColType(OzColType.);
        return fiCol;
    }


    public static FiColList genTableCols() {

        FiColList fiColList = new FiColList();

        fiColList.add(ofcTxFieldType());
        fiColList.add(ofcTxFieldName());
        fiColList.add(ofcTxHeader());
        fiColList.add(field_desc());
        fiColList.add(ofiTxIdType());
        fiColList.add(ofcBoUniqGro1());
        fiColList.add(ofcBoNullable());
        fiColList.add(ofcBoUnique());
        fiColList.add(ofcBoUtfSupport());
        fiColList.add(ofcTxDefValue());
        fiColList.add(ofcTxCollation());
        fiColList.add(ofcTxTypeName());
        fiColList.add(ofcLnLength());
        fiColList.add(ofcLnPrecision());
        fiColList.add(ofcLnScale());
        fiColList.add(ofcBoFilterLike());
        fiColList.add(oftBoTransient());

        return fiColList;
    }
}