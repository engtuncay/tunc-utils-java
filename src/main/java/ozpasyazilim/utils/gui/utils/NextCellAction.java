package ozpasyazilim.utils.gui.utils;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

public class NextCellAction extends AbstractAction {

    private JTable table;

    public NextCellAction(JTable table) {
	this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

	int col = table.getSelectedColumn();
	int row = table.getSelectedRow();

	if(table.isEditing())table.getCellEditor(row,col).stopCellEditing();

	int colCount = table.getColumnCount();
	int rowCount = table.getRowCount();

	col++;
	if (col >= colCount) {
	    col = 0;
	    row++;
	}

	if (row >= rowCount) {
	    row = 0;
	}

	table.getSelectionModel().setSelectionInterval(row, row);
	table.getColumnModel().getSelectionModel().setSelectionInterval(col, col);
    }

}