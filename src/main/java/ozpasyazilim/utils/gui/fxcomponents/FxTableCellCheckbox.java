package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.*;

public class FxTableCellCheckbox<S> extends TableCell<S, Boolean> {

	private CheckBox checkBox;

	public FxTableCellCheckbox() {

		checkBox = new CheckBox();
		//checkBox.setDisable(true);
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if(oldValue!=newValue){
					//System.out.println("index:"+getIndex());
					commitEdit(newValue);
				}
				//if (isEditing())commitEdit(newValue == null ? false : newValue);
			}
		});
		this.setGraphic(checkBox);
		this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		this.setEditable(true);
	}

	@Override
	public void cancelEdit() {
		System.out.println("cancel edit");
		super.cancelEdit();
		//checkBox.setDisable(true);
	}

	public void commitEdit(Boolean newValue) {
		System.out.println("commit edit");
		// isediting true olmaz , commitEdit event olayını duyurmuyor/fırlatmıyor.
		super.commitEdit(newValue);

		final TableView<S> table = getTableView();
		if (table != null) {
			// Inform the TableView of the edit being ready to be committed.
			TableColumn.CellEditEvent editEvent = new TableColumn.CellEditEvent(
					table,
					table.getEditingCell(),
					TableColumn.editCommitEvent(),
					newValue
			);

			Event.fireEvent(getTableColumn(), editEvent);
		}

		// inform parent classes of the commit, so that they can switch us
		// out of the editing state.
		// This MUST come before the updateItem call below, otherwise it will
		// call cancelEdit(), resulting in both commit and cancel events being
		// fired (as identified in RT-29650)
		// super.commitEdit(newValue);

		// update the item within this cell, so that it represents the new value
		updateItem(newValue, false);

		if (table != null) {
			// reset the editing cell on the TableView
			table.edit(-1, null);

			// request focus back onto the table, only if the current focus
			// owner has the table as a parent (otherwise the user might have
			// clicked out of the table entirely and given focus to something else.
			// It would be rude of us to request it back again.
			//ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
		}

		//checkBox.setDisable(true);
	}

	@Override
	public void startEdit() {
		System.out.println("start edit");
		super.startEdit();
		if (isEmpty()) {
			return;
		}
		//checkBox.setDisable(false);
		checkBox.requestFocus();
	}

	Integer i=0;
	@Override
	protected void updateItem(Boolean item, boolean empty) {
		//System.out.println("update item"+ (i++));
		super.updateItem(item, empty);
		if (isEmpty()) {
			setGraphic(null);
		} else {
			//if (!isEmpty()) {
			checkBox.setSelected((Boolean) item);
		}
	}

}

