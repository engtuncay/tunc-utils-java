package ozpasyazilim.utils.core;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.w3c.dom.Document;

/**
 * Wrapper class for w3c.dom.Document
 */
public class FiDomDoc {

	Document doc;

	public FiDomDoc(Document docXml) {
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