package mark.utils.el.factory;

import mark.utils.bean.Formatter;
import mark.utils.el.FieldDefResolver;

public class FieldResolverFactory {
	private Class<?> clazz;

	public FieldResolverFactory(Class<?> tClass) {
		if(tClass == null)
			throw new IllegalArgumentException("Class can't be null!");
		clazz = tClass;
	}

	public FieldDefResolver createResolver(String fieldName, String colName) {
		return new FieldDefResolver(clazz, fieldName, colName);
	}

	public FieldDefResolver createResolver(String fieldName, String colName,
			Formatter formatter) {
		FieldDefResolver resolver = new FieldDefResolver(clazz, fieldName, colName);
		resolver.setFormatter(formatter);
		return resolver;
	}

	public FieldDefResolver createResolver(String string) {
		return createResolver(string, string);
	}

	public FieldDefResolver createResolver(String string, Formatter formatter) {
		return createResolver(string, string, formatter);
	}
}
