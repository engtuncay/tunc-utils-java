package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.gui.components.ComboItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListCombo extends ArrayList<ComboItem> {

	public ListCombo(int initialCapacity) {
		super(initialCapacity);
	}

	public ListCombo() {
	}

	public ListCombo(Collection<? extends ComboItem> c) {
		super(c);
	}

	public void addItem(Object key, Object value) {
		if(key==null)return;
		if(value==null) {
			add(new ComboItem(key.toString(), null));
			return;
		}
		add(new ComboItem(key.toString(), value.toString()));
	}
}
