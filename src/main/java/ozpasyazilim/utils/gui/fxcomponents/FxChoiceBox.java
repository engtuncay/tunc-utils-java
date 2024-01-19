package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import ozpasyazilim.utils.core.FiRegExp;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class FxChoiceBox<T> extends ChoiceBox<T> {

	private StringProperty txValue = new SimpleStringProperty();
	private StringProperty txFilterAutoComplete;
	private FilteredList<T> filteredList;
	private Tooltip filterTooltip;
	private Predicate<T> predicateFilter1;
	private Predicate<T> predicateFilterAutoComplete;

	public FxChoiceBox() {

	}

	public FxChoiceBox(ObservableList<T> items) {
		super(items);
	}

	public String getTxValue() {
		return txValue.get();
	}

	public StringProperty txValueProperty() {
		return txValue;
	}

	public void setTxValue(String txValue) {
		this.txValue.set(txValue);
	}

	public void setSelectedComboItemByTxValue() {

		if (FiString.isEmpty(getTxValue()) || isEmpty()) return;

		ObservableList<T> items = getItems();

		for (int index = 0; index < items.size(); index++) {

			T item = items.get(index);
			if (item == null) continue;

			if (item.toString().equals(getTxValue())) {
				setSelectedIndex(index);
			}

		}
	}

	public void activateAutoComplete() {
		//setOnKeyPressed(this::handleOnKeyPressed);
		addEventHandler(KeyEvent.KEY_RELEASED, this::handleOnKeyPressed);

		getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			//Loghelper.get(getClass()).debug("selection fired");
			if (newValue != null) {
				getFilterTooltip().hide();
				//hide();
			}

		});

		focusedProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue){
				getFilterTooltip().hide();
			}
//			else{
//				//
//			}
		});

		//setTooltip(getSearchTooltip());
//		setOnHidden(this::handleOnHiding);
//		setOnShown(event -> Loghelper.get(getClass()).debug("setonshown"));

		// changeListener : observable,oldValue,newValue (showingProperty'nin yeni değeri)
		showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
			if (isNowShowing) {
//				setTxFilter(getTxFilter().trim());
				//getFilterTooltip().setText(getTxFilter());
				showFiFilterToolTip();
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

		txFilterAutoCompleteProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue == null) {
				getFilterTooltip().setText("");
			} else {
				getFilterTooltip().setText(newValue);
			}

			//Loghelper.get(getClass()).debug("New Value:"+newValue);
			//Loghelper.get(getClass()).debug("TxFilter:"+getTxFilter());
			//Loghelper.get(getClass()).debug("----");


			setPredicateFilterAutoComplete(t -> {
				//Loghelper.get(getClass()).debug("T:"+t.toString());
				if (FiString.isEmpty(getTxFilterAutoComplete())) return true;
				if (t.toString().toLowerCase().contains(getTxFilterAutoComplete().toLowerCase())) {
					return true;
				}
				return false;
			});

			fiExecuteFilterPredicates();

			//show();

			//Loghelper.get(getClass()).debug("Show box");
			//Loghelper.get(getClass()).debug("F size:"+getFilteredList().size());

			//showFilterToolTip();
			//bazen açık görünüp kapalı oluyor - filtered list 0 olunca
			if (!getFilteredList().isEmpty()) {
				// filteredlist size 0 olunca show yapılırsa problem oluyor
				showFiFilterToolTip();
				show(); //show olunca triggerdan showFilterTooltip yapacak
			} else {
				hide(); // hide trigger olunca filterTooltip ne hide eder.
				// tekrar gösterilir (sonuç olmasa da)
				showFiFilterToolTip();
			}

		});

	}

	public void fiExecuteFilterPredicates() {

		Predicate<T> predicateAll = t -> true;

		if(getPredicateFilter1()!=null) predicateAll=predicateAll.and(getPredicateFilter1());

		if(getPredicateFilterAutoComplete()!=null) predicateAll=predicateAll.and(getPredicateFilterAutoComplete());

		getFilteredList().setPredicate(predicateAll);
	}

	private void handleOnKeyPressed(KeyEvent e) {
		//ObservableList<T> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();

		if (code == KeyCode.ENTER){
			return;
		}

		if (code == KeyCode.ESCAPE) {
//			Loghelper.get(getClass()).debug("esc pressed");
			setTxFilterAutoComplete(""); //this.txFilter = ""; //filter = "";
			return;
		}

		if (code == KeyCode.BACK_SPACE && getTxFilterAutoComplete().length() > 0) {
			//Loghelper.get(getClass()).debug("backspace");
			setTxFilterAutoComplete(getTxFilterAutoComplete().substring(0, getTxFilterAutoComplete().length() - 1));
			return;
		}

		if (code == KeyCode.SPACE && getTxFilterAutoComplete().length() > 0) {
			//Loghelper.get(getClass()).debug("");
//			setTxFilter(getTxFilter() + " ");
//			showFilterToolTip();
			//e.consume();
		}

		//Pattern.compile(regex)
		if (FiRegExp.checkAlpanumericAndSpaceAndTire(e.getText())) { //code.isLetterKey()
			setTxFilterAutoComplete(getTxFilterAutoComplete() + e.getText()); //filter += e.getText();
			//Loghelper.get(getClass()).debug("txFilter:" + getTxFilter());
			return;
		}

	}


	// XNOTE tooltip penceresi oluşturma
	public void showFiFilterToolTip() {
		//Loghelper.get(getClass()).debug("show Filter Tooltip");

		if (getTxFilterAutoComplete().length() == 0) {
			getFilterTooltip().hide();
		} else {
			Window stage = getScene().getWindow();
			double posX = stage.getX() + getBoundsInParent().getMinX();
			double posY = stage.getY() + getBoundsInParent().getMinY();
			Bounds bounds = localToScreen(getBoundsInLocal());
			//cmb.getTooltip().show(stage, posX, posY);
			getFilterTooltip().show(stage, bounds.getMinX() - 10, bounds.getMinY() - 50);

			//show();
		}
	}

