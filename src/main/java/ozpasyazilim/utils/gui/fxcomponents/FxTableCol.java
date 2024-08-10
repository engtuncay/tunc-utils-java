package ozpasyazilim.utils.gui.fxcomponents;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.reactfx.util.TriConsumer;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * N nedir ?
 *
 * @param <Clazz>
 */
public class FxTableCol<Clazz> extends TableColumn implements IFiCol<Clazz> {

	// FxTable Col a Özel Alanlar

	// /////////////////////////

	// Oztable Coldan
	// String id; // TableColumn da olduğu için çıkartıldı
	private String fieldName;
	private String fiHeader;
	//Integer prefSize;
	private Double prefSize;
	private Integer printSize;
	private OzColType colType;
	private String colComment;
	private Map<String, String> mapStyle;    // ColStyle,String şeklindeydi

	private OzColSummaryType summaryType;
	private Function summaryfunction;

	private Boolean fiEditable;
	private Boolean isHidden;

	// excelden sütunları ayarlar opsiyonel sütunların belinmesi için,
	// opsiyonel sütun demek excelde oladabilir,olmayadabilir. experimental
	private Boolean isOptional;

	private Boolean boIsRequired;

	// excekde sütunun bulunduğunu gösterir
	private Boolean boIsExist;

	// sütun excele aktarılmayacaksa true yapılır
	private Boolean boIsNotExportedExcel;

	// For Forms, entity is edit value for the field
	private Clazz entity;


	// *** Filter Editor Alanları
	private String colFilterNodeClass; // Tablolarda filtre editor sınıfı, deprecated Form Elemanı
	private Boolean colFilterable;
	private Object colFilterValue; // Formlarda değer tutmak için veya default değer atamak için


	// ** Editor İle İlgili Alanlar
	private String colEditorFactoryClass;    // Tablonun editor class için burası kullanılacak
	private Node colEditorFactoryNode; // Editor Node
	private Object colEditorValue;    // Editor tutulan değer saklamak için
	private String colFxEditorNodeText; // Editor kullanılacak comp. button ise button üzerinde yazacağı yazı
	EventHandler<KeyEvent> colEditorEnterFn; // editorde enter basılınca action tanımı
	private BiConsumer<Object, Node> fnEditorNodeRendererAfterLoad;

	private BiConsumer<Object, Node> fnEditorSetOnAction;
	private TriConsumer<Object, Node, FxTableCol> fnEditorSetOnActionWitCol;
	private TriConsumer<Object, Node, Object> fnEditorSetOnActionWitValue;

	private Function<Object, Object> fnEditorNodeValueFormmatter;
	private BiConsumer<Object, Node> fnEditorNodeRenderer; // celfactoryedinoderenderer
	private TriConsumer<Object, Node, FxTableCol> fnEditorNodeRendererWithCol; // celfactoryedinoderenderer
	private TriConsumer<Object, Node, Object> fnEditorNodeRendererWitValue; // celfactoryedinoderenderer

	// cell value dinamik olarak gelmiyor  deprecated
	//private TriFunction<Object, Node,Object,Object> fnEditorSetOnActionWithCellValue; // Entity,Node Comp,Cell Value

	@Deprecated // bi veya tri consumer olanlar kullanılacak
	private Consumer<Clazz> fnEditorSetOnActionWithEntity; // cellfactoryaction idi
	@Deprecated //yukarıdakiler kullanılacak
	private BiConsumer<Clazz, Button> fnColButton;


	// ---- end editor fields

	// ***** Tablo Header ile ilgili Alanlar (Filtre,Summary satırları)
	// Componentlerin ata referansı Node üzerinden tutar
	// Tabloda filtre editorünü refere eder.
	// @deprecated Formlarda componentleri refere eder.
	private Node colFilterNode;
	private EventHandler<KeyEvent> colFilterEnterFn; // Tablo başlığındaki filtre editoründe enter basılınca işlenecek event

	private Parent colHeaderSummaryNode;
	private FxCheckBox summaryFxCheckBox;
	// --> ------------------------------


	// excel kaçıncı sütunda olduğunu gösterir
	private Integer colIndex;

	// experimental
	private Function<Object, String> funcFormatter;
	private Format formatter;

	// For Excel Reading, the field shows whether or not column exists in the excel
	private Boolean colEnabled;


