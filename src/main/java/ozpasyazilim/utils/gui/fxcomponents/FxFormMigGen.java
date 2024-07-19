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
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColsUtil;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * FxFormMigGen Ayar Alanları direk FxFormMigGen sınıfında.
 * <p>
 * initCont ile component'ler initialize edilir.
 * <p>
 * ana kullanılacak form componenti !!!
 *
 * @param <EntClazz> Form alanın değerlerinin aktarılacağı veya alınacağı sınıf
 */
public class FxFormMigGen<EntClazz> extends FxMigPaneGenView<EntClazz> {

    private Class<EntClazz> entityClazz;
    private String txGuid;
    private Boolean boFormInitialized;
    private ChangeListener<Boolean> fnFocusedChangeListener;
    private List<FiCol> listFormElements;
    private FormType formType;
    private EntClazz refFormEntity; // 30-10-21 eklendi

    /**
     * Form güncellemek amacıyla açıldığını belirtir
     */
    private Boolean boUpdateForm; // 21-10-30 eklendi

    private Boolean boReadOnlyForm;

    private Function<FxFormMigGen, Fdr> fnValidateForm;

    // Metods

    public FxFormMigGen() {
        super("insets 0");
    }

    public FxFormMigGen(Class<EntClazz> entityClazz) {
        super("insets 0");
        this.entityClazz = entityClazz;
    }

    /**
     * @param listFormElements
     */
    public FxFormMigGen(List<FiCol> listFormElements) {
        super("insets 0");
        this.listFormElements = listFormElements;
    }

    public FxFormMigGen(List<FiCol> colsForm, FormType formType) {
        super("insets 0");
        this.listFormElements = colsForm;
        this.formType = formType;
    }

    // ********************* Main

    public void initCont() {

        setBoFormInitialized(true);

        if (FiCollection.isEmpty(getListFormElements())) {
            add(new FxLabel("Form Elemanları Tanımlanmamış !!!"), "span,pushx,growx");
            Loghelper.get(getClass()).debug("Form Elemanları Tanımlanmamış");
            return;
        }

        // Form Tiplerine Göre Form Oluşturma metodlarına Yönlendirme

        if (getFormTypeSelectedInit() == FormType.PlainFormV1) {
            initPlainFormV1();
            // formEntity varsa, alanlara değerler set edilir
            if (getRefFormEntity() != null) {
                FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getRefFormEntity());
            }
            // Form değerleri eklendikten sonra trigger edilecek metodlar
            trigEventsAfterFormLoaded();
            return;
        }

