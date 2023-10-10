package ozpasyazilim.utils.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 25-09-2023 coded
 * <p>
 * Xml üretmek için yardımcı metodlar
 */
public class FiXmlMaker {

    StringBuilder sbXml;

    List<String> listOpenedTags;

    public static FiXmlMaker bui() {
        return new FiXmlMaker();
    }

    public StringBuilder getSbXmlInit() {
        if (sbXml == null) {
            sbXml = new StringBuilder();
        }
        return sbXml;
    }

    public void setSbXml(StringBuilder sbXml) {
        this.sbXml = sbXml;
    }

    public StringBuilder getSbXml() {
        return sbXml;
    }

    public List<String> getListOpenedTagsInit() {
        if (listOpenedTags == null) {
            listOpenedTags = new ArrayList<>();
        }
        return listOpenedTags;
    }

    public void setListOpenedTags(List<String> listOpenedTags) {
        this.listOpenedTags = listOpenedTags;
    }

    public FiXmlMaker openCloseTag(String tagName, String content) {
        openTag(tagName);
        getSbXmlInit().append(FiString.orEmpty(content));
        closeTag(tagName);
        return this;
    }

    private void closeTag(String tagName) {
        getSbXmlInit().append("</");
        getSbXmlInit().append(tagName);
        getSbXmlInit().append(">");
    }

    private void openTag(String tagName) {
        getSbXmlInit().append("<");
        getSbXmlInit().append(tagName);
        getSbXmlInit().append(">");
    }

    public FiXmlMaker openTagWitMem(String tagName) {
        openTag(tagName);
        getListOpenedTagsInit().add(tagName);
        return this;
    }


}
