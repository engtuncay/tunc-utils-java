package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class CustomComboCellEditor extends AbstractCellEditor implements TableCellEditor {

	JComboBox<ComboItem> combobox; // = new JTextField();

	public CustomComboCellEditor(JComboBox comboBox) {

		combobox = comboBox;
		// editorComponent = comboBox;
		// comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		/*
		 * delegate = new EditorDelegate() {
		 * public void setValue(Object value) {
		 * comboBox.setSelectedItem(value);
		 * }
		 * 
		 * public Object getCellEditorValue() {
		 * return comboBox.getSelectedItem();
		 * }
		 * 
		 * public boolean shouldSelectCell(EventObject anEvent) {
		 * if (anEvent instanceof MouseEvent) {
		 * MouseEvent e = (MouseEvent)anEvent;
		 * return e.getID() != MouseEvent.MOUSE_DRAGGED;
		 * }
		 * return true;
		 * }
		 * public boolean stopCellEditing() {
		 * if (comboBox.isEditable()) {
		 * // Commit edited value.
		 * comboBox.actionPerformed(new ActionEvent(
		 * DefaultCellEditor.this, 0, ""));
		 * }
		 * return super.stopCellEditing();
		 * }
		 * };
		 */
		// comboBox.addActionListener(delegate);


	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex,
		int vColIndex) {

		// ((JComboBox) combobox).setSelectedItem(value);
		// Loghelper.getInstance(this.getClass()).info("editore gelen deger"+(String)value);

		setSelectedValue(combobox, (String) value);

		return combobox;
	}


	public void setSelectedValue(JComboBox comboBox, String value) {
		ComboItem item;
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			item = (ComboItem) comboBox.getItemAt(i);

			if (item.getLabel().equals(value)) {
				// Loghelper.getInstance(this.getClass()).info("seçildi"+(String)value);

				comboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public Object getCellEditorValue() {
        return combobox.getSelectedItem();

    }



}