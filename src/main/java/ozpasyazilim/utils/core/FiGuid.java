package ozpasyazilim.utils.core;

import java.util.UUID;

public class FiGuid {

	public static String genGuid(){
		return UUID.randomUUID().toString().toUpperCase();
	}

}
