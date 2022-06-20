package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.text.TextAlignment;
import org.apache.commons.beanutils.PropertyUtils;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.gui.fxTableViewExtra.NestedPropertyValueFactory;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.mvc.IFxSimpleEntityModule;
import ozpasyazilim.utils.mvc.IFxSimpleWitEntCont;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.returntypes.FnResult;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

// Notlar :
// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir.
// (Callback lambda bir fonksiyondur.) Callback<TableColumn<S, T>, TableCell<S, T>>
public class FxTableView2<EntClazz> extends TableView<EntClazz> implements IFxComp {

	private Class<EntClazz> entityClass;

	private String fxId;
	private Map<String, Object> styleMap;
	private List<FxTableCol2> fxTableColList;
	private FilteredList<EntClazz> filteredList;

	// gereksiz çıkartılabilir
	private Boolean filteredListActive;

	// Filter Editor Lokal ve Remote false olanlar hariç lokal filtreleme enable edilir
	private Boolean enableLocalFilterEditor;
	// Remote Filter enable edilir, filter comp de enter action enable yapılır
	private Boolean enableRemoteFilterEditor;
	private Boolean enableSummaryHeader;

	private List<Consumer<FiCol>> listenerBoSelection;

	// Sayfalama için yapıldı.
	private Integer pageViewRowCount;
	private Integer pageTotalRowCount;
	private BiConsumer<Integer, Integer> fnPageAction;

	private Runnable fnSummaryChanged;


	// Özel Filtreler buraya eklenir
	private List<Predicate> predFilterExtraList;
	// Lokal içeriden yapılan filtrelemedir(ör.tablonun filter editorden gelen)
	private Predicate predFilterLocal;
	// Remote ise belli server üzerinde kayıt çekilirken yapılır.
	private Predicate predFilterRemoteDb;

	private BooleanProperty propHeaderChange = new SimpleBooleanProperty(false);

	// Filter Node enter basılınca yapılacak işlem
	private EventHandler<KeyEvent> colRemoteFilterEnterEvent;
	// iç Mekanizmada wrapper sınıfı çalıştırılıyor (dıştan kullanılmıyor)
	private EventHandler<KeyEvent> colFilterNodeEnterEventWrapper;

	// filter node da aşağı tuşuna basınca elemanlar gider
	private EventHandler<KeyEvent> colFilterNodeKeyDownEvent;


	private String headerSummaryClass = "tblHeaderSummary";

	// Sayfalama componentleri
	private FxButton btnPageBegin;
	private FxButton btnPagePrev;
	private FxButton btnPageForward;
	//private FxButton btnPageEnd;
	// sayfadaki ilk satırın index no (1 den başlayarak)
	private Integer lnPageStartIndex;
	private Integer lnPageSize;
	private Integer lnTotalSize;
	private FxLabel lblPageNoIndex;

	public void removeItemsAllFi() {
		setItemsAsFilteredList(new ArrayList());
	}

	public void setPagingButtonsDisable(boolean boDisabled) {
		getBtnPagePrev().setDisable(boDisabled);
		getBtnPageBegin().setDisable(boDisabled);
		getBtnPageForward().setDisable(boDisabled);
	}

	public void setSelectionToCell() {
		// set selection mode to only 1 row
		//getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		getSelectionModel().setCellSelectionEnabled(true);
	}

	public Boolean existColumn(String fieldName) {
		for (FiCol fiTableCol : getFiTableColList()) {
			if (fiTableCol.getFieldName().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}

	public Boolean removeSelectedItem() {
		EntClazz selectedItemFiGen = getSelectedItemFiGen();

		if (selectedItemFiGen == null) {
			return false;
		}
		removeItemFi(selectedItemFiGen);
		return true;
	}

	// TableRow factory içerisine eklenecek eventlar bu map in içerisine tanımlanır
	private Map<FxTableRowActions, Consumer<TableRow>> mapTableRowEvents;

	private Map<FxTableRowActions, Consumer<EntClazz>> mapTableRowEventsByEntity;

	// Satır Actionları
	//EventHandler<MouseEvent>
	//BiConsumer<MouseEvent, TableRow> fiRowDoubleClickEvent;
	//TriConsumer<Object, Boolean, TableRow> fiRowFactoryUpdateFn;

	private Boolean boConfigAutoScrollToLast;

	// ctrl c basılınca key event için yapıldı , yerine addeventhandler eklendi
	//private ObjectProperty<KeyEvent> propTblKeyEvent = new SimpleObjectProperty<>();

	// FxTable comp i , fxtable mig içerisinde ise buraya set edilir.
	private FxTableMig2 fxTableMig;


	public void selectItemFi(int index) {
		getSelectionModel().select(index);
	}

	public void removeColsAllFi() {
		setFxTableColList(null);
		getColumns().clear();
	}

	public void removeItemsByPredicate(Predicate<EntClazz> predicateToRemove) {

		List<EntClazz> listSilinecek = new ArrayList<>();

		for (EntClazz entity : getItemsAllFi()) {
			if (predicateToRemove.test(entity)) {
				listSilinecek.add(entity);
			}
		}

		for (EntClazz entity : listSilinecek) {
			removeItemFi(entity);
		}

	}


	// ******* constructors

	public FxTableView2() {
		super();
		setupFxTable();
	}

	// **** setup method

	private void setupFxTable() {
		setupCopySelectionToClipboard();
	}

	// ****** Static Methods

	public static void setFiColFilterableToTrueIfNull(List<FxTableCol2> colTblMain) {
		colTblMain.forEach(fxTableCol -> {
			if (fxTableCol.getFiCol().getBoFilterable() == null)
				fxTableCol.getFiCol().setBoFilterable(true);
		});
	}

	public static void setFiColFilterableToTrueIfNullForIFiCol(List<IFiCol> listFiCol) {
		listFiCol.forEach(ificol -> {
			if (ificol.getBoFilterable() == null) ificol.setBoFilterable(true);
		});
	}

	public FilteredList<EntClazz> getItemsFiCheckedByBoolField(String fieldnameForSelection) {

		return getItemsCurrentFi(ent -> {
			try {
				return FiBoolean.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldnameForSelection));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		});

	}

	public List<EntClazz> getItemsFiCheckedByBoolFieldAsList(String fieldForSelection) {

		List<EntClazz> list = new ArrayList<>();

		FilteredList<EntClazz> itemsCurrentFi = getItemsCurrentFi(ent -> {
			try {
				return FiBoolean.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldForSelection));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		});

		for (EntClazz entClazz : itemsCurrentFi) {
			list.add(entClazz);
		}

		return list;
	}

	/**
	 * SourceList içinde satır silinince,otomatik filtered list etkilenip, filt.list'den de çıkartılıyor,dinamik olarak.
	 * <p>
	 * Dikkatli kullanılmalı.
	 *
	 * @return
	 */
	public FilteredList<EntClazz> getItemsCheckedByBoSelect() {

		FilteredList<EntClazz> itemsCurrentFi = getItemsCurrentFi(ent -> {
			try {
				return FiBoolean.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, getFiColSelection().getFieldName()));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		});

