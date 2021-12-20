package ozpasyazilim.utils.gui.components;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

public class CellRendererTogglebuttonLambda extends JToggleButton implements TableCellRenderer {

	private static final long serialVersionUID = -7617176338039685067L;
	JTable table;

	CellRendererTogglebuttonLambda(JTable table) {
		super();
		this.table = table;
		this.setMultiClickThreshhold(0);
	}

	public CellRendererTogglebuttonLambda() {

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {

		if (value == null) {
			setSelected(false);
			setText("X");
			return this;
		}

		boolean isActive = (Boolean) value;

		if (isActive) {
			this.setText("OK");
			this.setSelected(true);
			//value = Boolean.TRUE;
		} else {
			this.setText("X");
			this.setSelected(false);
			//value = Boolean.FALSE;
		}

		return this;

	}
}