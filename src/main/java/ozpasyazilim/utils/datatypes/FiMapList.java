package ozpasyazilim.utils.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiMapList<K, V> extends HashMap<K, List<V>> {

	public FiMapList(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public FiMapList(int initialCapacity) {
		super(initialCapacity);
	}

	public FiMapList() {
		super();
	}

	public FiMapList(Map<? extends K, ? extends List<V>> m) {
		super(m);
	}

	/**
	 * key map de varsa objeyi mevcut listeye ekler
	 * <p>
	 * key map de yoksa yeni liste oluşturur, objeyi ekler ve listeyi de map e ekler.
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
