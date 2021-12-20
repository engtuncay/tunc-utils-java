package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.accessibility.Accessible;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;
import javax.swing.WindowConstants;

public class DDialog extends JDialog implements WindowConstants, Accessible, RootPaneContainer
{
  private DMessagePane messagePane;
  public DDialog() throws HeadlessException {
      super((Frame)null, false);
  }
  public DDialog(Frame owner) throws HeadlessException {
      super(owner, false);
  }
  public DDialog(Frame owner, boolean modal) throws HeadlessException {
      super(owner, null, modal);
  }
  public DDialog(Frame owner, String title) throws HeadlessException {
      super(owner, title, false);     
  }
  public DDialog(Dialog owner, boolean modal) throws HeadlessException {
  super(owner,true);
  }
  public DDialog(Frame owner, String title, boolean modal) throws HeadlessException {
      super(owner, title, false);     
  }
  /**
   * since every gui extends DPanel a unique messagePane
   * variable is to be used for user interaction
   */
  public DMessagePane getMessagePane()
  {
    if (messagePane==null)
      messagePane = new DMessagePane(JOptionPane.getFrameForComponent(this));
    return messagePane;
  }
  
  public final static DMessagePane getMessagePane( Component comp )
  {
    return new DMessagePane(JOptionPane.getFrameForComponent(comp));  
  }
}
