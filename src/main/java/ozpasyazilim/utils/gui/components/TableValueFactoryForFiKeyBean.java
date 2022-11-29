/*
 * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package ozpasyazilim.utils.gui.components;


import javafx.beans.NamedArg;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import ozpasyazilim.utils.datatypes.FiKeyBean;

// * @see TableColumn
// * @see TableView
// * @see TableCell
// * @see TreeItemPropertyValueFactory
// * @see MapValueFactory
// * @param <S> The type of the class contained within the TableView.items list.
// * @param <T> The type of the class contained within the TableColumn cells.
public class TableValueFactoryForFiKeyBean<S> implements Callback<CellDataFeatures<S,Object>, ObservableValue<Object>> {

    private final String property;

//    private Class<?> columnClass;
//    private String previousProperty;
//    private PropertyReference<T> propertyRef;

    /**
     * Creates a default PropertyValueFactory to extract the value from a given
     * TableView row item reflectively, using the given property name.
     *
     * @param property The name of the property with which to attempt to
     *      reflectively extract a corresponding value for in a given object.
     */
    public TableValueFactoryForFiKeyBean(@NamedArg("property") String property) {
        this.property = property;
    }

    /** {@inheritDoc} */
    @Override public ObservableValue<Object> call(CellDataFeatures<S,Object> param) {
        return getCellDataReflectively(param.getValue());
    }

    /**
     * Returns the property name provided in the constructor.
     */
    public final String getProperty() { return property; }

    private ObservableValue<Object> getCellDataReflectively(S rowData) {
        if (getProperty() == null || getProperty().isEmpty() || rowData == null) return null;

        if(rowData instanceof FiKeyBean){

            FiKeyBean fiKeyBean = (FiKeyBean) rowData;

            if(fiKeyBean.containsKey(getProperty())){
                Object value = fiKeyBean.get(getProperty());
                return new ReadOnlyObjectWrapper<Object>(value);
            }
        }

        return null;

//        } catch (IllegalStateException e) {
//            // log the warning and move on
//            final PlatformLogger logger = Logging.getControlsLogger();
//            if (logger.isLoggable(Level.WARNING)) {
//               logger.finest("Can not retrieve property '" + getProperty() +
//                        "' in PropertyValueFactory: " + this +
//                        " with provided class type: " + rowData.getClass(), e);
//            }
//        }

    }
}
