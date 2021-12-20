package ozpasyazilim.utils.gui.components;

import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import ozpasyazilim.utils.core.FiDate;

public class DSortableTablePane extends JScrollPane {

    public static int COLUMN_TYPE_STRING = 1;
    public static int COLUMN_TYPE_NUMBER = 2;
    public static int COLUMN_TYPE_DATE = 3;

    private boolean showEmptyLines = true;
    private boolean showGradientHeader = true;

    private Color gradientColorFirst = new Color(128, 128, 128);
    private Color gradientColorSecond = new Color(204, 204, 204);
    private int[] columnTypes;
    private int currentColumn = -1;

    // private Color gradientColorFirst = new Color(64,64,224);
    // private Color gradientColorSecond = new Color(192,192,255);

    TableSorter sorter;
    protected SortableTable table = new SortableTable(new DDefaultTableModel());

    public void setShowEmptyLines(boolean showEmptyLines) {
	this.showEmptyLines = showEmptyLines;
    }

    public boolean isShowEmptyLines() {
	return showEmptyLines;
    }

    public void setShowGradientHeader(boolean showGradientHeader) {
	this.showGradientHeader = showGradientHeader;
    }

    public boolean isShowGradientHeader() {
	return showGradientHeader;
    }

    public void setColumnTypes(int[] columnTypes) {
	this.columnTypes = columnTypes;
    }

    public int[] getColumnTypes() {
	return columnTypes;
    }

    /**
     * Used for events.
     */
    class EventHandler implements TableColumnModelListener, AdjustmentListener, ContainerListener {
	// TableColumnModelListener

	public void columnAdded(TableColumnModelEvent e) {
	    DSortableTablePane.this.repaint();
	}

	public void columnRemoved(TableColumnModelEvent e) {
	    DSortableTablePane.this.repaint();
	}

	public void columnMoved(TableColumnModelEvent e) {
	    DSortableTablePane.this.repaint();
	}

	public void columnMarginChanged(ChangeEvent e) {
	    DSortableTablePane.this.repaint();
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
	}
	// AdjustmentListener

	public void adjustmentValueChanged(AdjustmentEvent e) {
	    DSortableTablePane.this.repaint();
	}
	// ContainerListener

	public void componentAdded(ContainerEvent e) {
	    DSortableTablePane.this.viewPortElementChanged();
	}

	public void componentRemoved(ContainerEvent e) {
	    DSortableTablePane.this.viewPortElementChanged();
	}
    }

    DSortableTablePane.EventHandler eventhandler = new DSortableTablePane.EventHandler();

    /**
     * Default constructor.
     */
    public DSortableTablePane() {
	setBackground(Color.WHITE);
	getViewport().setOpaque(false);
	table.getTableHeader().setForeground(Color.WHITE);
	getViewport().addContainerListener(eventhandler);
	getHorizontalScrollBar().addAdjustmentListener(eventhandler);
	getVerticalScrollBar().addAdjustmentListener(eventhandler);
	this.getViewport().add(table);
    }

    public JTable getTable() {
	return table;
    }

    public int getSelectedRow() {
	if (sorter == null)
	    return -1;
	int select = table.getSelectedRow();
	if (select == -1)
	    return select;
	return sorter.modelIndex(select);
    }

    public void setColumnWidth(int column, int width) {
	table.setColumnWidth(column, width);
    }

    public void setModel(TableModel dataModel) {
	table.setModel(dataModel);
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
	if (table == null) {
	    super.paint(g);
	    return;
	}

	super.paintComponent(g);
	if (showEmptyLines)
	    drawExtendedGrids(g);
	JScrollBar verticalScrollBar = getVerticalScrollBar();
	if (verticalScrollBar.isVisible()) {
	    g.setColor(getViewport().getBackground());
	    g.fillRect(verticalScrollBar.getX(), 0, verticalScrollBar.getX() + verticalScrollBar.getWidth(),
		    getHeight());
	}

	super.paintChildren(g);
	super.paintBorder(g);
    }

