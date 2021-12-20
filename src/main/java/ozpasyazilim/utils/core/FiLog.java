package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import java.util.List;

public class FiLog {

	public static void logFirstObject(List data,Class clazz) {

		for (Object datum : data) {
			if(datum==null) continue;
			Loghelper.debugLog(clazz, FiConsole.textObjectFieldsPlain(datum,null));
			break;
		}

	}

	public void logNull(String varName, Object varObject){
		Loghelper.get(getClass()).debug(" isNull "+varName + " :" + (varObject==null?"true":"false") );
	}

}
