package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observer;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;



// XIM custom jtable celleditor
/*
 * 
 */

public class CellEditorCheckbox2 extends AbstractCellEditor implements ItemListener, TableCellEditor

{

	private JCheckBox checkbox;
	CustomObservable observable = new CustomObservable();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Boolean avalue = null;
		if (value == null) avalue = false;
		if (value instanceof Boolean) avalue = (Boolean) value;
		if (avalue == null) return new JLabel("error");

		getCheckbox().setSelected(avalue);
		getCheckbox().setHorizontalAlignment(SwingConstants.LEFT);
		getCheckbox().addItemListener(this);
		return getCheckbox();
	}

	@Override
	public Object getCellEditorValue() {
		return getCheckbox().isSelected();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		stopCellEditing();
		observable.setChanged();
		observable.notifyObservers();
	}

	public JCheckBox getCheckbox() {
		if (checkbox == null) {
			checkbox = new JCheckBox();
		}
		return checkbox;
	}

	public void setCheckbox(JCheckBox checkbox) {
		this.checkbox = checkbox;
	}

	// add listener gibi
	public void addObserver(Observer obs) {
		observable.addObserver(obs);
	}

	public CustomObservable getObservable() {
		return observable;
	}

}
