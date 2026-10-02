package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxFormMig;
import ozpasyazilim.utils.gui.fxcomponents.FxFormMiga;

public class FxWindowFormCont extends FxWindowCrudCont {

  private FxFormMig formMain;

  public FxWindowFormCont(String connProfile) {
    super(connProfile);
  }

  @Override
  public void initCont() {
    super.initCont();
    formMain = getFormMainInit();
    // formMain alanları yüklendikten init yapılması gerektiği için init yapılmadı.

    getModView().getMigContent().addGrowPushSpan(formMain);
  }

  public FxFormMig getFormMain() {
    return formMain;
  }

  public FxFormMig getFormMainInit() {
    if (formMain == null) {
      formMain = new FxFormMig();
    }
    return formMain;
  }
}
