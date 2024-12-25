package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.gui.components.ComboItemObj;
import ozpasyazilim.utils.gui.components.ComboItemText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FxComboBox<T> extends ComboBox<T> {

    // Componentin Ekrandan görünen değerden(txLabel) farklı olarak arka planda tutulan değeri
    private StringProperty txValue; // = new SimpleStringProperty();
    private ObjectProperty objValue;
    private FilteredList<T> filteredList;
    private StringProperty txFilter;
    private Tooltip filterTooltip;
    //OzColType comboBoxValueType;

    public FxComboBox() {
        super();

    }

    public FxComboBox(ObservableList<T> items) {
        super(items);
    }

    public void setItemsAsList(List<T> list) {
        setItems(FXCollections.observableList(list));
    }

    public void addListenerFiSelectedItemChange(ChangeListener<? super T> listener) {
        getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void addListenerValue(ChangeListener<? super T> listener) {
        valueProperty().addListener(listener);
    }


    public enum ComboAfterSelectType {
        ZeroIndex, Null, DoNothing
    }

    public T getSelectedItemFi() {
        return getSelectionModel().getSelectedItem();
    }

    public String getTxValue() {
        return txValueProperty().get();
    }

    public StringProperty txValueProperty() {
        if (txValue == null) {
            txValue = new SimpleStringProperty();
        }
        return txValue;
    }

    public void setTxValue(String txValue) {
        this.txValueProperty().set(txValue);
    }

    public void setSelectedComboItemByTxValue() {

        if (FiString.isEmpty(getTxValue()) || isEmptyComboBox()) return;

        ObservableList<ComboItemText> itemsCombo = (ObservableList<ComboItemText>) getItems();

        for (int index = 0; index < itemsCombo.size(); index++) {

            ComboItemText item = itemsCombo.get(index);
            if (item.getValue() == null) continue;

            if (item.getValue().equals(getTxValue())) {
                setSelectedItemFiAsync(index);
            }

        }
    }

    public void setSelectedComboItemByObjValue() {

        if (getObjValue()==null || isEmptyComboBox()) return;

        ObservableList<ComboItemObj> itemsCombo = (ObservableList<ComboItemObj>) getItems();

        for (int index = 0; index < itemsCombo.size(); index++) {

            ComboItemObj item = itemsCombo.get(index);
            if (item.getValue() == null) continue;

            if (item.getValue().equals(getObjValue())) {
                setSelectedItemFiAsync(index);
            }

        }
    }

    public boolean isEmptyComboBox() {
        if (getItems() == null) return true;
        if (getItems().size() == 0) return true;
        return false;
    }

    public void setSelectedItemFiAsync(Integer index) {
        if (index == null) return;

        Platform.runLater(() -> {
            getSelectionModel().select(index);
        });

    }

    public void setSelectedItemFiSync(Integer index) {
        if (index == null) return;
        getSelectionModel().select(index);
    }

    public void clearSelectionFi() {
        Platform.runLater(() -> {
            getSelectionModel().clearSelection();
        });
    }

    public void addComboItem(T item) {
        getItems().add(item);
    }

    public <PrmEnt> void addFiList(FxComboBox<ComboItemText> compCombo, List<PrmEnt> list, Function<PrmEnt, Object> fnValue, Function<PrmEnt, Object> fnLabel) {

        for (PrmEnt prmEnt : list) {
            compCombo.addComboItem(ComboItemText.build(fnLabel.apply(prmEnt), fnValue.apply(prmEnt)));
        }

    }

    public void activateSelectedItem(FxComboBox<ComboItemText> compCombo) {

        if (FiString.isEmpty(getTxValue())) {
            compCombo.getSelectionModel().select(0);
        } else {
            compCombo.setFiSelectedItemByTxValue(compCombo);
        }

    }

    public void setFiSelectedItemByTxValue(FxComboBox<ComboItemText> compCombo) {

        if (FiString.isEmpty(getTxValue()) || isEmptyComboBox()) return;

        ObservableList<ComboItemText> items = compCombo.getItems();

        for (int index = 0; index < items.size(); index++) {

            ComboItemText item = items.get(index);
            if (item.getValue() == null) continue;

            if (item.getValue().equals(getTxValue())) {
                setSelectedItemFiAsync(index);
            }

        }
    }

    public void activateSetComboItemAfterAction(Boolean afterSelectZeroIndex) {
        if (FiBool.isTrue(afterSelectZeroIndex)) {
            activateSetComboItemAfterAction(ComboAfterSelectType.ZeroIndex);
        } else {
            activateSetComboItemAfterAction(ComboAfterSelectType.DoNothing);
        }
    }

    public void activateSetComboItemAfterAction(ComboAfterSelectType comboAfterSelectType) {

        addListenerFiSelectedItemChange((observable, oldValue, newValue) -> {

            if (newValue == null) return;

            ComboItemText cbi = (ComboItemText) newValue;

            if (cbi.getOnAction() != null) {
                cbi.getOnAction().run();
            }

            if (comboAfterSelectType == ComboAfterSelectType.ZeroIndex) {
                Platform.runLater(() -> {
                    getSelectionModel().select(0);
                });
            }

            if (comboAfterSelectType == ComboAfterSelectType.Null) {
                Platform.runLater(() -> {
                    getSelectionModel().select(null);
                });
            }

        });
    }

    public void activateSetNullAfterAction() {
        activateSetComboItemAfterAction(ComboAfterSelectType.Null);
    }

    public void activateAutoComplete() {
//		setOnKeyPressed(this::handleOnKeyPressed);
        addEventHandler(KeyEvent.KEY_RELEASED, this::handleOnKeyPressed);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//			Loghelper.get(getClass()).debug("selection fired");
            if (newValue != null) {
                getFilterTooltip().hide();
//				hide();
            }

        });

        //setTooltip(getSearchTooltip());
//		setOnHidden(this::handleOnHiding);
//		setOnShown(event -> Loghelper.get(getClass()).debug("setonshown"));

        showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                showFilterToolTip();
//				Loghelper.get(getClass()).debug("showing");
            } else {
//				Loghelper.get(getClass()).debug("hiding choice box");
                getFilterTooltip().hide();
                //T s = getSelectionModel().getSelectedItem();
                //cmb.getTooltip().hide();
                //getFilteredList().setPredicate(t -> true);
                //getSelectionModel().select(s);
            }
        });