	private FxMigPane fiPaneHeader;
	// oztable dan fxtablecol a convert işlemi buraya kadar yapıldı.


	// Summary Label  // inftable cola eklenmedi
	private FxLabel summaryLabelNode;

	// Fi Editor Value nullable izin veriyor mu?
	private Boolean boNullable;

	// Alanlarda update query oluşturulurken dahil edilmeyecek alanlar
	private Boolean boColNonUpdatable;

	// Formlarda update yapılmayacak alanı gösterir
	private Boolean boNonUpdatableForForm;

	// Sorgular oluştururken alanın primary key alanı olduğun belirtir
	private Boolean boKeyField;


	// Editörlerde , mesela tabloda bu pred e göre disable olmasını engeller veya açar   ( inf ye eklenmedi)
	private Predicate<Clazz> predFiEditorDisable;

	// Editörte bir değişim olursa çağrılır (tablonun hücresi değişiklik olursa bildirilir) (property de çevrilebilir ) , inf ye eklenmedi.
	private Consumer<Clazz> fnColCellChanged;

	private Locale locale = new Locale("tr", "TR");

	private Boolean boDontExportExcelTemplate;

	private IfxNode ifxNodeEditor;


	public FxTableCol() {

	}

	public FxTableCol(String fiHeader) {
		super(fiHeader);
	}

	public FxTableCol(String fieldName, String fiHeader) {
		super(fiHeader);
		this.setOfcTxFieldName(fieldName);
		//this.setId(fieldName);
		this.setOfcTxHeader(fiHeader);
		setCellValueFactory(new PropertyValueFactory<>(fieldName));
	}

	public FxTableCol(String fiHeader, String fieldName, OzColType fiDataType) {
		super(fiHeader);
		this.setOfcTxFieldName(fieldName);
		this.setId(fieldName);
		this.setOfcTxHeader(fiHeader);
		setCellValueFactory(new PropertyValueFactory<>(fieldName));
		setColType(fiDataType);  //setFiDataType
		setAutoFormatter(fiDataType);
	}

	public FxTableCol(FiCol fiTableCol) {
		setValuesFromInfTableCol(fiTableCol);
	}

	public FxTableCol(IFiCol ozTableCol) {
		setValuesFromInfTableCol(ozTableCol);
	}

	public static FxTableCol build(String header, String fieldName) {
		fieldName = fieldName.replaceFirst("__", ".");
		FxTableCol fxTableCol = new FxTableCol(fieldName, header);
		return fxTableCol;
	}

	public static FxTableCol build2(String fieldName, String header) {
		FxTableCol fxTableCol = new FxTableCol(fieldName, header);
		return fxTableCol;
	}

	public static FxTableCol build3(Object fieldName, String header) {
		FxTableCol fxTableCol = new FxTableCol(fieldName.toString(), header);
		return fxTableCol;
	}

	public FxTableCol buildFxNodeClass(String editorClassName) {
		this.setFilterNodeClass(editorClassName);
		return this;
	}

	public FxTableCol buildIsHidden(Boolean isHidden) {
		this.setBoHidden(isHidden);
		return this;
	}

	private void setValuesFromInfTableCol(IFiCol iFiCol) {

		setId(iFiCol.getOfcTxFieldName());
		setOfcTxFieldName(iFiCol.getOfcTxFieldName());
		setOfcTxHeader(iFiCol.getOfcTxHeader());
		if (iFiCol.getPrefSize() != null) setPrefSize(iFiCol.getPrefSize());
		setPrintSize(iFiCol.getPrintSize());
		setColType(iFiCol.getColType());
		if (iFiCol.getColType() == null) iFiCol.setColType(OzColType.String);
		setColComment(iFiCol.getColComment());
		setMapStyle(iFiCol.getMapStyle());
		setSummaryType(iFiCol.getSummaryType());
		setBoEditable(iFiCol.getBoEditable());
		setSummaryCalculateFn(iFiCol.getSummaryCalculateFn());
		setBoHidden(iFiCol.getBoHidden());
		setColEditorClass(iFiCol.getColEditorClass());
		setBoOptional(iFiCol.getBoOptional());
		setBoExist(iFiCol.getBoExist());
		setBoFilterable(iFiCol.getBoFilterable());
		setColEditorNodeText(iFiCol.getColEditorNodeText());
		setFilterNodeClass(iFiCol.getFilterNodeClass());
		setColFilterNode(iFiCol.getColFilterNode());
		setColFilterKeyEvent(iFiCol.getColFilterKeyEvent());
		setColIndex(iFiCol.getColIndex());
		setEntity((Clazz) iFiCol.getEntity());
		setFilterValue(iFiCol.getFilterValue());
		setFuncFormatter(iFiCol.getFuncFormatter());
		setFormatter(iFiCol.getFormatter());
		setBoEnabled(iFiCol.getBoEnabled());
		setFnEditorSetOnActionWithEntity(iFiCol.getFnEditorSetOnActionWithEntity());
		setPaneHeader(iFiCol.getPaneHeader());
		//
		setFnColButton(iFiCol.getFnColButton());

	}

