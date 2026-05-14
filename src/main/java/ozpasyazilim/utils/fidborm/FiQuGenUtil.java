package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FiCol;

import javax.annotation.Nonnull;

public class FiQuGenUtil {

  public static String formSqlAssignAnd(String fcTxFieldName) {
    return fcTxFieldName + " = @" + fcTxFieldName + getTxAnd();
  }

  public static String formSqlAssignAndByFic(FiCol fiCol) {
    return formSqlAssignAnd(fiCol.getFcTxFieldName());
  }

  /**
   * "fieldName = @fieldName , " template oluşturur
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlAssignComma(String fcTxFieldName) {
    return fcTxFieldName + " = @" + fcTxFieldName + getTxComma();
  }

  /**
   * "fieldName = @fieldName , " template oluşturur
   *
   * @param fiCol
   * @return
   */
  public static String formSqlAssignCommaByFic(FiCol fiCol) {
    return formSqlAssignComma(fiCol.getFcTxFieldName());
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

  public static String formSqlVarCommaByFic(FiCol fiCol) {
    return formSqlVarComma(fiCol.getFcTxFieldName());
  }

  public static String formSqlFieldComma(String fcTxFieldName) {
    return fcTxFieldName + getTxComma();
  }

  public static String formSqlFieldCommaByFic(FiCol fiCol) {
    return formSqlFieldComma(fiCol.getFcTxFieldName());
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
