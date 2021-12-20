package ozpasyazilim.utils.gui.components;

import java.awt.event.ActionEvent;

import java.lang.reflect.Method;

import javax.swing.AbstractAction;


public class DAction
  extends AbstractAction
{
  Object caller;
  String methodName;
  String formName;
  String title;


  public DAction(Object caller, String title)
  {
    super(title);
    this.title = title;
    this.caller = caller;
  }


  public void actionPerformed(ActionEvent e)
  {
    System.out.println("action");
    try
    {
      Method method = 
        caller.getClass().getMethod(this.methodName, new Class[]
          { e.getClass(), String.class, String.class, String.class });
      String fname = (String) this.getValue("FORM_NAME");
      method.invoke(this.caller, new Object[]
          { e, formName, title, (String) this.getValue("AC_ICON") });
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void setCaller(Object caller)
  {
    this.caller = caller;
  }

  public Object getCaller()
  {
    return caller;
  }

  public void setFormName(String formName)
  {
    this.formName = formName;
  }

  public String getFormName()
  {
    return formName;
  }

  public void setMethodName(String methodName)
  {
    this.methodName = methodName;
  }

  public String getMethodName()
  {
    return methodName;
  }
}
