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
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.datatypes.FiKeybean;

/**
 * Xml alanlarını okumamızı sağlar ve
 * <p>
 * Xml Template içinde değişkenlerin {{exampleVar}} iki süslü parentez içerisinde olanları fkbParamsdaki değerleriyle değiştirir.
 */
public class FiXml {

    private FiXmlDomDoc fiXmlDomDoc;

    /**
     * Document olarak okunan xml'in raw hali
     */
    private String txXmlRaw;

    /**
     * Xml Alanlarının belirtildiği xml içerik
     */
    private String txXmlTemplate;

    /**
     * Xml kullanılan String Interpolation değişkenlerin değerleri tutulur
     */
    private FiKeybean fkbParams;

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
        FiXmlDomDoc fiXmlDomDoc = fiXml.getFiDoc();

        System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
        System.out.println("------");

        String txKullaniciKod = fiXmlDomDoc.getElementValueByTagName("DisKullaniciKimlikDogrula2Result");
        System.out.println("Token:" + txKullaniciKod);

    }

    public FiXml makeDocument(String txXml) {
        setDoc(convertXmlContentToDoc(txXml));
        setTxXmlRaw(txXml);
        return this;
    }

    public static Document convertXmlContentToDoc(String txXmlContent) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(txXmlContent));
            return db.parse(is);
        } catch (ParserConfigurationException | IOException | SAXException e) {
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
        if (fiXmlDomDoc == null) return null;
        return fiXmlDomDoc.getDoc();
    }

    public void setDoc(Document docXml) {
        this.fiXmlDomDoc = new FiXmlDomDoc(docXml);
    }

    public FiXmlDomDoc getFiDoc() {
        return fiXmlDomDoc;
    }

    public void setFiDoc(FiXmlDomDoc fiXmlDomDoc) {
        this.fiXmlDomDoc = fiXmlDomDoc;
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

    public FiKeybean getMapParamsInit() {
        if (fkbParams == null) {
            fkbParams = new FiKeybean();
        }
        return fkbParams;
    }

    @FiDraft
    public void convertCommentParams(){
// Düzenli ifade ile değişiklik yapılıyor
        // String updatedXml = xmlContent.replaceAll("<!--\\s*\\{\\{edmFaturaAraYon}}\\s*-->", "{{edmFaturaAraYon}}");


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

    public String getXmlFkbConverted() {
        if (getMapParamsInit().isEmpty()) {
            setTxXmlRaw(getTxXmlRaw());
        } else {
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