package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxFormMig;

public class FiArbFormWindowDiaCont extends FiArbCrudWindowDiaCont {

    private FxFormMig formMain;

    public FiArbFormWindowDiaCont(String connProfile) {
        super(connProfile);
    }

    @Override
    public void initCont() {
        super.initCont();

        formMain = new FxFormMig();
        // formMain alanları yüklendikten init yapılması gerektiği için init yapılmadı.

        getModView().getMigContent().addGrowPushSpan(formMain);
    }

    public FxFormMig getFormMain() {
        return formMain;
    }
}
