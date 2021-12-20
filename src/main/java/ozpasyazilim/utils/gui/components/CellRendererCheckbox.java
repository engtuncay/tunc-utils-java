package ozpasyazilim.utils.gui.components;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class CellRendererCheckbox extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = -7617176338039685067L;
	JTable table;

	CellRendererCheckbox(JTable table) {
		super();
		this.table = table;
		this.setMultiClickThreshhold(0);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {

		if (table != null) {
			CellRendererCheckbox tableCellRenderer = (CellRendererCheckbox) table.getCellRenderer(
				row, column);
			tableCellRenderer.setBackground(isSelected
				? UIManager.getColor("Table.selectionBackground")
				: UIManager.getColor("Table.background"));
			tableCellRenderer.setSelected(Boolean.TRUE.equals(value));
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		}
		return this;
	}
}