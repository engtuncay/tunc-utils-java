package ozpasyazilim.utils.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import org.reactfx.util.TriConsumer;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.returntypes.Fdr;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * EntClazz Entity
 * <br>
 * Not: daha sonra <N,T> formuna dönüştürülebilir.N Entity Türünü, T alanın tipini gösterir.
 * <br>
 * (field type of column. for ex. string , bool , date ...)
 *
 * @param <EntClazz>
 */

public class FiCol<EntClazz> implements IFiCol<EntClazz> {

	//String id;

	// Alanın ismini (veritabanındaki veya objedeki refere ettiği alan ismi )
	private String fieldName;

	// Alanın başlık açıklaması ( tablo için sütün başlığı , form için label alanı değeri / excelde başlık )
	private String headerName;

	private String txLabel;

	/**
	 * Objedeki alan adı (fieldName) ile db deki alan adı aynı degilse kullanılır.
	 */
	private String txDbFieldName;

	/**
	 * Col Id olması için konuldu - tekil kodu
	 */
	private String txGuid;

	private ObjectProperty<Double> prefSize;

	private Integer printSize;

	// Alanın türünü belirtir (double,string,date vs )
	private OzColType colType;

	/**
	 * Column Generic Type. Sütun nasıl bir tipte olduğunu gösterir. (Data Tipi degil)
	 * <p>
	 * Örneğin , Xml parse edilirken, alanın xmlAttribute türünde olduğunu gösterir.
	 */
	private OzColType colGenType;

	// Formlarda default true olarak çalışır, false olursa düzenleme izni vermez
	private Boolean boEditable;

	// Formlarda gösterilmeyeceğini belirtir
	private Boolean boHidden;

	// excelden sütunları ayarlarken opsiyonel sütunların belinmesi için (zorunlu degil) , vs.. (boRequired:false da kullanılabilirdi.)
	private Boolean boOptional;

	// excelde sütunun bulunduğunu gösterir
	private Boolean boExist;

	// Excel için true olursa sütunun olması gerektiğini gösterir
	private Boolean boRequired;

	// For Excel Reading, the field shows whether or not column exists in the excel
	private Boolean boEnabled;

	// For Forms, entity is edit value for the field
	private EntClazz entity;

	// Filter Component tutulur
	Node colFilterNode;

	EventHandler<KeyEvent> colFilterKeyEvent;

	// -- FxTable Header Comp ile İlgili Alanlar

	// FxTable'da header pane (başlık componenti)
	FxMigPane paneHeader;

	// ---- Filter Node ile ilgili Alanlar
	private Object filterValue;

	// Tablo başlıklarındaki filtre için ve formlar daki comp.ler için
	private Boolean boFilterable;

	// Alan için hangi tür filter node kullanılacaksa sınıfın ismi tutulur
	String filterNodeClass;

	// experimental
	private Function<Object, String> funcFormatter;

	private Format formatter;

	// --Table Editor ile İgli Alanlar
	private String colEditorClass;
	private Node colEditorNode;
	private Object colValue;
	// Editörde kullanılacak component kullanılack yazı , örneğin button ise üzerine yazılacak text
	private String colEditorNodeText;
	EventHandler<KeyEvent> colEditorKeyEvent; // editorde enter basılınca action tanımı

	// ---- Editor Action
	private Consumer<EntClazz> fnEditorSetOnActionWithEntity;
	private BiConsumer<Object, Node> fnEditorSetOnAction;
	private TriConsumer<Object, Node, FxTableCol> fnEditorSetOnActionWitCol;
	private TriConsumer<Object, Node, Object> fnEditorSetOnActionWitValue;

	// entity giren, value çıkan -- comp üzerinde yazan (text) değeri formatlamak için , (arka plan değeri değiştirmez (txValue su varsa eğer)
	private Function<Object, Object> fnEditorNodeValueFormmatter;

	// ---- Editor Renderer

	// Node comp için özel renderer yapacaksak bu fonksiyonu kulanırız. (formlarda alanlar için, tabloda editor için)
	private BiConsumer<Object, Node> fnEditorNodeRendererBeforeSettingValue;
	private BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue1;
	private BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue2;

	// Form yüklendikten (After Setup) sonra çalıştırılacak
	// Editorler için , after form load, it executes
	// Object olarak form için kullanılacak entity gönderilir
	// Node : component için kullanılır (FxTextField,FxCombobox gibi)
	private BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad;

	private BiConsumer<Object, Node> fnEditorNodeAfterChangeForForm;

	private TriConsumer<Object, Node, FxTableCol2> fnEditorNodeRendererWithCol; // celfactoryedinoderenderer
	private TriConsumer<Object, Node, Object> fnEditorNodeRendererWitValue; // celfactoryedinoderenderer

	// Editörlerde , mesela tabloda bu pred e göre disable olmasını sağlar ( ifc ye eklenmedi)
	private Predicate<EntClazz> predFiEditorDisable;

