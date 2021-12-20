package ozpasyazilim.utils.core;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
public class FiJson {
 

    public String readJsonFieldFromFile(String jsonFilename, Class clazz, String fieldName) {

        JSONParser parser = new JSONParser();
 
        try {

	        File file = new File(clazz.getResource(jsonFilename+".json").toURI());

            FileReader fileReader = new FileReader(file);

            Object obj = parser.parse(fileReader);
 
            JSONObject jsonObject = (JSONObject) obj;
 
            String name = (String) jsonObject.get(fieldName);

            //String author = (String) jsonObject.get("Author");
            //JSONArray companyList = (JSONArray) jsonObject.get("Company List");
 
            //System.out.println("Name: " + name);
            //System.out.println("Author: " + author);
            //System.out.println("\nCompany List:");

	        /*Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/

	        return name;
 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}