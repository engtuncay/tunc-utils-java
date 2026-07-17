package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiConsole;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Fkl : FkbList
 */
public class FkbList extends ArrayList<Fkb> {

	/**
	 * FiKeybean listesi ile birlikte kullanılacak kolon bilgilerini tutar.
	 */
	List<FiCol> ficColList;

	public FkbList() {
		super();
	}

	public FkbList(Collection<? extends Fkb> c) {
		super(c);
	}

	public List<FiCol> getFicColList() {
		return ficColList;
	}

	public void setFicColList(List<FiCol> ficColList) {
		this.ficColList = ficColList;
	}

	public Fkb getNtn(int index) {
		if(index < 0 || (index+1) > size()) {
			return new Fkb();
		}
		//rangeCheck(index);
		return get(index);
	}

  public void logContentNtn(String txDesc) {
		Loghelper.get(getClass()).debug(txDesc + "\n" + FiConsole.textFkbListNtn(this));
	}
}