        Loghelper.get(getClass()).error("Null Form Type");
    }

    public FiColsUtil getFiColsUtil() {
        return FiColsUtil.bui(getListFormElementsInit());
    }

    public Node getCompByFieldName(String txFieldName) {
        return getFiColsUtil().findColumnByFieldName(txFieldName).getColEditorNode();
    }

    public FiCol getFormElementByFieldName(String txFieldName) {
        return getFiColsUtil().findColumnByFieldName(txFieldName);
    }

    public List<FiCol> getListFormElementsInit() {
        if (listFormElements == null) {
            listFormElements = new ArrayList<>();
        }
        return listFormElements;
    }

    public List<FiCol> getListFormElements() {
        return listFormElements;
    }

    public FxDatePicker getEditorCompAsFxDatePicker(String fieldName) {
        return getFiColsUtil().getEditorCompAsFxDatePicker(fieldName);
    }

    public Node getNode(Object fieldName) {
        return FxEditorFactory.getEditorNodeByFieldName(getListFormElements(), fieldName.toString());
    }


    public void bindEntitytoForm(EntClazz formMikroKodDegistir) {
        FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), formMikroKodDegistir);
    }

    /**
     * get Form As FiKeyBean
     *
     * @return
     */
    public FiKeyBean getFormAsFkb() {
        return FxEditorFactory.bindFormToFiKeyBeanByEditorNodeForFiCol(getListFormElements());
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

    //    public <T> T bindFormToEntity(Class<T> clazz) {
//        return FxEditorFactory.bindFormToEntityByEditorNode(getListFormElements(), clazz);
//    }


    public void clearFormFields() {
        FxEditorFactory.clearValuesOfFormFields(getListFormElements());
    }

    public void initFormHp1(List<FiCol> listFormElements, FormType formType, EntClazz formEntity) {
        setListFormElements(listFormElements);
        setFormTypeSelected(formType);
        setRefFormEntity(formEntity);
        initCont();
    }

    public void initWitFormConfig(FxFormConfig<EntClazz> fxFormConfig) {
        setFxFormConfig(fxFormConfig);
        initCont();
    }

    private void setFxFormConfig(FxFormConfig<EntClazz> fxFormConfig) {
        setListFormElements(fxFormConfig.getListFormElementsInit());
        setFormTypeSelected(fxFormConfig.getFormType());
        setRefFormEntity(fxFormConfig.getFormEntity());
        setBoUpdateForm(fxFormConfig.getBoUpdateForm());
        setFnValidateForm(fxFormConfig.getFnValidateFormForFormMigGen());
    }


    private Map<String, FiCol> getFormAsMapFieldNameToFiCol() {
        return FiCollection.listToMapSingle(getListFormElements(), FiCol::getFieldName);
    }

    /**
     * trigger Events After Form Loaded
     * <p>
     * getFnEditorNodeRendererAfterFormLoad(),getFnEditorNodeRendererAfterFormLoad2() metods
     */
    private void trigEventsAfterFormLoaded() {

        getListFormElements().forEach(fiCol -> {
            if (fiCol.getFnEditorNodeRendererAfterFormLoad() != null) {
                fiCol.getFnEditorNodeRendererAfterFormLoad().accept(getRefFormEntity(), fiCol.getColEditorNode());
            }
        });

        getListFormElements().forEach(fiCol -> {
            if (fiCol.getFnEditorNodeRendererAfterFormLoad2() != null) {
                fiCol.getFnEditorNodeRendererAfterFormLoad2().accept(getRefFormEntity(), fiCol.getColEditorNode(), getListFormElements());
            }
        });

    }

    // Helper Setups
    public void setupWitDefaultFormType(List<FiCol> listFormElements) {
        setListFormElements(listFormElements);
        setFormTypeSelected(getDefaultFormType());
        initCont();
    }

    private static FormType getDefaultFormType() {
        return FormType.PlainFormV1;
    }

    /**
     * setupFormElementsHp2 eski adı
     *
     * @param listFormElements
     * @param formType
     */
    public void setup1(List<FiCol> listFormElements, FormType formType) {
        setListFormElements(listFormElements);
        setFormTypeSelected(formType);
        initCont();
    }

    private void initPlainFormV1() {

        List<FiCol> listFormElements = getListFormElements();

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
                FxLabel fxLabelComment = new FxLabel(fiCol.getHeaderName());
                add(fxLabelComment, "pushx,growx,span");
                continue;
            }

            // Label oluşturulur
            Label lblForm = new Label(fiCol.getHeaderName());
            add(lblForm, "width 30%,wmax 150,wmin 120");

            Object entityForNode = getRefFormEntity();

            // Editor comp (node) oluşturulur, lifecycle metodları çalıştırılır
            Node node = FxEditorFactory.generateEditorNodeFullLifeCycle(fiCol, entityForNode);

            if (FiBool.isTrue(getBoReadOnlyFormNtn()) || FiBool.isFalse(fiCol.getBoEditable())
                    || FiBool.isTrue(fiCol.getBoNonEditableForForm())) {
                node.setDisable(true);
            }

            // formEntity yüklenirse eğer (update formu ise) nonupdatable alanlar disable yapılır
            if (getRefFormEntity() != null && FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                node.setDisable(true);
            }

            if (getBoUpdateFormNtn() && FiBool.isTrue(fiCol.getBoNonUpdatable())) {
                node.setDisable(true);
            }

            if (fiCol.getPrefSize() != null) {
                add(node, String.format("width %s,wrap", fiCol.getPrefSize().toString()));
            } else {
                add(node, FxMigHp.bcc("growx,pushx,wrap").addCcCompMaxWidthSizeByColTypeForFxForm(fiCol).getCcInit());
            }

        } // tblCol for döngüsü sonu


        // bütün form elemanları oluşturulduktan sonra çalıştırılacak eventlar
        lifeCycleAfterAllFormLoad();

    }

    private void lifeCycleAfterAllFormLoad() {

        for (FiCol fiCol : getListFormElementsInit()) {
            if (fiCol.getFnEditorNodeLfcAfterAllFormLoad()!=null) {
                fiCol.getFnEditorNodeLfcAfterAllFormLoad().accept(fiCol);
            }
        }
    }

    public Object getEntityByFilterNode(Class clazz) {
        return FxEditorFactory.bindFormToEntityByFilterNode(getListFormElements(), clazz);
    }

    public FormType getFormTypeSelectedInit() {
        if (formType == null) {
            formType = getDefaultFormType();
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

    public String getTxGuid() {
        return txGuid;
    }

    public void setTxGuid(String txGuid) {
        this.txGuid = txGuid;
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
        setRefFormEntity(formEntity);
        FxEditorFactory.bindEntityToFormByEditorValue(getListFormElements(), getRefFormEntity());
        trigEventsAfterFormLoaded();
    }

    public EntClazz getRefFormEntity() {
        return refFormEntity;
    }

    public void setRefFormEntity(EntClazz refFormEntity) {
        this.refFormEntity = refFormEntity;
    }

    public void setFormEntityForEdit(EntClazz formEntity) {
        setBoUpdateForm(true);
        setRefFormEntity(formEntity);
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

        if (fiCol != null) {
            if (fiCol.getColType().equals(OzColType.Boolean)) {
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

    public static void setFieldsAllToRequiredFi(List<? extends IFiCol> listColumn) {
        listColumn.forEach(o -> {
            o.setBoRequired(true);
        });
    }

    public Function<FxFormMigGen, Fdr> getFnValidateForm() {
        return fnValidateForm;
    }

    public void setFnValidateForm(Function<FxFormMigGen, Fdr> fnValidateForm) {
        this.fnValidateForm = fnValidateForm;
    }
}