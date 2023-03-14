package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColInfHelper;
import ozpasyazilim.utils.table.OzColType;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * FxFormMig3 de FormConfig çıkarıldı. Ana Alanlar direk FxFormMig3 sınıfına eklendi.
 *
 * @param <EntClazz> Form alanın değerlerinin aktarılacağı veya alınacağı sınıf
 */
public class FxFormMig3<EntClazz> extends FxMigPaneEnt<EntClazz> implements IFxModView {
	private Class<EntClazz> entityClazz;
	private String guid;
	private Boolean boFormInitialized;
	private ChangeListener<Boolean> fnFocusedChangeListener;
	private List<FiCol> listFormElements;
	private FormType formType;
	private EntClazz formEntity; // 30-10-21 eklendi

	/**
	 * Form güncellemek amacıyla açıldığını belirtir
	 */
	private Boolean boUpdateForm; // 21-10-30 eklendi

	private Boolean boReadOnlyForm;

	private Function<FxFormMig3, Fdr> fnValidateForm;

	public FxFormMig3() {
		super("insets 0");
	}

	public FxFormMig3(Class<EntClazz> entityClazz) {
		super("insets 0");
		this.entityClazz = entityClazz;
	}

	/**
	 * @param listFormElements
	 */
	public FxFormMig3(List<FiCol> listFormElements) {
		super("insets 0");
		this.listFormElements = listFormElements;
	}

