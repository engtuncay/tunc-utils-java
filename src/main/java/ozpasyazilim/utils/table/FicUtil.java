package ozpasyazilim.utils.table;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.metadata.fimCodegen.FimFtSpecFields;

public class FicUtil {

  public static boolean isDefField(FiCol fiCol) {

    if (fiCol.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
      return true;
    }

    return false;
  }

  public static boolean isIdAutoType(FiCol fiCol) {
    String txAutoType = "auto";
    return txAutoType.equals(fiCol.getFcTxIdType());
  }
}
