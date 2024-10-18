package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.gui.fxcomponents.FxEditorFactory;
import ozpasyazilim.utils.gui.fxcomponents.FxTableView2;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.*;

import java.util.List;

/**
 * çok fazla karmaşık olmuş formEntity fazlalık, amacı netleşmeli
 *
 * @param <FormClazz>
 * @param <EntClazz>
 */
public abstract class FxGenWindowContWindow<FormClazz, EntClazz> extends AbsFiModBaseCont implements IFiModCont {

	FxGenWindowView modView;
	FormClazz formEntity;
	EntClazz selectedEntity;

	public FxGenWindowContWindow() {
		//super.moduleAciklama = "Mos-Shared Form Window";
		//super.moduleCode = EntegreModules.ModContGen.getModuleCode();
	}

	@Override
	public void initCont() {
		modView = new FxGenWindowView();
		modView.initGui();
		//afterInit();
	}

	public FxGenWindowContWindow<FormClazz, EntClazz> buildInit() {
		initCont();
		return this;
	}

	public FormClazz getFormEntity() {
		return formEntity;
	}

	public void setFormEntity(FormClazz formEntity) {
		this.formEntity = formEntity;
	}

	public EntClazz getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(EntClazz selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public FormClazz bindFormToEntity(Class<FormClazz> clazz) {
		if (clazz == null) return null;
		//FormClazz formEntity = new FxEditorFactory().bindFormToEntityByFactoryClass(getModView().getFxFormMig().getListFormElements(), clazz);
		return formEntity;
	}


	public void bindEntityToForm(FormClazz entity) {
		if (entity == null) {
			FxDialogShow.showPopInfo("Lütfen bir kayıt seçiniz.");
			return;
		}
		if (getModView().getFxFormMig().getListFormElements().size() == 0) {
			Loghelper.debugLog(getClass(), "Form Alanları yüklenmemiş,Düzeltin");
			return;
		}
		setFormEntity(entity);
		FxEditorFactory.bindEntityToFormCompsByFilterValue(getModView().getFxFormMig().getListFormElements(), entity);
	}

	public FormClazz getFiFormEntityV2(Class<FormClazz> clazz) {
		List<? extends IFiCol> listFormElements = getModView().getFxFormMig().getListFormElements();
		if (listFormElements.size() == 0) return null;
		FormClazz formEntity = FxEditorFactory.bindFormToEntityByFilterNode(listFormElements, clazz);
		return formEntity;
	}

	public FxGenWindowView getModView() {
		return modView;
	}

	public List<? extends IFiCol> getListFormElements() {
		return getModView().getFxFormMig().getListFormElements();
	}

	public FxTableView2 getFxTableView() {
		return getModView().getFxTableMig().getFxTableView();
	}

}
