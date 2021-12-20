package ozpasyazilim.utils.gui.components;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class AddDeleteTableModel extends AbstractTableModel
{
  private Object[] columns;
  private ArrayList dataList;
  
  
  public AddDeleteTableModel(Object[] columns,ArrayList dataList)
  {
    this.columns=columns;
    this.dataList=dataList;
  }

  public int getColumnCount()
  {
    return columns.length;
  }
  
  public int getRowCount()
  {
    return dataList.size();
  }
  
  public Object getValueAt(int x,int y)
  {
    return dataList.get(x);
  }
}
