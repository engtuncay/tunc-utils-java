package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Finishings;
import javax.swing.JTable;

public class FiNumberToText {

	public static final String[] birler = {"", "BİR", "İKİ", "ÜÇ", "DÖRT", "BEŞ", "ALTI", "YEDİ", "SEKİZ", "DOKUZ"};
	public static final String[] onlar = {"", "ON", "YİRMİ", "OTUZ", "KIRK", "ELLİ", "ALTMIŞ ", "YETMİŞ", "SEKSEN",
			"DOKSAN"};
	public static final String[] basamaklar = {"", "BİN", "MİLYON"};

	public static int rakamBul(int i, int sayi) { // sayinin i basamagindaki rakamini bulur
		if (i <= 0)
			return 0;
		else
			return ((int) (sayi % Math.pow(10, i)) / (int) (Math.pow(10, i - 1)));
	}

	public static Optional<Integer> parseInt(String toParse) {
		try {
			return Optional.of(Integer.parseInt(toParse));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	public static int[] sayiAyir(long sayi_) { // sayiyi ucer ucer ayirir

		String sayi = "" + sayi_;
		int uzunluk = sayi.length();
		int[] sayilar = new int[uzunluk / 3 + ((uzunluk % 3 == 0) ? 0 : 1)];
		sayi = "000" + sayi;
		int i = 0;
		try {
			while (i < uzunluk / 3 + ((uzunluk % 3 == 0) ? 0 : 1)) {
				sayilar[i] = Integer.parseInt(sayi.substring(sayi.length() - 3));
				sayi = sayi.substring(0, sayi.length() - 3);
				i++;
			}
		} catch (Exception e) {
			sayilar[i] = Integer.parseInt(sayi);
		}

		return sayilar;
	}

	public static String yuzlukCevir(int sayi) { // uc basamakli sayiyi cevirir
		if (sayi / 1000 > 0)
			return "hatali sayi !";
		else if (sayi == 0)
			return "sifir";
		else {
			String sayininBirleri = birler[rakamBul(1, sayi)];
			String sayininOnlari = onlar[rakamBul(2, sayi)];
			String sayininYuzleri;
			if (rakamBul(3, sayi) == 0)
				sayininYuzleri = "";
			else if (rakamBul(3, sayi) == 1)
				sayininYuzleri = "YüZ";
			else
				sayininYuzleri = birler[rakamBul(3, sayi)] + "YüZ";
			return sayininYuzleri + sayininOnlari + sayininBirleri;
		}
	}

	public static String sayiCevir(long sayi) { // 1 kentrilyondan kucuk sayiyi cevirir.
		if (sayi == 0)
			return "sifir";
		String sonuc = "";
		String eksi = "";
		if (sayi < 0) {
			eksi = "eksi ";
			sayi = -sayi;
		}
		int[] bolumler = sayiAyir(sayi);
		if (bolumler.length > 6)
			return "Cok buyuk sayi";
		else {
			for (int i = 0; i < bolumler.length; i++) {
				if (i == 0) {
					if (bolumler[i] != 0)
						sonuc = sonuc + yuzlukCevir(bolumler[i]);
				} else if (i == 1) {
					if (bolumler[i] == 1)
						sonuc = basamaklar[i] + " " + sonuc;
					else if (bolumler[i] != 0)
						sonuc = yuzlukCevir(bolumler[i]) + basamaklar[i] + " " + sonuc;
				} else {
					if (bolumler[i] != 0)
						sonuc = yuzlukCevir(bolumler[i]) + basamaklar[i] + " " + sonuc;
				}
			}
			return eksi + sonuc;
		}
	}

	public static String sayiCevir(double sayi) {
		String sayiString = sayi + "";
		String sonuc = "";
		sonuc = sayiCevir(new Long(sayiString.substring(0, sayiString.indexOf("."))));
		sonuc += " TL.";
		if (sayiString.substring(sayiString.indexOf(".") + 1, sayiString.length()).length() == 1)
			sayiString = sayiString + "0";
		sonuc += sayiCevir(new Long(sayiString.substring(sayiString.indexOf(".") + 1, sayiString.length())));
		sonuc += " KR.";
		return sonuc;
	}

	public static String sayiCevir(String sayi) {
		DecimalFormat df = new DecimalFormat("#.##");
		sayi = df.format(new BigDecimal(sayi));
		sayi = sayi.replaceAll(",", ".");
		if (sayi.length() < 2)
			return sayiCevir(new Long(sayi)) + " TL.";
		if (sayi.length() - sayi.indexOf(".") < 3)
			sayi = sayi + "0";
		String sayiString = sayi + "";
		String sonuc = "";
		if (sayiString.indexOf(".") < 0)
			return sayiCevir(new Long(sayiString)) + " TL.";
		sonuc = sayiCevir(new Long(sayiString.substring(0, sayiString.indexOf("."))));
		sonuc += " TL.";
		sonuc += sayiCevir(new Long(sayiString.substring(sayiString.indexOf(".") + 1, sayiString.indexOf(".") + 3)));
		sonuc += " KR.";
		return sonuc;
	}

	public static void main(String[] args) {
		System.out.println(FiNumberToText.sayiCevir("11"));
	}

	static NumberFormat formatterBinlik = new DecimalFormat("#,###");

	private static Class getclass = FiNumberToText.class;

	public static String binlikAyir(Long number) {
		if (number == null)
			return null;
		return formatterBinlik.format(number);
	}

	// Ozpasyazilim util methods

	/**
	 * Farklı bölgesel ayarlarda ondalık ayraç farklı geldikleri için tek tipe dönüşüm yapılması saglandı.
	 * Virgüllü veya Noktalı Sayıları (23,12 veya 23.12 ) , Noktalı Tipe Dönüştürür ( 23.12 )
	 *
	 * @param strNumberOld
	 * @return
	 */
	public static String convertNumberFormatToDotSeperator(String strNumberOld) {

		// 1024 gibi nokta virgülsüz sayı gelirse

		String regExpCommaDot = ".*[\\.|,][0-9]+";

		if (!strNumberOld.matches(regExpCommaDot))
			return strNumberOld;

		String regExp = "(.*)[,\\.]([0-9]{0,6})[0-9]*";
		Pattern pattern = Pattern.compile(regExp); // "(\\d*)" + "\\.0$");
		Matcher matcher = pattern.matcher(strNumberOld);

		// FIXME mathcer 2 adet dönüş yaptı mı -- try catch
		if (matcher.find()) {

			String grup1 = matcher.group(1);
			String grup2 = matcher.group(2);

			// System.out.println("grup1:" + grup1);
			// . ve , tam sayı kısmından temizle
			grup1 = grup1.replaceAll("\\.", "");
			grup1 = grup1.replaceAll(",", "");

			if (grup1.length() == 0)
				grup1 = "0";
			if (grup2.length() == 0) {
				grup2 = "";
			} else {
				grup2 = "." + grup2;
			}

			// System.out.println("grup2:" + grup2);
			// System.out.println("Yeni:" + grup1 + grup2);

			return grup1 + grup2;
		}

		return null;

	}


	/**
	 * 12211,11 şeklinde ise true döner 12.211,11 şeklinde ise true döner
	 *
	 * @param text
	 * @return
	 */
	public static Boolean testNumberCommaFormat(String text) {
		// decimal comma format
		String regExp = ".+,[0-9]{1,2}";

		Boolean testresult = text.matches(regExp);
		// System.out.println("Text :" + text + "\tregexp:" + regexp + "\tMatch Result:"
		// + testresult);
		return testresult;

	}

	public static String clearCommaDotInNumber_DotFormat(String strDoubleValue) {

		String regExp = "(.+),([0-9]{1,2})";
		Pattern pattern = Pattern.compile(regExp); // "(\\d*)" + "\\.0$");
		Matcher matcher = pattern.matcher(strDoubleValue);

		if (matcher.find()) {
			String grup1 = matcher.group(1);
			String grup2 = matcher.group(2);

			System.out.println("grup1:" + grup1);

			grup1 = grup1.replaceAll("\\.", "");
			System.out.println("grup2:" + grup2);
			System.out.println("Yeni:" + grup1 + "." + grup2);

		}

		return null;

	}

	public static <E> Double sumValues_ofListData(List<E> listdata, Function<E, BigDecimal> myfunction) {

		BigDecimal sumvalue = BigDecimal.ZERO;

		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
			E myentity = (E) iterator.next();

			BigDecimal num = myfunction.apply(myentity);
			if (num != null)
				sumvalue = sumvalue.add(num);

		}

		return sumvalue.doubleValue();
	}

	public static <E> BigDecimal sumValuesBigDecimal(List<E> listdata, Function<E, BigDecimal> myfunction) {

		BigDecimal sumvalue = BigDecimal.ZERO;

		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
			E myentity = (E) iterator.next();

			BigDecimal num = myfunction.apply(myentity);
			if (num != null)
				sumvalue = sumvalue.add(num);

		}

		return sumvalue;
	}

