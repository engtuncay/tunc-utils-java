///*
// * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
// * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// */
//
//package ozpasyazilim.utils.gui.components;
//
//import com.sun.javafx.property.PropertyReference;
//import com.sun.javafx.scene.control.Logging;
//import javafx.beans.NamedArg;
//import javafx.beans.property.Property;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.beans.value.ObservableValue;
//import javafx.scene.control.TableCell;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableColumn.CellDataFeatures;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.MapValueFactory;
//import javafx.scene.control.cell.TreeItemPropertyValueFactory;
//import javafx.util.Callback;
//import sun.util.logging.PlatformLogger;
//import sun.util.logging.PlatformLogger.Level;
//
//
///**
// * A convenience implementation of the Callback interface, designed specifically
// * for use within the {@link TableColumn}
// * {@link TableColumn#cellValueFactoryProperty() cell value factory}. An example
// * of how to use this class is:
// *
// * <pre><code>
// * TableColumn&lt;Person,String&gt; firstNameCol = new TableColumn&lt;Person,String&gt;("First Name");
// * firstNameCol.setCellValueFactory(new PropertyValueFactory&lt;Person,String&gt;("firstName"));
// * </code></pre>
// *
// * In this example, the "firstName" string is used as a reference to an assumed
// * <code>firstNameProperty()</code> method in the <code>Person</code> class type
// * (which is the class type of the TableView
// * {@link TableView#itemsProperty() items} list). Additionally, this method must
// * return a {@link Property} instance. If a method meeting these requirements
// * is found, then the {@link TableCell} is populated with this ObservableValue<T>.
// * In addition, the TableView will automatically add an observer to the
// * returned value, such that any changes fired will be observed by the TableView,
// * resulting in the cell immediately updating.
// *
// * <p>If no method matching this pattern exists, there is fall-through support
// * for attempting to call get&lt;property&gt;() or is&lt;property&gt;() (that is,
// * <code>getFirstName()</code> or <code>isFirstName()</code> in the example
// * above). If a  method matching this pattern exists, the value returned from this method
// * is wrapped in a {@link ReadOnlyObjectWrapper} and returned to the TableCell.
// * However, in this situation, this means that the TableCell will not be able
// * to observe the ObservableValue for changes (as is the case in the first
// * approach above).
// *
// * <p>For reference (and as noted in the TableColumn
// * {@link TableColumn#cellValueFactory cell value factory} documentation), the
// * long form of the code above would be the following:
// *
// * <pre><code>
// * TableColumn&lt;Person,String&gt; firstNameCol = new TableColumn&lt;Person,String&gt;("First Name");
// * firstNameCol.setCellValueFactory(new Callback&lt;CellDataFeatures&lt;Person, String&gt;, ObservableValue&lt;String&gt;&gt;() {
// *     public ObservableValue&lt;String&gt; call(CellDataFeatures&lt;Person, String&gt; p) {
// *         // p.getValue() returns the Person instance for a particular TableView row
// *         return p.getValue().firstNameProperty();
// *     }
// *  });
// * }
// * </code></pre>
// *
// * @see TableColumn
// * @see TableView
// * @see TableCell
// * @see TreeItemPropertyValueFactory
// * @see MapValueFactory
// * @param <S> The type of the class contained within the TableView.items list.
// * @param <T> The type of the class contained within the TableColumn cells.
// * @since JavaFX 2.0
// */
//public class PropertyValueFactory2<S,T> implements Callback<CellDataFeatures<S,T>, ObservableValue<T>> {
//
//    private final String property;
//
//    private Class<?> columnClass;
//    private String previousProperty;
//    private PropertyReference<T> propertyRef;
//
//    /**
//     * Creates a default PropertyValueFactory to extract the value from a given
//     * TableView row item reflectively, using the given property name.
//     *
//     * @param property The name of the property with which to attempt to
//     *      reflectively extract a corresponding value for in a given object.
//     */
//    public PropertyValueFactory2(@NamedArg("property") String property) {
//        this.property = property;
//    }
//
//    /** {@inheritDoc} */
//    @Override public ObservableValue<T> call(CellDataFeatures<S,T> param) {
//        return getCellDataReflectively(param.getValue());
//    }
//
//    /**
//     * Returns the property name provided in the constructor.
//     */
//    public final String getProperty() { return property; }
//
//    private ObservableValue<T> getCellDataReflectively(S rowData) {
//        if (getProperty() == null || getProperty().isEmpty() || rowData == null) return null;
//
//        try {
//            // we attempt to cache the property reference here, as otherwise
//            // performance suffers when working in large data models. For
//            // a bit of reference, refer to RT-13937.
//            if (columnClass == null || previousProperty == null ||
//                    ! columnClass.equals(rowData.getClass()) ||
//                    ! previousProperty.equals(getProperty())) {
//
//                // create a new PropertyReference
//                this.columnClass = rowData.getClass();
//                this.previousProperty = getProperty();
//                this.propertyRef = new PropertyReference<T>(rowData.getClass(), getProperty());
//            }
//
//            if (propertyRef.hasProperty()) {
//                return propertyRef.getProperty(rowData);
//            } else {
//                T value = propertyRef.get(rowData);
//                return new ReadOnlyObjectWrapper<T>(value);
//            }
//        } catch (IllegalStateException e) {
//            // log the warning and move on
//            final PlatformLogger logger = Logging.getControlsLogger();
//            if (logger.isLoggable(Level.WARNING)) {
//               logger.finest("Can not retrieve property '" + getProperty() +
//                        "' in PropertyValueFactory: " + this +
//                        " with provided class type: " + rowData.getClass(), e);
//            }
//        }
//
//        return null;
//    }
//}
