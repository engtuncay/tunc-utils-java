package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.control.*;
import javafx.util.Callback;

import java.text.Format;

/**
 *
 *  Callback( T,R ) t giren eleman , r çıkan eleman
 *
 * @param <S>
 * @param <T>
 */
public class ColumnFormatterForTreeTable<S, T> implements Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {

	private Format format;

    public ColumnFormatterForTreeTable(Format format) {
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
    public TreeTableCell<S, T> call(TreeTableColumn<S, T> tableColumn) {

        return new TreeTableCell<S, T>() {
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