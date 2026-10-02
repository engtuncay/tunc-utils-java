package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiApp;
import ozpasyazilim.utils.core.FiFile;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FxTableMigV1<S> extends MigPane {

	FxTableViewV1<S> fxTableViewV1;
	FxMigPane paneFooter;
	FxLabel lblFooter;
	FxButton btnExcel;

	public FxTableMigV1() {
		//super("insets 0,fill", "0[grow]", "0[grow]4[]");
		super(new FxMigHp().lcgInset3Gap33().getLcg());
		fxTableViewV1 = new FxTableViewV1<>();
		//fxTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		initComp(fxTableViewV1);
	}

	public void initComp(FxTableViewV1<S> fxTableViewV1) {
		fxTableViewV1.setFxTableMig(this);
		paneFooter = new FxMigPane(new FxMigHp().lcgInset0Gap55().getLcg());
		lblFooter = new FxLabel("");

		btnExcel = new FxButton(Icons525.EXCEL,"Excel");
		btnExcel.setPrefHeight(15d);
		btnExcel.setMaxHeight(15d);
		btnExcel.setOnAction(event -> actBtnExcel());

		paneFooter.add(btnExcel,"ay bottom");
		paneFooter.add(lblFooter);

		this.add(fxTableViewV1, "span,grow,push,wrap");
		this.add(paneFooter, "span");
	}

	private void actBtnExcel() {
		String appDir = FiWinUtils.getUserDirOrDesktopDir();  //+ "\\" + AppParametersGeneral.entegreDirectory;
		if(FiApp.appUserTempDir !=null) appDir = FiApp.appUserTempDir;
		String fileName= "entegre_" + FiFile.getCurrentTimeStampForFile() + ".xlsx";
		getFxTableView().excelOpen(appDir,fileName);
	}

	private void initGui() {

	}

	public FxTableMigV1(FxTableViewV1 fxTableViewV1) {
		//super("insets 0,fill", "0[grow]", "0[grow]4[]");
		super(FxMigHp.bui().lcgInset3Gap33().getLcgPrepOnly());
		setFxTableView(fxTableViewV1);
		initComp(fxTableViewV1);
		//super.getChildren().add(fxTableMig);
	}

	public FxTableViewV1<S> getFxTableView() {
		return fxTableViewV1;
	}

	public void setFxTableView(FxTableViewV1<S> fxTableViewV1) {
		this.fxTableViewV1 = fxTableViewV1;
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