	public static <E> Double sumValuesDouble(List<E> listdata, FunctionalCollectDoublevalue<E> myfunction) {

		Double sumvalue = 0.0D;

		if(listdata==null || listdata.size()==0) return sumvalue;

		try {
			for (E listdatum : listdata) {
				if (myfunction.getValue(listdatum) == null) {
					continue;
				}
				sumvalue += myfunction.getValue(listdatum);
			}
		}catch (Exception e){
//			Loghelper.get(FiNumberToText.class).debug(FiException.exceptiontostring(e));
			Loghelper.get(FiNumberToText.class).debug("java.util.NoSuchElementException Hata");
		}

		//önceki hesaplama
//		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
////			Loghelper.get(getClass()).debug("");
//
//			E myentity = (E) iterator.next();
//
//			if (myfunction.getValue(myentity) == null) {
//				continue;
//			}
//			sumvalue += myfunction.getValue(myentity);
//		}

		return sumvalue;
	}

	public static <E> Double sumdoublevaluesWithHalfUp(List<E> listdata, FunctionalCollectDoublevalue<E> myfunction, Integer scale) {

		Double sumvalue = 0.0D;
		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
			E myentity = (E) iterator.next();

			if (myfunction.getValue(myentity) == null) {
				continue;
			}
			sumvalue += new FiNumber(myfunction.getValue(myentity)).buildScaleHalfUp(scale).getValueAsDouble();
		}

