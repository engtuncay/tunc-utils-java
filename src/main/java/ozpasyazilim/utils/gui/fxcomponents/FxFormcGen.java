package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiComp;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColsUtil;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * FxFormc -> FxForm with FxFormConfig
 * <p>
 * Form config bilgileri, FxFormConfig objesinde tutulur.
 *
 * @param <EntClazz>
 */
public class FxFormcGen<EntClazz> extends FxMigPaneGenView<EntClazz> implements IFiComp {

    private Class<EntClazz> entityClazz;
    private String uid;
    private FxFormConfig<EntClazz> fxFormConfig; // added 27-01-21
    private Boolean boFormInitialized;

    /**
     *
     */
    private ChangeListener<Boolean> fnFocusedChangeListener;

    //private Map<String, FiCol> formElementsMap;

    public FxFormcGen() {
        super("insets 0");
    }

    public FxFormcGen(Class<EntClazz> entityClazz) {
        super("insets 0");
        this.entityClazz = entityClazz;
    }

    /**
     * FormConfig oluşturularak listFormElements kaydedilir.
     *
     * @param listFormElements
     */
    public FxFormcGen(List<FiCol> listFormElements) {
        super("insets 0");
        getFxFormConfigInit().setListFormElements(listFormElements);
    }

    public FxFormcGen(List<FiCol> listFormElements, Boolean boInit) {
        super("insets 0");
        if (FiBool.isTrue(boInit)) {
            initContWitDefaultForm(listFormElements);
        }
    }

    public FxFormcGen(FxFormConfig<EntClazz> fxFormConfig) {
        initContWitConfig(fxFormConfig);
    }

    public void initContWitConfig(FxFormConfig fxFormConfig) {
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    // ********************* Main

    public void initCont() {
        // form initialized edildiğini belirtir
        setBoFormInitialized(true);

        // Form Elemanları Kontrolü
        if (FiCollection.isEmpty(getFxFormConfigInit().getListFormElements())) {
            add(new FxLabel("Form Elemanları Tanımlanmamış !!!"), "span,pushx,growx");
            Loghelper.get(getClass()).debug("Form Elemanları Tanımlanmamış");
            return;
        }

        // default form type specified
        if (getFormTypeSelected() == null) setFormTypeSelected(FormType.PlainFormV1);

        // Form Tiplerine Göre Form Oluşturma metodlarına Yönlendirme

        if (getFormTypeSelected() == FormType.PlainFormV1) initPlainFormV1();

        if (getFormEntity() != null) {
            FxEditorFactory.bindEntityToFormByEditorValue(getListFormElementsInit(), getFormEntity());
        }

        // Form Değerleri Yüklendikten sonraki Lifecycle metodu çalıştırılır
        trigEventsAfterLoadFormValue();

        //Loghelper.get(getClass()).debug("Null Form Type");
    }


    private void initPlainFormV1() {

        List<FiCol> listFormElements = getListFormElementsInit();

        if (FiCollection.isEmpty(listFormElements)) return;

        //Loghelperr.debug(getClass(), "Plain Form By Editor");

        // fxform migpane daha önceden doldurulmuşsa, çıkarılıp temizlenir
        if (!getChildren().isEmpty()) {
            getChildren().remove(0, getChildren().size() - 1);
        }

        for (FiCol fiCol : listFormElements) {
            //Loghelper.get(getClass()).debug("FiCol in Form" + fiCol.getFieldName());

            if (FiBool.isTrue(fiCol.getBoHidden())) {
                continue;
            }

            FxEditorFactory.setAutoColEditorClassByColType(fiCol);

            if (FiString.isEmpty(fiCol.getColEditorClass())) {
                fiCol.setColEditorClass(FxTextField.class.getName());
            }

            // Tek satır label göstermek için
            if (fiCol.getColEditorClass().equals(EnumColNodeType.FxLabelRowComment.toString())) {
                FxLabel fxLabelComment = new FxLabel(fiCol.getOfcTxHeader());
                add(fxLabelComment, "span,pushx,growx");
                continue;
            }

            Label lblForm = new Label(fiCol.getOfcTxHeader());
            add(lblForm, "width 30%,wmax 150,wmin 120");

            // Editor comp (node) oluşturulur
            Node node = FxEditorFactory.generateEditorNodeFullLifeCycle(fiCol, getFormEntity());

            if (FiBool.isTrue(getFxFormConfigInit().getBoReadOnlyForm()) || FiBool.isFalse(fiCol.getBoEditable())) {
                node.setDisable(true);
            }

            // getFormEntityForEdit() yerin getFormEntity getirildi 213010
            if (getFormEntity() != null && FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                node.setDisable(true);
            }

            if (fiCol.getPrefSize() != null) {
                add(node, String.format("width %s,wrap", fiCol.getPrefSize().toString()));
            } else {
                add(node, FxMigHp.bcc("growx,pushx,wrap").addCcCompMaxWidthSizeByColTypeForFxForm(fiCol).genCc());
            }

        } // tblCol for döngüsü sonu

    }

    /**
     * !!! Form içinden kullanıcının girdiği değerler buradan alınmaz. getFormAsFiKeybean den alınmalı
     *
     * @return
     */
    public FiColsUtil getFiColsUtil() {
        return FiColsUtil.bui(getListFormElementsInit());
    }

    public List<FiCol> getListFormElementsInit() {
        return getFxFormConfigInit().getListFormElementsInit();
    }

    public Node getNode(Object fieldName) {
        return FxEditorFactory.getEditorNodeByFieldName(getListFormElementsInit(), fieldName.toString());
    }

    public <T> T getFormAsObject(Class<T> clazz) {
        return FxEditorFactory.bindFormToEntityByEditorNode(getListFormElementsInit(), clazz);
    }

    public void getFormAsEntity(EntClazz formMikroKodDegistir) {
        FxEditorFactory.bindEntityToFormByEditorValue(getListFormElementsInit(), formMikroKodDegistir);
    }

    public FiKeyBean getFormAsFkb() {
        return FxEditorFactory.bindFormToFiKeyBeanByEditorNode(getListFormElementsInit());
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
        return FiCollection.listToMapSingle(getListFormElementsInit(), FiCol::getOfcTxFieldName);
    }

    private void trigEventsAfterLoadFormValue() {

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
    public void initContWitDefaultForm(List<FiCol> listFormElements) {
        FxFormConfig fxFormConfig = new FxFormConfig();
        fxFormConfig.setListFormElements(listFormElements);
        fxFormConfig.setFormType(FormType.PlainFormV1);
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    public void initContWitFormType(List<FiCol> listFormElements, FormType formType) {
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
        trigEventsAfterLoadFormValue();
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