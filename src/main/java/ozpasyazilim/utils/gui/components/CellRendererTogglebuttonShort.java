package ozpasyazilim.utils.gui.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CellRendererTogglebuttonShort extends STogglebutton implements TableCellRenderer {

	private static final long serialVersionUID = -7617176338039685067L;
	JTable table;
	//Integer count=0;
	//UUID id = UUID.randomUUID();

	CellRendererTogglebuttonShort(JTable table) {
		super();
		this.table = table;
		this.setMultiClickThreshhold(0);
	}

	public CellRendererTogglebuttonShort() {
		super();
		this.setMultiClickThreshhold(10);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {

		if (value == null) {
			this.setText("null");
			this.setSelected(false);
			this.setEnabled(true);
			//this.setCustomvalue(null);
			return this;
		}

		//this.setCustomvalue((Integer)value);

		Integer isActive = (Integer) value;

		if (isActive == 1) {
			this.setText("ok");
			this.setSelected(true);
			this.setEnabled(true);
			//value = Boolean.TRUE;
		} else if (isActive == 0) {
			this.setText("X");
			this.setSelected(false);
			this.setEnabled(true);
			//value = Boolean.FALSE;
		} else if (isActive == 2) {
			this.setText("Kısıtlı");
			this.setSelected(false);
			this.setEnabled(false);
		}

		return this;

	}

	public boolean iscellenabled(JTable table2, Object value, int row, int column) {
        return true;
    }
}