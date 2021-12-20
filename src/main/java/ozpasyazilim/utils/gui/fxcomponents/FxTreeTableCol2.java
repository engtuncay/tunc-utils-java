package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.table.IFxTableCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FxTreeTableCol aynı yapıda, o kullanılabilir
 *
 * @param <EntClazz>
 */
@FiDraft
public class FxTreeTableCol2<EntClazz> extends TreeTableColumn implements IFxTableCol<EntClazz> {

	FiCol<EntClazz> fiTableCol;

	public FxTreeTableCol2() {

	}

	public FxTreeTableCol2(String header) {
		super(header);
	}

	@Override
	public ObservableValue getCellObservableValue(Object item) {
		return null;
	}

	public FxTreeTableCol2(String header, String fieldName) {
		super(header);
		getFiCol().setHeaderName(header);
		getFiCol().setFieldName(fieldName);
		setId(fieldName);
		getFiCol().setId(fieldName);
		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
	}

	public FxTreeTableCol2(String header, String fieldName, OzColType dataType) {
		super(header);
		getFiCol().setFieldName(fieldName);
		setId(fieldName);
		getFiCol().setId(fieldName);
		setCellValueFactory(new TreeItemPropertyValueFactory<>(fieldName));
		getFiCol().setColType(dataType);
		setAutoFormatter(dataType);

	}

	private <S> void setAutoFormatter(OzColType dataType) {

		if ( getFiCol().getColType() == OzColType.Double) {

			Locale locale = new Locale("tr", "TR");
			DecimalFormatSymbols dcmEuropeSymbolStyle = new DecimalFormatSymbols(locale);
			dcmEuropeSymbolStyle.setDecimalSeparator('.');
			dcmEuropeSymbolStyle.setGroupingSeparator(',');

			DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00", dcmEuropeSymbolStyle);
			//String strnumber = decimalFormat.format(number);

			// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir. (Callback fonksiyonunu icra eder) Callback<TableColumn<S, T>, TableCell<S, T>>
			setCellFactory(new ColumnFormatterForTreeTable<S, Double>(decimalFormat));
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (getFiCol().getColType() == OzColType.Integer) {

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

		if ( getFiCol().getColType() == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}

	public FiCol<EntClazz> getFiCol() {
		if (fiTableCol == null) {
			fiTableCol = new FiCol<EntClazz>();
			getFiCol().setFxTreeTableCol(this);
		}
		return fiTableCol;
	}

	public void setFiCol(FiCol<EntClazz> fiTableCol) {this.fiTableCol = fiTableCol;}

	// Getter and Setter

}
