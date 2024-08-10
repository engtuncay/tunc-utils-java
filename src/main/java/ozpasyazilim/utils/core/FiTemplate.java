package ozpasyazilim.utils.core;

import org.apache.commons.lang3.text.StrSubstitutor;
import ozpasyazilim.utils.datatypes.FiKeyBean;

public class FiTemplate {

    /**
     * {{namedParameter}} ile değerlerini yer değişitirir
     *
     * @param txTemplate
     * @param fiKeyBean
     * @return
     */
    public static String replaceParams(String txTemplate, FiKeyBean fiKeyBean) {
        StrSubstitutor sub = new StrSubstitutor(fiKeyBean, "{{", "}}");
        return sub.replace(txTemplate);
    }


}
