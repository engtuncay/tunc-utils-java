package ozpasyazilim.utils.gui.components;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DTextField extends JTextField 
{
    public class UppercaseDocumentFilter extends DocumentFilter {
        public void insertString(DocumentFilter.FilterBypass fb, int offset, 
                                 String text, 
                                 AttributeSet attr) throws BadLocationException {

            fb.insertString(offset, text.toUpperCase(), attr);
        }

        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, 
                            String text, 
                            AttributeSet attrs) throws BadLocationException {

            fb.replace(offset, length, text.toUpperCase(), attrs);
        }
    }
    private UppercaseDocumentFilter uppercaseDocmentFilter;
    private DocumentFilter oldDocumentFilter;
    public void setUppercaseOnly(boolean val){
        if (!(getDocument() instanceof AbstractDocument))
            return;
        AbstractDocument document = (AbstractDocument ) getDocument ();
        if (val){
            if (uppercaseDocmentFilter==null)
                uppercaseDocmentFilter = new UppercaseDocumentFilter();
            if (oldDocumentFilter==null)
                oldDocumentFilter=(( AbstractDocument ) getDocument ()).getDocumentFilter();
            document.setDocumentFilter ( uppercaseDocmentFilter ) ; 
        }else{
            if (oldDocumentFilter!=null)
                document.setDocumentFilter ( oldDocumentFilter ) ; 
        }
    }
  private boolean nullibility = false;
    public DTextField() 
    {
      super();
    }
    /**
     * This method controls nullibility. If textfield.gettext == null; warn coder 
     */
  public DTextField(boolean nullibility) 
  {
    super();
    this.nullibility = nullibility;
  }
    
    public DTextField(int size) {
      super(size);
    }
  public String getText() 
  {
      if(nullibility)
      {
          if(super.getText().equals(""))
          {
            JOptionPane.showMessageDialog(this, "Gerekli alanlar� bo� ge�emezsiniz!!!");
            return null;
          }
        return super.getText();
      }
      else return super.getText();
  }
}
