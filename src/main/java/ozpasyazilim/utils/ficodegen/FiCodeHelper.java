package ozpasyazilim.utils.ficodegen;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.fidbanno.FiTable;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.fidborm.FiQueryGenerator;

import javax.persistence.Table;
import java.util.List;

public class FiCodeHelper {

	/**
	 * header ı listheaders dan
	 * <p>
	 * fieldName listField dan alır.
	 * <p>
	 * method ismi parametre olarak gönderilebilir.
	 *
	 * @param listHeaders
	 * @param methodName
	 * @param listFields
	 * @return
	 */
	public static String codeFiColListFromHeadersAndFields(List<String> listHeaders, String methodName, List<String> listFields) {

		StringBuilder query = new StringBuilder();

		query.append(String.format("public List<FiCol> genCols%s(){\n\n", methodName));

		query.append("\tList<FiCol> listCols = new ArrayList<>();\n\n");

		int index = 0;
		for (String header : listHeaders) {

			String fieldName = index + 1 > listFields.size() ? null : listFields.get(index);

			if (FiString.isEmptyTrim(fieldName)) {
				fieldName = FiString.replaceTurkishCharacterstoLatin(header).replaceAll(" ", "");
				fieldName = FiString.firstLetterLowerOnly(fieldName);
			}

			query.append(String.format("\tlistCols.add(FiCol.build(\"%s\",\"%s\"));\n", header, fieldName));
			index++;
		}

		query.append("\n\treturn listCols;\n");
		query.append("\n}\n");

		return query.toString();

	}

	public static String codeFiTableColsFromHeaderAndFieldName2(List<String> listHeaders, String methodNameSuffix, List<String> listFieldName) {

		StringBuilder query = new StringBuilder();

		query.append(String.format("public List<FiTableCol> genCols%s(){\n\n", methodNameSuffix));

		String varNameListCol = "listFiCols";
		query.append(String.format("\tList<FiTableCol>  %s= new ArrayList<>();\n\n", varNameListCol));

		int max = Math.max(FiNumber.orZero(listFieldName.size()), FiNumber.orZero(listHeaders.size()));

		for (int index = 0; index < max; index++) {

			String fieldHeader = "";

			if (index < listHeaders.size()) {
				fieldHeader = listHeaders.get(index);
			}

			String fieldName = "";

			if (index < listFieldName.size()) {
				fieldName = listFieldName.get(index);
			}

			if (!FiString.isEmptyTrim(fieldName)) {
				fieldName = FiString.replaceTurkishCharacterstoLatin(fieldHeader).replaceAll(" ", "");
				fieldName = FiString.firstLetterLowerOnly(fieldName);
			}

			query.append(String.format("\t%s.add(FiTableCol.build(\"%s\",\"%s\"));\n", varNameListCol, fieldHeader, fieldName));

		}

		query.append(String.format("\n\treturn %s;\n", varNameListCol));
		query.append("\n}\n");

		return query.toString();
	}

	public static String codeEntityClass(List<String> listHeaders, List<String> listFields, String clazzName, String fieldPrefix) {

		StringBuilder entitycode = new StringBuilder();

		entitycode.append("\n\nimport java.util.Date;\n");
		entitycode.append("\n");
		entitycode.append("public class " + clazzName + " { \n\n");

		int index = 1;
		for (String fieldName : listFields) {

			String header = "";

			if (listHeaders.size() >= index) {
				header = listHeaders.get(index - 1);
			}
			index++;

			if (FiString.isEmpty(fieldName) || fieldName.length() < 2) continue;

			String type = null;

			String fieldTypePrefix = fieldName.substring(0, 2);

			if (fieldTypePrefix.equals("dt")) {
				type = "Date";
			}

			if (fieldTypePrefix.equals("db")) {
				type = "Double";
			}

			if (fieldTypePrefix.equals("ln")) {
				type = "Integer";
			}

			if (fieldTypePrefix.equals("ti")) {
				type = "Integer";
			}

			if (fieldTypePrefix.equals("tx")) {
				type = "String";
			}

			if (fieldTypePrefix.equals("bn")) {
				type = "Byte";
			}

			if (fieldTypePrefix.equals("bo")) {
				type = "Boolean";
			}

			if (type == null) type = "String";

			if (!FiString.isEmpty(fieldPrefix)) {
				fieldName = fieldPrefix + FiString.firstLetterUpperOnly(fieldName);
			}

			entitycode.append(String.format("\t%s %s; // %s\n", type, fieldName, header));

		}

		entitycode.append("\n}\n");
		return entitycode.toString();
	}

