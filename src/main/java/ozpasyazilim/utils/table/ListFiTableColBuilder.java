//package ozpasyazilim.utils.table;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Deprecated
//public class ListFiTableColBuilder {
//
//	List<FiCol> list;
//
//	public static ListFiTableColBuilder build() {
//		ListFiTableColBuilder listFiTableCol = new ListFiTableColBuilder();
//		return listFiTableCol;
//	}
//
//	public ListFiTableColBuilder addField(Object field) {
//		FiCol fiTableCol = new FiCol(field.toString());
//		getList().add(fiTableCol);
//		return this;
//	}
//
//	public ListFiTableColBuilder addFieldfh(Object field, Object header) {
//		FiCol fiTableCol = new FiCol(header.toString(), field.toString());
//		getList().add(fiTableCol);
//		return this;
//	}
//
//	public ListFiTableColBuilder addFields(Object... field) {
//		for (Object fieldsToAdd : field) {
//			FiCol fiTableCol = new FiCol(fieldsToAdd.toString());
//			getList().add(fiTableCol);
//		}
//		return this;
//	}
//
//	public List<FiCol> getList() {
//		if (list == null) {
//			list= new ArrayList<>();
//		}
//		return list;
//	}
//
//	public void setList(List<FiCol> list) {
//		this.list = list;
//	}
//}
