package ozpasyazilim.utils.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.io.IOUtils;
import org.joox.JOOX;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.joox.JOOX.$;


public class FiXmlParser {

	public static FiXmlParser bui() {
		return new FiXmlParser();
	}

	public static void main(String[] args) {


	}

	public <T> void parseXmlElement(File file, String elementName, List<IFiCol> listColumn, List<T> listData, Class<T> clazz) {

		if (listData == null) return;

		Match $xmldom = openXmlFile(file);

		$xmldom.find(elementName).forEach(
				xmlElement -> {
					T objectt = parseXmlElementToEntity(xmlElement, listColumn, clazz);
					if (objectt != null) listData.add(objectt);
				});

	}

	public List<String> parseXmlTagsElement(File file, String elementName) {

		//if (listData == null) return;

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		List<String> tagList = new ArrayList<>();

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

		Match first = $xmldom.find(elementName).first();
//		tagList.add(first.find("LNGKOD").text());

		first.children().forEach(xmlElement -> {
			//Loghelper.get(getClass()).debug(FiConsole.logMain($(xmlElement)));
			tagList.add($(xmlElement).tag());
		});

		//Loghelper.get(getClass()).debug(FiConsole.logStringList(tagList));

		return tagList;
	}

	public Boolean existTagWithValue(File file, String txElementName, String txChildElementName, String txValue) {

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

		BooleanProperty boFound = new SimpleBooleanProperty(false);
		for (Element xmlParentElement : $xmldom.find(txElementName)) {
			if ($(xmlParentElement).find(txChildElementName).text().trim().equals(txValue.trim())) {
				boFound.setValue(true);
				break;
			}
		}

		return boFound.getValue();
	}

	public static <EntPrmClazz> EntPrmClazz findEntity(File file, String txElementName, String txKeyElementName, String txKeyElementValue, Class<EntPrmClazz> clazz, List<FiCol> listEntCols) {

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

//		BooleanProperty boFound = new SimpleBooleanProperty(false);

		for (Element xmlParentElement : $xmldom.find(txElementName)) {

			if ($(xmlParentElement).find(txKeyElementName).text().trim().equals(txKeyElementValue.trim())) {

				EntPrmClazz entPrmClazz = parseMatchToEntityWithOneChild($(xmlParentElement), listEntCols, clazz, false, null);
				return entPrmClazz;
//				boFound.setValue(true);
//				break;
			}
		}

		return null;
	}


