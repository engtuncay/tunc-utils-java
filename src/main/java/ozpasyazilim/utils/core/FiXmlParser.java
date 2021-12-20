package ozpasyazilim.utils.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.joox.JOOX;
import org.joox.Match;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.joox.JOOX.$;

public class FiXmlParser {

	public static FiXmlParser bui() {
		return new FiXmlParser();
	}

	public static void main(String[] args) {

		String path = "Y:\\TEST\\KENT\\FATURA_20181102_170740.XML";

		File file = new File("Y:\\TEST\\KENT\\FATURA_20181102_170740.XML");

		Match xmlRoot = openXmlFile(file);

		xmlRoot.find("BASLIK").forEach(elementBaslik -> {
			System.out.println($(elementBaslik).find("LNGBELGEKOD").text());

			System.out.println("Sayı Detay" + $(elementBaslik).find("DETAY").size());

//			$(elementBaslik).find("DETAY").forEach(elementDetay -> {
//				System.out.println("  "+$(elementDetay).find("LNGBELGEDETAYKOD").text());
//			});

			for (Element detay : $(elementBaslik).find("DETAY")) {
				System.out.println("  " + $(detay).find("LNGBELGEDETAYKOD").text());
			}

			System.out.println("");

		});

	}

	public <T> void parseXmlElement(File file, String elementName, List<IFiCol> listColumn, List<T> listData, Class<T> clazz) {

		if (listData == null) return;

		Match $xmldom = openXmlFile(file);

		$xmldom.find(elementName).forEach(
				xmlElement -> {
					T objectt = parseXmlElementToEntity(xmlElement, listColumn, clazz);
					if (objectt != null) listData.add(objectt);
				});

	}

	public List<String> parseXmlTagsElement(File file, String elementName) {

		//if (listData == null) return;

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		List<String> tagList = new ArrayList<>();

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

		Match first = $xmldom.find(elementName).first();
//		tagList.add(first.find("LNGKOD").text());

		first.children().forEach(xmlElement -> {
			//Loghelper.get(getClass()).debug(FiConsole.logMain($(xmlElement)));
			tagList.add($(xmlElement).tag());
		});

		//Loghelper.get(getClass()).debug(FiConsole.logStringList(tagList));

		return tagList;
	}

	public Boolean existTagWithValue(File file, String txElementName, String txChildElementName, String txValue) {

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

		BooleanProperty boFound = new SimpleBooleanProperty(false);
		for (Element xmlParentElement : $xmldom.find(txElementName)) {
			if ($(xmlParentElement).find(txChildElementName).text().trim().equals(txValue.trim())) {
				boFound.setValue(true);
				break;
			}
		}

		return boFound.getValue();
	}

	public static <EntPrmClazz> EntPrmClazz findEntity(File file, String txElementName, String txKeyElementName, String txKeyElementValue,Class<EntPrmClazz> clazz,List<FiCol> listEntCols) {

		//Loghelper.get(getClass()).debug("Element Name:"+elementName);
		//Loghelper.get(getClass()).debug("File Name:"+file.getName());

		// xml ana iskeleti
		Match $xmldom = openXmlFile(file);

//		BooleanProperty boFound = new SimpleBooleanProperty(false);

		for (Element xmlParentElement : $xmldom.find(txElementName)) {

			if ($(xmlParentElement).find(txKeyElementName).text().trim().equals(txKeyElementValue.trim())) {

				EntPrmClazz entPrmClazz = parseMatchToEntityWithChild($(xmlParentElement), listEntCols, clazz);
				return entPrmClazz;
//				boFound.setValue(true);
//				break;
			}
		}

		return null;
	}


