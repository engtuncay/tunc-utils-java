package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FiColList;

public interface IFiTableMeta {

    String getITxTableName();

    FiColList genITableCols();

    FiColList genITableColsTrans();
}
