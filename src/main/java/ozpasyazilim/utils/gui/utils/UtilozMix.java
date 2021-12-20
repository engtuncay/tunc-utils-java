package ozpasyazilim.utils.gui.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import ozpasyazilim.utils.gui.components.CellEditorCheckbox;
import ozpasyazilim.utils.gui.components.CellRendererBoolean;
import ozpasyazilim.utils.gui.components.CellRendererDate;
import ozpasyazilim.utils.gui.components.CellRendererDouble;
import ozpasyazilim.utils.gui.components.CellRendererFloat;
import ozpasyazilim.utils.gui.components.CellRendererIntegerAll;
import ozpasyazilim.utils.gui.components.CellRendererObject;
import ozpasyazilim.utils.gui.components.CellRendererStringFloat;
import ozpasyazilim.utils.gui.components.CellShortRenderer;
import ozpasyazilim.utils.gui.components.Columndef;
import ozpasyazilim.utils.gui.components.CustomTablemodelList2;
import ozpasyazilim.utils.gui.components.CustomTablemodelList4;
import ozpasyazilim.utils.gui.components.OzPanel;
import ozpasyazilim.utils.gui.components.ReturnObjectAbs;
import ozpasyazilim.utils.gui.components.TableCellrendererCheckBox;
import ozpasyazilim.utils.gui.components.TableeditorCheckbox;
import ozpasyazilim.utils.datacontainers.OzTableColumn;

public class UtilozMix {

