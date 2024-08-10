package ozpasyazilim.utils.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ozpasyazilim.utils.datacontainers.DataTableOz;
import ozpasyazilim.utils.gui.fxcomponents.FxEditorFactory;
import ozpasyazilim.utils.table.FiCol;

public class FiHtmlParserJsoup {

	/**
	 * ilk satırı sütun olarak okur, sonrasi ise degerdir
	 * 
	 * @param tableid
	 * @param file
	 * @param charsetName
	 *            examples ISO-8859-9 , UTF-8
	 * @return
	 */
	public static DataTableOz parseHtmlTable(String tableid, File file, String charsetName) {

		DataTableOz dataTable = new DataTableOz();

		Document doc = null;
		try {
			doc = Jsoup.parse(file, charsetName);
		} catch (IOException e) {
			e.printStackTrace();
		}


		List<List<String>> listrows = new ArrayList<>(); // dataTable.getListrows();
		List<Map<String, String>> listmapColNametoVal = new ArrayList<>();
		// dataTable.getListMapColNametoVal();
		List<String> listcolnames = new ArrayList<>();
		// dataTable.getListcolname();

		Element content = doc.getElementById(tableid);

		if(content==null){
			return dataTable;
		}

		Elements raporrows = content.getElementsByTag("tr");

		Element raporRowColName = raporrows.get(0);
		Elements listCol = raporRowColName.getElementsByTag("td");

		for (Element tdcell : listCol) {
			String cellText = tdcell.text().trim();
			// eklenecek containerlar
			listcolnames.add(cellText);
			// System.out.print(tdcell.text() + " - ");
		}
		dataTable.setListcolname(listcolnames);
		dataTable.generateMapsColumn();

		// System.out.println("");

		for (int i = 1; i < raporrows.size(); i++) {
			Element trow = raporrows.get(i);
			Elements tds = trow.getElementsByTag("td");

			Map<String, String> maprows = new HashMap<>();
			List<String> listrowdata = new ArrayList<>();
			Integer colindex = 0;
			for (Element tdcell : tds) {
				String celldataText = tdcell.text().trim();

				listrowdata.add(celldataText);

				String colname = "col" + colindex.toString();
				if (dataTable.containsCol(colindex)) {
					colname = dataTable.getColName(colindex);
				}
				maprows.put(colname, celldataText);
				colindex++;
			}

			listrows.add(listrowdata);
			listmapColNametoVal.add(maprows);

		}

		dataTable.setListrows(listrows);
		dataTable.setListMapColNametoVal(listmapColNametoVal);

		// dataTable.generateMapColNametoVal();


		return dataTable;
	}

	public static <P> List<P> parseHtmlExcelPansisTable(File file, List<FiCol> fiTableColList, Class<P> aClass) {
		return parseHtmlTableBindList("dgrRapor",file, "ISO-8859-9", fiTableColList, aClass);
	}

	public static <P> List<P> parseHtmlTableBindListByIso(String tableid, File file, List<FiCol> fiTableColList, Class<P> aClass) {
		return parseHtmlTableBindList(tableid,file, "ISO-8859-9", fiTableColList, aClass);
	}

	/**
	 * ilk satırı sütun olarak okur, sonrasi ise degerdir
	 *
	 * @param tableid
	 * @param file
	 * @param charsetName
	 *            examples ISO-8859-9 , UTF-8
	 * @return
	 */
	public static <P> List<P> parseHtmlTableBindList(String tableid, File file, String charsetName, List<FiCol> fiTableColList, Class<P> aClass) {

		DataTableOz dataTableOz = parseHtmlTable(tableid, file, charsetName);

		List<Map<String, String>> listMapColNametoVal = dataTableOz.getListMapColNametoVal();

		List<P> list = new ArrayList<>();

		for (Map<String,String> mapRow: listMapColNametoVal) {

			P entity = null;
			try {
				entity = aClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			for (FiCol fiTableCol : fiTableColList) {

				try {

					Object cellvalue=null;

					String strCellvalue = mapRow.get(fiTableCol.getOfcTxHeader());

					if(strCellvalue==null)continue;

					// &nbsp; html den gelmişse temizlemek için
					strCellvalue = strCellvalue.replace("\u00a0", "");
					//String cleaned = s.replace("&nbsp;"," ");

					cellvalue = FxEditorFactory.convertStringValueToObjectByOzColType(fiTableCol.getColType(), strCellvalue);

//					if(fiTableCol.getColType()== OzColType.Double){
//						cellvalue = FiNumber.convertNumberStrtoDouble(strCellvalue);
//					}
//
//					if(fiTableCol.getColType()== OzColType.Date){
//						cellvalue = FiDate.strToDateGeneric(strCellvalue);
//					}
//
//					if(fiTableCol.getColType()== OzColType.Integer){
//						cellvalue = Integer.parseInt(strCellvalue);
//					}

					PropertyUtils.setProperty(entity, fiTableCol.getOfcTxFieldName(), cellvalue);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}


			}

			list.add(entity);

		}


		return list;

	}



}
