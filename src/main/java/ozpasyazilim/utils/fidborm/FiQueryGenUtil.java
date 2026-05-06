package ozpasyazilim.utils.fidborm;

import javax.annotation.Nonnull;

public class FiQueryGenUtil {

  public static String formSqlAssignTemp(String fcTxFieldName) {
    return fcTxFieldName + " = @" + fcTxFieldName + getTxAnd();
  }

  /**
   * "@varName ," template oluşturur
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlVarComma(String fcTxFieldName) {
    return " @" + fcTxFieldName + getTxComma();
  }

  public static String formSqlFieldComma(String fcTxFieldName) {
    return fcTxFieldName + getTxComma();
  }

  @Nonnull
  public static String getTxComma() {
    return ", ";
  }

  @Nonnull
  public static String getTxAnd() {
    return " AND ";
  }


}
