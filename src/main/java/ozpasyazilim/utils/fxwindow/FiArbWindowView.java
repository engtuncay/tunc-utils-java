package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.mvc.IFiModView;

public class FiArbWindowView implements IFiModView {

	// Containers
	private FxMigPane migRoot;
	private FxMigPane migToolbar;
	private FxMigPane migContent;
	private FxMigPane migFooter;
	//private FxMigPane migFooter;

	public FiArbWindowView() {

	}

	public FiArbWindowView(Boolean withInit) {
		initGui();
	}

	@Override
	public Pane getRootPane() {
		return migRoot;
	}

	@Override
	public void initGui() {

		// Container Initial.
		migRoot = new FxMigPane(FxMigHp.bui().lcgInset5Gap05().getLcg()); //migRoot = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());
		migToolbar = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());
		migContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
		migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());
		//migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

		//	migToolbar = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());
		//	migContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap50().getLcgPrep2());

		// Container Setup
		getMigRoot().addGrowXPushXSpan(migToolbar);
		getMigRoot().addGrowPushSpan(migContent);
		getMigRoot().addGrowXPushXSpan(migFooter);

	}

	public FxMigPane getMigRoot() {
		return migRoot;
	}

	public FxMigPane getMigToolbar() {
		return migToolbar;
	}

	public FxMigPane getMigContent() {
		return migContent;
	}

	public FxMigPane getMigFooter() {
		return migFooter;
	}
}