	public static Match openXmlFile(File file) {

		Match $xmldom = null;

		try {
			$xmldom = $(file); // $xmldom match objesi
		} catch (SAXException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exceptiontostring(e));
		} catch (IOException e) {
			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exceptiontostring(e));
		}

		return $xmldom;
	}

	public static Match openXml(String txXml) {

//		Match $xmldom = null;
//
//		try {
//			$xmldom = JOOX.builder().parse(sr); // $(file); // $xmldom match objesi
//		} catch (SAXException e) {
//			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exceptiontostring(e));
//		} catch (IOException e) {
//			Loghelper.get(FiXmlParser.class).error("Hata :" + FiException.exceptiontostring(e));
//		}
//
//		return $xmldom;

		return null;
	}



	public <T> void parseXmlElementToList(Match xmldom, List<IFiCol> listColumn, List<T> listData, String elementName, Class<T> clazz) {

		xmldom.find(elementName).forEach(
				xmlElement -> {
					T objectt = parseXmlElementToEntity(xmlElement, listColumn, clazz);
					if (objectt != null) listData.add(objectt);
				});

	}

	public <T> T parseXmlElementToEntity(Element xmlElement, List<IFiCol> listColumn, Class<T> clazz) {

		T objectt = new FiReflection().generateObject(clazz);

		for (int colidx = 0; colidx < listColumn.size(); colidx++) {

			IFiCol ozTableCol = listColumn.get(colidx);

			String text = $(xmlElement).find(ozTableCol.getHeaderName()).text();
			//println(" CellValue:"+text);
			new FiReflection().setter(ozTableCol, objectt, text);

		}

		return objectt;
	}

	public static <EntClazz> EntClazz parseMatchToEntityWithChild(Match xmlMatch, List<FiCol> listColumn, Class<EntClazz> clazz) {

		EntClazz entity = FiReflection.generateObject(clazz);

		for (int colidx = 0; colidx < listColumn.size(); colidx++) {

			FiCol fiTableColParent = listColumn.get(colidx);

			if (fiTableColParent.getColTypeNotNull() == OzColType.XmlChildList) {

				List listChildren = parseMatchElementToList(xmlMatch, fiTableColParent.getHeaderName(), fiTableColParent.getListChildCol(), fiTableColParent.getChildClazz());

				FiReflection.setProperty(entity, fiTableColParent.getFieldName(), listChildren);

			} else if(fiTableColParent.getColTypeNotNull() == OzColType.XmlChild) {

				Match elementChild = xmlMatch.find(fiTableColParent.getHeaderName());

				Object listChildren = parseMatchToEntityWithChild(elementChild, fiTableColParent.getListChildCol(), fiTableColParent.getChildClazz());
				FiReflection.setProperty(entity, fiTableColParent.getFieldName(), listChildren);

			}else{

				Match elementChild = xmlMatch.find(fiTableColParent.getHeaderName());
				String text = elementChild.text();
				//println("CellValue:"+text);
				FiReflection.setter(fiTableColParent, entity, text);
			}


		}

		return entity;
	}


	public static <EntClazz> Fdr<List<EntClazz>> parseXmlFileToEntityListWithChild(File fileXml, String txSelectorTag, List<FiCol> listColumn, Class<EntClazz> clazz) {

		//Loghelper.get(FiXmlParser.class).debug("parseXmlToEntityListWithChild");

		Fdr<List<EntClazz>> fdrResult = new Fdr<>();
		Match xmlRoot = FiXmlParser.openXmlFile(fileXml);

		List<EntClazz> entClazzes = parseMatchElementToList(xmlRoot, txSelectorTag, listColumn, clazz);
		fdrResult.setValue(entClazzes);

		fdrResult.setBoResult(true);
		return fdrResult;

	}

	public static <EntClazz> List<EntClazz> parseMatchElementToList(Match xmlMatch, String txSelectorTag, List<FiCol> fiColList, Class<EntClazz> clazz) {

		List<EntClazz> dataList = new ArrayList<>();

		for (Element xmlElement : xmlMatch.find(txSelectorTag)) {
			EntClazz entity = FiXmlParser.parseMatchToEntityWithChild($(xmlElement), fiColList, clazz);
			dataList.add(entity);
		}

		return dataList;
	}

}

//String dbop = $(xmlElement).attr("DBOP");
//String faturatip = $(xmlElement).find("TYPE").text();

// Pano versiyonuna göre okunması gerekir
// chh.setMetaMikroEvrak(getMapPano6BytTurMap().get(chh.getTyPanoBytTur()));

//sorumluluk kodu eklendi
// uuid eklenecek

// Diger method tanımlandığından burada commente alındı
// chh.setCha_evrakno_seri(tblAktarimFirma.getTxEvrakSeri());
// chh.setCha_srmrkkodu(tblAktarimFirma.getTxSormerKod());

//			if (tblAktarimFirma.getAfrTxTxSablonKod().equals(new MetaAktarimSablon().Panoroma6)) {
//				chh.setMetaMikroEvrak(getMapPano6BytTurMap().get(chh.getTyPanoBytTur()));
//			}
//			if (tblAktarimFirma.getTxSablonKod().equals(new MetaAktarimSablon().Panoroma7)) {
//				chh.setMetaMikroEvrak(getMapPano6BytTurMap().get(chh.getTyPanoBytTur()));
//			}

