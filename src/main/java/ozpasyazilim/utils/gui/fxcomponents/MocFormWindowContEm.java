//package ozpasyazilim.utils.gui.fxcomponents;
//
//import ozpasyazilim.utils.gui.fxcomponents.*;
//
///**
// * MocFormWindowContEm initCont yapılması lazım, daha sonra gui metodlarını kullanmak lazım.
// * <p>
// * Form ayarları yapıldıktan sonra initForm yapılması lazım
// */
//public class MocFormWindowContEm extends MocCrudWindowContEm {
//
//  private FxFormMig formMain;
//
//  private FxLabel lblMessHeader;
//
//  /**
//   * Content Alanın üstünde Header Alanı
//   */
//  private FxMigPane migContentHeader;
//  private FxTextArea txaMess;
//
//  public MocFormWindowContEm(String connProfile) {
//    super(connProfile);
//  }
//
//  @Override
//  public void initCont() {
//    super.initCont();
//
//    formMain = new FxFormMig();
//    // formMain alanları yüklendikten init yapılması gerektiği için init yapılmadı.
//    // formMain config yüklendikten sonra init yapılması gerek
//
//    migContentHeader = FxMigPane.buiStandard();
//
//    getModView().getMigContent().addGrowPushSpan(migContentHeader);
//    getModView().getMigContent().addGrowPushSpan(formMain);
//
//  }
//
//  public void setTextToTextArea(String txMessage) {
//    getTxaMessInit().setText(txMessage);
//  }
//
//  public FxFormMig getFormMain() {
//    return formMain;
//  }
//
//  private void setFormMain(FxFormMig formMain) {
//    this.formMain = formMain;
//  }
//
//  public FxFormConfig getFormConfigInit() {
//    return getFormMain().getFxFormConfigInit();
//  }
//
//  public void setFormConfig(FxFormConfig formConfig) {
//    getFormMain().setFxFormConfig(formConfig);
//  }
//
//  public void setTextMessToHeader(String txValue) {
//    getLblMessHeaderInit().setText(txValue);
//    //setTextToTextArea(txValue);
//  }
//
//  public FxLabel getLblMessHeader() {
//    return lblMessHeader;
//  }
//
//  public FxLabel getLblMessHeaderInit() {
//    if (lblMessHeader == null) {
//      lblMessHeader = new FxLabel("");
//      getMigContentHeader().addGrowXPushXSpan(lblMessHeader);
//    }
//    return lblMessHeader;
//  }
//
//  private void setLblMessHeader(FxLabel lblMessHeader) {
//    this.lblMessHeader = lblMessHeader;
//  }
//
//  public void initForm() {
//    getFormMain().initCont();
//  }
//
//  public FxMigPane getMigContentHeader() {
//    return migContentHeader;
//  }
//
//  public void setMigContentHeader(FxMigPane migContentHeader) {
//    this.migContentHeader = migContentHeader;
//  }
//
//  public FxTextArea getTxaMess() {
//    return txaMess;
//  }
//
//  public FxTextArea getTxaMessInit() {
//    if (txaMess == null) {
//      txaMess = new FxTextArea();
//      txaMess.setPrefHeight(80);
//      txaMess.setEditable(false);
//      getMigContentHeader().addGrowXPushXSpan(txaMess);
//    }
//    return txaMess;
//  }
//
//  public void setTxaMess(FxTextArea txaMess) {
//    this.txaMess = txaMess;
//  }
//}