package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXDatePicker;

import ozpasyazilim.utils.core.FiDate;

public class DTable extends JTable {
    public static int RIGHT = JLabel.RIGHT;
    public static int LEFT = JLabel.LEFT;
    public static int CENTER = JLabel.CENTER;

    private Map alignmentMap = new TreeMap();

    public DTable() {
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	Component comp = super.prepareRenderer(renderer, row, column);
	if (comp instanceof DNumberField) {
	    int alignment = JLabel.RIGHT;
	    if (alignmentMap.containsKey(column))
		alignment = Integer.parseInt(alignmentMap.get(column).toString());
	    ((JLabel) comp).setHorizontalAlignment(alignment);
	} else if (comp instanceof JXDatePicker) {
	    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
	    JLabel label = (JLabel) comp;

	    if (label != null) {
		String fortmatteddate = f.format(label.getText());
		label.setText(fortmatteddate);
	    }
	} else if (comp instanceof JLabel) {
	    try {
		// Logmain.info("gettext"+
		// ((JLabel)comp).getText());

		if (FiDate.isValidDate(((JLabel) comp).getText())) {

		    // Loghelper.getInstance(this.getClass()).info("tarih ok");
		    int alignment = JLabel.RIGHT;
		    ((JLabel) comp).setHorizontalAlignment(alignment);
		    SimpleDateFormat f = new SimpleDateFormat("dd.M.yy");
		    // ((JLabel) comp).setText("sss");
		    String formatteddate = f.format(((JLabel) comp).getText());
		    ((JLabel) comp).setText(formatteddate);

		}

		if (Integer.parseInt(((JLabel) comp).getText()) > 0) {
		    int alignment = JLabel.RIGHT;
		    if (alignmentMap.containsKey(column))
			alignment = Integer.parseInt(alignmentMap.get(column).toString());
		    ((JLabel) comp).setHorizontalAlignment(alignment);
		    ((JLabel) comp).setText(((JLabel) comp).getText() + " ");
		} else if (Double.parseDouble(((JLabel) comp).getText()) > 0) {
		    int alignment = JLabel.RIGHT;
		    if (alignmentMap.containsKey(column))
			alignment = Integer.parseInt(alignmentMap.get(column).toString());
		    ((JLabel) comp).setHorizontalAlignment(alignment);
		    ((JLabel) comp).setText(((JLabel) comp).getText() + " ");
		}
		if (((JLabel) comp).getText().contains(",") || ((JLabel) comp).getText().contains(".")) {
		    int alignment = JLabel.RIGHT;
		    if (alignmentMap.containsKey(column))
			alignment = Integer.parseInt(alignmentMap.get(column).toString());
		    ((JLabel) comp).setHorizontalAlignment(alignment);
		    ((JLabel) comp).setText(((JLabel) comp).getText() + " ");
		}
	    } catch (Exception ex) {
		int alignment = JLabel.LEFT;
		if (alignmentMap.containsKey(column))
		    alignment = Integer.parseInt(alignmentMap.get(column).toString());
		((JLabel) comp).setHorizontalAlignment(alignment);
		((JLabel) comp).setText(" " + ((JLabel) comp).getText());
	    }
	} else
	    return comp;
	return comp;
    }

    public void setAlignmentMap(Map alignmentMap) {
	this.alignmentMap = alignmentMap;
    }

    public Map getAlignmentMap() {
	return alignmentMap;
    }

    public void setColumnWidth(int col, int width) {
	getColumnModel().getColumn(col).setMinWidth(width);
	getColumnModel().getColumn(col).setMaxWidth(width);
	getColumnModel().getColumn(col).setPreferredWidth(width);
    }
}
