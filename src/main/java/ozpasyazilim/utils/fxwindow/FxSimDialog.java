package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.core.FxPredicateString;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Farklı olarak {@link FxSimDialogView} kullanıldı
 * <p>
 * DialogInit initcont çıkarıldı
 * <p>
 * FxFormc çıkarıldı.
 *
 * @param <EntClazz>
 */
public class FxSimDialog<EntClazz> extends AbsFiModBaseCont {

    FxSimDialogView modView;

    FiDialogMetaType fiDialogMetaType;

    // Comps
    private FxButton btnOk;
    private FxButton btnCancel;
    private Boolean boInitExecuted;
    private FxLabel lblHeader;
    private FxFormMigGen fxForm;

    //
    private String messageContent;
    private String messageHeader;
    private String messageHeaderLong;

    //
    // Dialog gelen veriyi tutmak için (component text proplarını bind ederiz)
    private StringProperty txValue;
    private OzColType ozColType;

    //
    private List<Text> listText;

    //
    private Class entityClass;

    private List<FiCol> listFiCol;

    private String txValidateErrorMessage;
    private String txInitialValue;

    // Preds
    private Predicate<String> predValidateString;
    // Experimental
    private Predicate<EntClazz> predValidateEntity;

    private Predicate<FxFormMigGen> predValidateForm;

    /**
     * set edilirse actBtnOk kontrol edilir.
     */
    private Predicate<FxSimDialog> predValidateComp;

    /**
     * Dialog penceresinde ok tıklandıktan sonra çalıştırılacak
     */
    private Runnable runAfterOkEvent;

    private EntClazz refValue;

    //    private FxMigPane fxMigToolbar;
    private FxTextField fxTextFieldGeneral;

    // Methods

    public static void creInfoDialog(String message) {
        FxSimDialog modDialogCont = FxSimDialog.bui(FiDialogMetaType.InfoLabelDialog).buiMessageContent(message);
        modDialogCont.openAsDialogSync();
    }

    public static FxSimDialog buiTextFieldDialog(String message) {
        FxSimDialog modDialogCont = FxSimDialog.bui(FiDialogMetaType.TextField).buiMessageContent(message);
        modDialogCont.openAsDialogSync();
        return modDialogCont;
    }

    public FxSimDialog buiMessageContent(String text) {
        setMessageContent(text);
        return this;
    }

    public FxSimDialog() {

    }

    public static FxSimDialog bui(FiDialogMetaType fiDialogMetaType) {
        FxSimDialog fxSimpleDialog = new FxSimDialog();
        fxSimpleDialog.setFxSimpleDialogType(fiDialogMetaType);
        return fxSimpleDialog;
    }

    public FxSimDialog buildAddAllText(Text... arrText) {
        setListText(Arrays.asList(arrText));
        return this;
    }

    /**
     * With start of initCont
     *
     * @param fiDialogMetaType
     * @param messageContent
     */
    public FxSimDialog(FiDialogMetaType fiDialogMetaType, String messageContent) {
        setFxSimpleDialogType(fiDialogMetaType);
        setMessageContent(messageContent);
        //setiFxModCont(this);
        initCont();
    }


    public FxSimDialog(FiDialogMetaType fiDialogMetaType, String messageContent, String messageHeader) {
        setFxSimpleDialogType(fiDialogMetaType);
        setMessageContent(messageContent);
        setMessageHeader(messageHeader);
        initCont();
    }

    public FxSimDialog(FiDialogMetaType fiDialogMetaType) {
        setFxSimpleDialogType(fiDialogMetaType);
    }

    public void openAsDialogSync() {
        if (!getBoInitExecutedNtn()) {
            initCont();
        }
        //getiFxModCont().getModView().getRootPane().getStylesheets().add("main.css");
        FxWindow.nodeWindow(null, this);
    }

