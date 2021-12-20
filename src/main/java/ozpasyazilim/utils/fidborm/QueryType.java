package ozpasyazilim.utils.fidborm;

public class QueryType {

	public String select = "SELECT";
	public String insert = "INSERT";
	public String update = "UPDATE";
	public String updatePop = "UPDATE-POP";
	public String delete = "DELETE";

	public static QueryType bui() {
		return new QueryType();
	}

}
