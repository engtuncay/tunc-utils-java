package ozpasyazilim.utils.gui.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public abstract class ComboboxModelList<E> extends AbstractListModel implements ComboBoxModel {
	private Object selectedItem;

	private List<E> listdata;

	public ComboboxModelList() {
		listdata = new ArrayList(); // initialization
	}

	public ComboboxModelList(List arrayList) {
		listdata = arrayList;
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object newValue) {
		selectedItem = newValue;
	}

	public int getSize() {
		return listdata.size();
	}

	public abstract Object getElementAt(int i);

	// {
	// return anArrayList.get(i);
	// }

	public E getListitem(int i) {
		if (i < 0) return null;
		return listdata.get(i);
	}

	public void clean() {
		listdata = new ArrayList();
		selectedItem = null;
		fireContentsChanged(this, 0, 0);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("ArrayListComboBoxModel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Collection col = System.getProperties().values();
		ArrayList arrayList = new ArrayList(col);

		ComboboxModelList model = new ComboboxModelList(arrayList) {

			@Override
			public Object getElementAt(int i) {
                return this.getListdata().get(i);
            }

		};

		JComboBox comboBox = new JComboBox(model);

		Container contentPane = frame.getContentPane();
		contentPane.add(comboBox, BorderLayout.NORTH);
		frame.setSize(300, 225);
		frame.setVisible(true);
	}

	public List getListdata() {
		return listdata;
	}

	public void setListdata(List<E> list) {
		this.listdata = list;
		this.fireContentsChanged(this, 0, list.size());
	}
}
