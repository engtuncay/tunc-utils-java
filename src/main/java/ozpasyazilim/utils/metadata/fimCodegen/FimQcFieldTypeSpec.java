package ozpasyazilim.utils.metadata.fimCodegen;

import ozpasyazilim.utils.core.FiString;

/**
 * {@link FimQcFieldType} için yardımcı metodlar
 */
public class FimQcFieldTypeSpec {


  public static boolean isDouble(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimQcFieldType.fDouble().getValue()
        , FimQcFieldType.fDecimal().getValue()
        , FimQcFieldType.fFloat().getValue());
  }

  public static boolean isInteger(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimQcFieldType.fint().getValue()
        , FimQcFieldType.ftinyint().getValue());
  }

  public static boolean isDate(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimQcFieldType.fdate().getValue()
    );
  }

  public static boolean isBool(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimQcFieldType.fbool().getValue());
  }
}
