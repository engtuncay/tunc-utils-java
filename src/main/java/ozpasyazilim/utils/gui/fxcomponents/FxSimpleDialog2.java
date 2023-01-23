package ozpasyazilim.utils.gui.fxcomponents;

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
