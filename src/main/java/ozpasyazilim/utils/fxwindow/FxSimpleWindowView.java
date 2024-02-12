package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.mvc.IFiModView;
import ozpasyazilim.utils.mvc.IFxSimpleView;

public class FxSimpleWindowView implements IFiModView, IFxSimpleView {

	// Containers
	// rootMigPane super de
	// private FxStackPane fxStackPane;
	private FxMigPane migRoot;
	private FxMigPane migToolbar;
	private FxMigPane migContent;
	private FxMigPane migFooter;

	public FxSimpleWindowView() {

	}

	public FxSimpleWindowView(Boolean withInit) {
		initGui();
	}

	@Override
	public void initGui() {
		// Root Container
		this.migRoot = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());
		//setRootMigPane(fxMigPane);

		migToolbar = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().lcgNoGrid().getLcg()); // lcStandard2WithInset3
		migContent = new FxMigPane(FxMigHp.lcgStandard1InsetZeroGap50);
		migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap00().getLcgPrep2());

		// Container Setup
		this.migRoot.add(migToolbar, "span,growx,pushx");
		this.migRoot.add(migContent, "span,grow,push");
		this.migRoot.add(migFooter, "span,growx,pushx");
		//fxTableMig.setPrefWidth(prefWidth);
	}

	@Override
	public Pane getRootPane() {
		return migRoot;
	}

	public FxMigPane getMigToolbar() {
		return migToolbar;
	}

	public FxMigPane getMigRoot() {
		return migRoot;
	}

	public FxMigPane getMigContent() {
		return migContent;
	}

	public FxMigPane getMigFooter() {return migFooter;}
}
