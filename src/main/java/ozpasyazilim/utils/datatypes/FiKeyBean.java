package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiString;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Data Type
 * <p>
 * Map<String,Object> türünde bir obje ve özel metodları olan
 */
public class FiKeyBean extends HashMap<String, Object> {

	public FiKeyBean() {
	}
	public FiKeyBean(Map<? extends String, ?> m) {
		super(m);
	}
	public static FiKeyBean build() {
		return new FiKeyBean();
	}
	public FiKeyBean buildPut(Object fieldName, Object value) {
		if (fieldName == null) return this;
		this.put(fieldName.toString(), value);
		return this;
	}
	public FiKeyBean buildPutIfNotNull(Object fieldName, Object value) {
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
	public FiKeyBean bind(Object key, Object value) {
		if (key == null) return this;
		put(key.toString(), value);
		return this;
	}

	public Object getAfterCheck(Object txKey) {
		if(FiString.isEmptyToString(txKey)) return null;

		if (this.containsKey(txKey.toString())) {
			return get(txKey.toString());
		}else{
			return null;
		}

	}

	public Object geto(Object txKey) {
		if (txKey == null) return null;
		return get(txKey.toString());
	}

}
