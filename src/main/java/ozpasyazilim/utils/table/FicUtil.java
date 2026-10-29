package ozpasyazilim.utils.table;

import ozpasyazilim.utils.metadata.fimCodegen.FimFcIdTypes;
import ozpasyazilim.utils.metadata.fimCodegen.FimFcSpecFields;

public class FicUtil {

  /**
   * isDefinitionField (qcfTxSqTableName gibi tanımlama alanları için true döner)
   *
   * @param fiCol
   * @return
   */
  public static boolean isDefField(FiCol fiCol) {

    if (fiCol.getFcTxFieldName().equals(FimFcSpecFields.qcfTxSqTableName().getKey())) {
      return true;
    }

    return false;
  }

  public static boolean isIdAutoType(FiCol fiCol) {
    String txAutoType = FimFcIdTypes.auto().getKey();
    return txAutoType.equals(fiCol.getFcTxIdType());
  }
}
