package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;

public class FxFormMig extends FxFormMigGen<Object> {

  public FxFormMig() {
  }

  public FxFormMig(Class<Object> entityClazz) {
    super(entityClazz);
  }

  public FxFormMig(List<FiCol> listFormElements) {
    super(listFormElements);
  }

  public FxFormMig(List<FiCol> listFormElements, Boolean boInit) {
    super(listFormElements, boInit);
  }

  /**
   * super'de initCont(!!!) otomatik çalıştırılır
   *
   * @param fxFormConfig
   */
  public FxFormMig(FxFormConfig<Object> fxFormConfig) {
    super(fxFormConfig);
  }

  public Fkb getFormAsFkbNotNullKeys() {

    Fkb formAsFkb = FxEditorFactory.getFkbColsByEditorNodeForFiCols(getListFiColWithFormValue());

    List<Object> listDeletedKey = new ArrayList<>();

    formAsFkb.forEach((key, value) -> {
      if (value == null) {
        listDeletedKey.add(key);
      }
    });

    for (Object key : listDeletedKey) {
      formAsFkb.remove(key);
    }

    return formAsFkb;
  }

}
