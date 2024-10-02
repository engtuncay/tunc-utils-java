package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.core.FxPredicateString;
import ozpasyazilim.utils.fidborm.FiFieldUtil;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FxSimpleDialog<EntClazz> extends AbsFiModBaseCont {

    FxMigPaneView modView;
    FiDialogMetaType fiDialogMetaType;
    private FxButton btnOk;
    private FxButton btnCancel;
    private String messageContent;
    private String messageHeader;
    // Dialog gelen veriyi tutmak için (component text proplarını bind ederiz)
    private StringProperty txValue;
    private OzColType ozColType;
    private List<Text> listText;
    private Boolean boInitExecuted;
    private FxLabel lblHeader;
    private Class entityClass;

    @Deprecated
    private FxFormcGen fxFormcMig;
    private FxFormMigGen fxFormMigGen;

    private List<FiCol> fiColList;
    private Predicate<String> predValidateString;
    private String validateErrorMessage;
    private String txInitialValue;

    // Experimental
    private Predicate<EntClazz> predValidate;

    private Predicate<FxFormMigGen> predValidateForm;

    /**
     * Dialog penceresinde ok tıklandıktan sonra çalıştırılacak
     */
    private Runnable runAfterOkEvent;
    private EntClazz value;
    private FxMigPane fxMigToolbar;
    private FxTextField fxTextFieldGeneral;


    public static void creInfoDialog(String message) {
        FxSimpleDialog modDialogCont = FxSimpleDialog.bui(FiDialogMetaType.InfoLabelDialog).buiMessageContent(message);
        modDialogCont.openAsDialogSync();
    }

    public static FxSimpleDialog buiTextFieldDialog(String message) {
        FxSimpleDialog modDialogCont = FxSimpleDialog.bui(FiDialogMetaType.TextField).buiMessageContent(message);
        modDialogCont.openAsDialogSync();
        return modDialogCont;
    }

    public FxSimpleDialog buiMessageContent(String text) {
        setMessageContent(text);
        return this;
    }

    public FxSimpleDialog() {

    }

    public static FxSimpleDialog bui(FiDialogMetaType fiDialogMetaType) {
        FxSimpleDialog fxSimpleDialog = new FxSimpleDialog();
        fxSimpleDialog.setFiDialogMetaType(fiDialogMetaType);
        return fxSimpleDialog;
    }

    public FxSimpleDialog buildAddAllText(Text... arrText) {
        setListText(Arrays.asList(arrText));
        return this;
    }

    /**
     * With start of initCont
     *
     * @param fiDialogMetaType
     * @param messageContent
     */
    public FxSimpleDialog(FiDialogMetaType fiDialogMetaType, String messageContent) {
        setFiDialogMetaType(fiDialogMetaType);
        setMessageContent(messageContent);
        //setiFxModCont(this);
        initCont();
    }

    public FxSimpleDialog(FiDialogMetaType fiDialogMetaType, String messageContent, String messageHeader) {
        setFiDialogMetaType(fiDialogMetaType);
        setMessageContent(messageContent);
        setMessageHeader(messageHeader);
        initCont();
    }

    public FxSimpleDialog(FiDialogMetaType fiDialogMetaType) {
        setFiDialogMetaType(fiDialogMetaType);
    }

    public void openAsDialogSync() {
        if (!getBoInitExecutedNtn()) {
            initCont();
        }
        //getiFxModCont().getModView().getRootPane().getStylesheets().add("main.css");
        FxWindow.nodeWindow(null, this);
    }

    public static FxSimpleDialog bui(FiDialogMetaType fiDialogMetaType, String message) {
        FxSimpleDialog modDialogCont = new FxSimpleDialog(fiDialogMetaType, message);
        return modDialogCont;
    }

    @Override
    public void initCont() {
        setBoInitExecuted(true);
        modView = new FxMigPaneView(FxMigHp.bui().lcgInset3Gap33().getLcg());
        dialogInitByType();
    }

    private void dialogInitByType() {
        // default Simple Dialog Type
        if (getFiDialogMetaType() == null) {
            initTextFieldDouble();
            return;
        }

        if (getFiDialogMetaType() == FiDialogMetaType.TextFieldDouble) {
            initTextFieldDouble();
            return;
        }

        if (getFiDialogMetaType() == FiDialogMetaType.TextFieldWithValidation) {
            initTextFieldValidation();
            return;
        }

        if (getFiDialogMetaType() == FiDialogMetaType.TextField) {
            initTextField();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextAreaString) {
            initTextAreaString2();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextAreaString2) {
            initTextAreaString2();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.TextFieldInteger) {
            initTextFieldInteger();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.InfoTextFlowDialog) {
            initInfoTextFlowDialog();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.FormAutoByCandIdFields) {
            initFormAutoByCandIdFields();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.InfoLabelDialog) {
            initInfoLabelDialog();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.DialogError) {
            initDialogError();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.DialogInfo) {
            initDialogInfo();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.FormDialog) {
            initFormDialog();
            return;
        }

        if (fiDialogMetaType == FiDialogMetaType.LogTable) {
            initLogTable();
            return;
        }
    }

    private void initTextFieldDouble() {
        setupTextHeaderLabel();
        setupTextFieldDoubleDialog();
        setupFooterOkCancel(null);
    }

    private void initTextFieldValidation() {
        setupTextFieldWithValidation();
        setupFooterWithValidateString();
    }

    private void initTextAreaString2() {
        setupTextHeaderLabel();
        setupTextAreaString();
        setupFooterOkCancel(null);
    }

    private void initTextFieldInteger() {
        setupTextHeaderLabel();
        setupTextFieldIntegerDialog();
        setupFooterOkCancel(null);
    }

    private void initInfoTextFlowDialog() {
        setupTextHeaderLabel();
        setupInfoDialog();
        setupFooterOkCancel(null);
    }

    public void initTextField() {
        setupTextHeaderLabel();
        setupTextFieldString();
        setupFooterOkCancel(null);
    }

    public void initFormAutoByCandIdFields() {
        setupTextHeaderLabel();
        setupFormByCandID();
        setupFooterOkCancel(null);
    }

    public void initLogTable() {
        setupTextHeaderLabel();
        initFormDialog();
    }

    public void initFormDialog() {
        setupFormDialog();
        setupFooterOkCancel(null);
    }

    public void initDialogInfo() {
        setupDialogWithTextArea();
        setupFooterOkCancel(true);
    }

    public void initInfoLabelDialog() {
        setupTextHeaderLabel();
        setupInfoLabelDialog();
        setupFooterOkCancel(null);
    }

    public FxSimpleDialog<EntClazz> initDialogError() {
        setupDialogWithTextArea();
        setupFooterOkCancel(true);
        return this;
    }


    public void setupFooterOkCancel(Boolean boDontAddCancel) {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

        if (!FiBool.isTrue(boDontAddCancel)) {
            btnCancel = new FxButton("İptal", Icons525.CANCEL);
            btnCancel.setOnAction(event -> actBtnCancel());
            migFooter.add(btnCancel);
        }

        btnOk = new FxButton("Ok", Icons525.OK);
        btnOk.setOnAction(event -> actBtnOK());
        migFooter.add(btnOk);

        getModView().addAlignxRight(migFooter);
    }

    public void setupFooterWithValidateString() {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgNoGrid().lcgInset0Gap55().getLcg());

        btnOk = new FxButton("Ok", Icons525.OK);
        btnCancel = new FxButton("İptal", Icons525.CANCEL);

        btnOk.setOnAction(event -> actBtnOKWithValidate());
        btnCancel.setOnAction(event -> actBtnCancel());

        migFooter.add(btnCancel);
        migFooter.add(btnOk);

        modView.wrapFi();
        modView.addGrowXPushXSpan(migFooter, "alignx right");
    }

    public void setupFooterWithValidateForm() {

        FxMigPane migFooter = new FxMigPane(FxMigHp.bui().lcgNoGrid().lcgInset0Gap55().getLcg());

        btnOk = new FxButton("Ok", Icons525.OK);
        btnCancel = new FxButton("İptal", Icons525.CANCEL);

        btnOk.setOnAction(event -> actBtnOKWithValidateForm());
        btnCancel.setOnAction(event -> actBtnCancel());

        migFooter.add(btnCancel);
        migFooter.add(btnOk);

        modView.add(migFooter, "span,alignx right");
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

    private void setupFormByCandID() {

        getModView().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());

        fxFormcMig = new FxFormcGen();

        List<FiField> listFiFieldsCandId = FiFieldUtil.getListFieldsCandId(getEntityClass());

        List<FiCol> fiTableColList = FiCol.convertListFiField(listFiFieldsCandId);
        setFiColList(fiTableColList);

        fxFormcMig.setupForm(fiTableColList, FormType.PlainFormV1);

        fxContent.add(fxFormcMig, "wrap");
        getModView().add(fxContent, "wrap");

    }

    private void setupFormDialog() {
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());

        if (getFxFormcMig() != null) {
            fxContent.addGrowPushSpan(getFxFormcMig(), ""); //"wrap"
        }

        if (getFxFormMigGen() != null) {
            fxContent.addGrowPushSpan(getFxFormMigGen(), "");
        }

        getModView().addGrowPushSpan(fxContent, ""); //"wrap"
    }

    public void setupFormDialog(List<FiCol> fiCols, FormType formType) {
        getFxFormMigGenInit().setup1(fiCols, FormType.PlainFormV1);
        setFiDialogMetaType(FiDialogMetaType.Undefined);
        initCont();
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        fxContent.addGrowPushSpan(getFxFormMigGenInit());
        getModView().add(fxContent, "wrap");
        setupFooterWithValidateForm();
    }

    public void initFormDialogForUpdate(List<FiCol> fiCols, FormType formType, Object entity) {
        getFxFormMigGenInit().setRefFormEntity(entity);
        getFxFormMigGenInit().setBoUpdateForm(true);
        setupFormDialog(fiCols, formType);
    }

    @Override
    public FxMigPaneView getModView() {
        return modView;
    }


    private void actBtnOK() {
//		if(getPredValidate()!=null){
//			if(!getPredValidate().test(getValue())){
//				return;
//			}
//		}

        // Form Alanlarına validasyon eklenmişse onlar kontrol edilir.
        if (getFiDialogMetaType() == FiDialogMetaType.FormDialog) {

            // Obje ile validasyon yapmak istersek
//			if (getFxFormMig().getFxFormConfigInit().getFnValidateForm() != null) {
//
//				if (getEntityClass() != null) {
//					Fdr fdr = (Fdr) getFxFormMig().getFxFormConfig().getFnValidateForm().apply(getFxFormMig().getFormAsObject(getEntityClass()));
//
//					if (!fdr.isTrueBoResult()) {
//						FxDialogShow.showDbResult(fdr);
//						return;
//					}
//				}
//
//			}

            if (getFxFormcMig() != null && getFxFormcMig().getFxFormConfigInit().getFnValidateFormForFormc() != null) {

                Fdr fdr = (Fdr) getFxFormcMig().getFxFormConfig().getFnValidateFormForFormc().apply(getFxFormcMig());

                if (fdr == null) {
                    FxDialogShow.showPopWarn("İşlem yapılamadı. Sistem Yöneticinize Başvurun. Hata Tanımı:Fdr-Null");
                    return;
                }

                if (!fdr.isTrueBoResult()) {
                    FxDialogShow.showPopWarn("Hata \n" + fdr.getMessage());
                    return;
                }

            }

            if (getFxFormMigGen() != null && getFxFormMigGen().getFnValidateForm() != null) {

                Fdr fdr = (Fdr) getFxFormMigGen().getFnValidateForm().apply(getFxFormMigGen());

                if (fdr == null) {
                    FxDialogShow.showPopWarn("İşlem yapılamadı. Sistem Yöneticinize Başvurun. Hata Tanımı:Fdr-Null");
                    return;
                }

                if (!fdr.isTrueBoResult()) {
                    FxDialogShow.showPopWarn("Hata \n" + fdr.getMessage());
                    return;
                }

            }

        }

        if (getRunAfterOkEvent() != null) {
            getRunAfterOkEvent().run();
        }

        super.closeStageWithDoneReason();
    }

    private void actBtnOKWithValidate() {
        if (getPredValidateString() != null) {
            if (!getPredValidateString().test(getTxValue())) {
                String message = getValidateErrorMessage();
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

        return FxEditorFactory.convertStringValueToObjectByOzColType(getOzColType(), getTxValue());

    }

    public Object getFormValue() {
        return FxEditorFactory.bindFormToEntityByEditorNode(getFiColList(), getEntityClass());
    }

    // ******** Setup Methods

    private void setupTextFieldDoubleDialog() {
        getModView().addGrowXPushXSpan(lblHeader);

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.Double);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().add(fxContent, "wrap");

    }

    private void setupTextFieldIntegerDialog() {
        modView.add(lblHeader, "growx,pushx,wrap");
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        fxTextField.convertNumberTextField2();
        setOzColType(OzColType.Integer);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");
        getModView().add(fxContent, "wrap");

    }

    private void setupTextFieldString() {

        getModView().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextField fxTextField = new FxTextField();
        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().add(fxContent, "wrap");

    }

    private void setupTextFieldWithValidation() {

        FxLabel lblHeader2 = new FxLabel(getMessageHeader());

        getModView().add(lblHeader2, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        fxTextFieldGeneral = new FxTextField();

        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextFieldGeneral.textProperty());

        if (!FiString.isEmpty(getTxInitialValue())) {
            fxTextFieldGeneral.setText(getTxInitialValue());
        }

        fxContent.add(fxTextFieldGeneral, "growx,pushx,wrap");
        getModView().add(getFxMigToolbar(), "growx,pushx,wrap");
        getModView().add(fxContent, "grow,push,wrap");

    }

    private void setupTextAreaString() {

        modView.add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextArea fxTextField = new FxTextArea();
        //fxTextField.convertNumberDoubleTextField1();
        setOzColType(OzColType.String);
        txValueProperty().bindBidirectional(fxTextField.textProperty());
        fxContent.add(fxTextField, "wrap");

        getModView().add(fxContent, "wrap");

    }

    private void setupTextAreaString2() {

        getModView().add(lblHeader, "growx,pushx,wrap");

        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxTextArea fxTextField = new FxTextArea();
        //fxTextField.convertNumberDoubleTextField1();
        //setOzColType(OzColType.String);
        fxTextField.setText(getMessageContent());
        fxContent.add(fxTextField, "wrap");

        getModView().add(fxContent, "wrap");

    }

    private void setupInfoDialog() {
        if (getListText().size() == 0) {
            getListText().add(new Text(""));
        }
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        Text[] texts = (Text[]) getListText().toArray();
        FxTextFlow fxTextFlow = new FxTextFlow(texts);
        fxContent.add(fxTextFlow, "wrap");
        getModView().add(fxContent, "wrap");
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
        getModView().add(fxContent, "wrap");
    }

    /**
     * modView.prefHeight : 150d
     */
    private void setupDialogWithTextArea() {

        FxMigPane fxHeader = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        getModView().addGrowXPushXSpan(fxHeader);
        getModView().addGrowPushSpan(fxContent);
        getModView().prefHeight(150d);

        this.lblHeader = new FxLabel(getMessageHeader());

        FxButton fxButton = new FxButton();
        fxButton.setDisable(true);

        if (getFiDialogMetaType() == FiDialogMetaType.DialogInfo) {
            fxButton.setFxIcon(Icons525.INFO);
        }

        if (getFiDialogMetaType() == FiDialogMetaType.DialogError) {
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

    public FiDialogMetaType getFiDialogMetaType() {
        return fiDialogMetaType;
    }

    public void setFiDialogMetaType(FiDialogMetaType fiDialogMetaType) {
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

    public FxFormcGen getFxFormcMig() {
        return fxFormcMig;
    }

    public void setFxFormcMig(FxFormcGen fxFormcMig) {
        this.fxFormcMig = fxFormcMig;
    }

    public List<FiCol> getFiColList() {
        return fiColList;
    }

    public void setFiColList(List<FiCol> fiColList) {
        this.fiColList = fiColList;
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

    public Predicate<EntClazz> getPredValidate() {
        return predValidate;
    }

    public void setPredValidate(Predicate<EntClazz> predValidate) {
        this.predValidate = predValidate;
    }

    public EntClazz getValue() {
        return value;
    }

    public void setValue(EntClazz value) {
        this.value = value;
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

    public String getValidateErrorMessage() {
        return validateErrorMessage;
    }

    public void setValidateErrorMessage(String validateErrorMessage) {
        this.validateErrorMessage = validateErrorMessage;
    }

    public FxMigPane getFxMigToolbar() {
        if (fxMigToolbar == null) {
            fxMigToolbar = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
        }
        return fxMigToolbar;
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

//	public FxFormMigGen getFxFormMig3() {
//		return fxFormMigGen;
//	}

    public FxFormMigGen getFxFormMigGenInit() {
        if (fxFormMigGen == null) {
            fxFormMigGen = new FxFormMigGen<>();
        }
        return fxFormMigGen;
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

    public FxFormMigGen getFxFormMigGen() {
        return fxFormMigGen;
    }

    public void setFxFormMigGen(FxFormMigGen fxFormMigGen) {
        this.fxFormMigGen = fxFormMigGen;
    }

}