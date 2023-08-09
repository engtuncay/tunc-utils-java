package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.core.FxPredicateString;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.AbsFxSimpleBaseCont;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FxSimpleDialog<EntClazz> extends AbsFxSimpleBaseCont {

	FxMigPaneView modView;
	FxSimpleDialogMetaType fxSimpleDialogMetaType;
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
	private FxFormc fxFormMig;

	private FxFormMig3 fxFormMig3;
	private List<FiCol> fiColList;
	private Predicate<String> predValidateString;
	private String validateErrorMessage;
	private String txInitialValue;

	// Experimental
	private Predicate<EntClazz> predValidate;

	private Predicate<FxFormMig3> predValidateForm;

	/**
	 * Dialog penceresinde ok tıklandıktan sonra çalıştırılacak
	 */
	private Runnable runAfterOkEvent;
	private EntClazz value;
	private FxMigPane fxMigToolbar;
	private FxTextField fxTextFieldGeneral;


	public static void creInfoDialog(String message) {
		FxSimpleDialog modDialogCont = FxSimpleDialog.bui(FxSimpleDialogMetaType.InfoLabelDialog).buiMessageContent(message);
		modDialogCont.openAsDialogSync();
	}

	public static FxSimpleDialog buiTextFieldDialog(String message) {
		FxSimpleDialog modDialogCont = FxSimpleDialog.bui(FxSimpleDialogMetaType.TextField).buiMessageContent(message);
		modDialogCont.openAsDialogSync();
		return modDialogCont;
	}

	public FxSimpleDialog buiMessageContent(String text) {
		setMessageContent(text);
		return this;
	}

	public FxSimpleDialog() {

	}

	public static FxSimpleDialog bui(FxSimpleDialogMetaType fxSimpleDialogMetaType) {
		FxSimpleDialog fxSimpleDialog = new FxSimpleDialog();
		fxSimpleDialog.setFxSimpleDialogType(fxSimpleDialogMetaType);
		return fxSimpleDialog;
	}

	public FxSimpleDialog buildAddAllText(Text... arrText) {
		setListText(Arrays.asList(arrText));
		return this;
	}

	/**
	 * With start of initCont
	 *
	 * @param fxSimpleDialogMetaType
	 * @param messageContent
	 */
	public FxSimpleDialog(FxSimpleDialogMetaType fxSimpleDialogMetaType, String messageContent) {
		setFxSimpleDialogType(fxSimpleDialogMetaType);
		setMessageContent(messageContent);
		//setiFxModCont(this);
		initCont();
	}



	public FxSimpleDialog(FxSimpleDialogMetaType fxSimpleDialogMetaType, String messageContent, String messageHeader) {
		setFxSimpleDialogType(fxSimpleDialogMetaType);
		setMessageContent(messageContent);
		setMessageHeader(messageHeader);
		initCont();
	}

	public FxSimpleDialog(FxSimpleDialogMetaType fxSimpleDialogMetaType) {
		setFxSimpleDialogType(fxSimpleDialogMetaType);
	}

	public void openAsDialogSync() {
		if (!getBoInitExecutedNtn()) {
			initCont();
		}
		//getiFxModCont().getModView().getRootPane().getStylesheets().add("main.css");
		FxDialogShow.nodeWindow(null, this);
	}

	public static FxSimpleDialog bui(FxSimpleDialogMetaType fxSimpleDialogMetaType, String message) {
		FxSimpleDialog modDialogCont = new FxSimpleDialog(fxSimpleDialogMetaType, message);
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
		if (getFxSimpleDialogType() == null) {
			setupTextHeaderLabel();
			setupTextFieldDoubleDialog();
			setupFooterOkCancel(null);
			return;
		}

		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.TextFieldDouble) {
			setupTextHeaderLabel();
			setupTextFieldDoubleDialog();
			setupFooterOkCancel(null);
			return;
		}

		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.TextFieldWithValidation) {
			setupTextFieldWithValidation();
			setupFooterWithValidateString();
			return;
		}

		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.TextField) {
			initTextField();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.TextAreaString) {
			setupTextHeaderLabel();
			setupTextAreaString();
			setupFooterOkCancel(null);
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.TextFieldInteger) {
			setupTextHeaderLabel();
			setupTextFieldIntegerDialog();
			setupFooterOkCancel(null);
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.InfoTextFlowDialog) {
			setupTextHeaderLabel();
			setupInfoDialog();
			setupFooterOkCancel(null);
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.FormAutoByCandIdFields) {
			initFormAutoByCandIdFields();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.InfoLabelDialog) {
			initInfoLabelDialog();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.DialogError) {
			initDialogError();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.DialogInfo) {
			initDialogInfo();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.FormDialog) {
			initFormDialog();
			return;
		}

		if (fxSimpleDialogMetaType == FxSimpleDialogMetaType.LogTable) {
			initLogTable();
			return;
		}
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

		btnOk = new FxButton("Ok", Icons525.OK);
		btnOk.setOnAction(event -> actBtnOK());

		if(!FiBoolean.isTrue(boDontAddCancel)){
			btnCancel = new FxButton("İptal", Icons525.CANCEL);
			btnCancel.setOnAction(event -> actBtnCancel());
			migFooter.add(btnCancel);
		}
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

		fxFormMig = new FxFormc();

		List<FiField> listFiFieldsCandId = FiEntity.getListFieldsCandId(getEntityClass());

		List<FiCol> fiTableColList = FiCol.convertListFiField(listFiFieldsCandId);
		setFiColList(fiTableColList);

		fxFormMig.setupForm(fiTableColList, FormType.PlainFormV1);

		fxContent.add(fxFormMig, "wrap");
		getModView().add(fxContent, "wrap");

	}

	private void setupFormDialog() {
		FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
		fxContent.add(getFxFormMig(), "wrap");
		getModView().add(fxContent, "wrap");
	}

	public void setupFormDialog(List<FiCol> fiCols, FormType formType) {
		getFxFormInit().setup1(fiCols,FormType.PlainFormV1);
		setFxSimpleDialogType(FxSimpleDialogMetaType.Undefined);
		initCont();
		FxMigPane fxContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
		fxContent.addGrowPushSpan(getFxFormInit());
		getModView().add(fxContent, "wrap");
		setupFooterWithValidateForm();
	}

	public void initFormDialogForUpdate(List<FiCol> fiCols, FormType formType, Object entity) {
		getFxFormInit().setFormEntity(entity);
		getFxFormInit().setBoUpdateForm(true);
		setupFormDialog(fiCols,formType);
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
		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.FormDialog) {

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

			if (getFxFormMig().getFxFormConfigInit().getFnValidateForm() != null) {

				Fdr fdr = (Fdr) getFxFormMig().getFxFormConfig().getFnValidateForm().apply(getFxFormMig());

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

		if (getRunAfterOkEvent()!=null) {
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
			if (!getPredValidateForm().test(getFxFormInit())) {
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
	 *
	 *
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

		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.DialogInfo) {
			fxButton.setFxIcon(Icons525.INFO);
		}

		if (getFxSimpleDialogType() == FxSimpleDialogMetaType.DialogError) {
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

	public FxSimpleDialogMetaType getFxSimpleDialogType() {
		return fxSimpleDialogMetaType;
	}

	public void setFxSimpleDialogType(FxSimpleDialogMetaType fxSimpleDialogMetaType) {
		this.fxSimpleDialogMetaType = fxSimpleDialogMetaType;
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

	public FxFormc getFxFormMig() {
		return fxFormMig;
	}

	public void setFxFormMig(FxFormc fxFormMig) {
		this.fxFormMig = fxFormMig;
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

	public FxFormMig3 getFxFormMig3() {
		return fxFormMig3;
	}

	public FxFormMig3 getFxFormInit() {
		if (fxFormMig3 == null) {
			fxFormMig3 = new FxFormMig3<>();
		}
		return fxFormMig3;
	}

	public Predicate<FxFormMig3> getPredValidateForm() {
		return predValidateForm;
	}

	public void setPredValidateForm(Predicate<FxFormMig3> predValidateForm) {
		this.predValidateForm = predValidateForm;
	}

	public Runnable getRunAfterOkEvent() {
		return runAfterOkEvent;
	}

	public void setRunAfterOkEvent(Runnable runAfterOkEvent) {
		this.runAfterOkEvent = runAfterOkEvent;
	}
}
