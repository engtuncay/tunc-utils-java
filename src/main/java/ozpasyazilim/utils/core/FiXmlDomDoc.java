package ozpasyazilim.utils.core;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Wrapper class for w3c.dom.Document
 */
public class FiXmlDomDoc {

	Document doc;

	String txXmlRaw;

	public static FiXmlDomDoc buiWitRaw(String txXmlContent){
		Document document = convertXmlContentToDoc(txXmlContent);
		FiXmlDomDoc fiXmlDomDoc = new FiXmlDomDoc(document);
		fiXmlDomDoc.setTxXmlRaw(txXmlContent);
		return fiXmlDomDoc;
	}

	public FiXmlDomDoc(Document docXml) {
		setDoc(docXml);
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getElementValueByTagName(String tagName) {
		if(getDoc().getElementsByTagName(tagName).getLength()>0){
			return getDoc().getElementsByTagName(tagName).item(0).getTextContent();
		}else{
			return null;
		}
	}

	public boolean checkElementExistByTagName(String tagName) {
        return getDoc().getElementsByTagName(tagName).getLength() > 0;
	}

	public String getTxXmlRaw() {
		return txXmlRaw;
	}

	public void setTxXmlRaw(String txXmlRaw) {
		this.txXmlRaw = txXmlRaw;
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
}

// Xmldeki belli bir alanı okumak için

// 		// get <staff>
//		NodeList list = doc.getElementsByTagName("staff");
//
//		for (int temp = 0; temp < list.getLength(); temp++) {
//
//			Node node = list.item(temp);
//
//			if (node.getNodeType() == Node.ELEMENT_NODE) {
//
//				Element element = (Element) node;
//
//				// get staff's attribute
//				String id = element.getAttribute("id");
//
//				// get text
//				String token = element.getElementsByTagName("DisKullaniciKimlikDogrula2Result").item(0).getTextContent();
//
////				String lastname = element.getElementsByTagName("lastname").item(0).getTextContent();
////				String nickname = element.getElementsByTagName("nickname").item(0).getTextContent();
//
////				NodeList salaryNodeList = element.getElementsByTagName("salary");
////				String salary = salaryNodeList.item(0).getTextContent();
//
//				// get salary's attribute
////				String currency = salaryNodeList.item(0).getAttributes().getNamedItem("currency").getTextContent();
//
//				System.out.println("Current Element :" + node.getNodeName());
//				System.out.println("Staff Id : " + id);
//				System.out.println("Token: " + token);
////				System.out.println("Last Name : " + lastname);
////				System.out.println("Nick Name : " + nickname);
////				System.out.printf("Salary [Currency] : %,.2f [%s]%n%n", Float.parseFloat(salary), currency);
//
//			}
//		}