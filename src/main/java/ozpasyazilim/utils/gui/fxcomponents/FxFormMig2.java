package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiMapParams;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColInfHelper;
import ozpasyazilim.utils.table.FiColList;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
public class FxFormMig2<EntClazz> extends FxMigPane<EntClazz> implements IFxModView {
	private Class<EntClazz> entityClazz;

	private Map<String, FiCol> formElementsMap;
	private String guid;
	private FxFormConfig<EntClazz> fxFormConfig; // added 27-01-21
	private Boolean boFormInitialized;
	private ChangeListener<Boolean> fnFocusedChangeListener;
	public FxFormMig2() {
		super("insets 0");
	}

	public FxFormMig2(Class<EntClazz> entityClazz) {
		super("insets 0");
		this.entityClazz = entityClazz;
	}

	public FxFormMig2(List<FiCol> listFormElements) {
		super("insets 0");
		setupListFormElementsDefault(listFormElements);
	}

	public FxFormMig2(List<FiCol> listFormElements, Boolean boInit) {
		super("insets 0");
		if (FiBoolean.isTrue(boInit)) {
			setupListFormElementsDefault(listFormElements);
		}
	}

	@Override
	public Pane getRootPane() {
		return this;
	}

	@Override
	public void initGui() {
	}


	public Node getCompByFieldName(String toString) {
		return FiColInfHelper.build(getListFormElements()).findColumnByFieldName(toString).getColEditorNode();
	}

	public IFiCol getColByFieldName(String toString) {
		return FiColInfHelper.build(getListFormElements()).findColumnByFieldName(toString);
	}

	public List<FiCol> getListFormElements() {
		return getFxFormConfigInit().getListFormElementsInit();
	}

	public FxDatePicker getEditorCompFxDatePicker(String fieldName) {

		IFiCol ozTableCol = FiColInfHelper.build(getListFormElements()).getFiTableColByID(fieldName);

		if (ozTableCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
			FxDatePicker comp = (FxDatePicker) ozTableCol.getColEditorNode();
			return comp;
		}

		return null;
	}

	public Node getNode(Object fieldName) {
		return FxEditorFactory.getEditorNodeByFieldName(getListFormElements(), fieldName.toString());
	}

	public <T> T getFormAsObject(Class<T> clazz) {
		return FxEditorFactory.bindFormToEntityByEditorNode(getListFormElements(), clazz);
	}

