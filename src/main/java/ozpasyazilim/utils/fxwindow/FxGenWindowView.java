package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxFormMig;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMig2;
import ozpasyazilim.utils.mvc.IFxSimpleView;

public class FxGenWindowView implements IFxSimpleView {

	// Containers
	private FxMigPane fxRootMigPane;
	private FxMigPane fxMigToolbar;
	private FxMigPane fxContent;

	private FxFormMig fxFormMig;
	private FxTableMig2 fxTableMig;


	public FxGenWindowView() {

	}

	public FxGenWindowView(Boolean withInit) {
		initGui();
	}

	@Override
	public Pane getRootPane() {
		return fxRootMigPane;
	}

	@Override
	public void initGui() {

		// root init
		fxRootMigPane = new FxMigPane(FxMigPane.lcStandardInset3Gap33);

		// Container Initial.
		fxFormMig = new FxFormMig();
		fxMigToolbar = new FxMigPane(FxMigPane.lcStandardInset3Gap33);
		fxTableMig = new FxTableMig2();
		fxContent = new FxMigPane(FxMigPane.lcStandardInset0Gap50);

		// Comp Init

		// hbox kullanılırsa
		//hbxToolbar = new HBox(10);
		//hbxToolbar.setAlignment(Pos.CENTER_RIGHT);

		// Container Setup
		fxContent.add(fxTableMig, "span,grow,push");
		getFxRootMigPane().add(fxMigToolbar, "span,growx,pushx");
		getFxRootMigPane().add(fxFormMig, "span,growx,pushx");
		getFxRootMigPane().add(fxContent, "span,grow,push");

	}

	public FxMigPane getFxRootMigPane() {
		return fxRootMigPane;
	}

	public FxFormMig getFxFormMig() {
		return fxFormMig;
	}

	public FxMigPane getFxMigToolbar() {
		return fxMigToolbar;
	}

	public FxTableMig2 getFxTableMig() {
		return fxTableMig;
	}


}
