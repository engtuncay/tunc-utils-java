package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.scene.input.KeyCode;
import ozpasyazilim.utils.gui.fxcomponents.FxButton;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;
import ozpasyazilim.utils.mvc.IFiModCont;

/**
 *
 */
public abstract class FiArbCrudWindowCont extends FiArbWindowCont implements IFiModCont {

	public FiArbCrudWindowCont() {
	}

	@Override
	public void initCont() {
		super.initCont();

	}

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
		btnCrudSelect = genBtnSecim();
		btnCrudRefresh = genBtnRefresh();
		// Add Layout
		getModView().getMigToolbar().add(btnCrudSelect);
		getModView().getMigToolbar().add(btnCrudRefresh);
	}

	protected void addRefreshButton() {
		btnCrudRefresh = genBtnRefresh();
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
		btnCrudRefresh = genBtnRefresh();
		getModView().getMigToolbar().add(btnCrudRefresh);
		btnCrudRefresh.setOnAction((event) -> actCrudRefresh());
	}


	/**
	 * Thread olarak pullTableData yı çalıştırır.
	 */
	protected void addRefreshButtonWithActionThread() {
		btnCrudRefresh = genBtnRefresh();
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
		btnCrudAdd = genBtnAdd();
		btnCrudEdit = genBtnEdit();
		btnCrudDelete = genBtnDelete();

		getModView().getMigToolbar().add(btnCrudAdd);
		getModView().getMigContent().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditAndDeleteButtons() {

		btnCrudEdit = genBtnEdit();
		btnCrudDelete = genBtnDelete();

		getModView().getMigToolbar().add(btnCrudEdit);
		getModView().getMigToolbar().add(btnCrudDelete);
	}

	protected void addEditButton() {
		btnCrudEdit = genBtnEdit();
		getModView().getMigToolbar().add(btnCrudEdit);
	}


	protected void addEditButtonWithActionTextArg() {
		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		getModView().getMigToolbar().add(btnCrudEdit);
		// URFIXME metaent taşınacak
		//btnCrudEdit.setOnAction(event -> actBtnCrudAddEdit(MetaEntConstant.editAction()));
	}

	protected void addEditButtonWithActionBoolArg() {
		btnCrudEdit = genBtnEdit();
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

	public static FxButton genBtnAdd() {
		FxButton btn = new FxButton("Ekle", Icons525.ADDTHIS);
		return btn;
	}

//	public static void configBtnSave(FxButton btn) {
//		if (btn == null) return;
//		btn.setFxIcon(Icons525.SAVE);
//		btn.setText("Kaydet");
//	}


	public static FxButton genBtnEdit() {
		FxButton btnTemp = new FxButton("Düzenle", Icons525.EDIT); // Icons525.PENCIL Alternatif
		return btnTemp;
	}

	public static FxButton genBtnDelete() {
		FxButton btnTemp = new FxButton("Sil", Icons525.CIRCLEDELETE);
		return btnTemp;
	}

	public static FxButton genBtnSecim() {
		FxButton btn = new FxButton("Seç", Icons525.SELECT);
		return btn;
	}

	public FxButton genBtnRefresh() {
		return new FxButton("Yenile", Icons525.REFRESH);
	}


}
