package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.jdbi.FiKeyBeanMapper;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.*;

public abstract class AbsFkbRepoJdbi extends RepoGeneralJdbi implements IRepoJdbi {

	protected Handle handleRepo;

	public AbsFkbRepoJdbi() {

	}

	public AbsFkbRepoJdbi(String connProfile) {
		this.connProfile = connProfile;
	}

	public void setAutoClass() {

	}

	public AbsFkbRepoJdbi(Jdbi jdbi) {
		this.jdbi = jdbi;
	}

	public Handle getHandleRepo() {
		return handleRepo;
	}

	public void setHandleRepo(Handle handleRepo) {
		this.handleRepo = handleRepo;
	}

	public AbsFkbRepoJdbi(Handle handleRepo) {
		setHandleRepo(handleRepo);
	}

	// Sorgu Metodları
	public Fdr<List<FiKeyBean>> jdSelectFkbListBindMapMain(String sqlQuery, Map<String, Object> mapBind) {

		Fdr<List<FiKeyBean>> fdr = new Fdr<>();
		fdr.setValue(new ArrayList<>());

		try {
			List<FiKeyBean> result = getJdbi().withHandle(handle -> {
				return handle.createQuery(FiQueryTools.stoj(sqlQuery))
						.bindMap(mapBind)
						.map(new FiKeyBeanMapper())
						.list();
			});
			fdr.setBoResultAndValue(true, result, 1);

		} catch (Exception ex) {
			Loghelper.get(getClass()).error("Query Problem");
			Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exceptionToStrMain(ex));
			fdr.setBoResult(false, ex);
		}

		return fdr;
	}

	/**
	 * Value null dönerse, -1 olarak yorumlar
	 *
	 * @param sql
	 * @param fiMapParams
	 * @return
	 */
	public Fdr<Integer> jdSelectSingleIntOrMinus1(String sql, FiKeyBean fiMapParams) {
		Fdr<Integer> fdrSql = jdSelectSingleCustomEntityBindMap(sql, fiMapParams, Integer.class);
		if (fdrSql.getValue() == null) {
			fdrSql.setValue(-1);
		}
		return fdrSql;
	}

	public Fdr<FiKeyBean> jdSelectFkbSingleBindMapMain(String sqlQuery, Map<String, Object> mapBind) {

		Fdr<FiKeyBean> fdr = new Fdr<>();
		fdr.setValue(new FiKeyBean());

		try {
			Optional<FiKeyBean> result = getJdbi().withHandle(handle -> {
				return handle.createQuery(FiQueryTools.stoj(sqlQuery))
						.bindMap(mapBind)
						.map(new FiKeyBeanMapper())
						.findOne();
			});

			if (result.isPresent()) fdr.setValue(result.get());
			fdr.setBoResultAndRowsAff(true, 1);

		} catch (Exception ex) {
			Loghelper.get(getClass()).error("Query Problem");
			Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exceptionToStrMain(ex));
			fdr.setBoResult(false, ex);
		}

		return fdr;
	}

	/**
	 * Sorgu çalışır ve deger cekemezse degeri null olur
	 * <p>
	 * Sorguda hata olursa result false olur, deger yine null olur
	 *
	 * @param sql
	 * @param fiKeyBean
	 * @return
	 */
	public Fdr<Integer> jdSelectSingleInt(String sql, FiKeyBean fiKeyBean) {
		return jdSelectSingleCustomEntityBindMap(sql, fiKeyBean, Integer.class);
	}

	public <PrmEnt> Fdr<PrmEnt> jdSelectSingleCustomEntityBindMap(String sql, Map<String, Object> mapParam, Class<PrmEnt> resultClazz) {

		Fdr<PrmEnt> fdr = new Fdr<>();
		fdr.setValue(null);

		try {
			Optional<PrmEnt> result = getJdbi().withHandle(handle -> {
				return handle.select(FiQueryTools.stoj(sql))
						.bindMap(mapParam)
						.mapTo(resultClazz)
						.findFirst();
			});

			if (result.isPresent()) {
				fdr.setValue(result.get());
			}
			fdr.setBoResult(true);
		} catch (Exception ex) {
			Loghelper.errorLog(getClass(), "Query Problem");
			Loghelper.errorException(getClass(), ex);
			fdr.setBoResult(false);
			fdr.setValue(null);
		}
		return fdr;
	}

	public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
		return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
	}

	public Fdr jdUpdateBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {
		Fdr fdrMain = new Fdr();
		try {
			Integer rowCountUpdate = getJdbi().withHandle(handle -> {
				return handle.createUpdate(FiQueryTools.stoj(updateQuery))
						.bindMap(fiMapParams)
						.execute(); // returns row count updated
			});
			//Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
			//fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
			fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
		} catch (Exception ex) {
			fdrMain.setBoResult(false, ex);
			Loghelper.get(getClass()).error(FiException.exToLog(ex));
		}
		return fdrMain;
	}

	public <EntMethodClazz> Fdr<List<EntMethodClazz>> jdSelectListBindMapMain(String sqlQuery, Map<String, Object> mapBind, Class<EntMethodClazz> clazz) {

		Fdr<List<EntMethodClazz>> fdr = new Fdr<>();
		fdr.setValue(new ArrayList<>());

		try {
			List<EntMethodClazz> result = getJdbi().withHandle(handle -> {
				return handle.createQuery(FiQueryTools.stoj(sqlQuery))
						.bindMap(mapBind)
						.mapToBean(clazz)
						.list();
			});
			fdr.setBoResultAndValue(true, result, 1);
		} catch (Exception ex) {
			Loghelper.get(getClass()).error("Query Problem");
			Loghelper.get(getClass()).error("Hata (Exception):\n" + FiException.exceptionToStrMain(ex));
			fdr.setBoResult(false, ex);
		}
		return fdr;
	}


}