    public void openAsDialogASync() {
        if (!getBoInitExecutedNtn()) {
            initCont();
        }

        Platform.runLater(()->{
            FxWindow.nodeWindow(null, this);
        });
        //getiFxModCont().getModView().getRootPane().getStylesheets().add("main.css");

    }

    public static FxSimDialog bui(FiDialogMetaType fiDialogMetaType, String message) {
        FxSimDialog fxSimDialog = new FxSimDialog(fiDialogMetaType, message);
        return fxSimDialog;
    }

    @Override
    public void initCont() {
        setBoInitExecuted(true);
        modView = new FxSimDialogView(); //FxMigPaneView(FxMigHp.bui().lcgInset3Gap33().getLcg());
        modView.initGui();
        //dialogInitByType();
    }

    public void initContAndDialog() {
        initCont();
        initDialog();
    }

    public void initDialog() {
        // default Simple Dialog Type
        if (getFxSimpleDialogType() == null) {
            setupTextHeaderLabel();
            setupTextFieldDoubleDialog();
            setupFooterOkCancel(null);
            return;
        }

        if (getFxSimpleDialogType() == FiDialogMetaType.CustomContent1) {
            setupCustomContent1();
            return;
        }

        if (getFxSimpleDialogType() == FiDialogMetaType.TextFieldDouble) {
            setupTextHeaderLabel();
            setupTextFieldDoubleDialog();
            setupFooterOkCancel(null);
            return;
        }

        if (getFxSimpleDialogType() == FiDialogMetaType.TextFieldWithValidation) {
            setupTextFieldWithValidation();
            setupFooterWithValidateString();
            return;
        }

        if (getFxSimpleDialogType() == FiDialogMetaType.TextField) {
            setupTextField();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextAreaString) {
            setupTextHeaderLabel();
            setupTextAreaString();
            setupFooterOkCancel(null);
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextAreaString2) {
            setupTextHeaderLabel();
            setupTextAreaString();
            setupFooterOkCancel(null);
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextFieldInteger) {
            setupTextHeaderLabel();
            setupTextFieldIntegerDialog();
            setupFooterOkCancel(null);
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.InfoTextFlowDialog) {
            setupTextHeaderLabel();
            setupInfoDialog();
            setupFooterOkCancel(null);
            return;
        }

//		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.FormAutoByCandIdFields) {
//			initFormAutoByCandIdFields();
//			return;
//		}

        if (fiDialogMetaType == FiDialogMetaType.InfoLabelDialog) {
            setupInfoLabelDialog2();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.DialogError) {
            setupDialogError();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.DialogInfo) {
            setupDialogInfo();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.FormDialog) {
            setupFormDialog2();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.LogTable) {
            setupLogTable();
            return;
        }
    }

    private void setupCustomContent1() {
        setupTextHeaderLabel2();
        setupFooterOkCancel(null);
    }

    public void setupTextField() {
        setupTextHeaderLabel();
        setupTextFieldString();
        setupFooterOkCancel(null);
    }

//	public void initFormAutoByCandIdFields() {
//		setupTextHeaderLabel();
//		setupFormByCandID();
//		setupFooterOkCancel(null);
//	}

    public void setupLogTable() {
        setupTextHeaderLabel();
        setupFormDialog2();
    }

    public void setupFormDialog2() {
        setupFormDialog();
        setupFooterOkCancel(null);
    }

    public void setupDialogInfo() {
        setupDialogWithTextArea();
        setupFooterOkCancel(true);
    }

    public void setupInfoLabelDialog2() {
        setupTextHeaderLabel();
        setupInfoLabelDialog();
        setupFooterOkCancel(null);
    }

    public FxSimDialog<EntClazz> setupDialogError() {
        setupDialogWithTextArea();
        setupFooterOkCancel(true);
        return this;
    }


    public void setupFooterOkCancel(Boolean boDontAddCancel) {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

        btnOk = new FxButton("Ok", Icons525.OK);
        btnOk.setOnAction(event -> actBtnOK());

        migFooter.add(btnOk);

        if (!FiBool.isTrue(boDontAddCancel)) {
            btnCancel = new FxButton("İptal", Icons525.CANCEL);
            btnCancel.setOnAction(event -> actBtnCancel());
            migFooter.add(btnCancel);
        }

        getModView().getMigRoot().addAlignxRight(migFooter);
    }

