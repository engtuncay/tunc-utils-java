package ozpasyazilim.utils.dbmeta;

import ozpasyazilim.utils.datatypes.FiMeta;
import ozpasyazilim.utils.datatypes.FiMetaKey;

public class MetaDbObjTypes {


    public static FiMeta table(){
        return new FiMeta("Tablo");
    }

    public static FiMeta index() {
        return new FiMeta("Index");
    }

    public static FiMeta trigger() {
        return new FiMeta("Trigger");
    }

    public static FiMeta function() {
        return new FiMeta("Function");
    }

    public static FiMeta view() {
        return new FiMeta("View");
    }

    /*
    public static String getTypeNameTrigger() {
        return "Trigger";
    }

    public static String getTypeNameView() {
        return "View";
    }

    private static String getTypeNameFunc() {
        return "Function";
    }
     */

}
