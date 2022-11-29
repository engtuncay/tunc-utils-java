package ozpasyazilim.utils.core;

import org.apache.commons.beanutils.PropertyUtils;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportHtml2 {

	String report="";

	public <T> String reportBasicHtml(List<T> listdata, List<? extends IFiCol> listColumns, T footer) {

		Integer numberofcol = listColumns.size();

		//Loghelperr.getInstance(ReportHtml.class).info("col size:" + numberofcol);

		// başlık sütunları
		String tagTh = "<th style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #dedede;\">";

		// Text Hücreler
		String tagTdText = "<td style=\"border-width: 1px;padding: 8px;border-style: solid;"
				+ "border-color: #666666;background-color: #ffffff;\">";

		// Sayısal hücreler için
		String tagTdNum = "<td class=\"formattutar\" style=\"text-align: right;"
				+ "border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;"
				+ "background-color: #ffffff;\">";

		String tagTdClose = "</td>";

		String htmlreport = "";

		// table tanımı

		htmlreport += "<table " + "style=\"font-family: verdana,arial,sans-serif;font-size: 11px;"
				+ "color: #333333;border-width: 1px;border-color: #666666;border-collapse: collapse;\">\r\n";

		// tablo başlık satırı
		htmlreport += "<tr>\r\n";

		// sütunlar bastırılır

		for (int i = 0; i < listColumns.size(); i++) {
			htmlreport += tagTh + listColumns.get(i).getHeaderName() + "</th>";
		}

		// satırlar bastırılır

		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {

			T rowdata = (T) iterator.next();

			htmlreport += "<tr>\r\n";

			for (int i = 0; i < numberofcol; i++) {

				String result = generateColumnTags(listColumns, tagTdText, tagTdNum, tagTdClose, rowdata, i);
				if (result == null) continue;
				htmlreport += result;

			}

			htmlreport += "</tr>\r\n";

		}

		// toplam satırı var mı ?
		if (footer != null) {

			T footerdata = footer;

			htmlreport += "<tr>\r\n";

			for (int i = 0; i < numberofcol; i++) {

				String result = generateColumnTags(listColumns, tagTdText, tagTdNum, tagTdClose, footerdata, i);
				if (result == null) continue;
				htmlreport += result;

			}

			htmlreport += "</tr>\r\n";

		}

		htmlreport += "</table>\r\n" + "";

		return htmlreport;

	}

	public <T> ReportHtml2 reportBasicHtmlAutoFooter(List<T> listdata, List<IFiCol> listColumns, Class<T> clazz) {

		Integer numberofcol = listColumns.size();

		//Loghelperr.getInstance(ReportHtml.class).info("col size:" + numberofcol);

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

		for (int i = 0; i < listColumns.size(); i++) {
			htmlreport += htmlthtag + listColumns.get(i).getHeaderName() + "</th>";
		}

		// satırlar bastırılır

		for (Iterator iterator = listdata.iterator(); iterator.hasNext(); ) {

			T rowdata = (T) iterator.next();

			htmlreport += "<tr>\r\n";

			for (int i = 0; i < numberofcol; i++) {

				String result = generateColumnTags(listColumns, tag_td_string, tag_td_num, tag_td_end, rowdata, i);
				if (result == null) continue;
				htmlreport += result;

			}

			htmlreport += "</tr>\r\n";

		}

		T footer = prepFooter(listdata, listColumns, clazz);

		// toplam satırı var mı ?
		if (footer != null) {

			T footerdata = footer;

			htmlreport += "<tr>\r\n";

			for (int i = 0; i < numberofcol; i++) {

				String result = generateColumnTags(listColumns, tag_td_string, tag_td_num, tag_td_end, footerdata, i);
				if (result == null) continue;
				htmlreport += result;

			}

			htmlreport += "</tr>\r\n";

		}

		htmlreport += "</table>\r\n" + "";

		this.report+=htmlreport;

		return this;

	}

	private <T> T prepFooter(List<T> listdata, List<IFiCol> listColumns, Class<T> clazz) {

		T footer= new FiReflection().generateObject(clazz);

		for (IFiCol ozTableCol : listColumns ) {

			if (ozTableCol.getSummaryType() != null && ozTableCol.getSummaryType() == OzColSummaryType.HEADINGFORTOTAL) {
				new FiReflection().setter(ozTableCol, footer, "TOPLAM");
			}


			if(ozTableCol!=null && ozTableCol.getColType()== OzColType.Double){

				Class clazzType = new FiReflection().getPropertyType(footer,ozTableCol.getFieldName());

				//Loghelperr.getInstance(getClass()).debug(" Type"+ clazzType.getSimpleName());

				//if(clazzType.equals(Double.class)) Loghelperr.getInstance(getClass()).debug(" Double class"+ ozTableCol.getFieldName());

				if ( ozTableCol.getSummaryType()==OzColSummaryType.SUM && clazzType.equals(Double.class)) {

					//Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

					Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
						Object objectByField = new FiReflection().getPropertyy(ent, ozTableCol.getFieldName());
						if (objectByField == null) return 0d;
						return (Double) objectByField;
					});

					//Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

					new FiReflection().setter(footer, ozTableCol, sumDouble);

				}

				if ( ozTableCol.getSummaryType()==OzColSummaryType.AVG && clazzType.equals(Double.class)) {

					//Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

					Double sumDouble = FiNumberToText.avgDoubleValues(listdata, ent -> {
						Object objectByField = new FiReflection().getPropertyy(ent, ozTableCol.getFieldName());
						if (objectByField == null) return 0d;
						return (Double) objectByField;
					});

					//Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

					new FiReflection().setter(footer, ozTableCol, sumDouble);

				}





			}

			if ( ozTableCol.getSummaryType()!=null && ozTableCol.getSummaryType()==OzColSummaryType.CUSTOM ) {

				//Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

				if(ozTableCol.getSummaryCalculateFn()!=null){

					Object summaryvalue = ozTableCol.getSummaryCalculateFn().apply(listdata);

					//Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

					new FiReflection().setter(footer, ozTableCol, summaryvalue);


				}

			}
		}

		return footer;
	}

	private <T> String generateColumnTags(List<? extends IFiCol> listColumns, String tag_td_string, String tag_td_num, String tag_td_end, T rowdata, int colindex) {

		if (listColumns.get(colindex).getFieldName() == null) return null;

		String htmlreport = "";

		Object cellvalue = null;

		try {
			cellvalue = PropertyUtils.getProperty(rowdata, listColumns.get(colindex).getFieldName());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		OzColType colType = listColumns.get(colindex).getColType();

		if (cellvalue == null) return htmlreport = tag_td_string + "" + tag_td_end;

		if (cellvalue instanceof Double || cellvalue instanceof Float){
			cellvalue = FiString.formatNumberParagosterimi(cellvalue);
			colType= OzColType.Money;
		}

		if (cellvalue instanceof BigDecimal){
			BigDecimal value= (BigDecimal) cellvalue;
			cellvalue = FiString.formatNumberParagosterimi(value.doubleValue());
			colType= OzColType.Money;
		}

		htmlreport = tag_td_string + cellvalue.toString() + tag_td_end;

		if (listColumns.get(colindex).getMapStyle().containsKey(FiCol.ColStyle.alignment)) {

			String key = FiCol.ColStyle.alignment.toString();
			Map<String,String> mapStyle = listColumns.get(colindex).getMapStyle();
			String alignment = mapStyle.get(key);

			if (alignment.equalsIgnoreCase("LEFT")) htmlreport = tag_td_string + cellvalue.toString() + tag_td_end;
			if (alignment.equalsIgnoreCase("RIGHT")) htmlreport = tag_td_num + cellvalue.toString() + tag_td_end;

		}

		if (colType != null &&  ( colType==OzColType.Money || colType==OzColType.Number || colType==OzColType.Double || colType==OzColType.Integer ) ) {
			htmlreport = tag_td_num + cellvalue.toString() + tag_td_end;
		}


		return htmlreport;
	}



	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public ReportHtml2 h4Heading(String s) {

		this.report += "<h4>" + s + "</h4>";
		return this;
	}

	public ReportHtml2 h3Heading(String s) {

		this.report += "<h3>" + s + "</h3>";
		return this;
	}

	public ReportHtml2 h2Heading(String s) {

		this.report += "<h2>" + s + "</h2>";
		return this;
	}


}


