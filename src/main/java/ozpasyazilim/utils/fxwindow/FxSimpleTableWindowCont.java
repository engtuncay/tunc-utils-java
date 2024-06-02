package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;
import ozpasyazilim.utils.mvc.IFiModCont;

import java.util.List;

/**
 * Bir Toolbar Pane ve Content Paneden oluşan, Content'ine Table eklenen pencere
 * <p>
 * EntClazz Tablo Satır Entity si
 *
 * @param <EntClazz> tablo satır entity
 */
public class FxSimpleTableWindowCont<EntClazz> extends AbsFiModBaseCont implements IFiModCont {

	FxSimpleTableWindowView<EntClazz> modView;
	String txSelected;

	List<EntClazz> listEntityDefault;
	List<EntClazz> listAll;
	List<EntClazz> listSelectedItem;

	//	EntClazz entityDefault;
	EntClazz entityLastSaved;
	EntClazz entitySelected;

	private FxButton btnCrudAdd;
	private FxButton btnCrudEdit;
	private FxButton btnCrudDelete;
	private FxButton btnCrudRefresh;
	private FxButton btnCrudReport;
	private FxButton btnCrudSelect;

	public FxSimpleTableWindowCont() {
		setModuleLabel("simple-table-window");
		setModuleCode("100");
	}

	@Override
	public FxSimpleTableWindowView<EntClazz> getModView() {
		return modView;
	}

	@Override
	public void initCont() {
		modView = new FxSimpleTableWindowView(true);
	}

	public FxSimpleTableWindowCont buildInit() {
		initCont();
		return this;
	}

	public void setPrefWidthWindows() {

//		ObservableList<TableColumn<EntClazz, ?>> columns = getFxTableView().getColumns();
//
//		Double prefWidth = 30d;
//
//		for (TableColumn column : columns) {
//			//Loghelper.debug(getClass(), "Col Pref Width:"+column.widthProperty().getValue());
//
//			if(column.getPrefSize()!=null){
//				prefWidth+=column.getPrefSize();
//			}else{
//				prefWidth+=80;
//			}
//			//prefWidth += column.getPrefWidth();
//		}


		//Loghelper.debug(getClass(), "Pref Width Calc:"+prefWidth);
		//Loghelper.debug(getClass(), "Migpane Width:"+ getModView().getRootMigPane().getWidth());

		//getModView().getRootMigPane().setMinWidth(prefWidth+200);

		//getModView().getFxTableMig().getFxTableView().setMinWidth(prefWidth);
		//getFxStage().setMinWidth(500d);
		//getFxStage().sizeToScene();

	}

	public void selectItems(List<Integer> propObjValue) {

	}

	public EntClazz getSelectedItemInTable() {
		return (EntClazz) getFxTableView().getSelectedItemFi();
	}

//	public <EntClazz, B> void assignBoSecim(FxTextFieldBtn fxTextFieldBtn, Function<EntClazz, B> fnKeyValue) {
//
//		if (fxTextFieldBtn.getEntValue() == null) return;
//
//		List<EntClazz> listData = (List<EntClazz>) fxTextFieldBtn.getEntValue();
//
//		FilteredList<EntClazz> itemsCurrent = (FilteredList<EntClazz>) getFxTableView().getItemsCurrentFi();
//		Map<B, EntClazz> mapKeyToObject = FiCollection.listToMapSingle(listData, fnKeyValue);
//
//		itemsCurrent.forEach(entity -> {
//			B keyValue = fnKeyValue.apply(entity);
//			if (mapKeyToObject.containsKey(keyValue)) {
//				FiReflection.setter(entity, "boSecim", true);
//				//entity.setBoSecim(true);
//			}
//		});
//	}

