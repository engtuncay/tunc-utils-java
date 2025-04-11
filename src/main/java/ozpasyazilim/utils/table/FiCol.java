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
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.datatypes.FiMeta;
import ozpasyazilim.utils.fidbanno.FiTable;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.fidborm.IFiField;
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
 * <p>
 * (field type of column and field for ex. string , bool , date ...)
 *
 * @param <EntClazz>
 */
@FiTable
public class FiCol<EntClazz> implements IFiCol<EntClazz>, IFiField {

    /**
     * Alanın ismini (veritabanındaki veya objedeki refere ettiği alan ismi )
     */
    private String ofcTxFieldName;

    /**
     * Alanın başlık açıklaması ( tablo için sütün başlığı , form için label alanı değeri / excelde başlık )
     */
    private String ofcTxHeader;

    private String txLabel;

    /**
     * Objedeki alan adı (fieldName) ile db deki alan adı aynı degilse kullanılır.
     */
    private String ofcTxDbField;

    /**
     * Col Id olması için konuldu - tekil kodu
     */
    private String txGuid;

    private ObjectProperty<Double> prefSize;

    private Integer printSize;

    // Alanın türünü belirtir (double,string,date vs )
    private OzColType colType;

    private String ofcTxFieldDesc;

    /**
     * Column Generic Type. Sütun nasıl bir tipte olduğunu gösterir. (Data Tipi degil)
     * <p>
     * Örneğin , Xml parse edilirken, alanın xmlAttribute türünde olduğunu gösterir.
     */
    private OzColType colGenType;

    // Formlarda default true olarak çalışır, false olursa düzenleme izni vermez
    private Boolean boEditable;


    /**
     * Formlarda gösterilmeyeceğini belirtir
     */
    private Boolean boHidden;

    // excelden sütunları ayarlarken opsiyonel sütunların belinmesi için (zorunlu degil) , vs.. (boRequired:false da kullanılabilirdi.)
    private Boolean boOptional;

    // excelde sütunun bulunduğunu gösterir
    private Boolean boExist;

    // Excel için true olursa sütunun olması gerektiğini gösterir
    private Boolean boRequired;

    // For Excel Reading, the field shows whether or not column exists in the excel
    private Boolean boEnabled;

    // phpde buraya kadar kopyalanadı

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

    /**
     * Tablo başlıklarındaki filtre için ve formlar daki comp.ler için
     */
    private Boolean boLocFilterable;

    /**
     * Tablodaki sütunun remote filterable olduğunu gösterir. default true kabul edilir
     */
    private Boolean boRemFilterable;

    /**
     * Alan için hangi tür filter node kullanılacaksa sınıfın ismi tutulur
     */
    String filterNodeClass;

    // experimental
    private Function<Object, String> funcFormatter;

    private Format formatter;

    // --Table Editor ile İgli Alanlar
    private String colEditorClass;
    private Node colEditorNode;

    private Object colValue;

    /**
     * Editörde kullanılacak component kullanılack yazı , örneğin button ise üzerine yazılacak text
     */
    private String colEditorNodeText;

    /**
     * editorde enter basılınca action tanımı
     */
    EventHandler<KeyEvent> colEditorKeyEvent; //

    // ---- Editor Action
    private Consumer<EntClazz> fnEditorSetOnActionWithEntity;
    private BiConsumer<Object, Node> fnEditorSetOnAction;
    private TriConsumer<Object, Node, FxTableColDep> fnEditorSetOnActionWitCol;
    private TriConsumer<Object, Node, Object> fnEditorSetOnActionWitValue;

    /**
     * entity giren, value çıkan -- comp üzerinde yazan (text) değeri formatlamak için , (arka plan değeri değiştirmez (txValue su varsa eğer)
     */
    private Function<Object, Object> fnEditorNodeValueFormmatter;

    // ---- Editor Renderer

    /**
     * Değer ataması öncesinde Node comp'ine özel renderer yapacaksak bu fonksiyonu kulanırız. (formlarda alanlar için, tabloda editor için)
     */
    private BiConsumer<Object, Node> fnEditorNodeRendererBeforeSettingValue;

