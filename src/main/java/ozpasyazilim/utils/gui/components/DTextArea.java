package ozpasyazilim.utils.gui.components;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DTextArea
  extends JTextArea
{
  public DTextArea()
  {
    super();
  }
  
  public DTextArea(int x,int y)
  {
    super(x,y);
  }
  class NumberPlainDocument extends PlainDocument 
  {
      private String myInsert(String num, int offs, String str)
      {
        if(num.length()==offs){
          return num.concat(str);
        }else
        {
          if(offs==0)
            return str+num.substring(0,num.length());
          else return num.substring(0,offs)+str+num.substring(offs,num.length());
        }
      }
      
      private String myRemove(int offs,int len, String num)
      {
        if(offs==0){
          if(len==num.length())
            return "";
          else return num.substring(offs+len+1);
        }else
        {
          String ret=num.substring(0,offs);
          if(offs+len ==num.length() )
            return ret;
          else return ret+num.substring(offs+len,num.length());
        }
      }
      
      
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
      {
        
        String num=super.getText(0,super.getLength());
          super.insertString(offs, str, a);
         
      }
      
      public void replace(int offs, int len, String text,AttributeSet attrs) throws BadLocationException 
      {
        String num=super.getText(0,super.getLength());
        super.replace(offs,len,text,attrs);
      }
      
      public void remove(int offs,int len) throws BadLocationException 
      {
        String num=super.getText(0,super.getLength());
        String rem=myRemove(offs,len,num);
        super.remove(offs,len);
      }
  }  

}
