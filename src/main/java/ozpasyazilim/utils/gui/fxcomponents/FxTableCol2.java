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

	private ObjectProperty<FiCol<EntClazz>> fiCol = new SimpleObjectProperty<>();

	public FxTableCol2() {
		super();
		setupFiTableListener();
	}

	public FxTableCol2(String fiHeader) {
		super(fiHeader);
		setupFiTableListener();
		getFiCol().setHeaderName(fiHeader);
	}

	public FxTableCol2(String fiHeader, String fieldName) {
		super(fiHeader);
		setupFiTableListener();
		getFiCol().setFieldName(fieldName);
		//this.setId(fieldName);
		getFiCol().setHeaderName(fiHeader);
		setCellValueFactory(new PropertyValueFactory<>(fieldName));
	}

	public FxTableCol2(FiCol fiCol) {
		super();
		setupFiTableListener();
		setFiCol(fiCol);
		fiCol.setFxTableCol2(this);
		fiCol.setTableColumnFx(this);
	}


	private void setupFiTableListener() {
		fiColProperty().addListener((observable, oldValue, newValue) -> {
			setupFiTableParamListeners();
		});
	}

	private void setupFiTableParamListeners() {

		getFiCol().prefSizeProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null){
				setPrefWidth((Double) newValue);
			}
		});

		// initial setup
		if(getFiCol().getPrefSize()!=null ){ //&& getFiTableCol().getPrefSize()!=0d
			//Loghelperr.getInstance(getClass()).debug(" Col:"+getFiTableCol().getFiHeader() + " Size:"+getFiTableCol().getPrefSize().toString());
			setPrefWidth(getFiCol().getPrefSize());
		}

	}


	public void styleAlignCenterFi() {
		setStyle("-fx-alignment: CENTER");
	}

	@Override
	public FiCol getFiCol() {
		if (fiCol.get() == null) {
			//fiTableCol = new SimpleObjectProperty<>();
			fiCol.set(new FiCol<>());
			fiCol.get().setTableColumnFx(this);
		}
		return fiCol.get();
	}

	public ObjectProperty<FiCol<EntClazz>> fiColProperty() {
//		if (fiTableCol == null) {
//			fiTableCol = new SimpleObjectProperty<>();
//		}
		return fiCol;
	}

	public void setFiCol(FiCol prmfiCol) {
//		if (fiTableCol == null) {
//			fiTableCol = new SimpleObjectProperty<FiTableCol<EntClazz>>();
//		}
		this.fiCol.set(prmfiCol);
		prmfiCol.setTableColumnFx(this);
	}

	public <S> void setAutoFormatter(OzColType dataType) {

		if (getFiCol().getColType() == OzColType.Double) {

			Locale locale = new Locale("tr", "TR");
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator('.');
			otherSymbols.setGroupingSeparator(',');
			DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,##0.00", otherSymbols);
			//String strnumber = decimalFormatter.format(number);

			setCellFactory(new CellFactoryFormatter<S, Double>(decimalFormatter));
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

		if (getFiCol().getColType() == OzColType.Date) {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			setCellFactory(new CellFactoryFormatter<S, Date>(f));
		}


	}


}
