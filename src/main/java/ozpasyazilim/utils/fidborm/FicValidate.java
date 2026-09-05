package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;

import java.util.List;

public class FicValidate {
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
    fdrMain.setBoResult(true);

    for (FiCol fiCol : fiCols) {

      // Required ise Fkb'de bulunmalıdır (boş olmasına gerek yok)
      if (FiBool.isTrue(fiCol.getFcBoRequired())) {

        if (!fkbEntity.containsFic(fiCol)) {
          Fdr fdrReq1 = new Fdr();
          String message = String.format("%s alan bilgisi yoktur. Zorunludur.", fiCol.getFcTxHeader());
          fdrReq1.setFdTxMessage(message);
          fdrReq1.setBoResult(false);
          fdrMain.combineAnd(fdrReq1);
          return fdrMain;
        }

      }

      // Nullable false ise boş olmamalı
      if (FiBool.isFalse(fiCol.getFcBoNullable())) {

        Object cellValue = fkbEntity.getFicVal(fiCol);
        String message = String.format("%s Alanı Zorunludur.Boş olamaz.", fiCol.getFcTxHeader());

        // Null ve Boş Alan Kontrolü
        if (cellValue == null) {
          Fdr fdrReq = new Fdr();
          fdrReq.setFdTxMessage(message);
          fdrReq.setBoResult(false);
          fdrMain.combineAnd(fdrReq);
          return fdrMain;
        }

        // String ise boşsa
        if (cellValue instanceof String && FiString.isEmpty((String) cellValue)) {
          Fdr fdrReq = new Fdr();
          fdrReq.setFdTxMessage(message);
          fdrReq.setBoResult(false);
          fdrMain.combineAnd(fdrReq);
          return fdrMain;
        }

      }

      // diger kontroller ...

    } // end for

    return fdrMain;
  }

  //  /**
//   * Null , Required Checks
//   * <p>
//   * Form verileri kontrol edilirken kullanılıyor
//   *
//   * @param fiCols
//   * @return
//   */
//  public static Fdr validateFiCols(List<FiCol> fiCols) {
//
//    for (FiCol fiCol : fiCols) {
//
//      // boRequired True, BoNullable False yapılmışsa boş geçilemez.
//      if (FiBool.isTrue(fiCol.getFcBoRequired()) || FiBool.isFalse(fiCol.getFcBoNullable())) {
//
//        Object cellValue = fiCol.getColValue();
//
//        // Null ve Boş Alan Kontrolü
//        if (cellValue == null ||
//            (cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
////					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
//          return new Fdr(false, String.format("%s Alanı Zorunludur.Boş Geçilemez.", fiCol.getFcTxHeader()));
//        }
//
//      }
//    }
//
//    return new Fdr(true);
//  }

}
