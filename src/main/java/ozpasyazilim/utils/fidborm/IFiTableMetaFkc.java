package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.FkbList;

public interface IFiTableMetaFkc {

    String getITxTableName();

    FkbList genITableCols();

    FkbList genITableColsTrans();
}
