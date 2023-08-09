package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.mvc.IFiComp;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.IFiColHelper;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * c -> config'li FxForm
 * <p>
 * Form config bilgileri, FxFormConfig objesinde tutulur.
 *
 * @param <EntClazz>
 */
public class FxFormc<EntClazz> extends FxMigPaneGenView<EntClazz> implements IFiComp {
    private Class<EntClazz> entityClazz;
    private String uid;
    private FxFormConfig<EntClazz> fxFormConfig; // added 27-01-21
    private Boolean boFormInitialized;
    /**
     *
     */
    private ChangeListener<Boolean> fnFocusedChangeListener;

    //private Map<String, FiCol> formElementsMap;

    public FxFormc() {
        super("insets 0");
    }

    public FxFormc(Class<EntClazz> entityClazz) {
        super("insets 0");
        this.entityClazz = entityClazz;
    }

    /**
     * FormConfig oluşturularak listFormElements kaydedilir.
     *
     * @param listFormElements
     */
    public FxFormc(List<FiCol> listFormElements) {
        super("insets 0");
        getFxFormConfigInit().setListFormElements(listFormElements);
    }

    public FxFormc(List<FiCol> listFormElements, Boolean boInit) {
        super("insets 0");
        if (FiBoolean.isTrue(boInit)) {
            initDefaultForm(listFormElements);
        }
    }

    public FxFormc(FxFormConfig<EntClazz> fxFormConfig) {
        initForm(fxFormConfig);
    }


    // ********************* Main
    public void initCont() {
        // form initialized edildiğini belirtir
        setBoFormInitialized(true);

        // Form Elemanları Kontrolü
        if (getFxFormConfig() == null) {
            Loghelper.get(getClass()).debug("Form Config Tanımlanmamış");
            return;
        } else if (FiCollection.isEmpty(getFxFormConfig().getListFormElements())) {
            add(new FxLabel("Form Elemanları Yüklenmemiş !!!"), "span,pushx,growx");
            Loghelper.get(getClass()).debug("Form Alanları Tanımlanmamış.");
            return;
        }

        // default form type specified
        if (getFormTypeSelected() == null) setFormTypeSelected(FormType.PlainFormV1);

        // Form Tiplerine Göre Form Oluşturma metodlarına Yönlendirme

        if (getFormTypeSelected() == FormType.PlainFormV1) initPlainFormV1();

        if (getFormEntity() != null)
            FxEditorFactory.bindEntityToFormByEditorValue(getListFormElementsInit(), getFormEntity());

        // Form Değerleri Yüklendikten sonraki Lifecycle metodu çalıştırılır
        trigEventAfterLoadFormValue();

        //Loghelper.get(getClass()).debug("Null Form Type");
    }

