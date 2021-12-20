package ozpasyazilim.utils.datatypes;

public class IntRef {

	Integer value;

	public IntRef(Integer value) {
		this.value = value;
	}

	public static IntRef bui() {
		return new IntRef(0);
	}

	public static IntRef bui(int i) {
		return new IntRef(i);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