//

	public void setItemsAsFilteredListMain(Collection dataList) {
		if (dataList == null) dataList = new ArrayList();
		FilteredList filteredList = new FilteredList(FXCollections.observableArrayList(dataList), getFilterPredicatesAll());
		setFilteredListFi(filteredList);

		setItems(filteredList);//.addAll(listRapor);
		//eventsAfterTableViewDataChange();
	}

	private void setFilteredListFi(FilteredList<T> filteredList) {
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


	public void setSelectedIndex(Integer index) {
		if (index == null) return;
		Platform.runLater(() -> {
			getSelectionModel().select(index);
		});

	}

	public void clearSelectionFi() {
		Platform.runLater(() -> {
			getSelectionModel().clearSelection();
		});
	}

	public boolean isEmpty() {
		if (getItems() == null) return true;
		if (getItems().size() == 0) return true;
		return false;
	}

	public String getTxFilterAutoComplete() {
		return txFilterAutoCompleteProperty().get();
	}

	public StringProperty txFilterAutoCompleteProperty() {
		if (txFilterAutoComplete == null) {
			txFilterAutoComplete = new SimpleStringProperty("");
		}
		return txFilterAutoComplete;
	}

	public void setTxFilterAutoComplete(String txFilterAutoComplete) {
		txFilterAutoCompleteProperty().set(txFilterAutoComplete);
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

	public Predicate<T> getPredicateFilter1() {
		return predicateFilter1;
	}

	public void setPredicateFilter1(Predicate<T> predicateFilter1) {
		this.predicateFilter1 = predicateFilter1;
	}

	public Predicate<T> getPredicateFilterAutoComplete() {
		return predicateFilterAutoComplete;
	}

	public void setPredicateFilterAutoComplete(Predicate<T> predicateFilterAutoComplete) {
		this.predicateFilterAutoComplete = predicateFilterAutoComplete;
	}

	public void activateBackSpaceClearSelection() {
		this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
			if (event.getCode() == KeyCode.BACK_SPACE) {
				this.clearSelectionFi();
			}
		});
	}
}
