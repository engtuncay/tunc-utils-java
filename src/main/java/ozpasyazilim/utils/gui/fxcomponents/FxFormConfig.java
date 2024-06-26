package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FxFormConfig<EntClazz> {

    private List<FiCol> listFormElements;
    private FormType formType;
    private EntClazz formEntity; // 21-10-30 eklendi

    /**
     * Form güncellemek amacıyla açıldığını belirtir
     */
    private Boolean boUpdateForm; // 21-10-30 eklendi

    private Boolean boReadOnlyForm;

    private Function<FxFormcGen, Fdr> fnValidateFormForFormc;

    private Function<FxFormMigGen, Fdr> fnValidateFormForFormMigGen;

    public FxFormConfig() {
    }

    public FxFormConfig(List<FiCol> fiCols) {
        this.listFormElements = fiCols;
    }

    public List<FiCol> getListFormElements() {
        return listFormElements;
    }

    public List<FiCol> getListFormElementsInit() {
        if (listFormElements == null) {
            listFormElements = new ArrayList<>();
        }
        return listFormElements;
    }

    public void setListFormElements(List<FiCol> listFormElements) {
        this.listFormElements = listFormElements;
    }

    public FormType getFormType() {
        return formType;
    }

    public FormType getFormTypeOrDef() {
        if (formType == null) {
            return FormType.PlainFormV1;
        }
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    public Boolean getBoUpdateForm() {
        return boUpdateForm;
    }

    public void setBoUpdateForm(Boolean boUpdateForm) {
        this.boUpdateForm = boUpdateForm;
    }

    public EntClazz getFormEntity() {
        return formEntity;
    }

    public void setFormEntity(EntClazz formEntity) {
        this.formEntity = formEntity;
    }

    public Boolean getBoUpdateFormInit() {
        if (boUpdateForm == null) return false;
        return boUpdateForm;
    }

    public Boolean getBoReadOnlyForm() {
        return boReadOnlyForm;
    }

    public void setBoReadOnlyForm(Boolean boReadOnlyForm) {
        this.boReadOnlyForm = boReadOnlyForm;
    }

    public Function<FxFormcGen, Fdr> getFnValidateFormForFormc() {
        return fnValidateFormForFormc;
    }

    public void setFnValidateFormForFormc(Function<FxFormcGen, Fdr> fnValidateFormForFormc) {
        this.fnValidateFormForFormc = fnValidateFormForFormc;
    }

    public Function<FxFormMigGen, Fdr> getFnValidateFormForFormMigGen() {
        return fnValidateFormForFormMigGen;
    }

    public void setFnValidateFormForFormMigGen(Function<FxFormMigGen, Fdr> fnValidateFormForFormMigGen) {
        this.fnValidateFormForFormMigGen = fnValidateFormForFormMigGen;
    }

}
