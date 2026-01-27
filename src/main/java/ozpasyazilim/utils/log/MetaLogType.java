package ozpasyazilim.utils.log;

public enum MetaLogType {
  INFO, LOG, WARN, ERROR, ALERT
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