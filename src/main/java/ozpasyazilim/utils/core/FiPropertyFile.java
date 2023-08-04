package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FiPropertyFile {

	public static Properties readProperties(String relativePath) {

		//String path = new File(relativePath).getAbsolutePath();
		//Path path1 = Paths.get(relativePath);

		//Loghelperr.getInstance(getClassi()).debug("path:"+path);
		//System.out.println("Path:"+path);

		try (InputStream input = new FileInputStream(relativePath)) { //"path/to/config.properties"

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			//System.out.println(prop.getProperty("db.url"));

			return prop;

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;

	}

	private static Class<FiPropertyFile> getClassi() {
		return FiPropertyFile.class;
	}

	public static void writeProperties(Properties prop, String filePath) {

		try (OutputStream output = new FileOutputStream(filePath)) { //"filePath/to/config.properties"

//			Properties prop = new Properties();
//
			//// set the properties value
//			prop.setProperty("db.url", "localhost");
//			prop.setProperty("db.user", "mkyong");
//			prop.setProperty("db.password", "password");

			// save properties to project root folder
			prop.store(output, null);

			//System.out.println(prop);

		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public static Map<String, String> readPropFile1(String fileName) {

		Stream<String> propFileContentStream = null;

		try {
			propFileContentStream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException exception) {
			System.out.println("Prop File Okunurken Hata oluştu.");
			Loghelper.get(getClassi()).error("Prop File Okunurken Hata Oluştu :"+fileName);
			Loghelper.get(FiPropertyFile.class).error(FiException.exceptionToStrMain(exception));
		}

		// propFileContentStream null olabilir
		if(propFileContentStream==null){
			System.out.println("Prop File Bulunamadı !!! File:"+ fileName);
			Loghelper.get(getClassi()).debug("Prop File Bulunamadı !!! File:"+ fileName);
			return new HashMap<>();
		}

		List<String> listContent = propFileContentStream.collect(Collectors.toList());

		Map<String, String> propMap = new HashMap<>();

		for (String rowprop : listContent) {

			// comment rows skip
			if (rowprop.matches("^#.*")) continue;

			String[] arrRow = rowprop.split("=", 2);

			if (arrRow.length == 2) {
				propMap.put(arrRow[0].trim(), arrRow[1].trim());
				//System.out.println("key:" + arrRow[0] + " value:" + arrRow[1]);
			}

		}
		//FiConsole.printMap(propMap);
		return propMap;
	}

}
