package ozpasyazilim.utils.core;

import com.github.underscore.lodash.Xml;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Properties;

import com.github.underscore.lodash.U;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.datatypes.FiKeyString;

public class FiXml {
	private FiDomDoc fiDomDoc;
	private String txXmlRaw;
	private String txXmlTemplate;
	private FiKeyBean fksParams;

	public FiXml() {
	}

	public FiXml(String txXmlTemplate) {
		this.txXmlTemplate = txXmlTemplate;
	}

	public static FiXml bui() {
		return new FiXml();
	}

	public static FiXml buiMakeDocument() {
		return new FiXml();
	}

	public static void main(String[] args) {

		// Instantiate the Factory
		// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// optional, but recommended
		// process XML securely, avoid attacks like XML External Entities (XXE)
		// dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
				"<soap:Body>" +
				"<DisKullaniciKimlikDogrula2Response xmlns=\"http://tempuri.org/\">" +
				"<DisKullaniciKimlikDogrula2Result>121212121</DisKullaniciKimlikDogrula2Result>" +
				"</DisKullaniciKimlikDogrula2Response>" +
				"</soap:Body></soap:Envelope>";

		FiXml fiXml = FiXml.bui().makeDocument(xml);

		//fiXml.getDocXml()
		Document doc = fiXml.getDoc();
		FiDomDoc fiDomDoc = fiXml.getFiDoc();

		System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
		System.out.println("------");

		String txKullaniciKod = fiDomDoc.getElementValueByTagName("DisKullaniciKimlikDogrula2Result");
		System.out.println("Token:"+txKullaniciKod);

	}

	public FiXml makeDocument(String txXml){
		setDoc(parseXmlFile(txXml));
		setTxXmlRaw(txXml);
		return this;
	}

	public static Document parseXmlFile(String strXml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(strXml));
			return db.parse(is);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//format the XML in your String
	public static String formatXML(String unformattedXml) {
		return U.formatXml(unformattedXml);
//		try {
//
//			Document document = parseXmlFile(unformattedXml);
//			OutputFormat format = new OutputFormat(document);
//			format.setIndenting(true);
//			format.setIndent(3);
//			format.setOmitXMLDeclaration(true);
//			Writer out = new StringWriter();
//			XMLSerializer serializer = new XMLSerializer(out, format);
//			serializer.serialize(document);
//			return out.toString();
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
	}

	public String getTxXmlRawFormatted() {
		return U.formatXml(FiString.orEmpty(getTxXmlRaw()), Xml.XmlStringBuilder.Step.TABS);
	}

	public static void test() {

		final String xmlStr = "<employees>" +
				"   <employee id=\"101\">" +
				"        <name>Lokesh Gupta</name>" +
				"       <title>Author</title>" +
				"   </employee>" +
				"   <employee id=\"102\">" +
				"        <name>Brian Lara</name>" +
				"       <title>Cricketer</title>" +
				"   </employee>" +
				"</employees>";

		//Use method to convert XML string content to XML Document object
		Document doc = convertStringToXMLDocument(xmlStr);

		//Verify XML document is build correctly
		System.out.println(doc.getFirstChild().getNodeName());
	}

	public static Document convertStringToXMLDocument(String xmlString) {
		//Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		//API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			//Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			//Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Document getDoc() {
		if(fiDomDoc ==null) return null;
		return fiDomDoc.getDoc();
	}

	public void setDoc(Document docXml) {
		this.fiDomDoc = new FiDomDoc(docXml);
	}

	public FiDomDoc getFiDoc() {
		return fiDomDoc;
	}

	public void setFiDoc(FiDomDoc fiDomDoc) {
		this.fiDomDoc = fiDomDoc;
	}

	public String getTxXmlRaw() {
		return txXmlRaw;
	}

	public void setTxXmlRaw(String txXmlRaw) {
		this.txXmlRaw = txXmlRaw;
	}

	public String getTxXmlTemplate() {
		return txXmlTemplate;
	}

	public void setTxXmlTemplate(String txXmlTemplate) {
		this.txXmlTemplate = txXmlTemplate;
	}

	public FiKeyBean getMapParamsInit() {
		if (fksParams == null) {
			fksParams = new FiKeyBean();
		}
		return fksParams;
	}

	public void setFksParams(FiKeyBean fksParams) {
		this.fksParams = fksParams;
	}

	public Properties readXmlAsProp(String xmlFilename, Class clazz) {

		File file = null;

		try {

			String fileName = xmlFilename + ".xml";
			System.out.println(fileName);
			String absPath = clazz.getResource("").getPath() + fileName;
			file = new File(absPath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		InputStream fileIS = null;
		try {
			fileIS = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		//InputStream fileIS = clazz.getResourceAsStream("./"+xmlFilename+".xml");

		System.out.println("path:" + file.getPath());

		Properties prop = new Properties();

		try {
			prop.loadFromXML(fileIS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	public String getXmlConverted() {
		if(getMapParamsInit().isEmpty()) {
			setTxXmlRaw(getTxXmlRaw());
		}else{
			StrSubstitutor sub = new StrSubstitutor(getMapParamsInit(), "{{", "}}");
			setTxXmlRaw(sub.replace(getTxXmlTemplate()));
		}
		return getTxXmlRaw();
	}

	public String getElementValueByTagName(String txTagName) {
		return getFiDoc().getElementValueByTagName(txTagName);
	}

	// Xml Example For Prop
	/*
	<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">

			<properties>
			<entry key="Super Duper UNIQUE Key">
                MEGA
                LONG
                MULTILINE
           </entry>
		</properties>
	 */
}


// Soap İsteğinin Sonuna yazılmıştı
//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
// Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
// NodeList nodeLst = document.getElementsByTagName(rootTagName); //"<TagName of the element to be retrieved>"
// String elementValue = nodeLst.item(0).getTextContent();
// System.out.println(elementValue);

// Write the SOAP message formatted to the console.
// String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
// System.out.println(formattedSOAPResponse);
// return elementValue;