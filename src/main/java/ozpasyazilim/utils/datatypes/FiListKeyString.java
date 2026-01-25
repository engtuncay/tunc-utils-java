package ozpasyazilim.utils.datatypes;

import com.google.common.base.Optional;
import ozpasyazilim.utils.core.FiString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FiListKeyString extends ArrayList<FiKeytext> {

	public FiListKeyString() {
		super();
	}

	public FiListKeyString(Collection<? extends FiKeytext> c) {
		super(c);
	}

	public void clearEmptyKeys() {
		for (FiKeytext fiKeytext : this) {
			fiKeytext.clearEmptyKeys();
		}
	}

	public void clearRowsKeyIfEmpty(String txDateField) {
		List<FiKeytext> listToDelete = new ArrayList<>();
		for (FiKeytext fiKeytext : this) {
			if (fiKeytext.isEmptyKey(txDateField)) {
				listToDelete.add(fiKeytext);
			}
		}
		for (FiKeytext fiKeytext : listToDelete) {
			this.remove(fiKeytext);
		}
	}

	public Optional<FiKeytext> getRow(String txKey, String txValue) {
		if(FiString.isEmpty(txKey)) return Optional.of(null);
		for (FiKeytext fiKeytext : this) {
			if (fiKeytext.getOrDefault(txKey,"-1").equals(txValue)) {
				Optional<FiKeytext> optResult = Optional.of(fiKeytext);
				return optResult;
			}
		}
		return Optional.of(null);
	}
}