    /**
     * Draws extended grids
     * 
     * @param g
     */
    protected void drawExtendedGrids(Graphics g) {
	TableColumnModel columnModel = table.getColumnModel();
	int sumWidth = 0;
	Color gridColor = table.getGridColor();
	int red = (0xFF * 2 + gridColor.getRed()) / 3;
	int green = (0xFF * 2 + gridColor.getGreen()) / 3;
	int blue = (0xFF * 2 + gridColor.getBlue()) / 3;
	Color extraGridColor = new Color(red, green, blue);
	g.setColor(extraGridColor);
	Border border = getBorder();
	int left;
	int top;
	if (border == null) {
	    left = this.getViewport().getViewPosition().x;
	    top = this.getViewport().getViewPosition().y;
	} else {
	    left = border.getBorderInsets(getViewport()).left - 1 - this.getViewport().getViewPosition().x;
	    top = border.getBorderInsets(getViewport()).top - 1 - this.getViewport().getViewPosition().y;
	}

	if (table.getShowVerticalLines()) {
	    for (int index = 0; index < columnModel.getColumnCount(); ++index) {
		TableColumn column = columnModel.getColumn(index);
		sumWidth += column.getWidth();
		g.drawLine(sumWidth + left, 0, sumWidth + left, getHeight());
		if (sumWidth + left > getWidth())
		    break;
	    }
	}
	int sumHeight = table.getTableHeader().getHeaderRect(0).height + table.getRowHeight(0);

	if (table.getShowHorizontalLines()) {
	    for (int index = 1; sumHeight + top < getHeight(); index++) {
		g.drawLine(0, sumHeight + top, getWidth(), sumHeight + top);
		sumHeight += table.getRowHeight(index);
	    }
	}
    }

    /**
     * Called when a component is added to viewport or removed from viewport
     * This function adds new handlers to new table and removes handlers from old
     * table
     */
    private void viewPortElementChanged() {
	SortableTable newtable = null;
	if (getViewport().getComponent(0) instanceof JTable) {
	    newtable = (SortableTable) getViewport().getComponent(0);
	    newtable.getColumnModel().addColumnModelListener(eventhandler);
	}
	if (this.table != null)
	    table.getColumnModel().removeColumnModelListener(eventhandler);

	this.table = newtable;
    }

    // JTable

