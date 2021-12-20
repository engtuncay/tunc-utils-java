package ozpasyazilim.utils.gui.components;

import javax.swing.JFrame;

public class DFrame
  extends JFrame
{

  public DFrame()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public DFrame(String title)
  {
    super(title);
  }

  private void jbInit()
    throws Exception
  {
  }
}