	public static String codeFiTableColsGeneraterMethods(List<String> listHeaders, String methodName, List<String> listFields) {

		StringBuilder query = new StringBuilder();

		int index = 0;
		for (String field : listHeaders) {

			String fieldName = index + 1 > listFields.size() ? null : listFields.get(index);

			if (FiString.isEmptyTrim(fieldName)) {
				fieldName = FiString.replaceTurkishCharacterstoLatin(field).replaceAll(" ", "");
				fieldName = FiString.firstLetterLowerOnly(fieldName);
			}

			String fieldGen = String.format("public FiCol %s() {\n" +
					"\t\tFiCol fiCol = new FiCol(\"%s\", \"%s\");\n" +
					"\t\treturn fiCol;\n" +
					"\t}\n\n", fieldName, fieldName, field);
			query.append(fieldGen);
			index++;
		}

		//query.append("\n\treturn listTableCols;\n");
		//query.append("\n}\n");

		return query.toString();

	}

	/**
	 * Excel 'den alanları almak için kullanılabilir
	 *
	 * @param listHeaders
	 * @param methodName
	 * @param listFields
	 * @param fieldPrefix
	 * @return
	 */
	public static String codeFiColsMethodsFromHeaderAndFieldName(List<String> listHeaders, String methodName, List<String> listFields, String fieldPrefix) {

		StringBuilder templateDetail = new StringBuilder();

		int index = 1;
		for (String fieldName : listFields) {

			String fieldHeader = "";
			if (index <= listHeaders.size()) fieldHeader = listHeaders.get(index - 1);
			index++;

			if (FiString.isEmptyTrim(fieldName)) {
				continue;
			}

			if (!FiString.isEmpty(fieldPrefix)) {
				fieldName = fieldPrefix + FiString.firstLetterUpperOnly(fieldName);
			}

			templateDetail.append(String.format("\t\tlistTableCols.add(FiColsMikro.%s().buildHeader(\"%s\"));\n", fieldName, fieldHeader));

		}

		String template = String.format("\tpublic List<FiTableCol> genCols%s() {\n" +
				"\n" +
				"\t\tList<FiTableCol> listTableCols = new ArrayList<>();\n" +
				"\n" +
				"%s" +
				"\n" +
				"\t\treturn listTableCols;\n" +
				"\t}", methodName, templateDetail.toString());

		return template;

	}

