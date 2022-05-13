package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;

public class FxFormConfig<EntClazz> {

	private List<FiCol> listFormElements;
	private FormType formType;
	// form yüklenecek formEntity - yerine FormEntity kullanılacak
	//	@Deprecated
	//	private EntClazz formInitEntityEdit;
	//	@Deprecated
	//	private EntClazz formInitEntityInsert;

	private EntClazz formEntity; // 21-10-30 eklendi

	// Form güncellemek amacıyla mı açıldı
	private Boolean boUpdateForm; // 21-10-30 eklendi

	private Boolean boReadOnlyForm;

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
}
