package ozpasyazilim.utils.mvc;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.gui.fxcomponents.IfxNode;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;

import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IFiCol<N> {

	static void setAutoFieldName(List<IFiCol> listCol) {
		listCol.forEach(ent -> {
			if (ent.getFieldName() == null) {
				ent.setFieldName(FiString.trimFieldNameWithEngAccent(ent.getHeaderName()));
			}
		});
	}

	static Map<String, String> getMapColHeaderToFieldName(List<IFiCol> listCol) {
		Map<String, String> mapHeaderToField = new HashMap<>();
		listCol.forEach(ozTableCol -> mapHeaderToField.put(ozTableCol.getHeaderName(), ozTableCol.getFieldName()));
		return mapHeaderToField;
	}

	static Map<String, String> getMapColFieldToHeaderName(List<IFiCol> listCol) {
		Map<String, String> mapFieldtoHeader = new HashMap<>();
		listCol.forEach(ozTableCol -> mapFieldtoHeader.put(ozTableCol.getFieldName(), ozTableCol.getHeaderName()));
		return mapFieldtoHeader;
	}

	// <-- Build Methods

	IFiCol buildColType(OzColType colType);

	IFiCol buildPrefSize(Double prefSize);

	IFiCol buildPrintSize(Integer printSize);

	IFiCol buildSumType(OzColSummaryType summaryType);

	IFiCol buildHeader(String header);

	// --> -----------------

	public String getId();

	public void setId(String id);

	public String getFieldName();

	public void setFieldName(String fieldName);

	public String getHeaderName();

	public void setHeaderName(String headerName);

	public Double getPrefSize();

	public void setPrefSize(Double prefSize);

	public Map<String, String> getMapStyle();

	public void setMapStyle(Map<String, String> mapStyle);

	public OzColType getColType();

	public void setColType(OzColType colType);

	public Format getFormatter();

	public void setFormatter(Format formatter);

	public Function<Object, String> getFuncFormatter();

	public void setFuncFormatter(Function<Object, String> funcFormatter);

	public String getColComment();

	public void setColComment(String colComment);

	public Boolean getBoEnabled();

	public void setBoEnabled(Boolean boEnabled);

	public OzColSummaryType getSummaryType();

	public void setSummaryType(OzColSummaryType summaryType);

	public Integer getPrintSize();

	public void setPrintSize(Integer printSize);

	public Boolean getBoEditable();

	public void setBoEditable(Boolean boEditable);

	public Function getSummaryCalculateFn();

	public void setSummaryCalculateFn(Function summaryCalculateFn);

	public Boolean getBoFilterable();

	public void setBoFilterable(Boolean boFilterable);

	public String getFilterNodeClass();

	public void setFilterNodeClass(String filterNodeClass);

	public Consumer<N> getFnEditorSetOnActionWithEntity();

	public void setFnEditorSetOnActionWithEntity(Consumer<N> fnEditorSetOnActionWithEntity);

	public String getColEditorNodeText();

	public void setColEditorNodeText(String colEditorNodeText);

	public BiConsumer<N, Button> getFnColButton();

	public void setFnColButton(BiConsumer<N, Button> fnColButton);

	public Node getColFilterNode();

	public void setColFilterNode(Node colFilterNode);

	public N getEntity();

	public void setEntity(N entity);

	public Object getFilterValue();

	void setFilterValue(Object filterValue);

	Boolean getBoHidden();

	/**
	 * Formlarda gizli olduğunu belirtir , forma eklenmez.
	 *
	 * @param hidden true olursa formda gizlenir olur
	 */
	void setBoHidden(Boolean hidden);

	String getColEditorClass();

	/**
	 *
	 *
	 * @param colEditorClass
	 */
	void setColEditorClass(String colEditorClass);

	EventHandler<KeyEvent> getColFilterKeyEvent();

	void setColFilterKeyEvent(EventHandler<KeyEvent> colFilterKeyEvent);

	Boolean getBoOptional();

	void setBoOptional(Boolean optional);

	Boolean getBoExist();

	void setBoExist(Boolean exist);

	Integer getColIndex();

	void setColIndex(Integer colIndex);

	public FxMigPane getPaneHeader();

	public void setPaneHeader(FxMigPane paneHeader);

	public Boolean getBoNullable();

	public void setBoNullable(Boolean boNullable);

	public Boolean getBoNonUpdatable();

	public void setBoNonUpdatable(Boolean boNonUpdatable);

	public Boolean getBoKeyField();

	public void setBoKeyField(Boolean boKeyField);

	public Boolean getBoNonEditableForForm();

	public void setBoNonEditableForForm(Boolean boNonEditableForForm);

	public Node getColEditorNode();

	public void setColEditorNode(Node colEditorNode);

	public BiConsumer<Object, Node> getFnEditorNodeRendererBeforeSettingValue();

	public void setFnEditorNodeRendererOnLoad(BiConsumer<Object, Node> fnEditorNodeRendererOnLoad);

	/**
	 * use colValue
	 *
	 * @return
	 */
	@Deprecated
	public Object getColEditorValue();

	@Deprecated
	public void setColEditorValue(Object colEditorValue);

	public Boolean getBoRequired();

	public void setBoRequired(Boolean boRequired);

	public EventHandler<KeyEvent> getColEditorKeyEvent();

	public void setColEditorKeyEvent(EventHandler<KeyEvent> colEditorKeyEvent);

	/**
	 * Formlarda componenta değeri basılmazdan önce,
	 * bu fonksiyona  entity gönderilir, string bir değer alınır ve componenta basılır.
	 *
	 * @return
	 */
	Function<Object, Object> getFnEditorNodeValueFormmatter();

	void setFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter);

	BiConsumer<Object, Node> getFnEditorNodeRendererAfterFormLoad();

	void setFnEditorNodeRendererAfterFormLoad(BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad);

	Boolean equalsColType(OzColType ozColType);

	Boolean getBoDontExportExcelTemplate();

	void setBoDontExportExcelTemplate(Boolean boDontExportExcelTemplate);

	void setIfxNodeEditor(IfxNode iFxNode);

	IfxNode getIfxNodeEditor();

	public Object getColValue();

	public void setColValue(Object colValue);

}