    class SortableTable extends JTable {
	public SortableTable() {
	    try {
		jbInit();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	    Component comp = super.prepareRenderer(renderer, row, column);
	    return prepareTableRenderer(comp, renderer, row, column);
	}

	public SortableTable(TableModel dm) {
	    setModel(dm);

	    try {
		jbInit();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	private void jbInit() throws Exception {

	}

	public void setModel(TableModel dataModel) {
	    sorter = new TableSorter(dataModel);
	    super.setModel(sorter);
	    sorter.setTableHeader(this.getTableHeader());
	}

	public void setColumnWidth(int column, int width) {
	    this.getColumnModel().getColumn(column).setMinWidth(width);
	    this.getColumnModel().getColumn(column).setMaxWidth(width);
	    this.getColumnModel().getColumn(column).setPreferredWidth(width);
	}
    }
    // Sorter

    class TableSorter extends AbstractTableModel {
	protected TableModel tableModel;

	public final int DESCENDING = -1;
	public final int NOT_SORTED = 0;
	public final int ASCENDING = 1;
	private TableSorter.Directive EMPTY_DIRECTIVE = new TableSorter.Directive(-1, NOT_SORTED);

	public final TRComparator COMPARABLE_COMAPRATOR = new TRComparator();
	/*
	 * { public int compare(Object o1, Object o2) { return ((Comparable) o1).compareTo(o2); } };
	 */
	public final TRComparator LEXICAL_COMPARATOR = new TRComparator();
	/*
	 * {
	 * 
	 * public int compare(Object o1, Object o2) { return o1.toString().compareTo(o2.toString()); } };
	 */
	private TableSorter.Row[] viewToModel;
	private int[] modelToView;

	private JTableHeader tableHeader;
	private MouseListener mouseListener;
	private TableModelListener tableModelListener;
	private Map columnComparators = new HashMap();
	private List sortingColumns = new ArrayList();

	public TableSorter() {
	    this.mouseListener = new TableSorter.MouseHandler();
	    this.tableModelListener = new TableSorter.TableModelHandler();
	}

	public TableSorter(TableModel tableModel) {
	    this();
	    setTableModel(tableModel);
	}

	public void fireTableDataChanged() {
	    super.fireTableDataChanged();
	}

	public TableSorter(TableModel tableModel, JTableHeader tableHeader) {
	    this();
	    setTableHeader(tableHeader);
	    setTableModel(tableModel);
	}

	private void clearSortingState() {
	    viewToModel = null;
	    modelToView = null;
	}

	public TableModel getTableModel() {
	    return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
	    if (this.tableModel != null) {
		this.tableModel.removeTableModelListener(tableModelListener);
	    }

	    this.tableModel = tableModel;
	    if (this.tableModel != null) {
		this.tableModel.addTableModelListener(tableModelListener);
	    }

	    clearSortingState();
	    fireTableStructureChanged();
	}

	public JTableHeader getTableHeader() {
	    return tableHeader;
	}

	public void setTableHeader(JTableHeader tableHeader) {
	    if (this.tableHeader != null) {
		this.tableHeader.removeMouseListener(mouseListener);
		TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
		if (defaultRenderer instanceof SortableHeaderRenderer) {
		    this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
		}
	    }
	    this.tableHeader = tableHeader;
	    if (this.tableHeader != null) {
		this.tableHeader.addMouseListener(mouseListener);
		if (showGradientHeader)
		    this.tableHeader.setDefaultRenderer(new TableSorter.SortableHeaderRenderer());
		else
		    this.tableHeader.setDefaultRenderer(
			    new TableSorter.SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
	    }
	}

	public boolean isSorting() {
	    return sortingColumns.size() != 0;
	}

	private TableSorter.Directive getDirective(int column) {
	    for (int i = 0; i < sortingColumns.size(); i++) {
		TableSorter.Directive directive = (Directive) sortingColumns.get(i);
		if (directive.column == column) {
		    return directive;
		}
	    }
	    return EMPTY_DIRECTIVE;
	}

	public int getSortingStatus(int column) {
	    return getDirective(column).direction;
	}

	private void sortingStatusChanged() {
	    clearSortingState();
	    fireTableDataChanged();
	    if (tableHeader != null) {
		tableHeader.repaint();
	    }
	}

	public void setSortingStatus(int column, int status) {
	    TableSorter.Directive directive = getDirective(column);
	    if (directive != EMPTY_DIRECTIVE) {
		sortingColumns.remove(directive);
	    }
	    if (status != NOT_SORTED) {
		sortingColumns.add(new TableSorter.Directive(column, status));
	    }
	    sortingStatusChanged();
	}

	protected Icon getHeaderRendererIcon(int column, int size) {
	    TableSorter.Directive directive = getDirective(column);
	    if (directive == EMPTY_DIRECTIVE) {
		return null;
	    }
	    return new TableSorter.Arrow(directive.direction == DESCENDING, size, sortingColumns.indexOf(directive));
	}

	private void cancelSorting() {
	    sortingColumns.clear();
	    sortingStatusChanged();
	}

	public void setColumnComparator(Class type, Comparator comparator) {
	    if (comparator == null) {
		columnComparators.remove(type);
	    } else {
		columnComparators.put(type, comparator);
	    }
	}

	protected Comparator getComparator(int column) {
	    Class columnType = tableModel.getColumnClass(column);
	    Comparator comparator = (Comparator) columnComparators.get(columnType);
	    if (comparator != null) {
		return comparator;
	    }
	    if (Comparable.class.isAssignableFrom(columnType)) {
		return COMPARABLE_COMAPRATOR;
	    }
	    return LEXICAL_COMPARATOR;
	}

	private TableSorter.Row[] getViewToModel() {
	    if (viewToModel == null) {
		int tableModelRowCount = tableModel.getRowCount();
		viewToModel = new TableSorter.Row[tableModelRowCount];
		for (int row = 0; row < tableModelRowCount; row++) {
		    viewToModel[row] = new TableSorter.Row(row);
		}

		if (isSorting()) {
		    Arrays.sort(viewToModel);
		}
	    }
	    return viewToModel;
	}

	public int modelIndex(int viewIndex) {
	    return getViewToModel()[viewIndex].modelIndex;
	}

	private int[] getModelToView() {
	    if (modelToView == null) {
		int n = getViewToModel().length;
		modelToView = new int[n];
		for (int i = 0; i < n; i++) {
		    modelToView[modelIndex(i)] = i;
		}
	    }
	    return modelToView;
	}

	public int getRowCount() {
	    return (tableModel == null) ? 0 : tableModel.getRowCount();
	}

	public int getColumnCount() {
	    return (tableModel == null) ? 0 : tableModel.getColumnCount();
	}

	public String getColumnName(int column) {
	    return tableModel.getColumnName(column);
	}

	public Class getColumnClass(int column) {
	    return tableModel.getColumnClass(column);
	}

	public boolean isCellEditable(int row, int column) {
	    return tableModel.isCellEditable(modelIndex(row), column);
	}

	public Object getValueAt(int row, int column) {
	    return tableModel.getValueAt(modelIndex(row), column);
	}

	public void setValueAt(Object aValue, int row, int column) {
	    tableModel.setValueAt(aValue, modelIndex(row), column);
	}

	private class Row implements Comparable {
	    public int modelIndex;

	    public Row(int index) {
		this.modelIndex = index;
	    }

	    public int compareTo(Object o) {
		int row1 = modelIndex;

		int row2 = ((Row) o).modelIndex;

		for (Iterator it = sortingColumns.iterator(); it.hasNext();) {
		    TableSorter.Directive directive = (Directive) it.next();
		    int column = directive.column;
		    Object o1 = tableModel.getValueAt(row1, column);
		    Object o2 = tableModel.getValueAt(row2, column);
		    currentColumn = column;
		    int comparison = 0;
		    if (o1 == null && o2 == null) {
			comparison = 0;
		    } else if (o1 == null) {
			comparison = -1;
		    } else if (o2 == null) {
			comparison = 1;
		    } else {
			comparison = getComparator(column).compare(o1, o2);
		    }

		    if (comparison != 0) {
			return directive.direction == DESCENDING ? -comparison : comparison;
		    }
		}
		return 0;
	    }
	}

	private class TableModelHandler implements TableModelListener {
	    public void tableChanged(TableModelEvent e) {
		if (!isSorting()) {
		    clearSortingState();
		    fireTableChanged(e);
		    return;
		}

		if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
		    cancelSorting();
		    fireTableChanged(e);
		    return;
		}

		int column = e.getColumn();
		if (e.getFirstRow() == e.getLastRow() && column != TableModelEvent.ALL_COLUMNS
			&& getSortingStatus(column) == NOT_SORTED && modelToView != null) {
		    int viewIndex = getModelToView()[e.getFirstRow()];
		    fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex, viewIndex, column, e.getType()));
		    return;
		}

		clearSortingState();
		fireTableDataChanged();
		return;
	    }
	}

	private class MouseHandler extends MouseAdapter {
	    public void mouseClicked(MouseEvent e) {
		JTableHeader h = (JTableHeader) e.getSource();
		TableColumnModel columnModel = h.getColumnModel();
		int viewColumn = columnModel.getColumnIndexAtX(e.getX());
		int column = columnModel.getColumn(viewColumn).getModelIndex();
		if (column != -1) {
		    int status = getSortingStatus(column);
		    if (!e.isControlDown()) {
			cancelSorting();
		    }
		    status = status + (e.isShiftDown() ? -1 : 1);
		    status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
		    setSortingStatus(column, status);
		}
	    }
	}

	private class Arrow implements Icon {
	    private boolean descending;
	    private int size;
	    private int priority;

	    public Arrow(boolean descending, int size, int priority) {
		this.descending = descending;
		this.size = size;
		this.priority = priority;
	    }

	    public void paintIcon(Component c, Graphics g, int x, int y) {
		Color color = c == null ? Color.GRAY : c.getBackground();
		int dx = (int) (size / 2 * Math.pow(0.8, priority));
		int dy = descending ? dx : -dx;
		y = y + 5 * size / 6 + (descending ? -dy : 0);
		int shift = descending ? 1 : -1;
		g.translate(x, y);

		g.setColor(color.darker());
		g.drawLine(dx / 2, dy, 0, 0);
		g.drawLine(dx / 2, dy + shift, 0, shift);

		g.setColor(color.brighter());
		g.drawLine(dx / 2, dy, dx, 0);
		g.drawLine(dx / 2, dy + shift, dx, shift);

		if (descending) {
		    g.setColor(color.darker().darker());
		} else {
		    g.setColor(color.brighter().brighter());
		}
		g.drawLine(dx, 0, 0, 0);

		g.setColor(color);
		g.translate(-x, -y);
	    }

	    public int getIconWidth() {
		return size;
	    }

	    public int getIconHeight() {
		return size;
	    }
	}

	private class UIResourceTableCellRenderer extends DefaultTableCellRenderer implements UIResource {
	    private GradientPaint gradientPaint;

	    private int shadowSize = 1;

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		    boolean hasFocus, int row, int column) {
		if (table != null) {
		    JTableHeader header = table.getTableHeader();
		    if (header != null) {
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		    }
		}
		setMinimumSize(new Dimension(0, 18));
		setPreferredSize(new Dimension(0, 18));
		setText((value == null) ? "" : value.toString());
		setBorder(null);
		return this;
	    }

	    public void paint(Graphics g) {
		gradientPaint = new GradientPaint(0, 0, gradientColorFirst, 0, getHeight(), gradientColorSecond, false);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(gradientPaint);
		g2d.fillRect(0, 0, this.getWidth() - shadowSize - 1, getHeight());
		g2d.setColor(lightenColor(gradientColorFirst, -16));
		g2d.drawRect(0, 0, this.getWidth() - shadowSize - 1, getHeight() - 2);
		g2d.setColor(lightenColor(gradientColorFirst, +96));
		g2d.drawLine(1, 1, this.getWidth() - shadowSize - 2, 1);
		super.paint(g);
	    }

	    public Color lightenColor(Color c, int value) {
		int r = c.getRed() + value;
		int g = c.getGreen() + value;
		int b = c.getBlue() + value;
		if (r < 0)
		    r = 0;
		if (r > 255)
		    r = 255;
		if (g < 0)
		    g = 0;
		if (g > 255)
		    g = 255;
		if (b < 0)
		    b = 0;
		if (b > 255)
		    b = 255;
		return new Color(r, g, b);
	    }
	}

