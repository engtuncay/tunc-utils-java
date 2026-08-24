package ozpasyazilim.utils.log;

/**
 * xxFE - Front End için
 *
 */
public enum MetaLogType {
  INFO, INFOBACK, LOG, WARN, WARNBACK, ERROR
  , ERRBACK
  , ALERT
  /**
   * Front-End'e uyarı verir - {@link ozpasyazilim.entegrefx.entMocs.MocErrorDialogV2} 'da modal olarak açar
   */
  , WARNFE
  /**
   * Ntn metodlar için
   */
  , UNDEFINED
  /**
   *
   */
  , ACTION
  /**
   * İşlem Aşamaları Hakkında Bilgi Verir
   */
  , STEP
  /**
   * Development - Arka Plan için Mesajlar (Debug türü)
   */
  , DEV
  , INFO_IMP
  , WARN_IMP
}


/*
    const EMERGENCY = 'emergency';
    const ALERT     = 'alert';
    const CRITICAL  = 'critical';
    const ERROR     = 'error';
    const WARNING   = 'warning';
    const NOTICE    = 'notice';
    const INFO      = 'info';
    const DEBUG     = 'debug';
*/