package ozpasyazilim.utils.core;

import ozpasyazilim.utils.datatypes.FiKeytext;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FiPropertyFile {

  public static Fdr<FiKeytext> readPropFile(String fileName) {

    Fdr<FiKeytext> fdrMain = new Fdr<>();

    Path path = Paths.get(fileName);
    System.out.println("Prop File Path:" + path.toAbsolutePath());
    //Loghelper.get(getClassi()).debug("Prop File Path:"+path.toAbsolutePath());

    FiKeytext propMap = new FiKeytext();

    try (Stream<String> propFileContentStream = Files.lines(path, StandardCharsets.UTF_8)) {
      List<String> listContent = propFileContentStream.collect(Collectors.toList());

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
      fdrMain.setValue(propMap);
      fdrMain.setBoResult(true);
      return fdrMain;
    } catch (IOException exception) {
      System.out.println("Prop File Okunurken Hata oluştu.");
      fdrMain.setMessageForAppend("Prop File Okunurken Hata Oluştu :" + fileName);
      fdrMain.setValue(new FiKeytext());
      fdrMain.setBoResult(false);
      return fdrMain;
    }

  }

  public static Properties readProperties(String relativePath) {

    //String path = new File(relativePath).getAbsolutePath();
    //Path path1 = Paths.get(relativePath);

    //Loghelperr.getInstance(getClassi()).debug("path:"+path);
    //System.out.println("Path:"+path);

    try (InputStream input = Files.newInputStream(Paths.get(relativePath))) { //"path/to/config.properties"

      Properties prop = new Properties();

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      //System.out.println(prop.getProperty("db.url"));

      return prop;

    } catch (IOException ex) {
      //ex.printStackTrace();
      System.out.println("Properties File Okunurken Hata oluştu. File:" + relativePath);
      Loghelper.get(getClassi()).error(FiException.exTosMain(ex));
    }

    return null;

  }

  private static Class<FiPropertyFile> getClassi() {
    return FiPropertyFile.class;
  }

  //	public static void writeProperties(Properties prop, String filePath) {
//
//		try (OutputStream output = new FileOutputStream(filePath)) { //"filePath/to/config.properties"
//
////			Properties prop = new Properties();
////
//			//// set the properties value
////			prop.setProperty("db.url", "localhost");
////			prop.setProperty("db.user", "mkyong");
////			prop.setProperty("db.password", "password");
//
//			// save properties to project root folder
//			prop.store(output, null);
//
//			//System.out.println(prop);
//
//		} catch (IOException io) {
//			io.printStackTrace();
//		}
//
//	}


}
