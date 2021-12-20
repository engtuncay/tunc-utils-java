package ozpasyazilim.utils.gui.components;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class MyuTable extends JTable {
    
	MyuTable (final TableModel model) {
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