 package ozpasyazilim.utils.gui.components;

 import java.awt.Color;
 import java.awt.Component;
 import javax.swing.JLabel;
 import javax.swing.JTable;
 import javax.swing.table.DefaultTableCellRenderer;

 public class CellRendererInteger
   extends DefaultTableCellRenderer
 {
   public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column)
   {
     JLabel cell = (JLabel)super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);


     if(obj!=null)cell.setText(((Integer)obj).toString());
     cell.setHorizontalAlignment(4);
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
     return cell;
   }
 }

