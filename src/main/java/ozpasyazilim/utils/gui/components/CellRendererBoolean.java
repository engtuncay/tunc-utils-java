package ozpasyazilim.utils.gui.components;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;


public class CellRendererBoolean extends JCheckBox implements TableCellRenderer, UIResource {

	// for dışında olacak
	Boolean used = false;

	private static final long serialVersionUID = 1L;
	private final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public CellRendererBoolean() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorderPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {

		// her bir table, kendisi için bir adet renderer oluşturur,
		// o renderer sayesinde her hücresine component üretir
		// for içinde olacak
		if (used == false) {
			// Loghelper.getInstance(this.getClass()).info("boolean renderer çalıştı");
			used = true;
		}

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		setSelected(value != null && ((Boolean) value).booleanValue());
		if (value == null) setSelected(false);

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(noFocusBorder);
		}
		return this;
	}
}