	protected void activateCrudShortCutsOnRootPane() {
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

//	protected void addSelectAndRefreshButton() {
//		btnCrudSelect = EntCompHelper.genBtnSecim();
//		btnCrudRefresh = EntCompHelper.genBtnRefresh();
//
//		getModView().getFxMigToolbar().add(btnCrudSelect);
//		getModView().getFxMigToolbar().add(btnCrudRefresh);
//	}
//
//	protected void addRefreshButton() {
//		btnCrudRefresh = EntCompHelper.genBtnRefresh();
//		getModView().getFxMigToolbar().add(btnCrudRefresh);
//	}
//
//	/**
//	 * Refrest Button Action Method pullTableData
//	 */
//	protected void addRefreshButtonWithAction() {
//		btnCrudRefresh = EntCompHelper.genBtnRefresh();
//		getModView().getFxMigToolbar().add(btnCrudRefresh);
//		btnCrudRefresh.setOnAction(event -> pullTableData());
//	}
//
//	protected void pullTableDataFirst() {
//
//	}
//
//	protected void pullTableData() {
//
//	}

//	protected void addCrudButtons() {
//		btnCrudAdd = new FxButton("Ekle", Icons525.ADDTHIS);
//		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
//		btnCrudDelete = new FxButton("Sil", Icons525.CIRCLEDELETE);
//
//		getModView().getFxMigToolbar().add(btnCrudAdd);
//		getModView().getFxMigToolbar().add(btnCrudEdit);
//		getModView().getFxMigToolbar().add(btnCrudDelete);
//	}

	protected void addEditAndDeleteButtons() {

		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		btnCrudDelete = new FxButton("Sil", Icons525.CIRCLEDELETE);

		getModView().getFxMigToolbar().add(btnCrudEdit);
		getModView().getFxMigToolbar().add(btnCrudDelete);

	}

	protected void addEditButton() {
		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		getModView().getFxMigToolbar().add(btnCrudEdit);
	}

	protected void addEditButtonWithAction() {
		btnCrudEdit = new FxButton("Düzenle", Icons525.PENCIL);
		getModView().getFxMigToolbar().add(btnCrudEdit);
		btnCrudEdit.setOnAction(event -> actBtnAddEdit("edit"));
	}


	// Getters Setters

	public List<EntClazz> getListAll() {
		return listAll;
	}

	public void setListAll(List<EntClazz> listAll) {
		this.listAll = listAll;
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

	public EntClazz getEntityLastSaved() {
		return entityLastSaved;
	}

	public void setEntityLastSaved(EntClazz entityLastSaved) {
		this.entityLastSaved = entityLastSaved;
	}

	public FxButton getBtnCrudSelect() {
		return btnCrudSelect;
	}

	public FxTableView2<EntClazz> getFxTableView() {
		return getModView().getFxTableMig().getFxTableView();
	}

//	public EntClazz getEntityDefault() {
//		return entityDefault;
//	}
//
//	public void setEntityDefault(EntClazz entitySelected) {
//		this.entityDefault = entitySelected;
//	}

	public String getTxSelected() {
		return txSelected;
	}

	public void setTxSelected(String txSelected) {
		this.txSelected = txSelected;
	}

	public List<EntClazz> getListEntityDefault() {
		return listEntityDefault;
	}

	public void setListEntityDefault(List<EntClazz> listEntityDefault) {
		this.listEntityDefault = listEntityDefault;
	}

	public FxButton getBtnCrudRefresh() {
		return btnCrudRefresh;
	}

	protected void disableCrudButton() {
		if (getBtnCrudAdd() != null) getBtnCrudAdd().setDisable(true);
		if (getBtnCrudEdit() != null) getBtnCrudEdit().setDisable(true);
		if (getBtnCrudDelete() != null) getBtnCrudDelete().setDisable(true);
	}

	public FxMigPane getMigToolbar() {
		return getModView().getFxMigToolbar();
	}

//	protected FxButton addBtnMotReport() {
//		btnMotReport = new FxButton("Rapor", FiEntegreIcon.getReportIcon());
//		getModView().getFxMigToolbar().add(btnMotReport);
//		return btnMotReport;
//	}
//
//	protected FxButton addBtnReportWithAction() {
//		btnMotReport = new FxButton("Rapor", FiEntegreIcon.getReportIcon());
//		getModView().getFxMigToolbar().add(btnMotReport);
//		btnMotReport.setOnAction(event -> pullTableDataFirst());
//		return btnMotReport;
//	}

	public void registerDeleteOnTable(FxButton btn) {
		getModView().getFxTableView().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			if (event.getCode() == KeyCode.DELETE) {
				btn.fire();
			}
		});
	}

	public FxButton getBtnCrudReport() {
		return btnCrudReport;
	}


	public void actBtnAddEdit(String txAction) {
	}


	public void actBtnDelete() {
	}

	public void setTableContent(List<EntClazz> sqlTableList) {
		getFxTableView().removeItemsAllFi();
		getFxTableView().addAllItemsFi(sqlTableList);
	}

	public void activateSelectButton() {
		btnCrudSelect = new FxButton("Seç");
		btnCrudSelect.setOnAction(event -> {
			EntClazz selectedItemFiGen = getFxTableView().getSelectedItemFiGen();
			if (selectedItemFiGen == null) {
				FxDialogShow.showPopWarn("Lütfen seçim yapınız.");
			} else {
				setEntitySelected(selectedItemFiGen);
				closeStageWithDoneReason();
			}
		});
		getMigToolbar().add(btnCrudSelect);

	}

	public void activateSelectButtonForMulti() {
		btnCrudSelect = new FxButton("Seç");
		btnCrudSelect.setOnAction(event -> {
			List<EntClazz> listSelectedItem = getFxTableView().getItemsFiCheckedAsNewListInAllElements();
			if (FiCollection.isEmpty(listSelectedItem)) {
				FxDialogShow.showPopWarn("Lütfen seçim yapınız.");
			} else {
				setListSelectedItem(listSelectedItem);
				closeStageWithDoneReason();
			}
		});
		getMigToolbar().add(btnCrudSelect);

	}

	public void setEntitySelected(EntClazz entitySelected) {
		this.entitySelected = entitySelected;
	}

	public EntClazz getEntitySelected() {
		return entitySelected;
	}

	public void addNoteLine(String txMessage) {
		getMigToolbar().wrapFi();
		getMigToolbar().addSpan(new FxLabel(txMessage));
	}

	public List<EntClazz> getListSelectedItem() {
		return listSelectedItem;
	}

	public void setListSelectedItem(List<EntClazz> listSelectedItem) {
		this.listSelectedItem = listSelectedItem;
	}
}

