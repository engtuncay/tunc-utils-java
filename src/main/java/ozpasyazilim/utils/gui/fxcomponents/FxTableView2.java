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
import ozpasyazilim.utils.datatypes.FiListString;
import ozpasyazilim.utils.fxwindow.FiArbFormWindowDiaCont;
import ozpasyazilim.utils.gui.components.TableValueFactoryForFkb;
import ozpasyazilim.utils.gui.fxTableViewExtra.NestedPropertyValueFactory;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metadata.FiCol.FimFicTxFilterType;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.mvc.IFxTableCont;
import ozpasyazilim.utils.mvc.IFxTableSelectionCont;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.returntypes.FnResult;
import ozpasyazilim.utils.table.FiColList;
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

    //private static final Logger log = LoggerFactory.getLogger(FxTableView2.class);
    private Class<EntClazz> entityClass;
    private String fxId;
    private Map<String, Object> styleMap;
    private List<FxTableCol2> listFxTableCol;
    private FilteredList<EntClazz> filteredList;

    // gereksiz çıkartılabilir
    private Boolean filteredListActive;

    /**
     * Dataların FiKeybean olarak yüklendiği durumu gösterir
     */
    private Boolean boFkbEnabled;

    // Filter Editor Lokal false olanlar hariç lokal filtreleme enable edilir (ve Remote çıkarıldı)
    private Boolean enableLocalFilterEditor;
    // Remote Filter enable edilir, filter comp de enter action enable yapılır
    private Boolean enableRemoteFilterEditor;
    private Boolean enableSummaryHeader;

    private List<Consumer<FiCol>> listenerBoSelection;

    // ****** Sayfalama Alanları

    /**
     * use fnPageChanged
     */
    @Deprecated
    private BiConsumer<Integer, Integer> fnPageAction;

    private Runnable fnPageChanged;

    // Sayfalama componentleri

    private FxButton btnPageBegin;
    private FxButton btnPagePrev;
    private FxButton btnPageForward;
    private FxButton btnPageEnd;

    // sayfadaki ilk satırın index no (1 den başlayarak)
    private Integer lnPageStartIndex;
    private Integer lnPageSize;
    private Integer lnTotalSize;

    private Integer lnCurrentPageNo;
    private FxLabel lblPageNoIndex;

    /**
     * Sayfalamanın aktif olduğunu gösterir
     */
    private Boolean boPagingInitialized;

    // --- end - Sayfalama

    private Runnable fnSummaryChanged;

    /**
     * Lokal içeriden yapılan filtrelemedir(ör.tablonun filter editorden gelen)
     */
    private Predicate predFilterLocal;

    /**
     * predFilterSpec1 - kullanıcı tarafından özel eklenen liste filtresi
     * <p>
     * Tüm filtreler, tabloya data eklenirken çalıştırılır.
     */
    private Predicate predFilterSpec1;

    /**
     * Diğer özel filtreler buraya eklenir
     */
    private List<Predicate> predFilterExtraList;


    private BooleanProperty propHeaderChange = new SimpleBooleanProperty(false);

    // Filter Node enter basılınca yapılacak işlem
    private EventHandler<KeyEvent> colRemoteFilterEnterEvent;
    // iç Mekanizmada wrapper sınıfı çalıştırılıyor (dıştan kullanılmıyor)
    private EventHandler<KeyEvent> colFilterNodeEnterEventWrapper;

    // filter node da aşağı tuşuna basınca elemanlar gider
    private EventHandler<KeyEvent> colFilterNodeKeyDownEvent;

    //private String headerSummaryClass = "tblHeaderSummary";

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

    /**
     * Header gelen fkb'ye ek olarak alanlar buraya eklenebilir.
     */
    private FiKeyBean fkbHeaderFilterExtra;
    private FiColList ficsFormElemsHeaderFilterExtra;

    IFxTableCont iFxTableCont;

    private FxButton btnExtraFilter;

    // ******* constructors

    public FxTableView2() {
        super();
        setupFxTableByConstructor();
    }

    // *********** Metodlar

    public void removeItemsAllFi() {
        setItemsAsFilteredList(new ArrayList());
    }

    public void setPagingButtonsDisable(boolean boDisabled) {
        if (getBtnPagePrev() != null) getBtnPagePrev().setDisable(boDisabled);
        if (getBtnPageBegin() != null) getBtnPageBegin().setDisable(boDisabled);
        if (getBtnPageForward() != null) getBtnPageForward().setDisable(boDisabled);
        if (getBtnPageEnd() != null) getBtnPageEnd().setDisable(boDisabled);
    }

    public void actiSelectionToSingleCell() {
        // set selection mode to only 1 row
        //getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getSelectionModel().setCellSelectionEnabled(true);
    }

    public Boolean existColumn(String fieldName) {
        for (FiCol fiTableCol : getFiColList()) {
            if (fiTableCol.getOfcTxFieldName().equals(fieldName)) {
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


    public void selectItemFi(int index) {
        getSelectionModel().select(index);
    }

    public void removeColsAllFi() {
        setListFxTableCol(null);
        getColumns().clear();
    }

    public void removeItemsByPredicate(Predicate<EntClazz> predicateToRemove) {

        List<EntClazz> listSilinecek = new ArrayList<>();

        for (EntClazz entity : getItemsAllFromSourceFi()) {
            if (predicateToRemove.test(entity)) {
                listSilinecek.add(entity);
            }
        }

        for (EntClazz entity : listSilinecek) {
            removeItemFi(entity);
        }

    }


    /**
     * Constructor ile başlangıçta setup edilecek şeyler
     */
    private void setupFxTableByConstructor() {
        setupCopySelectionToClipboard();
    }

    // ****** Static Methods

    public static void setFiColFilterableToTrueIfNull(List<FxTableCol2> colTblMain) {
        colTblMain.forEach(fxTableCol -> {
            if (fxTableCol.getRefFiCol().getBoLocFilterable() == null)
                fxTableCol.getRefFiCol().setBoLocFilterable(true);
        });
    }

    public static void setFiColFilterableToTrueIfNullForIFiCol(List<IFiCol> listFiCol) {
        listFiCol.forEach(ificol -> {
            if (ificol.getBoLocFilterable() == null) ificol.setBoLocFilterable(true);
        });
    }

    public FilteredList<EntClazz> getItemsFiCheckedByBoolField(String fieldnameForSelection) {

        return getItemsCurrentFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(fieldnameForSelection), false);
            }

            try {
                return FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldnameForSelection));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

    }

    public List<EntClazz> getItemsCurrentFiCheckedByBoolField(String fieldForSelection) {

        List<EntClazz> list = new ArrayList<>();

        FilteredList<EntClazz> itemsCurrentFi = getItemsCurrentFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(fieldForSelection), false);
            }

            try {
                return FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldForSelection));
            } catch (Exception e) {
                Loghelper.get(getClass()).error(FiException.exTosMain(e));
                return false;
            }
        });

        list.addAll(itemsCurrentFi);

        return list;
    }

    public List<EntClazz> getItemsFiUnCheckedBySelFieldAsList() {
        return getItemsFiUnCheckedByBoolFieldAsList(getSelectionColName());
    }

    public List<EntClazz> getItemsFiUnCheckedByBoolFieldAsList(String fieldForSelection) {

        List<EntClazz> list = new ArrayList<>();

        FilteredList<EntClazz> itemsCurrentFi = getItemsCurrentFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(fieldForSelection), false);
            }

            try {
                return FiBool.isFalseOrNull(FiBool.convertBoolean(PropertyUtils.getNestedProperty(ent, fieldForSelection)));
            } catch (Exception e) {
                Loghelper.get(getClass()).error(FiException.exTosMain(e));
                return false;
            }
        });

        list.addAll(itemsCurrentFi);

        return list;
    }

    public List<EntClazz> getItemsChecked() {
        return getItemsCurrentFiCheckedByBoolField(getSelectionColName());
    }

    /**
     * ItemsChecked By BoSelect (field) As List In Current Elements
     * <p>
     * Liste üzerinde çıkarmalar eklemeler tablo listesini eklememesi için yeni bir list için oluşturuldu
     *
     * @return
     */
    public List<EntClazz> getItemsCurrentFiChecked() {
        return new ArrayList<>(getItemsCurrentFiCheckedAsSourceList());
    }

    /**
     * SourceList içinde satır silinince  tomatik filtered list etkilenip
     * <p>
     * filt.list'den de çıkartılıyor,dinamik olarak.
     * <p>
     * Dikkatli kullanılmalı.
     *
     * @return
     */
    public FilteredList<EntClazz> getItemsCurrentFiCheckedAsSourceList() {

        return getItemsCurrentFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(getFiColSelection().getOfcTxFieldName()), false);
            }

            try {
                return FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, getFiColSelection().getOfcTxFieldName()));
            } catch (Exception e) { // çevirmede hata olursa false döner
                Loghelper.get(getClass()).debug("bool çevirmede hata" + FiException.exToErrorLog(e));
                return false;
            }
        });

    }

    public FilteredList<EntClazz> getItemsFkbCurrentFiCheckedAsSourceList() {

        return getItemsCurrentFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(getFiColSelection().getOfcTxFieldName()), false);
            }

            return false;
        });

    }

    public List<EntClazz> getItemsFiCheckedAsNewListInAllElements() {

        FilteredList<EntClazz> itemsCurrentFi = getItemsAllFromSourceFi(ent -> {

            if (ent instanceof FiKeyBean) {
                FiKeyBean fkbRow = (FiKeyBean) ent;
                return FiBool.or(fkbRow.getAsBoolean(getFiColSelection().getOfcTxFieldName()), false);
            }

            try {
                return FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, getFiColSelection().getOfcTxFieldName()));
            } catch (Exception e) {
                Loghelper.get(getClass()).debug(FiException.exceptiontostring1(e));
                return false;
            }
        });

        List<EntClazz> listSelected = new ArrayList<>(itemsCurrentFi);

        return listSelected;
    }

    public <PrmEntClazz> PrmEntClazz getFilterAsEntity(Class<PrmEntClazz> clazz) {
        return FxEditorFactory.bindFormToEntityByFilterNode(getFiColList(), clazz);
    }

    /**
     * FiCol'un BoFilterLike True ise , string değeri % arasına alır
     *
     * @return
     */
    public FiKeyBean getHeaderFilterAsFkb() {
        return FxEditorFactory.bindFiColListToFkbByFilterNode(getFiColList());
    }

    public EntClazz getHeaderFilterAsEntityGen() {
        return FxEditorFactory.bindFormToEntityByFilterNode(getFiColList(), getEntityClassAuto());
    }

    /**
     * FxTableCol2 objesindeki FiCol alanlarının listesini verir
     *
     * @return
     */
    public List<FiCol> getFiColList() {
        return getListFxTableCol().stream().map(FxTableCol2::getRefFiCol).collect(Collectors.toList());
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

    public FxTableView2 addAllFiColsAuto(List<FiCol> listFiCol) {
        for (FiCol fiCol : listFiCol) {
            addFiColAuto(fiCol);
        }
        return this;
    }

    public void addAllFiColsAutoAsFkb(List<FiCol> listFiCol) {
        for (FiCol fiCol : listFiCol) {
            addFiColAutoAsFkb(fiCol);
        }
    }

    public void addFiColSelection() {
        FiCol fiTableCol = getFiColSelection();
        addFiColAuto(fiTableCol);
    }

    private FiCol getFiColSelection() {
        FiCol fiCol = new FiCol(getSelectionColName(), getSelectionHeaderName());
        fiCol.setPrefSize(40d);
        fiCol.buiColType(OzColType.Boolean).buiBoEditable(true).buiSumType(OzColSummaryType.CheckBox);
        return fiCol;
    }

    private String getSelectionHeaderName() {
        return "Seç";
    }

    private String getSelectionColName() {
        return "boSecim";
    }

    public FxTableView2 addFiColsAuto(FiCol fiCol) {
        addFiColAuto(fiCol);
        return this;
    }

    public void addFiColAuto(FiCol fiCol) {
        FxTableCol2 fxTableCol = new FxTableCol2(fiCol);
        addFxTableColAuto(fxTableCol);
    }

    public void addFiColAutoAsFkb(FiCol fiCol) {
        FxTableCol2 fxTableCol = new FxTableCol2(fiCol);
        addColumnAutoAsFkb(fxTableCol);
        setBoFkbEnabled(true);
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

    public void addColumnAutoAsFkb(FxTableCol2 fxTableCol) {
        // Cell Value Factory and Editor Factory leri ayarlanır
        setupCellValueAndEditorFactoryAsFkb(fxTableCol);
        //Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableCol.getId());
        addFxTableColFi(fxTableCol);
    }

    public void setAutoClass() {
        if (this.entityClass == null) {
            try {
                this.entityClass = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
            } catch (Exception ex) {
                Loghelper.get(FxTableView2.class).error("Generic Tip Sınıfı Tespit Edilirken Hata Oluştu. :" + FiException.exToErrorLog(ex));
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
        fxTableCol.setText(fxTableCol.getRefFiCol().getOfcTxHeader());

        // iki nokta (aa..bb) şeklinde de olabilir , tek nokta karışabilir
        if (fxTableCol.getRefFiCol().getOfcTxFieldName().contains(".")) {
            //Loghelperr.getInstance(getClass()).debug("Nested:" + fxTableCol.getFiTableCol().getFieldName());
            fxTableCol.setCellValueFactory(new NestedPropertyValueFactory(fxTableCol.getRefFiCol().getOfcTxFieldName()));
        } else {
            fxTableCol.setCellValueFactory(new PropertyValueFactory<>(fxTableCol.getRefFiCol().getOfcTxFieldName()));
        }

        //bydefault idi
        FxTableViewCellFactoryModal.setupCellFactoryGeneral(fxTableCol, getEntityClass());
        fxTableCol.setId(fxTableCol.getRefFiCol().getOfcTxFieldName());

    }

    private void setupCellValueAndEditorFactoryAsFkb(FxTableCol2 fxTableCol) {
        //Loghelperr.getInstance(getClass()).debug("added "+ fxTableCol.getFieldName());
        fxTableCol.setText(fxTableCol.getRefFiCol().getOfcTxHeader());

        fxTableCol.setCellValueFactory(new TableValueFactoryForFkb<>(fxTableCol.getRefFiCol().getOfcTxFieldName()));

        //bydefault idi
        FxTableViewCellFactoryModal.setupCellFactoryGeneral(fxTableCol, getEntityClass());
        fxTableCol.setId(fxTableCol.getRefFiCol().getOfcTxFieldName());
        //fxTableCol.setAutoFormatter(fxTableCol.getFiTableCol().getColType());
    }

    public void addFxTableColFi(FxTableCol2 fxTableCol) {
        getColumns().add(fxTableCol);
        getListFxTableCol().add(fxTableCol);
        setupHeader1ForTableCol(fxTableCol);
    }

    public Object getSelectedItemFi() {
        return getSelectionModel().getSelectedItem();
    }

    public EntClazz getSelectedItemFiGen() {
        return getSelectionModel().getSelectedItem();
    }

    public void setActivateEnableLocalFilterAndColsFilterableNullToTrue() {
        getListFxTableCol().forEach(fxTableCol -> {
            if (fxTableCol.getRefFiCol().getBoLocFilterable() == null)
                fxTableCol.getRefFiCol().setBoLocFilterable(true);
        });
        setEnableLocalFilterEditor(true);
        activateFilters();
    }


    public void setFxColsFilterable(Boolean boFilterable) {
        getListFxTableCol().forEach(fxTableCol -> {
            fxTableCol.getRefFiCol().setBoLocFilterable(boFilterable);
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
    public ObservableList<EntClazz> getItemsAllFromSourceFi() {
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
    public FilteredList<EntClazz> getItemsAllFromSourceFi(Predicate<EntClazz> predFilter) {

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
        Platform.runLater(this::refresh);
    }

    public FxLabel getFiLblFooterRowCount() {
        if (getFxTableMig() != null) {
            return getFxTableMig().getLblFooterRowCount();
        }
        return null;
    }

    public FxLabel getFiLblFooterMessage() {
        if (getFxTableMig() != null) {
            return getFxTableMig().getLblFooterMsg();
        }
        return null;
    }


    public void onRowDoubleClickEventFi(Consumer<TableRow> doubleClickEvent) {

        if (doubleClickEvent == null) return;
        //getMapTableRowEvents().remove(TableRowActions.DoubleClick);
        getMapTableRowEvents().put(FxTableRowActions.DoubleClick, doubleClickEvent);

    }


    /**
     * Event tanımları rowfactory de yapıldı, herhangi aktive etme işlemi gerekmez.
     *
     * @param doubleClickEvent
     */
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
        for (FxTableCol2 fxTableCol : getListFxTableCol()) {
            activateFilterSearch(fxTableCol);
        }
    }

    @FiDraft
    @Deprecated
    public void activateHeader(FxTableColDep fxTableColDep) {

    }

    /**
     * FiCol.filterNode'a changeListener eklenerek gerekli filtreleme yapmasını sağlamak
     *
     * @param fxTableCol
     */
    private void activateFilterSearch(FxTableCol2 fxTableCol) {

        // Col Filterable değilse, hiçbir işlem yapılmaz
        if (FiBool.isFalse(fxTableCol.getRefFiCol().getBoLocFilterable())) {
            return;
        }

        // Aşağıda şartlar olursa Filtreleme etkin oluyor, Header Eklenmemişse eklenir.
        if (getEnableLocalFilterEditorNtn() || getEnableRemoteFilterEditorNtn()
                || FiType.isTrue(fxTableCol.getRefFiCol().getBoLocFilterable())
                || FiType.isTrue(fxTableCol.getRefFiCol().getBoRemFilterable())
        ) {
            // filter Node eklenmemişse Header Setup edilir.
            if (fxTableCol.getRefFiCol().getColFilterNode() == null) {
                setupHeader1ForTableCol(fxTableCol);
            }

        }

        if (checkColFilterableLocal(fxTableCol)) {
            //Loghelperr.getInstance(getClass()).debug("Local Filter enabled");

            // Filter Node içindeki değer filterLocal fonk'a verilerek tabloda filtreleme yapılır.
            Consumer<String> fnFilterLocal = this::execFilterLocal;

            // Filter node değişimi tetikleme
            FxEditorFactory.registerTextPropertyWithDurationForFilterNode(fxTableCol.getRefFiCol(), fnFilterLocal, 250);
        }

        // 20-02-2020 çıkarıldı enter event zaten setupHeaderFilterNode 'da set ediliyor
//        if (checkColFilterableRemote(fxTableCol)) {
//            // new FxEditorFactory().registerEnterFnForFilterNode(fxTableCol.getFiTableCol(), getColFilterNodeEnterEvent());
//        }

    }

    /**
     * Sütunun başlık componentini (Header) ve
     * <p>
     * eklenecek filtre componenti (TextField,DatePicker vs) hazırlar
     *
     * @param fxcol
     */
    private void setupHeader1ForTableCol(FxTableCol2 fxcol) {

        //if (fxcol.getOzTableCol().getFiPaneHeader() != null) return;
        FxLabel label = new FxLabel(fxcol.getRefFiCol().getOfcTxHeader());
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
        fxcol.getRefFiCol().setPaneHeader(migColHeader);
        fxcol.setText(fxcol.getRefFiCol().getOfcTxHeader());

        if (checkColFilterableLocal(fxcol) || checkColFilterableRemote(fxcol)) {
            fxcol.getRefFiCol().setBoLocFilterable(true);
            setupHeader2ForFilterNode(fxcol);
        }

        Boolean isExistSummaryNode = false;
        for (FxTableCol2 fxTableCol2 : getListFxTableCol()) {
            if (fxTableCol2.getRefFiCol().getSummaryType() != null) {
                isExistSummaryNode = true;
            }
        }

        if (getEnableSummaryHeaderNtn()) { //&& fxcol.getFiTableCol().getSummaryType()!=null
            //Loghelper.get(getClass()).debug("Summary Node eklenecek FiColFieldName:"+fxcol.getFiCol().getHeaderName());
            setupHeaderSummaryNode(fxcol);
        }

        fxcol.setGraphic(fxcol.getRefFiCol().getPaneHeader());

        //fxcol.setStyle("-fx-table-header-alignment: top-left;");
        setPropHeaderChange(true);

    }


    private void setupHeader2ForFilterNode(FxTableCol2 fxTableCol) {

        // filterable degilse yapma

        //Loghelperr.getInstance(getClass()).debug(" Fi Header Setup:" + fxTableCol.getFiHeader());

        //if (fxTableCol.getColFxNode() == null) {
        FxMigPane migHeader = fxTableCol.getRefFiCol().getPaneHeader();
        Node node = null;
        node = defAutoEditorClass(Collections.singletonList(fxTableCol.getRefFiCol()));
        if (node == null) node = new FxLabel("");
        node.setId("filterNode");

        if (FiBool.isFalse(fxTableCol.getRefFiCol().getBoLocFilterable())) {
            //Loghelperr.getInstance(getClass()).debug("Node Filter Pasif");
            node.setDisable(true);
        }

        node.getStyleClass().add(getHeaderFilterNodeStyleClass());

        migHeader.addGrowXSpan(node);
        fxTableCol.getRefFiCol().setColFilterNode(node);
        //FxEditorFactory.registerKeyEventForNode(node,fxTableCol.getFiTableCol().getColFilterNodeClass(),getColFilterKeyDownEvent());
        node.addEventHandler(KeyEvent.KEY_PRESSED, getColFilterNodeKeyDownEvent());
        node.addEventHandler(KeyEvent.KEY_PRESSED, getColFilterNodeEnterEventWrapper());

        activateFilterSearch(fxTableCol);

    }

    private void setupHeaderSummaryNode(FxTableCol2 fxcol) {

        if (fxcol.getRefFiCol().getSummaryType() == null) {

            //Loghelper.get(getClass()).debug("Summary Node belirlenmemiş.Genel Boş Label Eklenir." + fxcol.getFiCol().getFieldName());

            FxLabel lblSummary = new FxLabel("");

            //lblSummary.setStyle("-fx-padding: 1px;");
            //lblSummary.getStyleClass().add(headerSummaryClass);
            lblSummary.setWrapText(false);
            //lblSummary.setAlignment(Pos.CENTER_LEFT);
            //lblSummary.setTextAlignment(TextAlignment.RIGHT);
            lblSummary.prefWidthProperty().bind(fxcol.getRefFiCol().getPaneHeader().prefWidthProperty());
            fxcol.getRefFiCol().setSummaryLabelNode(lblSummary);
            fxcol.getRefFiCol().setSummaryNode(lblSummary);

            //fxcol.getFiPaneHeader().getChildren().add(lblSummary);
            fxcol.getRefFiCol().getPaneHeader().add(lblSummary, "span");

            return;
        }

        // Filtre editor eklenmemiş eklensin
        if (fxcol.getRefFiCol().getColFilterNode() == null) setupHeader2ForFilterNode(fxcol);

        if (fxcol.getRefFiCol().getSummaryType() == OzColSummaryType.CheckBox) {

            FxMigPane pane = new FxMigPane("insets 0");

            FxCheckBox fxCheckBox = new FxCheckBox();

            fxcol.getRefFiCol().setSummaryNode(fxCheckBox);
            fxcol.getRefFiCol().setSummaryCheckBox(fxCheckBox);

            fxCheckBox.setOnAction(event -> {

                Boolean boStatus = fxCheckBox.isSelected();


                getItemsAllFromSourceFi().forEach(ent -> {


                    Boolean disabledSelection = false;

                    if (fxcol.getRefFiCol().getPredEditorDisable() != null
                            && fxcol.getRefFiCol().getPredEditorDisable().test(ent)) {
                        disabledSelection = true;
                    }

                    if (!disabledSelection) {
                        String fieldName = fxcol.getRefFiCol().getOfcTxFieldName();
                        try {
                            PropertyUtils.setNestedProperty(ent, fieldName, false);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            //e.printStackTrace();
                            Loghelper.get(getClass()).error(FiException.exTosMain(e));

                        } catch (NoSuchMethodException e) {
                            //e.printStackTrace();
                            Loghelper.get(getClass()).error(FiException.exTosMain(e));
                            Loghelper.get(getClass()).debug("Setter Metodu Mevcut Degil !!! Alan Adı: " + fieldName);
                        }
                    }
                });

                getItemsCurrentFi().forEach(ent -> {

                    Boolean disabledSelection = false;

                    if (fxcol.getRefFiCol().getPredEditorDisable() != null && fxcol.getRefFiCol().getPredEditorDisable().test(ent)) {
                        disabledSelection = true;
                    }

                    if (!disabledSelection) {
                        String fieldName = fxcol.getRefFiCol().getOfcTxFieldName();
                        try {
                            PropertyUtils.setNestedProperty(ent, fieldName, boStatus);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            //e.printStackTrace();
                            Loghelper.get(getClass()).error(FiException.exTosMain(e));
                        } catch (NoSuchMethodException e) {
                            //e.printStackTrace();
                            Loghelper.get(getClass()).error(FiException.exTosMain(e));
                            Loghelper.get(getClass()).debug("Setter Metodu Mevcut Degil !!! Alan Adı: " + fieldName);
                        }
                    }


                });

                refreshTableFiAsyn();
            });

            fxCheckBox.setAlignment(Pos.CENTER);
            pane.add(fxCheckBox, "span,push,align 50%");

            //fxcol.getFiPaneHeader().getChildren().add(pane);
            fxcol.getRefFiCol().getPaneHeader().add(pane, "span,grow");
            return;
        }

        FxLabel lblSummary = new FxLabel("");
        //Loghelper.get(getClass()).debug("Summary Node Alanı açılacak.:" + fxcol.getFiCol().getFieldName());

        //lblSummary.setStyle("-fx-padding: 1px;");
        lblSummary.getStyleClass().add(getHeaderSummaryClass());
        lblSummary.setWrapText(false);
        lblSummary.setAlignment(Pos.CENTER_LEFT);
        //lblSummary.setTextAlignment(TextAlignment.RIGHT);
        lblSummary.prefWidthProperty().bind(fxcol.getRefFiCol().getPaneHeader().prefWidthProperty());
        fxcol.getRefFiCol().setSummaryLabelNode(lblSummary);
        fxcol.getRefFiCol().setSummaryNode(lblSummary);

        //fxcol.getFiPaneHeader().getChildren().add(lblSummary);
        fxcol.getRefFiCol().getPaneHeader().add(lblSummary, "span");

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

    public void setItemsAsFilteredListAsync(List listData) {

        Platform.runLater(() -> {
            setItemsAsFilteredList(listData);
        });

    }

    public void setItemsAsFilteredListAsync(List listTable, Integer lnTotalSize) {

        if (lnTotalSize != null) {
            setLnTotalSize(lnTotalSize);
        }

        setItemsAsFilteredListAsync(listTable);

    }

    public void setItemsAsFilteredListAsync2(List listData) {

        if (listData == null) {
            listData = new ArrayList();
        }

        if (listData.isEmpty()) {
            FxDialogShow.showPopInfo("Tabloya Eklenecek Veri Yok.");
        }

        List finalListTable = listData;
        Platform.runLater(() -> {
            setItemsAsFilteredList(finalListTable);
        });

    }


    /**
     * Tablonun içinde bulunan satırları filtreler
     *
     * @param newValue
     */
    private void execFilterLocal(Object newValue) {

        //ObservableValue<? extends String> observable, String oldValue, String newValue, String fieldName
        //System.out.println("text:"+ newValue);

        //Loghelper.get(getClass()).debug("Filter Lokal Çalıştı");

        Predicate predFilterLocal = ent -> {

            Predicate predAllCols = ent2 -> true;

            //FiBool logSingleShow = new FiBool(false);
            for (FxTableCol2 fxTableColumn : getListFxTableCol()) {

                // sütun filtrelenebilir olması gerekir
                if (checkColFilterableLocal(fxTableColumn)) {

                    // !!! true dönünce sonuca eklenir. (filtreden geçer)
                    // !!! false dönerse sonuca eklenmez. (filtreden geçmez)
                    // filterCheckResult false olursa filtreden geçmez , sonuca girmez.

                    // !!!!!!!!!
                    // true yakalarsa continue olacak , kontrole devam edecek
                    // false yakalarsa return yapılacak , sonuca dahil edilmeyecek

                    // Filter editor boşluk olunca onu integer çevirdiğinde null olarak dönüş yapar
                    FiCol refFiCol = fxTableColumn.getRefFiCol();
                    String txFilterValue = FxEditorFactory.getValueOfFilterNodeAsString(refFiCol);

                    // Filtre değeri boş ise filtreleme yapılmaz
                    if (FiString.isEmpty(txFilterValue)) {
                        continue;
                    }

                    //Loghelperr.getInstance(getClass()).debug(" field"+ fxTableCol.getFieldName());
                    Object objFilterValue = FxEditorFactory.getNodeObjValueByFilterNode(refFiCol);
                    // fxTableCol.getTxfFilter().getText();
//                    Loghelper.get(getClass()).debug(String.format("Filter Lokal %s : %s (objFilterValue) ", fxTableColumn.getRefFiCol().getOfcTxFieldName(), objFilterValue));

                    // Tablonun içinde hücre değeri
                    Object objCellValue = null;

                    if (FiBool.isTrue(getBoFkbEnabled())) {
                        if (ent instanceof FiKeyBean) {
                            //Loghelper.get(getClass()).debug("FiKeybean Row Instance");
                            FiKeyBean fkbEnd = (FiKeyBean) ent;
                            objCellValue = fkbEnd.getAsObj(refFiCol.getOfcTxFieldName());
                        }
                    } else {
                        objCellValue = FiReflection.getProperty(ent, refFiCol.getOfcTxFieldName());
                    }

//                    if (objCellValue != null) {
//                        Loghelper.get(getClass()).debug("Filter CellValue:" + objCellValue + objCellValue.getClass().getSimpleName());
//                    }else {
//                        Loghelper.get(getClass()).debug("Filter CellValue null");
//                    }

                    //Loghelper.get(getClass()).debug("Filter CellValue:" + objCellValue);

                    // !!!!! CellValue Null gelmişse, cellValue yu override edebiliriz türüne göre
                    if (objCellValue == null) {

                        // FIXME dogruluğu tekrar incelenmeli
                        // Boolean için null değer false olarak yorumlandı
                        if (refFiCol.getColType() == OzColType.Boolean) {
                            objCellValue = false;
                        }

                    } // end if - cellValue == null

                    // Özel Aramalar (! ,!! ) ve Boşluk Aramaları
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

                        if (!strDateFilter.isEmpty() && !strDateCell.equals(strDateFilter)) {
                            //filterCheckResult = false;
                            predAllCols = predAllCols.and(entTmp -> false);
                            break;
                        }

                        continue;

                    }


                    // Double değer kontrolü
                    // Not : instanceof olduğu için objCellValue null gelmez
                    if (objCellValue instanceof Double) {

                        // objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
                        if (objFilterValue == null) continue;

                        Double valueCol = (Double) objCellValue;
                        String txValueCol = FiNumber.formatNumberPlain(valueCol);
                        txValueCol = txValueCol.replace("-", "");

                        if (!FiString.isEmptyTrim(refFiCol.getTxFilterType())) {
                            //Loghelper.get(getClass()).debug("! filter ına girdi");
                            //txFilterValue.charAt(0) == '>'
                            if (refFiCol.getTxFilterType().equals(FimFicTxFilterType.greaterThan().gk())) {
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

//                            if (txFilterValue.charAt(0) == '<') {
//                                if (valueCol <= (Double) objFilterValue) {
//                                    //return filterCheckResult;
//                                    continue;
//                                } else {
//                                    //return false;
//                                    predAllCols = predAllCols.and(entTmp -> false);
//                                    //continue;
//                                    break;
//                                }
//                            }

                        }

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
                        String txFilter = FiNumber.formatStringExpoNumber(objFilterValue.toString());

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

                    if (objCellValue instanceof Integer || objCellValue instanceof Short) {

                        //Loghelper.get(getClass()).debug("Filter Local Integer-Short Çalıştı ObjFilterValue :" + objFilterValue);

                        // objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
                        if (objFilterValue == null) continue;

                        String txValueCol = objCellValue.toString();
                        String txFilter = objFilterValue.toString(); //new FiNumber().formatStringExpoNumber(objFilterValue.toString());
                        txFilter = txFilter.replace(" ", "\\s");
                        //Loghelperr.getInstance(getClass()).debug(String.format("Double Obj Value : %s , Filter Value: %s",txValueCol,txFilter));

                        // integer,short'tan büyük olduğu için genişletme yapar
                        Integer valueCol = null;
                        if (objCellValue instanceof Short) {
                            valueCol = Integer.valueOf((Short) objCellValue);
                        } else {
                            valueCol = (Integer) objCellValue;
                        }

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

                        if (!txValueCol.matches(txFilter + ".*")) {
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

                        objFilterValue = FiBool.convertBooleanElseValue(objFilterValue, null);

                        //FiConsole.printObjectDefinitonLimityByClass(objFilterValue, "Filter Value", getClass());

                        // objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
                        if (objFilterValue == null) continue;

                        //Loghelperr.getInstance(getClass()).debug(" Obj Filter :" + objFilterValue + " Obj Cell Value" + objCellValue.toString());

                        //if (objCellValue == null) objCellValue = false;
                        Boolean boCellValue = FiBool.convertBooleanElseFalse(objCellValue);

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

        if (FiBool.isFalse(fxTableColumn.getRefFiCol().getBoLocFilterable())) {
            //Loghelper.get(getClass()).debug("checkColFilterableLocal LocFilterable False:"+ fxTableColumn.getRefFiCol().getOfcTxFieldName());
            return false;
        }

        // fiCol.colFilterable true ise ve enableLocalFilterEditor false edilmemişse
        if (FiBool.isTrue(fxTableColumn.getRefFiCol().getBoLocFilterable())
                && !FiBool.isFalse(getEnableLocalFilterEditorNtn())) {
            //Loghelper.get(getClass()).debug("FiTableCol Lokal ColFilterable is True :" + fxTableColumn.getRefFiCol().getOfcTxFieldName());
            return true;
        }

        // enableLocalFilterEditor true edilmiş  ( ficol.colFilterable false edilmemişse yukarıda şart saglanmış zaten)
        //Loghelperr.getInstance(getClass()).debug("Enable Local Filter True");
        return getEnableLocalFilterEditorNtn();
    }

    private boolean checkColFilterableRemote(FxTableCol2 fxTableColumn) {

        //if (FiBoolean.isTrue(fxTableColumn.getFiTableCol().getColFilterable()) && !FiBoolean.isFalse(getEnableRemoteFilterEditor())) return true;

        if (getEnableRemoteFilterEditorNtn()
                && !FiBool.isFalse(fxTableColumn.getRefFiCol().getBoLocFilterable())) {
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
            txFilterValueLower = txFilterValueLower.replace("%", ".*");
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

        // Tabloda veri yoksa toplam hesaplanmaz
        if (getFilteredList() == null || getFilteredList().size() == 0) {
            if (getEnableSummaryHeaderNtn()) {
                getListFxTableCol().forEach(fxTableCol -> {
                    if (fxTableCol.getRefFiCol().getSummaryLabelNode() != null && fxTableCol.getRefFiCol().getSummaryType() != null) {
                        Platform.runLater(() -> {
                            fxTableCol.getRefFiCol().getSummaryLabelNode().setText("");
                            new FxTableModal().styleSummaryLabel(fxTableCol.getRefFiCol().getSummaryLabelNode(), fxTableCol);
                        });
                    }
                });
            }
            return;
        }

        //Loghelperr.debugLog(getClass(), "update summary");

        if (getEnableSummaryHeaderNtn()) {
            //Loghelper.get(getClass()).debug("Tablonun Sütunlar için özet Fonksiyonu çalışacak");

            FiReportConfig fiReportConfig = new FiReportConfig();
            for (FxTableCol2 fxTableCol : getListFxTableCol()) {
                if (fxTableCol.getRefFiCol().getSummaryLabelNode() != null && fxTableCol.getRefFiCol().getSummaryType() != null) {
                    Platform.runLater(() -> {
                        String sumValue = FiNumber.formatNumber(FxTableModal.calcSummaryValue(getFilteredList(), fxTableCol.getRefFiCol(), fiReportConfig, FiBool.isTrue(getBoFkbEnabled())));
                        fxTableCol.getRefFiCol().getSummaryLabelNode().setText(sumValue);
                        new FxTableModal().styleSummaryLabel(fxTableCol.getRefFiCol().getSummaryLabelNode(), fxTableCol);
                    });
                }
            }
        }

    } // end of updateSummary

    public FxTableCol2 getColumnByID(String colID) {

        //ObservableList<TableColumn<S, ?>> columns = getColumns();

        for (FxTableCol2 fxTableCol : getListFxTableCol()) {
            if (fxTableCol.getId().equals(colID)) {
                return fxTableCol;
            }

        }
        return null;
    }

    public FxTableCol2 getColumnByFieldName(String fieldName) {

        for (FxTableCol2 fxTableCol : getListFxTableCol()) {
            if (fxTableCol.getRefFiCol().getOfcTxFieldName().equals(fieldName)) {
                return fxTableCol;
            }
        }

        return null;
    }

    public TableColumn getColumnByFieldName1(String fieldName) {

        for (TableColumn tableColumn : getColumns()) {
            if (FiString.orEmpty(tableColumn.getId()).equals(fieldName)) {
                return tableColumn;
            }
        }

        return null;
    }

    public List<FxTableCol2> getListFxTableCol() {
        if (listFxTableCol == null) {
            listFxTableCol = new ArrayList<>();
        }
        return listFxTableCol;
    }

    private void setListFxTableCol(List<FxTableCol2> listFxTableCol) {
        this.listFxTableCol = listFxTableCol;
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
        eventsAfterTableViewDataChange();
    }

    public void addAllItemsFi(EntClazz... c) {
        getFiSourceList().addAll(c);
        eventsAfterTableViewDataChange();
    }

    public void addAllItemsFi(int index, EntClazz c) {
        getFiSourceList().addAll(index, c);
        eventsAfterTableViewDataChange();
    }

    public void addItemFi(EntClazz c) {
        getFiSourceList().add(c);
        eventsAfterTableViewDataChange();
    }

    public void addItemFi(int index, EntClazz c) {
        getFiSourceList().add(index, c);
        eventsAfterTableViewDataChange();
    }

    public void removeItemFi(int index) {
        getFiSourceList().remove(index);
        eventsAfterTableViewDataChange();
    }

    public void removeItemFi(int from, int to) {
        getFiSourceList().remove(from, to);
        eventsAfterTableViewDataChange();
    }

    public void removeItemFi(EntClazz entity) {
        getFiSourceList().remove(entity);
        eventsAfterTableViewDataChange();
        //updateSummary(); 1-12-22 commented
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
                eventsAfterTableViewDataChange();
                return true;
            }

        }
        return false;
    }

    public Boolean removeItemFiById(EntClazz entity) {

        for (Iterator<EntClazz> iterator = getFiSourceList().iterator(); iterator.hasNext(); ) {
            EntClazz next = iterator.next();

            Object candId = FiReflection.getIdValue(next, getEntityClass());
            Object candId2 = FiReflection.getIdValue(entity, getEntityClass());

            if (candId == null || candId2 == null) {
                continue;
            }

            if (candId.equals(candId2)) {
                iterator.remove();
                eventsAfterTableViewDataChange();
                return true;
            }

        }
        return false;
    }

    public void removeAllItemsFi(Collection<?> c) {
        getFiSourceList().removeAll(c);
        eventsAfterTableViewDataChange();
    }

    public void removeAllItemsFi(EntClazz... c) {
        getFiSourceList().removeAll(c);
        eventsAfterTableViewDataChange();
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

        ObservableList<EntClazz> tableData = getItemsAllFromSourceFi();
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
                if (FiBool.isFalseOrNull(found)) {
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
                updateCalculationColumns.accept(getItemsAllFromSourceFi());
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

        ObservableList<EntClazz> tableData = getItemsAllFromSourceFi();
        List<EntClazz> listEklenecek = new ArrayList<>();
        if (!tableData.isEmpty()) {
            Map<KeyClazz, EntClazz> mapKeyToEntity = FiCollection.listToMapSingle(tableData, fnKeySelection);

            for (EntClazz newItem : listData) {
                EntClazz entityFromTable = mapKeyToEntity.getOrDefault(fnKeySelection.apply(newItem), null);
                boolean found = false;

                if (entityFromTable != null) {
                    fnWorksForAppendingOfEqualObjects.accept(entityFromTable, newItem);
                    found = true;
                }

                if (!found) {
                    listEklenecek.add(newItem);
                }
            }
            if (!listEklenecek.isEmpty()) {
                Platform.runLater(() -> {
                    //Loghelper.debug(getClass(), "Yeni Itemlar Veriler Ekleniyor");
                    addAllItemsFi(listEklenecek);
                    refreshTableFiAsyn();
                });
            }

            Platform.runLater(() -> {
                if (fnWorksAfterAllAppending != null) {
                    fnWorksAfterAllAppending.accept(getItemsAllFromSourceFi());
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
            new FxDialogShow().showModalWarningAlert("Dosya Oluşturulamadı.Sistem Yöneticinize Başvurun.");
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

    public void removeAllFromPredFilterExtraList() {
        setPredFilterExtra(null);
        executeFiltersLocalAndExtra();
    }

    public void executeFiltersLocalAndExtra() {
        getFilteredList().setPredicate(getFilterPredicatesAll());
        eventsAfterTableViewDataChange();
    }

    /**
     * UBOM Local filtreleme için tüm predicateler birleştirilir
     * <p>
     * getPredFilterLocal + getPredFilterRemoteDb + getPredFilterExtraList
     *
     * @return
     */
    private Predicate getFilterPredicatesAll() {
        Predicate predAll = ent -> true;
        // lokal filtrelemeler, filtre editorune giriş yapılan verilerle yapılan
        if (getPredFilterLocal() != null) predAll = predAll.and(getPredFilterLocal());
        // harici modülden,dışarıdan eklenen filtreler
        if (getPredFilterSpec1() != null) predAll = predAll.and(getPredFilterSpec1());
        //Loghelperr.getInstance(getClass()).debug("Size Filter Out : "+ getListPredFilterExtra().size());
        for (Predicate predItem : getPredFilterExtraList()) {
            predAll = predAll.and(predItem);
        }
        return predAll;
    }

    private void eventsAfterTableViewDataChange() {

        Platform.runLater(() -> {
            if (getBoConfigAutoScrollToLast()) scrollToLastForFilteredList();
        });

        updateSummary();
        updateStatusBar();
        if (getBtnPagePrev() != null) updatePageToolbarComps();

        for (FxTableCol2 fxTableCol2 : getListFxTableCol()) {
            if (fxTableCol2.getRefFiCol() != null && fxTableCol2.getRefFiCol().getFnColCellManualChanged() != null) {
                fxTableCol2.getRefFiCol().getFnColCellManualChanged().accept(null);
            }
        }


    }

    private void setPredFilterLocalThenUpdate(Predicate predFilterLocal) {
        this.predFilterLocal = predFilterLocal;
        //setListpredFilterIn(Arrays.asList(predFilterLocal));
        executeFiltersLocalAndExtra();
    }

    public void removeFxTableCol(FxTableColDep fxTableColDep) {
        getColumns().remove(fxTableColDep);
        getListFxTableCol().remove(fxTableColDep);
    }

    public void removeFiColByFxTableCol2(FiCol fiCol) {
        if (fiCol.getFxTableCol2() != null) {
            getColumns().remove(fiCol.getFxTableCol2());
            getListFxTableCol().remove(fiCol.getFxTableCol2());
        }
    }

    public void removeFiColById(FiCol fiCol) {

        TableColumn tableColumn = getColumnByFieldName1(fiCol.getOfcTxFieldName());

        if (tableColumn != null) {
            getColumns().remove(tableColumn);

            if (tableColumn instanceof FxTableCol2) {
                Loghelper.get(getClass()).debug("FxTableCol2 instance");
                getListFxTableCol().remove(tableColumn);
            } else {
                //Loghelper.get(getClass()).debug("TableCol FxTableCol2 instance degil !!!");
            }

        }

    }

    public void removeFxTableCol(FxTableCol2 fxTableCol) {
        getColumns().remove(fxTableCol);
        getListFxTableCol().remove(fxTableCol);
    }

    @Override
    public String getFxId() {
        return fxId;
    }

    public String getFxIdNtn() {
        if (fxId == null) {
            return "";
        }
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

    public Boolean getEnableSummaryHeaderNtn() {
        if (enableSummaryHeader == null) return false;
        return enableSummaryHeader;
    }

    public Boolean getEnableSummaryHeader() {
        return enableSummaryHeader;
    }

    public void setEnableSummaryHeader(Boolean value) {
        this.enableSummaryHeader = value;
        activateHeaderSummary();
    }

    private void activateHeaderSummary() {
        getListFxTableCol().forEach(fxTableCol -> {
            if (getEnableSummaryHeaderNtn()) setupHeaderSummaryNode(fxTableCol);
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

        StringBuilder sbClipboardText = new StringBuilder();
        ObservableList<TablePosition> positionList = getSelectionModel().getSelectedCells();
        int prevRow = -1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            if (col < 0) continue;

            // determine whether we advance in a row (tab) or a column
            // (newline).
            if (prevRow == row) {
                sbClipboardText.append('\t');
            } else if (prevRow != -1) {
                sbClipboardText.append('\n');
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

            } else { // observableValue!=null and instanceof Something
                //System.out.println("Unsupported observable value: " + observableValue);

                Object value = ((ObjectProperty) observableValue).getValue();
                if (value != null) {
                    text = value.toString();
                } else {
                    text = "";
                }

            }
            // add new item to clipboard
            sbClipboardText.append(text);
            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(sbClipboardText.toString());

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


    /**
     * Enter veya Double Click ile Seç ve Kapat
     *
     * @param iFxTableSelectionCont
     */
    public void activateExtensionFxTableSelectAndClose(IFxTableSelectionCont iFxTableSelectionCont) {

        setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                extensionSelectAndClose(iFxTableSelectionCont);
            }

            //default olarak eklendi
//			if (event.getCode() == KeyCode.DOWN) {
//				Platform.runLater(() -> {
//					requestFocus();
//				});
//			}

        });

        onRowDoubleClickEventFi(tableRow -> {
            extensionSelectAndClose(iFxTableSelectionCont);
        });
    }

    public void extensionSelectAndClose(IFxTableSelectionCont iFxTableSelectionCont) {
        EntClazz selectedItem = getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        iFxTableSelectionCont.setEntitySelected(selectedItem);
        iFxTableSelectionCont.setCloseReason("done");
        iFxTableSelectionCont.getFxStageInit().close();
    }

//    public void extensionSelectAndClose(IFxSimpleEntityModule iFxMosCont) {
//
//        EntClazz selectedItem = getSelectionModel().getSelectedItem();
//        if (selectedItem == null) return;
//
//        iFxMosCont.setSelectedEntity(selectedItem);
//        iFxMosCont.setCloseReason("done");
//        iFxMosCont.getFxStage().close();
//    }

//    public void activateExtensionFxTableSelectAndClose(IFxSimpleEntityModule iFxSimpleModule) {
//
//        setOnKeyReleased(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                extensionSelectAndClose(iFxSimpleModule);
//            }
//        });
//
//        onRowDoubleClickEventFi(tableRow -> {
//            extensionSelectAndClose(iFxSimpleModule);
//        });
//    }


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

    public Predicate getPredFilterSpec1() {
        return predFilterSpec1;
    }

    public void setPredFilterSpec1(Predicate predFilterSpec1) {
        this.predFilterSpec1 = predFilterSpec1;
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
                        FiKeyBean fkbFilter = getHeaderFilterAsFkb();

                        FiListString fullKeyList = fkbFilter.getFullKeys();

                        boolean boRemoFilterable = false;

                        if (!fullKeyList.isEmpty()) {

                            FiColList fiCols = new FiColList(getFiColList());

                            for (String key : fullKeyList) {
                                FiCol fiColByField = fiCols.getFiColByField(key);

                                if(fiColByField==null)continue;

                                if (FiBool.isNullOrTrue(fiColByField.getBoRemFilterable())) {
                                    boRemoFilterable = true;
                                }
                            }
                        }

                        if (!boRemoFilterable && !fullKeyList.isEmpty()) {
                            getFiLblFooterMessage().setText("Bu sütun henüz veri çekimine müsait değil");
                            return;
                        }

                        //sayfalamayı reset eder (1 sayfaya getirir)
                        updatePageSystemBeforeRemoteFilter();
                        getColRemoteFilterEnterEvent().handle(keyEvent);
                    }
                }
            };
        }
        return colFilterNodeEnterEventWrapper;
    }


    public void activatePageToolbar() {

        if (getFxTableMig() == null || FiBool.isTrue(getBoPagingInitialized())) {
            return;
        }

        setBoPagingInitialized(true);
        btnPageBegin = new FxButton("<<");
        lblPageNoIndex = new FxLabel("");
        btnPagePrev = new FxButton("<");
        btnPageForward = new FxButton(">");
        btnPageBegin.setFiSimpleTooltip("Başa Dön");
        btnPageEnd = new FxButton(">>");

        FxComboBoxObj cmbPageSize = new FxComboBoxObj();
        cmbPageSize.setObjValue(30);
        cmbPageSize.addComboItem(30, "30");
        cmbPageSize.addComboItem(100, "100");
        cmbPageSize.addComboItem(500, "500");
        cmbPageSize.addComboItem(1000, "1000");
        cmbPageSize.addComboItem(2000, "2000");
        cmbPageSize.addComboItem(5000, "5000");
        cmbPageSize.setSelectedItemByObjValueFi();

        cmbPageSize.trigSelectedItemListenerFi((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getValue() instanceof Integer) {
                setLnPageSizeAndLnCurrentPageNo((Integer) newValue.getValue(), 1);
                if (getFnPageChanged() != null) getFnPageChanged().run();
            }
        });

        FxMigPane paneTableHeader = getFxTableMig().getPaneTablePagingHeader();

        paneTableHeader.add(btnPageBegin);
        paneTableHeader.add(btnPagePrev);
        paneTableHeader.add(lblPageNoIndex);
        paneTableHeader.add(btnPageForward);
        paneTableHeader.add(btnPageEnd);
        paneTableHeader.add(cmbPageSize);

        //setLnPageNo(1);
        //if (getLnPageSize()==null) {setLnPageSize(30);}

        updatePageToolbarComps();

        // Page 1 : 1-20 : 1 + 20 -1 = 20 lastIndex
        // Page 2 : 21-40 : 21 + 20 - 1 = 40 lastIndex
        // 21 + 20 = 41

        btnPageBegin.setOnAction(event -> {
            //setLnPageStartIndex(1); // 21-20 = 1
            setLnCurrentPageNoAndPageStartIndex(1);
            updatePageToolbarComps();
            if (getFnPageChanged() != null) getFnPageChanged().run();
            if (getFnPageAction() != null)
                getFnPageAction().accept(getLnPageStartIndexInit(), getLnPageStartIndexInit() + getLnPageSizeInit() - 1);
        });

        btnPagePrev.setOnAction(event -> {
            if (getLnCurrentPageNoInit() == 1) return;
            if (getLnCurrentPageNoInit() > 1) {
                setLnCurrentPageNoAndPageStartIndex(getLnCurrentPageNoInit() - 1);
                updatePageToolbarComps();
                if (getFnPageAction() != null)
                    getFnPageAction().accept(getLnPageStartIndexInit(), getLnPageStartIndexInit() + getLnPageSizeInit() - 1);
                if (getFnPageChanged() != null) getFnPageChanged().run();
            }
        });

        btnPageForward.setOnAction(event -> {
            if (getLnCurrentPageNoInit() < calcLnLastPageNo()) {
                setLnCurrentPageNoAndPageStartIndex(getLnCurrentPageNoInit() + 1);
                updatePageToolbarComps();
                if (getFnPageChanged() != null) getFnPageChanged().run();
                if (getFnPageAction() != null)
                    getFnPageAction().accept(getLnPageStartIndexInit(), getLnPageStartIndexInit() + getLnPageSizeInit() - 1);
            }
        });

        btnPageEnd.setOnAction(event -> {
            //lnPageNo = (getLnTotalSize() / getLnPageSize()) + 1;
            if (FiObjects.equals(getLnCurrentPageNoInit(), calcLnLastPageNo())) return;

            setLnCurrentPageNoAndPageStartIndex(calcLnLastPageNo());
            //getFnPageAction().accept((lnPageNo - 1) * getLnPageSize() + 1, lnPageNo * lnPageSize);
            updatePageToolbarComps();
            if (getFnPageChanged() != null) getFnPageChanged().run();
            if (getFnPageAction() != null)
                getFnPageAction().accept(getLnPageStartIndexInit(), getLnPageStartIndexInit() + getLnPageSizeInit() - 1);
        });


    }

    public void activateExtraFiltreButton() {

        FxTableMig2 tableMig = getFxTableMig();

        if (tableMig == null) {
            Loghelper.get(getClass()).debug("activateExtraFiltreButton fxTableMig null !!!");
            return;
        }

        if (getBtnExtraFilter() != null) {
            return;
        }

        btnExtraFilter = new FxButton("Tablo Kriterler");

        btnExtraFilter.setOnAction(event -> {

            FiArbFormWindowDiaCont emmFormWindowCont = new FiArbFormWindowDiaCont(null);
            emmFormWindowCont.initCont();
            emmFormWindowCont.addCrudSaveButtonAndAction();

            emmFormWindowCont.getFormMain().setListFormElements(ficsFormElemsHeaderFilterExtra);

            // daha önceden girilen form değerleri yüklenir
            if (getFkbHeaderFilterExtra() != null) {
                emmFormWindowCont.getFormMain().setRefFormFkb(getFkbHeaderFilterExtra());
            }

            emmFormWindowCont.getFormMain().initCont();
            //emmFormWindowCont.getFormMain().setFormTypeSelected(FormType.PlainFormV1);

            emmFormWindowCont.setFnSaveClose(() -> {
                FiKeyBean formAsFkb = emmFormWindowCont.getFormMain().getFormAsFkbNotNullKeys();
                formAsFkb.logParams();
                setFkbHeaderFilterExtra(formAsFkb);
                return Fdr.bui(true);
            });

            emmFormWindowCont.openAsNonModal();

            if (emmFormWindowCont.checkClosedWithDone()) {
                if (getiFxTableCont() != null) {
                    getiFxTableCont().pullTableData();
                }
            }

        });

        getFxTableMig().getPaneTablePagingHeader().add(btnExtraFilter);

    }

    /**
     * RemoteFilter çalışmazdan önce Sayfalama güncellenmesi gereken işlemler
     */
    private void updatePageSystemBeforeRemoteFilter() {
        setLnCurrentPageNoAndPageStartIndex(1);
        updatePageToolbarComps();
    }

    /**
     * Baş,Son Sayfa disabled durumunu günceller
     * <p>
     * İleri,Geri tuşlarının tooltiplerini günceller
     */
    public void updatePageToolbarComps() {

        if (getBtnPageForward() == null) return;

        Platform.runLater(() -> {

            Integer pageCurrent = getLnCurrentPageNoInit();
            //String txPageForwardTooltip = String.valueOf(pageCurrent + 1); // 1/20 =0.1+1 =1+1 =2

            //getBtnPageForward().setFiSimpleTooltip(txPageForwardTooltip);
            getLblPageNoIndex().setText(pageCurrent.toString() + "/" + calcLnLastPageNo().toString());

            //Loghelper.debug(getClass(), "LnPageStartIndex :"+ getLnPageStartIndex());

            // İlk Sayfada Prev ve Begin Disable olur
            if (getLnPageStartIndexInit() == 1) {
                getBtnPagePrev().setDisable(true);
                //getBtnPagePrev().setFiSimpleTooltip("");
                getBtnPageBegin().setDisable(true);
            } else {
                getBtnPagePrev().setDisable(false);
                //String pagePrev = String.valueOf(pageCurrent - 1);
                //getBtnPagePrev().setFiSimpleTooltip(pagePrev);
                getBtnPageBegin().setDisable(false);
            }

            // Loghelper.debug(getClass(), "Page Start Index:" + getLnPageStartIndex().toString());

            // Son sayfada ise PageForward,PageEnd Disable yapılır
            if (FiObjects.equals(getLnCurrentPageNoInit(), calcLnLastPageNo())) {
                getBtnPageForward().setDisable(true);
                getBtnPageEnd().setDisable(true);
            } else {
                getBtnPageForward().setDisable(false);
                getBtnPageEnd().setDisable(false);
            }

        });

    }

    public String getHeaderFilterNodeStyleClass() {
        //headerFilterNodeStyleClass
        return "tblHeaderFilter";
    }

    public String getHeaderSummaryClass() {
        return "tblHeaderSummary";
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

    public Integer getLnPageSizeInit() {
        if (lnPageSize == null) {
            lnPageSize = 30;
        }
        return lnPageSize;
    }

//    public Integer calcLnPageCurrentNo() {
//        return (getLnPageStartIndexInit() - 1) / getLnPageSizeInit() + 1; // (21 sayfa) -1=20/20pagesize=1+1 = 2 sayfa
//    }

    public Integer calcLnLastPageNo() {
        // Total
        // Total Size : 20 -> 19 / 20 + 1= 0.9 , 19 / 20
        if (getLnTotalSize() == 0) return 1;
        return Math.floorDiv((getLnTotalSize() - 1), getLnPageSizeInit()) + 1;
    }

//    public Integer calcLnLastPageNoV1() {
//        return (getLnTotalSize() - 1) / getLnPageSizeInit() + 1;
//    }

    public Integer calcLnPageLastIndex() {
        // 1 -> 1+20-1=20
        // return getLnPageStartIndexInit() +  getLnPageSizeInit() - 1;
        // 1 -> 20 , 2-> 40
        //return getLnCurrentPageNoInit() * getLnPageSizeInit();
        return getLnPageStartIndexInit() + getLnPageSizeInit() - 1;
    }

    public void setLnPageSize(Integer lnPageSize) {
        this.lnPageSize = lnPageSize;
    }

    @Deprecated
    public BiConsumer<Integer, Integer> getFnPageAction() {
        return fnPageAction;
    }

    @Deprecated
    public void setFnPageAction(BiConsumer<Integer, Integer> fnPageAction) {
        this.fnPageAction = fnPageAction;
    }

    public Integer getLnTotalSize() {
        if (lnTotalSize == null) {
            lnTotalSize = getLnPageSizeInit();
        }
        return lnTotalSize;
    }

    public void setLnTotalSize(Integer lnTotalSize) {
        this.lnTotalSize = lnTotalSize;
        updatePageToolbarComps();
    }

    public Integer getLnPageStartIndexInit() {
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
        updatePageToolbarComps();
    }


    public FxLabel getLblPageNoIndex() {
        return lblPageNoIndex;
    }

    public void configColSelection(FiCol fiCol) {

//		fiCol.buildFnEditorRenderer((o, node) -> {
//
//			FxCheckBox node1 = (FxCheckBox) node;
//			//Loghelper.get(getClass()).debug("Renderer işledi");
//
//			node1.selectedProperty().addListener((observable, oldValue, newValue) -> {
//				for (Consumer<List<EntClazz>> consumer : getListenerBoSecim()) {
//					consumer.accept(getItemsFiCheckedByBoolFieldAsList(fiCol.getFieldName()));
//				}
//			});
//		});

        fiCol.setFnColCellManualChanged(ent -> {
            for (Consumer<FiCol> consumer : getListenerBoSelection()) {
                consumer.accept(fiCol); //getItemsFiCheckedByBoolFieldAsList(fiCol.getFieldName()
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

    public String getIdNtn() {
        return FiString.orEmpty(getId());
    }

    /**
     * setEnableLocalFilterEditor(true);
     * <p>
     * setEnableSummaryHeader(true);
     * <p>
     * addAllFiColsAuto(fiCols);
     *
     * @param fiCols
     */
    public void standardSetup1(List<FiCol> fiCols) {
        setEnableLocalFilterEditor(true);
        setEnableSummaryHeader(true);
        addAllFiColsAuto(fiCols);
    }

    public EntClazz getItemsCheckedOneItemWitWarn() {
        FilteredList<EntClazz> checkedByBoSelect = getItemsCurrentFiCheckedAsSourceList();

        if (checkedByBoSelect.isEmpty()) {
            FxDialogShow.showPopWarn("Lütfen tablodan bir kayıdı seçiniz.");
            return null;
        } else if (checkedByBoSelect.size() > 1) {
            FxDialogShow.showPopWarn("Lütfen tablodan sadece bir kayıt seçiniz.");
            return null;
        }

        return checkedByBoSelect.get(0);
    }

    public EntClazz getItemsCheckedOneItem() {
        FilteredList<EntClazz> checkedByBoSelect = getItemsCurrentFiCheckedAsSourceList();

        if (checkedByBoSelect.isEmpty()) {
            //FxDialogShow.showPopWarn("Lütfen tablodan bir kayıdı seçiniz.");
            return null;
        } else if (checkedByBoSelect.size() > 1) {
            //FxDialogShow.showPopWarn("Lütfen tablodan sadece bir kayıt seçiniz.");
            return null;
        }

        return checkedByBoSelect.get(0);
    }

    public Runnable getFnPageChanged() {
        return fnPageChanged;
    }

    public void setFnPageChanged(Runnable fnPageChanged) {
        this.fnPageChanged = fnPageChanged;
    }

    public Boolean getBoPagingInitialized() {
        return boPagingInitialized;
    }

    private void setBoPagingInitialized(Boolean boPagingInitialized) {
        this.boPagingInitialized = boPagingInitialized;
    }

    public Integer getLnCurrentPageNoInit() {
        if (lnCurrentPageNo == null) {
            lnCurrentPageNo = 1;
        }
        return lnCurrentPageNo;
    }

    public void setLnCurrentPageNoAndPageStartIndex(Integer lnPageNo) {
        setLnCurrentPageNo(lnPageNo);
        // 1 -> 0*20+1=1 , 2->(2-1)*20+1=21
        setLnPageStartIndex((lnPageNo - 1) * getLnPageSizeInit() + 1);
    }

    /**
     * Ayrıca pageStartIndex'i de günceller.
     *
     * @param lnPageSize
     * @param lnPageNo
     */
    public void setLnPageSizeAndLnCurrentPageNo(Integer lnPageSize, Integer lnPageNo) {
        setLnPageSize(lnPageSize);
        setLnCurrentPageNoAndPageStartIndex(lnPageNo);
    }

    public void setLnCurrentPageNo(Integer lnCurrentPageNo) {
        this.lnCurrentPageNo = lnCurrentPageNo;
    }

    public FxButton getBtnPageEnd() {
        return btnPageEnd;
    }

    public FiKeyBean getFkbHeaderFilterExtra() {
        return fkbHeaderFilterExtra;
    }

    public FiKeyBean getFkbHeaderFilterExtraInit() {
        if (fkbHeaderFilterExtra == null) {
            fkbHeaderFilterExtra = new FiKeyBean();
        }
        return fkbHeaderFilterExtra;
    }

    public void setFkbHeaderFilterExtra(FiKeyBean fkbHeaderFilterExtra) {
        this.fkbHeaderFilterExtra = fkbHeaderFilterExtra;
    }

    public Boolean getBoFkbEnabled() {
        return boFkbEnabled;
    }

    public void setBoFkbEnabled(Boolean boFkbEnabled) {
        this.boFkbEnabled = boFkbEnabled;
    }

    public IFxTableCont getiFxTableCont() {
        return iFxTableCont;
    }

    public void setiFxTableCont(IFxTableCont iFxTableCont) {
        this.iFxTableCont = iFxTableCont;
    }

    public FxButton getBtnExtraFilter() {
        return btnExtraFilter;
    }

    public void setBtnExtraFilter(FxButton btnExtraFilter) {
        this.btnExtraFilter = btnExtraFilter;
    }
}