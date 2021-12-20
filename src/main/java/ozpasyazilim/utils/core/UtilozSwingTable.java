package ozpasyazilim.utils.core;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class UtilozSwingTable {
    public UtilozSwingTable() {
    }

    public static void alignCellText(JTable table, int col ,  int align){
	DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer)table.getColumnModel().getColumn(col).getCellRenderer();
	if (dtcr==null) {
	    dtcr = new DefaultTableCellRenderer();
	    table.getColumnModel().getColumn(col).setCellRenderer(dtcr);
	}
	dtcr.setHorizontalAlignment(align);
    }

    public static void setColumnWidth(JTable table , int col , int width)
    {
	table.getColumnModel().getColumn(col).setMinWidth( width );
	table.getColumnModel().getColumn(col).setMaxWidth( width );
	table.getColumnModel().getColumn(col).setPreferredWidth( width );
    }



}
