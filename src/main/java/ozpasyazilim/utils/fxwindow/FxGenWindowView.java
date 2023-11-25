package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxFormMigDep;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMig2;
import ozpasyazilim.utils.mvc.IFxSimpleView;

public class FxGenWindowView implements IFxSimpleView {

	// Containers
	private FxMigPane fxRootMigPane;
	private FxMigPane fxMigToolbar;
	private FxMigPane fxContent;

	private FxFormMigDep fxFormMig;
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
		fxRootMigPane = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());

		// Container Initial.
		fxFormMig = new FxFormMigDep();
		fxMigToolbar = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());
		fxTableMig = new FxTableMig2();
		fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap50().getLcgPrep2());

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

	public FxFormMigDep getFxFormMig() {
		return fxFormMig;
	}

	public FxMigPane getFxMigToolbar() {
		return fxMigToolbar;
	}

	public FxTableMig2 getFxTableMig() {
		return fxTableMig;
	}


}
