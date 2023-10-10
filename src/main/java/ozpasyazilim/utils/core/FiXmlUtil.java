package ozpasyazilim.utils.core;

import com.github.underscore.lodash.U;
import com.github.underscore.lodash.Xml;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ozpasyazilim.utils.log.Loghelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class FiXmlUtil {

    public static Document parseXmlFile(String txXml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(txXml));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            Loghelper.get(getClassi()).debug(FiException.exceptionIfToString(e));
            //throw new RuntimeException(e);
        } catch (SAXException e) {
            Loghelper.get(getClassi()).debug(FiException.exceptionIfToString(e));
            //throw new RuntimeException(e);
        } catch (IOException e) {
            Loghelper.get(getClassi()).debug(FiException.exceptionIfToString(e));
            //throw new RuntimeException(e);
        }
        return null;
    }

    private static Class<FiXmlUtil> getClassi() {
        return FiXmlUtil.class;
    }


    //format the XML in your String
    public static String formatXML(String unformattedXml) {
        return U.formatXml(FiString.orEmpty(unformattedXml), Xml.XmlStringBuilder.Step.TABS);

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
            //e.printStackTrace();
            Loghelper.get(FiXml2.class).error(FiException.exTosMain(e));
        }
        return null;
    }

}
