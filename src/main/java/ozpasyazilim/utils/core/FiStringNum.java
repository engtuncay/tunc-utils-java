package ozpasyazilim.utils.core;

import org.apache.commons.lang3.text.StrSubstitutor;
import ozpasyazilim.utils.datatypes.FiKeyBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiStringNum {

    FiKeyBean fkbParams;

    String txTemplate;

    public FiStringNum(String txTemplate) {
        this.txTemplate = txTemplate;
    }

    public static FiStringNum bui(String txTemplate) {
        FiStringNum fiStringNum = new FiStringNum(txTemplate);
        return fiStringNum;
    }

    public FiStringNum addParams(Integer lnValue, Object value) {
        getFkbParams().add(lnValue.toString(), FiString.orEmpty(value));
        return this;
    }

    public String getTxTemplate() {
        return txTemplate;
    }

    public void setTxTemplate(String txTemplate) {
        this.txTemplate = txTemplate;
    }

    public FiKeyBean getFkbParams() {
        if (fkbParams == null) {
            fkbParams = new FiKeyBean();
        }
        return fkbParams;
    }

    public void setFkbParams(FiKeyBean fkbParams) {
        this.fkbParams = fkbParams;
    }

    public String tos() {
        return tos2();
    }

    public String tos1() {
        return substitutor(getTxTemplate(), getFkbParams());
    }

    /**
     * namedParametreleri ({...}) string değerleri ile yer değiştirir.
     * <p>
     * Source : https://www.baeldung.com/java-string-formatting-named-placeholders
     *
     * @return
     */
    public String tos2() { //String template, Map<String, Object> parameters
        StringBuilder newTemplate = new StringBuilder(getTxTemplate());
        List<Object> valueList = new ArrayList<>();

        //Matcher matcher = Pattern.compile("[$][{](\\w+)}").matcher(getTxTemplate());
        Matcher matcher = Pattern.compile("[{](\\w+)}").matcher(getTxTemplate());

        // String'de bulunan key'lerin yerine %s eklemiş, bir sonraki adım %s leri String.format ile yer değiştirmiş
        while (matcher.find()) {
            String key = matcher.group(1);

            //String paramName = "${" + key + "}";
            String paramName = "{" + key + "}";
            int index = newTemplate.indexOf(paramName);
            if (index != -1) {
                newTemplate.replace(index, index + paramName.length(), "%s");
                valueList.add(getFkbParams().get(key));
            }
        }

        return String.format(newTemplate.toString(), valueList.toArray());
    }

    // bunda bazılarını çevirmedi
    public String substitutor(String txTemplate, FiKeyBean fiKeyBean) {
        StrSubstitutor sub = new StrSubstitutor(fiKeyBean, "{", "}");
        return sub.replace(txTemplate);
    }


}
