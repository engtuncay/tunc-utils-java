package ozpasyazilim.utils.gui.fxTableViewExtra;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.log.Loghelper;

public class NestedPropertyValueFactory implements Callback<TableColumn.CellDataFeatures<Object, Object>, ObservableValue<Object>> {

	private String property;

	public NestedPropertyValueFactory(String property) {
		this.property = property;
	}

	public ObservableValue<Object> call(TableColumn.CellDataFeatures<Object, Object> param) {

		Object value = param.getValue();
		Object objValue = null;
		try {
			objValue = PropertyUtils.getNestedProperty(value, property);
		} catch (Exception e) {
			//logger.error("Error in StringCellValueFactory", e);
			Loghelper.get(getClass()).error(FiException.exceptionToStrMain(e));
		}

		return new SimpleObjectProperty<>(objValue);
	}

}