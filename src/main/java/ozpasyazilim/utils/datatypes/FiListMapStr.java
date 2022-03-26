package ozpasyazilim.utils.datatypes;

import com.google.common.base.Optional;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FiListMapStr extends ArrayList<FiMapString> {

	public FiListMapStr() {
		super();
	}

	public FiListMapStr(Collection<? extends FiMapString> c) {
		super(c);
	}

	public void clearEmptyKeys() {
		for (FiMapString fiMapString : this) {
			fiMapString.clearEmptyKeys();
		}
	}

	public void clearRowsKeyIfEmpty(String txDateField) {
		List<FiMapString> listToDelete = new ArrayList<>();
		for (FiMapString fiMapString : this) {
			if (fiMapString.isEmptyKey(txDateField)) {
				listToDelete.add(fiMapString);
			}
		}
		for (FiMapString fiMapString : listToDelete) {
			this.remove(fiMapString);
		}
	}

	public Optional<FiMapString> getRow(String txKey, String txValue) {
		if(FiString.isEmpty(txKey)) return Optional.of(null);
		for (FiMapString fiMapString : this) {
			if (fiMapString.getOrDefault(txKey,"-1").equals(txValue)) {
				Optional<FiMapString> optResult = Optional.of(fiMapString);
				return optResult;
			}
		}
		return Optional.of(null);
	}
}
