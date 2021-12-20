package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observer;
import java.util.function.Predicate;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

// XIM custom jtable celleditor
/*
 * 
 */

public class CellEditorCheckboxPredicate<E> extends AbstractCellEditor implements ItemListener, ActionListener,
	TableCellEditor

{
	int i = 0;
	int c = 0;
	Predicate<E> custompredicate = e -> false;

	Customaction<ItemEvent> actioncustom = (e) -> {
		System.out.println("empty action" + (i++));
	};

	// Customaction<ActionEvent> actionperformed = (ae) -> {
	// System.out.println("empty actionperformed" + (i++));
	// };

	ActionListener myaction = (ae) -> {
		System.out.println("empty actionperformed:" + (c++));
	};

	CustomTablemodelList4<E> customTablemodelList4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JCheckBox mycompchb;
	CustomObservable observable = new CustomObservable();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Boolean avalue = null;
		if (value == null) avalue = false;
		if (value instanceof Boolean) avalue = (Boolean) value;
		if (avalue == null) return new JLabel("error");

		setMycompchb(new JCheckBox());

		getMycompchb().setSelected(avalue);

		if (custompredicate.test(getCustomTablemodelList4().getItem(table.convertRowIndexToModel(row)))) {
			getMycompchb().setEnabled(false);
		} else {
			getMycompchb().setEnabled(true);
		}

		getMycompchb().setHorizontalAlignment(SwingConstants.LEFT);
		getMycompchb().addItemListener(this);
		getMycompchb().addActionListener(myaction);
		return getMycompchb();
	}

	@Override
	public Object getCellEditorValue() {
		return getMycompchb().isSelected();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// Loghelper.getInstance(this.getClass()).info("item state changed");
		stopCellEditing();
		actioncustom.test(e);
		observable.setChanged();
		observable.notifyObservers();
	}

	// add listener gibi
	public void addObserver(Observer obs) {
		observable.addObserver(obs);
	}

	public CustomObservable getObservable() {
		return observable;
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

	public Customaction<ItemEvent> getActioncustom() {
		return actioncustom;
	}

	public void setActioncustom(Customaction<ItemEvent> actioncustom) {
		this.actioncustom = actioncustom;
	}

	public JCheckBox getMycompchb() {
		if (mycompchb == null) {
			mycompchb = new JCheckBox();
		}
		return mycompchb;
	}

	public void setMycompchb(JCheckBox mycompchb) {
		this.mycompchb = mycompchb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

    }

	public ActionListener getMyaction() {
		return myaction;
	}

	public void setMyaction(ActionListener myaction) {
		this.myaction = myaction;
	}

}
