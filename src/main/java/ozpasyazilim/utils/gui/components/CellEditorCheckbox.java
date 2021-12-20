package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;

/*
 * can extends AbstractCellEditor
 */

public class CellEditorCheckbox extends DefaultCellEditor implements ItemListener {

	protected static JCheckBox checkBox = new JCheckBox();

	public CellEditorCheckbox() {
		super(checkBox);
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		//checkBox.setBackground(Color.white);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
		int row, int column) {
		if (value == null) return checkBox;

		checkBox.addItemListener(this);

		if (((Boolean) value).booleanValue()) checkBox.setSelected(true);
		else
			checkBox.setSelected(false);

		return checkBox;
	}

	public Object getCellEditorValue() {
		if (checkBox.isSelected() == true) return new Boolean(true);
		else
			return new Boolean(false);
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
    }

	@Override
	public void cancelCellEditing() {
        super.cancelCellEditing();
    }

	@Override
	public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
        super.removeCellEditorListener(l);
    }

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

	@Override
	public boolean stopCellEditing() {
        return true;
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
        //        System.out.println("Firing!");
    }
}
