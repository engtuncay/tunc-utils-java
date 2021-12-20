package ozpasyazilim.utils.core;

public class Transform<EntClazz> {

	Class<EntClazz> clazz;

	public Transform(Class<EntClazz> clazz) {
		this.clazz = clazz;
	}

	public Class<EntClazz> getClazz() {
		return clazz;
	}

	public void setClazz(Class<EntClazz> clazz) {
		this.clazz = clazz;
	}
}
