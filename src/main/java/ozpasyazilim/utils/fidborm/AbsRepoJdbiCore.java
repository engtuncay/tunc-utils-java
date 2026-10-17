package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.table.FicList;

/**
 * Repolarda genel olması istenen alanlar ve metodlar
 * <p>
 * Kullanan Sınıflar : AbsRepoGenMainJdbi, AbsRepoRawJdbi (not generic), AbsRepoFkbJdbi
 */
public class AbsRepoJdbiCore {

  protected String connProfile;
  protected Jdbi jdbi;
  // Optional (bilgi amaçlı)
  protected String databaseName;
  protected IFiTableMeta iFiTableMeta;

  /**
   *
   */
  protected Fkf repoFkfAll;

  protected FicList repoFclDto;

  // Getter and Setter
  public Jdbi getJdbi() {
    return jdbi;
  }

  public void setJdbi(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public String getConnProfile() {
    return connProfile;
  }

  public void setConnProfile(String connProfile) {
    this.connProfile = connProfile;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public Fkf getRepoFkfAll() {
    return repoFkfAll;
  }

  public void setRepoFkfAll(Fkf repoFkfAll) {
    this.repoFkfAll = repoFkfAll;
  }

  public FicList getRepoFclDto() {
    return repoFclDto;
  }

  public void setRepoFclDto(FicList repoFclDto) {
    this.repoFclDto = repoFclDto;
  }
}
