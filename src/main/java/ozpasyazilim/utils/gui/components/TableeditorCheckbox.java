package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class TableeditorCheckbox extends AbstractCellEditor implements TableCellEditor,ItemListener {

	private JCheckBox checkBox = new JCheckBox();
	private Boolean isDisabledifselected=false;
	Valuedelegate valuedelegate;
	
	public TableeditorCheckbox() {
		checkBox.addItemListener(this);
	}
	
	@Override
	public Object getCellEditorValue() {
        return checkBox.isSelected();
    }

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		if(value==null)value=false; // TODO added without test. to-15-01-2018 
		Boolean compvalue= (Boolean) value;
		checkBox.setSelected(compvalue);

		Boolean enabled=true;
		if( isDisabledifselected==true) {
			if(valuedelegate!=null){
				if(valuedelegate.getdelegatevalue(row)!=null){
					enabled=(Boolean)valuedelegate.getdelegatevalue(row);
				}
			}
		}
		
		checkBox.setEnabled(enabled);
		return checkBox;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
        this.stopCellEditing();
    }

	public void setIsDisabledifselected(Boolean isDisabledifselected) {
		this.isDisabledifselected = isDisabledifselected;
	}

	public Valuedelegate getValuedelegate() {
		return valuedelegate;
	}

	public void setValuedelegate(Valuedelegate valuedelegate) {
		this.valuedelegate = valuedelegate;
	}
	
}
