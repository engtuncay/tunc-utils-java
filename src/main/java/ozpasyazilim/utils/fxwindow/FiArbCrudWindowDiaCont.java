package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.scene.input.KeyCode;
import ozpasyazilim.utils.gui.fxcomponents.FxButton;
import ozpasyazilim.utils.metadata.MetaCrudConstant;
import ozpasyazilim.utils.mvc.IFiModCont;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public class FiArbCrudWindowDiaCont extends FiArbWindowCont implements IFiModCont {

	protected FxButton btnCrudAdd;
	protected FxButton btnCrudEdit;
	protected FxButton btnCrudDelete;
	protected FxButton btnCrudSaveAndClose;

	Function<Boolean,Fdr> fnAddEdit;
	Supplier<Fdr> fnDelete;
	Supplier<Fdr> fnSaveClose;

	/**
	 * Seçim Düğmesi
	 */
	protected FxButton btnCrudSelect;

	/**
	 * Rapor oluştur Düğmesi
	 */
	protected FxButton btnCrudReport;
	protected FxButton btnCrudRefresh;

	public FiArbCrudWindowDiaCont(String connProfile) {
		super(connProfile);
	}

	@Override
	public void initCont() {
		super.initCont();

	}



	public void addCrudSaveButtonAndAction() {
		btnCrudSaveAndClose = new FxButton("Kaydet", Icons525.MAIL_SEND);
		//btnCrudSaveAndNew = new FxButton("Kaydet ve Yeni", Icons525.DATABASE);

		getModView().getMigToolbar().add(btnCrudSaveAndClose);

		btnCrudSaveAndClose.setOnAction(event -> {
			if (getFnSaveClose()!=null) {
				Fdr fdrSaveClose = getFnSaveClose().get();

				if(fdrSaveClose.isTrueBoResult()){
				    closeStageWithDoneReason();
				}

			}
		});
		//getModView().getFxMigToolbar().add(btnCrudSaveAndNew);
	}

	public void addCrudDeleteButtonOnly() {
		btnCrudDelete = new FxButton("Sil", Icons525.CIRCLEDELETE);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	public void activateCrudSaveAndCloseWithInsert() {
		if (getBtnCrudSaveAndClose() == null) return;
		registerBtnWitInsert(getBtnCrudSaveAndClose());
	}

	public void activateCrudDeleteWithDelKey() {
		if (getBtnCrudDelete() == null) return;
		registerBtnWitDelete(getBtnCrudDelete());
	}

	public void activateCrudShortcutsOnRootPane() {
		getModView().getRootPane().setOnKeyReleased(event -> {

			if (event.getCode() == KeyCode.INSERT) {
				if (getBtnCrudAdd() != null) getBtnCrudAdd().fire();
			}

			if (event.getCode() == KeyCode.DELETE) {
				if (getBtnCrudDelete() != null) getBtnCrudDelete().fire();
			}

			if (event.getCode() == KeyCode.F10) {
				if (getBtnCrudEdit() != null) getBtnCrudEdit().fire();
			}

			if (event.getCode() == KeyCode.F5) {
				if (getBtnCrudRefresh() != null) getBtnCrudRefresh().fire();
			}

		});
	}

	protected void activateAddEditRefreshShortCutsOnRootPane() {
		getModView().getRootPane().setOnKeyReleased(event -> {

			if (event.getCode() == KeyCode.INSERT) {
				if (getBtnCrudAdd() != null) getBtnCrudAdd().fire();
			}

			if (event.getCode() == KeyCode.F10) {
				if (getBtnCrudEdit() != null) getBtnCrudEdit().fire();
			}

			if (event.getCode() == KeyCode.F5) {
				if (getBtnCrudRefresh() != null) getBtnCrudRefresh().fire();
			}

		});
	}

	public void addSelectAndRefreshButton() {
		btnCrudSelect = FiButtonIcons.genBtnSecim();
		btnCrudRefresh = FiButtonIcons.genBtnRefresh();
		// Add Layout
		getModView().getMigToolbar().add(btnCrudSelect);
		getModView().getMigToolbar().add(btnCrudRefresh);
	}

	protected void addRefreshButton() {
		btnCrudRefresh = FiButtonIcons.genBtnRefresh();
		getModView().getMigContent().add(btnCrudRefresh);
	}

	protected void actBtnRefreshFire() {
		getBtnCrudRefresh().fire();
	}

	public FxButton getBtnCrudSaveAndClose() {
		return btnCrudSaveAndClose;
	}

	public FxButton getBtnCrudAdd() {
		return btnCrudAdd;
	}

	public FxButton getBtnCrudEdit() {
		return btnCrudEdit;
	}

	public FxButton getBtnCrudDelete() {
		return btnCrudDelete;
	}

	public FxButton getBtnCrudRefresh() {
		return btnCrudRefresh;
	}

	protected void disableCrudButton() {
		if (getBtnCrudAdd() != null) getBtnCrudAdd().setDisable(true);
		if (getBtnCrudEdit() != null) getBtnCrudEdit().setDisable(true);
		if (getBtnCrudDelete() != null) getBtnCrudDelete().setDisable(true);
	}

	/**
	 * Refrest Button, Default Action pullTableData metodu çalıştırılır (override edilebilir)
	 */
	protected void addRefreshButtonWithAction() {
		btnCrudRefresh = FiButtonIcons.genBtnRefresh();
		getModView().getMigToolbar().add(btnCrudRefresh);
		btnCrudRefresh.setOnAction((event) -> actCrudRefresh());
	}


	/**
	 * Thread olarak pullTableData yı çalıştırır.
	 */
	protected void addRefreshButtonWithActionThread() {
		btnCrudRefresh = FiButtonIcons.genBtnRefresh();
		getModView().getMigToolbar().add(btnCrudRefresh);
		btnCrudRefresh.setOnActionWithThread(this::actCrudRefresh);
	}

	public void actCrudRefresh(){

	}

	/**
	 *
	 * actBtnCrudAddEdit(Boolean boUpdate) ve actBtnDelete actionları tanımlanır
	 *
	 */
	protected void addCrudButtonsWithActionsBoolArg() {
		addCrudButtons();
		// Action tanımlamaları
		getBtnCrudAdd().setOnAction(event -> actBtnCrudAddEdit(false));
		getBtnCrudEdit().setOnAction(event -> actBtnCrudAddEdit(true));
		getBtnCrudDelete().setOnAction(event -> actBtnCrudDelete());
	}

	protected void addCrudButtons() {
		btnCrudAdd = FiButtonIcons.genBtnAdd();
		btnCrudEdit = FiButtonIcons.genBtnEdit();
		btnCrudDelete = FiButtonIcons.genBtnDelete();

		getModView().getMigToolbar().add(btnCrudAdd);
		getModView().getMigContent().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditAndDeleteButtons() {

		btnCrudEdit = FiButtonIcons.genBtnEdit();
		btnCrudDelete = FiButtonIcons.genBtnDelete();

		getModView().getMigToolbar().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditButton() {
		btnCrudEdit = FiButtonIcons.genBtnEdit();
		getModView().getMigToolbar().add(btnCrudEdit);
	}


	protected void addEditButtonWithActionTextArg() {
		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		getModView().getMigToolbar().add(btnCrudEdit);
		btnCrudEdit.setOnAction(event -> actBtnCrudAddEdit(MetaCrudConstant.editAction()));
	}

	protected void addEditButtonWithActionBoolArg() {
		btnCrudEdit = FiButtonIcons.genBtnEdit();
		getModView().getMigToolbar().add(btnCrudEdit);
		btnCrudEdit.setOnAction(event -> actBtnCrudAddEdit(true));
	}

	/**
	 * Use actBtnCrudAddEdit(Boolean boUpdate)
	 *
	 * @param txAction
	 */
	@Deprecated
	public void actBtnCrudAddEdit(String txAction){

	}


	public void actBtnCrudDelete(){
		if (getFnDelete()!=null) {
			Fdr fdr = getFnDelete().get();
		}
	}

	public void actBtnCrudAddEdit(Boolean boEdit){
		if (getFnAddEdit()!=null) {
			Fdr apply = getFnAddEdit().apply(boEdit);
		}
	}

	public Function<Boolean, Fdr> getFnAddEdit() {
		return fnAddEdit;
	}

	public void setFnAddEdit(Function<Boolean, Fdr> fnAddEdit) {
		this.fnAddEdit = fnAddEdit;
	}

	public Supplier<Fdr> getFnDelete() {
		return fnDelete;
	}

	public void setFnDelete(Supplier<Fdr> fnDelete) {
		this.fnDelete = fnDelete;
	}

	public Supplier<Fdr> getFnSaveClose() {
		return fnSaveClose;
	}

	public void setFnSaveClose(Supplier<Fdr> fnSaveClose) {
		this.fnSaveClose = fnSaveClose;
	}
}
