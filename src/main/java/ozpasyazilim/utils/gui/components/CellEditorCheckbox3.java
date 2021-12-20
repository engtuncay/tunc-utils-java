package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

/**
 * 
 * to : 22012018
 * 
 * @author tuncpc
 *
 */
public class CellEditorCheckbox3 extends AbstractCellEditor implements ItemListener, TableCellEditor
{
	
	OCheckBox ocBox;
	Consumer<OCheckBox> itemlistenerfunc = e -> {};
	Predicate<Integer> funcIsCompEnabledByRowIndex;  //= e -> false;

	//CustomObservable observable = new CustomObservable();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		ocBox = new OCheckBox();
		
		Boolean avalue = null;
		if (value == null) avalue = false;
		if (value instanceof Boolean) avalue = (Boolean) value;
		if (avalue == null) return new JLabel("error");

		ocBox.setSelected(avalue);
		ocBox.setRowindex(row);
		ocBox.setHorizontalAlignment(SwingConstants.CENTER);
		ocBox.addItemListener(this);
		
		if(funcIsCompEnabledByRowIndex!=null) {
			
			Boolean isCompEnabled = funcIsCompEnabledByRowIndex.test(row);
			if(isCompEnabled!=null)ocBox.setEnabled(isCompEnabled);
			
		}
		
		return ocBox;
	}

	@Override
	public Object getCellEditorValue() {
		return ocBox.isSelected();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		OCheckBox oc = (OCheckBox) e.getSource();
		//Loghelperr.getInstance(getClass()).info("dd:" + oc.getRowindex());
		super.stopCellEditing();
		
		itemlistenerfunc.accept(oc);
	}

	public void setItemListenerfunc(Consumer<OCheckBox> listenerfunc) {
		this.itemlistenerfunc = listenerfunc;
	}

	public void setFuncIsCompEnabledByRowIndex(Predicate<Integer> funcIsCompEnabledByRowIndex) {
		this.funcIsCompEnabledByRowIndex = funcIsCompEnabledByRowIndex;
	}
	

}
