package ozpasyazilim.utils.metadata.fimCodegen;

import ozpasyazilim.utils.core.FiString;

/**
 * {@link FimFtFieldType} için yardımcı metodlar
 */
public class FimFtFieldTypeSpec {


  public static boolean isDouble(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFtFieldType.fDouble().getValue()
        , FimFtFieldType.fDecimal().getValue()
        , FimFtFieldType.fFloat().getValue());
  }

  public static boolean isInteger(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFtFieldType.fint().getValue()
        , FimFtFieldType.ftinyint().getValue());
  }

  public static boolean isDate(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFtFieldType.fdate().getValue()
    );
  }

  public static boolean isBool(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFtFieldType.fbool().getValue());
  }
}
