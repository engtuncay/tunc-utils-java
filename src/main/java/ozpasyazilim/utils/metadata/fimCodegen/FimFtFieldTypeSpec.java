package ozpasyazilim.utils.metadata.fimCodegen;

import ozpasyazilim.utils.core.FiString;

/**
 * {@link FimFcFiTypes} için yardımcı metodlar
 */
public class FimFtFieldTypeSpec {


  public static boolean isDouble(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFcFiTypes.fDouble().getValue()
        , FimFcFiTypes.fDecimal().getValue()
        , FimFcFiTypes.fFloat().getValue());
  }

  public static boolean isInteger(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFcFiTypes.fint().getValue()
        , FimFcFiTypes.ftinyint().getValue());
  }

  public static boolean isDate(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFcFiTypes.fdate().getValue()
    );
  }

  public static boolean isBool(String txFieldType) {
    return FiString.equalsAny(txFieldType
        , FimFcFiTypes.fBool().getValue()
        , FimFcFiTypes.fBit().getValue()
    );
  }
}
