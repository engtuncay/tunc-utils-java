package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FxFormConfig<EntClazz> {

  private List<FiCol> listFormElements;
  private FormType formType;
  private EntClazz formEntity; // 21-10-30 eklendi
  private FiKeybean fkbEntity; // 26-04-29
  private Function<FxFormMigGen,Fdr> fnFormSave;

  /**
   * Form güncellemek amacıyla açıldığını belirtir
   */
  private Boolean boUpdateForm; // 21-10-30 eklendi

  private Boolean boReadOnlyForm;

  private Function<FxFormMigGen, Fdr> fnValidateForm;

  // Miggen için yapılmış
  private Function<FxFormMigaGen, Fdr> fnValidateFormForFormMigaGen;

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

  public Function<FxFormMigGen, Fdr> getFnValidateForm() {
    return fnValidateForm;
  }

  public void setFnValidateForm(Function<FxFormMigGen, Fdr> fnValidateForm) {
    this.fnValidateForm = fnValidateForm;
  }

  public Function<FxFormMigaGen, Fdr> getFnValidateFormForFormMigaGen() {
    return fnValidateFormForFormMigaGen;
  }

  public void setFnValidateFormForFormMigaGen(Function<FxFormMigaGen, Fdr> fnValidateFormForFormMigaGen) {
    this.fnValidateFormForFormMigaGen = fnValidateFormForFormMigaGen;
  }

  public Function<FxFormMigGen, Fdr> getFnFormSave() {
    return fnFormSave;
  }

  public void setFnFormSave(Function<FxFormMigGen, Fdr> fnFormSave) {
    this.fnFormSave = fnFormSave;
  }

  public FiKeybean getFkbEntity() {
    return fkbEntity;
  }

  public void setFkbEntity(FiKeybean fkbEntity) {
    this.fkbEntity = fkbEntity;
  }
}
