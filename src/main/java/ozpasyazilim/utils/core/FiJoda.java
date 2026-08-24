package ozpasyazilim.utils.core;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FiJoda {

  /**
   * 1) Offset yok, T yok, milisaniye yok
   * @param dtIndex
   * @return
   */
  public static String convertF1(DateTime dtIndex) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    String txDate = formatter.print(dtIndex);
    return txDate;
  }

  public static String convertYmd(DateTime dtIndex) {
    return dtIndex.toString("yyyyMMdd");
  }

  //DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

  //// 2) T var, offset yok
  //DateTimeFormatter f2 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
  //String v2 = f2.print(dt);
  //// -> 2026-08-21T18:03:46
  //
  //// 3) Offset var, milisaniye yok
  //DateTimeFormatter f3 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
  //String v3 = f3.print(dt);
  //// -> 2026-08-21T18:03:46+03:00
  //
  //// 4) Sadece tarih
  //DateTimeFormatter f4 = DateTimeFormat.forPattern("yyyy-MM-dd");
  //String v4 = f4.print(dt);
  //// -> 2026-08-21

}
