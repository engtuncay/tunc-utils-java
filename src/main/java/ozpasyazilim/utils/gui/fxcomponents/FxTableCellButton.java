package ozpasyazilim.utils.gui.fxcomponents;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class FxTableCellButton<S> extends TableCell<S, Button> {

	private final Button actionButton;

	public FxTableCellButton(String label, Function<S, S> functionn) {

		//css style class !!!
		this.getStyleClass().add("action-button-table-cell");

		this.actionButton = new Button(label);
		this.actionButton.setOnAction((ActionEvent e) -> {
			functionn.apply(getCurrentItem());
		});
		this.actionButton.setMaxWidth(Double.MAX_VALUE);
	}

	public S getCurrentItem() {
		return (S) getTableView().getItems().get(getIndex());
	}

	/**
	 * CallBack Function üretir , tablecolumn<E,F> parametre alır, TableCell<E,F> döndüren fonksiyon
	 *
	 * @param text
	 * @param function
	 * @param <S>
	 * @return callback object
	 */
	public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String text, Function<S, S> function) {
		return param -> new FxTableCellButton<>(text, function);
	}

	@Override
	public void updateItem(Button item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(actionButton);
		}
	}
}