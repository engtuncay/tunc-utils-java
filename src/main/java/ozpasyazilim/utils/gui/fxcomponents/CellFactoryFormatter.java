package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.text.Format;

/**
 * implements Callback( T,R ) : t giren eleman (input) , r çıkan eleman (output)
 * <p>
 * TabloColumn input , TableCell output
 *
 * @param <S> The type of the TableView generic type (i.e. S == TableView&lt;S&gt;).
 *            This should also match with the first generic type in TableColumn.
 *            (Tablo satırının entity türü)
 * @param <T> The type of the item contained within the Cell. (Hücrenin içindeki değer tipi)
 */
public class CellFactoryFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

	private Format format;

	public CellFactoryFormatter(Format format) {
		super();
		this.format = format;
	}

	/**
	 * Tablo hücresini render yapıyor
	 *
	 * @param tableColumn
	 * @return
	 */
	@Override
	public TableCell<S, T> call(TableColumn<S, T> tableColumn) {

		return new TableCell<S, T>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setGraphic(null);
				} else {
					setGraphic(new Label(format.format(item)));
				}
			}
		};
	}
}