    public void setupFooterWithValidateString() {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgNoGrid().lcgInset0Gap55().getLcg());

        btnOk = new FxButton("Ok", Icons525.OK);
        btnCancel = new FxButton("İptal", Icons525.CANCEL);

        btnOk.setOnAction(event -> actBtnOKWithValidate());
        btnCancel.setOnAction(event -> actBtnCancel());

        migFooter.add(btnCancel);
        migFooter.add(btnOk);

        getModView().getMigContent().wrapFi();
        getModView().getMigContent().addGrowXPushXSpan(migFooter, "alignx right");
    }

    public void setupFooterWithValidateForm() {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgNoGrid().lcgInset0Gap55().getLcg());

        btnOk = new FxButton("Ok", Icons525.OK);
        btnCancel = new FxButton("İptal", Icons525.CANCEL);

        btnOk.setOnAction(event -> actBtnOKWithValidateForm());
        btnCancel.setOnAction(event -> actBtnCancel());

        migFooter.add(btnCancel);
        migFooter.add(btnOk);

        getModView().getMigContent().add(migFooter, "span,alignx right");
    }

    private void actBtnCancel() {
        closeStageWithCancelReason();
    }

    public void setupTextHeaderLabel() {

        lblHeader = new FxLabel("");
        lblHeader.setWrapText(true);

        if (getMessageContent() != null) {
            lblHeader.setText(getMessageContent());
        } else {
            lblHeader.setText("Lütfen Gerekli Alanları Doldurunuz.");
        }

    }

    /**
     * messageHeader alanını dolu ise lblHeader olarak Toolbar alanına eklenir
     */
    public void setupTextHeaderLabel2() {

        if (!FiString.isEmpty(getMessageHeader())) {
            lblHeader = new FxLabel(getMessageHeader());
            lblHeader.setWrapText(true);
            getModView().getMigToolbar().addGrowXPushXSpan(lblHeader);
            getModView().getMigToolbar().wrapFi();
        }

        if (!FiString.isEmpty(getMessageHeaderLong())) {
            FxLabel lblHeaderLong = new FxLabel(getMessageHeaderLong());
            lblHeaderLong.setWrapText(true);
            getModView().getMigToolbar().addGrowXPushXSpan(lblHeaderLong);
        }

    }

//	private void setupFormByCandID() {
//
//		getModView().add(lblHeader, "growx,pushx,wrap");
//
//		FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
//
//		fxFormcMig = new FxFormcGen();
//
//		List<FiField> listFiFieldsCandId = FiEntity.getListFieldsCandId(getEntityClass());
//
//		List<FiCol> fiTableColList = FiCol.convertListFiField(listFiFieldsCandId);
//		setFiColList(fiTableColList);
//
//		fxFormcMig.setupForm(fiTableColList, FormType.PlainFormV1);
//
//		fxContent.add(fxFormcMig, "wrap");
//		getModView().add(fxContent, "wrap");
//
//	}

    private void setupFormDialog() {
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());