	public static Match openXmlFile(File file) {

		Match $xmldom = null;

		try {
			$xmldom = $(file); // $xmldom match objesi
		} catch (SAXException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exTosMain(e));
		} catch (IOException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exTosMain(e));
		}

		return $xmldom;
	}

	public static Match openXml(String txXml) {
		Match $matchDoc = null;
		try {
			InputStream targetStream = IOUtils.toInputStream(txXml, Charset.forName("UTF-8"));
			Document document = JOOX.builder().parse(targetStream); // $matchDoc match objesi
			$matchDoc = $(document); //.find("root");
			return $matchDoc;
		} catch (SAXException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exTosMain(e));
		} catch (IOException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exTosMain(e));
		}

		return null;
	}


	public <T> void parseXmlElementToList(Match xmldom, List<IFiCol> listColumn, List<T> listData, String elementName, Class<T> clazz) {

		xmldom.find(elementName).forEach(
				xmlElement -> {
					T objectt = parseXmlElementToEntity(xmlElement, listColumn, clazz);
					if (objectt != null) listData.add(objectt);
				});

	}

	public <T> T parseXmlElementToEntity(Element xmlElement, List<IFiCol> listColumn, Class<T> clazz) {

		T objectt = new FiReflection().generateObject(clazz);

		for (int colidx = 0; colidx < listColumn.size(); colidx++) {

			IFiCol ozTableCol = listColumn.get(colidx);

			String text = $(xmlElement).find(ozTableCol.getHeaderName()).text();
			//println(" CellValue:"+text);
			new FiReflection().setter(ozTableCol, objectt, text);

		}

		return objectt;
	}

	/**
	 * @param xmlMatch
	 * @param listColumn
	 * @param clazz
	 * @param boChildSameClassCheck Çocuk elementi aynı sınıfa sahipse,aynı entity üzerine yazmaya devam eder.
	 * @param entity
	 * @param <EntClazz>
	 * @return
	 */
	public static <EntClazz> EntClazz parseMatchToEntityWithOneChild(Match xmlMatch, List<FiCol> listColumn, Class<EntClazz> clazz, Boolean boChildSameClassCheck, EntClazz entity) {

		if (entity == null) entity = FiReflection.generateObject(clazz);

		for (int colidx = 0; colidx < listColumn.size(); colidx++) {

			// L1 means Layer1 or Parent
			FiCol fiColParent = listColumn.get(colidx);

			if (fiColParent.getColTypeNtn() == OzColType.XmlChildList) {

				List listChildren = parseMatchElementToList(xmlMatch, fiColParent.getHeaderName(), fiColParent.getListChildCol(), fiColParent.getChildClazz(), false);
				FiReflection.setProperty(entity, fiColParent.getFieldName(), listChildren);
				continue;
			}

			if (fiColParent.getColTypeNtn() == OzColType.XmlChild) {

				Match elementChild = xmlMatch.find(fiColParent.getHeaderName());

				if (FiBoolean.isTrue(boChildSameClassCheck) && fiColParent.getChildClazz().equals(clazz)) {
					parseMatchToEntityWithOneChild(elementChild, fiColParent.getListChildCol(), fiColParent.getChildClazz(), boChildSameClassCheck, entity);

				} else {
					Object objChild = parseMatchToEntityWithOneChild(elementChild, fiColParent.getListChildCol(), fiColParent.getChildClazz(), boChildSameClassCheck, null);
					FiReflection.setProperty(entity, fiColParent.getFieldName(), objChild);
				}

				continue;
			}

			if (fiColParent.getColGenTypeNtn().equals(OzColType.XmlAttribute)) {
				String text = xmlMatch.attr(fiColParent.getHeaderName());
				FiReflection.setter(fiColParent, entity, text);
//			Loghelper.get(FiXmlParser.class).debug("Attr HeaderName:"+ fiColParent.getHeaderName());
//			Loghelper.get(FiXmlParser.class).debug("Attr Value:" + text);
				continue;
			}

			//Loghelper.get(FiXmlParser.class).debug("HeaderName:"+ fiColParent.getHeaderName());

			Match elementChild = xmlMatch.find(fiColParent.getHeaderName());
			String text = elementChild.text();
			FiReflection.setter(fiColParent, entity, text);
		}
		return entity;
	}


	public static <EntClazz> Fdr<List<EntClazz>> parseXmlFileToEntityListWithChild(File fileXml, String txSelectorTag, List<FiCol> listColumn, Class<EntClazz> clazz) {

		//Loghelper.get(FiXmlParser.class).debug("parseXmlToEntityListWithChild");

		Fdr<List<EntClazz>> fdrResult = new Fdr<>();
		Match xmlRoot = FiXmlParser.openXmlFile(fileXml);

		List<EntClazz> entClazzes = parseMatchElementToList(xmlRoot, txSelectorTag, listColumn, clazz, false);
		fdrResult.setValue(entClazzes);

		fdrResult.setBoResult(true);
		return fdrResult;

	}

	public static <EntClazz> List<EntClazz> parseMatchElementToList(String txXml, String txSelectorTag, List<FiCol> fiColList, Class<EntClazz> clazz) {
		return parseMatchElementToList(openXml(txXml), txSelectorTag, fiColList, clazz, false);
	}

	public static <EntClazz> List<EntClazz> parseMatchElementToList(String txXml, String txSelectorTag, List<FiCol> fiColList, Class<EntClazz> clazz, Boolean boChildSameClassCheck) {
		return parseMatchElementToList(openXml(txXml), txSelectorTag, fiColList, clazz, boChildSameClassCheck);
	}

	public static <EntClazz> List<EntClazz> parseMatchElementToList(Match xmlMatch, String txSelectorTag, List<FiCol> fiColList, Class<EntClazz> clazz, Boolean boChildSameClassCheck) {

		if (xmlMatch == null) return new ArrayList<>();

		List<EntClazz> dataList = new ArrayList<>();

		for (Element xmlElement : xmlMatch.find(txSelectorTag)) {
			EntClazz entity = FiXmlParser.parseMatchToEntityWithOneChild($(xmlElement), fiColList, clazz, boChildSameClassCheck, null);
			dataList.add(entity);
		}

		return dataList;
	}

}