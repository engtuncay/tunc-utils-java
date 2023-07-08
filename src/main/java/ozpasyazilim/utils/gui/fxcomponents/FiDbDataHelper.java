package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiString;

public class FiDbDataHelper {


    public static String getSqlValueWithQuote(Object objValue, String dataType) {

        if(objValue==null) return "null";

        // Sayısal değerler aynı şekilde verilir
        if(FiString.equalsOne(dataType,"tinyint","smallint","int","float","double")){
            return objValue.toString();
        }

        // Text değerler çift tırnak içinde verilir
        if(FiString.equalsOne(dataType,"nvarchar","varchar")){
            return "\""+objValue.toString()+"\"";
        }

        if(FiString.equalsOne(dataType,"datetime")){
            return "\""+ objValue.toString()+"\"";
        }

        return "NaN";
    }
}
