package ozpasyazilim.utils.gui.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRendererObject extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected,
		boolean hasFocus, int row, int column) {

		Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row,
			column);

		/*
		 * if (isSelected)
		 * {
		 * cell.setBackground(Color.yellow);
		 * cell.setForeground(Color.black);
		 * }
		 * else if (row % 2 == 0)
		 * {
		 * cell.setBackground(Color.white);
		 * //cell.setBackground(Color.cyan);
		 * }
		 * else
		 * {
		 * cell.setBackground(Color.white);
		 * //cell.setBackground(Color.lightGray);
		 * }
		 */

		return cell;
	}
}
