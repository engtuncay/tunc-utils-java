package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.table.FiCol;

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
   * super'de initCont otomatik çalıştırılır
   *
   * @param fxFormConfig
   */
  public FxFormMig(FxFormConfig<Object> fxFormConfig) {
    super(fxFormConfig);
  }

}
