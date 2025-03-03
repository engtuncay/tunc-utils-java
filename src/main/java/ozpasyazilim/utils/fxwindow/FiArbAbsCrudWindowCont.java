package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.scene.input.KeyCode;
import ozpasyazilim.utils.gui.fxcomponents.FxButton;
import ozpasyazilim.utils.metadata.MetaCrudConstant;
import ozpasyazilim.utils.mvc.IFiModCont;

/**
 *
 */
public abstract class FiArbAbsCrudWindowCont extends FiArbWindowCont implements IFiModCont {

	protected FxButton btnCrudAdd;
	protected FxButton btnCrudEdit;
	protected FxButton btnCrudDelete;
	protected FxButton btnCrudSaveAndClose;

	/**
	 * Seçim Düğmesi
	 */
	protected FxButton btnCrudSelect;

	/**
	 * Rapor oluştur Düğmesi
	 */
	protected FxButton btnCrudReport;
	protected FxButton btnCrudRefresh;

	public FiArbAbsCrudWindowCont() {
	}

	@Override
	public void initCont() {
		super.initCont();

	}



	protected void addCrudSaveButtonByCw() {
		btnCrudSaveAndClose = new FxButton("Kaydet", Icons525.MAIL_SEND);
		//btnCrudSaveAndNew = new FxButton("Kaydet ve Yeni", Icons525.DATABASE);

		getModView().getMigToolbar().add(btnCrudSaveAndClose);
		//getModView().getFxMigToolbar().add(btnCrudSaveAndNew);
	}

	protected void addCrudDeleteButtonOnly() {
		btnCrudDelete = new FxButton("Sil", Icons525.CIRCLEDELETE);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void activateCrudSaveAndCloseWithInsert() {
		if (getBtnCrudSaveAndClose() == null) return;
		registerBtnWitInsert(getBtnCrudSaveAndClose());
	}

	protected void activateCrudDeleteWithDelKey() {
		if (getBtnCrudDelete() == null) return;
		registerBtnWitDelete(getBtnCrudDelete());
	}

	protected void activateCrudShortcutsOnRootPane() {
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

	protected void addSelectAndRefreshButton() {
		btnCrudSelect = FiButtons.genBtnSecim();
		btnCrudRefresh = FiButtons.genBtnRefresh();
		// Add Layout
		getModView().getMigToolbar().add(btnCrudSelect);
		getModView().getMigToolbar().add(btnCrudRefresh);
	}

	protected void addRefreshButton() {
		btnCrudRefresh = FiButtons.genBtnRefresh();
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
		btnCrudRefresh = FiButtons.genBtnRefresh();
		getModView().getMigToolbar().add(btnCrudRefresh);
		btnCrudRefresh.setOnAction((event) -> actCrudRefresh());
	}


	/**
	 * Thread olarak pullTableData yı çalıştırır.
	 */
	protected void addRefreshButtonWithActionThread() {
		btnCrudRefresh = FiButtons.genBtnRefresh();
		getModView().getMigToolbar().add(btnCrudRefresh);
		btnCrudRefresh.setOnActionWithThread(this::actCrudRefresh);
	}

	public abstract void actCrudRefresh();

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
		btnCrudAdd = FiButtons.genBtnAdd();
		btnCrudEdit = FiButtons.genBtnEdit();
		btnCrudDelete = FiButtons.genBtnDelete();

		getModView().getMigToolbar().add(btnCrudAdd);
		getModView().getMigContent().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditAndDeleteButtons() {

		btnCrudEdit = FiButtons.genBtnEdit();
		btnCrudDelete = FiButtons.genBtnDelete();

		getModView().getMigToolbar().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditButton() {
		btnCrudEdit = FiButtons.genBtnEdit();
		getModView().getMigToolbar().add(btnCrudEdit);
	}


	protected void addEditButtonWithActionTextArg() {
		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		getModView().getMigToolbar().add(btnCrudEdit);
		btnCrudEdit.setOnAction(event -> actBtnCrudAddEdit(MetaCrudConstant.editAction()));
	}

	protected void addEditButtonWithActionBoolArg() {
		btnCrudEdit = FiButtons.genBtnEdit();
		getModView().getMigToolbar().add(btnCrudEdit);
		btnCrudEdit.setOnAction(event -> actBtnCrudAddEdit(true));
	}

	/**
	 * Use actBtnCrudAddEdit(Boolean boUpdate)
	 *
	 * @param txAction
	 */
	@Deprecated
	public abstract void actBtnCrudAddEdit(String txAction);


	public abstract void actBtnCrudDelete();

	public abstract void actBtnCrudAddEdit(Boolean boEdit);

}
