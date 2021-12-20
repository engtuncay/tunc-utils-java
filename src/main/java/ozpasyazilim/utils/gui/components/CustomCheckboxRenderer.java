package ozpasyazilim.utils.gui.components;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CustomCheckboxRenderer extends JCheckBox implements TableCellRenderer {

	public CustomCheckboxRenderer() {
		setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {
		System.out.println("checkboxrenderer-gettablecellrenderer");
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			//super.setBackground(table.getSelectionBackground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		if (value == null) value = false; //optional
		if ((boolean) value) {

		}

		setSelected((value != null && ((Boolean) value).booleanValue()));
		return this;
	}
}
