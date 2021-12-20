package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.table.TableCellRenderer;

public class DLastColumnNullTable extends DTable
{
  public DLastColumnNullTable()
  {
  }
  
  public void paintComponent(Graphics g)
  {
    int nullColl=getColumnCount()-1;
    super.paintComponent(g);
    g.setColor( Color.WHITE );
    int width = getColumnModel().getColumn(nullColl).getWidth() -1;
    int x=0;
    for (int i = 0; i < nullColl; i++)
    {
      x+=getColumnModel().getColumn(i).getWidth();
    }
    int y=0;
    for (int i = 0; i < getRowCount()+1; i++)
    {
     y = (getRowHeight(0)*i) - 1;
     g.drawLine(x, y, width+x, y);
    }
    g.drawLine(width+x, 0, width+x, y);
  }
  
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
  {
    Component comp=super.prepareRenderer( renderer,  row,  column);
    
    return comp;
  }
}