	/**
	 *
	 * FiCol dönen metodları oluşturur, genCols metodunu oluşturur.
	 *
	 *
	 * @param listHeaders
	 * @param listFields
	 * @return
	 */
	public static String codeFiColsMethodsFromHeaderAndFieldNameForExcel(List<String> listHeaders, List<String> listFields) {

		//String methodName
    //, String fieldPrefix
		//List<FiField> listFields = FiEntity.getListFieldsWoutStatic(entclazz, true);

		StringBuilder txMethods = new StringBuilder();

		StringBuilder txColListMethod = new StringBuilder();

		//--* FiCol Metodları
		for (int ind = 0; ind < listFields.size(); ind++) {
			String fieldName = listFields.get(ind);
			String label = listHeaders.get(ind);

			String fieldGen = String.format("\npublic FiCol %s() {" +
					"\n\t\tFiCol fiCol = new FiCol(\"%s\", \"%s\");", fieldName, fieldName, FiString.orEmpty(label));

			fieldGen = fieldGen + String.format("\n\t\t//fiCol.buildColType(OzColType.%s);", "");
			fieldGen = fieldGen + "\n\t\treturn fiCol;\n\t}\n";
			txMethods.append(fieldGen);

			txColListMethod.append(String.format("\tlistTableCols.add(FiTableColFactory.bui().%s());\n", fieldName));
		}
		//--end

		//--* FiColList Metodu
		String txListVarName = "listFiCols";

		StringBuilder txFiColListDetail = new StringBuilder();

		for (int ind = 0; ind < listFields.size(); ind++) {

			String fieldName = listFields.get(ind);
			//String fieldHeader = FiString.orEmpty(listHeaders.get(ind));

			if (FiString.isEmptyTrim(fieldName)) {
				continue;
			}

//			if (!FiString.isEmpty(fieldPrefix)) {
//				fieldName = fieldPrefix + FiString.firstLetterUpperOnly(fieldName);
//			}

			txFiColListDetail.append(String.format("\t\t%s.add(FiColsMikro.%s());\n", txListVarName,  fieldName));

		}

		String txMetodFiColListTemplate = "\n\npublic List<FiCol> genCols(){\n" +
				"\n" +
				"\tList<FiCol> {2} = new ArrayList<>();\n" +
				"\n" +
				"{3}" +
				"\t\n" +
				"\treturn {2};\n" +
				"\t\n" +
				"\t}";// , "", txColListMethod.toString()

		String fiColListDetail = FiStringNum.bui(txMetodFiColListTemplate).addParams(2, txListVarName)
				.addParams(3, txFiColListDetail.toString()).tos();

		txMethods.append("\n\n");
		txMethods.append(fiColListDetail);

		return txMethods.toString();

	}

	/**
	 *
	 * genCols metodunu oluşturur.
	 *
	 * FiColsMikro ile alanları doldurur.
	 *
	 *
	 * @param listHeaders
	 * @param listFields
	 * @return
	 */
	public static String codeFiColListFromHeaderAndFieldNameByFiColsMikroWay(List<String> listHeaders, List<String> listFields) {

		//--* FiColList Metodu
		String txVarNameOfList = "listFiCols";

		StringBuilder txFiColListDetail = new StringBuilder();

		for (int ind = 0; ind < listFields.size(); ind++) {

			String txFieldName = listFields.get(ind);
			String txHeaderName = FiString.orEmpty(listHeaders.get(ind));

			// excel başlık yoksa alınmadı
			if (FiString.isEmptyTrim(txHeaderName)) {
				continue;
			}

			//txFiColListDetail.append(String.format("\t\t%s.add(FiColsMikro.(),\"%s\");\n", txVarNameOfList, txHeaderName));
			txFiColListDetail.append(String.format("\t// %s.add(FiColsMikro.().buiHeader(\"%s\")); // %s \n", txVarNameOfList, txHeaderName,txFieldName));
			// listFiCols.add(FiColsMikro.cari_kod().buiHeader(""))
		}

		String txTemplate = "\n\npublic List<FiCol> genCols() {\n" +
				"\n" +
				"\tList<FiCol> {2} = new ArrayList<>();\n" +
				"\n" +
				"{3}" +
				"\t\n" +
				"\treturn {2};\n" +
				"\n" +
				"}";// , "", txColListMethod.toString()

		String txFiColList = FiStringNum.bui(txTemplate).addParams(2, txVarNameOfList)
				.addParams(3, txFiColListDetail.toString()).tos();

		return txFiColList.toString();
	}

