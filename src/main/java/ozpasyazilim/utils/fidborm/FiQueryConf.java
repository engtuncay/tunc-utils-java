package ozpasyazilim.utils.fidborm;

public class FiQueryConf {

	Boolean boActivateOnlyFullParams;

	public static FiQueryConf bui() {
		return new FiQueryConf();
	}

	// Getter and Setter

	public Boolean getBoActivateOnlyFullParams() {
		return boActivateOnlyFullParams;
	}

	public FiQueryConf setBoActivateOnlyFullParams(Boolean boActivateOnlyFullParams) {
		this.boActivateOnlyFullParams = boActivateOnlyFullParams;
		return this;
	}
}