	public void bindEntitytoForm(EntClazz formMikroKodDegistir) {
		FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), formMikroKodDegistir);
	}

	public FiMapParams getFormAsFiMapParams() {
		return FxEditorFactory.bindFormEditorToMapByEditorNode(getListFormElements());
	}

	public List<FiCol> getListFiTableColWithFormValue() {
		FxEditorFactory.bindFormValueToFiTableListByEditor(getListFormElements());
		return getListFormElements();
	}

	public EntClazz bindFormToEntity() {
		if (getEntityClazz() == null) setAutoClass();
		EntClazz formEntity = FxEditorFactory.bindFormToEntityByEditorNode(getListFormElements(), getEntityClazz());
		return formEntity;
	}

	public void clearFormFields() {
		FxEditorFactory.clearFormFields(getListFormElements());
	}

	public void setFormElementsHp1(List<FiCol> listFormElements, FormType formType, EntClazz formEntityForEdit, EntClazz formEntityForInsert) {

		FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
		fxFormConfig.setFormType(formType);
		fxFormConfig.setListFormElements(listFormElements);
		if (formEntityForEdit != null) fxFormConfig.setFormEntity(formEntityForEdit);
		if (formEntityForInsert != null) fxFormConfig.setFormEntity(formEntityForInsert);
		setFxFormSetup(fxFormConfig);

		initFormElementsMain();
	}

	public void setupFormElementsHp3(List<FiCol> listFormElements, FormType formType, EntClazz formEntity) {
		FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
		fxFormConfig.setFormType(formType);
		fxFormConfig.setListFormElements(listFormElements);
		fxFormConfig.setFormEntity(formEntity);
		setFxFormSetup(fxFormConfig);
		initFormElementsMain();
	}

	public void setupFormElementsHp2(List<FiCol> listFormElements, FormType formType) {
		FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
		fxFormConfig.setFormType(formType);
		fxFormConfig.setListFormElements(listFormElements);
		setFxFormSetup(fxFormConfig);

		initFormElementsMain();
	}

	// ********************* Main
	public void initFormElementsMain() {
		// form initialized edildiğini belirtir
		setBoFormInitialized(true);
		// Form Configden Degerler Yüklenir (doluysa eğer)
		if (getFxFormConfig() != null) {
			if (getListFormElements() != null) setListFormElements(getFxFormConfig().getListFormElements());
			if (getFxFormConfig().getFormEntity() != null) setFormEntity(getFxFormConfig().getFormEntity());
			if (getFxFormConfig().getBoUpdateForm() != null) setBoUpdateForm(getFxFormConfig().getBoUpdateForm());
			if (getFxFormConfig().getFormType() != null) setFormTypeSelected(getFxFormConfig().getFormType());
		} else { // null sa initiliaze edilir
			getFxFormConfigInit();
		}

		// default form type specified
		if (getFormTypeSelected() == null) setFormTypeSelected(FormType.PlainFormV1);

		// ?????
		if (getListFormElements() instanceof FiColList) {
			FiColList fiTableCols = (FiColList) getListFormElements();
			setFormElementsMap(fiTableCols.getMapCols());
		} else {
			Map<String, FiCol> formMap = FiCollection.listToMapSingle(getListFormElements(),FiCol::getFieldName);
			setFormElementsMap(formMap);
		}

		// Form Oluşturma metodları
		if (getFormTypeSelected() == FormType.PlainFormV1) {
			setupPlainFormV1(getListFormElements());  //,formEntityForEdit,formEntityInsert
			FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getFormEntity());
			afterLoadFormValue();
			return;
		}

		Loghelper.get(getClass()).debug("Null Form Type");
	}

	private void afterLoadFormValue() {

		getListFormElements().forEach(fiTableCol -> {

			if (fiTableCol.getFnEditorNodeRendererAfterFormLoad() != null) {

				Object entityForNodeRenderer = getFormEntity();

				fiTableCol.getFnEditorNodeRendererAfterFormLoad().accept(entityForNodeRenderer, fiTableCol.getColEditorNode());

			}
		});

		getListFormElements().forEach(fiTableCol -> {
			if (fiTableCol.getFnEditorNodeRendererAfterFormLoad2() != null) {
				Object entityForNodeRenderer = getFormEntity();
				fiTableCol.getFnEditorNodeRendererAfterFormLoad2().accept(entityForNodeRenderer, fiTableCol.getColEditorNode(), getListFormElements());
			}
		});
	}

	// Helper Setups
	public void setupListFormElementsDefault(List<FiCol> listFormElements) {
		FxFormConfig fxFormConfig = new FxFormConfig();
		fxFormConfig.setListFormElements(listFormElements);
		fxFormConfig.setFormType(FormType.PlainFormV1);
		setFxFormSetup(fxFormConfig);
		initFormElementsMain();
	}

	public void setFormConfigAndInit(List<FiCol> listFormElements, FormType formType) {
		FxFormConfig fxFormConfig = new FxFormConfig();
		fxFormConfig.setListFormElements(listFormElements);
		fxFormConfig.setFormType(formType);
		setFxFormSetup(fxFormConfig);
		initFormElementsMain();
	}


	public void setFormConfigAndInit(FxFormConfig fxFormConfig) {
		setFxFormSetup(fxFormConfig);
		initFormElementsMain();
	}


	private void setupPlainFormV1(List<FiCol> listFormElements) {

		//Loghelperr.debug(getClass(), "Plain Form By Editor");

		// fxform migpane daha önceden doldurulmuşsa, çıkarılıp temizlenir
		if (getChildren().size() > 0) {
			getChildren().remove(0, getChildren().size() - 1);
		}

		for (FiCol fiCol : listFormElements) {

			Loghelper.get(getClass()).debug("FiCol in Form" + fiCol.getFieldName());

			if (FiBoolean.isTrue(fiCol.getBoHidden())) {
				continue;
			}

			FxEditorFactory.setAutoColEditorClassByColType(fiCol);

			if (FiString.isEmpty(fiCol.getColEditorClass())) {
				fiCol.setColEditorClass(FxTextField.class.getName());
			}

			// Tek satır label göstermek için
			if (fiCol.getColEditorClass().equals(EnumColNodeType.FxLabelRowComment.toString())) {
				FxLabel fxLabelComment = new FxLabel(fiCol.getHeaderName());
				add(fxLabelComment, "span,pushx,growx");
				continue;
			}

			Label lblForm = new Label(fiCol.getHeaderName());
			add(lblForm, "width 30%,wmax 150,wmin 120");

			Object entityForNode = getFormEntity();

			// Editor comp (node) oluşturulur
			Node node = FxEditorFactory.generateEditorNodeFullLifeCycle(fiCol, entityForNode);

			if (FiBoolean.isTrue(getFxFormConfigInit().getBoReadOnlyForm()) || FiBoolean.isFalse(fiCol.getBoEditable())) {
				node.setDisable(true);
			}

			// getFormEntityForEdit() yerin getFormEntity getirildi 213010
			if (getFormEntity() != null && FiBoolean.isTrue(fiCol.getBoNonUpdatable())) {
				node.setDisable(true);
			}

			if (fiCol.getPrefSize() != null) {
				add(node, String.format("width %s,wrap", fiCol.getPrefSize().toString()));
			} else {
				add(node, FxMigHelper.bcc("growx,pushx,wrap").addCcCompMaxWidthSizeByColType(fiCol).genCc());
			}

		} // tblCol for döngüsü sonu

	}

	public Object getEntityByFilterNode(Class clazz) {
		return FxEditorFactory.bindFormToEntityByFilterNode(getListFormElements(), clazz);
	}

	public FormType getFormTypeSelected() {
		return getFxFormConfigInit().getFormTypeOrDef();
	}
	public void setAutoClass() {
		if (getEntityClazz() == null) {
			this.entityClazz = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
	}

	public void setFormTypeSelected(FormType formTypeSelected) {
		getFxFormConfigInit().setFormType(formTypeSelected);
	}
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Class<EntClazz> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<EntClazz> entityClazz) {
		this.entityClazz = entityClazz;
	}

	public void setListFormElements(List<FiCol> listFormElements) {
		getFxFormConfigInit().setListFormElements(listFormElements);
	}
	public Map<String, FiCol> getFormElementsMap() {
		return formElementsMap;
	}

	public void setFormElementsMap(Map<String, FiCol> formElementsMap) {
		this.formElementsMap = formElementsMap;
	}

	public FxFormConfig<EntClazz> getFxFormConfigInit() {
		if (fxFormConfig == null) {
			fxFormConfig = new FxFormConfig<>();
		}
		return fxFormConfig;
	}

	public FxFormConfig<EntClazz> getFxFormConfig() {
		return fxFormConfig;
	}

	public void setFxFormSetup(FxFormConfig<EntClazz> fxFormConfig) {
		this.fxFormConfig = fxFormConfig;
	}

	public Boolean getBoUpdateForm() {
		return getFxFormConfigInit().getBoUpdateForm();
	}

	public Boolean getBoUpdateFormInit() {
		return getFxFormConfigInit().getBoUpdateFormInit();
	}

	public void setBoUpdateForm(Boolean boUpdateForm) {
		getFxFormConfigInit().setBoUpdateForm(boUpdateForm);
	}
	public Boolean getBoFormInitialized() {
		return boFormInitialized;
	}
	public void setBoFormInitialized(Boolean boFormInitialized) {
		this.boFormInitialized = boFormInitialized;
	}
	public void loadEntityToForm(EntClazz formEntity) {
		setFormEntity(formEntity);

//		for (FiCol fiCol : getListFormElements()) {
//
//			// comp'a değer atanır
//			FxEditorFactory.setNodeValueByCompClass(fiCol.getColEditorNode(), fiCol.getColEditorClass(), fiCol.getColEditorValue());
//
//			fiCol.doNodeOperationsAfterInitialValue1(formEntity,fiCol.getColEditorNode());
//			fiCol.doNodeOperationsAfterInitialValue2(formEntity,fiCol.getColEditorNode());
//		}

		FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getFormEntity());
		afterLoadFormValue();
	}

	public EntClazz getFormEntity() {
		return getFxFormConfigInit().getFormEntity();
	}

	public void setFormEntity(EntClazz formEntity) {
		getFxFormConfigInit().setFormEntity(formEntity);
	}

	public void setFormEntityForEdit(EntClazz formEntityForEdit) {
		setBoUpdateForm(true);
		setFormEntity(formEntityForEdit);
	}

	public void formFocusListener(ChangeListener<Boolean> fnFocusedChangeListener){
		if (getFnFocusedChangeListener()!=null) {
			for (FiCol listFormElement : getListFormElements()) {
				listFormElement.getColEditorNode().focusedProperty().removeListener(getFnFocusedChangeListener());
			}
		}

		setFnFocusedChangeListener(fnFocusedChangeListener);
		for (FiCol listFormElement : getListFormElements()) {
			listFormElement.getColEditorNode().focusedProperty().addListener(getFnFocusedChangeListener());
		}
	}

	public ChangeListener<Boolean> getFnFocusedChangeListener() {
		return fnFocusedChangeListener;
	}

	public void setFnFocusedChangeListener(ChangeListener<Boolean> fnFocusedChangeListener) {
		this.fnFocusedChangeListener = fnFocusedChangeListener;
	}

	public void showAsDialog() {
		FxSimpleDialog fxSimpleDialog = new FxSimpleDialog();
	}
}


