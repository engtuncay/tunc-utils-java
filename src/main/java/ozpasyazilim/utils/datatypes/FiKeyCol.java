package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.HashMap;
import java.util.Map;

/**
 * Key'e karşılık FiCol tutulduğu Obje. FiCol'da bir değeri tutan, gelişmiş bir objedir.
 */
public class FiKeyCol extends HashMap<String, FiCol> {

	public FiKeyCol() {
		super();
	}

	public FiKeyCol(Map<? extends String, ? extends FiCol> m) {
		super(m);
	}


	public void putWithHeaderName(FiCol fiCol) {
		put(fiCol.getHeaderName(), fiCol);
	}
}
