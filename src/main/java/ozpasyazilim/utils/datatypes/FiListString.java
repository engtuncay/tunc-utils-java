package ozpasyazilim.utils.datatypes;

import java.util.ArrayList;
import java.util.List;

public class FiListString extends ArrayList<String> {

	public static FiListString bui() {
		return new FiListString();
	}

	public FiListString addo(Object item) {
		add(item.toString());
		return this;
	}

	public FiListString adds(String item) {
		add(item.toString());
		return this;
	}
}