	// --Excel İle İlgili Alanlar

	// excel için kaçıncı sütunda olduğunu gösterir
	Integer colIndex;

	// Xml okumalar için
	List<FiCol> listChildCol;
	Class childClazz;

	// Hangi Tablonun veya Sınıfın Alanı olduğunu belirtir
	Class<EntClazz> entClass;

	// Excelde, sütunun yorum balonunda gösterilecek açıklamayı belirtir
	private String colComment;

	// ColStyle,String şeklindeydi
	private Map<String, String> mapStyle;

	// --Form ile İlgili Alanlar


	// -- FxTableView için alanlar

	private BiConsumer<EntClazz, Button> fnColButton;

	// ****** Sql Sorgular ile ilgili alanlar

	// Fi Editor Value nullable izin veriyor mu?
	private Boolean boNullable;

	/**
	 * Alanlarda update query oluşturulurken dahil edilmeyecek alanlar
	 */
	private Boolean boNonUpdatable;

	// Formlarda edit yapılmayacak alanı gösterir
	private Boolean boNonEditableForForm;

	// ****** SORGULAR İÇİN OLUŞTURULAN ALANLAR *******

	// Genel olarak parametre ismi fieldName seçilir , farklı seçilecekse bu alandaki değer kullanılır.
	private String txParamName;

	/**
	 * Sorgular için, alanın primary key alanı olduğunu belirtir
	 */
	private Boolean boKeyField;

	/**
	 * Sorgu hazırlanırken update olacak alan olduğunu gösterir
	 */
	private Boolean boUpdateField;

	/**
	 * True olur Sorguda Aktif Edilmesini
	 * <p>
	 * False olursa Pasif Edilmesini gösterir
	 */
	private Boolean boParamStatus;

	// ------ End - Sorgular için --------------


	// ***** FxTable ve FxTreeTable ile İlişkilendirmeleri *****
	private ObjectProperty<TableColumn> tableColumnFx = new SimpleObjectProperty<>();
	private ObjectProperty<TreeTableColumn> fxTreeTableCol = new SimpleObjectProperty<>();
	// Exprerimental - çıkartılabilir
	private FxTableCol2<EntClazz> fxTableCol2;

	// yeni 02-10-19

	/**
	 * Editörte bir değişim olursa çağrılır (tablonun hücresi değişiklik olursa bildirilir) (property de çevrilebilir ) , inf ye eklenmedi.
	 */
	private Consumer<EntClazz> fnColCellManualChanged;

	// -- header summary node ile ilgili alanlar
	private FxLabel summaryLabelNode; // summary Label  // inftable cola eklenmedi
	private FxCheckBox summaryCheckBox;
	private Node summaryNode;
	// Tablolarda özet alanının türünü belirtir (toplam,ortalama vs. gibi )
	private OzColSummaryType summaryType;
	private Function summaryCalculateFn;

	private Boolean boDontExportExcel;
	private Boolean boDontExportExcelTemplate;

	private IfxNode ifxNodeEditor;

	// filter node like araması yapılacak mı
	private Boolean boFilterLike;
	private TriConsumer<Object, Node, List<FiCol>> fnEditorNodeRendererAfterFormLoad2;

	// formlarda compenete focus olunca trigger eder
	private Consumer<Node> fnNodeFocusTrigger;

	// Sütun veya Alan için Sayısal karşılık saklamak için, canceled için 1 gibi
	private Integer lnCode;

	// sütun veya form alanı için validate fonksiyonu, object olarak form alanınına değeri gönderilir (draft)
	private Function<Object, Fdr> fnValidate;

	public FiCol() {
		// Table Column için yapılacak atamalar
		setupFiCol();
	}

	// ***** Constructors

	public FiCol(String fieldName, String headerName) {
		this.headerName = headerName;
		this.fieldName = fieldName;
		setupFiCol();
	}

	public FiCol(String fieldName) {
		this.fieldName = fieldName;
		setupFiCol();
	}

	public FiCol(String fieldName, String headerName, String colComment) {
		this.headerName = headerName;
		this.fieldName = fieldName;
		setColComment(colComment);
		setupFiCol();
	}

	public FiCol(String headerName, Object fieldName) {
		this.headerName = headerName;
		this.fieldName = fieldName.toString();
		setupFiCol();
	}

	public FiCol(Object fieldName, String headerName, OzColType colType) {
		this.headerName = headerName;
		if (fieldName != null) this.fieldName = fieldName.toString();
		this.setColType(colType);
		setupFiCol();
	}

	public FiCol(Object fieldName, String headerName, OzColType colType, String colComment) {
		this.headerName = headerName;
		if (fieldName != null) this.fieldName = fieldName.toString();
		setColType(colType);
		setColComment(colComment);
		setupFiCol();
	}