	public static <E> void initColumnSizes(JTable table, CustomTablemodelList2<E> model) {

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;

		// header için long value degerleri
		Object[] longValues = model.getLongValues();

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		Integer colsize = table.getColumnCount();

		for (int i = 0; i < colsize; i++) {
			column = table.getColumnModel().getColumn(i);

			// table dan columnn modele ulaşılır. columnmodel den column
			// objesine(column attributes) ulaşılır.

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, longValues[i],
					false, false, 0, i);
			// componente ulaşıyor daha sonra comp.un pref.size erişiyor

			cellWidth = comp.getPreferredSize().width;

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
		}
	}

	public static <E> void adjustColumnSizesObjecttblmodel(JTable table, TableModel tableModel, Object[] longvalues) {

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;
		Object[] longValues = longvalues;// header için long value
		// degerleri
		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < longValues.length; i++) {
			column = table.getColumnModel().getColumn(i); // table dan columnn
			// modele ulaşılır.
			// columnmodel den
			// column
			// objesine(column
			// attributes)
			// ulaşılır.

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			comp = table.getDefaultRenderer(tableModel.getColumnClass(i)).getTableCellRendererComponent(table,
					longValues[i], false, false, 0, i); // componente
			// ulaşıyor
			// daha
			// sonra
			// comp.un
			// pref.size
			// erişiyor
			cellWidth = comp.getPreferredSize().width;

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
		}
	}

	public static <E> void adjustColumnSizesObjecttblmodel(JTable table, TableModel tableModel) {

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		Integer columncount = tableModel.getColumnCount();

		for (int i = 0; i < columncount; i++) {
			column = table.getColumnModel().getColumn(i); // table dan columnn
			// modele ulaşılır.
			// columnmodel den
			// column
			// objesine(column
			// attributes)
			// ulaşılır.

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			comp = table.getDefaultRenderer(tableModel.getColumnClass(i)).getTableCellRendererComponent(table,
					tableModel.getValueAt(0, i), false, false, 0, i); // componente
			// ulaşıyor daha
			// sonra comp.un
			// pref.size
			// erişiyor
			cellWidth = comp.getPreferredSize().width;

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
		}
	}

	// FIXME *** OZTABLE içine çekilecek
	public static <E> void adjustColumnSizeTablemodel(JTable table, Integer[] columnsize) {

		TableModel tableModel = table.getModel();

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < Math.min(table.getColumnCount(), columnsize.length); i++) {
			// table dan columnn modele ulaşılır. columnmodel den column
			// objesine(column attributes) ulaşılır.
			column = table.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			cellWidth = (columnsize[i] != null ? columnsize[i] : 0);

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
			// column.setPreferredWidth(cellWidth);
		}
	}

	public static void adjustColumnSizeTableGeneric(JTable table, Integer[] columnsize) {

		TableModel tableModel = table.getModel();

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < Math.min(table.getColumnCount(), columnsize.length); i++) {
			// table dan columnn modele ulaşılır. columnmodel den column
			// objesine(column attributes) ulaşılır.
			column = table.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			cellWidth = (columnsize[i] != null ? columnsize[i] : 0);

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
			// column.setPreferredWidth(cellWidth);
		}
	}

	public static PlainDocument plaindocumentwithslashes() {
		PlainDocument plaindocumentwithslash = new PlainDocument() {
			@Override
			public void insertString(int off, String str, AttributeSet attr) throws BadLocationException {
				if (off < 10) { // max size clause
					if (off == 1 || off == 6) { // insert the '/' occasionally
						str = str + "/";
					}
					super.insertString(off, str, attr);
				}
			}

		};

		return plaindocumentwithslash;

	}

	public static PlainDocument plaindocumentdouble() {

		PlainDocument plaindocumentwithslash = new PlainDocument() {

			public static final String FLOAT = "0123456789.,";

			protected String acceptedChars = FLOAT;

			protected boolean negativeAccepted = false;

			// public JTextFieldFilter() {
			// this(FLOAT);
			// }

			// public JTextFieldFilter(String acceptedchars) {
			// acceptedChars = acceptedchars;
			// }

			public void setNegativeAccepted(boolean negativeaccepted) {
				if (acceptedChars.equals(FLOAT)) {
					negativeAccepted = negativeaccepted;
					acceptedChars += "-";
				}
			}

			@Override
			public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

				if (str == null) {
					return;
				}

				// Logmain.info("offset:"+offset+" - String:"+
				// str);

				// str: klavyeden tek karakter gelir - fakat panodan fazla
				// sayıda gelebilir
				for (int i = 0; i < str.length(); i++) {
					if (acceptedChars.indexOf(str.valueOf(str.charAt(i))) == -1) {
						return;
					}
				}

				if (acceptedChars.equals(FLOAT) || (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
					if (str.indexOf(".") != -1) {
						if (getText(0, getLength()).indexOf(".") != -1) {
							return;
						}
					}
				}

				if (negativeAccepted && str.indexOf("-") != -1) {
					if (str.indexOf("-") != 0 || offset != 0) {
						return;
					}
				}

				super.insertString(offset, str, attr);


				// Locale locale = new Locale("tr", "TR");
				// DecimalFormatSymbols otherSymbols = new
				// DecimalFormatSymbols(locale);
				// otherSymbols.setDecimalSeparator('.');
				// otherSymbols.setGroupingSeparator(',');
				// DecimalFormat decimalpattern = new
				// DecimalFormat("#,###,##0.00", otherSymbols);
				// String fieldtext="";
				// try {
				// fieldtext = getText(0, getLength());
				// super.remove(0, getLength());
				// super.insertString(0,
				// decimalpattern.format(Double.parseDouble(fieldtext)), null);
				//
				// } catch (BadLocationException e) {

				// Logmain.info( "Hata :" +
				// UtilModel.exceptiontostring (e ));
				// }

			}

		};

		return plaindocumentwithslash;

	}

	public static String getStringcolumns(List<Columndef> listtbldevircoldef) {
		String columns = "";
		int i = 0;
		for (Iterator iterator = listtbldevircoldef.iterator(); iterator.hasNext();) {
			Columndef columndef = (Columndef) iterator.next();
			if (i != 0) {
				columns += ",";
			}
			i++;
			columns += columndef.getColumnname();
		}
		return columns;
	}

	public static Integer[] getintarrayofcolvalues(List<Columndef> listtbldevircoldef) {

		Integer[] columnsvalues = new Integer[listtbldevircoldef.size()];

		for (int i = 0; i < columnsvalues.length; i++) {
			columnsvalues[i] = listtbldevircoldef.get(i).getColumnsize() == null ? 50
					: listtbldevircoldef.get(i).getColumnsize();
		}

		return columnsvalues;
	}

	public static void bilgilendirkullaniciyi_notif_dialog(ReturnObjectAbs modelresult, OzPanel dpanel) {

		if (modelresult != null) {
			if (modelresult.getResult()) {
				if (modelresult.getMessageSpecial() == null) {
					modelresult.setMessageSpecial("");
				}
				dpanel.setviewNotification("*** İşlem Başarılı *** \n" + modelresult.getMessageSpecial());
			} else {
				if (modelresult.getMessageSpecial() == null) {
					modelresult.setMessageSpecial("");
				}
				dpanel.showMessageDialog("!!! Hata var !!! \n" + modelresult.getMessageSpecial());
			}
		}

	}

	public static void initColumnSizesMap(JTable table, TableModel model, Map<Integer, OzTableColumn> mapcol_order) {

		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellprefWidth = 0;
		int cellassignWidth = 0;

		// header için long value degerleri
		// Object[] longValues = model.getLongValues();

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		Integer colsize = table.getColumnCount();

		for (int i = 0; i < colsize; i++) {
			column = table.getColumnModel().getColumn(i);

			// table dan columnn modele ulaşılır. columnmodel den column
			// objesine(column attributes) ulaşılır.

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			// içerik olarak null atandı
			comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, null, false,
					false, 0, i);
			// componente ulaşıyor daha sonra comp.un pref.size erişiyor

			cellprefWidth = comp.getPreferredSize().width;

			if (mapcol_order.containsKey(i) && mapcol_order.get(i).getLength() != null) {
				cellassignWidth = mapcol_order.get(i).getLength();
			}

			// System.out.println("Initializing width of column " + i + ". " +
			// "headerWidth = "
			// + headerWidth + "; cellWidth = " + cellWidth);

			column.setPreferredWidth(Math.max(headerWidth, cellassignWidth));
		}
	}

	public static <E> List<E> getTableobjectsthatfiltered(CustomTablemodelList4<E> mytablemodel, JTable table) {

		List<E> listobjeler = new ArrayList<>();

		for (int i = 0; i < table.getRowCount(); i++) {
			listobjeler.add(mytablemodel.getListdata().get(table.convertRowIndexToModel(i)));
		}
		return listobjeler;

	}

	public static void setDefaulttablerenderers(JTable table) {

		CellRendererObject renderer = new CellRendererObject();
		CellRendererStringFloat numberstringRenderer = new CellRendererStringFloat();
		CellRendererFloat floatRenderer = new CellRendererFloat();
		CellRendererDouble numberRenderer = new CellRendererDouble();
		CellRendererIntegerAll integerRenderer = new CellRendererIntegerAll();
		CellShortRenderer shortRenderer = new CellShortRenderer();
		CellRendererBoolean cellrendererboolean = new CellRendererBoolean();

		CellRendererDate cellRendererDate = new CellRendererDate();
		TableCellrendererCheckBox cellrendererCheckBox = new TableCellrendererCheckBox();
		CellEditorCheckbox cellEditorCheckbox = new CellEditorCheckbox();
		JCheckBox mycheckBox = new JCheckBox();
		DefaultCellEditor defaultCellEditor = new DefaultCellEditor(mycheckBox);
		TableeditorCheckbox tableeditorCheckbox = new TableeditorCheckbox();

		// this.tblMusteriler.setDefaultRenderer(Object.class, renderer);
		// this.tblMusteriler.setDefaultRenderer(String.class, renderer);
		table.setDefaultRenderer(Double.class, numberRenderer);
		table.setDefaultRenderer(Float.class, numberRenderer);
		table.setDefaultRenderer(Integer.class, integerRenderer);
		table.setDefaultRenderer(Date.class, cellRendererDate);
		table.setDefaultRenderer(Boolean.class, cellrendererboolean);

		// table.setDefaultRenderer(Boolean.class, cellrendererCheckBox);
		table.setDefaultEditor(Boolean.class, tableeditorCheckbox);
		table.setDefaultRenderer(Short.class, shortRenderer);

		// Loghelper2.getInstance(UtilozMix.class).info("Default table renderers are
		// configured..");
	}

}
