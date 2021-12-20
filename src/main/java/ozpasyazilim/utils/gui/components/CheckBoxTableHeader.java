package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
 
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
 
/*
 * I modified this version:
 * http://www.coderanch.com/t/433703/GUI/java/Select-All-checkbox-Jtable
 */
public class CheckBoxTableHeader extends JCheckBox implements TableCellRenderer, MouseListener, ItemListener {
    private static final long serialVersionUID = -518033763678852926L;
    private CheckBoxTableHeader rendererComponent;  
    private JTable table;
    private int column;  
    private boolean mousePressed = false;
    private boolean currentValue = false;
    private boolean flag = true;
     
    public CheckBoxTableHeader(JTable table) {  
        rendererComponent = this;  
        this.setHorizontalAlignment(JLabel.LEFT);
        rendererComponent.addItemListener(this);
        this.table = table;
    }  
         
         
    public void itemStateChanged(ItemEvent e) {  
        Object source = e.getSource();  
        if (source instanceof AbstractButton == false) return;  
        boolean checked = e.getStateChange() == ItemEvent.SELECTED;  
        CheckBoxTableHeader checkBoxHeader = (CheckBoxTableHeader)(e.getSource());
        checkBoxHeader.setValue(new Boolean(checked));
        JTable table  = checkBoxHeader.getTable();
         
        // only do if the flag is set to true
        // else if the flag is true, set it to false
        if(checkBoxHeader.getFlag()){
            ((CheckBoxTableModel) table.getModel()).setAllCheckBoxes(new Boolean(checked));
        }else{
            checkBoxHeader.setFlag(true);
        }
    }
         
         
    public Component getTableCellRendererComponent(  
            JTable table, Object value,  
            boolean isSelected, boolean hasFocus, int row, int column) {  
        if (table != null) {  
            JTableHeader header = table.getTableHeader();  
            if (header != null) {  
                rendererComponent.setForeground(header.getForeground());  
                rendererComponent.setBackground(header.getBackground());  
                rendererComponent.setFont(header.getFont());  
                header.addMouseListener(rendererComponent);  
            }  
        }  
        setColumn(column);  
        rendererComponent.setText("");
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return rendererComponent;  
    }  
    protected void setColumn(int column) {  
        this.column = column;  
    }  
    public int getColumn() {  
        return column;  
    }
     
    public JTable getTable() {  
        return table;  
    }
         
    public boolean getValue() {  
        return currentValue;  
    }
         
         
    public void setValue(boolean currentValue) {  
        this.currentValue=currentValue;  
    }
     
    /**
     * this flag determines if the itemlistener for the 
     * checkbox should uncheck, or uncheck all checkboxes, 
     * when it is run. this is reset to true each time the event is run
     * @param flag if true then everything will run normally
     */
    public void setFlag(boolean flag){
        this.flag=flag;
    }
         
    /**
     * gets the flag that determines if the itemlistener 
     * for the checkbox should uncheck, or uncheck all checkboxes, 
     * when it is run. this is reset to true each time the event is run
     * @return if true then everything will run normally
     */
    public boolean getFlag(){
        return flag;
    }
         
    private void handleClickEvent(MouseEvent e) {  
        if (mousePressed) {  
            mousePressed=false;  
            JTableHeader header = (JTableHeader)(e.getSource());  
            JTable tableView = header.getTable();  
            TableColumnModel columnModel = tableView.getColumnModel();  
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());  
            int column = tableView.convertColumnIndexToModel(viewColumn);  
 
            if (viewColumn == this.column && e.getClickCount() > 0 && column != -1) {
                doClick();  
            }  
        }
    }
     
    /**
     * only do repaint if the user clicked on the checkbox, 
     * and not if the user clicked somewhere outside it
     */
    public void mouseClicked(MouseEvent e) {  
        JTableHeader header = (JTableHeader)(e.getSource());  
        switch (e.getID()) {
        case MouseEvent.MOUSE_CLICKED:
        case MouseEvent.MOUSE_PRESSED:
        case MouseEvent.MOUSE_RELEASED:
            JTable tableView = header.getTable();
 
            TableColumnModel columnModel = tableView.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
 
            Rectangle bounds = tableView.getCellRect(-1, viewColumn, true);
 
            if (e.getX() > bounds.x + 16) {
                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    return;
                }
            }else{
                handleClickEvent(e);
                ((JTableHeader)e.getSource()).repaint();
            }
        }
    }  
    public void mousePressed(MouseEvent e) {  
        mousePressed = true;  
    }  
    public void mouseReleased(MouseEvent e) {  
    }  
    public void mouseEntered(MouseEvent e) {  
    }  
    public void mouseExited(MouseEvent e) {  
    }  
}