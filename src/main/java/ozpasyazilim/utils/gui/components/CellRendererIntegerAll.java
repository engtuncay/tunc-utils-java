 package ozpasyazilim.utils.gui.components;

 import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

 public class CellRendererIntegerAll
   extends DefaultTableCellRenderer
 {
   public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column)
   {
     JLabel cell = (JLabel)super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);

     if(obj!=null)cell.setText(((Integer)obj).toString());
     cell.setHorizontalAlignment(4);

     return cell;
   }
 }

