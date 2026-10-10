package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;

public class RepoGenJdbi<EntClazz> extends AbsRepoGenJdbi<EntClazz> {

  public RepoGenJdbi(Jdbi jdbi, Class<EntClazz> clazz) {
    super(jdbi, clazz);
  }

}