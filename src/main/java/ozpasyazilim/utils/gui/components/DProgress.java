package ozpasyazilim.utils.gui.components;
  
import java.awt.Color;

  public interface DProgress 
  {
    public Color ERROR_FOREGROUND = new Color(0xFF0000);
    public Color ERROR_BACKROUND = new Color(0xFFFFB2);
    public Color NORMAL_FOREGROUND = Color.BLACK;
    public Color NORMAL_BACKROUND = null;
    public Color SUCCES_FOREGROUND = Color.BLACK;
    public Color SUCCES_BACKROUND = new Color(0xB3FFB2);
    
    public int MESSAGE_MODE_NORMAL = 1;
    public int MESSAGE_MODE_ERROR = 2;
    public int MESSAGE_MODE_SUCCES = 3;
    public void setProgressDialog(DProgressDialog dialog);
    public void preProgress();
    public void progress();
    public void postProgress();
  }