    /**
     * Değer ataması sonrasında bileşene(comp) yapılacak işlemler. Genellikle Object olarak entity gönderilir.
     */
    private BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue1;
    private BiConsumer<Object, Node> fnEditorNodeRendererAfterInitialValue2;

    /**
     * Form yüklendikten (After Setup) sonra çalıştırılacak
     * <p>
     * Editorler için , after form load, it executes
     * <p>
     * Object olarak form için kullanılacak entity gönderilir
     * <p>
     * Node : component için kullanılır (FxTextField,FxCombobox gibi)
     * <p>
     * Form objesi dinamik olarak çekilmeli
     */
    @Deprecated
    private BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad;

    private BiConsumer<Object, Node> fnEditorNodeAfterChangeForForm;

    private TriConsumer<Object, Node, FxTableCol2> fnEditorNodeRendererWithCol;
    private TriConsumer<Object, Node, Object> fnEditorNodeRendererWitValue;

    /**
     * bütün form elemanları oluşturulduktan sonra çalıştırılır {@link FxFormMigGen}
     */
    private Consumer<FiCol> fnEditorNodeLfcAfterAllFormLoad;


    /**
     * Editör comp, mesela tabloda bu pred e göre disable olmasını sağlar ( ifc ye eklenmedi)
     */
    private Predicate<EntClazz> predEditorDisable;

    // --Excel İle İlgili Alanlar

    /**
     * excel için kaçıncı sütunda olduğunu gösterir
     */
    Integer colIndex;

    // Xml okumalar için

    /**
     * Elemente ait, alt çocuk elementleri
     * <p>
     * Form Elemanın içinde bulunan alt propertiler
     */
    List<FiCol> listChildCol;

    /**
     *
     */
    Class childClazz;

    /**
     * Hangi Tablonun veya Sınıfın Alanı olduğunu belirtir
     */
    Class<EntClazz> entClass;


    /**
     * Excelde, sütunun yorum balonunda gösterilecek açıklamayı belirtir
     */
    private String colComment;

    /**
     *
     */
    private Map<String, String> mapStyle;


    // --Form ile İlgili Alanlar


    // -- FxTableView için alanlar

    private BiConsumer<EntClazz, Button> fnColButton;

    // ****** Sql Sorgular ile ilgili alanlar


    /**
     * Bu alanın null değer alıp alamayacağını gösterir.
     */
    private Boolean boNullable;

    /**
     * Alanlarda update query oluşturulurken dahil edilmeyecek alanlar
     * <p>
     * Formlarda ilgili alanın update edilmeyeceğini gösterir
     */
    private Boolean boNonUpdatable;

    /**
     * Formlarda edit yapılmayacak alanı gösterir
     */
    private Boolean boNonEditableForForm;

    // ****** SORGULAR İÇİN OLUŞTURULAN ALANLAR *******

    /**
     * Genel olarak parametre ismi fieldName seçilir , farklı seçilecekse bu alandaki değer kullanılır.
     */
    private String txParamName;

    /**
     * Sorgular için, alanın primary key alanı olduğunu belirtir
     */
    private Boolean boKeyIdField;

    private Boolean boKeyIdentityField;

    /**
     * Sorgu hazırlanırken update olacak alan olduğunu gösterir
     */
    private Boolean boUpdateFieldForQuery;

