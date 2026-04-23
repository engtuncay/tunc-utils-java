package ozpasyazilim.utils.metadata.fimCodegen;

import ozpasyazilim.utils.core.FiString;

/**
 * {@link FimOcFieldType} için yardımcı metodlar
 */
public class FimOcFieldTypeExt {


  public static boolean isDouble(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimOcFieldType.fdouble().getValue()
        , FimOcFieldType.fdecimal().getValue());
  }

  public static boolean isInteger(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimOcFieldType.fint().getValue()
        , FimOcFieldType.ftinyint().getValue());
  }

  public static boolean isDate(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimOcFieldType.fdate().getValue()
    );
  }

  public static boolean isBool(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimOcFieldType.fbool().getValue());
  }
}
