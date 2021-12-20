package ozpasyazilim.utils.gui.components;
  
  public abstract class DAbstractProgress implements DProgress
  {
    private DProgressDialog progressDialog;  
    
    public DAbstractProgress()
    {
    }
    public void preProgress()
    {    
    }  
    public void postProgress()
    { 
    }
    public void setProgressDialog(DProgressDialog progressDialog)
    {
      this.progressDialog = progressDialog;
    }  
    public void setErrorMessage(String message)
    {    
      progressDialog.setErrorMessage(message);
    }
    public void setSuccessMessage(String message)
    {    
      progressDialog.setSuccessMessage(message);
    }  
    public void setCloseOnEndOfProgress(boolean b)
    {
      progressDialog.setCloseOnEndOfProgress(b);
    }
    public void setMessage(String message)
    {
      progressDialog.setMessage(message);
    }
      public void setProgreesBarIndeterminate(boolean bool)
    {
      progressDialog.setProgreesBarIndeterminate(bool);
    }
    public void setProgressBarValue(int value)
    {
      progressDialog.setProgressBarValue(value);
    }
    public void setMaxProgressBarValue(int value)
    {
      progressDialog.setMaxProgressBarValue(value);
    }
    public void setMinProgressBarValue(int value)
    {
      progressDialog.setMinProgressBarValue(value);
    }
  }
