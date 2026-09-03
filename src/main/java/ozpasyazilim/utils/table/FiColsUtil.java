package ozpasyazilim.utils.table;

import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.gui.fxcomponents.FxDatePicker;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.List;

public class FiColsUtil {

  List<FiCol> fiColList;

  public static FiColsUtil bui(List<FiCol> fiColList) {
    return new FiColsUtil(fiColList);
  }

  public FiColsUtil(List<FiCol> fiColList) {
    this.fiColList = fiColList;
  }

  // Getter and Setter

  public List<FiCol> getFiColList() {
    return fiColList;
  }

  public void setFiColList(List<FiCol> fiColList) {
    this.fiColList = fiColList;
  }

  // end - getter and setter

  public FiCol findColumnByFieldName(Object objFieldName) {
    return findColumnByFieldName(objFieldName.toString());
  }

  public FiCol findColumnByFieldName(String fieldName) {

    if (!getFiColList().isEmpty()) {

      for (FiCol fiCol : getFiColList()) {
        if (fiCol.getFcTxFieldName().equals(fieldName)) {
          return fiCol;
        }
      }
    }
    return null;
  }

  public FxDatePicker getEditorCompAsFxDatePicker(String fieldName) {

    FiCol fiCol = findColumnByFieldName(fieldName); //IFiColHelper.build(getListFormElements()).getIFiColByID(fieldName);

    if (fiCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
      FxDatePicker comp = (FxDatePicker) fiCol.getColEditorNode();
      return comp;
    }

    return null;
  }

  public static FiCol changeFieldNameWithNew(FiCol fiCol) {
    fiCol.setFcTxFieldName(fiCol.getFcTxFieldName() + "_new");
    return fiCol;
  }

  /**
   * Null , Required Checks
   * <p>
   * Form verileri kontrol edilirken kullanılıyor
   *
   * @param fiCols
   * @return
   */
  public static Fdr validateFiCols(List<FiCol> fiCols) {

    for (FiCol fiCol : fiCols) {

      // boRequired True, BoNullable False yapılmışsa boş geçilemez.
      if (FiBool.isTrue(fiCol.getFcBoRequired()) || FiBool.isFalse(fiCol.getFcBoNullable())) {

        Object cellValue = fiCol.getColValue();

        // Null ve Boş Alan Kontrolü
        if (cellValue == null ||
            (cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
//					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
          return new Fdr(false, String.format("%s Alanı Zorunludur.Boş Geçilemez.", fiCol.getFcTxHeader()));
        }

      }
    }

    return new Fdr(true);
  }

  /**
   * Null , Required Checks
   * <p>
   * Form verileri kontrol edilirken kullanılıyor
   *
   * @param fiCols
   * @return
   */
  public static Fdr validateFkbByFiCols(Fkb fkbEntity, List<FiCol> fiCols) {

    Fdr fdrMain = new Fdr();

    for (FiCol fiCol : fiCols) {

      // boRequired True, BoNullable False yapılmışsa boş geçilemez.
      if (FiBool.isTrue(fiCol.getFcBoRequired()) || FiBool.isFalse(fiCol.getFcBoNullable())) {

        Object cellValue = fkbEntity.getFicVal(fiCol);
        String message = String.format("%s Alanı Zorunludur.Boş Geçilemez.", fiCol.getFcTxHeader());

        // Null ve Boş Alan Kontrolü
        if (cellValue == null) {
          fdrMain.buiBoResult(false, message);
          return fdrMain;
        }

        if (cellValue instanceof String && FiString.isEmpty((String) cellValue)) {
          fdrMain.buiBoResult(false, message);
          return fdrMain;
        }

      }
    }

    fdrMain.setBoResult(true);

    return fdrMain;
  }

}
