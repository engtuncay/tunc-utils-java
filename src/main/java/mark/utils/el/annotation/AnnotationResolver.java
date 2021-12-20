package mark.utils.el.annotation;

import java.lang.reflect.Field;

import io.leangen.geantyref.AnnotationFormatException;
import io.leangen.geantyref.TypeFactory;
import mark.utils.bean.Formatter;
import mark.utils.el.FieldDefResolver;
import mark.utils.el.handler.FieldAccessHandler;

/**
 * Class to get FieldResolver from the Resolvable annotation.
 * 
 * @see mark.utils.el.annotation.Resolvable
 * @author Marcos Vasconcelos
 */
public class AnnotationResolver {
	
	private Class<?> clazz;
	Boolean formatterEnabled = false;

	public AnnotationResolver(Class<?> clazz) {
		if (clazz == null) throw new IllegalArgumentException("Class can't be null!");
		this.clazz = clazz;
	}

	/**
	 * For each String of the given parameter are returned a FieldResolver.
	 * 
	 * @param fieldNames
	 *            .
	 * @return The FieldResolvers for the given field names.
	 */
	public FieldDefResolver[] resolve(String... fieldNames) {
		
		FieldDefResolver resolvers[] = new FieldDefResolver[fieldNames.length];
		if (fieldNames.length == 0) return resolvers;

		for (int i = 0; i < fieldNames.length; i++) {
			try {
				String fieldN = fieldNames[i];
				String colName = "";
				int index = fieldN.lastIndexOf(":");
				if (index > -1) {
					colName = fieldN.substring(index + 1);
					fieldN = fieldN.substring(0, index);
				}
				if (fieldN.contains(".")) resolvers[i] = resolve(fieldN, clazz, colName);
				else {
					Field field = clazz.getDeclaredField(fieldN);
					// XIM object-table-model : annotion resolution 
					if (field.isAnnotationPresent(Resolvable.class)) {
						Resolvable resolvable = field.getAnnotation(Resolvable.class);
						resolvers[i] = resolve(resolvable, field.getName(), clazz, colName);
					}else {
						// if you want to add parameters to the annotation [to]
						//Map<String, Object> annotationParameters = new HashMap<>();
						//annotationParameters.put("name", "someName");
						Resolvable resolvableDyn = TypeFactory.annotation( Resolvable.class, null);
						resolvers[i] = resolve(resolvableDyn, field.getName(), clazz, colName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resolvers;
	}

	/**
	 * Give the field names in a one String value. Each name separated by
	 * commas(,) The same as resolve(arg.split("[,]"))
	 */
	public FieldDefResolver[] resolve(String arg) {
		return resolve(arg.split("[,]"));
	}

	/**
	 * Return the FieldResolver for the given field name.
	 */
	public FieldDefResolver resolveSingle(String arg) {
		return resolve(arg)[0];
	}

	// XIM object-table-model : noktalı alt alanları ayrıştırmak için
	private FieldDefResolver resolve(String fieldName, Class<?> clazz, String colname) throws InstantiationException,
		IllegalAccessException, SecurityException, NoSuchFieldException {
		String fields[] = fieldName.split("[.]");

		Field last = clazz.getDeclaredField(fields[0]);
		for (int i = 1; i < fields.length; i++)
			last = last.getType().getDeclaredField(fields[i]);

		
		//Resolvable resolvable = last.getAnnotation(Resolvable.class);
		String colName = ""; // resolvable.colName();
		Resolvable resolvable =null;
		
		if (last.isAnnotationPresent(Resolvable.class)) {
			resolvable = last.getAnnotation(Resolvable.class);
			colName = resolvable.colName(); 
			//resolvers[i] = resolve(resolvable, last.getName(), clazz, colName);
		}else {
			// if you want to add parameters to the annotation [to]
			//Map<String, Object> annotationParameters = new HashMap<>();
			//annotationParameters.put("name", "someName");
			try {
				resolvable = TypeFactory.annotation( Resolvable.class, null);
			} catch (AnnotationFormatException e) {
				// TODO try catch
				e.printStackTrace();
			}
			//resolvers[i] = resolve(resolvableDyn, field.getName(), clazz, colName);
		}
		

		if (!colname.isEmpty()) colName = colname;
		else if (colName.isEmpty()) colName = fieldName;

		//		else colName = colName;

		//TODO önceki hali - güncellendi 20.06 by to
		//fieldName.substring(0, fieldName.lastIndexOf(".")).concat(colName);

		FieldDefResolver resolver = new FieldDefResolver(clazz, fieldName, colName, (FieldAccessHandler) resolvable
			.accessMethod().newInstance());
		
		if( this.formatterEnabled ) resolver.setFormatter((Formatter) resolvable.formatter().newInstance());
		
		return resolver;
	}

	private FieldDefResolver resolve(Resolvable resolvable, String fieldName, Class<?> clazz, String colname)
		throws InstantiationException, IllegalAccessException {
		
		String colName = resolvable.colName();

		if (colName.isEmpty()) if (colname.isEmpty()) colName = fieldName;
		else
			colName = colname;

		FieldDefResolver resolver = new FieldDefResolver(clazz, fieldName, colName, (FieldAccessHandler) resolvable
			.accessMethod().newInstance());
		resolver.setFormatter((Formatter) resolvable.formatter().newInstance());
		return resolver;
	}

	public Boolean getFormatterEnabled() {
		return formatterEnabled;
	}

	public void setFormatterEnabled(Boolean formatterEnabled) {
		this.formatterEnabled = formatterEnabled;
	}
}
