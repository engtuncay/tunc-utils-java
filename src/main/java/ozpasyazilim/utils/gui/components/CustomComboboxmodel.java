package ozpasyazilim.utils.gui.components;

import java.util.*;
import javax.swing.DefaultComboBoxModel;

/**
 * extend The default model for combo boxes.
 *
 * @param <E>
 *            the type of the elements of this model
 *
 * @author Arnaud Weber
 * @author Tom Santos
 */

public class CustomComboboxmodel<E> extends DefaultComboBoxModel<E> {
	List<E> objects;
	List<E> listalldata;

	Object selectedObject;

	/**
	 * Constructs an empty DefaultComboBoxModel object.
	 */
	public CustomComboboxmodel() {
		objects = new ArrayList<E>();
	}

	/**
	 * Constructs a DefaultComboBoxModel object initialized with
	 * an array of objects.
	 *
	 * @param items
	 *            an array of Object objects
	 */
	public CustomComboboxmodel(final E items[]) {
		objects = new ArrayList<E>();
		// objects.ensureCapacity( items.length );

		int i, c;
		for (i = 0, c = items.length; i < c; i++)
			objects.add(items[i]);

		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	/**
	 * Constructs a DefaultComboBoxModel object initialized with
	 * a vector.
	 *
	 * @param v
	 *            a Vector object ...
	 */
	public CustomComboboxmodel(List<E> v) {
		objects = v;

		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	public void setData(List<E> v) {
		objects = v;

		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
		// Loghelper.getInstance(this.getClass()).info("entered setdata");

		fireContentsChanged(this, 0, objects.size() - 1);
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * 
	 * @param anObject
	 *            The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject) {
		if ((selectedObject != null && !selectedObject.equals(anObject))
			|| selectedObject == null && anObject != null) {
			selectedObject = anObject;
			fireContentsChanged(this, -1, -1);
		}
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem() {
		return selectedObject;
	}

	// implements javax.swing.ListModel
	public int getSize() {
		return objects.size();
	}

	// implements javax.swing.ListModel
	public E getElementAt(int index) {
		if (index >= 0 && index < objects.size()) return objects.get(index);
		else
			return null;
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject
	 * @return an int representing the index position, where 0 is
	 *         the first position
	 */
	public int getIndexOf(Object anObject) {
		return objects.indexOf(anObject);
	}

	// implements javax.swing.MutableComboBoxModel
	public void addElement(E anObject) {
		objects.add(anObject);
		fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
		if (objects.size() == 1 && selectedObject == null && anObject != null) {
			setSelectedItem(anObject);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(E anObject, int index) {
		objects.set(index, anObject);
		fireIntervalAdded(this, index, index);

	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(int index) {
		if (getElementAt(index) == selectedObject) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		objects.remove(index);

		fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(Object anObject) {
		int index = objects.indexOf(anObject);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (objects.size() > 0) {
			int firstIndex = 0;
			int lastIndex = objects.size() - 1;
			objects.clear();
			selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			selectedObject = null;
		}
	}

	public List<E> getObjects() {
		return objects;
	}

	public List<E> getListalldata() {
		return listalldata;
	}

	public void setListalldata(List<E> listalldata) {
		this.listalldata = listalldata;
	}
}