	public static String codeFiTableColsGeneraterMethodsByFiFields(Class entclazz) {

		List<FiField> listFields = FiEntity.getListFieldsWoutStatic(entclazz, true);

		StringBuilder query = new StringBuilder();

		StringBuilder colList = new StringBuilder();

		int index = 0;
		for (FiField field : listFields) {

			String fieldName = field.getDbFieldName();

			String fieldGen = String.format("\npublic FiCol %s() {" +
					"\n\tFiCol fiCol = new FiCol(\"%s\", \"%s\");", fieldName, fieldName, FiString.orEmpty(field.getLabel()));

			String simpleType = field.getClassNameSimple();

			fieldGen = fieldGen + String.format("\n\tfiCol.buildColType(OzColType.%s);", simpleType);

			//field.getClassNameSimpleAsOzColType();

			fieldGen = fieldGen + "\n\treturn fiCol;\n\t}\n";

			query.append(fieldGen);

			colList.append(String.format("\tlistTableCols.add(FiTableColFactory.bui().%s());\n", fieldName));

			index++;
		}


		String listColsTemplate = String.format("\n\npublic List<FiCol> genCols%s(){\n" +
				"\n" +
				"\tList<FiCol> listTableCols = new ArrayList<>();\n" +
				"\n" +
				"%s" +
				"\t\n" +
				"\treturn listTableCols;\n" +
				"\t\n" +
				"\t}", entclazz.getSimpleName(), colList.toString());


		return query.toString() + listColsTemplate;

	}

	public static String codeClassFieldList(Class clazz) {

		List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

		StringBuilder txFieldList = new StringBuilder();
		for (FiField fiField : fieldListFilterAnno) {
			txFieldList.append(fiField.getName() + "\n");
		}

		return txFieldList.toString();
	}

	public static String codeClassFieldListWithValue(Class clazz) {

		List<FiField> fieldListFilterAnno = FiEntity.getListFieldsShortWithId(clazz);

		StringBuilder txFieldList = new StringBuilder();
		for (FiField fiField : fieldListFilterAnno) {
			txFieldList.append(fiField.getName() + "\n");
		}

		return txFieldList.toString();
	}

	public static String getTableName(Class clazz) {

		String tableName = clazz.getSimpleName();

		if (clazz.isAnnotationPresent(Table.class)) {
			Table annoTable = (Table) clazz.getAnnotation(Table.class);
			if (!FiString.isEmpty(annoTable.name())) return annoTable.name();
		}

		if (clazz.isAnnotationPresent(FiTable.class)) {
			FiTable annoTable = (FiTable) clazz.getAnnotation(FiTable.class);
			if (!FiString.isEmpty(annoTable.name())) return annoTable.name();
		}

		return tableName;

	}

	public static String codeRepoClass(Class clazz) {

		String format =
				"import ozpasyazilim.utils.fidborm.AbsRepoJdbi;\n" +
						"\n" +
						"import java.util.List;\n" +
						"\n" +
						"public class Repo{className} extends AbsRepoJdbi<{className}> implements IRepoJdbi {\n" +
						"\n" +
						"\tpublic Repo{className}() {\n" +
						"\t\tsetDbInit();\n" +
						"\t}\n" +
						"\n" +
						"\tpublic Repo{className}(String connProfile) {\n" +
						"\t\tsetConnProfile(connProfile);\n" +
						"\t\tsetDbInit();\n" +
						"\t}\n" +
						"\n" +
						"\n" +
						"}\n";

		return OzFormatter.ofm(format).putNamedd("className", clazz.getSimpleName()).generate();

	}

	private static void createTable(Jdbi jdbi, Class clazz) {

		String sqlCreate = FiQueryGenerator.createQuery20(clazz);

		//System.out.println("db:"+jdbi.toString());

		System.out.println("Query will be execute:\n" + sqlCreate);
		System.out.println("");

//		try (Handle handle = jdbi.open()) {
//			handle.execute(sqlCreate);
//			//handle.commit();
//		}catch (Exception e){
//			FiException.exceptiontostring(e);
//		}

		jdbi.useHandle(handle -> {

			handle.begin();

			try {
				// transactions
				handle.execute(sqlCreate);

				handle.commit();
				//return true;

			} catch (Exception e) {
				System.out.println("Hata Oluştu" + FiException.exceptionToStrMain(e));
				handle.rollback();
				//return false;
			}
			//return true;
		});


	}


}