	public static FiCol buildo(Object objFieldName) {
		return new FiCol(objFieldName.toString());
	}

	public static List<FiCol> convertListFiField(List<FiField> listFiFieldsSummary) {

		List<FiCol> fiTableColList = new ArrayList<>();

		for (FiField fiField : listFiFieldsSummary) {
			if (FiBoolean.isTrue(fiField.getBoExcludeFromAutoColList())) continue;
			FiCol fiTableCol = new FiCol(fiField.getName(), fiField.getName());
			fiTableCol.setColType(convertOzColType(fiField.getClassNameSimple()));
			fiTableColList.add(fiTableCol);
		}
		return fiTableColList;
	}

	public static OzColType convertOzColType(String classNameSimple) {

		for (OzColType value : OzColType.values()) {

			if (value.toString().equals(classNameSimple)) {
				return value;
			}
		}

		return null;
	}

	// build Methods

	public FiCol buildHeader(String header) {
		this.setHeaderName(header);
		return this;
	}


	public FiCol buildNullable(Boolean nullable) {
		this.setBoNullable(nullable);
		return this;
	}

	public FiCol buildOptional(Boolean isOptional) {
		this.setBoOptional(isOptional);
		return this;
	}

	public FiCol buildFilterNodeClass(String editorClassName) {
		this.setFilterNodeClass(editorClassName);
		return this;
	}

	public FiCol buildEditorNodeClass(String editorClassName) {
		this.setColEditorClass(editorClassName);
		return this;
	}

	public FiCol buildEditorNodeClass(Class editorClass) {
		this.setColEditorClass(editorClass.getName());
		return this;
	}

	/**
	 * Formlarda Gösterilmemesini sağlar
	 *
	 * @param isHidden
	 * @return
	 */
	public FiCol buiBoHidden(boolean isHidden) {
		this.setBoHidden(isHidden);
		return this;
	}

	public FiCol buildKeyField(boolean boKeyField) {
		this.setBoKeyField(boKeyField);
		return this;
	}

