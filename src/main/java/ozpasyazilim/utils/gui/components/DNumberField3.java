package ozpasyazilim.utils.gui.components;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class DNumberField3 extends JFormattedTextField
{ 

  //this can only be a characters of '#' '.' '-' at the first index 
  private String npb=null;
  private int digitNum;
  private int decimalNum;
  private int maxLength;
  Locale locale= new Locale("tr","TR","");
  
  public DNumberField3(){
    super();
     NumberFormat type = NumberFormat.getNumberInstance(locale);
      
    setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter((NumberFormat)type)));

    //this.setDocument(new NumberPlainDocument());
    this.setHorizontalAlignment(JTextField.RIGHT);
  }
  
  
}