	public FxFormMig3(List<FiCol> colsForm, FormType formType) {
		super("insets 0");
		this.listFormElements = colsForm;
		this.formType = formType;
	}

//	public FxFormMig3(List<FiCol> listFormElements, Boolean boInit) {
//		super("insets 0");
//		if (FiBoolean.isTrue(boInit)) {
//			setupWitDefaultFormType(listFormElements);
//		}
//	}

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
		return listFormElements;
	}

	public FxDatePicker getEditorCompAsFxDatePicker(String fieldName) {

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

	public FiKeyBean getFormAsFiMapParams() {
		return FxEditorFactory.bindFormToKeyBeanByEditorNode(getListFormElements());
	}

	public FiKeyBean getFormAsFkb() {
		return FxEditorFactory.bindFormToKeyBeanByEditorNode(getListFormElements());
	}

	public List<FiCol> getFormAsFiColListWithFormValue() {
		FxEditorFactory.bindFormValueToFiColListByEditor(getListFormElements());
		return getListFormElements();
	}

	public EntClazz bindFormToEntity() {
		if (getEntityClazz() == null) setAutoClass();
		EntClazz formEntity = FxEditorFactory.bindFormToEntityByEditorNode(getListFormElements(), getEntityClazz());
		return formEntity;
	}

	public <PrmEntClazz> PrmEntClazz bindFormToEntity(Class<PrmEntClazz> entClazz) {
		if (entClazz == null) return null;
		PrmEntClazz formEntity = FxEditorFactory.bindFormToEntityByEditorNode(getListFormElements(), entClazz);
		return formEntity;
	}

	public void clearFormFields() {
		FxEditorFactory.clearFormFields(getListFormElements());
	}

	public void initFormHp1(List<FiCol> listFormElements, FormType formType, EntClazz formEntity) {
		setListFormElements(listFormElements);
		setFormTypeSelected(formType);
		setFormEntity(formEntity);
		initFormElementsMain();
	}

//	public void setupFormElementsHp3(List<FiCol> listFormElements, FormType formType, EntClazz formEntity) {
//		FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
//		fxFormConfig.setFormType(formType);
//		fxFormConfig.setListFormElements(listFormElements);
//		fxFormConfig.setFormEntity(formEntity);
//		setFxFormSetup(fxFormConfig);
//		initFormElementsMain();
//	}

//	public void setupFormElementsHp2(List<FiCol> listFormElements, FormType formType) {
//		FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
//		fxFormConfig.setFormType(formType);
//		fxFormConfig.setListFormElements(listFormElements);
//		setFxFormSetup(fxFormConfig);
//
//		initFormElementsMain();
//	}

	// ********************* Main
	public void initFormElementsMain() {
		// form initialized edildiğini belirtir
		setBoFormInitialized(true);
		// Form Configden Degerler Yüklenir (doluysa eğer)

		if (FiCollection.isEmpty(getListFormElements())) {
			Loghelper.get(getClass()).debug("Form Alanları Tanımlanmamış.");
			return;
		}

		// default form type specified
		if (getFormTypeSelected() == null) setFormTypeSelected(FormType.PlainFormV1);

		// FiColList map halinde tutmak için
		// if (getListFormElements() instanceof FiColList) {
		// FiColList fiTableCols = (FiColList) getListFormElements();
		// setFormElementsMap(fiTableCols.getMapCols());
		// } else {
		// Map<String, FiCol> formMap = getFormMap();
		// setFormElementsMap(formMap);
		// }

		// Form Tiplerine Göre Form Oluşturma metodlarına Yönlendirme
		if (getFormTypeSelected() == FormType.PlainFormV1) {
			initPlainFormV1();
			// formEntity varsa, alanlara değerler set edilir
			if (getFormEntity() != null) {
				FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getFormEntity());
			}
			// Form değerleri eklendikten sonra trigger edilecek metodlar
			trigEventsAfterFormLoaded();
			return;
		}

		Loghelper.get(getClass()).debug("Null Form Type");
	}

	private Map<String, FiCol> getFormMap() {
		return FiCollection.listToMapSingle(getListFormElements(), FiCol::getFieldName);
	}

	private void trigEventsAfterFormLoaded() {

		getListFormElements().forEach(fiTableCol -> {
			if (fiTableCol.getFnEditorNodeRendererAfterFormLoad() != null) {
				fiTableCol.getFnEditorNodeRendererAfterFormLoad().accept(getFormEntity(), fiTableCol.getColEditorNode());
			}
		});

		getListFormElements().forEach(fiTableCol -> {
			if (fiTableCol.getFnEditorNodeRendererAfterFormLoad2() != null) {
				fiTableCol.getFnEditorNodeRendererAfterFormLoad2().accept(getFormEntity(), fiTableCol.getColEditorNode(), getListFormElements());
			}
		});

	}

	// Helper Setups
	public void setupWitDefaultFormType(List<FiCol> listFormElements) {
		setListFormElements(listFormElements);
		setFormTypeSelected(FormType.PlainFormV1);
		initFormElementsMain();
	}

	public void setup1(List<FiCol> listFormElements, FormType formType) {
		setListFormElements(listFormElements);
		setFormTypeSelected(formType);
		initFormElementsMain();
	}

	private void initPlainFormV1() {

		List<FiCol> listFormElements = getListFormElements();

		//Loghelperr.debug(getClass(), "Plain Form By Editor");

		// fxform migpane daha önceden doldurulmuşsa, çıkarılıp temizlenir
		if (getChildren().size() > 0) {
			getChildren().remove(0, getChildren().size() - 1);
		}

		for (FiCol fiCol : listFormElements) {

			//Loghelper.get(getClass()).debug("FiCol in Form" + fiCol.getFieldName());

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
				add(fxLabelComment, "pushx,growx,span");
				continue;
			}

			// Label oluşturulur
			Label lblForm = new Label(fiCol.getHeaderName());
			add(lblForm, "width 30%,wmax 150,wmin 120");

			Object entityForNode = getFormEntity();

			// Editor comp (node) oluşturulur, lifecycle metodları çalıştırılır
			Node node = FxEditorFactory.generateEditorNodeFullLifeCycle(fiCol, entityForNode);

			if (FiBoolean.isTrue(getBoReadOnlyFormNtn()) || FiBoolean.isFalse(fiCol.getBoEditable())
			|| FiBoolean.isTrue(fiCol.getBoNonEditableForForm())) {
				node.setDisable(true);
			}

			// formEntity yüklenirse eğer (update formu ise) nonupdatable alanlar disable yapılır
			if (getFormEntity() != null && FiBoolean.isTrue(fiCol.getBoNonUpdatable())) {
				node.setDisable(true);
			}

			if (getBoUpdateFormNtn() && FiBoolean.isTrue(fiCol.getBoNonUpdatable())) {
				node.setDisable(true);
			}

			if (fiCol.getPrefSize() != null) {
				add(node, String.format("width %s,wrap", fiCol.getPrefSize().toString()));
			} else {
				add(node, FxMigHp.bcc("growx,pushx,wrap").addCcCompMaxWidthSizeByColType(fiCol).getCcInit());
			}

		} // tblCol for döngüsü sonu

	}

	public Object getEntityByFilterNode(Class clazz) {
		return FxEditorFactory.bindFormToEntityByFilterNode(getListFormElements(), clazz);
	}

	public FormType getFormTypeSelected() {
		if (formType == null) {
			formType = FormType.PlainFormV1;
		}
		return formType;
	}

	public FormType getFormType() {
		return formType;
	}

	public void setAutoClass() {
		if (getEntityClazz() == null) {
			this.entityClazz = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
	}

	public void setFormTypeSelected(FormType formTypeSelected) {
		this.formType = formTypeSelected;
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
		this.listFormElements = listFormElements;
	}

//	public void setFxFormSetup(FxFormConfig<EntClazz> fxFormConfig) {
//		this.fxFormConfig = fxFormConfig;
//	}

	public Boolean getBoUpdateForm() {
		return boUpdateForm;
	}

	public Boolean getBoUpdateFormNtn() {
		if (boUpdateForm == null) return false;
		return boUpdateForm;
	}

	public void setBoUpdateForm(Boolean boUpdateForm) {
		this.boUpdateForm = boUpdateForm;
	}

	public Boolean getBoFormInitialized() {
		return boFormInitialized;
	}

	public void setBoFormInitialized(Boolean boFormInitialized) {
		this.boFormInitialized = boFormInitialized;
	}

	public void bindEntityToForm(EntClazz formEntity) {
		setFormEntity(formEntity);
		FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getFormEntity());
		trigEventsAfterFormLoaded();
	}

	public EntClazz getFormEntity() {
		return formEntity;
	}

	public void setFormEntity(EntClazz formEntity) {
		this.formEntity = formEntity;
	}

	public void setFormEntityForEdit(EntClazz formEntity) {
		setBoUpdateForm(true);
		setFormEntity(formEntity);
	}

	public void formFocusListener(ChangeListener<Boolean> fnFocusedChangeListener) {
		if (getFnFocusedChangeListener() != null) {
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

	public Boolean getBoReadOnlyFormNtn() {
		if (boReadOnlyForm == null) {
			return false;
		}
		return boReadOnlyForm;
	}

	public Boolean getValueAsBoolean(FiCol entBoAnacariBirlestir) {

		FiCol fiCol = getFiCol(entBoAnacariBirlestir);

		if(fiCol!=null){
			if(fiCol.getColType().equals(OzColType.Boolean)){
				Boolean value = (Boolean) fiCol.getColValue();
				return value;
			}
		}

		return null;
	}

	private FiCol getFiCol(FiCol entBoAnacariBirlestir) {
		FiCol fiColFound = null;
		for (FiCol fiCol : getListFormElements()) {
			if (fiCol.getFieldName().equals(entBoAnacariBirlestir.getFieldName())) {
				fiColFound = fiCol;
			}
		}
		return fiColFound;
	}

	//	public void setupForm(List<FiCol> listFormElements, FormType formType) {
//		FxFormConfig fxFormConfig = new FxFormConfig();
//		fxFormConfig.setListFormElements(listFormElements);
//		fxFormConfig.setFormType(formType);
//		setFxFormSetup(fxFormConfig);
//		initFormElementsMain();
//	}

}


