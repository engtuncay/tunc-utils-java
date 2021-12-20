package ozpasyazilim.utils.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class CompFilteredTable extends JPanel{
	private JTable tblFilter;
	private JTable tblData;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	
	public CompFilteredTable() {
		setLayout(new BorderLayout(0, 0));
		add(getScrollPane(), BorderLayout.CENTER);
	}

	
	public JTable getTblFilter() {
		if (tblFilter == null) {
			tblFilter = new FilteredTable(new TestTableModel2());
			tblFilter.setPreferredScrollableViewportSize(new Dimension(0, 0));
			tblFilter.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblFilter.setTableHeader (null);
			
		}
		return tblFilter;
	}
	
	public JTable getTblData() {
		if (tblData == null) {
			tblData = new FilteredTable(new TestTableModel());
			tblData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				
	        tblFilter.setColumnModel(tblData.getColumnModel());
		}	
		return tblData;
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(600, 400));
		
		CompFilteredTable compFilteredTable = new CompFilteredTable();
		
        frame.getContentPane().add(compFilteredTable);
        frame.pack();
        frame.setVisible(true);

	}
	
	private class TestTableModel extends DefaultTableModel {
	    @Override
	    public int getRowCount () {
	        return 10;
	    }
	    @Override
	    public int getColumnCount () {
	        return 10;
	    }
	    @Override
	    public String getColumnName (final int column) {
	        return "Col-"+column;
	    }

	    @Override
	    public Object getValueAt (final int row, final int column) {
	        return (row * column) + "";
	    }
	}
	
	private class TestTableModel2 extends DefaultTableModel {
	    @Override
	    public int getRowCount () {
	        return 1;
	    }
	    @Override
	    public int getColumnCount () {
	        return 10;
	    }
	    @Override
	    public String getColumnName (final int column) {
	        return "Col-"+column;
	    }

	    @Override
	    public Object getValueAt (final int row, final int column) {
	        return (row * column) + "";
	    }
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getPanel());
		}
		return scrollPane;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getScrollPane_2(), BorderLayout.NORTH);
			panel.add(getScrollPane_1());
		}
		return panel;
	}
	
	
	
	private JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_1.setViewportView(getTblData());
			Dimension d = tblData.getPreferredSize();
			scrollPane_1.setPreferredSize(new Dimension(d.width,tblData.getRowHeight()*tblData.getRowCount()+1));
			
		}
		return scrollPane_1;
	}
	private JScrollPane getScrollPane_2() {
		if (scrollPane_2 == null) {
			scrollPane_2 = new JScrollPane();
			scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			scrollPane_2.setViewportView(getTblFilter());
			Dimension d = tblFilter.getPreferredSize();
			scrollPane_2.setPreferredSize(new Dimension(d.width,tblFilter.getRowHeight()*tblFilter.getRowCount()+1));
			
		}
		return scrollPane_2;
	}
}

class FilteredTable extends JTable {
    
	FilteredTable (final TableModel model) {
        super (model);
    }

    private boolean isColumnMarginChangeInProgress=false;
    @Override
    public void columnMarginChanged(final ChangeEvent e) {
        if (isColumnMarginChangeInProgress) {
            return;
        }
        isColumnMarginChangeInProgress = true;

        if (isEditing()) {
            removeEditor();
        }
        TableColumn resizingColumn = null;
        if (tableHeader != null) {
            resizingColumn = tableHeader.getResizingColumn();
        }
        if (resizingColumn != null) {
            if (autoResizeMode == AUTO_RESIZE_OFF) {
                resizingColumn.setPreferredWidth(resizingColumn.getWidth());
            } else {    // this else block is missing in jdk1.4 as compared to 1.3
                doLayout();
            }
            repaint();
        } else {
            resizeAndRepaint();
        }
        isColumnMarginChangeInProgress = false;
    }
}

