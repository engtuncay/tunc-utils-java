package ozpasyazilim.utils.gui.fxgui;

import ozpasyazilim.utils.fxwindow.FiFxWindowCont;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMig2;

public class FiCsvTableWindow extends FiFxWindowCont {

    @Override
    public void initCont() {
        super.initCont();

        FxTableMig2 fxTable = new FxTableMig2();
        getModView().getMigContent().addGrowPushSpan(fxTable);


    }
}
