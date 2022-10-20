package ozpasyazilim.utils.core;

public class FiObject {
    public static String nullcheckstring(Object object) {
    
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        return null;
    }

    public static String nullcheckstringandinitialize(Object object) {
    
        if (object == null) {
            return "";
        }
        if (object instanceof String) {
            return (String) object;
        }
        return "";
    }

    public static Integer nullcheckinteger(Object object) {
    
        if (object == null) {
            return null;
        }
        if (object instanceof Integer) {
            return (Integer) object;
        }
        if (object instanceof String) {
            return (Integer) object;
        }
        return null;
    }

    public static Integer nullcheckintegerandinitialize_zero(Object object) {
    
        if (object == null) {
            return 0;
        }
        if (object instanceof String) {
            return (Integer) object;
        }
        if (object instanceof Integer) {
            return (Integer) object;
        }
        return null;
    }

    public static <E> E nullcheckobject(Object object) {
    
        if (object == null) {
            return null;
        }
        return (E) object;
    }

    public static Double nullcheckdoubleandinitialize_zero(Object object) {
        if (object == null) {
            return 0.0D;
        }
        if (object instanceof Double) {
            return (Double) object;
        }
        if (object instanceof Float) {
            return ((Float) object).doubleValue();
        }
        if (object instanceof String) {
            return (Double) object;
        }
        return null;
    }

    public static Object nullcheckObject_init_string(Object object) {
        if (object == null) {
            return "";
        }
        return object;
    }

    /**
     * use FiBoolean
     * @param filterable
     * @return
     */
    @Deprecated
    public static boolean isTrue(Boolean filterable) {

    	if(filterable!=null && filterable==true) return true;
    	return false;
    }

    public static <E> E orValue(E object,E orValue) {
        if (object == null) return orValue;
        return object;
    }

}
