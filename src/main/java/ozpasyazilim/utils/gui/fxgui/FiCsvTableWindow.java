package ozpasyazilim.utils.gui.fxgui;

import ozpasyazilim.utils.fxwindow.FiFxWindowCont;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMigV2;

public class FiCsvTableWindow extends FiFxWindowCont {

    @Override
    public void initCont() {
        super.initCont();

        FxTableMigV2 fxTable = new FxTableMigV2();
        getModView().getMigContent().addGrowPushSpan(fxTable);


    }
}
