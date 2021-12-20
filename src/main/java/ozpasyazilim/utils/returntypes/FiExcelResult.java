package ozpasyazilim.utils.returntypes;

import java.util.List;

public class FiExcelResult<E> {

	private List<E> listData;
	private List<String> listCol;
	private Integer indexHeader;

	public List<E> getListData() {
		return listData;
	}

	public void setListData(List<E> listData) {
		this.listData = listData;
	}

	public List<String> getListCol() {
		return listCol;
	}

	public void setListCol(List<String> listCol) {
		this.listCol = listCol;
	}

	public Integer getIndexHeader() {
		return indexHeader;
	}

	public void setIndexHeader(Integer indexHeader) {
		this.indexHeader = indexHeader;
	}

}
