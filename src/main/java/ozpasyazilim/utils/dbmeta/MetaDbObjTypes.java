package ozpasyazilim.utils.dbmeta;

import ozpasyazilim.utils.datatypes.FiMeta;
import ozpasyazilim.utils.datatypes.FiMetaKey;

public class MetaDbObjTypes {


    public static FiMetaKey table(){
        return new FiMetaKey("Tablo");
    }

    public static FiMetaKey index() {
        return new FiMetaKey("Index");
    }

}