		return sumvalue;
	}


	public static <E> Double avgDoubleValues(List<E> listdata, FunctionalCollectDoublevalue<E> myfunction) {

		Double sumvalue = 0.0D;

		int itemcount = 0;
		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
			E myentity = (E) iterator.next();

			if (myfunction.getValue(myentity) == null) {
				continue;
			}
			sumvalue += myfunction.getValue(myentity);
			itemcount++;
		}

		return sumvalue / itemcount;
	}

	public static <E> Float sumfloatvalues(List<E> listdata, FunctionalCollectFloatvalue<E> myfunction) {

		Float sumvalue = (float) 0.0;
		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {
			E myentity = (E) iterator.next();

			if (myfunction.getValue(myentity) == null) {
				continue;
			}

			sumvalue += myfunction.getValue(myentity);
		}

		return sumvalue;
	}

	public static <E> Double sumdoublevaluespredicateobjects(List<E> listdata, Predicate<E> mypredicate,
	                                                         FunctionalCollectDoublevalue<E> myfunction) {

		Double sumvalue = 0.0D;
		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {

			E myentity = (E) iterator.next();

			if (mypredicate.test(myentity)) {
				sumvalue += myfunction.getValue(myentity);
			}
		}

		return sumvalue;
	}

	public static <E> Double sumdoublevaluestablefilteredandpredicated(List<E> listdata, Predicate<E> mypredicate,
	                                                                   FunctionalCollectDoublevalue<E> myfunction, JTable table) {

		Double sumvalue = 0.0D;

		for (int i = 0; i < table.getRowCount(); i++) {
			E myentity = listdata.get(table.convertRowIndexToModel(i));

			if (mypredicate.test(myentity)) {
				sumvalue += myfunction.getValue(myentity);
			}

		}

		return sumvalue;
	}

}
