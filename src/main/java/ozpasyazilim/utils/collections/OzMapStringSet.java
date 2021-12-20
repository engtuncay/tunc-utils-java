package ozpasyazilim.utils.collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OzMapStringSet<E> extends HashMap<String, Set<E>> {

	public void add(String key, E value) {

		if (value == null)
			return;

		if (containsKey(key)) {

			Set<E> listofkey = get(key);
			if (listofkey != null) {
				listofkey.add(value);
			} else {
				listofkey = new HashSet<>();
				listofkey.add(value);
			}

		} else {

			Set<E> setofkey = new HashSet<>();
			setofkey.add(value);
			put(key, setofkey);

		}

	}

	public Set<String> getKeySet() {
		return keySet();
	}

}
