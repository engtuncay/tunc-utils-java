package mark.utils.el;

import mark.utils.bean.Formatter;
import mark.utils.el.handler.FieldAccessHandler;
import mark.utils.el.handler.FieldHandler;
import mark.utils.el.handler.MethodHandler;

/**
 * The class to access the field value.
 *
 * @author Marcos Vasconcelos
 */
public class FieldDefResolver {

	private String fieldName;// The field Name.
	private String name;// A name for this field column.
	private Formatter formatter;
	private FieldAccessHandler method;
	private Class<?> owner;

	public FieldDefResolver(Class<?> clazz, String fieldName, String name) {
		this(clazz, fieldName, name, null);
	}

	public FieldDefResolver(Class<?> clazz, String fieldName) {
		this(clazz, fieldName, "", null);
	}

	public FieldDefResolver(Class<?> clazz, String fieldName, FieldAccessHandler handler) {
		this(clazz, fieldName, "", handler);
	}

	public FieldDefResolver(Class<?> clazz, String fieldName, String name, int handler) {
		this(clazz, fieldName, name, (handler == 0) ? new FieldHandler() : new MethodHandler());
	}

	public FieldDefResolver(Class<?> clazz, String fieldName, String name, FieldAccessHandler handler) {
		if (handler == null) handler = new FieldHandler();

		owner = clazz;

		this.fieldName = fieldName;
		this.name = name;

		method = handler;
		method.resolveField(clazz, fieldName);

		setFormatter(new BasicFormatter());
	}

	public void setFormatter(Formatter formatter) {
		if (formatter == null) throw new IllegalArgumentException("Formatter can't be null!");
		this.formatter = formatter;
	}

	public void setValue(Object t, Object value) {
		method.setValue(t, value, formatter);
	}

	public Object getValue(Object t) {
		return method.getValue(t, formatter);
	}

	public String getName() {
		return name;
	}

	public Class<?> getFieldType() {
		return method.getFieldType();
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<?> getOwnerClass() {
		return owner;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * The default Formatter if no one is givem this will be taken.
	 *
	 * This formatter assume all Object are String.
	 */
	public static class BasicFormatter implements Formatter {

		public String format(Object obj) {
			if (obj == null) return "";
			return obj.toString();
		}


		public Object parse(Object obj) {
			return obj;
		}


		public String getName() {
			return "string_basic";
		}
	};

	public Class<?> getTraceClassAt(int idx) {
		return method.getTraceClassAt(idx);
	}

	public static final int FIELD_HANDLER = 0;
	public static final int METHOD_HANDLER = 1;

}
