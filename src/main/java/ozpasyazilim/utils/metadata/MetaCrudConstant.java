package ozpasyazilim.utils.metadata;

import ozpasyazilim.utils.datatypes.FiMeta;

/**
 * Program kullanılan string sabit değerler
 */
public class MetaCrudConstant {

	public MetaCrudConstant() {
	}

	public static MetaCrudConstant bui() {
		return new MetaCrudConstant();
	}

	public static String soapAction(){
		return "SOAPaction";
	}

	public static String editAction(){
		return "edit";
	}

	public static String addAction(){
		return "add";
	}

	public static FiMeta ekstreDevirBelgeNoContent() {
		return FiMeta.bui("_DEVİR_");
	}

}
