package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.table.FicList;

public interface IFkcTableMeta {

    String getITxTableName();

    FkbList genITableCols();

    FkbList genITableColsTrans();
}
