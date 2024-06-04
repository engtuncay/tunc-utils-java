package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.FiMetaKey;

public class MetaQueryTypes {

	public String select = "SELECT";
	public String insert = "INSERT";
	public String update = "UPDATE";
	public String updatePop = "UPDATE-POP";
	public String delete = "DELETE";

	public static MetaQueryTypes bui() {
		return new MetaQueryTypes();
	}

	public static FiMetaKey select(){
	    FiMetaKey fiMeta = new FiMetaKey("SELECT","");
	    return fiMeta;
	}



}
