package ozpasyazilim.utils.log;

public class UtilozLog {

	private static Class thisclass = UtilozLog.class;

	public static void null_check_log(Object object, String varname, Class logclass) {

		if (varname == null) varname = "";
		if (logclass == null) logclass = UtilozLog.class;

		Boolean nullcheck = (object == null);

		Loghelper.get(thisclass).info("Logfrom:"
			+ logclass.getName()
			+ " Var Name:"
			+ varname
			+ "isNull:"
			+ nullcheck);

	}

}
