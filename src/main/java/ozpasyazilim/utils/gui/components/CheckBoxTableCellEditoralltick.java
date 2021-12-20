package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
 
 
public class CheckBoxTableCellEditoralltick extends JCheckBox implements TableCellEditor, ItemListener {
    private static final long serialVersionUID = 1415957817360568471L;
    private Vector editorListeners;
    private CheckBoxTableHeader checkBoxHeader;
    private CheckBoxTableModel tableModel;
    private JTable table;
         
    public CheckBoxTableCellEditoralltick(JTable table, CheckBoxTableModel tableModel, CheckBoxTableHeader checkBoxHeader){
        addItemListener(this);
        editorListeners = new Vector();
        this.table=table;
        this.tableModel=tableModel;
        this.checkBoxHeader=checkBoxHeader;
             
        /*
         * should throw an exception if the 
         * tablemodel or checkboxheader does not belong to the table
         */
    }
 
    public void itemStateChanged(ItemEvent e) {
        for(int i=0; i<editorListeners.size(); ++i){
            ((CellEditorListener)editorListeners.
                    elementAt(i)).editingStopped(
                            new ChangeEvent(this));
        }
        boolean checked = e.getStateChange() == ItemEvent.SELECTED;
        if(checked==true){
            // if all are true while the checkBox in the header is false, 
            // then change the checkBox in the header to true
            if(tableModel.isAllCheckBoxesTrue()){
                if(!checkBoxHeader.getValue()){
                    checkBoxHeader.setFlag(false);
                    checkBoxHeader.setSelected(true);
                    table.getTableHeader().repaint();
                    table.repaint();
                }
            }
        }
        
        // if not all are true while the checkBox in the header are true, 
        // then change the check box in the header to false
        if(checked==false){
            if(!tableModel.isAllCheckBoxesTrue()){
                if(checkBoxHeader.getValue()){
                    checkBoxHeader.setFlag(false);
                    checkBoxHeader.setSelected(false);
                    table.getTableHeader().repaint();
                    table.repaint();
                }
            }
        }           
    }
 
 
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.setSelected(Boolean.TRUE.equals(value));
        setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
 
        return this;
    }
 
    public Object getCellEditorValue(){
        return this.isSelected();
    }
 
    public boolean isCellEditable(EventObject anEvent){
        MouseEvent e = (MouseEvent)anEvent;
             
        JTable table = (JTable)(e.getSource());
        Point p = e.getPoint();
        int viewColumn = table.columnAtPoint(p);
        int viewRow = table.rowAtPoint(p);
         
        Rectangle bounds = table.getCellRect(viewRow, viewColumn, true);
         
        /*
         * checks to see that the coordinates are on the checkBox, 
         * and not too much to the left or right sides
         * x-axis
         */
        if (e.getX() > bounds.x + 14 || e.getX() < bounds.x + 2) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                return false;
            }
        }
        /*if the above was true then check the y coordinates of mouseclick, 
         * so that you did'nt click below or above the checkBox. 
         * the value of y is less up than down 
         * (opposite to a normal mathematical coordinate system)
         * y-axis
         */
        if (e.getY() > bounds.y+13 || e.getY() < bounds.y+2) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                return false;
            }
        }
             
        return true;
    }
 
    public boolean shouldSelectCell(EventObject anEvent){
        return true;
    }
 
    public boolean stopCellEditing(){
        return true;
    }
 
    public void cancelCellEditing() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
                ((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
            }          
        }
    }
 
    public void addCellEditorListener(CellEditorListener l){
        editorListeners.add(l);
    }
 
    public void removeCellEditorListener(CellEditorListener l){
        editorListeners.remove(l);
    }
}