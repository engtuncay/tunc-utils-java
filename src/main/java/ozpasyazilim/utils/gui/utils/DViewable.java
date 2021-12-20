package ozpasyazilim.utils.gui.utils;

import java.util.Properties;

import javax.swing.JToolBar;

public interface DViewable
{
  public void view(Properties params);
  public JToolBar getContextToolbar();
  public String getScreenName();

}
