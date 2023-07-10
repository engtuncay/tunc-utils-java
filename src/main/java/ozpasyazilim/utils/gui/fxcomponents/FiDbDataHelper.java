package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiString;

/**
 * Db Data ile ilgili işlemler
 */
public class FiDbDataHelper {


    /**
     *
     * Gelen değeri , sql sorgusunu yazılabilecek şekle getirir, örneğin stringi tırnak içerisine alır.
     *
     * @param objValue
     * @param dataType
     * @return
     */
    public static String getSqlValueWithQuote(Object objValue, String dataType) {

        if (objValue == null) return "null";

        // Sayısal değerler aynı şekilde verilir
        if (FiString.equalsOne(dataType, "tinyint", "smallint", "int", "float", "double")) {
            return objValue.toString();
        }

        // Text değerler çift tırnak içinde verilir
        if (FiString.equalsOne(dataType, "nvarchar", "varchar")) {
            return "'" + objValue + "'";
        }

        if (FiString.equalsOne(dataType, "datetime")) {
            return "'" + objValue + "'";
        }

        return "NaN";
    }
}
