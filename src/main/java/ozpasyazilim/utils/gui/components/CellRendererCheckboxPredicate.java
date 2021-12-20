package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.util.function.Predicate;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

public class CellRendererCheckboxPredicate<E> extends JCheckBox implements TableCellRenderer,
	UIResource {

	Predicate<E> custompredicate = e -> false;
	CustomTablemodelList4<E> customTablemodelList4;

	private static final long serialVersionUID = 1L;
	private final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public CellRendererCheckboxPredicate() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorderPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {

		// her bir table, kendisi için bir adet renderer oluşturur, 
		// o renderer sayesinde her hücresine component üretir

		// for içinde olacak

		JCheckBox mycomp = new JCheckBox();

		if (isSelected) {
			mycomp.setForeground(table.getSelectionForeground());
			mycomp.setBackground(table.getSelectionBackground());
		} else {
			mycomp.setForeground(table.getForeground());
			mycomp.setBackground(table.getBackground());
		}

		mycomp.setSelected(value != null && ((Boolean) value).booleanValue());
		if (value == null) {
			mycomp.setSelected(false);
		}

		if (hasFocus) {
			mycomp.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			mycomp.setBorder(noFocusBorder);
		}

		if (custompredicate.test(getCustomTablemodelList4().getItem(table.convertRowIndexToModel(
			row)))) {
			mycomp.setEnabled(false);
		} else {
			mycomp.setEnabled(true);
		}
		return mycomp;
	}

	public Predicate<E> getCustompredicate() {
		return custompredicate;
	}

	public void setCustompredicate(Predicate<E> custompredicate) {
		this.custompredicate = custompredicate;
	}

	public CustomTablemodelList4<E> getCustomTablemodelList4() {
		return customTablemodelList4;
	}

	public void setCustomTablemodelList4(CustomTablemodelList4<E> customTablemodelList4) {
		this.customTablemodelList4 = customTablemodelList4;
	}
}