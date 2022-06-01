package ozpasyazilim.utils.core;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import ozpasyazilim.utils.gui.fxcomponents.FxTableModal;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColType;

public class FiHtmlReport {

	public static String ALIGNLEFT = "-1";
	public static String ALIGNRIGHT = "1";

	public static String KEYContent = "content";
	public static String KEYAlign = "align";

	public static <T> String reportBasicHtml(List<T> listrut, Map<Integer, String> mapCols,
	                                         BiFunction<T, Integer, Map<String, String>> funcReport3045, T footer) {

		Integer numberofcol = mapCols.size();

		//Loghelper.get(FiHtmlReport.class).info("col size:" + numberofcol);

		// başlık sütunları
		String htmlthtag = "<th style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #dedede;\">";

		String tag_td_string = "<td style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #ffffff;\">";

		String tag_td_num = "<td class=\"formattutar\" style=\"text-align: right;"
				+ "border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;"
				+ "background-color: #ffffff;\">";

		String tag_td_end = "</td>";

		String htmlreport = "";

		// table tanımı

		htmlreport += "<table " + "style=\"font-family: verdana,arial,sans-serif;font-size: 11px;"
				+ "color: #333333;border-width: 1px;border-color: #666666;border-collapse: collapse;\">\r\n";

		// tablo başlık satırı
		htmlreport += "<tr>\r\n";

		// sütunlar bastırılır

		for (int i = 1; i <= mapCols.size(); i++) {

			String colname = "";
			if (mapCols.containsKey(i)) {
				colname = mapCols.get(i);
			}
			htmlreport += htmlthtag + colname + "</th>";
		}

		// satırlar bastırılır

		for (Iterator iterator = listrut.iterator(); iterator.hasNext(); ) {

			T t = (T) iterator.next();

			htmlreport += "<tr>\r\n";

			// FIXME numara satırlarını yazdırmak için
			for (int i = 1; i <= numberofcol; i++) {
				Map<String, String> colmap = funcReport3045.apply(t, i);
				String content = "";
				if (colmap.containsKey(KEYContent))
					content = colmap.get(KEYContent);

				String align = "-1";
				if (colmap.containsKey(KEYAlign))
					align = colmap.get(KEYAlign);

				if (align.equals(FiHtmlReport.ALIGNLEFT))
					htmlreport += tag_td_string + content + tag_td_end;
				if (align.equals(FiHtmlReport.ALIGNRIGHT))
					htmlreport += tag_td_num + content + tag_td_end;

			}

			htmlreport += "</tr>\r\n";

		}

		// toplam satırı var mı ?
		if (footer != null) {

			T rowFooter = footer;

			htmlreport += "<tr>\r\n";

			for (int i = 1; i <= numberofcol; i++) {
				Map<String, String> colmap = funcReport3045.apply(rowFooter, i);
				String content = "";
				if (colmap.containsKey(KEYContent))
					content = colmap.get(KEYContent);

				String align = "-1";
				if (colmap.containsKey(KEYAlign))
					align = colmap.get(KEYAlign);

				if (align.equals("-1"))
					htmlreport += tag_td_string + content + tag_td_end;
				if (align.equals("1"))
					htmlreport += tag_td_num + content + tag_td_end;

			}

			htmlreport += "</tr>\r\n";

		}

		htmlreport += "</table>\r\n" + "";

		return htmlreport;

	}

	public static <T> String reportBasicHtmlByFiTableCol(List<T> listData, List<FiCol> colList, FiHtmlReportConfig fiHtmlReportConfig) {

		//Integer numberofcol = colList.size();
		//Loghelper.get(FiHtmlReport.class).info("col size:" + numberofcol);

		// başlık sütunları
		String tagTh = "<th style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #dedede;\">";

		String tagTdString = "<td style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #ffffff;\">";

		String tagTdNum = "<td class=\"formattutar\" style=\"text-align: right;"
				+ "border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;"
				+ "background-color: #ffffff;\">";

		String tagTdNumSum = "<td class=\"formattutar\" style=\"text-align: right;"
				+ "border-width: 1px;padding: 8px;border-style: solid;border-color: red;"
				+ "background-color: #ffffff;font-weight: bold;\">";

		String tagTdStringSum = "<td style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: red;background-color: #ffffff;font-weight: bold;\">";

		String tagTdEnd = "</td>";

		String htmlContent = "";

		// table tanımı

		htmlContent += "<table " + "style=\"font-family: verdana,arial,sans-serif;font-size: 11px;"
				+ "color: #333333;border-width: 1px;border-color: #666666;border-collapse: collapse;\">\r\n";

		// tablo başlık satırı
		htmlContent += "<tr>\r\n";

		// sütunlar bastırılır
		for (FiCol fiTableCol : colList) {
			htmlContent += tagTh + FiString.orEmpty(fiTableCol.getHeaderName()) + "</th>";
		}

		// satırlar bastırılır

		for (Iterator iterator = listData.iterator(); iterator.hasNext(); ) {

			T t = (T) iterator.next();

			if (t == null) continue;

			htmlContent += "<tr>\r\n";

			// FIXME numara satırlarını yazdırmak için
			for (FiCol fiTableCol : colList) {

				String fieldName = fiTableCol.getFieldName();

				if (FiString.isEmpty(fieldName)) continue;

				Object objCellValue = FiReflection.getProperty(t, fieldName);
				String txCellValue = FiString.ToStrOrEmpty(objCellValue);

				if (fiTableCol.getColType() == OzColType.Double || fiTableCol.getColType() == OzColType.Integer
						|| fiTableCol.getColType() == OzColType.BigDecimal) {

					if (objCellValue instanceof Double) {
						if (objCellValue == null) objCellValue = 0d;
						Double dbValue = (Double) objCellValue;
						txCellValue = FiNumber.formatlaNumbertoString(dbValue);
					}

					if (objCellValue instanceof BigDecimal) {
						if (objCellValue == null) objCellValue = BigDecimal.ZERO;
						Double dbValue = ((BigDecimal) objCellValue).doubleValue();
						txCellValue = FiNumber.formatlaNumbertoString(dbValue);
					}

					htmlContent += tagTdNum + txCellValue + tagTdEnd;
				} else {
					htmlContent += tagTdString + txCellValue + tagTdEnd;
				}

			}

			htmlContent += "</tr>\r\n";

		}


		// toplam satırı var mı ?
		if (FiBoolean.isTrue(fiHtmlReportConfig.getBoSummaryEnabled())) {

			htmlContent += "<tr>\r\n";
			for (FiCol fiTableCol : colList) {

				if (fiTableCol.getSummaryType() != null) {
					String sumValue = FiNumber.formatNumber(FxTableModal.calcSummaryValueFi(listData, fiTableCol, fiHtmlReportConfig));
					htmlContent += tagTdNumSum + sumValue + tagTdEnd;
				} else {
					htmlContent += tagTdStringSum + "" + tagTdEnd;
				}
			}
			htmlContent += "</tr>\r\n";
		}

		htmlContent += "</table>\r\n" + "";

		return htmlContent;

	}


}
