package ozpasyazilim.utils.gui.fxgui;

import ozpasyazilim.utils.fxwindow.FiArbWindowCont;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMig2;

public class FiCsvTableWindow extends FiArbWindowCont {

    @Override
    public void initCont() {
        super.initCont();

        FxTableMig2 fxTable = new FxTableMig2();
        getModView().getMigContent().addGrowPushSpan(fxTable);


    }
}
