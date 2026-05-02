package ozpasyazilim.utils.metadata.fimCodegen;

import ozpasyazilim.utils.core.FiString;

/**
 * {@link FimQcFieldType} için yardımcı metodlar
 */
public class FimQcFieldTypeExt {


  public static boolean isDouble(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimQcFieldType.fdouble().getValue()
        , FimQcFieldType.fdecimal().getValue());
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
