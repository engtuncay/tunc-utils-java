package ozpasyazilim.utils.gui.components;

import javax.swing.JComboBox;

public class OzComboBox<E> extends JComboBox<E> {
	
	public E getSelectedObject() {
		return (E) super.getSelectedItem();
	}

}
