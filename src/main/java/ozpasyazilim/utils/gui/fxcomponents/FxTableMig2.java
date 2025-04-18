package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiApp;
import ozpasyazilim.utils.core.FiFile;
import ozpasyazilim.utils.mvc.IFiModCont;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FxTableMig2<EntClazz> extends MigPane {

	private FxTableView2<EntClazz> fxTableView;

	private FxMigPane paneFooter;
	private FxButton btnExcel;
	private FxButton btnSettings;

	private FxLabel lblFooterVer;
	private FxLabel lblFooterRowCount;
	private FxLabel lblFooterMsg;
	private FxLabel lblFooterMessageSelection;

	/**
	 * Sayfalama buttonların konulduğu panel. Sütun başlıkları paneli degil.
	 */
	private FxMigPane paneTablePagingHeader;

	/**
	 *
	 */
	private IFiModCont iFiModCont;


	public FxTableMig2() {
		super(FxMigHp.bui().lcgInset0Gap03().getLcg());
		fxTableView = new FxTableView2<>();
		//fxTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		initComp(fxTableView);
	}

	public FxTableMig2(FxTableView2 fxTableView) {
		super(FxMigHp.bui().lcgInset3Gap00().getLcg());
		setFxTableView(fxTableView);
		initComp(fxTableView);
	}

	public void initComp(FxTableView2<EntClazz> fxTableView) {
		fxTableView.setFxTableMig(this);
		paneFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());
		lblFooterRowCount = new FxLabel("");
		lblFooterMsg = new FxLabel("");
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
		paneFooter.add(lblFooterMsg);
		paneFooter.add(lblFooterMessageSelection);

		paneTablePagingHeader = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

		this.add(paneTablePagingHeader, "span");
		this.add(fxTableView, "span,grow,push");
		this.add(paneFooter, "span");
	}

	private void actBtnSettings() {

//		FxSimpleCont fxSimpleCont = new FxSimpleCont();
//		fxSimpleCont.openAsDialogSync(null,true);
		FxDialogShow.showPopWarn("Ayarlar Henüz Aktif Degil.Yakında aktif olacak.");

	}


	private void actBtnExcel() {

		String appDir = FiWinUtils.getUserDirOrDesktopDir();  //+ "\\" + AppParametersGeneral.entegreDirectory;

		if(FiApp.appUserTempDir !=null) appDir = FiApp.appUserTempDir;

		String windowName = "entegre_";

		if(getIFiModCont()!=null){
			windowName = getIFiModCont().getModuleLabel();
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

	public FxLabel getLblFooterMsg() {return lblFooterMsg;}

	public void setLblFooterMsg(FxLabel lblFooterMsg) {this.lblFooterMsg = lblFooterMsg;}

	public FxMigPane getPaneTablePagingHeader() {
		return paneTablePagingHeader;
	}

	public void setPaneTablePagingHeader(FxMigPane paneTablePagingHeader) {
		this.paneTablePagingHeader = paneTablePagingHeader;
	}

	public IFiModCont getIFiModCont() {
		return iFiModCont;
	}

	public void setIFiModCont(IFiModCont iFiModCont) {
		this.iFiModCont = iFiModCont;
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
