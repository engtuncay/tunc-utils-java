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
				return handle.createQuery(FiQuery.stoj(sqlQuery))
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

	public Fdr jdUpdateBindMapMain(FiQuery fiQuery) {
		return jdUpdateBindMapMain(fiQuery.getTxQuery(), fiQuery.getMapParams());
	}

	public Fdr jdUpdateBindMapMain(String updateQuery, Map<String, Object> fiMapParams) {
		Fdr fdrMain = new Fdr();
		try {
			Integer rowCountUpdate = getJdbi().withHandle(handle -> {
				return handle.createUpdate(FiQuery.stoj(updateQuery))
						.bindMap(fiMapParams)
						.execute(); // returns row count updated
			});
			//Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
			//fiDbResult.setLnSuccessWithUpBoResult(1, rowCountUpdate);
			fdrMain.setBoResultAndRowsAff(true, rowCountUpdate);
		} catch (Exception ex) {
			fdrMain.setBoResult(false, ex);
			Loghelper.get(getClass()).debug(FiException.exToLog(ex));
		}
		return fdrMain;
	}


	public void setAutoClass() {

	}
}
