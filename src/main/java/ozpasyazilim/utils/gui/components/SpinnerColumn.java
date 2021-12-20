package ozpasyazilim.utils.gui.components;

import java.awt.*;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.border.*;
import javax.swing.table.*;

public class SpinnerColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private static final long serialVersionUID = 1L;
    private JSpinner editSpinner, renderSpinner;
    private JTable table;
    private String[] list;
    private Border originalBorder;

    public SpinnerColumn(JTable table, int column) {
        editSpinner = new JSpinner();
        renderSpinner = new JSpinner();
        originalBorder = editSpinner.getBorder();
        editSpinner.setBorder(new LineBorder(Color.BLUE));
        this.table = table;
        table.getColumnModel().getColumn(column).setCellEditor(this);
    }

    public SpinnerColumn(String[] list, JTable table, int column) {
        editSpinner = new JSpinner();
        editSpinner.setModel(new SpinnerListModel(list));
        renderSpinner = new JSpinner();
        originalBorder = editSpinner.getBorder();
        editSpinner.setBorder(new LineBorder(Color.BLUE));
        this.list = list;
        this.table = table;
        table.getColumnModel().getColumn(column).setCellEditor(this);
    }

    @Override
    public Object getCellEditorValue() {
        return editSpinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {
        if (list != null) {
            editSpinner.setValue(list[0]);
        } else {
            editSpinner.setValue(0);
        }
        if (value != null) {
            editSpinner.setValue(value);
        }
        return editSpinner;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderSpinner.setBorder(new LineBorder(Color.BLUE));
        } else {
            renderSpinner.setBorder(originalBorder);
        }// *** here's where we set the spinner's value
        if (value == null) {
            renderSpinner.setValue(0);
        } else {
            int intValue = ((Integer) value).intValue();
            renderSpinner.setValue(intValue);
        }
        return renderSpinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("SpinnerColumn");
                JPanel panel = new JPanel(new GridLayout(1, 1));
                JTable table = new JTable(5, 1);
                SpinnerColumn spinnerColumn = new SpinnerColumn(table, 0);
                table.setDefaultRenderer(Object.class, spinnerColumn);
                panel.add(table);
                frame.setContentPane(panel);
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}