package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxFormMiga;

public class FiArbFormWindowDiaCont extends FiArbCrudWindowDiaCont {

    private FxFormMiga formMain;

    public FiArbFormWindowDiaCont(String connProfile) {
        super(connProfile);
    }

    @Override
    public void initCont() {
        super.initCont();

        formMain = new FxFormMiga();
        // formMain alanları yüklendikten init yapılması gerektiği için init yapılmadı.

        getModView().getMigContent().addGrowPushSpan(formMain);
    }

    public FxFormMiga getFormMain() {
        return formMain;
    }
}
