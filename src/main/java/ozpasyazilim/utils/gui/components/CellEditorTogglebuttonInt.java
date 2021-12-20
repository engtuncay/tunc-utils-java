package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;



/*
 * can extends AbstractCellEditor
 */

public class CellEditorTogglebuttonInt extends AbstractCellEditor implements ActionListener, ItemListener,
	TableCellEditor {

	protected static STogglebutton togglebutton = new STogglebutton();
	// STogglebutton togglebutton;
	// Integer count = 0;
	// UUID id = UUID.randomUUID();

	JTable jtable;
	int roweditor = -2;
	int columneditor = -2;

	public CellEditorTogglebuttonInt() {
		// super();
		togglebutton.addActionListener(this);
		// togglebutton.addItemListener(this);
	}

	public boolean iscellenabled(JTable table, Object value, int row, int column) {
		if (togglebutton.getCustomvalue() == null) return true;
		if (togglebutton.getCustomvalue() == 2) return false;
		return true;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		// her getcomponent e yeni sıfır component oluşturmak için
		// togglebutton = new STogglebutton();

		// count++;
		// Logmain.info("editor getcomponet count:"+count+" editor
		// id:"+id);
		// Loghelper.getInstance(this.getClass()).info("editor component entry value:" + value);

		this.jtable = table;
		this.roweditor = row;
		this.columneditor = column;

		if (value == null) togglebutton.setCustomvalue(null);
		else
			togglebutton.setCustomvalue((Integer) value);

		togglebutton.setEnabled(iscellenabled(table, value, row, column));

		if (value == null) {
			togglebutton.setText("Null");
			// setselected yapılınca itemstate listerlar çalıştırılır
			togglebutton.setSelected(false);
			return togglebutton;
		}

		if ((Integer) value == 0) {
			togglebutton.setText("X");
			togglebutton.setSelected(false);
			return togglebutton;
		}

		if ((Integer) value == 1) {
			togglebutton.setText("OK");
			togglebutton.setSelected(true);
			return togglebutton;
		}

		if ((Integer) value == 2) {
			togglebutton.setText("Kısıtlı");
			togglebutton.setSelected(false);
			return togglebutton;
		}

		return new STogglebutton();
	}

	public Object getCellEditorValue() {
		// Logmain.info("getcelleditor value:" +
		// togglebutton.getCustomvalue());
		// Loghelper.getInstance(this.getClass()).info("editor value:" + togglebutton.getCustomvalue());
		return togglebutton.getCustomvalue();

	}

	@Override
	public boolean stopCellEditing() {
		// Loghelper.getInstance(this.getClass()).info("Stopcellediting yapıldı.");
		return super.stopCellEditing();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Loghelper.getInstance(this.getClass()).info("item state changed");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Loghelper.getInstance(this.getClass()).info("action performed :"
		// + togglebutton.getCustomvalue()
		// + " row editor:"
		// + roweditor);

		// stopcellediting yapıldığında tekrar action perform oluyor, engellemek için konuldu
		if (roweditor < 0) return;

		if (togglebutton.getCustomvalue() == null) togglebutton.setCustomvalue(0);

		switch (togglebutton.getCustomvalue()) {
		case 0:
			togglebutton.setCustomvalue(1);
			togglebutton.setText("OK");
			break;
		case 1:
			togglebutton.setCustomvalue(0);
			togglebutton.setText("X");
			break;
		case 2:
			togglebutton.setText("Kısıtlı");
			break;
		}

		// modele değer gitmesi için stopcellediting yapılır, entity değeri alır (getvalue metodundan alıyor tblmodel) ,
		// stopcellediting yapılıyor
		stopCellEditing();

		// veritabanına kayıt olması için
		toggleclicked(jtable, roweditor, columneditor);


	}

	public void toggleclicked(JTable jtable2, int roweditor2, int columneditor2) {
		// Loghelper.getInstance(this.getClass()).info("toggle clicked:row:" + roweditor2 + " col:" + columneditor2);
	}

	public JTable getJtable() {
		return jtable;
	}

	public void setJtable(JTable jtable) {
		this.jtable = jtable;
	}
}
