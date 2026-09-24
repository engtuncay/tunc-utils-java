package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FicList;

/**
 * abstract getRepoFkfAll eklendi (memory açısında daha iyi olması için abstract oldu, state yerine)
 */
public abstract class AbsRepoFkbV2 extends AbsRepoFkbJdbi {

  public AbsRepoFkbV2(String connProfile) {
    super(connProfile);
  }

  public AbsRepoFkbV2(Handle handleRepo) {
    super(handleRepo);
  }

  public AbsRepoFkbV2(Jdbi jdbi) {
    super(jdbi);
  }

  /**
   * metod olarak kalması daha performanslı, state olarak tutmak memory maliyeti yapar
   * @return
   */
  public abstract Fkf getRepoFkfAll();

  //public abstract Fkf getRepoFkfDto();

  public abstract FicList getFclDto();

  public Fdr fkInsert(Fkb fkbEntity) {
    Fdr fdrMain = new Fdr();

    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setFkfAll(getRepoFkfAll());

    Fdr fdrSorgu = FiQugenMs.insQueryV3(fiQuconf);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    FiQuery fiQuery = new FiQuery(fdrSorgu.getFdTxVal(), fkbEntity);
    fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * Sadece FicList'deki alanlara göre insert yapar (id dahil etmez)
   *
   * @param fkbEntity
   * @param fclInsert
   * @return
   */
  public Fdr fkInsert(Fkb fkbEntity, FicList fclInsert) {
    Fdr fdrMain = new Fdr();

    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setFkfAll(getRepoFkfAll());

    Fdr fdrSorgu = FiQugenMs.insQueryV3Selected(fiQuconf, fclInsert);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    FiQuery fiQuery = new FiQuery(fdrSorgu.getFdTxVal(), fkbEntity);
    fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }
  /**
   * Delete Entities By Where Params
   *
   * @param fkbEntity
   * @param fclWhere
   * @return
   */
  public Fdr fkDeleteV1(Fkb fkbEntity, FicList fclWhere) {
    Fdr fdrMain = new Fdr();

    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setFkfAll(getRepoFkfAll());
    fiQuconf.setFicListWhere(fclWhere);

    Fdr fdrSorgu = FiQugenMs.delQueryV2(fiQuconf);
    fdrMain.combineAnd(fdrSorgu);

    if (fdrMain.isFalseBoResult()) return fdrMain;

    FiQuery fiQuery = new FiQuery(fdrSorgu.getFdTxVal(), fkbEntity);
    fiQuery.logQueryAndParams();

    return jdInsertFiQuery(fiQuery);
  }

  /**
   * fkbList
   * @return
   */
  public Fdr fkSelDtoList(Fkb fkbParams, FicList ficListExtra) {
    FiQuconf fiQuconf = new FiQuconf();
    FicList fclDto = getFclDto();

    if(!FiCollection.isEmpty(ficListExtra)) {
      fclDto.addAll(ficListExtra);
    }

    fiQuconf.setFclQuery(fclDto);

    Fdr fdrSorgu = FiQugenMs.selQueryV2(fiQuconf);
    if (fdrSorgu.isFalseBoResult()) return fdrSorgu;

    FiQuery fiQuery = new FiQuery(fdrSorgu.getFdTxVal(), fkbParams);
    fiQuery.logQueryAndParams();

    return jdSelectFkbList(fiQuery);
  }

}