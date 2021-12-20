package ozpasyazilim.utils.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiErp {

	public static void main(String[] args) {
		String efat = "DML20190000000002039";
		System.out.println(efaturaRemoveZeros(efat));
	}

	public static String efaturaRemoveZeros(String text) {

		if(FiString.isEmptyTrim(text)) return text;

		if(text.length()>7){
			String efaturaSeri = text.substring(0,3);
			String efaturaYil = text.substring(3,7);
			String efaturaNo = text.substring(7,text.length());
			efaturaNo = FiString.clearFirstZeros(efaturaNo);
			return efaturaSeri+"-"+efaturaYil+"-"+efaturaNo;
		}

		return text;
	}

	public static Boolean validateVergiNo(String txVergiNo) {
		if(txVergiNo==null || FiString.isEmptyTrim(txVergiNo)) return false;
		return txVergiNo.trim().matches("\\d{10}");
	}

	public static Boolean validateVergiOrTcNo(String txVergiNo) {
		if(txVergiNo==null || FiString.isEmptyTrim(txVergiNo)) return false;

		Boolean matches = txVergiNo.trim().matches("\\d{10}");
		if(matches) return true;

		Boolean matches2 = txVergiNo.trim().matches("\\d{11}");
		if(matches2) return true;

		return false;
	}
}
