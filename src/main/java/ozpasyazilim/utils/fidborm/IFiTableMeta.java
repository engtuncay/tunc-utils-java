package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FicList;

public interface IFiTableMeta {

    String getITxTableName();

    FicList genITableCols();

    FicList genITableColsTrans();
}