	/**
	 * Formlarda node component üretildikten sonra bu fonksiyon ile özelleştirmeleri yapılır. TextfieldButton mesela
	 * , button özelliği ayarlanır.
	 * <p>
	 * Editor Comp :
	 * <p>
	 * Formlarda editor componenti
	 * <p>
	 * Tablolarda ?? satırlar için üretilen prototip component
	 *
	 * @param fnCellFactoryEdiNodeRenderer
	 * @return
	 */
	public FiCol buildFnEditorRenderer(BiConsumer<Object, Node> fnCellFactoryEdiNodeRenderer) {
		setFnEditorNodeRendererOnLoad(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	/**
	 * Formlarda componenta değeri basılmazdan önce,
	 * bu fonksiyona  entity gönderilir, string bir değer alınır ve componenta basılır.
	 *
	 * @return
	 */
	public FiCol buildFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter) {
		setFnEditorNodeValueFormmatter(fnEditorNodeValueFormmatter);
		return this;
	}

	public FiCol buildFnEditorRenderer(TriConsumer<Object, Node, FxTableCol2> fnCellFactoryEdiNodeRenderer) {
		setFnEditorNodeRendererWithCol(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	public FiCol buildFnEditoreRendererWithVal(TriConsumer<Object, Node, Object> fnCellFactoryEdiNodeRenderer) {
		setFnEditorNodeRendererWitValue(fnCellFactoryEdiNodeRenderer);
		return this;
	}

	public static FiCol build(String header, String fieldName) {
		return new FiCol(fieldName, header);
	}

	public static FiCol build(String fieldName) {
		return new FiCol(fieldName, fieldName);
	}

	public static FiCol build(String header, Object fieldName) {
		return new FiCol(header, fieldName);
	}

	public static FiCol buildo(Object fieldName, String header, OzColType colType) {
		return new FiCol(fieldName, header, colType);
	}

	public FiCol buildColType(OzColType colType) {
		setColType(colType);
		return this;
	}

	public FiCol buildColGenType(OzColType colType) {
		setColGenType(colType);
		return this;
	}

	public FiCol buildPrefSize(Double prefSize) {
		setPrefSize(prefSize);
		return this;
	}

	public FiCol buildPrefSize(Integer prefSize) {
		if (prefSize == null) setPrefSize(null);
		setPrefSize(prefSize.doubleValue());
		return this;
	}

	public FiCol buildPrefSizeSet(Double prefSize) {
		setPrefSize(prefSize);
		return this;
	}

	/**
	 * Karakter olarak uzunluğu verilir.
	 *
	 * @param charLength
	 * @return
	 */
	public FiCol buildPrintSize(Integer charLength) {
		this.setPrintSize(charLength);
		return this;
	}


	public FiCol buildSumType(OzColSummaryType summaryType) {
		setSummaryType(summaryType);
		return this;
	}

	public FiCol buildNonEditable(boolean boNonEditable) {
		setBoNonEditableForForm(boNonEditable);
		return this;
	}

	public FiCol buildComment(String txComment) {
		setColComment(txComment);
		return this;
	}

	public FiCol buildFiEditable(Boolean boEditable) {
		setBoEditable(boEditable);
		return this;
	}

//	public FiTableCol buildFiEditable(Boolean editable) {
//		this.setFiEditable(editable);
//		return this;
//	}

	public FiCol buildDontExportExcel(Boolean boDoNotExportExcel) {
		setBoDontExportExcel(boDoNotExportExcel);
		return this;
	}

	public FiCol buildDontExportExcelTemplate(Boolean boDontExportExcelTemplate) {
		setBoDontExportExcelTemplate(boDontExportExcelTemplate);
		return this;
	}

	public FiCol buildColFilterValue(Object colFilterValue) {
		setFilterValue(colFilterValue);
		return this;
	}

	public FiCol buiColEditorValue(Object colFilterValue) {
		setColEditorValue(colFilterValue);
		return this;
	}


	/**
	 * Alanın zorunlu doldurulması gerektiğini belirtir (Formlar için)
	 *
	 * @param boIsRequired
	 * @return
	 */
	public FiCol buiBoIsRequired(Boolean boIsRequired) {
		setBoRequired(boIsRequired);
		return this;
	}

	public FiCol buiBoKeyField(Boolean boKeyField) {
		setBoKeyField(boKeyField);
		return this;
	}

	public FiCol buildColEditorClass(String name) {
		setColEditorClass(name);
		return this;
	}


	/**
	 * @param fnEditorNodeRendererAfterLoad
	 * @return
	 */
	public FiCol buildAfterFormLoadRenderer(BiConsumer<Object, Node> fnEditorNodeRendererAfterLoad) {
		this.fnEditorNodeRendererAfterFormLoad = fnEditorNodeRendererAfterLoad;
		return this;
	}

	public FiCol buildAfterFormLoadRenderer2(TriConsumer<Object, Node, List<FiCol>> fnEditorNodeRendererAfterLoad2) {
		this.fnEditorNodeRendererAfterFormLoad2 = fnEditorNodeRendererAfterLoad2;
		return this;
	}

	public FiCol buildFieldName(String fieldName) {
		this.fieldName = fieldName;
		return this;
	}

	public FiCol buildColFilterable(Boolean boColFilterable) {
		setBoFilterable(boColFilterable);
		return this;
	}

	public FiCol buildFiEditorDisabled(Predicate<EntClazz> predFiEditorDisable) {
		setPredFiEditorDisable(predFiEditorDisable);
		return this;
	}

	public void doNodeOperationsBeforeSettingValue(Object entity, Node comp) {
		if (getFnEditorNodeRendererBeforeSettingValue() != null) {
			getFnEditorNodeRendererBeforeSettingValue().accept(entity, comp);
		}
	}

	public void doNodeOperationsAfterInitialValue1(Object entity, Node comp) {
		if (getFnEditorNodeRendererAfterInitialValue1() != null) {
			getFnEditorNodeRendererAfterInitialValue1().accept(entity, comp);
		}
	}

	public void doNodeOperationsAfterInitialValue2(Object entity, Node comp) {
		if (getFnEditorNodeRendererAfterInitialValue2() != null) {
			getFnEditorNodeRendererAfterInitialValue2().accept(entity, comp);
		}
	}

	public FiCol buildBoFilterLike(Boolean boFilterLike) {
		setBoFilterLike(boFilterLike);
		return this;
	}

	public FiCol buiTxParamName(String txParamName) {
		setTxParamName(txParamName);
		return this;
	}

	public enum ColStyle {alignment;}

	public void setupFiCol() {
//		fxTableColProperty().addListener((observable, oldValue, tableColNew) -> {
//			setupPrefSize();
//		});
//
//		fxTreeTableColProperty().addListener((observable, oldValue, tableColNew) -> {
//			setupPrefSize();
//		});
	}


	public static void setAutoFieldName(List<IFiCol> listCol) {
		listCol.forEach(ent -> {
			if (ent.getFieldName() == null) {
				ent.setFieldName(FiString.trimFieldNameWithEngAccent(ent.getHeaderName()));
			}
		});
	}

	public static Map<String, String> getMapColHeaderToFieldName(List<FiCol> listCol) {

		Map<String, String> mapHeaderToField = new HashMap<>();

		listCol.forEach(ozTableCol -> mapHeaderToField.put(ozTableCol.getHeaderName(), ozTableCol.fieldName));

		return mapHeaderToField;

	}

	public static Map<String, String> getMapColFieldToHeaderName(List<IFiCol> listCol) {

		Map<String, String> mapFieldtoHeader = new HashMap<>();

		listCol.forEach(ozTableCol -> mapFieldtoHeader.put(ozTableCol.getFieldName(), ozTableCol.getHeaderName()));

		return mapFieldtoHeader;

	}

	// ***** Getter and Setters

	public String getId() {
		return getFieldName();
	}

	public void setId(String id) {
		this.fieldName = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getHeaderName() {
		if (headerName == null) return "";
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public ObjectProperty<Double> prefSizeProperty() {
		if (prefSize == null) {
			prefSize = new SimpleObjectProperty<>();
		}
		return prefSize;
	}

	@Override
	public Double getPrefSize() {
		return prefSizeProperty().get();
	}


	public void setPrefSize(Double prefSize) {
		prefSizeProperty().set(prefSize);
	}

//	public void setupPrefSize() {
//
//		if (getFxTableCol() != null && getPrefSize() != null) {
//			getFxTableCol().setPrefWidth(getPrefSize());
//		}
//
//		if (getFxTreeTableCol() != null && getPrefSize() != null) {
//			getFxTreeTableCol().setPrefWidth( getPrefSize());
//		}
//
//	}

	public Map<String, String> getMapStyle() {
		if (mapStyle == null) {
			mapStyle = new HashMap<>();
		}
		return mapStyle;
	}

	public void setMapStyle(Map<String, String> mapStyle) {
		this.mapStyle = mapStyle;
	}

	public OzColType getColType() {
		return colType;
	}

	public OzColType getColTypeNtn() {
		if (colType == null) {
			return OzColType.Undefined;
		}
		return colType;
	}

	public void setColType(OzColType colType) {
		this.colType = colType;
	}

	public Format getFormatter() {
		return formatter;
	}

	public void setFormatter(Format formatter) {
		this.formatter = formatter;
	}

	public Function<Object, String> getFuncFormatter() {
		return funcFormatter;
	}

	public void setFuncFormatter(Function<Object, String> funcFormatter) {
		this.funcFormatter = funcFormatter;
	}

	public String getColComment() {
		return colComment;
	}

	public void setColComment(String colComment) {
		this.colComment = colComment;
	}

	public Boolean getBoEnabled() {
		return boEnabled;
	}

	public void setBoEnabled(Boolean boEnabled) {
		this.boEnabled = boEnabled;
	}

	public OzColSummaryType getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(OzColSummaryType summaryType) {
		this.summaryType = summaryType;
	}

	public Integer getPrintSize() {
		return printSize;
	}

	public void setPrintSize(Integer printSize) {
		this.printSize = printSize;
	}

	public Boolean getBoEditable() {
		return boEditable;
	}

	public void setBoEditable(Boolean boEditable) {
		this.boEditable = boEditable;
	}

	public Function getSummaryCalculateFn() {
		return summaryCalculateFn;
	}

	public void setSummaryCalculateFn(Function summaryCalculateFn) {
		this.summaryCalculateFn = summaryCalculateFn;
	}

	public Boolean getBoFilterable() {
		return boFilterable;
	}

	public void setBoFilterable(Boolean boFilterable) {
		this.boFilterable = boFilterable;
	}

	public String getFilterNodeClass() {
		return filterNodeClass;
	}

	public void setFilterNodeClass(String filterNodeClass) {
		this.filterNodeClass = filterNodeClass;
	}

	public Consumer<EntClazz> getFnEditorSetOnActionWithEntity() {
		return fnEditorSetOnActionWithEntity;
	}

	public void setFnEditorSetOnActionWithEntity(Consumer<EntClazz> fnEditorSetOnActionWithEntity) {
		this.fnEditorSetOnActionWithEntity = fnEditorSetOnActionWithEntity;
	}

	public String getColEditorNodeText() {
		return colEditorNodeText;
	}

	public void setColEditorNodeText(String colEditorNodeText) {
		this.colEditorNodeText = colEditorNodeText;
	}

	public BiConsumer<EntClazz, Button> getFnColButton() {
		return fnColButton;
	}

	public void setFnColButton(BiConsumer<EntClazz, Button> fnColButton) {
		this.fnColButton = fnColButton;
	}

	public Node getColFilterNode() {
		return colFilterNode;
	}

	public void setColFilterNode(Node colFilterNode) {
		this.colFilterNode = colFilterNode;
	}

	public EntClazz getEntity() {
		return entity;
	}

	public void setEntity(EntClazz entity) {
		this.entity = entity;
	}

	public Object getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}

	public Boolean getBoHidden() {
		return boHidden;
	}

	public void setBoHidden(Boolean hidden) {
		boHidden = hidden;
	}

	public String getColEditorClass() {
		return colEditorClass;
	}

	public String getColEditorClassInit() {
		if (colEditorClass == null) {
			colEditorClass = "";
		}
		return colEditorClass;
	}

	public void setColEditorClass(String colEditorClass) {
		this.colEditorClass = colEditorClass;
	}

	public void setColEditorClassByClass(Class colEditorClass) {
		this.colEditorClass = colEditorClass.getName();
	}

	public EventHandler<KeyEvent> getColFilterKeyEvent() {
		return this.colFilterKeyEvent;
	}

	public void setColFilterKeyEvent(EventHandler<KeyEvent> colFilterKeyEvent) {
		this.colFilterKeyEvent = colFilterKeyEvent;
	}

	public Boolean getBoOptional() {
		return boOptional;
	}

	public void setBoOptional(Boolean optional) {
		boOptional = optional;
	}

	public Boolean getBoExist() {
		return boExist;
	}

	public void setBoExist(Boolean exist) {
		boExist = exist;
	}

	public Integer getColIndex() {
		return colIndex;
	}

	public void setColIndex(Integer colIndex) {
		this.colIndex = colIndex;
	}

	public FxMigPane getPaneHeader() {
		return paneHeader;
	}

	public void setPaneHeader(FxMigPane paneHeader) {
		this.paneHeader = paneHeader;
	}

	public Boolean getBoNullable() {
		return boNullable;
	}

	public void setBoNullable(Boolean boNullable) {
		this.boNullable = boNullable;
	}

	public Boolean getBoNonUpdatable() {
		return boNonUpdatable;
	}

	public void setBoNonUpdatable(Boolean boNonUpdatable) {
		this.boNonUpdatable = boNonUpdatable;
	}

	public FiCol<EntClazz> buiBoNonUpdatable(Boolean boNonUpdatable) {
		this.boNonUpdatable = boNonUpdatable;
		return this;
	}

	public FiCol<EntClazz> buiBoUpdateField(Boolean boUpdateField) {
		this.boUpdateField = boUpdateField;
		return this;
	}

	public Boolean getBoKeyField() {
		return boKeyField;
	}

	public void setBoKeyField(Boolean boKeyField) {
		this.boKeyField = boKeyField;
	}

	public Boolean getBoNonEditableForForm() {
		return boNonEditableForForm;
	}

	public void setBoNonEditableForForm(Boolean boNonEditableForForm) {
		this.boNonEditableForForm = boNonEditableForForm;
	}

	@Override
	public Node getColEditorNode() {
		return colEditorNode;
	}

	@Override
	public void setColEditorNode(Node colEditorNode) {
		this.colEditorNode = colEditorNode;
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererBeforeSettingValue() {
		return fnEditorNodeRendererBeforeSettingValue;
	}

	public void setFnEditorNodeRendererOnLoad(BiConsumer<Object, Node> fnEditorNodeRendererOnLoad) {
		this.fnEditorNodeRendererBeforeSettingValue = fnEditorNodeRendererOnLoad;
	}

	@Override
	public Object getColEditorValue() {
		return colValue;
	}

	@Override
	public void setColEditorValue(Object colEditorValue) {
		this.colValue = colEditorValue;
	}

	@Override
	public Boolean getBoRequired() {
		return boRequired;
	}

	@Override
	public void setBoRequired(Boolean boRequired) {
		this.boRequired = boRequired;
	}

	public Predicate<EntClazz> getPredFiEditorDisable() {
		return predFiEditorDisable;
	}

	public void setPredFiEditorDisable(Predicate<EntClazz> predFiEditorDisable) {
		this.predFiEditorDisable = predFiEditorDisable;
	}

	/**
	 * table col'da manuel bir değişik yapıldıktan sonra tetiklenir
	 *
	 * @return
	 */
	public Consumer<EntClazz> getFnColCellManualChanged() {
		return fnColCellManualChanged;
	}

	/**
	 * table col'da manuel bir değişik yapıldıktan sonra tetiklenir
	 *
	 * @return
	 */
	public void setFnColCellManualChanged(Consumer<EntClazz> fnColCellManualChanged) {
		this.fnColCellManualChanged = fnColCellManualChanged;
	}

	public FxLabel getSummaryLabelNode() {
		return summaryLabelNode;
	}

	public void setSummaryLabelNode(FxLabel summaryLabelNode) {
		this.summaryLabelNode = summaryLabelNode;
	}

	public Node getSummaryNode() {
		return summaryNode;
	}

	public void setSummaryNode(Node summaryNode) {
		this.summaryNode = summaryNode;
	}

	public FxCheckBox getSummaryCheckBox() {
		return summaryCheckBox;
	}

	public void setSummaryCheckBox(FxCheckBox summaryCheckBox) {
		this.summaryCheckBox = summaryCheckBox;
	}

	public TriConsumer<Object, Node, FxTableCol2> getFnEditorNodeRendererWithCol() {
		return fnEditorNodeRendererWithCol;
	}

	public void setFnEditorNodeRendererWithCol(TriConsumer<Object, Node, FxTableCol2> fnEditorNodeRendererWithCol) {
		this.fnEditorNodeRendererWithCol = fnEditorNodeRendererWithCol;
	}


	public TriConsumer<Object, Node, Object> getFnEditorNodeRendererWitValue() {
		return fnEditorNodeRendererWitValue;
	}

	public void setFnEditorNodeRendererWitValue(TriConsumer<Object, Node, Object> fnEditorNodeRendererWitValue) {
		this.fnEditorNodeRendererWitValue = fnEditorNodeRendererWitValue;
	}

	public BiConsumer<Object, Node> getFnEditorSetOnAction() {
		return fnEditorSetOnAction;
	}

	public void setFnEditorSetOnAction(BiConsumer<Object, Node> fnEditorSetOnAction) {
		this.fnEditorSetOnAction = fnEditorSetOnAction;
	}

	public TriConsumer<Object, Node, FxTableCol> getFnEditorSetOnActionWitCol() {
		return fnEditorSetOnActionWitCol;
	}

	public void setFnEditorSetOnActionWitCol(TriConsumer<Object, Node, FxTableCol> fnEditorSetOnActionWitCol) {
		this.fnEditorSetOnActionWitCol = fnEditorSetOnActionWitCol;
	}

	public TriConsumer<Object, Node, Object> getFnEditorSetOnActionWitValue() {
		return fnEditorSetOnActionWitValue;
	}

	public void setFnEditorSetOnActionWitValue(TriConsumer<Object, Node, Object> fnEditorSetOnActionWitValue) {
		this.fnEditorSetOnActionWitValue = fnEditorSetOnActionWitValue;
	}

	public FxTableCol2<EntClazz> getFxTableCol2() {
		return fxTableCol2;
	}

	public void setFxTableCol2(FxTableCol2<EntClazz> fxTableCol2) {
		this.fxTableCol2 = fxTableCol2;
	}

	public Boolean getBoDontExportExcel() {
		return boDontExportExcel;
	}

	public void setBoDontExportExcel(Boolean boDontExportExcel) {
		this.boDontExportExcel = boDontExportExcel;
	}

	public TableColumn getTableColumnFx() {
		if (tableColumnFx == null) return null;
		return tableColumnFx.get();
	}

	public ObjectProperty<TableColumn> tableColumnFxProperty() {
//		if (tableColProp == null) {
//			tableColProp = new SimpleObjectProperty<>();
//		}
		return tableColumnFx;
	}

	public void setTableColumnFx(TableColumn prmtableColProp) {
//		if (tableColProp == null) {
//			tableColProp = new SimpleObjectProperty<>();
//		}
		this.tableColumnFx.set(prmtableColProp);
	}

	public TreeTableColumn getFxTreeTableCol() {
		return fxTreeTableCol.get();
	}

	public ObjectProperty<TreeTableColumn> fxTreeTableColProperty() {
		return fxTreeTableCol;
	}

	public void setFxTreeTableCol(TreeTableColumn fxTreeTableCol) {
		this.fxTreeTableCol.set(fxTreeTableCol);
	}

	public EventHandler<KeyEvent> getColEditorKeyEvent() {
		return colEditorKeyEvent;
	}

	public void setColEditorKeyEvent(EventHandler<KeyEvent> colEditorKeyEvent) {
		this.colEditorKeyEvent = colEditorKeyEvent;
	}

	public Function<Object, Object> getFnEditorNodeValueFormmatter() {
		return fnEditorNodeValueFormmatter;
	}

	/**
	 * Formlarda componenta değeri basılmazdan önce,
	 * bu fonksiyona entity gönderilir, string bir değer alınır ve componenta basılır.
	 *
	 * @return
	 */
	public void setFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter) {
		this.fnEditorNodeValueFormmatter = fnEditorNodeValueFormmatter;
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererAfterFormLoad() {
		return fnEditorNodeRendererAfterFormLoad;
	}

	public void setFnEditorNodeRendererAfterFormLoad(BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad) {
		this.fnEditorNodeRendererAfterFormLoad = fnEditorNodeRendererAfterFormLoad;
	}

	@Override
	public Boolean equalsColType(OzColType ozColType) {
		if (getColType() == null) return false;
		return getColType() == ozColType;
	}

	public Boolean getBoDontExportExcelTemplate() {
		return boDontExportExcelTemplate;
	}

	public void setBoDontExportExcelTemplate(Boolean boDontExportExcelTemplate) {
		this.boDontExportExcelTemplate = boDontExportExcelTemplate;
	}

	@Override
	public String toString() {
		return FiString.orEmpty(fieldName);
	}

	public IfxNode getIfxNodeEditor() {
		return ifxNodeEditor;
	}

	public void setIfxNodeEditor(IfxNode ifxNodeEditor) {
		this.ifxNodeEditor = ifxNodeEditor;
	}

	public BiConsumer<Object, Node> getFnEditorNodeAfterChangeForForm() {
		return fnEditorNodeAfterChangeForForm;
	}

	public void setFnEditorNodeAfterChangeForForm(BiConsumer<Object, Node> fnEditorNodeAfterChangeForForm) {
		this.fnEditorNodeAfterChangeForForm = fnEditorNodeAfterChangeForForm;
	}

	public void setFnEditorNodeRendererBeforeSettingValue(BiConsumer<Object, Node> fnEditorNodeRendererBeforeSettingValue) {
		this.fnEditorNodeRendererBeforeSettingValue = fnEditorNodeRendererBeforeSettingValue;
	}

	public FiCol buiFnEditorNodeRendererBeforeSettingValue(BiConsumer<Object, Node> fnEditorNodeRendererBeforeSettingValue) {
		this.fnEditorNodeRendererBeforeSettingValue = fnEditorNodeRendererBeforeSettingValue;
		return this;
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererAfterInitialValue1() {
		return fnEditorNodeRendererAfterInitialValue1;
	}

	public FiCol setFnEditorNodeRendererAfterInitialValue1(BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue1) {
		this.fnEditorNodeRendererAfterInitialValue1 = fnEditorNodeRendererAfterInitialValue1;
		return this;
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererAfterInitialValue2() {
		return fnEditorNodeRendererAfterInitialValue2;
	}

	public FiCol setFnEditorNodeRendererAfterInitialValue2(BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue2) {
		this.fnEditorNodeRendererAfterInitialValue2 = fnEditorNodeRendererAfterInitialValue2;
		return this;
	}

	public List<FiCol> getListChildCol() {
		return listChildCol;
	}

	public void setListChildCol(List<FiCol> listChildCol) {
		this.listChildCol = listChildCol;
	}

	public Class getChildClazz() {
		return childClazz;
	}

	/**
	 * Xml okunurken, çocuk xml elementin class tanımı
	 *
	 * @param childClazz
	 */
	public void setChildClazz(Class childClazz) {
		this.childClazz = childClazz;
	}

	public Boolean getBoFilterLike() {
		return boFilterLike;
	}

	public void setBoFilterLike(Boolean boFilterLike) {
		this.boFilterLike = boFilterLike;
	}

	public TriConsumer<Object, Node, List<FiCol>> getFnEditorNodeRendererAfterFormLoad2() {
		return fnEditorNodeRendererAfterFormLoad2;
	}

	public void setFnEditorNodeRendererAfterFormLoad2(TriConsumer<Object, Node, List<FiCol>> fnEditorNodeRendererAfterFormLoad2) {
		this.fnEditorNodeRendererAfterFormLoad2 = fnEditorNodeRendererAfterFormLoad2;
	}

	public Consumer<Node> getFnNodeFocusTrigger() {
		return fnNodeFocusTrigger;
	}

	public void setFnNodeFocusTrigger(Consumer<Node> fnNodeFocusTrigger) {
		this.fnNodeFocusTrigger = fnNodeFocusTrigger;
	}

	public Integer getLnCode() {
		return lnCode;
	}

	public void setLnCode(Integer lnCode) {
		this.lnCode = lnCode;
	}

	public Function<Object, Fdr> getFnValidate() {
		return fnValidate;
	}

	public void setFnValidate(Function<Object, Fdr> fnValidate) {
		this.fnValidate = fnValidate;
	}

	public Object getColValue() {
		return colValue;
	}

	public void setColValue(Object colValue) {
		this.colValue = colValue;
	}

	public Boolean getBoUpdateField() {
		return boUpdateField;
	}

	public void setBoUpdateField(Boolean boUpdateField) {
		this.boUpdateField = boUpdateField;
	}

	public String getTxLabel() {
		return txLabel;
	}

	public void setTxLabel(String txLabel) {
		this.txLabel = txLabel;
	}

	public Class<EntClazz> getEntClass() {
		return entClass;
	}

	public void setEntClass(Class<EntClazz> entClass) {
		this.entClass = entClass;
	}

	public String getTxParamName() {
		return txParamName;
	}

	public void setTxParamName(String txParamName) {
		this.txParamName = txParamName;
	}

	public String getTxDbFieldName() {
		return txDbFieldName;
	}

	public void setTxDbFieldName(String txDbFieldName) {
		this.txDbFieldName = txDbFieldName;
	}

	public String getTxGuid() {
		return txGuid;
	}

	public void setTxGuid(String txGuid) {
		this.txGuid = txGuid;
	}

	public Boolean getBoParamStatus() {
		return boParamStatus;
	}

	public void setBoParamStatus(Boolean boParamStatus) {
		this.boParamStatus = boParamStatus;
	}

	public OzColType getColGenType() {
		return colGenType;
	}

	public OzColType getColGenTypeNtn() {
		if (colGenType == null) {
			return OzColType.Undefined;
		}
		return colGenType;
	}

	public void setColGenType(OzColType colGenType) {
		this.colGenType = colGenType;
	}
}