    /**
     * Sorgu hazırlanırken insert sorgusuna dahil edilecek alanları gösterir
     */
    private Boolean boInsertFieldForQuery;

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
     * Editörte bir değişim olursa çağrılır (tablonun hücresi değişiklik olursa bildirilir) (property de çevrilebilir) (Inf'ye eklenmedi.)
     */
    private Consumer<EntClazz> fnColCellManualChanged;

    // Header summary node ile ilgili alanlar

    /**
     * Header Summary Label (inftable cola eklenmedi)
     */
    private FxLabel summaryLabelNode; //

    /**
     *
     */
    private FxCheckBox summaryCheckBox;
    /**
     * Tabloda Özetleme Alanına Yerleştirilecek Bileşen (node)
     */
    private Node summaryNode;

    /**
     * Tablolarda özet alanının türünü belirtir (toplam,ortalama vs. gibi )
     */
    private OzColSummaryType summaryType;

    /**
     *
     */
    private Function summaryCalculateFn;

    /**
     * Sütunun excel dosyasına aktarılmayacağını gösterir
     */
    private Boolean boDontExportExcel;

    /**
     * Sütunun excel şablon dosyasına aktarılmayacağını gösterir
     */
    private Boolean boDontExportExcelTemplate;

    /**
     *
     */
    private IFiNode IFiNodeEditor;

    /**
     * Filter node like araması yapılacak mı
     */
    private Boolean boFilterLike;

    /**
     * Entity , Node Componet, List-FiCol- degerlerini alan consumer fn
     */
    private TriConsumer<Object, Node, List<FiCol>> fnEditorNodeRendererAfterFormLoad2;


    /**
     * formlarda comp.'a focus olunca trigger edilecek fonksiyon
     */
    private Consumer<Node> fnNodeFocusTrigger;

    /**
     * Sütun veya Alan için Sayısal karşılık saklamak için, canceled için 1 gibi
     */
    private Integer lnCode;


    /**
     * sütun veya form alanı için validate fonksiyonu, object olarak form alanınına değeri gönderilir (draft)
     */
    private Function<Object, Fdr> fnValidate;

    /**
     * Editor Component içerisine sadece sayısal veri girilecek (String olan VergiNo alanına sadece sayısal alan girilir)
     */
    private Boolean boEditorOnlyNumber;

    /**
     * Tanımlanmamış FiCol olduğunu , obje olarak null pointer almaması için oluşturulduğunu gösterir
     */
    // private Boolean boUndefinedCol;

    /**
     * Sorgu oluşturulurken where alanına yazılacak alanlar (update sorgusu için kullanılır)
     */
    private Boolean boWhereField;


    // Reflection Field Alanlar

    // FiId
    private String ofcTxIdType;
    // FiColumn
    private Boolean ofcBoUniqGro1;
    private Boolean ofcBoNullable;
    private Boolean ofcBoUnique;
    private Boolean ofcBoUtfSupport;
    /**
     * Default Value
     */
    private String ofcTxDefValue;
    private String ofcTxCollation;
    private String ofcTxTypeName;
    private String ofcTxFieldType;

    /**
     * column definition (sql alan ilgili extra bilgiler)
     */
    private String ofcTxColDefinition;
    /**
     * Text alanın max uzunluğu
     * <p>
     * FxEditorFactory'de textfield oluştururken text limiti olarak belirlenir
     */
    private Integer ofcLnLength;
    private Integer ofcLnPrecision;
    private Integer ofcLnScale;
    private Boolean ofcBoFilterLike;

    //FiTransient

    private String ofcTxEntityName;


    private String txClassNameSimple;

    /**
     * alanın veritabanında olmadığını belirtir
     */
    private Boolean ofcBoTransient;

    private String txFilterType;

    /**
     * Code Generate ederken oluşturulan alan tanımı
     * <p>
     * Code Generator tarafından kullanılır.
     */
    private String ficTxSqlFieldDefinition;


    // ***** Constructors
    public FiCol() {
        // FiCol için standard ön ayarlar
        setupFiCol();
    }

    public FiCol(String ofcTxFieldName, String ofcTxHeader) {
        this.ofcTxHeader = ofcTxHeader;
        this.ofcTxFieldName = ofcTxFieldName;
        setupFiCol();
    }

    public FiCol(String ofcTxFieldName) {
        this.ofcTxFieldName = ofcTxFieldName;
        setupFiCol();
    }

    public FiCol(String ofcTxFieldName, String ofcTxHeader, String colComment) {
        this.ofcTxHeader = ofcTxHeader;
        this.ofcTxFieldName = ofcTxFieldName;
        setColComment(colComment);
        setupFiCol();
    }

    public FiCol(String ofcTxHeader, Object ofcTxFieldName) {
        this.ofcTxHeader = ofcTxHeader;
        this.ofcTxFieldName = ofcTxFieldName.toString();
        setupFiCol();
    }

    public FiCol(Object ofcTxFieldName, String ofcTxHeader, OzColType colType) {
        this.ofcTxHeader = ofcTxHeader;
        if (ofcTxFieldName != null) this.ofcTxFieldName = ofcTxFieldName.toString();
        this.setColType(colType);
        setupFiCol();
    }

    public FiCol(Object ofcTxFieldName, String ofcTxHeader, OzColType colType, String colComment) {
        this.ofcTxHeader = ofcTxHeader;
        if (ofcTxFieldName != null) this.ofcTxFieldName = ofcTxFieldName.toString();
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
            if (FiBool.isTrue(fiField.getBoExcludeFromAutoColList())) continue;
            FiCol fiTableCol = new FiCol(fiField.getOfcTxFieldName(), fiField.getOfcTxFieldName());
            fiTableCol.setColType(convertOzColType(fiField.getClassNameSimple()));
            if (fiTableCol.getColTypeNtn().equals(OzColType.Double) || fiTableCol.getColTypeNtn().equals(OzColType.Integer)) {
                fiTableCol.setSummaryType(OzColSummaryType.SUM);
            }
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

    /**
     * Eksik ihtiyaçlara göre doldurulması lazım
     *
     * @param iFiCol
     * @return
     */
    public static FiCol buildFromIFiCol(IFiCol iFiCol) {
        FiCol fiCol = new FiCol();

        fiCol.setOfcTxHeader(iFiCol.getOfcTxHeader());
        fiCol.setOfcTxFieldName(iFiCol.getOfcTxFieldName());
        fiCol.setColType(iFiCol.getColType());
        fiCol.setPrefSize(iFiCol.getPrefSize());
        fiCol.setBoEditable(iFiCol.getBoEditable());

        return fiCol;
    }

    // build Methods

    public FiCol buiHeader(String header) {
        this.setOfcTxHeader(header);
        return this;
    }

    public FiCol buiSynFieldToHeader() {
        this.setOfcTxHeader(getOfcTxFieldName());
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

    public FiCol buiEditorNodeClass(String editorClassName) {
        this.setColEditorClass(editorClassName);
        return this;
    }

    public FiCol buiEditorNodeClass(Class editorClass) {
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

    /**
     * vt de key alanını işaret eder
     *
     * @param boKeyField
     * @return
     */
    public FiCol buildKeyField(boolean boKeyField) {
        this.setBoKeyIdField(boKeyField);
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

    public FiCol buiColType(OzColType colType) {
        setColType(colType);
        return this;
    }

    public FiCol buildColGenType(OzColType colType) {
        setColGenType(colType);
        return this;
    }

    public FiCol buiPrefSize(Double prefSize) {
        setPrefSize(prefSize);
        return this;
    }

    public FiCol buiPrefSize(Integer prefSize) {
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


    public FiCol buiSumType(OzColSummaryType summaryType) {
        setSummaryType(summaryType);
        return this;
    }

    public FiCol buiNonEditable(boolean boNonEditable) {
        setBoNonEditableForForm(boNonEditable);
        return this;
    }

    /**
     * Excelde, sütunun yorum balonunda gösterilecek açıklamayı belirtir
     */
    public FiCol buiComment(String txComment) {
        setColComment(txComment);
        return this;
    }

    public FiCol buiBoEditable(Boolean boEditable) {
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

    public FiCol buiColValue(Object colValue) {
        setColValue(colValue);
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

    public FiCol buiBoKeyIdField(Boolean boKeyField) {
        setBoKeyIdField(boKeyField);
        return this;
    }

    /**
     * Class getName kullanılır (uzun sınıf ismi)
     *
     * @param name
     * @return
     */
    public FiCol buiColEditorClass(String name) {
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
        this.ofcTxFieldName = fieldName;
        return this;
    }

    public FiCol buiBoLocFilter(Boolean boFilterable) {
        setBoLocFilterable(boFilterable);
        return this;
    }

    public FiCol buildFiEditorDisabled(Predicate<EntClazz> predFiEditorDisable) {
        setPredEditorDisable(predFiEditorDisable);
        return this;
    }

    /**
     * getFnEditorNodeRendererBeforeSettingValue çalıştırır
     *
     * @param entity
     * @param comp
     */
    public void lifeCycleNodeOperationsBeforeSettingValue(Object entity, Node comp) {
        if (getFnEditorNodeRendererBeforeSettingValue() != null) {
            getFnEditorNodeRendererBeforeSettingValue().accept(entity, comp);
        }
    }

    public void lifeCycleNodeOperationsAfterInitialValue(Object entity, Node comp) {

        if (getFnEditorNodeRendererAfterInitialValue1() != null) {
            getFnEditorNodeRendererAfterInitialValue1().accept(entity, comp);
        }

        if (getFnEditorNodeRendererAfterInitialValue2() != null) {
            getFnEditorNodeRendererAfterInitialValue2().accept(entity, comp);
        }

    }

    public FiCol buiBoFilterLike(Boolean boFilterLike) {
        setBoFilterLike(boFilterLike);
        return this;
    }

    public FiCol buiBoFiltAndLike(Boolean boFilterLike) {
        setBoLocFilterable(true);
        setBoFilterLike(boFilterLike);
        return this;
    }

    public FiCol buiTxParamName(String txParamName) {
        setTxParamName(txParamName);
        return this;
    }

    public FiCol buiBoRemoteFilterable(Boolean boValue) {
        setBoRemFilterable(boValue);
        return this;
    }

    public FiCol buiTxFilterType(FiMeta fimFicFilterType) {
        setTxFilterType(fimFicFilterType.getTxKey());
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
            if (ent.getOfcTxFieldName() == null) {
                ent.setOfcTxFieldName(FiString.trimFieldNameWithEngAccent(ent.getOfcTxHeader()));
            }
        });
    }

    public static Map<String, String> getMapColHeaderToFieldName(List<FiCol> listCol) {

        Map<String, String> mapHeaderToField = new HashMap<>();

        listCol.forEach(ozTableCol -> mapHeaderToField.put(ozTableCol.getOfcTxHeader(), ozTableCol.ofcTxFieldName));

        return mapHeaderToField;

    }

    public static Map<String, String> getMapColFieldToHeaderName(List<IFiCol> listCol) {

        Map<String, String> mapFieldtoHeader = new HashMap<>();

        listCol.forEach(ozTableCol -> mapFieldtoHeader.put(ozTableCol.getOfcTxFieldName(), ozTableCol.getOfcTxHeader()));

        return mapFieldtoHeader;

    }

    // ***** Getter and Setters

    public String getId() {
        return getOfcTxFieldName();
    }

    public void setId(String id) {
        this.ofcTxFieldName = id;
    }

    public String getOfcTxFieldName() {
        return ofcTxFieldName;
    }

    public void setOfcTxFieldName(String ofcTxFieldName) {
        this.ofcTxFieldName = ofcTxFieldName;
    }

    public String getOfcTxHeader() {
        if (ofcTxHeader == null) return "";
        return ofcTxHeader;
    }

    public void setOfcTxHeader(String ofcTxHeader) {
        this.ofcTxHeader = ofcTxHeader;
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

    public Boolean getBoLocFilterable() {
        return boLocFilterable;
    }

    public void setBoLocFilterable(Boolean boLocFilterable) {
        this.boLocFilterable = boLocFilterable;
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

    public FiCol<EntClazz> buiBoUpdateFieldForQuery(Boolean boUpdateField) {
        this.boUpdateFieldForQuery = boUpdateField;
        return this;
    }

    public Boolean getBoKeyIdField() {
        return boKeyIdField;
    }

    public void setBoKeyIdField(Boolean boKeyIdField) {
        this.boKeyIdField = boKeyIdField;
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
    public Boolean getBoRequired() {
        return boRequired;
    }

    @Override
    public void setBoRequired(Boolean boRequired) {
        this.boRequired = boRequired;
    }

    public Predicate<EntClazz> getPredEditorDisable() {
        return predEditorDisable;
    }

    public void setPredEditorDisable(Predicate<EntClazz> predEditorDisable) {
        this.predEditorDisable = predEditorDisable;
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

    public TriConsumer<Object, Node, FxTableColDep> getFnEditorSetOnActionWitCol() {
        return fnEditorSetOnActionWitCol;
    }

    public void setFnEditorSetOnActionWitCol(TriConsumer<Object, Node, FxTableColDep> fnEditorSetOnActionWitCol) {
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

    public void setTableColumnFx(TableColumn tableColumn) {
        this.tableColumnFx.set(tableColumn);
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
        return FiString.orEmpty(ofcTxFieldName);
    }

    public IFiNode getIfxNodeEditor() {
        return IFiNodeEditor;
    }

    public void setIfxNodeEditor(IFiNode IFiNodeEditor) {
        this.IFiNodeEditor = IFiNodeEditor;
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

    public Boolean getBoUpdateFieldForQuery() {
        return boUpdateFieldForQuery;
    }

    public void setBoUpdateFieldForQuery(Boolean boUpdateFieldForQuery) {
        this.boUpdateFieldForQuery = boUpdateFieldForQuery;
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

    public String getOfcTxDbField() {
        return ofcTxDbField;
    }

    public String getTxDbFieldNameOrFieldName() {
        if (ofcTxDbField != null) return ofcTxDbField;
        return ofcTxFieldName;
    }

    public void setOfcTxDbField(String ofcTxDbField) {
        this.ofcTxDbField = ofcTxDbField;
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

    public Boolean getBoEditorOnlyNumber() {
        return boEditorOnlyNumber;
    }

    public void setBoEditorOnlyNumber(Boolean boEditorOnlyNumber) {
        this.boEditorOnlyNumber = boEditorOnlyNumber;
    }

    public Boolean getBoWhereField() {
        return boWhereField;
    }

    public void setBoWhereField(Boolean boWhereField) {
        this.boWhereField = boWhereField;
    }

    public Consumer<FiCol> getFnEditorNodeLfcAfterAllFormLoad() {
        return fnEditorNodeLfcAfterAllFormLoad;
    }

    public void setFnEditorNodeLfcAfterAllFormLoad(Consumer<FiCol> fnEditorNodeLfcAfterAllFormLoad) {
        this.fnEditorNodeLfcAfterAllFormLoad = fnEditorNodeLfcAfterAllFormLoad;
    }

    public FiCol buiFnEditorNodeLfcAfterAllFormLoad(Consumer<FiCol> fnEditorNodeRendererAfterAllFormLoad) {
        this.fnEditorNodeLfcAfterAllFormLoad = fnEditorNodeRendererAfterAllFormLoad;
        return this;
    }

    public Boolean getBoKeyIdentityField() {
        return boKeyIdentityField;
    }

    public void setBoKeyIdentityField(Boolean boKeyIdentityField) {
        if (FiBool.isTrue(boKeyIdentityField)) setBoKeyIdField(true);
        this.boKeyIdentityField = boKeyIdentityField;
    }

    public Boolean getOfcBoTransient() {
        return ofcBoTransient;
    }

    public void setOfcBoTransient(Boolean ofcBoTransient) {
        this.ofcBoTransient = ofcBoTransient;
    }

    public Boolean getBoInsertFieldForQuery() {
        return boInsertFieldForQuery;
    }

    public FiCol setBoInsertFieldForQuery(Boolean boInsertFieldForQuery) {
        this.boInsertFieldForQuery = boInsertFieldForQuery;
        return this;
    }

    public String getOfcTxIdType() {
        return ofcTxIdType;
    }

    public void setOfcTxIdType(String ofcTxIdType) {
        this.ofcTxIdType = ofcTxIdType;
    }

    public Boolean getOfcBoUniqGro1() {
        return ofcBoUniqGro1;
    }

    public void setOfcBoUniqGro1(Boolean ofcBoUniqGro1) {
        this.ofcBoUniqGro1 = ofcBoUniqGro1;
    }

    public Boolean getOfcBoNullable() {
        return ofcBoNullable;
    }

    public void setOfcBoNullable(Boolean ofcBoNullable) {
        this.ofcBoNullable = ofcBoNullable;
    }

    public Boolean getOfcBoUnique() {
        return ofcBoUnique;
    }

    public void setOfcBoUnique(Boolean ofcBoUnique) {
        this.ofcBoUnique = ofcBoUnique;
    }

    public Boolean getOfcBoUtfSupport() {
        return ofcBoUtfSupport;
    }

    public void setOfcBoUtfSupport(Boolean ofcBoUtfSupport) {
        this.ofcBoUtfSupport = ofcBoUtfSupport;
    }

    public String getOfcTxDefValue() {
        return ofcTxDefValue;
    }

    public void setOfcTxDefValue(String ofcTxDefValue) {
        this.ofcTxDefValue = ofcTxDefValue;
    }

    public String getOfcTxCollation() {
        return ofcTxCollation;
    }

    public void setOfcTxCollation(String ofcTxCollation) {
        this.ofcTxCollation = ofcTxCollation;
    }

    public String getOfcTxTypeName() {
        return ofcTxTypeName;
    }

    public void setOfcTxTypeName(String ofcTxTypeName) {
        this.ofcTxTypeName = ofcTxTypeName;
    }

    public Integer getOfcLnLength() {
        return ofcLnLength;
    }

    public void setOfcLnLength(Integer ofcLnLength) {
        this.ofcLnLength = ofcLnLength;
    }

    public Integer getOfcLnPrecision() {
        return ofcLnPrecision;
    }

    public void setOfcLnPrecision(Integer ofcLnPrecision) {
        this.ofcLnPrecision = ofcLnPrecision;
    }

    public Integer getOfcLnScale() {
        return ofcLnScale;
    }

    public void setOfcLnScale(Integer ofcLnScale) {
        this.ofcLnScale = ofcLnScale;
    }

    public Boolean getOfcBoFilterLike() {
        return ofcBoFilterLike;
    }

    public void setOfcBoFilterLike(Boolean ofcBoFilterLike) {
        this.ofcBoFilterLike = ofcBoFilterLike;
    }

    public String getOfcTxFieldType() {
        return ofcTxFieldType;
    }

    public void setOfcTxFieldType(String ofcTxFieldType) {
        this.ofcTxFieldType = ofcTxFieldType;
    }

    public String getFicTxSqlFieldDefinition() {
        return ficTxSqlFieldDefinition;
    }

    public void setFicTxSqlFieldDefinition(String ficTxSqlFieldDefinition) {
        this.ficTxSqlFieldDefinition = ficTxSqlFieldDefinition;
    }

    public String getOfcTxEntityName() {
        return ofcTxEntityName;
    }

    public void setOfcTxEntityName(String ofcTxEntityName) {
        this.ofcTxEntityName = ofcTxEntityName;
    }

    public String getTxClassNameSimple() {
        return txClassNameSimple;
    }

    public void setTxClassNameSimple(String txClassNameSimple) {
        this.txClassNameSimple = txClassNameSimple;
    }

    public String getOfcTxColDefinition() {
        return ofcTxColDefinition;
    }

    public void setOfcTxColDefinition(String ofcTxColDefinition) {
        this.ofcTxColDefinition = ofcTxColDefinition;
    }

    public Boolean getBoRemFilterable() {
        return boRemFilterable;
    }

    public void setBoRemFilterable(Boolean boRemFilterable) {
        this.boRemFilterable = boRemFilterable;
    }

    public IFiNode getIFiNodeEditor() {
        return IFiNodeEditor;
    }

    public void setIFiNodeEditor(IFiNode IFiNodeEditor) {
        this.IFiNodeEditor = IFiNodeEditor;
    }

    public String getOfcTxFieldDesc() {
        return ofcTxFieldDesc;
    }

    public void setOfcTxFieldDesc(String ofcTxFieldDesc) {
        this.ofcTxFieldDesc = ofcTxFieldDesc;
    }

    public String getTxFilterType() {
        return txFilterType;
    }

    public void setTxFilterType(String txFilterType) {
        this.txFilterType = txFilterType;
    }
}


//    /**
//     * Formlarda componenta değeri basılmazdan önce,
//     * bu fonksiyona  entity gönderilir, string bir değer alınır ve componenta basılır.
//     *
//     * @return
//     */
//    public FiCol buildFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter) {
//        setFnEditorNodeValueFormmatter(fnEditorNodeValueFormmatter);
//        return this;
//    }

//    @Override
//    public Object getColEditorValue() {
//        return colValue;
//    }
//
//    @Override
//    public void setColEditorValue(Object colEditorValue) {
//        this.colValue = colEditorValue;
//    }