//		if(getFxFormcMig()!=null){
//			fxContent.add(getFxFormcMig(), "wrap");
//		}

        if (getFxForm() != null) {
            fxContent.add(getFxForm(), "wrap");
        }

        getModView().getMigContent().add(fxContent, "wrap");
    }

    public void setupFormDialog(List<FiCol> fiCols, FormType formType) {
        getFxFormMigGenInit().setup1(fiCols, FormType.PlainFormV1);
        setFxSimpleDialogType(FiDialogMetaType.Undefined);
        initCont();
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        fxContent.addGrowPushSpan(getFxFormMigGenInit());
        getModView().getMigContent().add(fxContent, "wrap");
        setupFooterWithValidateForm();
    }

    public void initFormDialogForUpdate(List<FiCol> fiCols, FormType formType, Object entity) {
        getFxFormMigGenInit().setRefFormEntity(entity);
        getFxFormMigGenInit().setBoUpdateForm(true);
        setupFormDialog(fiCols, formType);
    }

    @Override
    public FxSimDialogView getModView() {
        return modView; //.getMigContent(); //modView;
    }


    private void actBtnOK() {

//		if(getPredValidate()!=null){
//			if(!getPredValidate().test(getValue())){
//				return;
//			}
//		}

        if(getPredValidateComp()!=null){
            if (!getPredValidateComp().test(this)) {
                 return;
            }
        }

        // Form Alanlarına validasyon eklenmişse onlar kontrol edilir.
        if (getFxSimpleDialogType() == FiDialogMetaType.FormDialog) {

            if (getFxForm() != null && getFxForm().getFnValidateForm() != null) {

                Fdr fdr = (Fdr) getFxForm().getFnValidateForm().apply(getFxForm());

                if (fdr == null) {
                    FxDialogShow.showPopWarn("İşlem yapılamadı. Sistem Yöneticinize Başvurun. Hata Tanımı:Fdr-Null");
                    return;
                }

                if (!fdr.isTrueBoResult()) {
                    FxDialogShow.showPopWarn("Hata \n" + fdr.getFdrTxMessage());
                    return;
                }

            }

        }

        if (getRunAfterOkEvent() != null) {
            getRunAfterOkEvent().run();
        }

        super.closeStageWithDoneReason();

    } // end of actBtnOK

    private void actBtnOKWithValidate() {
        if (getPredValidateString() != null) {
            if (!getPredValidateString().test(getTxValue())) {
                String message = getTxValidateErrorMessage();
                if (message == null) message = "Lütfen Geçerli Değerler giriniz.";
                FxDialogShow.showPopWarn(message);
                return;
            }
        }
        super.closeStageWithDoneReason();
    }

    private void actBtnOKWithValidateForm() {
        if (getPredValidateForm() != null) {
            if (!getPredValidateForm().test(getFxFormMigGenInit())) {
//				String message = getValidateErrorMessage();
//				if (message == null) message = "ForLütfen Geçerli bir değer giriniz.";
//				FxDialogShow.showPopWarn(message);
                return;
            }
        }
        super.closeStageWithDoneReason();
    }


    public Boolean isClosedWithOk() {
        if (getCloseReason().equals("done")) {
            return true;
        }
        return false;
    }

    public Object getObjectValue() {

        return FxEditorFactory.toTypeByOzColType(getOzColType(), getTxValue());

    }

    public Object getFormValue() {
        return FxEditorFactory.bindFormToEntityByEditorNode(getListFiCol(), getEntityClass());
    }

    // ******** Setup Methods

    private void setupTextFieldDoubleDialog() {
        getModView().getMigContent().addGrowXPushXSpan(lblHeader);

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        fxTextField.setupNumberDoubleTextField1();
        setOzColType(OzColType.Double);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().getMigContent().add(fxContent, "wrap");

    }

    private void setupTextFieldIntegerDialog() {
        modView.getMigContent().add(lblHeader, "growx,pushx,wrap");
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        fxTextField.setupNumberTextField2();
        setOzColType(OzColType.Integer);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");
        getModView().getMigContent().add(fxContent, "wrap");

    }

    private void setupTextFieldString() {

        getModView().getMigContent().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().getMigContent().add(fxContent, "wrap");

    }

    private void setupTextFieldWithValidation() {

        FxLabel lblHeader2 = new FxLabel(getMessageHeader());

        getModView().getMigContent().add(lblHeader2, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        fxTextFieldGeneral = new FxTextField();

        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextFieldGeneral.textProperty());

        if (!FiString.isEmpty(getTxInitialValue())) {
            fxTextFieldGeneral.setText(getTxInitialValue());
        }

        fxContent.add(fxTextFieldGeneral, "growx,pushx,wrap");
        //getModView().getMigContent().add(getFxMigToolbar(), "growx,pushx,wrap");
        getModView().getMigContent().add(fxContent, "grow,push,wrap");

    }

    private void setupTextAreaString() {

        modView.getMigContent().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextArea fxTextField = new FxTextArea();
        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().getMigContent().add(fxContent, "wrap");

    }

    private void setupTextAreaString2() {

        getModView().getMigContent().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextArea fxTextField = new FxTextArea();
        //fxTextField.convertNumberDoubleTextField1();
        //setOzColType(OzColType.String);
        fxTextField.setText(getMessageContent());
        fxContent.add(fxTextField, "wrap");

        getModView().getMigContent().add(fxContent, "wrap");

    }

    private void setupInfoDialog() {
        if (getListText().size() == 0) {
            getListText().add(new Text(""));
        }
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        Text[] texts = (Text[]) getListText().toArray();
        FxTextFlow fxTextFlow = new FxTextFlow(texts);
        fxContent.add(fxTextFlow, "wrap");
        getModView().getMigContent().add(fxContent, "wrap");
    }

    private void setupInfoLabelDialog() {

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());

        ScrollPane scrollPane = new ScrollPane();
        WebView webView = new WebView();
        webView.getEngine().loadContent(messageContent);

        // FxLabel fxLabel = new FxLabel(FiString.ifNullThenEmpty(getMessage()));
        // fxLabel.set
        // webView.setMinHeight(150);
        // webView.setMaxWidth(500);
        scrollPane.setContent(webView);

        fxContent.add(scrollPane, "wrap");
        getModView().getMigContent().add(fxContent, "wrap");
    }

    /**
     * modView.prefHeight : 150d
     */
    private void setupDialogWithTextArea() {

        FxMigPane fxHeader = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        getModView().getMigContent().addGrowXPushXSpan(fxHeader);
        getModView().getMigContent().addGrowPushSpan(fxContent);
        getModView().getMigContent().prefHeight(150d);

        this.lblHeader = new FxLabel(getMessageHeader());

        FxButton fxButton = new FxButton();
        fxButton.setDisable(true);

        if (getFxSimpleDialogType() == FiDialogMetaType.DialogInfo) {
            fxButton.setFxIcon(Icons525.INFO);
        }

        if (getFxSimpleDialogType() == FiDialogMetaType.DialogError) {
            fxButton.setFxIcon(Icons525.WARNING_SIGN);
        }

        fxHeader.add(fxButton, "gapafter 5");
        fxHeader.addGrowXPushXSpan(getLblHeader());

        if (!FiString.isEmptyTrim(getMessageContent())) {
            FxTextArea fxTextArea = new FxTextArea();
            fxTextArea.setText(messageContent);
            //fxTextArea.setMaxHeight(300);
            //fxTextArea.setMaxWidth(300);
            fxTextArea.setEditable(false);
//			ScrollPane scrollPane = new ScrollPane();
//			scrollPane.setContent(fxTextArea);
            fxContent.addGrowPushSpan(fxTextArea);
        }

    }


    // Getter and Setter

    public FiDialogMetaType getFxSimpleDialogType() {
        return fiDialogMetaType;
    }

    public void setFxSimpleDialogType(FiDialogMetaType fiDialogMetaType) {
        this.fiDialogMetaType = fiDialogMetaType;
    }

    public FxButton getBtnOk() {
        return btnOk;
    }

    public FxButton getBtnCancel() {
        return btnCancel;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTxValue() {
        return txValueProperty().get();
    }

    public StringProperty txValueProperty() {
        if (txValue == null) {
            txValue = new SimpleStringProperty();
        }
        return txValue;
    }

    public void setTxValue(String txValue) {
        txValueProperty().set(txValue);
    }

    public OzColType getOzColType() {
        return ozColType;
    }

    public void setOzColType(OzColType ozColType) {
        this.ozColType = ozColType;
    }

    public List<Text> getListText() {
        if (listText == null) {
            listText = new ArrayList<>();
        }
        return listText;
    }

    public void setListText(List<Text> listText) {
        this.listText = listText;
    }

    public Boolean getBoInitExecutedNtn() {
        if (boInitExecuted == null) {
            return false;
        }
        return boInitExecuted;
    }

    public void setBoInitExecuted(Boolean boInitExecuted) {
        this.boInitExecuted = boInitExecuted;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public List<FiCol> getListFiCol() {
        return listFiCol;
    }

    public void setListFiCol(List<FiCol> listFiCol) {
        this.listFiCol = listFiCol;
    }

    public String getMessageHeader() {
        if (messageHeader == null) {
            return "";
        }
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;

        if (getLblHeader() != null) {
            getLblHeader().setText(messageHeader);
        }

    }

    public FxLabel getLblHeader() {
        return lblHeader;
    }

    public Predicate<EntClazz> getPredValidateEntity() {
        return predValidateEntity;
    }

    public void setPredValidateEntity(Predicate<EntClazz> predValidateEntity) {
        this.predValidateEntity = predValidateEntity;
    }

    public EntClazz getRefValue() {
        return refValue;
    }

    public void setRefValue(EntClazz refValue) {
        this.refValue = refValue;
    }

    public Predicate<String> getPredValidateString() {
        return predValidateString;
    }

    public void setPredValidateString(Predicate<String> predValidateString) {
        this.predValidateString = predValidateString;
    }

    public void setPredValidateString(FxPredicateString fxPredicateString) {
        this.predValidateString = fxPredicateString.getPredicate();
    }

    public String getTxValidateErrorMessage() {
        return txValidateErrorMessage;
    }

    public void setTxValidateErrorMessage(String txValidateErrorMessage) {
        this.txValidateErrorMessage = txValidateErrorMessage;
    }


    public FxTextField getFxTextFieldGeneral() {
        return fxTextFieldGeneral;
    }

    public String getTxInitialValue() {
        return txInitialValue;
    }

    public void setTxInitialValue(String txInitialValue) {
        this.txInitialValue = txInitialValue;
    }

    public FxFormMigGen getFxFormMigGenInit() {
        if (fxForm == null) {
            fxForm = new FxFormMigGen<>();
        }
        return fxForm;
    }

    public Predicate<FxFormMigGen> getPredValidateForm() {
        return predValidateForm;
    }

    public void setPredValidateForm(Predicate<FxFormMigGen> predValidateForm) {
        this.predValidateForm = predValidateForm;
    }

    public Runnable getRunAfterOkEvent() {
        return runAfterOkEvent;
    }

    public void setRunAfterOkEvent(Runnable runAfterOkEvent) {
        this.runAfterOkEvent = runAfterOkEvent;
    }

    public FxFormMigGen getFxForm() {
        return fxForm;
    }

    public void setFxForm(FxFormMigGen fxForm) {
        this.fxForm = fxForm;
    }

    public String getMessageHeaderLong() {
        return messageHeaderLong;
    }

    public void setMessageHeaderLong(String messageHeaderLong) {
        this.messageHeaderLong = messageHeaderLong;
    }

    public Predicate getPredValidateComp() {
        return predValidateComp;
    }

    public void setPredValidateComp(Predicate<FxSimDialog> predValidateComp) {
        this.predValidateComp = predValidateComp;
    }
}


//    public FxMigPane getFxMigToolbar() {
//        if (fxMigToolbar == null) {
//            fxMigToolbar = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
//        }
//        return fxMigToolbar;
//    }

//	public FxFormMigGen getFxFormMig3() {
//		return fxFormMigGen;
//	}

//
//	public FxFormcGen getFxFormcMig() {
//		return fxFormcMig;
//	}
//
//	public void setFxFormcMig(FxFormcGen fxFormcMig) {
//		this.fxFormcMig = fxFormcMig;
//	}
