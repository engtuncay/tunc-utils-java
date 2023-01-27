package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.gui.components.ComboItemText;

import java.util.ArrayList;
import java.util.Collection;

public class ListCombo extends ArrayList<ComboItemText> {

	public ListCombo(int initialCapacity) {
		super(initialCapacity);
	}

	public ListCombo() {
	}

	public ListCombo(Collection<? extends ComboItemText> c) {
		super(c);
	}

	public void addItem(Object key, Object value) {
		if(key==null)return;
		if(value==null) {
			add(new ComboItemText(key.toString(), null));
			return;
		}
		add(new ComboItemText(key.toString(), value.toString()));
	}
}
