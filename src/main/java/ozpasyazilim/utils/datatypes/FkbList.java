package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FkbList extends ArrayList<FiKeybean> {

	/**
	 * FiKeybean listesi ile birlikte kullanılacak kolon bilgilerini tutar.
	 */
	List<FiCol> ficColList;

	public FkbList() {
		super();
	}

	public FkbList(Collection<? extends FiKeybean> c) {
		super(c);
	}

	public List<FiCol> getFicColList() {
		return ficColList;
	}

	public void setFicColList(List<FiCol> ficColList) {
		this.ficColList = ficColList;
	}

	public FiKeybean getNtn(int index) {
		if(index < 0 || (index+1) > size()) {
			return new FiKeybean();
		}
		//rangeCheck(index);
		return get(index);
	}

}
