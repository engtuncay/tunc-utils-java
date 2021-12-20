package ozpasyazilim.utils.gui.fxcomponents;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;
// DRAFT
public class FxTableCellCheckbox2<S> extends TableCell<S, CheckBox> {

	private final CheckBox actionButton;

	public FxTableCellCheckbox2(String label, Function<S, S> function) {

		//css style class !!!
		this.getStyleClass().add("action-button-table-cell");

		this.actionButton = new CheckBox(label);
		this.actionButton.setOnAction((ActionEvent e) -> {
			function.apply(getCurrentItem());
		});
		this.actionButton.setMaxWidth(Double.MAX_VALUE);
	}

	public S getCurrentItem() {
		return (S) getTableView().getItems().get(getIndex());
	}

	/**
	 *
	 * @param text
	 * @param function
	 * @param <S>
	 * @return callback object
	 */
	public static <S> Callback<TableColumn<S, Button>, TableCell<S, CheckBox>> forTableColumn(String text, Function<S, S> function) {
		return param -> new FxTableCellCheckbox2<>(text, function);
	}

	@Override
	public void updateItem(CheckBox item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(actionButton);
		}
	}
}