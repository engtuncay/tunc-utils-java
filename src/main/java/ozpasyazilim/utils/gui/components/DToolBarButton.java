package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

  public class DToolBarButton extends JButton 
  {

    private String feature;

    public DToolBarButton()
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
      this.setSize(new Dimension(27, 27));
      this.addMouseListener(new java.awt.event.MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            this_mouseEntered(e);
          }

          public void mouseExited(MouseEvent e)
          {
            this_mouseExited(e);
          }

          public void mouseClicked(MouseEvent e)
          {
          }


        });
      setBorderPainted(false);
      setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
    }

    private void this_mouseEntered(MouseEvent e)
    {
      if ( isEnabled() )
      {
        setBorderPainted(true);
        setBackground(new Color(169,178,202));
      }
    }

    private void this_mouseExited(MouseEvent e)
    {
      if ( isEnabled() )
      {
        setBorderPainted(false);
        setBackground(new Color(212,208,200));
      }
    }

    public String getFeature()
    {
      return feature;
    }

    public void setFeature(String feature)
    {
      this.feature = feature;
    }

  }

