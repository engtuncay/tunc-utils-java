package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import ozpasyazilim.utils.table.IFxTableCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @param <EntClazz>
 */
public class FxTableCol2<EntClazz> extends TableColumn implements IFxTableCol<EntClazz> {

	private ObjectProperty<FiCol<EntClazz>> refFiCol = new SimpleObjectProperty<>();

	public FxTableCol2() {
		super();
		setupFiTableListener();
	}

	public FxTableCol2(String fiHeader) {
		super(fiHeader);
		setupFiTableListener();
		getRefFiCol().setOfcTxHeader(fiHeader);
	}

	public FxTableCol2(String fiHeader, String fieldName) {
		super(fiHeader);
		setupFiTableListener();
		getRefFiCol().setOfcTxFieldName(fieldName);
		//this.setId(fieldName);
		getRefFiCol().setOfcTxHeader(fiHeader);
		setCellValueFactory(new PropertyValueFactory<>(fieldName));
	}

	/**
	 * FiCol'dan FxTableCol üretimi
	 *
	 * @param refFiCol
	 */
	public FxTableCol2(FiCol refFiCol) {
		super();
		setupFiTableListener();
		setRefFiCol(refFiCol);
		// added 29-04-2023 tor (setId)
		setId(refFiCol.getOfcTxFieldName());
		refFiCol.setFxTableCol2(this);
		refFiCol.setTableColumnFx(this);
	}


	private void setupFiTableListener() {
		refFiColProperty().addListener((observable, oldValue, newValue) -> {
			setupFiTableParamListeners();
		});
	}

	private void setupFiTableParamListeners() {

		getRefFiCol().prefSizeProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null){
				setPrefWidth((Double) newValue);
			}
		});

		// initial setup
		if(getRefFiCol().getPrefSize()!=null ){ //&& getFiTableCol().getPrefSize()!=0d
			//Loghelperr.getInstance(getClass()).debug(" Col:"+getFiTableCol().getFiHeader() + " Size:"+getFiTableCol().getPrefSize().toString());
			setPrefWidth(getRefFiCol().getPrefSize());
		}

	}


	public void styleAlignCenterFi() {
		setStyle("-fx-alignment: CENTER");
	}

	//@Override
	public FiCol getRefFiCol() {
		if (refFiCol.get() == null) {
			//fiTableCol = new SimpleObjectProperty<>();
			refFiCol.set(new FiCol<>());
			refFiCol.get().setTableColumnFx(this);
		}
		return refFiCol.get();
	}

	public ObjectProperty<FiCol<EntClazz>> refFiColProperty() {
//		if (fiTableCol == null) {
//			fiTableCol = new SimpleObjectProperty<>();
//		}
		return refFiCol;
	}

	public void setRefFiCol(FiCol prmfiCol) {
//		if (fiTableCol == null) {
//			fiTableCol = new SimpleObjectProperty<FiTableCol<EntClazz>>();
//		}
		this.refFiCol.set(prmfiCol);
		prmfiCol.setTableColumnFx(this);
	}

	public <S> void setAutoFormatter(OzColType dataType) {

		if (getRefFiCol().getColType() == OzColType.Double) {

			Locale locale = new Locale("tr", "TR");
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator('.');
			otherSymbols.setGroupingSeparator(',');
			DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00", otherSymbols);
			//String strnumber = decimalFormatter.format(number);

			setCellFactory(new CellFactoryFormatter<S, Double>(decimalFormatter));
			setStyle("-fx-alignment: CENTER-RIGHT;");

		}

		if (getRefFiCol().getColType() == OzColType.Integer) {

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

		if (getRefFiCol().getColType() == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}


}
