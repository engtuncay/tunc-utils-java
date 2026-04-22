package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.configmisc.FiConnConfig;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.HashMap;
import java.util.Map;

public class FiJdbiPool {

  private static Map<String, Jdbi> mapKeyJdbi;

  private static Map<String, Jdbi> getMapKeyJdbiInit() {
    if (mapKeyJdbi == null) {
      mapKeyJdbi = new HashMap<>();
    }
    return mapKeyJdbi;
  }

  public static Fdr<Jdbi> getOrCreateJdbi(String txKey, FiConnConfig fiConnConfig) {
    Fdr<Jdbi> fdrMain = new Fdr<>();

    if (fiConnConfig == null) {
      fdrMain.setFdTxMessage("ConnConfig null");
      return fdrMain;
    }
    //String txKey = fiConnConfig.getName();
    if (txKey == null) {
      fdrMain.setFdTxMessage("Key null");
      return fdrMain;
    }
    Map<String, Jdbi> mapKeyJdbi = getMapKeyJdbiInit();
    if (mapKeyJdbi.containsKey(txKey)) {
      Jdbi jdbi = mapKeyJdbi.get(txKey);
      fdrMain.setValue(jdbi);
      fdrMain.setBoResult(true);
      return fdrMain;
    } else {
      return FiJdbiFactory.createJdbiAsFdr(fiConnConfig);
    }
  }



  private static Map<String, Jdbi> getMapKeyJdbi() {
    return mapKeyJdbi;
  }

  private static void setMapKeyJdbi(Map<String, Jdbi> mapKeyJdbi) {
    FiJdbiPool.mapKeyJdbi = mapKeyJdbi;
  }

}