//		onMouseClickedProperty().addListener((observable, oldValue, newValue) -> {
//			Loghelper.get(getClass()).debug("mouse clicked");
//		});

        txFilterProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                getFilterTooltip().setText("");
            } else {
                getFilterTooltip().setText(newValue);
            }

            //Loghelper.get(getClass()).debug("New Value:"+newValue);
            //Loghelper.get(getClass()).debug("TxFilter:"+getTxFilter());
            //Loghelper.get(getClass()).debug("----");

            getFilteredList().setPredicate(t -> {
                //Loghelper.get(getClass()).debug("T:"+t.toString());
                if (FiString.isEmpty(getTxFilter())) return true;
                if (t.toString().toLowerCase().contains(getTxFilter().toLowerCase())) {
                    return true;
                }
                return false;
            });

            //Loghelper.get(getClass()).debug("Show box");
            //Loghelper.get(getClass()).debug("F size:"+getFilteredList().size());

            showFilterToolTip();
            // bazen açık görünüp kapalı oluyor - filtered list 0 olunca
            if (getFilteredList().size() > 0) {
                // filteredlist size 0 olunca show yapılırsa problem oluyor
                show(); //show olunca triggerdan showFilterTooltip yapacak
            } else {
                hide();
            }
        });
    }

    public void handleOnKeyPressed(KeyEvent e) {

        //ObservableList<T> filteredList = FXCollections.observableArrayList();
        KeyCode code = e.getCode();

        if (code == KeyCode.ENTER) {
            return;
        }

        if (!FiString.isEmpty(e.getText())) { //code.isLetterKey()
            setTxFilter(getTxFilter() + e.getText()); //filter += e.getText();
            //Loghelper.get(getClass()).debug("txFilter:" + getTxFilter());
            return;
        }

        if (code == KeyCode.BACK_SPACE && getTxFilter().length() > 0) {
            //Loghelper.get(getClass()).debug("backspace");
            setTxFilter(getTxFilter().substring(0, getTxFilter().length() - 1));
        }

        if (code == KeyCode.SPACE && getTxFilter().length() > 0) {
            //Loghelper.get(getClass()).debug("");
//			setTxFilter(getTxFilter() + " ");
//			showFilterToolTip();
            //e.consume();
        }

        if (code == KeyCode.ESCAPE) {
//			Loghelper.get(getClass()).debug("esc pressed");
            setTxFilter(""); //this.txFilter = ""; //filter = "";
        }

//		cmb.getItems().setAll(filteredList);

    }


    public void showFilterToolTip() {
        //Loghelper.get(getClass()).debug("show Filter Tooltip");
        if (getTxFilter().length() == 0) {
            getFilterTooltip().hide();
        } else {
            Window stage = getScene().getWindow();
            double posX = stage.getX() + getBoundsInParent().getMinX();
            double posY = stage.getY() + getBoundsInParent().getMinY();
            Bounds bounds = localToScreen(getBoundsInLocal());
            //cmb.getTooltip().show(stage, posX, posY);
            getFilterTooltip().show(stage, bounds.getMinX() - 10, bounds.getMinY() - 30);
            //show();
        }
    }

    public void setItemsAsFilteredListMain(Collection dataList) {
        if (dataList == null) dataList = new ArrayList();
        FilteredList filteredList = new FilteredList(FXCollections.observableArrayList(dataList), getFilterPredicatesAll());
        setFilteredList(filteredList);
        setItems(filteredList);
        //eventsAfterTableViewDataChange();
    }

    public void setFilteredList(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }

    private Predicate getFilterPredicatesAll() {
        Predicate predAll = ent -> true;

        //if (getPredFilterLocal() != null) predAll = predAll.and(getPredFilterLocal());

        // bu out olmalı
        //if (getPredFilterRemote() != null) predAll = predAll.and(getPredFilterRemote());

        //if (FiCollection.isNotEmpty(getPredFilterExtraList())) {
        //Loghelperr.getInstance(getClass()).debug("Size Filter Out : "+ getListPredFilterExtra().size());
//		for (Predicate predItem : getPredFilterExtraList()) {
//			predAll = predAll.and(predItem);
//		}
        //}

        return predAll;
    }

    public String getTxFilter() {
        return txFilterProperty().get();
    }

    public StringProperty txFilterProperty() {
        if (txFilter == null) {
            txFilter = new SimpleStringProperty("");
        }
        return txFilter;
    }

    public void setTxFilter(String txFilter) {
        txFilterProperty().set(txFilter);
    }

    public Tooltip getFilterTooltip() {
        if (filterTooltip == null) {
            filterTooltip = new Tooltip();
        }
        return filterTooltip;
    }

    public void setFilterTooltip(Tooltip filterTooltip) {
        this.filterTooltip = filterTooltip;
    }

    public void trigSelectedItemListenerFi(ChangeListener<? super T> listener) {
        getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public Object getObjValue() {
        return objValueProperty().get();
    }

    public ObjectProperty objValueProperty() {
        if (objValue == null) {
            objValue = new SimpleObjectProperty();
        }
        return objValue;
    }

    public void setObjValue(Object objValue) {
        this.objValueProperty().set(objValue);
    }

}
