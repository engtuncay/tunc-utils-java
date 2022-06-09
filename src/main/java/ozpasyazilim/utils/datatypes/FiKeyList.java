package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Custom Data Type
 * <p>
 * Map<String,List<Ent>> DataTYpe
 */
public class FiKeyList<Ent> extends HashMap<String, List<Ent>> {

	public FiKeyList() {
		super();
	}

	public static FiKeyList build() {
		return new FiKeyList();
	}

	public void clearEmptyKeys() {
		List<String> listToDelete = new ArrayList<>();
		this.forEach((key,value) -> {
			if(FiCollection.isEmpty(value)){
				listToDelete.add(key);
			}
		});
		for (String key : listToDelete) {
			this.remove(key);
		}
	}

	public Boolean isEmptyKey(String txKey) {
		if(this.containsKey(txKey)){
			if (FiCollection.isEmpty(this.get(txKey))) {
				return true;
			}
		}
		return false;
	}

}
