package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.UUID;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/*
 * can extends AbstractCellEditor
 */

public class CellEditorTogglebuttonLambda extends AbstractCellEditor implements TableCellEditor, ActionListener,
	ItemListener {

	protected static CellRendererTogglebuttonLambda togglebutton = new CellRendererTogglebuttonLambda(); // new
																											// JToggleButton();

	FunctionToggleButtonActionPerfomed functionToggleButtonActionPerfomed = new FunctionToggleButtonActionPerfomed() {

		@Override
		public void actionPerformed(Integer row, Integer column, Object value) {
			// Loghelper.getInstance(this.getClass()).info("action tanımlanmamış");
		}
	};

	FunctionPredicateTableComponent predComponentenable = new FunctionPredicateTableComponent() {

		@Override
		public boolean test(JTable table, Object value, boolean isSelected, int row, int column) {
			return false;
		}
	};

	String uuid = UUID.randomUUID().toString();

	Integer lastrow;
	Integer lastcol;
	Object componentInitValue;

	// DefaultCellEditor aa = new DefaultCellEditor(new JCheckBox());

	public CellEditorTogglebuttonLambda() {

		// bu sınıf bir table için bir defa oluşur , action listener sadece bir tane eklemek istiyoruz
		// global component ayarları burada yapılır
		togglebutton.addActionListener(this);
		// togglebutton.addItemListener(this);

	}

	/**
	 * 
	 * jtable comp nin ilgili alanına edit yapılacağı vakit bu component teslim edilir.
	 * 
	 */

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		// !!!!!! global componente add listener burada eklenmez;
		// eğer eklenirse, componente tıklanınca listener sayısı artar

		lastcol = column;
		lastrow = row;
		componentInitValue = value;

		Component togglebuttonedit = togglebutton.getTableCellRendererComponent(table, value, isSelected, isSelected,
			row, column);

		// cellrenderer ile aynı component verilmesi istenirse... (cons.da sadece listener eklendi)
		togglebuttonedit.setEnabled(predComponentenable.test(table, value, isSelected, row, column));

		// Loghelper.getInstance(this.getClass()).info("geteditorcomp :" + lastrow + "-" + lastcol + "::" + (Boolean)
		// componentInitValue);

		return togglebuttonedit;

		// if (value == null) {
		// togglebutton.setSelected(false);
		// togglebutton.setText("X");
		// return togglebutton;
		// }
		//
		// if (((Boolean) value).booleanValue()) togglebutton.setSelected(true);
		// else
		// togglebutton.setSelected(false);
		//
		// return togglebutton;
	}

	/**
	 * 
	 * 
	 * @return Buradaki return değeri renderer setcomponent methoduna teslim edilir [to,tez]
	 * 
	 */
	public Object getCellEditorValue() {

		if (togglebutton.isSelected() == true) return new Boolean(true);
		else
			return new Boolean(false);

	}

	// @Override
	// public void addCellEditorListener(CellEditorListener l) {
	// 
	// super.addCellEditorListener(l);
	// }
	//
	// @Override
	// public void cancelCellEditing() {
	// 
	// super.cancelCellEditing();
	// }
	//
	// @Override
	// public boolean isCellEditable(EventObject anEvent) {
	// 
	// return true;
	// }
	//
	// @Override
	// public void removeCellEditorListener(CellEditorListener l) {
	// 
	// super.removeCellEditorListener(l);
	// }
	//
	// @Override
	// public boolean shouldSelectCell(EventObject anEvent) {
	// 
	// return true;
	// }
	//
	// @Override
	// public boolean stopCellEditing() {
	// 
	// return true;
	// }

	@Override
	public void itemStateChanged(ItemEvent e) {
        // System.out.println("Firing!");
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// !!!!!! stopcellediting veya fireeditingstopped yapınca otomatik change listener event oluşturuyor
		fireEditingStopped(); // make the renderer reappear
		// Loghelper.getInstance(this.getClass()).info("Actper uuid:" + uuid + "::" + lastrow + "-" + lastcol + "::" +
		// getCellEditorValue().toString());
		functionToggleButtonActionPerfomed.actionPerformed(lastrow, lastcol, getCellEditorValue());
	}

	public void setFunctionToggleButtonActionPerfomed(
		FunctionToggleButtonActionPerfomed functionToggleButtonActionPerfomed) {
		this.functionToggleButtonActionPerfomed = functionToggleButtonActionPerfomed;
	}


	public void setPredComponentenable(FunctionPredicateTableComponent predComponentenable) {
		this.predComponentenable = predComponentenable;
	}
}
