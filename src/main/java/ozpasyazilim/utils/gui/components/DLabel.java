package ozpasyazilim.utils.gui.components;

import java.awt.Dimension;

import java.awt.Font;

import javax.swing.JLabel;

public class DLabel
  extends JLabel
{
  public DLabel()
  {
      try
      {
      jbInit();
      }
      catch(Exception e)
      {
      e.printStackTrace();
      }
  }

    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(109, 21));
        this.setFont(new Font("Tahoma", 0, 11));
    }

}
