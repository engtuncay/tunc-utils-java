package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import ozpasyazilim.utils.gui.components.ComboItem;

public class AutoShowComboBoxHelper {

    public static void activate(final ComboBox<ComboItem> comboBox) {

        //, final Callback<String, String> textBuilder

        final ObservableList<ComboItem> items = FXCollections.observableArrayList(comboBox.getItems());

        comboBox.getEditor().textProperty().addListener((ov, o, n) -> {
            if (n.equals(comboBox.getSelectionModel().getSelectedItem())) {
                return;
            }

            //comboBox.hide();
            final FilteredList<ComboItem> filtered = items.filtered(s -> s.toString().toLowerCase().contains(n.toLowerCase()));
            if (filtered.isEmpty()) {
                comboBox.getItems().setAll(items);
            } else {
                comboBox.getItems().setAll(filtered);
                comboBox.show();
            }
        });
    }
}