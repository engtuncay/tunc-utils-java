package ozpasyazilim.utils.gui.components;

import javax.swing.table.AbstractTableModel;

public class CheckBoxTableModel extends AbstractTableModel {
     private static final long serialVersionUID = 289604576288633559L;
     private String[] columnNames = null;
     private Object[][] rowData = null;
 
    public CheckBoxTableModel(Object[][] rowData,  String[] columnNames){
        this.rowData=rowData;
        this.columnNames=columnNames;
    }
      
    public int getColumnCount() {
        return columnNames.length;
    }
 
    public int getRowCount() {
        return rowData.length;
    }
 
    public String getColumnName(int col) {
        return columnNames[col];
    }
 
    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }
 
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
         
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if(col==0){
            return true;
        }else{
            return false;
        }
        //return true;
    }
 
    public void setAllCheckBoxes(boolean value){
        for(int i=0; i<rowData.length; i++){
            rowData[i][0] = value;
        }
        this.fireTableDataChanged();
    }
         
    /*
     * returns true if all checkboxes are true
     */
    public boolean isAllCheckBoxesTrue(){
        boolean flag = true;
        boolean tempFlag;
        for(int i=0; i<rowData.length; i++){
            tempFlag = Boolean.TRUE.equals(rowData[i][0]);
            if(!tempFlag){
                flag=false;
            }
        }
             
        return flag;
    }
         
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}