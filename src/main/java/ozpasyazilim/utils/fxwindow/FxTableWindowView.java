package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.gui.fxcomponents.FxTableMig2;
import ozpasyazilim.utils.gui.fxcomponents.FxTableView2;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.mvc.IFxSimpleView;
import ozpasyazilim.utils.mvc.IFxTempView;

public class FxTableWindowView<EntClazz> implements IFxModView, IFxTempView<FxTableWindowCont>, IFxSimpleView {

	// Containers
	// rootMigPane super de
	//private FxStackPane fxStackPane;
	private FxMigPane fxMigPane;
	private FxTableMig2<EntClazz> fxTableMig;
	private FxMigPane fxMigToolbar;
	private FxMigPane fxMigHeader;
	private FxMigPane fxMigContent;

	public FxTableWindowView() {

	}

	public FxTableWindowView(Boolean withInit) {
		initGui();
	}

	@Override
	public void initGui() {
		// Container Initial.
		this.fxMigPane = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().getLcgPrep2());
//		setRootMigPane(fxMigPane);

		fxMigHeader = new FxMigPane(FxMigHp.bui().lcgInset0Gap00().getLcgPrep2());

		// Comp Init
		fxTableMig = new FxTableMig2<>();
		fxMigToolbar = new FxMigPane(FxMigHp.bui().lcgInset3Gap33().lcgNoGrid().getLcgPrep()); // lcStandard2WithInset3
		fxMigContent = new FxMigPane(FxMigHp.lgcStandard1InsetZeroGap50);

		this.fxMigContent.add(fxTableMig, "span,grow,push");

		// Container Setup
		this.fxMigPane.add(fxMigToolbar, "span,growx,pushx");
		this.fxMigPane.add(fxMigHeader, "span,growx,pushx");
		this.fxMigPane.add(fxMigContent, "span,grow,push");

		//fxTableMig.setPrefWidth(prefWidth);

//		forEach(o -> {
//			TableColumn tc = (TableColumn) o;
//			tc.getPreferredWidth();
//		});


	}

	@Override
	public Pane getRootPane() {
		return fxMigPane;
	}

	public FxMigPane getFxMigToolbar() {
		return fxMigToolbar;
	}

	public FxTableMig2<EntClazz> getFxTableMig() {
		return fxTableMig;
	}

	public FxTableView2<EntClazz> getFxTableView() {
		return getFxTableMig().getFxTableView();
	}

	public FxMigPane getFxMigPane() {
		return fxMigPane;
	}

	public FxMigPane getFxMigContent() {
		return fxMigContent;
	}

	public FxMigPane getFxMigHeader() {return fxMigHeader;}
}
