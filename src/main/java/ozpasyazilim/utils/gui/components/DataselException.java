package ozpasyazilim.utils.gui.components;

public class DataselException extends Exception
{
    public static enum  MESSAGE 
    {
                        DEFAULT_EXCEPTION,
                        PERMISSION_DENIED,
                        READ_ACCESS_VIOLATION,
                        WRITE_ACCESS_VIOLATION,
                        USER_PASSWORD_DENIED
    };
                                                              
    
    public static final String[] MESSAGE_DETAIL =
    {    
                     /* DEFAULT_EXCEPTION      */   "Datasel Default Exception",
                     /* PERMISSION_DENIED      */   "Bu islemi gerceklestirmeye yetkiniz yok.",
                     /* READ_ACCESS_VIOLATION  */   "Bu veri/islem uzerinde okuma yetkiniz yok.",
                     /* WRITE_ACCESS_VIOLATION */   "Bu veri/islem uzerinde yazma yetkiniz yok.",
                     /* USER_PASSWORD_DENIED   */   "Kullanici adi ve/veya sifre hatali"
    };
                                         
    private MESSAGE reason = MESSAGE.DEFAULT_EXCEPTION;
    
    // -------------------------------------------------------------------------------------------
   
    public DataselException( MESSAGE message )
    {
        super ( MESSAGE_DETAIL[DataselException.MESSAGE.PERMISSION_DENIED.ordinal()] );
    }
    
    public static String getMessageDetail(MESSAGE message)
    {
      return MESSAGE_DETAIL[message.ordinal()];
    }
    public String getMessageDetail()
    {
      return MESSAGE_DETAIL[reason.ordinal()];
    }
    public MESSAGE getReason()
    {
      return this.reason;
    }
}