// yoruman alındı 9/11
//			new ModMikroAktarimEntityBinder().bindToChhForStokFatForPano(chh,chh.getMetaMikroEvrak(),chh.getCha_belge_no(),chh.getCha_evrakno_sira(),chh.getCha_satici_kodu()
//					,chh.getCha_kod(),chh.getCha_tarihi(),chh.getCha_aratoplam(),chh.getCha_ft_iskonto1(),chh.getDmVergiTutar(),chh.getCha_meblag()
//					,chh.getCha_aciklama(),tblAktarimFirma,chh.getCha_vade(),null);

//			if (chh.getMetaMikroEvrak() == MetaMikroEvrakEm.SF1_SATISFAT) {
//
//				$(xmlElement).find("DETAY").forEach(element -> {
//
//					List<IFiTableCol> listColStok = getFaturaDetayColumns();
//
//					MkSTOK_HAREKETLERI sth = xmlParser.parseXmlElementToEntity(element, listColStok, MkSTOK_HAREKETLERI.class);
//
//					List<Double> listIsk = new ArrayList<>();
//					listIsk.add(sth.getSth_iskonto1());
//					listIsk.add(sth.getSth_iskonto2());
//					listIsk.add(sth.getSth_iskonto3());
//					listIsk.add(sth.getSth_iskonto4());
//					listIsk.add(sth.getSth_iskonto5());
//					listIsk.add(sth.getSth_iskonto6());
//
//					//Double[] objects = listIsk.toArray(new Double[listIsk.size()]);
//
////					new ModMikroAktarimEntityBinder().bindStokHareketForCariHar(chh,sth,tblAktarimFirma, sth.getSth_stok_kod(),sth.getBirimAdiXml(),sth.getDmKdvOran()
////							,sth.getSth_miktar(),null,listIsk
////							,sth.getSth_vergi(),sth.getSth_tutar(),sth.getSth_satirno(),null,null);
//
//
//					Double miktar = sth.getSth_miktar() * sth.getDmCarpan();
//					sth.setSth_miktar(miktar);
//					sth.setSth_miktar2(miktar);
//
//					chh.getListStokHareketler().add(sth);
//
//				});
//
//
//			}
//
//			// KDVYE göre chavergi1,2,3 gelecek
//			//list.add(OzTableCol.build("DBLKDVTUTARI", Mkfields.cha_vergi1.toString(), OzColType.Double));
//
//			//reftarihi 00 tanımlamış
//			//miktar alanı hizmetse alınır yoksa 0 yazılır
//// kdv oranına göre vergi mikro alanı kayıt edilecek
//
//			//list.add(OzTableCol.build("TXTMUSTERIKOD", Mkfields.cha_ciro_cari_kodu.toString(), OzColType.Integer));
//
//			//list.add(OzTableCol.build("TRHVADETARIHI", Mkfields.cha_tarihi.toString(), OzColType.Date));
//
//
//			listData.add(chh);
//
//		});

//		EntClazz objectt = FiReflection.generateObject(clazz);
//
//		for (int colidx = 0; colidx < listColumn.size(); colidx++) {
//
//			FiTableCol fiTableColParent = listColumn.get(colidx);
//
//			if(fiTableColParent.getColType()!=null && fiTableColParent.getColType()== OzColType.XmlChild){
//
//				Match childDetay = $(xmlElement).find(fiTableColParent.getFiHeader());
//
//				List<FiTableCol> listChild = fiTableColParent.getListChildCol();
//
//				Object objectDetay = FiReflection.generateObject(fiTableColParent.getChildClazz());
//
//				for (FiTableCol fiTableCol : listChild) {
//					String textDetay = $(childDetay).find(fiTableCol.getFiHeader()).text();
//					FiReflection.setter(fiTableCol, objectDetay, textDetay);
//				}
//
//				FiReflection.setProperty(objectt,fiTableColParent.getFieldName(),objectDetay);
//
//			}else{
//				String text = $(xmlElement).find(fiTableColParent.getFiHeader()).text();
//				//println(" CellValue:"+text);
//				FiReflection.setter(fiTableColParent, objectt, text);
//			}
//
//
//		}

//		return objectt;

//		}