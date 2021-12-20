package ozpasyazilim.utils.datatypes;

import java.util.HashMap;

/**
 * Custom Data Type
 * <p>
 * Map<String,String> DataTYpe
 */
public class FiMapString extends HashMap<String, String> {

	public FiMapString() {
	}

	public static FiMapString build() {
		return new FiMapString();
	}

//	public FiMapStr buildPut(Object fieldName, Object value) {
//		if (fieldName == null) return this;
//		this.put(fieldName.toString(), value);
//		return this;
//	}

//	public FiMapStr buildPutIfNotNull(Object fieldName, Object value) {
//		if (fieldName == null) return this;
//		if (value == null) return this;
//
//		this.put(fieldName.toString(), value);
//		return this;
//	}

//	public void puto(Object field, Object value) {
//		this.put(field.toString(), value);
//	}
//
//	public void add(Object field, Object value) {
//		this.put(field.toString(), value);
//	}

//	public void putIfNotNull(Object objKey, Object value) {
//		if (value != null) {
//			this.put(objKey.toString(), value);
//		}
//	}

//	public FiMapStr bind(Object key, Object value) {
//		if (key == null) return this;
//		put(key.toString(), value);
//		return this;
//	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value) {
//		putIfNotEmpty(fieldName,value,false);
//		return this;
//	}

//	public FiMapParams buildPutIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
//		putIfNotEmpty(fieldName, value, addPercentage);
//		return this;
//	}

//	public FiMap putIfNotEmpty(Object fieldName, Object value) {
//		return putIfNotEmpty(fieldName, value, null);
//	}
//
//	public FiMap putIfNotEmpty(Object fieldName, Object value, Boolean addPercentage) {
//		if (fieldName == null) return this;
//		if (FiType.isEmptyGen(value)) return this;
//
//		if (FiBoolean.isTrue(addPercentage)) {
//			if(value instanceof String){
//				this.put(fieldName.toString(), "%"+ value +"%");
//			}else {
//				this.put(fieldName.toString(), value);
//			}
//		}else{
//			this.put(fieldName.toString(), value);
//		}
//		return this;
//	}


//	public FiMap buildMultiParam(Object fieldName, List<Integer> listData) {
//
//		Integer index = -1;
//		for (Integer listDatum : listData) {
//			index++;
//			put(fieldName.toString() + index.toString(), listData.get(index));
//		}
//		return this;
//	}


}
