package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiApp;
import ozpasyazilim.utils.core.FiFile;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FxTableMig<S> extends MigPane {

	FxTableView<S> fxTableView;
	FxMigPane paneFooter;
	FxLabel lblFooter;
	FxButton btnExcel;

	public FxTableMig() {
		//super("insets 0,fill", "0[grow]", "0[grow]4[]");
		super(new FxMigHp().lgcStInset3().genLayConst());
		fxTableView = new FxTableView<>();
		//fxTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		initComp(fxTableView);
	}

	public void initComp(FxTableView<S> fxTableView) {
		fxTableView.setFxTableMig(this);
		paneFooter = new FxMigPane(new FxMigHp().lgcStInset0Gap55().genLayConst());
		lblFooter = new FxLabel("");

		btnExcel = new FxButton(Icons525.EXCEL,"Excel");
		btnExcel.setPrefHeight(15d);
		btnExcel.setMaxHeight(15d);
		btnExcel.setOnAction(event -> actBtnExcel());

		paneFooter.add(btnExcel,"ay bottom");
		paneFooter.add(lblFooter);

		this.add(fxTableView, "span,grow,push,wrap");
		this.add(paneFooter, "span");
	}

	private void actBtnExcel() {
		String appDir = FiWinUtils.getDesktopDirectory();  //+ "\\" + AppParametersGeneral.entegreDirectory;
		if(FiApp.appUserTempDir !=null) appDir = FiApp.appUserTempDir;
		String fileName= "entegre_" + FiFile.getCurrentTimeStampForFile() + ".xlsx";
		getFxTableView().excelOpen(appDir,fileName);
	}

	private void initGui() {

	}

	public FxTableMig(FxTableView fxTableView) {
		//super("insets 0,fill", "0[grow]", "0[grow]4[]");
		super(FxMigHp.bui().lgcStInset3Gap33().getLayConstGen());
		setFxTableView(fxTableView);
		initComp(fxTableView);
		//super.getChildren().add(fxTableMig);
	}

	public FxTableView<S> getFxTableView() {
		return fxTableView;
	}

	public void setFxTableView(FxTableView<S> fxTableView) {
		this.fxTableView = fxTableView;
	}

	public FxMigPane getPaneFooter() {
		return paneFooter;
	}

	private void setPaneFooter(FxMigPane paneFooter) {
		this.paneFooter = paneFooter;
	}

	public FxLabel getLblFooter() {
		return lblFooter;
	}

	public void setLblFooter(FxLabel lblFooter) {
		this.lblFooter = lblFooter;
	}
}
