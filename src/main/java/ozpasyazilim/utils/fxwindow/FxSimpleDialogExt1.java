package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPaneView;

/**
 * initCont metodu yenilemesi - initCont'da otomatik dialogInit yapmaz, rootView oluşturur.
 * @param <EntClazz>
 */
public class FxSimpleDialogExt1<EntClazz> extends FxSimpleDialog<EntClazz> {

	public FxSimpleDialogExt1() {
	}

	public static FxSimpleDialogExt1 creInitWithMessageContent(String messageContent) {
		FxSimpleDialogExt1 fxSimpleDialog = new FxSimpleDialogExt1();
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
