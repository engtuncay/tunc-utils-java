package ozpasyazilim.utils.datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Data Type
 * <p>
 * Map<String,Object> DataTYpe
 */
public class FiMapStrobj extends HashMap<String, Object> {

	public FiMapStrobj() {
	}

	public FiMapStrobj(Map<? extends String, ?> m) {
		super(m);
	}

	public static FiMapStrobj build() {
		return new FiMapStrobj();
	}

	public FiMapStrobj buildPut(Object fieldName, Object value) {
		if (fieldName == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

	public FiMapStrobj buildPutIfNotNull(Object fieldName, Object value) {
		if (fieldName == null) return this;
		if (value == null) return this;

		this.put(fieldName.toString(), value);
		return this;
	}

	public void puto(Object field, Object value) {
		this.put(field.toString(), value);
	}

	public void add(Object field, Object value) {
		this.put(field.toString(), value);
	}

	public void putIfNotNull(Object objKey, Object value) {
		if (value != null) {
			this.put(objKey.toString(), value);
		}
	}

	public FiMapStrobj bind(Object key, Object value) {
		if (key == null) return this;
		put(key.toString(), value);
		return this;
	}

}
