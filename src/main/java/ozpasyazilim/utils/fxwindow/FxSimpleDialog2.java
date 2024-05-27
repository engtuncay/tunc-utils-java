package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPaneView;

/**
 * initCont metodu yenilemesi - initCont'da otomatik dialogInit yapmaz, rootView oluşturur.
 * @param <EntClazz>
 */
public class FxSimpleDialog2<EntClazz> extends FxSimpleDialog<EntClazz> {

	public FxSimpleDialog2() {
	}

	public static FxSimpleDialog2 creInitWithMessageContent(String messageContent) {
		FxSimpleDialog2 fxSimpleDialog = new FxSimpleDialog2();
		fxSimpleDialog.setMessageContent(messageContent);
		fxSimpleDialog.initCont();
		return fxSimpleDialog;
	}

	@Override
	public void initCont() {
		setBoInitExecuted(true);
		modView = new FxMigPaneView(FxMigHp.bui().lcgInset3Gap33().getLcg());
	}

}
