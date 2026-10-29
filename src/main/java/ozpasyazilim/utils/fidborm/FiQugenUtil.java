package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.table.FiCol;

import javax.annotation.Nonnull;

/**
 * Query Generation Util Methods
 * <p>
 * FiQuGenMs vs sınıflarda kulanılır
 */
public class FiQugenUtil {

  /**
   * Uygulanan Şablon :
   * <p>
   * fcTxFieldName + " = @" + fcTxFieldName + getTxAnd()
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlAssignWitAnd(String fcTxFieldName) {
    return fcTxFieldName + " = @" + fcTxFieldName + getTxAnd();
  }

  /**
   *
   * fcTxFieldName + " IN ( @" + fcTxFieldName + " ) " şeklinde üretir
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlAssignInWitAnd(String fcTxFieldName) {
    return fcTxFieldName + " IN ( @" + fcTxFieldName + " ) " + getTxAnd();
  }

  /**
   *
   * "fcTxFieldName = @fcTxFieldName AND " şeklinde şablon üretir
   *
   * @param fiCol
   * @return
   */
  public static String formSqlAssignWitAndByFic(FiCol fiCol) {
    return formSqlAssignWitAnd(fiCol.getFcTxFieldName());
  }

  /**
   *
   * fcTxFieldName + " IN ( @" + fcTxFieldName + " ) " şeklinde üretir
   *
   * @param fiCol
   * @return
   */
  public static String formSqlAssignInByFic(FiCol fiCol) {
    return formSqlAssignInWitAnd(fiCol.getFcTxFieldName());
  }


  /**
   * "fieldName = @fieldName , " template oluşturur
   *
   * @param fiCol
   * @return
   */
  public static String formSqlAssignVarWitCommaByFic(FiCol fiCol) {
    // dbField tanımlanmışsa, dbFieldAlan ismi kullanılır
    if (!FiString.isEmptyTrim(fiCol.getFcTxDbField())) {
      return formSqlAssignVarWitComma(fiCol.getFcTxDbField());
    }
    return formSqlAssignVarWitComma(fiCol.getFcTxFieldName());
  }

  /**
   * "fieldName = @fieldName , " template oluşturur
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlAssignVarWitComma(String fcTxFieldName) {
    return fcTxFieldName + " = @" + fcTxFieldName + getTxComma();
  }

  /**
   * "@varName ," template oluşturur
   *
   * @param fcTxFieldName
   * @return
   */
  public static String formSqlVarWitComma(String fcTxFieldName) {
    return " @" + fcTxFieldName + getTxComma();
  }

  /**
   * "@varName ," template oluşturur
   *
   * @param fiCol fiCol.getFcTxFieldName() kullanılır
   * @return
   */
  public static String formSqlVarWitCommaByFic(FiCol fiCol) {
    return formSqlVarWitComma(fiCol.getFcTxFieldName());
  }

  public static String formSqlFieldWitComma(String fcTxFieldName) {
    return fcTxFieldName + getTxComma();
  }

  public static String formSqlFieldWitCommaByFic(FiCol fiCol) {
    return formSqlFieldWitComma(fiCol.getFcTxFieldName());
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