    public void initForm(FxFormConfig fxFormConfig) {
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    private void initPlainFormV1() {

//        if (!FiBoolean.isTrue(getBoFormInitialized())) {
//            initCont();
//            return;
//        }

        List<FiCol> listFormElements = getListFormElementsInit();

        if (FiCollection.isEmpty(listFormElements)) return;

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
                add(fxLabelComment, "span,pushx,growx");
                continue;
            }

            Label lblForm = new Label(fiCol.getHeaderName());
            Loghelper.get(getClass()).debug("lblForm yükleniyor.");
            add(lblForm, "width 30%,wmax 150,wmin 120");

            // Editor comp (node) oluşturulur
            Node node = FxEditorFactory.generateEditorNodeFullLifeCycle(fiCol, getFormEntity());

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
                add(node, FxMigHp.bcc("growx,pushx,wrap").addCcCompMaxWidthSizeByColTypeForFxForm(fiCol).genCc());
            }

        } // tblCol for döngüsü sonu

    }

    public Node getCompByFieldName(String toString) {
        // URREV map üzerinden yapılabilir
        return IFiColHelper.build(getListFormElementsInit()).findColumnByFieldName(toString).getColEditorNode();
    }

    public IFiCol getColByFieldName(String toString) {
        return IFiColHelper.build(getListFormElementsInit()).findColumnByFieldName(toString);
    }

    public List<FiCol> getListFormElementsInit() {
        return getFxFormConfigInit().getListFormElementsInit();
    }

    public FxDatePicker getEditorCompFxDatePicker(String fieldName) {

        IFiCol ozTableCol = IFiColHelper.build(getListFormElementsInit()).getFiTableColByID(fieldName);

        if (ozTableCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = (FxDatePicker) ozTableCol.getColEditorNode();
            return comp;
        }

        return null;
    }

    public Node getNode(Object fieldName) {
        return FxEditorFactory.getEditorNodeByFieldName(getListFormElementsInit(), fieldName.toString());
    }

    public <T> T getFormAsObject(Class<T> clazz) {
        return FxEditorFactory.bindFormToEntityByEditorNode(getListFormElementsInit(), clazz);
    }

    public void bindEntitytoForm(EntClazz formMikroKodDegistir) {
        FxEditorFactory.bindEntityToFormByEditorValue(getListFormElementsInit(), formMikroKodDegistir);
    }

    public FiKeyBean getFormAsFiMapParams() {
        return FxEditorFactory.bindFormToKeyBeanByEditorNode(getListFormElementsInit());
    }

    public FiKeyBean getFormAsFiKeyBean() {
        return FxEditorFactory.bindFormToKeyBeanByEditorNode(getListFormElementsInit());
    }

    public List<FiCol> getListFiColWithFormValue() {
        FxEditorFactory.bindFormValueToFiColListByEditor(getListFormElementsInit());
        return getListFormElementsInit();
    }

    public EntClazz bindFormToEntity() {
        if (getEntityClazz() == null) setAutoClass();
        EntClazz formEntity = FxEditorFactory.bindFormToEntityByEditorNode(getListFormElementsInit(), getEntityClazz());
        return formEntity;
    }

    public void clearFormFields() {
        FxEditorFactory.clearValuesOfFormFields(getListFormElementsInit());
    }

    public void setFormElementsHp1(List<FiCol> listFormElements, FormType formType, EntClazz formEntityForEdit, EntClazz formEntityForInsert) {

        FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
        fxFormConfig.setFormType(formType);
        fxFormConfig.setListFormElements(listFormElements);
        if (formEntityForEdit != null) fxFormConfig.setFormEntity(formEntityForEdit);
        if (formEntityForInsert != null) fxFormConfig.setFormEntity(formEntityForInsert);
        setFxFormConfig(fxFormConfig);

        initCont();
    }

    public void setupFormElementsHp3(List<FiCol> listFormElements, FormType formType, EntClazz formEntity) {
        FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
        fxFormConfig.setFormType(formType);
        fxFormConfig.setListFormElements(listFormElements);
        fxFormConfig.setFormEntity(formEntity);
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    public void setupFormElementsHp2(List<FiCol> listFormElements, FormType formType) {
        FxFormConfig<EntClazz> fxFormConfig = new FxFormConfig();
        fxFormConfig.setFormType(formType);
        fxFormConfig.setListFormElements(listFormElements);
        setFxFormConfig(fxFormConfig);

        initCont();
    }


    private Map<String, FiCol> getFormMap() {
        return FiCollection.listToMapSingle(getListFormElementsInit(), FiCol::getFieldName);
    }

    private void trigEventAfterLoadFormValue() {

        getListFormElementsInit().forEach(fiTableCol -> {
            if (fiTableCol.getFnEditorNodeRendererAfterFormLoad() != null) {
                fiTableCol.getFnEditorNodeRendererAfterFormLoad().accept(getFormEntity(), fiTableCol.getColEditorNode());
            }
        });

        getListFormElementsInit().forEach(fiTableCol -> {
            if (fiTableCol.getFnEditorNodeRendererAfterFormLoad2() != null) {
                fiTableCol.getFnEditorNodeRendererAfterFormLoad2().accept(getFormEntity(), fiTableCol.getColEditorNode(), getListFormElementsInit());
            }
        });

    }

    // Helper Setups
    public void initDefaultForm(List<FiCol> listFormElements) {
        FxFormConfig fxFormConfig = new FxFormConfig();
        fxFormConfig.setListFormElements(listFormElements);
        fxFormConfig.setFormType(FormType.PlainFormV1);
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    public void initForm(List<FiCol> listFormElements, FormType formType) {
        FxFormConfig fxFormConfig = new FxFormConfig();
        fxFormConfig.setListFormElements(listFormElements);
        fxFormConfig.setFormType(formType);
        setFxFormConfig(fxFormConfig);
        initCont();
    }




    public Object getEntityByFilterNode(Class clazz) {
        return FxEditorFactory.bindFormToEntityByFilterNode(getListFormElementsInit(), clazz);
    }

    public FormType getFormTypeSelected() {
        return getFxFormConfigInit().getFormTypeOrDef();
    }

    public void setAutoClass() {
        if (getEntityClazz() == null) {
            this.entityClazz = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    public void setFormTypeSelected(FormType formTypeSelected) {
        getFxFormConfigInit().setFormType(formTypeSelected);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

//	public void setFormElementsMap(Map<String, FiCol> formElementsMap) {
//		this.formElementsMap = formElementsMap;
//	}

    public FxFormConfig<EntClazz> getFxFormConfigInit() {
        if (fxFormConfig == null) {
            fxFormConfig = new FxFormConfig<>();
        }
        return fxFormConfig;
    }

    public FxFormConfig<EntClazz> getFxFormConfig() {
        return fxFormConfig;
    }

    public void setFxFormConfig(FxFormConfig<EntClazz> fxFormConfig) {
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

        FxEditorFactory.bindEntityToFormByEditorValue(getListFormElementsInit(), getFormEntity());
        trigEventAfterLoadFormValue();
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

    public void formFocusListener(ChangeListener<Boolean> fnFocusedChangeListener) {
        if (getFnFocusedChangeListener() != null) {
            for (FiCol listFormElement : getListFormElementsInit()) {
                listFormElement.getColEditorNode().focusedProperty().removeListener(getFnFocusedChangeListener());
            }
        }

        setFnFocusedChangeListener(fnFocusedChangeListener);
        for (FiCol listFormElement : getListFormElementsInit()) {
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

    public void setupForm(List<FiCol> listFormElements, FormType formType) {
        FxFormConfig fxFormConfig = new FxFormConfig();
        fxFormConfig.setListFormElements(listFormElements);
        fxFormConfig.setFormType(formType);
        setFxFormConfig(fxFormConfig);
        initCont();
    }


}


