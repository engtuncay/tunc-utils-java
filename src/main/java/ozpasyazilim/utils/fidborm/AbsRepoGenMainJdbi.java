package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.jdbi.FiKeyBeanMapper;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ana Sorgu Methodları bu sınıfa taşınacak
 *
 * @param <EntClazz>
 */
public class AbsRepoGenMainJdbi<EntClazz> extends AbsRepoJdbiCore {

    // Taslak olduğunu belirtmek için deprecated anno kullanılabilir. Depraceted For Draft

    protected Class<EntClazz> entityClass;
    protected Handle handleRepo;

    // Getter and Setter

    public Class<EntClazz> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<EntClazz> entityClass) {
        this.entityClass = entityClass;
    }

    public Handle getHandleRepo() {
        return handleRepo;
    }

    public void setHandleRepo(Handle handleRepo) {
        this.handleRepo = handleRepo;
    }

    public void setAutoClass() {
        if (getEntityClass() == null) {
            this.entityClass = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }

    }

    // Sorgu Metodları

    /**
     * Fdr Value (Not Null) (Default ArrayList)
     *
     * @param sqlQuery
     * @param mapBind
     * @return Fdr<List < EntClazz>>
     */
    public Fdr<List<EntClazz>> jdSelectListBindMapMainNtn(String sqlQuery, Map<String, Object> mapBind) {

        if (entityClass == null) setAutoClass();

        Fdr<List<EntClazz>> fdrMain = new Fdr<>();

        try {
            List<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdrMain.setBoResultAndValue(true, result, 1);

        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem. Hata (Exception):\n" + FiException.exTosMain(ex));
            fdrMain.setBoResult(false, ex);
        }

        // Ntn (not null dönüş)
        if (fdrMain.getValue() == null) fdrMain.setValue(new ArrayList<>());

        return fdrMain;
    }

    public <PrmEnt> Fdr<List<PrmEnt>> jdcSelectList(FiQuery fiQuery, Class<PrmEnt> clazz) {
        return jdcSelectListBindMapMainNtn(fiQuery.getTxQuery(), fiQuery.getMapParams(), clazz);
    }

    public <PrmEnt> Fdr<List<PrmEnt>> jdcSelectListBindMapMainNtn(String sqlQuery, Map<String, Object> mapBind, Class<PrmEnt> clazz) {

        Fdr<List<PrmEnt>> fdrMain = new Fdr<>();

        try {
            List<PrmEnt> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(clazz)
                        .list();
            });
            fdrMain.setBoResultAndValue(true, result, 1);

        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem. Hata (Exception):\n" + FiException.exTosMain(ex));
            fdrMain.setBoResult(false, ex);
        }

        // Ntn (not null dönüş)
        if (fdrMain.getValue() == null) fdrMain.setValue(new ArrayList<>());

        return fdrMain;
    }

    public List<EntClazz> jdSelectListBindMapRaw(String sqlQuery, Map<String, Object> mapBind) {

        Jdbi jdbi = getJdbi();
        List<EntClazz> result = null;

        try {
            result = jdbi.withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(getEntityClass())
                        .list();
            });

        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public Fdr<List<EntClazz>> jdSelectListBindObjectMain(String sqlQuery, Object entClazz) {

        if (entityClass == null) setAutoClass();

        //String sqlNew = convertMapAndSqlMultiParam(sqlQuery, mapBind);

        Fdr<List<EntClazz>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindBean(entClazz)
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setBoResult(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }
        return fdr;

    }

    public Fdr<List<EntClazz>> jdSelectListBindObjectMain(String sqlQuery, Object entClazz, FiKeyBean mapParams) {

        if (entityClass == null) setAutoClass();

        //String sqlNew = convertMapAndSqlMultiParam(sqlQuery, mapBind);

        Fdr<List<EntClazz>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapParams)
                        .bindBean(entClazz)
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setBoResult(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }
        return fdr;

    }

    public List<EntClazz> jdSelectListBindMapRawWithDeAct(String sqlQuery, Map<String, Object> mapBind) {

        Jdbi jdbi = getJdbi();
        //Loghelperr.getInstance(getClass()).debug(fimSqlAt3WithDeActivation(sqlQuery));

        List<EntClazz> result = null;
        try {
            result = jdbi.withHandle(handle -> {
                return handle.createQuery(Fiqt.fimSqlQueryWithDeActType1(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(getEntityClass())
                        .list();
            });
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
        }

        return result;
    }

    public Fdr<List<EntClazz>> jdSelectListBindMapMainWithDeAct(String sqlQuery, Map<String, Object> mapBind) {

        //Loghelper.getInstance(getClass()).debug(sqlQuery);

        Fdr<List<EntClazz>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<EntClazz> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.fimSqlQueryWithDeActType1(sqlQuery))
                        .bindMap(mapBind)
                        .mapToBean(getEntityClass())
                        .list();
            });
            fdr.setBoResultAndValue(true, result);
        } catch (Exception ex) {
            Loghelper.errorLog(getClass(), "Query Problem");
            Loghelper.errorException(getClass(), ex);
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<List<FiKeyBean>> jdSelectFkbListBindMapMain(String sqlQuery, Map<String, Object> mapBind) {

        Fdr<List<FiKeyBean>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<FiKeyBean> result = getJdbi().withHandle(handle -> {
                return handle.createQuery(Fiqt.stoj(sqlQuery))
                        .bindMap(mapBind)
                        .map(new FiKeyBeanMapper(false))
                        .list();
            });
            fdr.setBoResultAndValue(true, result, 1);

        } catch (Exception ex) {
            Loghelper.get(getClass()).error("Query Problem");
            Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exTosMain(ex));
            fdr.setBoResult(false, ex);
        }

        return fdr;
    }

    public Fdr<List<String>> jdSelectListStringMain(String sql, FiKeyBean fiKeyBean) {
        Fdr<List<String>> fdr = new Fdr<>();
        fdr.setValue(new ArrayList<>());

        try {
            List<String> result = getJdbi().withHandle(handle -> {
                return handle.select(Fiqt.stoj(sql))
                        .bindMap(fiKeyBean)
                        .mapTo(String.class)
                        .collect(Collectors.toList());
            });
            fdr.setBoResult(true);
            fdr.setValue(result);
        } catch (Exception ex) {
            Loghelper.get(getClass()).error(FiException.exToLog(ex));
            fdr.setBoResult(false);
        }
        return fdr;
    }


}
