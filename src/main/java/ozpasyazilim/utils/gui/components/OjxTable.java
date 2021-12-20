package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import mark.utils.swing.table.ObjectTablemodel2;
import org.jdesktop.swingx.JXTable;

import java.awt.event.KeyEvent;
import java.util.Date;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import ozpasyazilim.utils.gui.utils.NextCellAction;

public class OjxTable extends JXTable{

    private CellEditorCheckbox3 checkboxEditorGlobal;

	public void setconfigsimple() {
	this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.setAutoCreateRowSorter(true);
    }
    
    public OjxTable setAutoSelectionSingle() {
    	this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	return this;
    }

    public OjxTable setAutoRenderers() {

	CellRendererFloat numberRenderer = new CellRendererFloat();
	CellRendererInteger integerRenderer = new CellRendererInteger();
	CellRendererDouble doubleRenderer = new CellRendererDouble();
	CellRendererDate dateRenderer = new CellRendererDate();

	// panelRutanaliz.getTblRutanaliz().setDefaultRenderer(Object.class,
	// renderer);
	// panelRutanaliz.getTblRutanaliz().setDefaultRenderer(String.class,
	// renderer);
	this.setDefaultRenderer(Float.class, numberRenderer);
	this.setDefaultRenderer(Integer.class, integerRenderer);
	this.setDefaultRenderer(Double.class, doubleRenderer);
	this.setDefaultRenderer(Date.class, dateRenderer);
	// TODO date renderer ekle

    return this;
    }
    
    public void setAutoColumnSizes( Integer... arrColsize ) {
    	
		TableModel tableModel = this.getModel();

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;

		TableCellRenderer headerRenderer = this.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < Math.min(this.getColumnCount(), arrColsize.length); i++) {
			// table dan columnn modele ulaşılır. columnmodel den column
			// objesine(column attributes) ulaşılır.
			column = this.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			cellWidth = (arrColsize[i] != null ? arrColsize[i] : 0);

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
			// column.setPreferredWidth(cellWidth);
		}

    	
    	
    }
    
    public void setAutoEditors() {

	checkboxEditorGlobal = new CellEditorCheckbox3();
	this.setDefaultEditor( Boolean.class, checkboxEditorGlobal);
	
    }

    public OjxTable setAutoSorting() {
	this.setSortOrderCycle(SortOrder.ASCENDING, SortOrder.DESCENDING, SortOrder.UNSORTED);
	return this;
    }

    public void setAutoExcelCopy() {
	ExcelAdapter excelAdaptercopy = new ExcelAdapter(this);
    }

    public OjxTable setAutoResizeOff() {
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return this;
    }


    public void setAutoConfig1() {
	this.setAutoRenderers();
	this.setAutoSorting();
	this.setAutoExcelCopy();
	this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public void setAutoEnterNextCol() {

	//this.putClientProperty("terminateEditOnFocusLost", true);

	InputMap im = this.getInputMap();
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Action.NextCell");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "Action.NextCell");

	ActionMap am = this.getActionMap();
	am.put("Action.NextCell", new NextCellAction(this));

    }

    public void setAutoFilter() {
	TableFilterHeader filterHeader = new TableFilterHeader( this, AutoChoices.ENABLED);
    }

    public <T> T getSelectedEntity( ObjectTablemodel2<T> objectTablemodel2) {

	    Integer selectedindex = this.getSelectedRow();

	    if(selectedindex==null || objectTablemodel2==null) return null;

	    Integer modelindex = this.convertRowIndexToModel(selectedindex);

	    T t = objectTablemodel2.getRow(modelindex);

	    return t;
    }
    
    public static void generateTableModel () {
    	
    }

	public CellEditorCheckbox3 getCheckboxEditor() {
		return checkboxEditorGlobal;
	}

	public void setCheckboxEditor(CellEditorCheckbox3 checkboxEditor) {
		this.checkboxEditorGlobal = checkboxEditor;
	}

}
