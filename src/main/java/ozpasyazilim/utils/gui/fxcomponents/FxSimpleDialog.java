package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.core.FxPredicateString;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.mvc.AbsFxSimpleCont;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FxSimpleDialog<EntClazz> extends AbsFxSimpleCont {
	FxMigPaneView modView;
	FxSimpleDialogType fxSimpleDialogType;
	private FxButton btnOk;
	private FxButton btnCancel;
	String messageContent;
	String messageHeader;
	// Dialog gelen veriyi tutmak için (component text proplarını bind ederiz)
	StringProperty txValue = new SimpleStringProperty();
	OzColType ozColType;
	List<Text> listText;
	Boolean boInitExecuted;
	private FxLabel lblHeader;
	private Class entityClass;
	private FxFormMig2 fxFormMig;
	private List<FiCol> fiColList;
	private Predicate<String> predValidateString;
	private String validateErrorMessage;
	private String txInitialValue;

	// Experimental
	private Predicate<EntClazz> predValidate;
	private EntClazz value;
	private FxMigPane fxMigToolbar;
	private FxTextField fxTextFieldGeneral;


	public static void creInfoDialog(String message) {
		FxSimpleDialog modDialogCont = FxSimpleDialog.build(FxSimpleDialogType.InfoLabelDialog).buiMessageContent(message);
		modDialogCont.openAsDialogSync();
	}

	public static FxSimpleDialog buiTextFieldDialog(String message) {
		FxSimpleDialog modDialogCont = FxSimpleDialog.build(FxSimpleDialogType.TextField).buiMessageContent(message);
		modDialogCont.openAsDialogSync();
		return modDialogCont;
	}

	public FxSimpleDialog buiMessageContent(String text) {
		setMessageContent(text);
		return this;
	}

	public FxSimpleDialog() {

	}

	public static FxSimpleDialog build(FxSimpleDialogType fxSimpleDialogType) {
		FxSimpleDialog fxSimpleDialog = new FxSimpleDialog();
		fxSimpleDialog.setFxSimpleDialogType(fxSimpleDialogType);
		return fxSimpleDialog;
	}

	public FxSimpleDialog buildAddAllText(Text... arrText) {
		setListText(Arrays.asList(arrText));
		return this;
	}

	/**
	 * With start of initCont
	 *
	 * @param fxSimpleDialogType
	 * @param messageContent
	 */
	public FxSimpleDialog(FxSimpleDialogType fxSimpleDialogType, String messageContent) {
		setFxSimpleDialogType(fxSimpleDialogType);
		setMessageContent(messageContent);
		//setiFxModCont(this);
		initCont();
	}

	public FxSimpleDialog(FxSimpleDialogType fxSimpleDialogType) {
		setFxSimpleDialogType(fxSimpleDialogType);
	}

	public void openAsDialogSync() {
		if (!getBoInitExecuted()) {
			initCont();
		}
		FxDialogShow fxDialogShow = new FxDialogShow();
		//getiFxModCont().getModView().getRootPane().getStylesheets().add("main.css");
		fxDialogShow.nodeModalByIFxMod(null, this);
	}

	public static FxSimpleDialog build(FxSimpleDialogType fxSimpleDialogType, String message) {
		FxSimpleDialog modDialogCont = new FxSimpleDialog(fxSimpleDialogType, message);
		return modDialogCont;
	}

	@Override
	public void initCont() {
		setBoInitExecuted(true);
		modView = new FxMigPaneView(FxMigHelper.bui().lcStInset3().lcNoGrid().genLc());

		// default Simple Dialog Type
		if (fxSimpleDialogType == null) {
			setupTextHeaderLabel();
			setupTextFieldDoubleDialog();
			setupFooterOkCancel();
		}

		if (fxSimpleDialogType == FxSimpleDialogType.TextFieldDouble) {
			setupTextHeaderLabel();
			setupTextFieldDoubleDialog();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.TextFieldWithValidation) {
			setupTextFieldWithValidation();
			setupFooterWithValidateString();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.TextField) {
			setupTextHeaderLabel();
			setupTextFieldString();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.TextAreaString) {
			setupTextHeaderLabel();
			setupTextAreaString();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.TextFieldInteger) {
			setupTextHeaderLabel();
			setupTextFieldIntegerDialog();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.InfoTextFlowDialog) {
			setupTextHeaderLabel();
			setupInfoDialog();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.FormAutoByCandIdFields) {
			setupTextHeaderLabel();
			setupFormByCandID();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.FormAutoByCandIdFields) {
			setupTextHeaderLabel();
			setupFormByCandID();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.InfoLabelDialog) {
			setupTextHeaderLabel();
			setupInfoLabelDialog();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.DialogError) {
			setupDialogInfoWarnTextArea();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.DialogInfo) {
			setupDialogInfoWarnTextArea();
			setupFooterOkCancel();
			return;
		}

		if (fxSimpleDialogType == FxSimpleDialogType.FormDialog) {
			setupFormDialog();
			setupFooterOkCancel();
			return;
		}

	}


	public void setupFooterOkCancel() {

		FxMigPane migFooter = new FxMigPane(FxMigHelper.bui().lcNoGrid().lcStInset0Gap55().genLc());

		btnOk = new FxButton("Ok", Icons525.OK);
		btnCancel = new FxButton("İptal", Icons525.CANCEL);

		btnOk.setOnAction(event -> actBtnOK());

		migFooter.add(btnCancel);
		migFooter.add(btnOk);

		modView.add(migFooter, "span,alignx right");
	}

	public void setupFooterWithValidateString() {

		FxMigPane migFooter = new FxMigPane(FxMigHelper.bui().lcNoGrid().lcStInset0Gap55().genLc());

		btnOk = new FxButton("Ok", Icons525.OK);
		btnCancel = new FxButton("İptal", Icons525.CANCEL);

		btnOk.setOnAction(event -> actBtnOKWithValidate());
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

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());

		fxFormMig = new FxFormMig2();

		List<FiField> listFiFieldsCandId = FiEntity.getListFieldsCandId(getEntityClass());

		List<FiCol> fiTableColList = FiCol.convertListFiField(listFiFieldsCandId);
		setFiColList(fiTableColList);

		fxFormMig.setupForm(fiTableColList, FormType.PlainFormV1);

		fxContent.add(fxFormMig, "wrap");
		getModView().add(fxContent, "wrap");

	}

	private void setupFormDialog() {

//		getModView().add(lblHeader, "growx,pushx,wrap");

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
		fxContent.add(getFxFormMig(), "wrap");
		getModView().add(fxContent, "wrap");

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
		if (getFxSimpleDialogType() == FxSimpleDialogType.FormDialog) {

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

				if(fdr==null){
					FxDialogShow.showPopWarn("İşlem yapılamadı. Sistem Yöneticinize Başvurun. Hata Tanımı:Fdr-Null");
					return;
				}

				if (!fdr.isTrueBoResult()) {
					FxDialogShow.showPopWarn("Hata \n"+fdr.getMessage());
					return;
				}

			}

		}

		super.closeStageWithDoneReason();
	}

	private void actBtnOKWithValidate() {
		if (getPredValidateString() != null) {
			if (!getPredValidateString().test(getTxValue())) {
				String message = getValidateErrorMessage();
				if (message == null) message = "Lütfen Geçerli bir değer giriniz.";
				FxDialogShow.showPopWarn(message);
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
		modView.add(lblHeader, "growx,pushx,wrap");

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
		FxTextField fxTextField = new FxTextField();
		fxTextField.convertNumberDoubleTextField1();
		setOzColType(OzColType.Double);
		txValueProperty().bindBidirectional(fxTextField.textProperty());
		fxContent.add(fxTextField, "wrap");

		getModView().add(fxContent, "wrap");

	}

	private void setupTextFieldIntegerDialog() {
		modView.add(lblHeader, "growx,pushx,wrap");
		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
		FxTextField fxTextField = new FxTextField();
		fxTextField.convertNumberTextField2();
		setOzColType(OzColType.Integer);
		txValueProperty().bindBidirectional(fxTextField.textProperty());
		fxContent.add(fxTextField, "wrap");
		getModView().add(fxContent, "wrap");

	}

	private void setupTextFieldString() {

		modView.add(lblHeader, "growx,pushx,wrap");

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
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

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
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

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
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
		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
		Text[] texts = (Text[]) getListText().toArray();
		FxTextFlow fxTextFlow = new FxTextFlow(texts);
		fxContent.add(fxTextFlow, "wrap");
		getModView().add(fxContent, "wrap");
	}

	private void setupInfoLabelDialog() {

		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());

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

	private void setupDialogInfoWarnTextArea() {

		FxMigPane fxHeader = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
		FxMigPane fxContent = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());

		this.lblHeader = new FxLabel(getMessageHeader());

		FxButton fxButton = new FxButton();

		if (getFxSimpleDialogType() == FxSimpleDialogType.DialogInfo) {
			fxButton.setFxIcon(Icons525.INFO);
		}

		if (getFxSimpleDialogType() == FxSimpleDialogType.DialogError) {
			fxButton.setFxIcon(Icons525.WARNING_SIGN);
		}

		fxButton.setDisable(true);
		fxHeader.add(fxButton, "gapafter 5");
		fxHeader.add(getLblHeader(), "span,growx,pushx");


		if (!FiString.isEmptyTrim(getMessageContent())) {
			FxTextArea fxTextArea = new FxTextArea();
			fxTextArea.setText(messageContent);
			fxTextArea.setPrefHeight(100);
			fxTextArea.setPrefWidth(300);
			fxTextArea.setEditable(false);

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setContent(fxTextArea);

			fxContent.add(scrollPane, "wrap,grow,push");
		}

		getModView().add(fxHeader, "growx,pushx,wrap");
		getModView().add(fxContent, "span,grow,push,wrap");

	}


	// Getter and Setter

	public FxSimpleDialogType getFxSimpleDialogType() {
		return fxSimpleDialogType;
	}

	public void setFxSimpleDialogType(FxSimpleDialogType fxSimpleDialogType) {
		this.fxSimpleDialogType = fxSimpleDialogType;
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
		return txValue.get();
	}

	public StringProperty txValueProperty() {
		return txValue;
	}

	public void setTxValue(String txValue) {
		this.txValue.set(txValue);
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

	public Boolean getBoInitExecuted() {
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

	public FxFormMig2 getFxFormMig() {
		return fxFormMig;
	}

	public void setFxFormMig(FxFormMig2 fxFormMig) {
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
			fxMigToolbar = new FxMigPane(FxMigHelper.bui().lcStInset0Gap55().genLc());
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
}
