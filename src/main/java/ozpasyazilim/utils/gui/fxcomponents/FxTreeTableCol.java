package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;
import ozpasyazilim.utils.table.IFxTableCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.text.*;
import java.util.Date;
import java.util.Locale;

public class FxTreeTableCol<E> extends TreeTableColumn implements IFxTableCol<E> { //implements IFiCol<E> // commented 23-11-17

	FiCol<E> fiCol;

	public Locale locale = new Locale("tr", "TR");

	public FxTreeTableCol() {

	}

	public FxTreeTableCol(String header) {
		super(header);
	}

//	@Override
//	public ObservableValue getCellObservableValue(Object item) {
//		return null;
//	}

	public FxTreeTableCol(String header, String fieldName) {
		super(header);
		getRefFiCol().setOfcTxFieldName(fieldName);
		this.setId(fieldName);
		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
	}

	public FxTreeTableCol(String header, String fieldName, OzColType dataType) {
		super(header);
		setId(fieldName);
		getRefFiCol().setOfcTxFieldName(fieldName);
		getRefFiCol().setOfcTxHeader(header);
		getRefFiCol().setColType(dataType);
		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
		setAutoFormatter(dataType);
	}

	public void setAutoColumnDefaultByFiCol() {
		setText(getRefFiCol().getOfcTxHeader());
		setCellValueFactory(new TreeItemPropertyValueFactory<>(getRefFiCol().getOfcTxFieldName()));
		setId(getRefFiCol().getOfcTxFieldName());
		setAutoFormatter(getRefFiCol().getColType());
	}

	private <S> void setAutoFormatter(OzColType dataType) {

		OzColType colType = getRefFiCol().getColType();

		if (colType == OzColType.Double) {

			DecimalFormatSymbols dcmEuropeSymbolStyle = new DecimalFormatSymbols(locale);
			dcmEuropeSymbolStyle.setDecimalSeparator('.');
			dcmEuropeSymbolStyle.setGroupingSeparator(',');

			DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00", dcmEuropeSymbolStyle);
			//String strnumber = decimalFormat.format(number);

			// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir. (Callback fonksiyonunu icra eder) Callback<TableColumn<S, T>, TableCell<S, T>>
			setCellFactory(new ColumnFormatterForTreeTable<S, Double>(decimalFormat));
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (colType == OzColType.Integer) {

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

		if (colType == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}

//	@Override
	public void setPrefSize(Double prefSize) {
		afterSetPrefSize(prefSize);
		getRefFiCol().setPrefSize(prefSize);
	}

	private void afterSetPrefSize(Double prefSize) {
		if (prefSize != null) {
			setPrefWidth(prefSize);
		}
	}

//	@Override
//	public void setPrintSize(Integer printSize) {
//		getFiCol().setPrintSize(printSize);
//	}

//	@Override
	public Boolean getBoEditable() {
		return getRefFiCol().getBoEditable();
	}

//	@Override
//	public void setBoEditable(Boolean boEditable) {
//		getFiCol().setBoEditable(boEditable);
//		afterSetFiEditable(boEditable);
//	}

	public FiCol<E> getRefFiCol() {
		if (fiCol == null) {
			fiCol = new FiCol<E>();
		}
		return fiCol;
	}

	public void setRefFiCol(FiCol<E> fiCol) {
		this.fiCol = fiCol;
		afterSetPrefSize(fiCol.getPrefSize());
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
		});

//singleCol.setCellFactory(new Callback<TreeTableColumn<Employee,Boolean>,TreeTableCell<Employee,Boolean>>() {
//@Override
//public TreeTableCell<Employee,Boolean> call( TreeTableColumn<Employee,Boolean> p ) {
//CheckBoxTreeTableCell<Employee,Boolean> cell = new CheckBoxTreeTableCell<Employee,Boolean>();
//cell.setAlignment(Pos.CENTER);
//return cell;
//}
//});


	}

//	@Override
//	public FxTreeTableCol buiPrefSize(Double prefSize) {
//		setPrefSize(prefSize);
//		return this;
//	}

//	@Override
//	public FxTreeTableCol buildPrintSize(Integer printSize) {
//		getFiCol().setPrintSize(printSize);
//		return this;
//	}

//	public FxTreeTableCol buiHeader(String header) {
//		getFiCol().setHeaderName(header);
//		return this;
//	}














}
