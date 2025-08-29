package ozpasyazilim.utils.core;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.w3c.dom.Document;
import ozpasyazilim.utils.datatypes.FiKeybean;

import java.io.*;
import java.util.Properties;

/**
 * FiKeyString yerine FiKeyBean konuldu.
 */
public class FiXml2 {
	private FiXmlDomDoc fiXmlDomDoc;
	private String txXmlValue;

	/**
	 * xml'in ilk haline, değişkenler dönüştürülmemiş hali
	 */
	private String txXmlTemplate;
	private FiKeybean fkbParams;

	public FiXml2() {
	}

	public FiXml2(String txXmlTemplate) {
		this.txXmlTemplate = txXmlTemplate;
	}

	public static FiXml2 bui() {
		return new FiXml2();
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

		FiXml2 fiXml = FiXml2.bui().makeDocument(xml);

		//fiXml.getDocXml()
		Document doc = fiXml.getDoc();
		FiXmlDomDoc fiXmlDomDoc = fiXml.getFiDoc();

		System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
		System.out.println("------");

		String txKullaniciKod = fiXmlDomDoc.getElementValueByTagName("DisKullaniciKimlikDogrula2Result");
		System.out.println("Token:"+txKullaniciKod);

	}

	public FiXml2 makeDocument(String txXml){
		setDoc(FiXmlUtil.parseXmlFile(txXml));
		setTxXmlValue(txXml);
		return this;
	}


	public String getTxXmlRawFormatted() {
		return FiXmlUtil.formatXML(getTxXmlValue());
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
		Document doc = FiXmlUtil.convertStringToXMLDocument(xmlStr);

		//Verify XML document is build correctly
		System.out.println(doc.getFirstChild().getNodeName());
	}



	public Document getDoc() {
		if(fiXmlDomDoc ==null) return null;
		return getFiDomDocInit().getDoc();
	}

	public Document getDocInit() {
		return getFiDomDocInit().getDoc();
	}

	public void setDoc(Document docXml) {
		this.fiXmlDomDoc = new FiXmlDomDoc(docXml);
	}

	public FiXmlDomDoc getFiDoc() {
		return fiXmlDomDoc;
	}

	public FiXmlDomDoc getFiDomDocInit() {
		if (fiXmlDomDoc == null) {
			// Xml Value Boş degilse, Xml Value Document oluşturur
			if(!FiString.isEmpty(getTxXmlValue())){
			   makeDocument(getTxXmlValue());
			}else{
				makeDocument(getTxXmlTemplate());
			}
		}
		return fiXmlDomDoc;
	}

	public FiXmlDomDoc getFiDomDoc() {
		return fiXmlDomDoc;
	}

	public void setFiDomDoc(FiXmlDomDoc fiXmlDomDoc) {
		this.fiXmlDomDoc = fiXmlDomDoc;
	}

	public void setFiDoc(FiXmlDomDoc fiXmlDomDoc) {
		this.fiXmlDomDoc = fiXmlDomDoc;
	}

	public String getTxXmlValue() {
		return txXmlValue;
	}

	public void setTxXmlValue(String txXmlValue) {
		this.txXmlValue = txXmlValue;
	}

	public String getTxXmlTemplate() {
		return txXmlTemplate;
	}

	public void setTxXmlTemplate(String txXmlTemplate) {
		this.txXmlTemplate = txXmlTemplate;
	}

	public FiKeybean getMapParamsInit() {
		if (fkbParams == null) {
			fkbParams = new FiKeybean();
		}
		return fkbParams;
	}

	public void setFkbParams(FiKeybean fkbParams) {
		this.fkbParams = fkbParams;
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

	/**
	 *
	 * @return
	 */
	public String bindXmlVariables() {

		if(getMapParamsInit().isEmpty()) {
			setTxXmlValue(getTxXmlTemplate());
		}else{
			StrSubstitutor sub = new StrSubstitutor(getMapParamsInit(), "{{", "}}");
			setTxXmlValue(sub.replace(getTxXmlTemplate()));
		}
		return getTxXmlValue();
	}

	public String getElementValueByTagName(String txTagName) {
		return getFiDoc().getElementValueByTagName(txTagName);
	}

	public FiKeybean getFkbParams() {
		return fkbParams;
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