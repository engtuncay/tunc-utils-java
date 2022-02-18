package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FiColList extends ArrayList<FiCol> {

	public FiColList(int initialCapacity) {
		super(initialCapacity);
	}

	public FiColList() {
		super();
	}

	public FiColList(Collection<? extends FiCol> c) {
		super(c);
	}

}
