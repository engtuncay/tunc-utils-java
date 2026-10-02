package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiApp;
import ozpasyazilim.utils.core.FiFile;
import ozpasyazilim.utils.mvc.IFiModCont;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FxTableMigV2<EntClazz> extends MigPane {

  private FxTableViewV2<EntClazz> fxTableView;

  private FxMigPane paneFooter;

  /**
   * Sayfalama buttonların konulduğu panel. Sütun başlıkları paneli degil.
   */
  private FxMigPane paneTablePagingHeader;
  private FxMigPane migFooterMain;

  private FxButton btnExcel;
  private FxButton btnSettings;

  private FxLabel lblVersion;
  private FxLabel lblRowCount;
  private FxLabel lblFooterMsg;
  private FxLabel lblFooterMsgSelection;


  /**
   *
   */
  private IFiModCont iFiModCont;


  public FxTableMigV2() {
    super(FxMigHp.bui().lcgInset0Gap03().getLcg());
    fxTableView = new FxTableViewV2<>();
    //fxTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    initComp(fxTableView);
  }

  public FxTableMigV2(FxTableViewV2 fxTableView) {
    super(FxMigHp.bui().lcgInset3Gap00().getLcg());
    setFxTableView(fxTableView);
    initComp(fxTableView);
  }

  public void initComp(FxTableViewV2<EntClazz> fxTableView) {
    fxTableView.setFxTableMig(this);
    paneFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

    lblRowCount = new FxLabel("");
    lblFooterMsg = new FxLabel("");
    lblFooterMsgSelection = new FxLabel("");
    lblVersion = new FxLabel("V2");

    btnExcel = new FxButton(Icons525.EXCEL, "Excel");
    // btnExcel.setPrefHeight(15d);
    // btnExcel.setMaxHeight(15d);
    btnExcel.setOnAction(event -> actBtnExcel());

    // btnSettings = new FxButton(Icons525.CIRCLE,"Ayarlar");
    // btnSettings.setPrefHeight(15d);
    // btnSettings.setMaxHeight(15d);
    // btnSettings.setOnAction(event -> actBtnSettings());

    paneFooter.add(lblVersion);
    // URNOTE ay bottom
    paneFooter.add(btnExcel, "ay bottom");
    paneFooter.add(lblRowCount);
    paneFooter.add(lblFooterMsg);
    paneFooter.add(lblFooterMsgSelection);

    paneTablePagingHeader = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

    migFooterMain = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());
    migFooterMain.add(paneTablePagingHeader, "");
    migFooterMain.add(paneFooter, FxMigHp.bui().ccGapBefore("20").genCc());

    this.add(fxTableView, "span,grow,push");
    this.add(migFooterMain, "span");

    // this.add(paneTablePagingHeader, "span");
    // this.add(paneFooter, "span");

  }

  private void actBtnSettings() {

//		FxSimpleCont fxSimpleCont = new FxSimpleCont();
//		fxSimpleCont.openAsDialogSync(null,true);
    FxDialogShow.showPopWarn("Ayarlar Henüz Aktif Degil.");

  }


  private void actBtnExcel() {

    String appDir = FiWinUtils.getUserDirOrDesktopDir();  //+ "\\" + AppParametersGeneral.entegreDirectory;

    if (FiApp.appUserTempDir != null) appDir = FiApp.appUserTempDir;

    String windowName = "entegre_";

    if (getIFiModCont() != null) {
      windowName = getIFiModCont().getModuleLabel();
    }

    String fileName = windowName + FiFile.getCurrentTimeStampForFile() + ".xlsx";
    //EntHelperFxWindow.DoJobWithDisable(getModView().getBtnExcel(), () -> {
    //FiExcel2.build().writeFxTableViewToExcelWithHeader2(getFxTableView(),);
    getFxTableView().excelOpen(appDir, fileName); //fxTableView.eexcelOpen(appDir,fileName);
    //});

  }


  public FxTableViewV2<EntClazz> getFxTableView() {
    return fxTableView;
  }

  public void setFxTableView(FxTableViewV2<EntClazz> fxTableView) {
    this.fxTableView = fxTableView;
  }

  public FxMigPane getPaneFooter() {
    return paneFooter;
  }

  private void setPaneFooter(FxMigPane paneFooter) {
    this.paneFooter = paneFooter;
  }

  public FxLabel getLblRowCount() {
    return lblRowCount;
  }

  public void setLblRowCount(FxLabel lblRowCount) {
    this.lblRowCount = lblRowCount;
  }

  public FxButton getBtnExcel() {
    return btnExcel;
  }

  public void setBtnExcel(FxButton btnExcel) {
    this.btnExcel = btnExcel;
  }

  public FxLabel getLblFooterMsg() {
    return lblFooterMsg;
  }

  public void setLblFooterMsg(FxLabel lblFooterMsg) {
    this.lblFooterMsg = lblFooterMsg;
  }

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

  public FxLabel getLblVersion() {
    return lblVersion;
  }

  public void setLblVersion(FxLabel lblVersion) {
    this.lblVersion = lblVersion;
  }

  public FxLabel getLblFooterMsgSelection() {
    return lblFooterMsgSelection;
  }

  public void setLblFooterMsgSelection(FxLabel lblFooterMsgSelection) {
    this.lblFooterMsgSelection = lblFooterMsgSelection;
  }

  public FxButton getBtnSettings() {
    return btnSettings;
  }

  public void setBtnSettings(FxButton btnSettings) {
    this.btnSettings = btnSettings;
  }

  public FxMigPane getMigFooterMain() {
    return migFooterMain;
  }

  public void setMigFooterMain(FxMigPane migFooterMain) {
    this.migFooterMain = migFooterMain;
  }
}
