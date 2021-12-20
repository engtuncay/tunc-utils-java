package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiApp;
import ozpasyazilim.utils.core.FiFile;
import ozpasyazilim.utils.mvc.IFxSimpleCont;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FxTableMig2<EntClazz> extends MigPane {

	private FxTableView2<EntClazz> fxTableView;
	private FxMigPane paneFooter;
	private FxLabel lblFooterRowCount;
	private FxButton btnExcel;
	private FxButton btnSettings;
	private FxLabel lblFooterMessage;
	private FxLabel lblFooterMessageSelection;
	private FxMigPane tableHeaderPane;
	private IFxSimpleCont iFxSimpleCont;
	private FxLabel lblFooterVer;

	public FxTableMig2() {
		super(FxMigHelper.bui().lcStInset3Gap0().genLc());
		fxTableView = new FxTableView2<>();
		//fxTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		initComp(fxTableView);
	}

	public FxTableMig2(FxTableView2 fxTableView) {
		super(FxMigHelper.bui().lcStInset3Gap0().genLc());
		setFxTableView(fxTableView);
		initComp(fxTableView);
	}

	public void initComp(FxTableView2<EntClazz> fxTableView) {
		fxTableView.setFxTableMig(this);
		paneFooter = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().lcNoGrid().genLc());
		lblFooterRowCount = new FxLabel("");
		lblFooterMessage = new FxLabel("");
		lblFooterMessageSelection = new FxLabel("");
		lblFooterVer = new FxLabel("V2");

		btnExcel = new FxButton(Icons525.EXCEL,"Excel");
		btnExcel.setPrefHeight(15d);
		btnExcel.setMaxHeight(15d);
		btnExcel.setOnAction(event -> actBtnExcel());

		btnSettings = new FxButton(Icons525.CIRCLE,"Ayarlar");
		btnSettings.setPrefHeight(15d);
		btnSettings.setMaxHeight(15d);
		btnSettings.setOnAction(event -> actBtnSettings());

		paneFooter.add(lblFooterVer);
		paneFooter.add(btnExcel,"ay bottom");
		paneFooter.add(btnSettings,"ay bottom");
		paneFooter.add(lblFooterRowCount);
		paneFooter.add(lblFooterMessage);
		paneFooter.add(lblFooterMessageSelection);

		tableHeaderPane = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().lcNoGrid().genLc());

		this.add(tableHeaderPane, "span");
		this.add(fxTableView, "span,grow,push");
		this.add(paneFooter, "span");
	}

	private void actBtnSettings() {

//		FxSimpleCont fxSimpleCont = new FxSimpleCont();
//		fxSimpleCont.openAsDialogSync(null,true);
		FxDialogShow.showPopWarn("Ayarlar Henüz Aktif Degil.Yakında aktif olacak.");

	}


	private void actBtnExcel() {

		String appDir = FiWinUtils.getDesktopDirectory();  //+ "\\" + AppParametersGeneral.entegreDirectory;

		if(FiApp.appUserTempDir !=null) appDir = FiApp.appUserTempDir;

		String windowName = "entegre_";

		if(getiFxSimpleCont()!=null){
			windowName = getiFxSimpleCont().getModuleLabel();
		}

		String fileName= windowName + FiFile.getCurrentTimeStampForFile() + ".xlsx";
		//EntHelperFxWindow.DoJobWithDisable(getModView().getBtnExcel(), () -> {
		//FiExcel2.build().writeFxTableViewToExcelWithHeader2(getFxTableView(),);
		getFxTableView().excelOpen(appDir,fileName); //fxTableView.eexcelOpen(appDir,fileName);
		//});

	}



	public FxTableView2<EntClazz> getFxTableView() {
		return fxTableView;
	}

	public void setFxTableView(FxTableView2<EntClazz> fxTableView) {
		this.fxTableView = fxTableView;
	}

	public FxMigPane getPaneFooter() {
		return paneFooter;
	}

	private void setPaneFooter(FxMigPane paneFooter) {
		this.paneFooter = paneFooter;
	}

	public FxLabel getLblFooterRowCount() {
		return lblFooterRowCount;
	}

	public void setLblFooterRowCount(FxLabel lblFooterRowCount) {
		this.lblFooterRowCount = lblFooterRowCount;
	}

	public FxButton getBtnExcel() {return btnExcel;}

	public void setBtnExcel(FxButton btnExcel) {this.btnExcel = btnExcel;}

	public FxLabel getLblFooterMessage() {return lblFooterMessage;}

	public void setLblFooterMessage(FxLabel lblFooterMessage) {this.lblFooterMessage = lblFooterMessage;}

	public FxMigPane getTableHeaderPane() {
		return tableHeaderPane;
	}

	public void setTableHeaderPane(FxMigPane tableHeaderPane) {
		this.tableHeaderPane = tableHeaderPane;
	}

	public IFxSimpleCont getiFxSimpleCont() {
		return iFxSimpleCont;
	}

	public void setiFxSimpleCont(IFxSimpleCont iFxSimpleCont) {
		this.iFxSimpleCont = iFxSimpleCont;
	}

	public FxLabel getLblFooterVer() {
		return lblFooterVer;
	}

	public void setLblFooterVer(FxLabel lblFooterVer) {
		this.lblFooterVer = lblFooterVer;
	}

	public FxLabel getLblFooterMessageSelection() {return lblFooterMessageSelection;
	}

	public void setLblFooterMessageSelection(FxLabel lblFooterMessageSelection) {
		this.lblFooterMessageSelection = lblFooterMessageSelection;
	}

	public FxButton getBtnSettings() {return btnSettings;}

	public void setBtnSettings(FxButton btnSettings) {this.btnSettings = btnSettings;}

}