		return itemsCurrentFi;
	}

	public List<EntClazz> getItemsCheckedByBoSelectAsListInCurrentElements() {

		String fieldForSelection = getFiColSelection().getFieldName();

		FilteredList<EntClazz> itemsCurrentFi = getItemsCurrentFi(ent -> {
			try {
				return FiBoolean.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldForSelection));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		});

		List<EntClazz> listSelected = new ArrayList<>(itemsCurrentFi);

		return listSelected;
	}

	public List<EntClazz> getItemsCheckedByBoSelectAsListInAllElements() {

		String fieldForSelection = getFiColSelection().getFieldName();

		FilteredList<EntClazz> itemsCurrentFi = getItemsAllFi(ent -> {
			try {
				return FiBoolean.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldForSelection));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		});

		List<EntClazz> listSelected = new ArrayList<>(itemsCurrentFi);

		return listSelected;
	}

	public <PrmEntClazz> PrmEntClazz getFilterEntity(Class<PrmEntClazz> clazz) {
		return FxEditorFactory.bindFormToEntityByFilterNode(getFiTableColList(), clazz);
	}

	public FiKeyBean getFilterMap() {
		return FxEditorFactory.bindFiColToMapByFilterNode(getFiTableColList());
	}

	public EntClazz getFilterEntityGen() {
		return FxEditorFactory.bindFormToEntityByFilterNode(getFiTableColList(), getEntityClassAuto());
	}

	public List<FiCol> getFiTableColList() {
		return getFxTableColList().stream().map(FxTableCol2::getFiCol).collect(Collectors.toList());
	}


	public static Node defAutoEditorClass(List<IFiCol> listColumn) {

		Node nodeGenerated = null;

		for (int i = 0; i < listColumn.size(); i++) {

			IFiCol ozTableColumn = listColumn.get(i);
			//ozTableColumn.setColFilterable(true);
			boolean boCompDefined = false;

			if (!boCompDefined && ozTableColumn.getColType() == OzColType.String || ozTableColumn.getColType() == OzColType.Double || ozTableColumn.getColType() == OzColType.Integer) {
				ozTableColumn.setFilterNodeClass(FxTextField.class.getName());
				nodeGenerated = FxEditorFactory.generateAndSetFilterNode(ozTableColumn);
				boCompDefined = true;
			}

			if (!boCompDefined && ozTableColumn.getColType() == OzColType.Date) {
				ozTableColumn.setFilterNodeClass(FxDatePicker.class.getName());
				nodeGenerated = FxEditorFactory.generateAndSetFilterNode(ozTableColumn);
				boCompDefined = true;
			}

			if (!boCompDefined) {
				ozTableColumn.setFilterNodeClass(FxTextField.class.getName());
				nodeGenerated = FxEditorFactory.generateAndSetFilterNode(ozTableColumn);
				continue;
			}

		}

		if (listColumn.size() == 1) return nodeGenerated;
		return null;

	}

	public void addAllFxTableColsPlain(List<FxTableCol2> fxTableColList) {

		if (fxTableColList == null) return;

		for (int i = 0; i < fxTableColList.size(); i++) {
			FxTableCol2 fxTableCol = fxTableColList.get(i);
			addFxTableColFi(fxTableCol);
		}

	}

	public FxTableView2 addAllFiTableColsAuto(List<FiCol> listFiCol) {
		for (FiCol fiCol : listFiCol) {
			addFiColAuto(fiCol);
		}
		return this;
	}

	public FxTableView2 addFiColSelection() {
		FiCol fiTableCol = getFiColSelection();
		addFiColAuto(fiTableCol);
		return this;
	}

	private FiCol getFiColSelection() {
		FiCol fiTableCol = new FiCol("boSecim", "Seç");
		fiTableCol.setPrefSize(40d);
		fiTableCol.buildColType(OzColType.Boolean).buildFiEditable(true).buildSumType(OzColSummaryType.CheckBox);
		return fiTableCol;
	}

	public FxTableView2 addFiColsAuto(FiCol fiCol) {
		addFiColAuto(fiCol);
		return this;
	}

	public void addFiColAuto(FiCol fiTableCol) {
		FxTableCol2 fxTableCol = new FxTableCol2(fiTableCol);
		addFxTableColAuto(fxTableCol);
	}

	public FxTableView2 addAllFxTableCols2Auto(List<FxTableCol2> fxTableColList) {
		for (int i = 0; i < fxTableColList.size(); i++) {
			FxTableCol2 fxTableCol = fxTableColList.get(i);
			addFxTableColAuto(fxTableCol);
		}
		return this;
	}

	public void addFxTableColAuto(FxTableCol2 fxTableCol) {
		// Cell Value Factory and Editor Factory leri ayarlanır
		setupCellValueAndEditorFactory(fxTableCol);
		//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableCol.getId());
		addFxTableColFi(fxTableCol);
	}

	public void setAutoClass() {
		if (this.entityClass == null) {
			try {
				this.entityClass = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
			} catch (Exception ex) {
				Loghelper.errorException(getClass(), ex, "Generic Tip Sınıfı Tespit Edilirken Hata Oluştu.");
			}

		}
	}

	/**
	 * !!!
	 * <p>
	 * Value Factory : Hücreye değerini atar
	 * <p>
	 * Editor Factory : Hücreye konulacak componenti hazırlar
	 *
	 * @param fxTableCol FxTableCol2
	 */
	private void setupCellValueAndEditorFactory(FxTableCol2 fxTableCol) {
		//Loghelperr.getInstance(getClass()).debug("added "+ fxTableCol.getFieldName());
		fxTableCol.setText(fxTableCol.getFiCol().getHeaderName());

		// iki nokta (aa..bb) şeklinde de olabilir , tek nokta karışabilir
		if (fxTableCol.getFiCol().getFieldName().contains(".")) {
			//Loghelperr.getInstance(getClass()).debug("Nested:" + fxTableCol.getFiTableCol().getFieldName());
			fxTableCol.setCellValueFactory(new NestedPropertyValueFactory(fxTableCol.getFiCol().getFieldName()));
		} else {
			fxTableCol.setCellValueFactory(new PropertyValueFactory<>(fxTableCol.getFiCol().getFieldName()));
		}

		//bydefault idi
		FxTableViewCellFactoryModal.setupCellFactoryGeneral(fxTableCol, getEntityClass());
		fxTableCol.setId(fxTableCol.getFiCol().getFieldName());
		//fxTableCol.setAutoFormatter(fxTableCol.getFiTableCol().getColType());
		setAutoFormatter(fxTableCol);

	}

	public void setAutoFormatter(FxTableCol2 fxTableCol) {
		// FxTableViewCellFactoryConfig e taşındı.
	}

	public void addFxTableColFi(FxTableCol2 fxTableCol) {
		getColumns().add(fxTableCol);
		getFxTableColList().add(fxTableCol);
		setupHeader1ForHeaderAndFilterNode(fxTableCol);
		// if (getEnabledLocalFilterEditor() || getEnabledRemoteFilterEditor()) setupHeaderFilterNode(fxTableCol);
		// if (getEnabledSummaryRowHeader() == true) setupHeaderSummaryNode(fxTableCol);
		// activateFilter(fxTableCol);  // setupHeaderFilter içinde konuldu
		// Editor Class belirtilmişse , Editor Factory si oluşturulur
		// FxTableViewCellFactoryModal.setupCellFactoryByEditorClass(this, fxTableCol);

	}

	public Object getSelectedItemFi() {
		return getSelectionModel().getSelectedItem();
	}

	public EntClazz getSelectedItemFiGen() {
		return getSelectionModel().getSelectedItem();
	}

	public FxTableView2 setActivateFxColsFilterableNullToTrue() {
		getFxTableColList().forEach(fxTableCol -> {
			if (fxTableCol.getFiCol().getBoFilterable() == null)
				fxTableCol.getFiCol().setBoFilterable(true);
		});
		setEnableLocalFilterEditor(true);
		activateFilters();
		return this;
	}


	public void setFxColsFilterable(Boolean boFilterable) {
		getFxTableColList().forEach(fxTableCol -> {
			fxTableCol.getFiCol().setBoFilterable(boFilterable);
		});
	}

	public FilteredList<EntClazz> getItemsCurrentFi() {
		return getFilteredList();
	}

	public FilteredList<EntClazz> getItemsCurrentFi(Predicate<EntClazz> predFilter) {
		return getFilteredList().filtered(predFilter);
	}

	/**
	 * Filtrelen-me-miş orijinal listeyi verir
	 *
	 * @return
	 */
	public ObservableList<EntClazz> getItemsAllFi() {
		// currentList
		return getFilteredList().getSource();
	}

	public List<EntClazz> getItemsAllCloneFi() {
		// currentList
		List<EntClazz> listClone = new ArrayList<>();
		for (Object entClazz : getFilteredList().getSource()) {
			EntClazz entClazz1 = (EntClazz) FiReflection.cloneObject(entClazz);
			listClone.add(entClazz1);
		}
		return listClone;
	}

	public ObservableList getFiSourceList() {
		return getFilteredList().getSource();
	}

	/**
	 * Filtrelenmemiş orijinal liste üzerinden filitreleme yapar
	 *
	 * @param predFilter
	 * @return
	 */
	public FilteredList<EntClazz> getItemsAllFi(Predicate<EntClazz> predFilter) {

		FilteredList<EntClazz> filtered = getFilteredList().getSource().filtered(predFilter);

		return filtered;
	}

	@Override
	public void refresh() {
		super.refresh();
		executeFiltersLocalAndExtra();
		updateStatusBar();
		updateSummary();
	}

	public void refreshTableFiAsyn() {
		Platform.runLater(() -> {
			refresh();
			executeFiltersLocalAndExtra();
			updateStatusBar();
			updateSummary();
		});
	}

	public FxLabel getFiLblFooterRowCount() {
		if (getFxTableMig() != null) {
			return getFxTableMig().getLblFooterRowCount();
		}
		return null;
	}

	public FxLabel getFiLblFooterMessage() {
		if (getFxTableMig() != null) {
			return getFxTableMig().getLblFooterMessage();
		}
		return null;
	}


	public void onRowDoubleClickEventFi(Consumer<TableRow> doubleClickEvent) {

		if (doubleClickEvent == null) return;
		//getMapTableRowEvents().remove(TableRowActions.DoubleClick);

		getMapTableRowEvents().put(FxTableRowActions.DoubleClick, doubleClickEvent);

	}


	public void onRowDoubleClickEventByEntityFi(Consumer<EntClazz> doubleClickEvent) {

		if (doubleClickEvent == null) return;
		//getMapTableRowEvents().remove(TableRowActions.DoubleClick);
		getMapTableRowEventsByEntity().put(FxTableRowActions.DoubleClick, doubleClickEvent);

	}

	public void removeRowDoubleClickEvent() {
		if (getMapTableRowEvents().containsKey(FxTableRowActions.DoubleClick))
			getMapTableRowEvents().remove(FxTableRowActions.DoubleClick);
	}

	public void setupRowFactory() {

		if (this.mapTableRowEvents == null) {
			this.mapTableRowEvents = new HashMap<>();
			this.mapTableRowEventsByEntity = new HashMap<>();
		}

		setRowFactory(tblview -> {

			TableRow tableRow = new TableRow<>();  // TableRow<Entity>

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

	private void activateFilters() {
		getFxTableColList().forEach(fxTableCol -> {
			activateFilterSearch(fxTableCol);
		});
	}

	@FiDraft
	@Deprecated
	public void activateHeader(FxTableCol fxTableCol) {

	}

	/**
	 * FiCol.filterNode'a changeListener eklenerek gerekli filtreleme yapmasını sağlamak
	 *
	 * @param fxTableCol
	 */
	private void activateFilterSearch(FxTableCol2 fxTableCol) {

		// Col Filterable değilse, hiçbir işlem yapılmaz
		if (FiBoolean.isFalse(fxTableCol.getFiCol().getBoFilterable())) {
			return;
		}

		// Aşağıda şartlar olursa Filtreleme etkin oluyor, Header Eklenmemişse eklenir.
		if (getEnableLocalFilterEditorNtn() || getEnableRemoteFilterEditorNtn()
				|| FiType.isTrue(fxTableCol.getFiCol().getBoFilterable())) {
			// filter Node eklenmemişse Header Setup edilir.
			if (fxTableCol.getFiCol().getColFilterNode() == null) {
				setupHeader1ForHeaderAndFilterNode(fxTableCol);
			}

		}

		if (checkColFilterableLocal(fxTableCol)) {
			//Loghelperr.getInstance(getClass()).debug("Local Filter enabled");

			// Filter Node içindeki değer filterLocal2 fonk'a verilerek tabloda filtreleme yapılır.
			Consumer<String> fncFilterLocal2 = textProp -> filterLocal(textProp);

			// Filter node değişimi tetikleme
			FxEditorFactory.registerTextPropertyWithDurationForFilterNode(fxTableCol.getFiCol(), fncFilterLocal2, 250);
		}

		if (checkColFilterableRemote(fxTableCol)) {
			// 20-02-2020 çıkarıldı enter event zaten setupHeaderFilterNode 'da set ediliyor
			// new FxEditorFactory().registerEnterFnForFilterNode(fxTableCol.getFiTableCol(), getColFilterNodeEnterEvent());
		}

	}

	/**
	 * Sütunun başlık componentini (Header) ve
	 * <p>
	 * eklenecek filtre componenti (TextField,DatePicker vs) hazırlar
	 *
	 * @param fxcol
	 */
	private void setupHeader1ForHeaderAndFilterNode(FxTableCol2 fxcol) {

		//if (fxcol.getOzTableCol().getFiPaneHeader() != null) return;
		FxLabel label = new FxLabel(fxcol.getFiCol().getHeaderName());
		label.setStyle("-fx-padding: 2px;");
		label.setWrapText(false); // true idi
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);

		//StackPane stack = new StackPane();
		FxMigPane migColHeader = new FxMigPane("insets 0,gap 0 0");
		//migColHeader.setAlignment(Pos.TOP_LEFT);

		//migColHeader.getChildren().add(label);
		migColHeader.add(label, "span");

		migColHeader.prefWidthProperty().bind(fxcol.widthProperty().subtract(5));
		label.prefWidthProperty().bind(migColHeader.prefWidthProperty());

		//fxcol.setFiHeaderAsVbox(migColHeader, fxcol);
		fxcol.getFiCol().setPaneHeader(migColHeader);
		fxcol.setText(fxcol.getFiCol().getHeaderName());

		if (checkColFilterableLocal(fxcol) || checkColFilterableRemote(fxcol)) {
			fxcol.getFiCol().setBoFilterable(true);
			setupHeader2ForFilterNode(fxcol);
		}

		Boolean isExistSummaryNode = false;
		for (FxTableCol2 fxTableCol2 : getFxTableColList()) {
			if (fxTableCol2.getFiCol().getSummaryType() != null) {
				isExistSummaryNode = true;
			}
		}

		if (getEnableSummaryHeader() == true) { //&& fxcol.getFiTableCol().getSummaryType()!=null
			setupHeaderSummaryNode(fxcol);
		}

		fxcol.setGraphic(fxcol.getFiCol().getPaneHeader());

		//fxcol.setStyle("-fx-table-header-alignment: top-left;");
		setPropHeaderChange(true);

	}


	private void setupHeader2ForFilterNode(FxTableCol2 fxTableCol) {

		// filterable degilse yapma

		//Loghelperr.getInstance(getClass()).debug(" Fi Header Setup:" + fxTableCol.getFiHeader());

		//if (fxTableCol.getColFxNode() == null) {
		FxMigPane migHeader = fxTableCol.getFiCol().getPaneHeader();
		Node node = null;
		node = defAutoEditorClass(Arrays.asList(fxTableCol.getFiCol()));
		node.setId("filterNode");

		if (FiBoolean.isFalse(fxTableCol.getFiCol().getBoFilterable())) {
			//Loghelperr.getInstance(getClass()).debug("Node Filter Pasif");
			node.setDisable(true);
		}

		node.getStyleClass().add(getHeaderFilterNodeStyleClass());

		migHeader.add(node, "span");
		fxTableCol.getFiCol().setColFilterNode(node);
		//FxEditorFactory.registerKeyEventForNode(node,fxTableCol.getFiTableCol().getColFilterNodeClass(),getColFilterKeyDownEvent());
		node.addEventHandler(KeyEvent.KEY_PRESSED, getColFilterNodeKeyDownEvent());
		node.addEventHandler(KeyEvent.KEY_PRESSED, getColFilterNodeEnterEventWrapper());

		activateFilterSearch(fxTableCol);

	}

	private void setupHeaderSummaryNode(FxTableCol2 fxcol) {

		if (fxcol.getFiCol().getSummaryType() == null) {

			FxLabel lblSummary = new FxLabel("");

			//lblSummary.setStyle("-fx-padding: 1px;");
			//lblSummary.getStyleClass().add(headerSummaryClass);
			lblSummary.setWrapText(false);
			//lblSummary.setAlignment(Pos.CENTER_LEFT);
			//lblSummary.setTextAlignment(TextAlignment.RIGHT);
			lblSummary.prefWidthProperty().bind(fxcol.getFiCol().getPaneHeader().prefWidthProperty());
			fxcol.getFiCol().setSummaryLabelNode(lblSummary);
			fxcol.getFiCol().setSummaryNode(lblSummary);

			//fxcol.getFiPaneHeader().getChildren().add(lblSummary);
			fxcol.getFiCol().getPaneHeader().add(lblSummary, "span");

			return;
		}

		// Filtre editor eklenmemiş eklensin
		if (fxcol.getFiCol().getColFilterNode() == null) setupHeader2ForFilterNode(fxcol);

		if (fxcol.getFiCol().getSummaryType() == OzColSummaryType.CheckBox) {

			FxMigPane pane = new FxMigPane("insets 0");

			FxCheckBox fxCheckBox = new FxCheckBox();

			fxcol.getFiCol().setSummaryNode(fxCheckBox);
			fxcol.getFiCol().setSummaryCheckBox(fxCheckBox);

			fxCheckBox.setOnAction(event -> {

				Boolean boStatus = fxCheckBox.isSelected();

				getItemsAllFi().forEach(ent -> {

					Boolean disabledSelection = false;

					if (fxcol.getFiCol().getPredFiEditorDisable() != null
							&& fxcol.getFiCol().getPredFiEditorDisable().test(ent)) {
						disabledSelection = true;
					}

					if (!disabledSelection) {
						try {
							PropertyUtils.setNestedProperty(ent, fxcol.getFiCol().getFieldName(), false);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}


				});

				getItemsCurrentFi().forEach(ent -> {

					Boolean disabledSelection = false;

					if (fxcol.getFiCol().getPredFiEditorDisable() != null && fxcol.getFiCol().getPredFiEditorDisable().test(ent)) {
						disabledSelection = true;
					}

					if (!disabledSelection) {
						try {
							PropertyUtils.setNestedProperty(ent, fxcol.getFiCol().getFieldName(), boStatus);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}


				});

				refreshTableFiAsyn();


			});

			fxCheckBox.setAlignment(Pos.CENTER);
			pane.add(fxCheckBox, "span,push,align 50%");

			//fxcol.getFiPaneHeader().getChildren().add(pane);
			fxcol.getFiCol().getPaneHeader().add(pane, "span,grow");
			return;
		}

		FxLabel lblSummary = new FxLabel("");

		//lblSummary.setStyle("-fx-padding: 1px;");
		lblSummary.getStyleClass().add(headerSummaryClass);
		lblSummary.setWrapText(false);
		lblSummary.setAlignment(Pos.CENTER_LEFT);
		//lblSummary.setTextAlignment(TextAlignment.RIGHT);
		lblSummary.prefWidthProperty().bind(fxcol.getFiCol().getPaneHeader().prefWidthProperty());
		fxcol.getFiCol().setSummaryLabelNode(lblSummary);
		fxcol.getFiCol().setSummaryNode(lblSummary);

		//fxcol.getFiPaneHeader().getChildren().add(lblSummary);
		fxcol.getFiCol().getPaneHeader().add(lblSummary, "span");

	}

	public void setItemsAsFilteredList(List listTable) {
		setItemsAsFilteredListMain(listTable);
	}

	public void setItemsAsFilteredListMain(Collection dataList) {

		if (dataList == null) dataList = new ArrayList();

		FilteredList filteredList = new FilteredList(FXCollections.observableArrayList(dataList), getFilterPredicatesAll());
		setFilteredListFi(filteredList);

		SortedList sortableData = new SortedList<>(filteredList);
		setItems(sortableData);
		sortableData.comparatorProperty().bind(this.comparatorProperty());

		if (getBoConfigAutoScrollToLast()) scrollToLastForFilteredList();

		eventsAfterTableViewDataChange();

	}

	public void setItemsAsFilteredListGen(List<EntClazz> listTable) {
		setItemsAsFilteredList(listTable);
	}

	public void setItemsAsFilteredCollection(Collection listTable) {

		if (listTable == null) listTable = new ArrayList<>();

		FilteredList<EntClazz> filteredList = new FilteredList(FXCollections.observableArrayList(listTable), getFilterPredicatesAll());
		setFilteredListFi(filteredList);

		SortedList sortableData = new SortedList<>(filteredList);
		setItems(sortableData);
		sortableData.comparatorProperty().bind(this.comparatorProperty());

		if (getBoConfigAutoScrollToLast()) scrollToLastForFilteredList();

		eventsAfterTableViewDataChange();

	}

	public void setItemsAsFilteredListAsync(List listTable) {

		Platform.runLater(() -> {
			setItemsAsFilteredList(listTable);
		});

	}

	/**
	 * Tablonun içinde bulunan satırları filtreler
	 *
	 * @param newValue
	 */
	private void filterLocal(Object newValue) {

		//ObservableValue<? extends String> observable, String oldValue, String newValue, String fieldName
		//System.out.println("text:"+ newValue);

		//Loghelperr.getInstance(getClass()).debug("Filter Lokal");

		Predicate predFilterLocal = ent -> {

			Predicate predAllCols = ent2 -> true;

			//FiBool logSingleShow = new FiBool(false);
			for (FxTableCol2 fxTableColumn : getFxTableColList()) {

				// sütun filtrelenebilir olması gerekir
				if (checkColFilterableLocal(fxTableColumn)) {

					// !!! true dönünce sonuca eklenir. (filtreden geçer)
					// !!! false dönerse sonuca eklenmez. (filtreden geçmez)
					// filterCheckResult false olursa filtreden geçmez , sonuca girmez.

					// !!!!!!!!!
					// true yakalarsa continue olacak , kontrole devam edecek
					// false yakalarsa return yapılacak , sonuca dahil edilmeyecek

					// Filter editor boşluk olunca onu integer çevirdiğinde null olarak dönüş yapar
					String txFilterValue = FxEditorFactory.getValueOfFilterNodeAsString(fxTableColumn.getFiCol());

					// Filtre değeri boş ise filtreleme yapılmaz
					if (FiString.isEmpty(txFilterValue)) {
						continue;
					}

					//Loghelperr.getInstance(getClass()).debug(" field"+ fxTableCol.getFieldName());
					Object objFilterValue = FxEditorFactory.getNodeObjValueByFilterNode(fxTableColumn.getFiCol());
					// fxTableCol.getTxfFilter().getText();
					//Loghelper.get(getClass()).debug(String.format("Filter Lokal %s : %s ", fxTableColumn.getFiTableCol().getFieldName(), objFilterValue));

					// Tablonun içinde hücre değeri
					Object objCellValue = FiReflection.getProperty(ent, fxTableColumn.getFiCol().getFieldName());

					// !!!!! CellValue Null gelmişse, cellValue yu override edebiliriz türüne göre
					if (objCellValue == null) {

						// FIXME dogruluğu tekrar incelenmeli
						// Boolean için null değer false olarak yorumlandı
						if (fxTableColumn.getFiCol().getColType() == OzColType.Boolean) {
							objCellValue = false;
						}

					} // end if - cellValue == null

					// Özel Aramalar (! ,!! ) ve Boşluk Aramaları
					if (true) {

						//Loghelperr.getInstance(getClass()).debug("Instance Type" );
						//Loghelperr.getInstance(getClass()).debug("ozel kontrol:[" + txFilterValue + "]");

						if (txFilterValue.equals("!") || txFilterValue.matches("^\\s+")) {

							if (FiString.isEmptyToStringWithTrim(objCellValue)) {
								//filterCheckResult = true;
								continue;
							} else {
								//filterCheckResult = false;
								predAllCols = predAllCols.and(entTmp -> false);
								//continue
								break;
							}
						}

						if (txFilterValue.equals("!!")) {

							if (FiString.isEmptyToStringWithTrim(objCellValue) || objCellValue.toString().matches("^\\s+")) {
								//filterCheckResult = false;
								predAllCols = predAllCols.and(entTmp -> false);
//								continue;
								break;
							} else {
								//filterCheckResult = true;
								continue;
							}

						}

					}


					if (objCellValue instanceof String) {

						Boolean filterStringCheck = filterCheckForString(objCellValue, objFilterValue);
						//Loghelper.get(getClass()).debug("Filter String Check Result:" + filterStringCheck);
						if (!filterStringCheck) {
							predAllCols = predAllCols.and(entTmp -> false);
							break;
						}
						continue;
					}

					if (objCellValue instanceof Date) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						//FiConsole.printObjectDefiniton(objFilterValue,"objFilterValue");

						// filterValue dolu olup, cell value boş olursa , sonuca dahil edilmez

						Date dtCellValue = (Date) objCellValue;

						String strDateCell = FiDate.toStringYmd(dtCellValue);
						String strDateFilter = "";

						if (objFilterValue instanceof Date) {
							strDateFilter = FiDate.toStringYmd((Date) objFilterValue);
						} else {
							strDateFilter = objFilterValue.toString();
						}

						//Loghelperr.logSingle("DateCh",getClass(),"Date Cell Value"+ strDateCell + "   : Filter Value :" + strDateFilter );
						//if (!dtCellValue.equals(objFilterValue)) filterCheckResult = false;

						if (!strDateFilter.equals("") && !strDateCell.equals(strDateFilter)) {
							//filterCheckResult = false;
							predAllCols = predAllCols.and(entTmp -> false);
							break;
						}

						continue;

					}


					// Double değer kontrolü

					if (objCellValue instanceof Double) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						// CellValue null ise sonuca ekleme
						if (objCellValue == null) {
							predAllCols = predAllCols.and(entTmp -> false);
							//continue;
							break;
						}

						Double valueCol = (Double) objCellValue;
						String txValueCol = new FiNumber().formatNumberPlain(valueCol);
						txValueCol = txValueCol.replace("-", "");

						if (txFilterValue.matches("^![0-9]*")) {
							//Loghelper.get(getClass()).debug("! filter ına girdi");
							if ((Double) objFilterValue == 0d) {
								if (Math.abs(valueCol - (Double) objFilterValue) < 0.10) {
									//return false;
									predAllCols = predAllCols.and(entTmp -> false);
									break;
								}
								continue;
							}

							// değeri eşit olanlar dahil edilmeyecek
							if (valueCol == objFilterValue) {
								predAllCols = predAllCols.and(entTmp -> false);
								break;
							}
							continue;
						}

						if (txFilterValue.matches("^[><][1-9]?[0-9]*")) {
							//Loghelper.get(getClass()).debug("! filter ına girdi");
							if (txFilterValue.charAt(0) == '>') {
								if (valueCol >= (Double) objFilterValue) {
									//return filterCheckResult;
									continue;
								} else {
									//return false;
									predAllCols = predAllCols.and(entTmp -> false);
									//continue;
									break;
								}
							}

							if (txFilterValue.charAt(0) == '<') {
								if (valueCol <= (Double) objFilterValue) {
									//return filterCheckResult;
									continue;
								} else {
									//return false;
									predAllCols = predAllCols.and(entTmp -> false);
									//continue;
									break;
								}
							}

						}

						// Filter Value

						// W1 Double çevirmeli Yöntem
						//Double dblFilter = (Double) objFilterValue;
						//String txFilter = new FiNumber().formatNumberPlain(dblFilter);  //objFilterValue.toString().replaceAll("\\.0$","");
						// ///////////////////////

						//String valueFilter2 = objFilterValue.toString().replaceAll("(<|>)","");

						// Loghelperr.logSingle("filter1",getClass(),"value filter :"+ valueFilter2);
						// W2 Expo Number To String
						String txFilter = new FiNumber().formatStringExpoNumber(objFilterValue.toString());

						//Loghelperr.getInstance(getClass()).debug(String.format("Double Obj Value : %s , Filter Value: %s",txValueCol,txFilter));
						if (!txValueCol.toString().matches(txFilter + ".*")) {
							//filterCheckResult = false;
							predAllCols = predAllCols.and(entTmp -> false);
							//continue;
							break;
						}
						continue;


//						if (filterCheckResult) {
//							continue;
//						} else {
//							predAllCols.and(entTmp -> false);
//							continue;
//							//return filterCheckResult;
//						}

					}

					if (objCellValue instanceof Integer) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						if (objCellValue == null) {
//							filterCheckResult = false;
//							return filterCheckResult;
							predAllCols = predAllCols.and(entTmp -> false);
							break;
							//continue;
						}

						//Integer cellValue = (Integer) objCellValue;
						String txValueCol = objCellValue.toString();
						String txFilter = objFilterValue.toString(); //new FiNumber().formatStringExpoNumber(objFilterValue.toString());
						txFilter = txFilter.replace(" ", "\\s");
						//Loghelperr.getInstance(getClass()).debug(String.format("Double Obj Value : %s , Filter Value: %s",txValueCol,txFilter));

						//
						Integer valueCol = (Integer) objCellValue;

						if (txFilterValue.matches("^[><][1-9]?[0-9]*")) {
							//Loghelper.get(getClass()).debug("! filter ına girdi");
							if (txFilterValue.charAt(0) == '>') {
								if (valueCol >= (Integer) objFilterValue) {
									//return filterCheckResult;
									continue;
								} else {
									//return false;
									predAllCols = predAllCols.and(entTmp -> false);
									//continue;
									break;
								}
							}

							if (txFilterValue.charAt(0) == '<') {
								if (valueCol <= (Integer) objFilterValue) {
									//return filterCheckResult;
									continue;
								} else {
									//return false;
									predAllCols = predAllCols.and(entTmp -> false);
									//continue;
									break;
								}
							}

						}

						if (!txValueCol.toString().matches(txFilter + ".*")) {
//							filterCheckResult = false;
//							return filterCheckResult;
							predAllCols = predAllCols.and(entTmp -> false);
							//continue;
							break;
						}

						continue;

//						if (filterCheckResult) {
//							continue;
//						} else {
//							return filterCheckResult;
//							predAllCols.and(entTmp -> false);
//							continue;
//						}

					}

					if (objCellValue instanceof Boolean) {

						objFilterValue = FiBoolean.convertBooleanElseValue(objFilterValue, null);

						//FiConsole.printObjectDefinitonLimityByClass(objFilterValue, "Filter Value", getClass());

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						//Loghelperr.getInstance(getClass()).debug(" Obj Filter :" + objFilterValue + " Obj Cell Value" + objCellValue.toString());

						//if (objCellValue == null) objCellValue = false;
						Boolean boCellValue = FiBoolean.convertBooleanElseFalse(objCellValue);

						//FiConsole.printObjectDefinitonLimityByClass(boCellValue, "Bo Cell Value", getClass());

						Boolean boFilterValue = (Boolean) objFilterValue;

						if (!boFilterValue.equals(boCellValue)) {
							predAllCols = predAllCols.and(entTmp -> false);
							break;
						}
						continue;
					}

					// son kontrol
					if (objFilterValue != null && objCellValue == null) {
//						filterCheckResult = false;
//						return filterCheckResult;
						predAllCols = predAllCols.and(entTmp -> false);
						//continue;
						break;
					}
				}

			} // end of For

			//Loghelperr.getInstance(getClass()).debug("-----------");
			return predAllCols.test(ent); //filterCheckResult;
		};
		setPredFilterLocalThenUpdate(predFilterLocal);
		//eventsAfterTableViewDataChange();
	}


	private boolean checkColFilterableLocal(FxTableCol2 fxTableColumn) {

		// fiCol.colFilterable true ise ve enableLocalFilterEditor false edilmişse
		if (FiBoolean.isTrue(fxTableColumn.getFiCol().getBoFilterable())
				&& !FiBoolean.isFalse(getEnableLocalFilterEditorNtn())) {
			//Loghelperr.getInstance(getClass()).debug("FiTableCol ColFilterable is True");
			return true;
		}

		// enableLocalFilterEditor true edilmiş ve ficol.colFilterable false edilmişse
		if (getEnableLocalFilterEditorNtn() &&
				!FiBoolean.isFalse(fxTableColumn.getFiCol().getBoFilterable())) {
			//Loghelperr.getInstance(getClass()).debug("Enable Local Filter True");
			return true;
		}
		return false;
	}

	private boolean checkColFilterableRemote(FxTableCol2 fxTableColumn) {

		//if (FiBoolean.isTrue(fxTableColumn.getFiTableCol().getColFilterable()) && !FiBoolean.isFalse(getEnableRemoteFilterEditor())) return true;

		if (getEnableRemoteFilterEditorNtn() && !FiBoolean.isFalse(fxTableColumn.getFiCol().getBoFilterable())) {
			return true;
		}

		return false;
	}

	private Boolean filterCheckForString(Object objCellValue, Object objFilterValue) {

		// objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
		if (objFilterValue == null) return true;

		// filter value varken, cellvalue null ise sonuca dahil olamaz.
		if (objCellValue == null) {
			return false;
			//filterCheckResult = false;
			//return filterCheckResult;
		}

		//Loghelperr.getInstance(getClass()).debug("Obj Value"+ objCellValue.toString());

		String txCellValue = (String) objCellValue;
		String txFilterValueLower = objFilterValue.toString().toLowerCase();

		String regexPattern = null;
		if (txFilterValueLower.trim().equals("")) {
			regexPattern = "\\s*";
		} else {
			txFilterValueLower = txFilterValueLower.replace("*", ".*");
			txFilterValueLower = txFilterValueLower.replace(" ", "\\s");
			regexPattern = ".*" + txFilterValueLower + ".*";
			//Loghelper.get(getClass()).debug("Regex:" + txFilterValueLower);
		}

		if (!txCellValue.toLowerCase().matches(regexPattern)) {
			//filterCheckResult = false;
			return false;
		}

		return true;

	}

	public void updateStatusBar() {

		//Loghelperr.debugLog(getClass(), "update status barr");

		if (getFxTableMig() != null) {
			Platform.runLater(() -> {
				getFxTableMig().getLblFooterRowCount().setText(" Kayıt Sayısı : " + getFilteredList().size());
			});
		}

		if (getFnSummaryChanged() != null) {
			getFnSummaryChanged().run();
		}

	}

	public void updateSummary() {
		if (getFilteredList() == null || getFilteredList().size() == 0) {
			if (getEnableSummaryHeader()) {
				getFxTableColList().forEach(fxTableCol -> {
					if (fxTableCol.getFiCol().getSummaryLabelNode() != null && fxTableCol.getFiCol().getSummaryType() != null) {
						Platform.runLater(() -> {
							fxTableCol.getFiCol().getSummaryLabelNode().setText("");
							new FxTableModal().styleSummaryLabel(fxTableCol.getFiCol().getSummaryLabelNode(), fxTableCol);
						});
					}
				});
			}
			return;
		}

		//Loghelperr.debugLog(getClass(), "update summary");

		if (getEnableSummaryHeader()) {
			FiHtmlReportConfig fiHtmlReportConfig = new FiHtmlReportConfig();
			getFxTableColList().forEach(fxTableCol -> {
				if (fxTableCol.getFiCol().getSummaryLabelNode() != null && fxTableCol.getFiCol().getSummaryType() != null) {
					Platform.runLater(() -> {
						String sumValue = FiNumber.formatNumber(FxTableModal.calcSummaryValue(getFilteredList(), fxTableCol.getFiCol(), fiHtmlReportConfig));
						fxTableCol.getFiCol().getSummaryLabelNode().setText(sumValue);
						new FxTableModal().styleSummaryLabel(fxTableCol.getFiCol().getSummaryLabelNode(), fxTableCol);
					});
				}
			});
		}
	} // end of updateSummary

	public FxTableCol2 getColumnByID(String colID) {

		//ObservableList<TableColumn<S, ?>> columns = getColumns();

		for (FxTableCol2 fxTableCol : getFxTableColList()) {
			if (fxTableCol.getId().equals(colID)) {
				return fxTableCol;
			}

		}
		return null;
	}

	public FxTableCol2 getColumnByFieldName(String fieldName) {

		for (FxTableCol2 fxTableCol : getFxTableColList()) {
			if (fxTableCol.getFiCol().getFieldName().equals(fieldName)) {
				return fxTableCol;
			}
		}

		return null;
	}

	public List<FxTableCol2> getFxTableColList() {
		if (fxTableColList == null) {
			fxTableColList = new ArrayList<>();
		}
		return fxTableColList;
	}

	private void setFxTableColList(List<FxTableCol2> fxTableColList) {
		this.fxTableColList = fxTableColList;
	}

	public Map<String, Object> getStyleMap() {
		if (this.styleMap == null) styleMap = new HashMap<>();
		return styleMap;
	}

	public void setStyleMap(Map<String, Object> styleMap) {
		this.styleMap = styleMap;
	}

	public Boolean getFilteredListActive() {
		return filteredListActive;
	}

	public void setFilteredListActive(Boolean filteredListActive) {
		this.filteredListActive = filteredListActive;
	}

	public FilteredList getFilteredList() {
		if (filteredList == null) {
			setItemsAsFilteredList(new ArrayList());
		}
		return filteredList;
	}

	public FilteredList<EntClazz> getFilteredListGen() {
		if (filteredList == null) {
			setItemsAsFilteredList(new ArrayList());
		}
		return filteredList;
	}


	public void addAllItemsFi(Collection<? extends EntClazz> c) {
		getFiSourceList().addAll(c);
	}

	public void addAllItemsFi(EntClazz... c) {
		getFiSourceList().addAll(c);
	}

	public void addAllItemsFi(int index, EntClazz c) {
		getFiSourceList().addAll(index, c);
	}

	public void addItemFi(EntClazz c) {
		getFiSourceList().add(c);
		eventsAfterTableViewDataChange();
	}

	public void addItemFi(int index, EntClazz c) {
		getFiSourceList().add(index, c);
	}

	public void removeItemFi(int index) {
		getFiSourceList().remove(index);
	}

	public void removeItemFi(int from, int to) {
		getFiSourceList().remove(from, to);
	}

	public void removeItemFi(EntClazz entity) {
		getFiSourceList().remove(entity);
		updateSummary();
		//getItems().remove(entity); // filterd listelerde olmuyor, unsupported action hatası veriyor
	}

	public Boolean removeItemFiByCandId(EntClazz entity) {

		for (Iterator<EntClazz> iterator = getFiSourceList().iterator(); iterator.hasNext(); ) {
			EntClazz next = iterator.next();

			Object candId = FiReflection.getCandId(next, getEntityClass());
			Object candId2 = FiReflection.getCandId(entity, getEntityClass());

			if (candId == null || candId2 == null) {
				continue;
			}

			if (candId.equals(candId2)) {
				iterator.remove();
				return true;
			}

		}
		return false;
	}

	public void removeAllItemsFi(Collection<?> c) {
		getFiSourceList().removeAll(c);
	}

	public void removeAllItemsFi(EntClazz... c) {
		getFiSourceList().removeAll(c);
	}

	/**
	 * fnFindEqualItemFromList ilk argünman keye karşılık gelen kısmi tablo datası, ikincisi obje yani entity yeni gelen entitydir, bu kısmi tablo datasının içersinden eşit bulunup , işlem yapılır ve bulunmuşsa true, bulunmamışsa false gönderilir.
	 *
	 * @param listData
	 * @param fnKeySelection
	 * @param fnFindEqualItemFromList
	 * @param updateCalculationColumns
	 * @param <KeyClazz>
	 */
	// keye göre birden fazla eleman düşüyorsa
	public <KeyClazz> void appendTableDataForKeyToMulti(List<EntClazz> listData
			, Function<EntClazz, KeyClazz> fnKeySelection
			, BiFunction<List<EntClazz>, EntClazz, Boolean> fnFindEqualItemFromList
			, Consumer<List<EntClazz>> updateCalculationColumns) {

		ObservableList<EntClazz> tableData = getItemsAllFi();
		List<EntClazz> listEklenecek = new ArrayList<>();

		if (tableData.size() > 0) {

			Map<KeyClazz, List<EntClazz>> mapEvrakSiraToListChh = FiCollection.listToMapMulti(tableData, fnKeySelection);

			for (EntClazz newItem : listData) {

				List<EntClazz> listPortionTableData = mapEvrakSiraToListChh.getOrDefault(fnKeySelection.apply(newItem), null);

				Boolean found = false;
				if (listPortionTableData != null) {
					found = fnFindEqualItemFromList.apply(listPortionTableData, newItem);
				}

				// null da bulunmamış kabul edildi.
				if (FiBoolean.isNullOrFalse(found)) {
					listEklenecek.add(newItem);
				}
			}

			if (listEklenecek.size() > 0) {
				Platform.runLater(() -> {
					//Loghelper.debug(getClass(), "Yeni Itemlar Veriler Ekleniyor");
					addAllItemsFi(listEklenecek);
					refreshTableFiAsyn();
				});

			}

			Platform.runLater(() -> {
				updateCalculationColumns.accept(getItemsAllFi());
				refreshTableFiAsyn();
			});


		} else { // tabloda data yok, yeni ekleniyor
			Platform.runLater(() -> {
				updateCalculationColumns.accept(listData);
				setItemsAsFilteredList(listData);
			});
		}

		eventsAfterTableViewDataChange();

	}

	/**
	 * appendTableDataForKeyToSingle Entity
	 * <p>
	 * keye göre tek eleman düşüyorsa
	 *
	 * @param listData
	 * @param fnKeySelection                    Neye göre arama yapıp eşit objeyi bulacak (tablodaki primary key gibi)
	 * @param fnWorksForAppendingOfEqualObjects prm1.EntClazz Tablodaki kayıt , prm2 EntClazz (yeni kayıt) tablodaki kayıt ile birleştirilecek, buna göre tablodaki kayıt güncellenir
	 * @param fnWorksAfterAllAppending          Birleştirme işlemi bittikten sonra yapılacak işlemler
	 * @param <KeyClazz>                        Tekil alanın türü - birleştirme yapılırken kontrol edilecek alan
	 */
	public <KeyClazz> void appendTableDataForKeyToSingle(Collection<EntClazz> listData
			, Function<EntClazz, KeyClazz> fnKeySelection
			, BiConsumer<EntClazz, EntClazz> fnWorksForAppendingOfEqualObjects
			, Consumer<Collection<EntClazz>> fnWorksAfterAllAppending) {

		ObservableList<EntClazz> tableData = getItemsAllFi();
		List<EntClazz> listEklenecek = new ArrayList<>();
		if (tableData.size() > 0) {
			Map<KeyClazz, EntClazz> mapKeyToEntity = FiCollection.listToMapSingle(tableData, fnKeySelection);

			for (EntClazz newItem : listData) {
				EntClazz entityFromTable = mapKeyToEntity.getOrDefault(fnKeySelection.apply(newItem), null);
				Boolean found = false;

				if (entityFromTable != null) {
					fnWorksForAppendingOfEqualObjects.accept(entityFromTable, newItem);
					found = true;
				}

				if (!found) {
					listEklenecek.add(newItem);
				}
			}
			if (listEklenecek.size() > 0) {
				Platform.runLater(() -> {
					//Loghelper.debug(getClass(), "Yeni Itemlar Veriler Ekleniyor");
					addAllItemsFi(listEklenecek);
					refreshTableFiAsyn();
				});
			}

			Platform.runLater(() -> {
				if (fnWorksAfterAllAppending != null) {
					fnWorksAfterAllAppending.accept(getItemsAllFi());
					refreshTableFiAsyn();
				}
			});

		} else { // tabloda data yok, yeni ekleniyor
			Platform.runLater(() -> {
				fnWorksAfterAllAppending.accept(listData);
				setItemsAsFilteredCollection(listData);
			});
		}
		eventsAfterTableViewDataChange();
	}

	private void setFilteredListFi(FilteredList<EntClazz> filteredList) {
		this.filteredList = filteredList;
	}

	public Boolean getEnableLocalFilterEditorNtn() {
		if (this.enableLocalFilterEditor == null) return false;
		return enableLocalFilterEditor;
	}

	public void setEnableLocalFilterEditor(Boolean enableLocalFilterEditor) {
		this.enableLocalFilterEditor = enableLocalFilterEditor;
	}

	public Boolean getEnableRemoteFilterEditorNtn() {
		if (this.enableRemoteFilterEditor == null) return false;
		return enableRemoteFilterEditor;
	}

	public void setEnableRemoteFilterEditor(Boolean enableRemoteFilterEditor) {
		this.enableRemoteFilterEditor = enableRemoteFilterEditor;
	}

	public static void autoFilterEventToAllCol(List<? extends IFiCol> listCol, EventHandler<KeyEvent> eventFilter) {
		for (int i = 0; i < listCol.size(); i++) {
			IFiCol tableCol = listCol.get(i);
			tableCol.setColFilterKeyEvent(eventFilter);
		}
	}

	public FnResult excelOpen(String appDir, String fileName) {
		return excelOpen(appDir, fileName, false);
	}

	public FnResult excelOpen(String appDir, String fileName, Boolean performanceEnabled) {
		String fullPath = appDir + "\\" + fileName;
		FnResult result = FnResult.genInstance();
		Path pathEntegre = Paths.get(appDir);

		if (!Files.exists(pathEntegre)) {
			new File(pathEntegre.toString()).mkdirs();
		}

		FiExcel2.genInstance().writeFxTableViewToExcelWithHeader2(this, Paths.get(fullPath), null, null, performanceEnabled);

		File fileResult = new File(fullPath);

		if (fileResult != null) {
			new FiExcel2().openExcelFileWithApp(fileResult);
			return result.buildResult(true);
		} else {
			new FxDialogShow().showModalWarning("Dosya Oluşturulamadı.Sistem Yöneticinize Başvurun.");
			return result.buildResult(false);
		}

	}

	@FiDraft
	public MigPane pagingPane() {
		MigPane migPane = new MigPane();
		return null;
	}

	public void scrollToLastForFilteredList() {
		final int size = getFilteredList().getSource().size();
		scrollTo(size - 1);
//		getFilteredList().getSource().addListener((ListChangeListener<S>) (c -> {
//			c.next();
//			final int size = getFilteredList().getSource().size();
//			if (size > 0) {
//				scrollTo(size - 1);
//			}
//		}));
	}

	public void initFilteredListIfNullFi() {
		if (getFilteredList() == null) {
			setItemsAsFilteredList(new ArrayList());
		}
	}

	public void setPredFilterExtra(Predicate predFilterExtra) {
		if (predFilterExtra == null) {
			setPredFilterExtraList(null);
		} else {
			setPredFilterExtraList(Arrays.asList(predFilterExtra));
		}
	}

	private void setPredFilterExtraList(List<Predicate> predFilterExtraList) {
		this.predFilterExtraList = predFilterExtraList;
		executeFiltersLocalAndExtra();
	}

	public void appendToPredFilterExtraList(Predicate predFilterToAdd) {
		if (!getPredFilterExtraList().contains(predFilterToAdd)) {
			getPredFilterExtraList().add(predFilterToAdd);
			executeFiltersLocalAndExtra();
		}
	}

	public void removeFromPredFilterExtraList(Predicate predicateToRemove) {
		if (getPredFilterExtraList().contains(predicateToRemove)) {
			getPredFilterExtraList().remove(predicateToRemove);
			executeFiltersLocalAndExtra();
		}
	}

	public void executeFiltersLocalAndExtra() {
		getFilteredList().setPredicate(getFilterPredicatesAll());
		eventsAfterTableViewDataChange();
	}

	private Predicate getFilterPredicatesAll() {
		Predicate predAll = ent -> true;
		// lokal filtrelemeler, filtre editorune giriş yapılan verilerle yapılan
		if (getPredFilterLocal() != null) predAll = predAll.and(getPredFilterLocal());
		// harici modülden,dışarıdan eklenen filtreler
		if (getPredFilterRemoteDb() != null) predAll = predAll.and(getPredFilterRemoteDb());
		//Loghelperr.getInstance(getClass()).debug("Size Filter Out : "+ getListPredFilterExtra().size());
		for (Predicate predItem : getPredFilterExtraList()) {
			predAll = predAll.and(predItem);
		}
		return predAll;
	}

	private void eventsAfterTableViewDataChange() {
		updateSummary();
		updateStatusBar();
		if (getBtnPagePrev() != null) updatePageToolbar();

		for (FxTableCol2 fxTableCol2 : getFxTableColList()) {
			if (fxTableCol2.getFiCol() != null && fxTableCol2.getFiCol().getFnColCellManualChanged() != null) {
				fxTableCol2.getFiCol().getFnColCellManualChanged().accept(null);
			}
		}


	}

	private void setPredFilterLocalThenUpdate(Predicate predFilterLocal) {
		this.predFilterLocal = predFilterLocal;
		//setListpredFilterIn(Arrays.asList(predFilterLocal));
		executeFiltersLocalAndExtra();
	}

	public void removeFxTableCol(FxTableCol fxTableCol) {
		getColumns().remove(fxTableCol);
		getFxTableColList().remove(fxTableCol);
	}

	public void removeFiTableCol(FiCol fxTableCol) {
		if (fxTableCol.getFxTableCol2() != null) {
			getColumns().remove(fxTableCol.getFxTableCol2());
			getFxTableColList().remove(fxTableCol.getFxTableCol2());
		}
	}

	public void removeFxTableCol(FxTableCol2 fxTableCol) {
		getColumns().remove(fxTableCol);
		getFxTableColList().remove(fxTableCol);
	}

	@Override
	public String getFxId() {
		return fxId;
	}

	@Override
	public void setFxId(String fxId) {
		this.fxId = fxId;
	}

	public EventHandler<KeyEvent> getColRemoteFilterEnterEvent() {
		return colRemoteFilterEnterEvent;
	}

	public void setColRemoteFilterEnterEvent(EventHandler<KeyEvent> colRemoteFilterEnterEvent) {
		this.colRemoteFilterEnterEvent = colRemoteFilterEnterEvent;
	}

	public Boolean getEnableSummaryHeader() {
		if (enableSummaryHeader == null) return false;
		return enableSummaryHeader;
	}

	public void setEnableSummaryHeader(Boolean value) {
		this.enableSummaryHeader = value;
		activateHeaderSummary();
	}

	private void activateHeaderSummary() {
		getFxTableColList().forEach(fxTableCol -> {
			if (getEnableSummaryHeader()) setupHeaderSummaryNode(fxTableCol);
		});
	}

	/**
	 * TableRow ile Entity ulaşmak için
	 * <br>TableRow row = (TableRow) event.getSource();
	 * <br>Entity myEntity = (Entity) row.getItem();
	 * <p>
	 */

	public Boolean getBoConfigAutoScrollToLast() {
		if (boConfigAutoScrollToLast == null) return false;
		return boConfigAutoScrollToLast;
	}

	public void setBoConfigAutoScrollToLast(Boolean boConfigAutoScrollToLast) {
		this.boConfigAutoScrollToLast = boConfigAutoScrollToLast;
	}

	@SuppressWarnings("rawtypes")
	private void copySelectionToClipboard() {

		final Set<Integer> rows = new TreeSet<>();
		for (final TablePosition tablePosition : getSelectionModel().getSelectedCells()) {
			rows.add(tablePosition.getRow());
		}

		final StringBuilder strb = new StringBuilder();
		boolean firstRow = true;

		for (final Integer row : rows) {
			if (!firstRow) {
				strb.append('\n');
			}
			firstRow = false;
			boolean firstCol = true;
			for (final TableColumn<?, ?> column : getColumns()) {
				if (!firstCol) {
					strb.append('\t');
				}
				firstCol = false;
				final Object cellData = column.getCellData(row);
				strb.append(cellData == null ? "" : cellData.toString());
			}
		}

		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(strb.toString());
		Clipboard.getSystemClipboard().setContent(clipboardContent);

	}

	/**
	 * Get table selection and copy it to the clipboard.
	 */
	public void copySelectionToClipboard2() {

		StringBuilder clipboardString = new StringBuilder();
		ObservableList<TablePosition> positionList = getSelectionModel().getSelectedCells();
		int prevRow = -1;

		for (TablePosition position : positionList) {

			int row = position.getRow();
			int col = position.getColumn();

			if (col < 0) continue;

			// determine whether we advance in a row (tab) or a column
			// (newline).
			if (prevRow == row) {
				clipboardString.append('\t');
			} else if (prevRow != -1) {
				clipboardString.append('\n');
			}

			// create string from cell
			String text = "";

			// java.util.ArrayList.get(ArrayList.java:435)
			// Exception in thread "JavaFX Application Thread" java.lang.ArrayIndexOutOfBoundsException: -1
			Object observableValue = (Object) getColumns().get(col).getCellObservableValue(row);

			// null-check: provide empty string for nulls
			if (observableValue == null) {
				text = "";
			} else if (observableValue instanceof DoubleProperty) { // TODO: handle boolean etc

				text = NumberFormat.getNumberInstance().format(((DoubleProperty) observableValue).get());

			} else if (observableValue instanceof IntegerProperty) {

				text = NumberFormat.getNumberInstance().format(((IntegerProperty) observableValue).get());

			} else if (observableValue instanceof StringProperty) {
				text = ((StringProperty) observableValue).get();

			} else {
				//System.out.println("Unsupported observable value: " + observableValue);
				if (observableValue != null) {

					Object value = ((ObjectProperty) observableValue).getValue();
					if (value != null) {
						text = value.toString();
					} else {
						text = "";
					}

				} else {
					text = "";
				}
			}

			// add new item to clipboard
			clipboardString.append(text);

			// remember previous
			prevRow = row;
		}

		// create clipboard content
		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(clipboardString.toString());

		// set clipboard content
		Clipboard.getSystemClipboard().setContent(clipboardContent);


	}


	public void setupCopySelectionToClipboard() {

		final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

		addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			if (keyCodeCopy.match(event)) { // || event.getCode() == KeyCode.ESCAPE
				//Loghelperr.getInstance(getClass()).debug("copy c pressed");
				copySelectionToClipboard2();
			}
		});

	}


	public void activateExtensionFxTableSelectAndClose(IFxSimpleWitEntCont iFxSimpleWitEntCont) {

		setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				extensionSelectAndClose(iFxSimpleWitEntCont);
			}

			//default olarak eklendi
