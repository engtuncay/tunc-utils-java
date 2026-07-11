package ozpasyazilim.utils.datatypes;

import com.google.common.base.Optional;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FiListKeyString extends ArrayList<Fks> {

	public FiListKeyString() {
		super();
	}

	public FiListKeyString(Collection<? extends Fks> c) {
		super(c);
	}

	public void clearEmptyKeys() {
		for (Fks fks : this) {
			fks.clearEmptyKeys();
		}
	}

	public void clearRowsKeyIfEmpty(String txDateField) {
		List<Fks> listToDelete = new ArrayList<>();
		for (Fks fks : this) {
			if (fks.isEmptyKey(txDateField)) {
				listToDelete.add(fks);
			}
		}
		for (Fks fks : listToDelete) {
			this.remove(fks);
		}
	}

	public Optional<Fks> getRow(String txKey, String txValue) {
		if(FiString.isEmpty(txKey)) return Optional.of(null);
		for (Fks fks : this) {
			if (fks.getOrDefault(txKey,"-1").equals(txValue)) {
				Optional<Fks> optResult = Optional.of(fks);
				return optResult;
			}
		}
		return Optional.of(null);
	}
}