	private class SortableHeaderRenderer implements TableCellRenderer {
	    private TableCellRenderer tableCellRenderer;

	    public SortableHeaderRenderer() {
		DefaultTableCellRenderer label = new UIResourceTableCellRenderer();
		label.setHorizontalAlignment(JLabel.CENTER);
		this.tableCellRenderer = label;
	    }

	    public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
		this.tableCellRenderer = tableCellRenderer;
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		    boolean hasFocus, int row, int column) {
		Component c = tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
			column);
		if (c instanceof JLabel) {
		    JLabel l = (JLabel) c;
		    l.setHorizontalTextPosition(JLabel.LEFT);
		    int modelColumn = table.convertColumnIndexToModel(column);
		    l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
		}
		return c;
	    }
	}

	private class Directive {
	    private int column;
	    private int direction;

	    public Directive(int column, int direction) {
		this.column = column;
		this.direction = direction;
	    }
	}
    }

    class TRComparator implements Comparator {

	public TRComparator() {
	}

	private double compareString(String mainString, String anotherString) {
	    int len1 = mainString.length();
	    int len2 = anotherString.length();
	    int n = Math.min(len1, len2);
	    char v1[] = mainString.toCharArray();
	    char v2[] = anotherString.toCharArray();
	    int i = 0;
	    int j = 0;
	    double cv1 = 0;
	    double cv2 = 0;
	    if (i == j) {
		int k = i;
		int lim = n + i;
		while (k < lim) {
		    char c1 = v1[k];
		    char c2 = v2[k];
		    cv1 = getCharVal(c1);
		    cv2 = getCharVal(c2);
		    if (cv1 != cv2) {
			return cv1 - cv2;
		    }
		    k++;
		}
	    } else {
		while (n-- != 0) {
		    char c1 = v1[i++];
		    char c2 = v2[j++];
		    cv1 = getCharVal(c1);
		    cv2 = getCharVal(c2);
		    if (cv1 != cv2) {
			return cv1 - cv2;
		    }
		}
	    }
	    return len1 - len2;
	}

	private double getCharVal(char ch) {
	    switch (ch) {
	    case 'c':  // �    // bilinmeyen karakter uyarsı verdi. yerine c yazıldı.
		return (double) 'c' + .5;
	    // FIXME : commente alındı   
	    /*
	     * case '�': return (double) 'C' + .5; case '�': return (double) 'i' - .5; case '�': return (double)
	     * 'I' + .5; case '�': return (double) 'g' + .5; case '�': return (double) 'G' + .5; case '�':
	     * return (double) 'o' + .5; case '�': return (double) 'O' + .5; case '�': return (double) 'u' + .5;
	     * case '�': return (double) 'U' + .5; case '�': return (double) 's' + .5; case '�': return (double)
	     * 'S' + .5;
	     */
	    default:
		return (double) ch;
	    }
	}

	public int compare(Object mainObject, Object anotherObject) {
	    if (mainObject instanceof String) {
		if (columnTypes == null || columnTypes[currentColumn] == COLUMN_TYPE_STRING) {
		    double result = compareString((String) mainObject, (String) anotherObject);

		    if (result > 0)
			return 1;
		    else if (result < 0)
			return -1;
		    else
			return 0;
		} else if (columnTypes[currentColumn] == COLUMN_TYPE_NUMBER) {
		    if (mainObject == null || ((String) mainObject).trim().equals(""))
			return 1;
		    if (anotherObject == null || ((String) anotherObject).trim().equals(""))
			return -1;
		    return new Long((String) mainObject).compareTo(new Long((String) anotherObject));
		} else {
		    Date date1 = new Date();
		    Date date2 = new Date();
		    if (mainObject == null || ((String) mainObject).trim().equals(""))
			return 1;
		    if (anotherObject == null || ((String) anotherObject).trim().equals(""))
			return -1;
		    try {
			date1 = FiDate.getStringAsDate((String) mainObject);
			date2 = FiDate.getStringAsDate((String) anotherObject);
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		    return date1.compareTo(date2);
		}

	    } else if (mainObject instanceof Long) {
		return ((Long) mainObject).compareTo(((Long) anotherObject));
	    } else if (mainObject instanceof Integer) {
		return ((Integer) mainObject).compareTo(((Integer) anotherObject));
	    } else if (mainObject instanceof Double) {
		return ((Double) mainObject).compareTo(((Double) anotherObject));
	    } else if (mainObject instanceof Date) {
		return ((Date) mainObject).compareTo(((Date) anotherObject));
	    } else
		return -1;

	}
    }

    public class DDefaultTableModel extends AbstractTableModel {
	String[] columnNames = { "Col 1", "Col 2", "Col 3" };
	String[][] vals = { { "1", "2", "3" }, { "4", "5", "6" }, { "7", "8", "9" } };

	public Object getValueAt(int pRow, int pCol) {
	    return vals[pRow][pCol];
	}

	public int getRowCount() {
	    return vals.length;
	}

	public int getColumnCount() {
	    return columnNames.length;
	}

	public String getColumnName(int col) {
	    return columnNames[col];
	}

	public Class getColumnClass(int column) {
	    return String.class;
	}

	public boolean isCellEditable(int row, int column) {
	    return false;
	}
    }

    public Component prepareTableRenderer(Component comp, TableCellRenderer renderer, int row, int column) {
	return comp;
    }

}
