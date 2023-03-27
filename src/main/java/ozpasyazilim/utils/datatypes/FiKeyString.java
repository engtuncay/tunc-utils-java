package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Map [String,String] türünde özel tip
 */
public class FiKeyString extends HashMap<String, String> {

	public FiKeyString() {
	}

	public static FiKeyString build() {
		return new FiKeyString();
	}

	public void clearEmptyKeys() {
		List<String> listToDelete = new ArrayList<>();
		this.forEach((key, value) -> {
			if (FiString.isEmpty(value)) {
				listToDelete.add(key);
			}
		});
		for (String key : listToDelete) {
			this.remove(key);
		}
	}

	public Boolean isEmptyKey(String txKey) {
		if (this.containsKey(txKey)) {
			if (FiString.isEmpty(this.get(txKey))) {
				return true;
			}
		}
		return false;
	}

	public String getTos(Object txKey) {
		if (txKey == null) return null;
		return get(txKey.toString());
	}

	public String getTosOrEmpty(Object txKey) {
		if (txKey == null) return "";
		if (!containsKey(txKey.toString())) return "";
		return FiString.orEmpty(get(txKey.toString()));
	}

}
