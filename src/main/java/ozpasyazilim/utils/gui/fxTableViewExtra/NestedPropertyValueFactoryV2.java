package ozpasyazilim.utils.gui.fxTableViewExtra;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;
import ozpasyazilim.utils.log.Loghelper;

public class NestedPropertyValueFactoryV2<S,T> implements
        Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    private String property;

    public NestedPropertyValueFactoryV2(String property) {
        this.property = property;
    }

    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {

        Object entity = param.getValue();
        T objValue = null;
        try {
            objValue = (T) PropertyUtils.getNestedProperty(entity, property);
        } catch (Exception e) {
            Loghelper.errorException(getClass(),e);
        }

        return new SimpleObjectProperty<T>(objValue);
    }

}