	public void setColType(String txColType) {

		for (OzColType type : OzColType.values()) {
			if (txColType.equals(type.toString())) {
				setColType(type);
			}
		}

	}

	public void setAutoColumnDefault() {
		setText(getOfcTxHeader());
		setCellValueFactory(new PropertyValueFactory<>(getOfcTxFieldName()));
		setId(getOfcTxFieldName());
		setAutoFormatter(getColType());
	}

	public void setAutoColumnNoFormatter() {
		setText(getOfcTxHeader());
		setCellValueFactory(new PropertyValueFactory<>(getOfcTxFieldName()));
		setId(getOfcTxFieldName());
		//setAutoFormatter(dataType);
	}

	public <S> void setAutoFormatter(OzColType dataType) {

		if (getColType() == OzColType.Double) {

			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator('.');
			otherSymbols.setGroupingSeparator(',');
			DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00", otherSymbols);
			//String strnumber = decimalFormatter.format(number);

			setCellFactory(new CellFactoryFormatter<S, Double>(decimalFormatter));
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (getColType() == OzColType.Integer) {

			/*
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator('.');
			otherSymbols.setGroupingSeparator(',');
			DecimalFormat decimalpattern = new DecimalFormat("###,###,###,##0.00", otherSymbols);
			//String strnumber = decimalpattern.format(number);

			// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir. (Callback fonksiyonunu icra eder) Callback<TableColumn<S, T>, TableCell<S, T>>
			setCellFactory(new CellFactoryColFormatter<S, Double>(decimalpattern));
			*/
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (getColType() == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}

	public FxTableCol buiColType(OzColType colType) {
		setColType(colType);
		return this;
	}

	public FxTableCol buiPrefSize(Double prefSize) {
		setPrefSize(prefSize);
		return this;
	}

	public FxTableCol buildPrefSize(Integer prefSize) {
		setPrefSize(prefSize.doubleValue());
		return this;
	}

	/**
	 * Karakter uzunluk Ör 20 Karakter
	 *
	 * @param lengthCharacter
	 * @return
	 */
	public FxTableCol buildPrintSize(Integer lengthCharacter) {
		setPrintSize(lengthCharacter);
		return this;
	}

	public FxTableCol buiSumType(OzColSummaryType summaryType) {
		setSummaryType(summaryType);
		return this;
	}

	public FxTableCol buildColFilterValue(Object fiValue) {
		setFilterValue(fiValue);
		return this;
	}
	public FxTableCol buildColEditorValue(Object fiValue) {
		setColValue(fiValue);
		return this;
	}

	/**
	 * Class Name uzun sınıf ismi kullanılıyor
	 *
	 * @param className
	 * @return
	 */
	public FxTableCol buildColEditorClass(String className) {
		setColEditorClass(className);
		return this;
	}

	public static FxTableCol findColumnByFieldName(List<FxTableCol> listFxCols, String fieldName) {

		if (listFxCols.size() > 0) {

			for (FxTableCol fxTableCol : listFxCols) {
				if (fxTableCol.getOfcTxFieldName().equals(fieldName)) {
					return fxTableCol;
				}
			}
		}
		return null;
	}

	public static FxTableCol2 findColumnByFieldName2(List<FxTableCol2> listFxCols, String fieldName) {

		if (listFxCols.size() > 0) {

			for (FxTableCol2 fxTableCol : listFxCols) {
				if (fxTableCol.getRefFiCol().getOfcTxFieldName().equals(fieldName)) {
					return fxTableCol;
				}
			}
		}
		return null;
	}

	public static FxTableCol findColumnByHeader(List<FxTableCol> listFxCols, String headerText) {

		if (listFxCols.size() > 0) {

			for (FxTableCol fxTableCol : listFxCols) {
				if (fxTableCol.getOfcTxHeader().equals(headerText)) {
					return fxTableCol;
				}
			}
		}
		return null;
	}

	/**
	 * Object --> Entity <br>
	 * Node --> İlgili Component
	 *
	 * @param biConsumer
	 * @return
	 */
	public FxTableCol buildFnEditorSetOnAction(BiConsumer<Object, Node> biConsumer) {
		setFnEditorSetOnAction(biConsumer);
		return this;
	}

	public FxTableCol buildFnEditorSetOnAction(TriConsumer<Object, Node,FxTableCol> triConsumer) {
		setFnEditorSetOnActionWitCol(triConsumer);
		return this;
	}

	/**
	 *
	 * <br> Object : Entity
	 * <br> Node : component
	 * <br> Object : Tablodaki Hücre Objesi , Entity deki fxtalbeCol da belirtilen alanı çekilir.
	 * <br>
	 * @param triConsumer
	 * @return
	 */
	public FxTableCol buildFnEditorSetOnActionWithValue(TriConsumer<Object, Node, Object> triConsumer) {
		setFnEditorSetOnActionWitValue(triConsumer);
		return this;
	}

	public FxTableCol buildFnEditorRenderer(BiConsumer<Object, Node> fnCellFactoryEdiNodeRenderer) {
		//this.fnCellFactoryEdiNodeRenderer = fnCellFactoryEdiNodeRenderer;
		setFnEditorNodeRendererOnLoad(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	public FxTableCol buildFnEditorRenderer(TriConsumer<Object, Node, FxTableCol> fnCellFactoryEdiNodeRenderer) {
		//this.fnCellFactoryEdiNodeRenderer = fnCellFactoryEdiNodeRenderer;
		setFnEditorNodeRendererWithCol(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	public FxTableCol buildFnEditoreRendererWithVal(TriConsumer<Object, Node, Object> fnCellFactoryEdiNodeRenderer) {
		//this.fnCellFactoryEdiNodeRenderer = fnCellFactoryEdiNodeRenderer;
		setFnEditorNodeRendererWitValue(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	public FxTableCol buildFxNodeText(String fxNodeText) {
		setColEditorNodeText(fxNodeText);
		return this;
	}

	public FxTableCol buildColFilterable(Boolean colFilterable) {
		setBoFilterable(colFilterable);
		return this;
	}

	public FxTableCol buildFiEditable(Boolean boFiEditable) {
		setBoEditable(true);
		return this;
	}


	// ************************** Getter and Setter

	@Override
	public BiConsumer<Clazz, Button> getFnColButton() {
		return fnColButton;
	}

	@Override
	public void setFnColButton(BiConsumer<Clazz, Button> fnColButton) {
		this.fnColButton = fnColButton;
	}

	@Override
	public String getOfcTxFieldName() {
		return fieldName;
	}

	@Override
	public void setOfcTxFieldName(String ofcTxFieldName) {
		ofcTxFieldName = ofcTxFieldName.replaceFirst("__", ".");
		this.fieldName = ofcTxFieldName;
		setId(ofcTxFieldName);
	}

	@Override
	public String getOfcTxHeader() {
		return fiHeader;
	}

	@Override
	public void setOfcTxHeader(String ofcTxHeader) {
		this.fiHeader = ofcTxHeader;
	}

//	@Override
//	public Integer getPrefSize() {
//		return prefSize;
//	}
//
//	@Override
//	public void setPrefSize(Integer prefSize) {
//		if (prefSize != null) {
//			setPrefWidth(prefSize);
//		}
//		this.prefSize = prefSize;
//	}

	@Override
	public Double getPrefSize() {
		return prefSize;
	}

	@Override
	public void setPrefSize(Double prefSize) {
		if (prefSize != null) {
			setPrefWidth(prefSize);
		}
		this.prefSize = prefSize;
	}

	@Override
	public Integer getPrintSize() {
		return printSize;
	}

	@Override
	public void setPrintSize(Integer printSize) {
		this.printSize = printSize;
	}

	@Override
	public OzColType getColType() {
		return colType;
	}

	@Override
	public void setColType(OzColType colType) {
		this.colType = colType;
	}

	@Override
	public String getColComment() {
		return colComment;
	}

	@Override
	public void setColComment(String colComment) {
		this.colComment = colComment;
	}

	@Override
	public Map<String, String> getMapStyle() {
		return mapStyle;
	}

	@Override
	public void setMapStyle(Map<String, String> mapStyle) {
		this.mapStyle = mapStyle;
	}

	@Override
	public OzColSummaryType getSummaryType() {
		return summaryType;
	}

	@Override
	public void setSummaryType(OzColSummaryType summaryType) {
		this.summaryType = summaryType;
	}

	@Override
	public Boolean getBoEditable() {
		return fiEditable;
	}

	@Override
	public void setBoEditable(Boolean boEditable) {
		if (boEditable != null) super.setEditable(boEditable);
		this.fiEditable = boEditable;
	}

	@Override
	public Function getSummaryCalculateFn() {
		return summaryfunction;
	}

	@Override
	public void setSummaryCalculateFn(Function summaryCalculateFn) {
		this.summaryfunction = summaryCalculateFn;
	}

	@Override
	public Boolean getBoHidden() {
		return isHidden;
	}

	@Override
	public void setBoHidden(Boolean hidden) {
		isHidden = hidden;
	}

	@Override
	public String getColEditorClass() {
		return colEditorFactoryClass;
	}

	@Override
	public void setColEditorClass(String colEditorClass) {
		this.colEditorFactoryClass = colEditorClass;
	}

	@Override
	public Boolean getBoOptional() {
		return isOptional;
	}

	@Override
	public void setBoOptional(Boolean optional) {
		isOptional = optional;
	}

	@Override
	public Boolean getBoExist() {
		return boIsExist;
	}

	@Override
	public void setBoExist(Boolean exist) {
		boIsExist = exist;
	}

	@Override
	public Boolean getBoFilterable() {
		return colFilterable;
	}

	@Override
	public void setBoFilterable(Boolean boFilterable) {
		this.colFilterable = boFilterable;
	}

	@Override
	public String getColEditorNodeText() {
		return colFxEditorNodeText;
	}

	@Override
	public void setColEditorNodeText(String colEditorNodeText) {
		this.colFxEditorNodeText = colEditorNodeText;
	}

	@Override
	public String getFilterNodeClass() {
		return colFilterNodeClass;
	}

	@Override
	public void setFilterNodeClass(String filterNodeClass) {
		this.colFilterNodeClass = filterNodeClass;
	}

	/**
	 * Tabloda filtre editorü için kullanılıyor
	 *
	 * @return
	 */
	@Override
	public Node getColFilterNode() {
		return colFilterNode;
	}

	@Override
	public void setColFilterNode(Node colFilterNode) {
		this.colFilterNode = colFilterNode;
	}

	@Override
	public EventHandler<KeyEvent> getColFilterKeyEvent() {
		return colFilterEnterFn;
	}

	@Override
	public void setColFilterKeyEvent(EventHandler<KeyEvent> colFilterKeyEvent) {
		this.colFilterEnterFn = colFilterKeyEvent;
	}

	@Override
	public Integer getColIndex() {
		return colIndex;
	}

	@Override
	public void setColIndex(Integer colIndex) {
		this.colIndex = colIndex;
	}

	@Override
	public Clazz getEntity() {
		return entity;
	}

	@Override
	public void setEntity(Clazz entity) {
		this.entity = entity;
	}

	@Override
	public Object getFilterValue() {
		return colFilterValue;
	}

	@Override
	public void setFilterValue(Object filterValue) {
		this.colFilterValue = filterValue;
	}

	@Override
	public Function<Object, String> getFuncFormatter() {
		return funcFormatter;
	}

	@Override
	public void setFuncFormatter(Function<Object, String> funcFormatter) {
		this.funcFormatter = funcFormatter;
	}

	@Override
	public Format getFormatter() {
		return formatter;
	}

	@Override
	public void setFormatter(Format formatter) {
		this.formatter = formatter;
	}

	@Override
	public Boolean getBoEnabled() {
		return colEnabled;
	}

	@Override
	public void setBoEnabled(Boolean boEnabled) {
		this.colEnabled = boEnabled;
	}

	@Deprecated
	@Override
	public Consumer<Clazz> getFnEditorSetOnActionWithEntity() {
		return fnEditorSetOnActionWithEntity;
	}

	@Override
	public void setFnEditorSetOnActionWithEntity(Consumer<Clazz> fnEditorSetOnActionWithEntity) {
		this.fnEditorSetOnActionWithEntity = fnEditorSetOnActionWithEntity;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public OzColType getFiDataType() {
		return this.colType;
	}

	public void setOzTableCol(IFiCol ozTableCol) {
		setValuesFromInfTableCol(ozTableCol);
	}

	public FxMigPane getPaneHeader() {
		return fiPaneHeader;
	}

	public void setPaneHeader(FxMigPane paneHeader) {
		this.fiPaneHeader = paneHeader;
	}

	@Override
	public FxTableCol buiHeader(String header) {
		this.fiHeader = header;
		return this;
	}

	public void setFiHeaderAsVbox(FxMigPane vboxHeader, FxTableCol fxTableCol) {
		setPaneHeader(vboxHeader);
		setText(fxTableCol.getOfcTxHeader());
		setGraphic(vboxHeader);
	}

	public BiConsumer<Object, Node> getFnEditorSetOnAction() {
		return fnEditorSetOnAction;
	}

	public void setFnEditorSetOnAction(BiConsumer<Object, Node> fnEditorSetOnAction) {
		this.fnEditorSetOnAction = fnEditorSetOnAction;
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererBeforeSettingValue() {
		return fnEditorNodeRenderer;
	}

	public void setFnEditorNodeRendererOnLoad(BiConsumer<Object, Node> fnEditorNodeRendererOnLoad) {
		this.fnEditorNodeRenderer = fnEditorNodeRendererOnLoad;
	}

	public FxLabel getSummaryLabelNode() {
		return summaryLabelNode;
	}

	public void setSummaryLabelNode(FxLabel summaryLabelNode) {
		this.summaryLabelNode = summaryLabelNode;
	}

	@Override
	public Boolean getBoNullable() {
		return boNullable;
	}

	@Override
	public void setBoNullable(Boolean boNullable) {
		this.boNullable = boNullable;
	}

	@Override
	public Boolean getBoNonUpdatable() {
		return boColNonUpdatable;
	}

	@Override
	public void setBoNonUpdatable(Boolean boNonUpdatable) {
		this.boColNonUpdatable = boNonUpdatable;
	}

	@Override
	public Boolean getBoKeyIdField() {
		return boKeyField;
	}

	@Override
	public void setBoKeyIdField(Boolean boKeyIdField) {
		this.boKeyField = boKeyIdField;
	}

	@Override
	public Boolean getBoNonEditableForForm() {
		return boNonUpdatableForForm;
	}

	@Override
	public void setBoNonEditableForForm(Boolean boNonEditableForForm) {
		this.boNonUpdatableForForm = boNonEditableForForm;
	}


	public Predicate<Clazz> getPredFiEditorDisable() {
		return predFiEditorDisable;
	}

	/**
	 * N : entity object , cast yapılarak alınabilir
	 * <br> true olursa disable yapar , false yapmaz
	 *
	 * @param predFiEditorDisable
	 */
	public void setPredFiEditorDisable(Predicate<Clazz> predFiEditorDisable) {
		this.predFiEditorDisable = predFiEditorDisable;
	}

	public Consumer<Clazz> getFnColCellChanged() {
		return fnColCellChanged;
	}

	public void setFnColCellChanged(Consumer<Clazz> fnColCellChanged) {
		this.fnColCellChanged = fnColCellChanged;
	}

	public Parent getColHeaderSummaryNode() {
		return colHeaderSummaryNode;
	}

	public void setColHeaderSummaryNode(Parent colHeaderSummaryNode) {
		this.colHeaderSummaryNode = colHeaderSummaryNode;
	}

	public FxCheckBox getSummaryFxCheckBox() {
		return summaryFxCheckBox;
	}

	public void setSummaryFxCheckBox(FxCheckBox summaryFxCheckBox) {
		this.summaryFxCheckBox = summaryFxCheckBox;
	}

	public void fiStyleAlignCenter() {
		setStyle("-fx-alignment: CENTER");
	}

	public Boolean getBoIsNotExportedExcel() {
		if (boIsNotExportedExcel == null) return false;
		return boIsNotExportedExcel;
	}

	public void setBoIsNotExportedExcel(Boolean boIsNotExportedExcel) {
		this.boIsNotExportedExcel = boIsNotExportedExcel;
	}

	public FxTableCol buildDoNotExportExcel(Boolean doNotExportExcel) {
		this.boIsNotExportedExcel = doNotExportExcel;
		return this;
	}

	public Node getColEditorNode() {
		return colEditorFactoryNode;
	}

	public void setColEditorNode(Node colEditorNode) {
		this.colEditorFactoryNode = colEditorNode;
	}

//	public Object getColEditorValue() {
//		return colEditorValue;
//	}
//
//	public void setColEditorValue(Object colEditorValue) {
//		this.colEditorValue = colEditorValue;
//	}

	public String getColFxEditorNodeText() {
		return colFxEditorNodeText;
	}

	public void setColFxEditorNodeText(String colFxEditorNodeText) {
		this.colFxEditorNodeText = colFxEditorNodeText;
	}

//	public Node getColFxNode() {return colFxNode;}
//
//	public void setColFxNode(Node colFxNode) {this.colFxNode = colFxNode;}


	public TriConsumer<Object, Node, FxTableCol> getFnEditorSetOnActionWitCol() {
		return fnEditorSetOnActionWitCol;
	}

	public void setFnEditorSetOnActionWitCol(TriConsumer<Object, Node, FxTableCol> fnEditorSetOnActionWitCol) {
		this.fnEditorSetOnActionWitCol = fnEditorSetOnActionWitCol;
	}

	public TriConsumer<Object, Node, FxTableCol> getFnEditorNodeRendererWithCol() {
		return fnEditorNodeRendererWithCol;
	}

	public void setFnEditorNodeRendererWithCol(TriConsumer<Object, Node, FxTableCol> fnEditorNodeRendererWithCol) {
		this.fnEditorNodeRendererWithCol = fnEditorNodeRendererWithCol;
	}

	public TriConsumer<Object, Node, Object> getFnEditorSetOnActionWitValue() {return fnEditorSetOnActionWitValue;}

	public void setFnEditorSetOnActionWitValue(TriConsumer<Object, Node, Object> fnEditorSetOnActionWitValue) {
		this.fnEditorSetOnActionWitValue = fnEditorSetOnActionWitValue;
	}

	public TriConsumer<Object, Node, Object> getFnEditorNodeRendererWitValue() {return fnEditorNodeRendererWitValue;}

	public void setFnEditorNodeRendererWitValue(TriConsumer<Object, Node, Object> fnEditorNodeRendererWitValue) {
		this.fnEditorNodeRendererWitValue = fnEditorNodeRendererWitValue;
	}

	public Boolean getBoRequired() {return boIsRequired;}

	public void setBoRequired(Boolean boRequired) {this.boIsRequired = boRequired;}

	@Override
	public EventHandler<KeyEvent> getColEditorKeyEvent() {return colEditorEnterFn;}

	@Override
	public void setColEditorKeyEvent(EventHandler<KeyEvent> colEditorKeyEvent) {this.colEditorEnterFn = colEditorKeyEvent;}

	@Override
	public Function<Object, Object> getFnEditorNodeValueFormmatter() {return fnEditorNodeValueFormmatter;}


//	public void setFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter) {this.fnEditorNodeValueFormmatter = fnEditorNodeValueFormmatter;}

	@Override
	public BiConsumer<Object, Node> getFnEditorNodeRendererAfterFormLoad() {return fnEditorNodeRendererAfterLoad;}

	@Override
	public void setFnEditorNodeRendererAfterFormLoad(BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad) {this.fnEditorNodeRendererAfterLoad = fnEditorNodeRendererAfterFormLoad;}

	@Override
	public Boolean equalsColType(OzColType ozColType) {
		if( getColType()==null) return false;
		return getColType()==ozColType;
	}

	@Override
	public Boolean getBoDontExportExcelTemplate() {return boDontExportExcelTemplate;}

	@Override
	public void setBoDontExportExcelTemplate(Boolean boDontExportExcelTemplate) {this.boDontExportExcelTemplate = boDontExportExcelTemplate;}

	public IfxNode getIfxNodeEditor() {
		return ifxNodeEditor;
	}

	@Override
	public Object getColValue() {
		return colEditorValue;
	}

	@Override
	public void setColValue(Object colValue) {
		this.colEditorValue = colValue;
	}
	public void setIfxNodeEditor(IfxNode ifxNodeEditor) {
		this.ifxNodeEditor = ifxNodeEditor;
	}

}
