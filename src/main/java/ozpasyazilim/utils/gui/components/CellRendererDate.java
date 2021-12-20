 package ozpasyazilim.utils.gui.components;

 import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

 public class CellRendererDate
   extends DefaultTableCellRenderer
 {
   public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column)
   {
     JLabel cell = (JLabel)super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);

     SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");

     /*
     if (isSelected)
     {
       cell.setBackground(Color.yellow);
       cell.setForeground(Color.black);
     }
     else if (row % 2 == 0)
     {
    	 cell.setBackground(Color.white);
       //cell.setBackground(Color.cyan);
     }
     else
     {
    	 cell.setBackground(Color.white);
       //cell.setBackground(Color.lightGray);
     }
     */
     
     if(obj==null)return cell;

     if( obj instanceof Date) {
         obj = f.format(obj);
     }

     cell.setText(obj.toString());
     cell.setHorizontalAlignment(4);

     return cell;
   }
 }

