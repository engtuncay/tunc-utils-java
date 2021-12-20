package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ozpasyazilim.utils.core.FiString;

public class CellRendererFloat extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
	    int row, int column) {
	JLabel cell = (JLabel) super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);

	if (isSelected) {
	    cell.setBackground(Color.yellow);
	    cell.setForeground(Color.black);
	} else if (row % 2 == 0) {
	    cell.setBackground(Color.white);
	    // cell.setBackground(Color.cyan);
	} else {
	    cell.setBackground(Color.white);
	    // cell.setBackground(Color.lightGray);
	}
	if (obj == null)
	    return cell;
	cell.setText(FiString.formatNumberParagosterimi((Number) obj));
	cell.setHorizontalAlignment(4);

	return cell;
    }
}
