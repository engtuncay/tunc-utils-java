package ozpasyazilim.utils.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiMapMulti<K, V> extends HashMap<K, List<V>> {

	public FiMapMulti(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public FiMapMulti(int initialCapacity) {
		super(initialCapacity);
	}

	public FiMapMulti() {
		super();
	}

	public FiMapMulti(Map<? extends K, ? extends List<V>> m) {
		super(m);
	}

	/**
	 * key map de varsa mevcut listeye ekler
	 * <p>
	 * key map de yoksa yeni liste oluşturup , map e ekler.
	 *
	 * @param key
	 * @param value
	 */
	public void add(K key, V value) {
		if (this.containsKey(key)) {
			List<V> vs = this.get(key);
			vs.add(value);
		} else { // map'e önceden eklenmemiş
			List<V> list = new ArrayList<>();
			list.add(value);
			put(key, list);
		}
	}
}