//			if (event.getCode() == KeyCode.DOWN) {
//				Platform.runLater(() -> {
//					requestFocus();
//				});
//			}

		});

		onRowDoubleClickEventFi(tableRow -> {
			extensionSelectAndClose(iFxSimpleWitEntCont);
		});
	}

	public void extensionSelectAndClose(IFxSimpleWitEntCont iFxMosCont) {
		EntClazz selectedItem = getSelectionModel().getSelectedItem();
		if (selectedItem == null) return;

		iFxMosCont.setEntityDefault(selectedItem);
		iFxMosCont.setCloseReason("done");
		iFxMosCont.getFxStage().close();
	}

	public void extensionSelectAndClose(IFxSimpleEntityModule iFxMosCont) {

		EntClazz selectedItem = getSelectionModel().getSelectedItem();
		if (selectedItem == null) return;

		iFxMosCont.setSelectedEntity(selectedItem);
		iFxMosCont.setCloseReason("done");
		iFxMosCont.getFxStage().close();
	}

	public void activateExtensionFxTableSelectAndClose(IFxSimpleEntityModule iFxSimpleModule) {

		setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				extensionSelectAndClose(iFxSimpleModule);
			}
		});

		onRowDoubleClickEventFi(tableRow -> {
			extensionSelectAndClose(iFxSimpleModule);
		});
	}


	//public Event getPropTblKeyEvent() {
	//      return propTblKeyEvent.get();
	//}

	//public ObjectProperty<KeyEvent> propTblKeyEventProperty() {
	//	return propTblKeyEvent;
	//}

	// Getter and Setters

	public Map<FxTableRowActions, Consumer<TableRow>> getMapTableRowEvents() {
		if (this.mapTableRowEvents == null) {
			setupRowFactory();
		}
		return mapTableRowEvents;
	}

	public Map<FxTableRowActions, Consumer<EntClazz>> getMapTableRowEventsByEntity() {
		if (this.mapTableRowEventsByEntity == null) {
			setupRowFactory();
		}
		return mapTableRowEventsByEntity;
	}

	public FxTableMig2 getFxTableMig() {
		return fxTableMig;
	}

	public void setFxTableMig(FxTableMig2 fxTableMig) {
		this.fxTableMig = fxTableMig;
	}

	public Class<EntClazz> getEntityClass() {
		if (entityClass == null) {
			//setAutoClass(); // hata veriyor tip belirlenmemişse
		}
		return entityClass;
	}

	public Class<EntClazz> getEntityClassAuto() {
		if (entityClass == null) {
			setAutoClass(); // hata veriyor tip belirlenmemişse
		}
		return entityClass;
	}

	public void setEntityClass(Class<EntClazz> entityClass) {
		this.entityClass = entityClass;
	}

	public List<Predicate> getPredFilterExtraList() {
		if (predFilterExtraList == null) {
			predFilterExtraList = new ArrayList<>();
		}
		return predFilterExtraList;
	}


	public Predicate getPredFilterLocal() {
		return predFilterLocal;
	}

	public Predicate getPredFilterRemoteDb() {
		return predFilterRemoteDb;
	}

	public void setPredFilterRemoteDb(Predicate predFilterRemoteDb) {
		this.predFilterRemoteDb = predFilterRemoteDb;
		activateFilters();
	}

	public boolean isPropHeaderChange() {
		return propHeaderChange.get();
	}

	public BooleanProperty propHeaderChangeProperty() {
		return propHeaderChange;
	}

	public void setPropHeaderChange(boolean propHeaderChange) {
		this.propHeaderChange.set(propHeaderChange);
	}

	public Integer getPageViewRowCount() {
		return pageViewRowCount;
	}

	public void setPageViewRowCount(Integer pageViewRowCount) {
		this.pageViewRowCount = pageViewRowCount;
	}

	public Integer getPageTotalRowCount() {
		return pageTotalRowCount;
	}

	public void setPageTotalRowCount(Integer pageTotalRowCount) {
		this.pageTotalRowCount = pageTotalRowCount;
	}

	public EventHandler<KeyEvent> getColFilterNodeKeyDownEvent() {
		if (colFilterNodeKeyDownEvent == null) {
			EventHandler<KeyEvent> customKeyEvent = keyEvent -> {
				if (keyEvent.getCode() == KeyCode.DOWN) {
					//Platform.runLater(() -> {
					requestFocus();
					//});
				}
			};
			colFilterNodeKeyDownEvent = customKeyEvent;
		}
		return colFilterNodeKeyDownEvent;
	}

	public void setColFilterNodeKeyDownEvent(EventHandler<KeyEvent> colFilterNodeKeyDownEvent) {
		this.colFilterNodeKeyDownEvent = colFilterNodeKeyDownEvent;
	}

	private EventHandler<KeyEvent> getColFilterNodeEnterEventWrapper() {
		if (colFilterNodeEnterEventWrapper == null) {
			colFilterNodeEnterEventWrapper = (keyEvent) -> {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					if (getColRemoteFilterEnterEvent() != null) {
						getColRemoteFilterEnterEvent().handle(keyEvent);
					}
				}
			};
		}
		return colFilterNodeEnterEventWrapper;
	}

	public void activateTablePageToolbar() {

		if (getFxTableMig() != null && getBtnPageBegin() == null) {

			btnPageBegin = new FxButton("<<");
			lblPageNoIndex = new FxLabel("");
			btnPagePrev = new FxButton("<");
			btnPageForward = new FxButton(">");
			btnPageBegin.setSimpleTooltip("Başa Dön");
			//btnPageEnd = new FxButton(">>");
			getFxTableMig().getTableHeaderPane().add(btnPageBegin);
			getFxTableMig().getTableHeaderPane().add(btnPagePrev);
			getFxTableMig().getTableHeaderPane().add(lblPageNoIndex);
			getFxTableMig().getTableHeaderPane().add(btnPageForward);

			//getFxTableMig().getTableHeaderPane().add(btnPageEnd);
			//setLnPageNo(1);
			//if (getLnPageSize()==null) {setLnPageSize(30);}
			updatePageToolbar();

			// Page 1 : 1-20 : 1 + 20 -1 = 20 lastIndex
			// Page 2 : 21-40 : 21 + 20 - 1 = 40 lastIndex
			// 21 + 20 = 41

			btnPageBegin.setOnAction(event -> {
				if (getFnPageAction() != null) {
					setLnPageStartIndex(1); // 21-20 = 1
					getFnPageAction().accept(getLnPageStartIndex(), getLnPageStartIndex() + getLnPageSize() - 1);
					updatePageToolbar();
				}
			});

			btnPagePrev.setOnAction(event -> {
				if (getFnPageAction() != null) {
					int lnPageStartIndex = getLnPageStartIndex() - getLnPageSize(); // 21-20 = 1
					if (lnPageStartIndex < 1) lnPageStartIndex = 1;

					setLnPageStartIndex(lnPageStartIndex); // 21-20 = 1
					getFnPageAction().accept(getLnPageStartIndex(), getLnPageStartIndex() + getLnPageSize() - 1);
					updatePageToolbar();

					// pageno ile
					// (lnPageNo - 1) * getLnPageSize() + 1, lnPageNo * lnPageSize
					//getFnPageAction().accept((lnPageNo - 1) * getLnPageSize() + 1, lnPageNo * lnPageSize);
				}
			});

			btnPageForward.setOnAction(event -> {
				if (getFnPageAction() != null) {
					int lnPageStartIndex = getLnPageStartIndex() + getLnPageSize(); // new Start Index 1+20 = 21
					if (lnPageStartIndex > getLnTotalSize()) {
						return;
					}
					setLnPageStartIndex(lnPageStartIndex); // 21-20 -1 = 40 new last Index
					getFnPageAction().accept(getLnPageStartIndex(), getLnPageStartIndex() + getLnPageSize() - 1);
					updatePageToolbar();
				}
			});

//				btnPageEnd.setOnAction(event -> {
//					if (getFnPageAction()!=null) {
//						lnPageNo = (getLnTotalSize() / getLnPageSize()) + 1;
//						updatePageTooltips();
//						getFnPageAction().accept((lnPageNo - 1) * getLnPageSize() + 1, lnPageNo * lnPageSize);
//					}
//				});


		}

	}

	public void updatePageToolbar() {

		Integer pageCurrent = getLnPageCurrentNo();
		String pageForward = String.valueOf(pageCurrent + 1); // 1/20 =0.1+1 =1+1 =2
		getBtnPageForward().setSimpleTooltip(pageForward);

		Platform.runLater(() -> {
			lblPageNoIndex.setText(pageCurrent.toString() + "/" + getLnLastPageNo().toString());
		});

		//Loghelper.debug(getClass(), "LnPageStartIndex :"+ getLnPageStartIndex());

		if (getLnPageStartIndex() == 1) {
			getBtnPagePrev().setDisable(true);
			getBtnPagePrev().setSimpleTooltip("");
		} else {
			getBtnPagePrev().setDisable(false);
			String pagePrev = String.valueOf(pageCurrent - 1);
			getBtnPagePrev().setSimpleTooltip(pagePrev);
		}

		// index1 totalcount21  1+20=21=21 > 20 pagesize
		// Loghelper.debug(getClass(), "Page Start Index:" + getLnPageStartIndex().toString());
		if (getLnPageStartIndex() + getLnPageSize() > getLnTotalSize()) { // 31+30 =61 > 39
			getBtnPageForward().setDisable(true);
		} else {
			getBtnPageForward().setDisable(false);
		}

		getBtnPageBegin().setDisable(false);

	}

	public String getHeaderFilterNodeStyleClass() {
		//headerFilterNodeStyleClass
		return "tblHeaderFilter";
	}

	public String getHeaderSummaryClass() {
		return headerSummaryClass;
	}

	public FxButton getBtnPageBegin() {
		return btnPageBegin;
	}

	public FxButton getBtnPagePrev() {
		return btnPagePrev;
	}

	public FxButton getBtnPageForward() {
		return btnPageForward;
	}

	public Integer getLnPageSize() {
		if (lnPageSize == null) {
			lnPageSize = 30;
		}
		return lnPageSize;
	}

	public Integer getLnPageCurrentNo() {
		return (getLnPageStartIndex() - 1) / getLnPageSize() + 1; // (21 sayfa) -1=20/20pagesize=1+1 = 2 sayfa
	}

	public void setLnPageSize(Integer lnPageSize) {
		this.lnPageSize = lnPageSize;
	}

	public BiConsumer<Integer, Integer> getFnPageAction() {
		return fnPageAction;
	}

	public void setFnPageAction(BiConsumer<Integer, Integer> fnPageAction) {
		this.fnPageAction = fnPageAction;
	}

	public Integer getLnTotalSize() {
		if (lnTotalSize == null) {
			lnTotalSize = getLnPageSize();
		}
		return lnTotalSize;
	}

	public void setLnTotalSize(Integer lnTotalSize) {
		this.lnTotalSize = lnTotalSize;
		updatePageToolbar();
	}

	public Integer getLnPageStartIndex() {
		if (lnPageStartIndex == null) {
			lnPageStartIndex = 1;
		}
		return lnPageStartIndex;
	}

	public void setLnPageStartIndex(Integer lnPageStartIndex) {
		this.lnPageStartIndex = lnPageStartIndex;
		//updatePageToolbar();
	}

	public void setLnPageStartIndexWithUpdatePageToolbar(Integer lnPageStartIndex) {
		this.lnPageStartIndex = lnPageStartIndex;
		updatePageToolbar();
	}

	public Integer getLnLastPageNo() {
		return (getLnTotalSize() - 1) / getLnPageSize() + 1;
	}

	public FxLabel getLblPageNoIndex() {
		return lblPageNoIndex;
	}

	public void configColSelection(FiCol fiTableCol) {

//		fiTableCol.buildFnEditorRenderer((o, node) -> {
//
//			FxCheckBox node1 = (FxCheckBox) node;
//			//Loghelper.get(getClass()).debug("Renderer işledi");
//
//			node1.selectedProperty().addListener((observable, oldValue, newValue) -> {
//				for (Consumer<List<EntClazz>> consumer : getListenerBoSecim()) {
//					consumer.accept(getItemsFiCheckedByBoolFieldAsList(fiTableCol.getFieldName()));
//				}
//			});
//		});

		fiTableCol.setFnColCellManualChanged(ent -> {
			for (Consumer<FiCol> consumer : getListenerBoSelection()) {
				consumer.accept(fiTableCol); //getItemsFiCheckedByBoolFieldAsList(fiTableCol.getFieldName()
			}
		});

	}

	public List<Consumer<FiCol>> getListenerBoSelection() {
		if (listenerBoSelection == null) {
			listenerBoSelection = new ArrayList<>();
		}
		return listenerBoSelection;
	}

	public void setListenerBoSelection(List<Consumer<FiCol>> listenerBoSelection) {
		this.listenerBoSelection = listenerBoSelection;
	}

	//	private void setColFilterNodeEnterEventWrapper(EventHandler<KeyEvent> colFilterNodeEnterEventWrapper) {
	//this.colFilterNodeEnterEventWrapper = colFilterNodeEnterEventWrapper;
	//}


	public Runnable getFnSummaryChanged() {
		return fnSummaryChanged;
	}

	public void setFnSummaryChanged(Runnable fnSummaryChanged) {
		this.fnSummaryChanged = fnSummaryChanged;
	}
}

