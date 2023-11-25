package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FxTreeTableView<EntClazz> extends TreeTableView<EntClazz> {

	List<FxTreeTableCol> fxTreeTableColumnList;
	Map<String,Object> styleMap;

	private Map<FxTableRowActions, Consumer<TreeTableRow>> mapTableRowEvents;
	private Map<FxTableRowActions, Consumer<EntClazz>> mapTableRowEventsByEntity;

	public FxTreeTableView() {
		super();
	}

	public void setColumnsFxTableColumn(List<FxTreeTableCol> tableColumns) {

		for (int i = 0; i < tableColumns.size(); i++) {
			FxTreeTableCol fxTableColumn = tableColumns.get(i);
			this.getColumns().add(fxTableColumn);
			getFxTreeTableColumnList().add(fxTableColumn);
		}


	}

	public void onRowDoubleClickEventFi(Consumer<TreeTableRow> doubleClickEvent) {

		if (doubleClickEvent == null) return;
		//getMapTableRowEvents().remove(TableRowActions.DoubleClick);

		getMapTableRowEvents().put(FxTableRowActions.DoubleClick, doubleClickEvent);

	}

	public void onRowDoubleClickEventByEntityFi(Consumer<EntClazz> doubleClickEvent) {

		if (doubleClickEvent == null) return;
		//getMapTableRowEvents().remove(TableRowActions.DoubleClick);
		getMapTableRowEventsByEntity().put(FxTableRowActions.DoubleClick, doubleClickEvent);

	}



	public void setColumnsByFiColList(List<FiCol> fiColList) {

		this.getColumns().clear();

		for (int colIndex = 0; colIndex < fiColList.size(); colIndex++) {
			FiCol fiCol = fiColList.get(colIndex);

			// Column geçerli değilse eklenmez
			if (fiCol.getBoEnabled()!=null && !fiCol.getBoEnabled()) continue;

			FxTreeTableCol fxTableColumn = new FxTreeTableCol();
			fxTableColumn.setFiCol(fiCol);
//			fxTableColumn.setHeader(fiCol.getHeaderName());
//			fxTableColumn.setFieldName(fiCol.getFieldName());
			fxTableColumn.setId(fiCol.getFieldName());
			if(fiCol.getColType()==null) fiCol.setColType(OzColType.String);
			//fxTableColumn.setColType(fiCol.getColType().toString());
			if(fiCol.getPrefSize()!=null){
				fxTableColumn.setPrefWidth(fiCol.getPrefSize());
			}
			/*if(ozTableCol.getEditable()!=null && ozTableCol.getEditable()){
				fxTableColumn.setEditable(true);
				fxTableColumn.setAutoEditor();
			}*/

			fxTableColumn.setAutoColumnDefaultByFiCol();

			//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableColumn.getId());

			this.getColumns().add(fxTableColumn);
			getFxTreeTableColumnList().add(fxTableColumn);
		}

	}

/*	private int countHeaderClick = -1 ;

	@Override
	public void sort() {
		final ObservableList<? extends TableColumn<S, ?>> sortOrder = getSortOrder();

		countHeaderClick++;
		super.sort();
		if (sortOrder.size() > 0) {
			//columnClicked = sortOrder.get(0).getId(); // name of column clicked

			//strSortTypeValue = sortOrder.get(0).getSortType().toString(); // ascending or descending

		} else {
           //<what ever you want to perform>
		}
	}*/

	/**
	 * sıralama daha düzgün oluyor , unsorted çalışıyor
	 *
	 * @param //listTable
	 */
//	public void setItemsAsSortedList(List listTable) {
//
//		SortedList sortedList = new SortedList(FXCollections.observableArrayList(listTable));
//
//		setItems(sortedList);//.addAll(listRapor);
//
//		sortedList.comparatorProperty().bind(comparatorProperty());
//
//	}

//	public void setItemsAsFilteredList(List listTable) {
//
//		FilteredList filteredList = new FilteredList(FXCollections.observableArrayList(listTable), o -> true);
//
//		setItems(filteredList);//.addAll(listRapor);
//
//		//filteredList.comparatorProperty().bind(comparatorProperty());
//
//	}


	public FxTreeTableCol getColumnByID(String colID) {

		ObservableList<TreeTableColumn<EntClazz, ?>> columns = getColumns();
		for (FxTreeTableCol fxTableColumn : getFxTreeTableColumnList()) {
			if (fxTableColumn.getId().equals(colID)) {
				System.out.println("fx table col bulundu");
				return fxTableColumn;

			}

		}
		return null;
	}

	public List<FxTreeTableCol> getFxTreeTableColumnList() {
		if (fxTreeTableColumnList == null) {
			fxTreeTableColumnList = new ArrayList<>();
		}
		return fxTreeTableColumnList;
	}

	public void setFxTreeTableColumnList(List<FxTreeTableCol> fxTreeTableColumnList) {
		this.fxTreeTableColumnList = fxTreeTableColumnList;
	}


	public void setItemMapList(Map<EntClazz,List<EntClazz>> dataMap){

		TreeItem rootTreeItem = new TreeItem();

		dataMap.keySet().forEach(entClazz -> {

			List listSubData = dataMap.get(entClazz);

			TreeItem subRoot= new TreeItem(entClazz);
			//subRoot.getChildren().addAll(FXCollections.observableArrayList(listSubData));
			listSubData.forEach(entData -> {
				TreeItem treeItem = new TreeItem(entData);
				subRoot.getChildren().add(treeItem);
			});

			rootTreeItem.getChildren().add(subRoot);

		});

		setRoot(rootTreeItem);

	}

//	public void setItemsAsList2(List<EntClazz> listRapor, Callback<EntClazz,String> callback) {
//
//		//listRapor.stream().collect(Collectors.toMap());
//
//		Map<String,List<EntClazz>> mapList = new HashMap<>();
//
//		listRapor.forEach(entClazz -> {
//
//			String key="Tanımsiz";
//
//			if(callback.call(entClazz)!=null) key=callback.call(entClazz);
//
//			if(!mapList.containsKey(key)) mapList.put(key,new ArrayList<>());
//
//			mapList.get(key).add(entClazz);
//
//		});
//
//
//		mapList.keySet().forEach(s -> {
//
//			//TreeItem treeItem = new TreeItem("");
//
//			//treeItem.getChildren().addAll(FXCollections.observableArrayList(map));
//
//
//		});
//
//	}

	public Map<String, Object> getStyleMap() {
		if(this.styleMap==null)styleMap=new HashMap<>();
		return styleMap;
	}

	public void setStyleMap(Map<String, Object> styleMap) {
		this.styleMap = styleMap;
	}

	public Map<FxTableRowActions, Consumer<TreeTableRow>> getMapTableRowEvents() {
		if (this.mapTableRowEvents == null) {
			setupRowFactory();
		}
		return mapTableRowEvents;
	}

	public void setupRowFactory() {

		if (this.mapTableRowEvents == null) {
			this.mapTableRowEvents = new HashMap<>();
			this.mapTableRowEventsByEntity = new HashMap<>();
		}

		setRowFactory(tv -> {

			TreeTableRow tableRow = new TreeTableRow();  // TableRow<Entity>

			tableRow.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					if (this.mapTableRowEvents.containsKey(FxTableRowActions.DoubleClick)) {
						this.mapTableRowEvents.get(FxTableRowActions.DoubleClick).accept(tableRow);
					}

					if (this.mapTableRowEventsByEntity.containsKey(FxTableRowActions.DoubleClick)) {
						EntClazz entClazz = (EntClazz) tableRow.getItem();
						this.mapTableRowEventsByEntity.get(FxTableRowActions.DoubleClick).accept(entClazz);
					}
				}

			});

			return tableRow;

		});

	}

	public Map<FxTableRowActions, Consumer<EntClazz>> getMapTableRowEventsByEntity() {
		if (this.mapTableRowEventsByEntity == null) {
			setupRowFactory();
		}
		return mapTableRowEventsByEntity;
	}


}
