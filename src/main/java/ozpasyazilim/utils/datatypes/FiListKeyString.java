package ozpasyazilim.utils.datatypes;

import com.google.common.base.Optional;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FiListKeyString extends ArrayList<FiKeyString> {

	public FiListKeyString() {
		super();
	}

	public FiListKeyString(Collection<? extends FiKeyString> c) {
		super(c);
	}

	public void clearEmptyKeys() {
		for (FiKeyString fiKeyString : this) {
			fiKeyString.clearEmptyKeys();
		}
	}

	public void clearRowsKeyIfEmpty(String txDateField) {
		List<FiKeyString> listToDelete = new ArrayList<>();
		for (FiKeyString fiKeyString : this) {
			if (fiKeyString.isEmptyKey(txDateField)) {
				listToDelete.add(fiKeyString);
			}
		}
		for (FiKeyString fiKeyString : listToDelete) {
			this.remove(fiKeyString);
		}
	}

	public Optional<FiKeyString> getRow(String txKey, String txValue) {
		if(FiString.isEmpty(txKey)) return Optional.of(null);
		for (FiKeyString fiKeyString : this) {
			if (fiKeyString.getOrDefault(txKey,"-1").equals(txValue)) {
				Optional<FiKeyString> optResult = Optional.of(fiKeyString);
				return optResult;
			}
		}
		return Optional.of(null);
	}
}
