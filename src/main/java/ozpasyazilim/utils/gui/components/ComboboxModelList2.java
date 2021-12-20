package ozpasyazilim.utils.gui.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.MutableComboBoxModel;

public abstract class ComboboxModelList2<E> extends AbstractListModel<E> implements MutableComboBoxModel<E> {
	private Object selectedItem;

	private List<E> listdata;

	public ComboboxModelList2() {
		listdata = new ArrayList(); // initialization
	}

	public ComboboxModelList2(List arrayList) {
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

	public abstract E getElementAt(int i);

	// {
	// return anArrayList.get(i);
	// }

	public E getItem(int i) {
		return listdata.get(i);
	}

	public void clean() {
		listdata = new ArrayList();
		fireContentsChanged(this, 0, 0);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("ArrayListComboBoxModel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Collection col = System.getProperties().values();
		ArrayList arrayList = new ArrayList(col);

		ComboboxModelList2 model = new ComboboxModelList2(arrayList) {

			@Override
			public Object getElementAt(int i) {
                return this.getListdata().get(i);
            }

			@Override
			public void addElement(Object item) {

            }

			@Override
			public void removeElement(Object obj) {

            }

			@Override
			public void insertElementAt(Object item, int index) {

            }

			@Override
			public void removeElementAt(int index) {

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
