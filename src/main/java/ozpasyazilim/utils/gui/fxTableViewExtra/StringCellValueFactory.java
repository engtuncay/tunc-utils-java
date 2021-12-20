package ozpasyazilim.utils.gui.fxTableViewExtra;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;

public class StringCellValueFactory implements
        Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>> {

    private String property;

    public StringCellValueFactory(String property) {
        this.property = property;
    }

    public ObservableValue<String> call(TableColumn.CellDataFeatures<Object, String> param) {
        Object value = param.getValue();
        String stringValue = null;
        try {
            //stringValue = BeanUtils.getNestedProperty(value, property);
            stringValue = PropertyUtils.getNestedProperty(value, property).toString();
        } catch (Exception e) {
            //logger.error("Error in StringCellValueFactory", e);
        }

        return new SimpleStringProperty(stringValue);
    }
}