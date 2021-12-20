package ozpasyazilim.utils.gui.components;

import java.text.DecimalFormat;

import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class DFormattedTextField extends JFormattedTextField
{
  private DecimalFormat df;
  private NumberFormatter nf;
  private DefaultFormatterFactory factory;
  private String denomination="";
  private Locale local = new Locale("tr", "TR");
  private String decimalFormat = "#,###,###.00";
  
  public DFormattedTextField()
  {
    init();
  }
  
  public DFormattedTextField(String denomination)
  {
    this.denomination=denomination;
    init();
  }
  
  public DFormattedTextField(String decimalFormat, String denomination)
  {
    this.decimalFormat=decimalFormat;
    this.denomination=denomination;
    init();
  }
  
  private void init()
  {
    df = new DecimalFormat(decimalFormat+((denomination.equals(""))?"":" "+denomination));
    nf = new NumberFormatter(df);
    setLocale(local);
    factory = new DefaultFormatterFactory(new NumberFormatter(df),new NumberFormatter(df),nf);
    setFormatterFactory(factory);
    setHorizontalAlignment(SwingConstants.RIGHT);
    nf.setAllowsInvalid(false);
  }

  public void setDenomination(String denomination)
  {
    this.denomination = denomination;
  }

  public String getDenomination()
  {
    return denomination;
  }

  public void setDecimalFormat(String decimalFormat)
  {
    this.decimalFormat = decimalFormat;
  }

  public String getDecimalFormat()
  {
    return decimalFormat;
  }
  
  public static void main(String[] args)
  {
    JFrame frame=new JFrame();
    frame.getContentPane().setLayout(null);
    frame.setSize(200,80);
    DFormattedTextField ftf= new DFormattedTextField("%");
    ftf.setValue(new Long(0));
    System.out.println(ftf.getValue().getClass().getName());
    ftf.setBounds(10,10,180,20);
    frame.getContentPane().add(ftf,null);
    frame.setVisible(true);
  }
}
