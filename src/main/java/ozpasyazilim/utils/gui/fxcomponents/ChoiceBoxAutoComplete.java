package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

import java.util.stream.Stream;

/**
 * 
 * Uses a combobox tooltip as the suggestion for auto complete and updates the
 * combo box itens accordingly <br />
 * It does not work with space, space and escape cause the combobox to hide and
 * clean the filter ... Send me a PR if you want it to work with all characters
 * -> It should be a custom controller - I KNOW!
 * 
 * @author wsiqueir
 *
 * @param <T>
 */
public class ChoiceBoxAutoComplete<T> {

	private ChoiceBox<T> cmb;
	String filter = "";
	private ObservableList<T> originalItems;
	Tooltip tooltip= new Tooltip();

	public ChoiceBoxAutoComplete(ChoiceBox<T> cmb) {
		this.cmb = cmb;
		originalItems = FXCollections.observableArrayList(cmb.getItems());
		cmb.setTooltip(new Tooltip());
//		cmb.setOnKeyPressed(this::handleOnKeyPressed);
		cmb.addEventHandler(KeyEvent.KEY_PRESSED, this::handleOnKeyPressed);
		cmb.setOnHidden(this::handleOnHiding);
	}

	public void handleOnKeyPressed(KeyEvent e) {
		cmb.setTooltip(tooltip);
		ObservableList<T> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();

		if (code.isLetterKey()) {
			filter += e.getText();
		}
		if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
			filter = filter.substring(0, filter.length() - 1);
			cmb.getItems().setAll(originalItems);
		}
		if (code == KeyCode.ESCAPE) {
			filter = "";
		}
		if (filter.length() == 0) {
			filteredList = originalItems;
			cmb.getTooltip().hide();
		} else {
			Stream<T> itens = cmb.getItems().stream();
			String txtUsr = filter.toString().toLowerCase();
			itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
			cmb.getTooltip().setText(txtUsr);
			Window stage = cmb.getScene().getWindow();
			double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
			double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
			Bounds bounds = cmb.localToScreen(cmb.getBoundsInLocal());
			//cmb.getTooltip().show(stage, posX, posY);
			cmb.getTooltip().show(stage, bounds.getMinX()-10, bounds.getMinY()-30);
			cmb.show();
		}
		cmb.getItems().setAll(filteredList);
	}

	public void handleOnHiding(Event e) {
		filter = "";
		tooltip.hide();
		//cmb.getTooltip().hide();
		cmb.setTooltip(null);
		T s = cmb.getSelectionModel().getSelectedItem();
		cmb.getItems().setAll(originalItems);
		cmb.getSelectionModel().select(s);
	}



}