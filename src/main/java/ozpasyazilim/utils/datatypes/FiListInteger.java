package ozpasyazilim.utils.datatypes;

import java.util.ArrayList;
import java.util.Arrays;

public class FiListInteger extends ArrayList<Integer> {

	public static FiListInteger bui() {
		return new FiListInteger();
	}

	public FiListInteger adds(Integer item) {
		add(item);
		return this;
	}

	public FiListInteger adds(Integer... item) {
		addAll(Arrays.asList(item));
		return this;
	}
}
