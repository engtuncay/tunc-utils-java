package ozpasyazilim.utils.ficodegen;

import ozpasyazilim.utils.fidborm.FiFieldUtil;
import ozpasyazilim.utils.fidborm.FiField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiTypescriptHelper {


	public static String tsEntity(Class<?> clazz) {

		StringBuilder result = new StringBuilder("");

		List<FiField> listFiFieldsSummary = FiFieldUtil.getListFieldsWoutStatic(clazz, true);

		result.append(String.format("export class %s {\n", clazz.getSimpleName()));

		for (FiField fiField : listFiFieldsSummary) {
			String typeTypesript = convertJavaTypeToTypescript(fiField); //"string";
			result.append(String.format("\t%s: %s;",fiField.getName(),typeTypesript));
			if (typeTypesript.equalsIgnoreCase("any")) {
				result.append(" // " + fiField.getClassNameSimple());
			}
			result.append("\n");
		}
		result.append("}\n");

		return result.toString();
	}

	private static String convertJavaTypeToTypescript(FiField fiField) {

		String javaSimpleType = fiField.getClassNameSimple();
		String tsType = getMapTypeConvertorJavaToTypescript().getOrDefault(javaSimpleType, "any");

		return tsType;
	}

	public static Map<String, String> getMapTypeConvertorJavaToTypescript() {

		Map<String, String> mapTypeConvertor = new HashMap<>();

		// Integer
		mapTypeConvertor.put("Integer", "number");
		mapTypeConvertor.put("int", "number");
		mapTypeConvertor.put("Short", "number");
		mapTypeConvertor.put("Long", "number");

		// Floating Point , hepsi decimal olarak yorumlandı
		mapTypeConvertor.put("Float", "number");
		mapTypeConvertor.put("Double", "number");
		mapTypeConvertor.put("double", "number");
		mapTypeConvertor.put("BigDecimal", "number");

		// String ( utf support olursa nvarchar a çevrilir)
		mapTypeConvertor.put("String", "string");
		mapTypeConvertor.put("string", "string");
		// Date Time
		mapTypeConvertor.put("Date", "Date");
		mapTypeConvertor.put("LocalDate", "Date");
		mapTypeConvertor.put("Timestamp", "Date");

		// Binary
		//mapTypeConvertor.put("ByteImage", "image");
		mapTypeConvertor.put("Boolean", "boolean");
		mapTypeConvertor.put("boolean", "boolean");

		return mapTypeConvertor;
	}

//	export class TblButceKural {
//		Id: number;
//		TxButceKod: string;
//		TxStKod: string;
//		TxHizmetKod: string;
//		TxAdi: string;
//		LnFirmaId: number;
//	}

}

