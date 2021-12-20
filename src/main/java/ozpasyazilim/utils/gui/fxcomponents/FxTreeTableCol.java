package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class FxTreeTableCol<E> extends TreeTableColumn implements IFiCol<E> {

	FiCol<E> fiTableCol;

	public Locale locale = new Locale("tr", "TR");

	public FxTreeTableCol() {

	}

	public FxTreeTableCol(String header) {
		super(header);
	}

	@Override
	public ObservableValue getCellObservableValue(Object item) {
		return null;
	}

	public FxTreeTableCol(String header, String fieldName) {
		super(header);
		this.setFieldName(fieldName);
		this.setId(fieldName);

		//empNoCol.setCellValueFactory(new TreeItemPropertyValueFactory<Employee, String>("empNo"));

		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
	}

	public FxTreeTableCol(String header, String fieldName, OzColType dataType) {
		super(header);
		this.setFieldName(fieldName);
		this.setId(fieldName);
		this.setHeader(header);
		this.setColType(dataType);
		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
		setAutoFormatter(dataType);
	}

	public void setAutoColumnDefault() {
		setText(getHeader());
		setCellValueFactory(new TreeItemPropertyValueFactory<>(getFieldName()));
		setId(getFieldName());
		setAutoFormatter(getFiTableCol().getColType());
	}

	private <S> void setAutoFormatter(OzColType dataType) {

		if (getColTypeDep() == OzColType.Double) {

			DecimalFormatSymbols dcmEuropeSymbolStyle = new DecimalFormatSymbols(locale);
			dcmEuropeSymbolStyle.setDecimalSeparator('.');
			dcmEuropeSymbolStyle.setGroupingSeparator(',');

			DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00", dcmEuropeSymbolStyle);
			//String strnumber = decimalFormat.format(number);

			// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir. (Callback fonksiyonunu icra eder) Callback<TableColumn<S, T>, TableCell<S, T>>
			setCellFactory(new ColumnFormatterForTreeTable<S, Double>(decimalFormat));
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (getColTypeDep() == OzColType.Integer) {

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

		if (getColTypeDep() == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}

	private void afterSetFiEditable(Boolean fiEditable) {
		super.setEditable(fiEditable);
	}

	public void setColType(String toString) {

		for (OzColType type : OzColType.values()) {
			if (toString.equals(type.toString())) {
				setColType(type);
			}
		}

	}

	//public enum DataType {Number, String, Money, Integer, Double, Date;}

	@Deprecated
	public OzColType getColTypeDep() {
		return getFiTableCol().getColType();
	}

	@Override
	public OzColType getColType() {
		return getFiTableCol().getColType();
	}

	public void setColType(OzColType dataType) {
		getFiTableCol().setColType(dataType);
	}

	//	@Override
//	public void setColType(OzColType colType) {
//		getOzTableCol().setColType(colType);
//	}

	@Override
	public Double getPrefSize() {
		return getFiTableCol().getPrefSize();
	}

	@Override
	public void setPrefSize(Double prefSize) {
		afterSetPrefSize(prefSize);
		getFiTableCol().setPrefSize(prefSize);
	}

	private void afterSetPrefSize(Double prefSize) {
		if (prefSize != null) {
			setPrefWidth(prefSize);
		}
	}

	@Override
	public Map<String, String> getMapStyle() {
		return getFiTableCol().getMapStyle();
	}

	@Override
	public void setMapStyle(Map<String, String> mapStyle) {
		getFiTableCol().setMapStyle(mapStyle);
	}


	@Override
	public Format getFormatter() {
		return getFiTableCol().getFormatter();
	}

	@Override
	public void setFormatter(Format formatter) {
		getFiTableCol().setFormatter(formatter);
	}

	@Override
	public Function<Object, String> getFuncFormatter() {
		return getFiTableCol().getFuncFormatter();
	}

	@Override
	public void setFuncFormatter(Function<Object, String> funcFormatter) {
		getFiTableCol().setFuncFormatter(funcFormatter);
	}

	@Override
	public String getColComment() {
		return getFiTableCol().getColComment();
	}

	@Override
	public void setColComment(String colComment) {
		getFiTableCol().setColComment(colComment);
	}

	@Override
	public Boolean getBoEnabled() {
		return getFiTableCol().getBoEnabled();
	}

	@Override
	public void setBoEnabled(Boolean boEnabled) {
		getFiTableCol().setBoEnabled(boEnabled);
	}

	@Override
	public OzColSummaryType getSummaryType() {
		return getFiTableCol().getSummaryType();
	}

	@Override
	public void setSummaryType(OzColSummaryType summaryType) {
		getFiTableCol().setSummaryType(summaryType);
	}

	@Override
	public Integer getPrintSize() {
		return getFiTableCol().getPrintSize();
	}

	@Override
	public void setPrintSize(Integer printSize) {
		getFiTableCol().setPrintSize(printSize);
	}

	@Override
	public Boolean getBoEditable() {
		return getFiTableCol().getBoEditable();
	}

	@Override
	public void setBoEditable(Boolean boEditable) {
		getFiTableCol().setBoEditable(boEditable);
		afterSetFiEditable(boEditable);
	}

	public FiCol<E> getFiTableCol() {
		if (fiTableCol == null) {
			fiTableCol = new FiCol<E>();
		}
		return fiTableCol;
	}

	public void setFiTableCol(FiCol<E> fiTableCol) {
		this.fiTableCol = fiTableCol;
		afterSetPrefSize(fiTableCol.getPrefSize());
	}

	public String getHeader() {
		return getFiTableCol().getHeaderName();
	}

	public void setHeader(String header) {
		getFiTableCol().setHeaderName(header);
	}

	public void setAutoEditor() {

		this.setCellFactory(new Callback<TreeTableColumn, TreeTableCell>() {

			@Override
			public TreeTableCell call(TreeTableColumn param) {

				//System.out.println("cell created");

				CheckBoxTreeTableCell cell = new CheckBoxTreeTableCell();
				cell.setAlignment(Pos.CENTER);

				cell.itemProperty().addListener((observable, oldValue, newValue) -> {
					//System.out.println("new Value: for fxtreecol " + newValue.toString());
				});

				return cell;

			}

//		         singleCol.setCellFactory(new Callback<TreeTableColumn<Employee,Boolean>,TreeTableCell<Employee,Boolean>>() {
//            @Override
//            public TreeTableCell<Employee,Boolean> call( TreeTableColumn<Employee,Boolean> p ) {
//                CheckBoxTreeTableCell<Employee,Boolean> cell = new CheckBoxTreeTableCell<Employee,Boolean>();
//                cell.setAlignment(Pos.CENTER);
//                return cell;
//            }
//        });

		});
	}

	@Override
	public IFiCol buildColType(OzColType colType) {
		return null;
	}

	@Override
	public FxTreeTableCol buildPrefSize(Double prefSize) {
		setPrefSize(prefSize);
		return this;
	}

	@Override
	public FxTreeTableCol buildPrintSize(Integer printSize) {
		this.setPrintSize(printSize);
		return this;
	}

	@Override
	public FxTreeTableCol buildHeader(String header) {
		setHeaderName(header);
		return this;
	}

	@Override
	public IFiCol buildSumType(OzColSummaryType summaryType) {
		return null;
	}

	public String getFieldName() {
		return getFiTableCol().getFieldName();
	}

	public void setFieldName(String fieldName) {
		getFiTableCol().setFieldName(fieldName);
	}

	public String getHeaderName() {
		return getFiTableCol().getHeaderName();
	}

	public void setHeaderName(String headerName) {
		getFiTableCol().setHeaderName(headerName);
	}

	@Override
	public Function getSummaryCalculateFn() {
		return getFiTableCol().getSummaryCalculateFn();
	}

	@Override
	public void setSummaryCalculateFn(Function summaryCalculateFn) {
		getFiTableCol().setSummaryCalculateFn(summaryCalculateFn);
	}

	public Boolean getBoFilterable() {
		return getFiTableCol().getBoFilterable();
	}

	public void setBoFilterable(Boolean boFilterable) {
		getFiTableCol().setBoFilterable(boFilterable);
	}

	public TextField getTxfFilter() {
		return null; //txfFilter;
	}

	public void setTxfFilter(TextField txfFilter) {
		//this.txfFilter = txfFilter;
	}

	public String getFilterNodeClass() {
		return getFiTableCol().getFilterNodeClass();
	}

	public void setFilterNodeClass(String filterNodeClass) {
		getFiTableCol().setFilterNodeClass(filterNodeClass);
	}

	public Consumer<E> getFnEditorSetOnActionWithEntity() {
		return getFiTableCol().getFnEditorSetOnActionWithEntity();
	}

	public void setFnEditorSetOnActionWithEntity(Consumer<E> fnEditorSetOnActionWithEntity) {
		getFiTableCol().setFnEditorSetOnActionWithEntity(fnEditorSetOnActionWithEntity);
	}

	public String getColEditorNodeText() {
		return getFiTableCol().getColEditorNodeText();
	}

	public void setColEditorNodeText(String colEditorNodeText) {
		//this.colFxNodeText = colFxNodeText;
		getFiTableCol().setColEditorNodeText(colEditorNodeText);
	}

	public BiConsumer<E, Button> getFnColButton() {
		return getFiTableCol().getFnColButton();
	}

	public void setFnColButton(BiConsumer<E, Button> fnColButton) {
		getFiTableCol().setFnColButton(fnColButton);
	}

	public Node getColFilterNode() {
		return getFiTableCol().getColFilterNode();
	}

	public void setColFilterNode(Node colFilterNode) {
		getFiTableCol().setColFilterNode(colFilterNode);
	}

	@Override
	public E getEntity() {
		return getFiTableCol().getEntity();
	}

	@Override
	public void setEntity(E entity) {
		getFiTableCol().setEntity(entity);
	}

	@Override
	public Object getFilterValue() {
		return getFiTableCol().getFilterValue();
	}

	@Override
	public void setFilterValue(Object filterValue) {
		getFiTableCol().setFilterValue(filterValue);
	}

	public Boolean getBoHidden() {
		return getFiTableCol().getBoHidden();
	}

	public void setBoHidden(Boolean hidden) {
		getFiTableCol().setBoHidden(hidden);
	}

	@Override
	public String getColEditorClass() {
		return getFiTableCol().getColEditorClass();
	}

	@Override
	public void setColEditorClass(String colEditorClass) {
		getFiTableCol().setColEditorClass(colEditorClass);
	}

	@Override
	public EventHandler<KeyEvent> getColFilterKeyEvent() {
		return getFiTableCol().getColFilterKeyEvent();
	}

	@Override
	public void setColFilterKeyEvent(EventHandler<KeyEvent> colFilterKeyEvent) {
		getFiTableCol().setColFilterKeyEvent(colFilterKeyEvent);
	}

	@Override
	public Boolean getBoOptional() {
		return getFiTableCol().getBoOptional();
	}

	@Override
	public void setBoOptional(Boolean optional) {
		getFiTableCol().setBoOptional(optional);
	}

	@Override
	public Boolean getBoExist() {
		return getFiTableCol().getBoExist();
	}

	@Override
	public void setBoExist(Boolean exist) {
		getFiTableCol().setBoExist(exist);
	}

	@Override
	public Integer getColIndex() {
		return getFiTableCol().getColIndex();
	}

	@Override
	public void setColIndex(Integer colIndex) {
		getFiTableCol().setColIndex(colIndex);
	}

	@Override
	public FxMigPane getPaneHeader() {
		return getFiTableCol().getPaneHeader();
	}

	@Override
	public void setPaneHeader(FxMigPane paneHeader) {
		getFiTableCol().setPaneHeader(paneHeader);
	}

	@Override
	public Boolean getBoNullable() {
		return getFiTableCol().getBoNullable();
	}

	@Override
	public void setBoNullable(Boolean boNullable) {
		getFiTableCol().setBoNullable(boNullable);
	}

	@Override
	public Boolean getBoNonUpdatable() {
		return getFiTableCol().getBoNonUpdatable();
	}

	@Override
	public void setBoNonUpdatable(Boolean boNonUpdatable) {
		getFiTableCol().setBoNonUpdatable(boNonUpdatable);
	}

	@Override
	public Boolean getBoKeyField() {
		return getFiTableCol().getBoKeyField();
	}

	@Override
	public void setBoKeyField(Boolean boKeyField) {
		getFiTableCol().setBoKeyField(boKeyField);
	}

	@Override
	public Boolean getBoNonEditableForForm() {
		return getFiTableCol().getBoNonEditableForForm();
	}

	@Override
	public void setBoNonEditableForForm(Boolean boNonEditableForForm) {
		getFiTableCol().setBoNonEditableForForm(boNonEditableForForm);
	}

	@Override
	public Node getColEditorNode() {
		return getFiTableCol().getColEditorNode();
	}

	@Override
	public void setColEditorNode(Node colEditorNode) {
		getFiTableCol().setColEditorNode(colEditorNode);
	}

	public BiConsumer<Object, Node> getFnEditorNodeRendererBeforeSettingValue() {
		return getFiTableCol().getFnEditorNodeRendererBeforeSettingValue();
	}

	public void setFnEditorNodeRendererOnLoad(BiConsumer<Object, Node> fnEditorNodeRendererOnLoad) {
		getFiTableCol().setFnEditorNodeRendererOnLoad(fnEditorNodeRendererOnLoad);
	}

	@Override
	public Object getColEditorValue() {
		return getFiTableCol().getColEditorValue();
	}

	@Override
	public void setColEditorValue(Object colEditorValue) {
		getFiTableCol().setColEditorValue(colEditorValue);
	}

	@Override
	public Boolean getBoRequired() {
		return getFiTableCol().getBoRequired();
	}

	@Override
	public void setBoRequired(Boolean boRequired) {
		getFiTableCol().setBoRequired(boRequired);
	}

	@Override
	public EventHandler<KeyEvent> getColEditorKeyEvent() {
		return getFiTableCol().getColEditorKeyEvent();
	}

	@Override
	public void setColEditorKeyEvent(EventHandler<KeyEvent> colEditorKeyEvent) {
		getFiTableCol().setColEditorKeyEvent(colEditorKeyEvent);
	}

	@Override
	public Function<Object, Object> getFnEditorNodeValueFormmatter() {
		return getFiTableCol().getFnEditorNodeValueFormmatter();
	}

	@Override
	public void setFnEditorNodeValueFormmatter(Function<Object, Object> fnEditorNodeValueFormmatter) {
		getFiTableCol().setFnEditorNodeValueFormmatter(fnEditorNodeValueFormmatter);
	}

	@Override
	public BiConsumer<Object, Node> getFnEditorNodeRendererAfterFormLoad() {
		return null;
	}

	@Override
	public void setFnEditorNodeRendererAfterFormLoad(BiConsumer<Object, Node> fnEditorNodeRendererAfterFormLoad) {

	}

	@Override
	public Boolean equalsColType(OzColType ozColType) {
		if (getColType() == null) return false;
		return getColType() == ozColType;
	}

	@Override
	public Boolean getBoDontExportExcelTemplate() {
		return getFiTableCol().getBoDontExportExcelTemplate();
	}

	@Override
	public void setBoDontExportExcelTemplate(Boolean boDontExportExcelTemplate) {
		getFiTableCol().setBoDontExportExcelTemplate(boDontExportExcelTemplate);
	}

	@Override
	public IfxNode getIfxNodeEditor() {
		return getFiTableCol().getIfxNodeEditor();
	}

	@Override
	public void setIfxNodeEditor(IfxNode ifxNodeEditor) {
		getFiTableCol().setIfxNodeEditor(ifxNodeEditor);
	}



}
