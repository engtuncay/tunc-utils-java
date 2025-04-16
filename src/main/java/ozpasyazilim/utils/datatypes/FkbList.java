package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FkbList extends ArrayList<FiKeyBean> {

	List<FiCol> fiColList;

	public FkbList() {
		super();
	}

	public FkbList(Collection<? extends FiKeyBean> c) {
		super(c);
	}

	public List<FiCol> getFiColList() {
		return fiColList;
	}

	public void setFiColList(List<FiCol> fiColList) {
		this.fiColList = fiColList;
	}
}
