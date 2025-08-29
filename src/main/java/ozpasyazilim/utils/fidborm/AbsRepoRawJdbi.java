package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.*;

import static ozpasyazilim.utils.core.FiStFormat.*;

/**
 * Raw means - Raw usage , Not Generic
 */
public abstract class AbsRepoRawJdbi extends AbsRepoJdbiCore { //implements IRepoJdbi

	private Handle handleRepo;

	public AbsRepoRawJdbi() {
	}

	public AbsRepoRawJdbi(String connProfile) {
		this.connProfile = connProfile;
	}

	public AbsRepoRawJdbi(Jdbi jdbi) {
		setJdbi(jdbi);
	}

	public AbsRepoRawJdbi(Handle handleRepo) {
		setHandleRepo(handleRepo);
	}

	public Handle getHandleRepo() {
		return handleRepo;
	}

	public void setHandleRepo(Handle handleRepo) {
		this.handleRepo = handleRepo;
	}

	// class tanımı yapılmayacak
	public void setAutoClass() {

	}

	// Sorgu Metodları

	private String convertSqlAndMapToMultiParam(String sqlQuery, Map<String, Object> mapBind) {

		if (mapBind == null) return sqlQuery;

		Map<String, List> mapParamMulti = new HashMap<>();

		Map<String, Object> mapParamNew = new HashMap<>();

		mapBind.forEach((param, value) -> {

			if (value instanceof List) {

				List listData = (List) value;
				//Loghelperr.getInstance(getClass()).debug("Field List:"+param.toString());
				mapParamMulti.put(param, listData);

				Integer index = -1;
				for (Object listDatum : listData) {
					index++;
					String sablon = param + "_" + index.toString();
					mapParamNew.put(sablon, listData.get(index));
				}

			} else {
				//Loghelperr.getInstance(getClass()).debug("Field Prim:"+param.toString());
				//mapParamNew.put(param,value);
			}


		});

		for (Map.Entry<String, List> entry : mapParamMulti.entrySet()) {
			//System.out.println(entry.getKey() + "/" + entry.getValue());
			sqlQuery = Fiqt.convertSqlForMultiParamByTemplate2(sqlQuery, entry.getKey(), entry.getValue().size());
			mapBind.remove(entry.getKey());
		}

		mapBind.putAll(mapParamNew);
		return sqlQuery;
	}

	public Fdr<Optional<Integer>> jdSelectSingleOptIntegerByMap(String sqlQuery, FiKeybean map) {

		Jdbi jdbi = getJdbi();
		Fdr<Optional<Integer>> fdr = new Fdr<>();

		try {
			Optional<Integer> result = jdbi.withHandle(handle -> {
				return handle.select(Fiqt.stoj(sqlQuery))
						.bindMap(map)
						.mapTo(Integer.class)
						.findFirst();
			});
			fdr.setBoResultAndValue(true, result, 1);
		} catch (Exception ex) {
			Loghelper.errorLog(getClass(), "Query Problem");
			Loghelper.errorException(getClass(), ex);
			fdr.setBoResult(false, ex);
		}
		return fdr;

	}

	public Integer jdRunBatchUpdateQuery(List<String> queryList) {
		if (queryList == null) return null;

		Integer rowCountUpdate = null;

		try {
			rowCountUpdate = getJdbi().withHandle(handle -> {

				Integer rowAffectedLast = null;

				for (String query : queryList) {
					rowAffectedLast = handle.createUpdate(query).execute();
				}
				//Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
				return rowAffectedLast;
			});
			return rowCountUpdate;
		} catch (Exception ex) {
			Loghelper.errorException(getClass(), ex);
			return null;
		}

	}

	public Fdr jdRunBatchUpdateQueryFi(List<String> queryList) {

		if (queryList == null || queryList.size() == 0) {
			return new Fdr(false, "Boş sorgu");
		}

		Fdr fdr = new Fdr();

		try {
			getJdbi().useHandle(handle -> {

				for (String query : queryList) {
					if (FiString.isEmpty(query.trim())) continue;
					Integer rowAffected = handle.createUpdate(query).execute();
					//System.out.println("Rows Affe:"+ rowAffected.toString());//0 dönüyor , onun  için aşağıda bir eklendi.
					fdr.appendRowsAffected(1);
				}
				//Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
			});
			fdr.setBoResultWithCheckRowsAffected(true);
		} catch (Exception ex) {
			Loghelper.errorException(getClass(), ex);
			fdr.setBoResult(false, ex);
		}

		return fdr;
	}

	// XNOTE jdbi transaction için güzel bir örnek - jdbi
	public Fdr jdUpdateBatchWitTrans(List<String> queryList, FiKeybean mapParams) {

		if (queryList == null || queryList.size() == 0) {
			return new Fdr(false, "Boş sorgu");
		}

		Fdr fdr = new Fdr();

		try {
			getJdbi().useTransaction(handle -> {

				for (String query : queryList) {
					if (FiString.isEmpty(query.trim())) continue;
					Integer rowAffected = handle.createUpdate(Fiqt.stoj(query)).bindMap(mapParams).execute();
					fdr.appendRowsAffected(rowAffected);
				}
				//Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
			});
			fdr.setFdrBoResult(true);
		} catch (Exception ex) {
			Loghelper.errorException(getClass(), ex);
			fdr.setBoResult(false, ex);
		}

		return fdr;
	}

	public Fdr jdUpdateBindMap(String updateQuery, Map<String, Object> fiMapParams) {

		Jdbi jdbi = getJdbi();

		Fdr fdr = new Fdr();

		try {
			Integer rowCountUpdate = jdbi.withHandle(handle -> {
				return handle.createUpdate(Fiqt.stoj(updateQuery))
						.bindMap(fiMapParams)
						.execute(); // returns row count updated
			});
			// "Row Count Update:"+rowCountUpdate;
			fdr.setBoResultAndRowsAff(true, rowCountUpdate);
		} catch (Exception ex) {
			Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
			fdr.setBoResult(false, ex);
			fdr.setRowsAffected(-1);
		}
		return fdr;
	}

	public Fdr jdUpdateBindMapWoRowsAff(String updateQuery, Map<String, Object> fiMapParams) {

		Jdbi jdbi = getJdbi();

		Fdr fdr = new Fdr();

		try {
			jdbi.useHandle(handle -> {
				handle.createUpdate(Fiqt.stoj(updateQuery))
						.bindMap(fiMapParams)
						.execute(); // returns row count updated
			});
			//Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
			fdr.setFdrBoResult(true);
		} catch (Exception ex) {
			Loghelper.debugException(getClass(), ex);
			fdr.setBoResult(false, ex);
			fdr.setRowsAffected(-1);
		}
		return fdr;
	}

	/**
	 * @param updateQuery
	 * @param fiMapParams
	 * @return
	 * @_ ile başlayan değişkenleri named parametreye çevirir (:)
	 */
	public Fdr jdUpdateBindMapViaAtTire(String updateQuery, Map<String, Object> fiMapParams) {
		Jdbi jdbi = getJdbi();
		Fdr fdr = new Fdr();
		try {
			Integer rowCountUpdate = jdbi.withHandle(handle -> {
				return handle.createUpdate(fimSqlAtTire(updateQuery))
						.bindMap(fiMapParams)
						.execute(); // returns row count updated
			});
			//Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
			fdr.setBoResultAndRowsAff(true, rowCountUpdate);
		} catch (Exception ex) {
			Loghelper.debugException(getClass(), ex);
			fdr.setBoResult(false, ex);
		}
		return fdr;
	}

	public Fdr jdUpdate(String updateQuery) {
		Fdr fdr = new Fdr();
		try {
			Integer rowCountUpdate = getJdbi().withHandle(handle -> {
				return handle.createUpdate(updateQuery)
						.execute(); // returns row count updated
			});
			//Loghelperr.getInstance(getClass()).debug("Row Count Update:"+rowCountUpdate);
			fdr.setBoResultAndRowsAff(true, rowCountUpdate);
		} catch (Exception ex) {
			Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
			fdr.setBoResult(false, ex);
		}
		return fdr;
	}

	public Fdr<Integer> jdSelectSingleInt(String sql, FiKeybean fiKeyBean) {
		return jdSelectSingleIntBindMapOrMinus1(sql, fiKeyBean);
	}

	/**
	 * isPresent yoksa , null ve hatalı sorgularda değer -1 olarak döner
	 *
	 * @param sql
	 * @param mapParam
	 * @return
	 */
	public Fdr<Integer> jdSelectSingleIntBindMapOrMinus1(String sql, Map<String, Object> mapParam) {
		Jdbi jdbi = getJdbi();
		Fdr<Integer> fdrResult = new Fdr<>();
		//Loghelper.get(getClass()).debug("Sql:" + FiQuery.stoj(sql));
		try {
			Optional<Integer> result = jdbi.withHandle(handle -> {
				return handle.select(Fiqt.stoj(sql))
						.bindMap(mapParam)
						.mapTo(Integer.class)
						.findFirst();
			});

			if (result.isPresent()) {
				fdrResult.setValue(result.get());
			} else {
				fdrResult.setValue(-1);
			}
			fdrResult.setFdrBoResult(true);
			return fdrResult;
		} catch (Exception ex) {
			Loghelper.get(getClass()).error( "Query Problem");
			Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
			fdrResult.setValue(-1);
			fdrResult.setFdrBoResult(false);
			return fdrResult;
		}
	}

	public Fdr<Integer> jdSelectSingleIntBindMapOrMinusOne2(String sql, Map<String, Object> mapParam) {
		Fdr<Integer> fdrResult = new Fdr<>();
		//Loghelper.get(getClass()).debug("Sql:" + FiQuery.stoj(sql));
		try {
			List<Map<String,Integer>> result = getJdbi().withHandle(handle ->
					handle.select(Fiqt.stoj(sql))
					.bindMap(mapParam)
					.mapToMap(Integer.class)
					.list());

			if(result!=null){
				result.get(0).forEach((s, intResult) -> {
					fdrResult.setValue(intResult);
				});
			}else{
				fdrResult.setValue(-1);
			}
			fdrResult.setFdrBoResult(true);
			return fdrResult;
		} catch (Exception ex) {
			Loghelper.get(getClass()).error( "Query Problem");
			Loghelper.get(getClass()).error(FiException.exToErrorLog(ex));
			fdrResult.setValue(-1);
			fdrResult.setFdrBoResult(false);
			return fdrResult;
		}
	}
	public <PrmEnt> Fdr<PrmEnt> jdSelectSingleCustomEntityBindMap(String sql, Map<String
			, Object> mapParam, Class<PrmEnt> resultClazz) {

		Jdbi jdbi = getJdbi();

		Fdr<PrmEnt> fdr = new Fdr<>();
		fdr.setValue(null);

		try {
			Optional<PrmEnt> result = jdbi.withHandle(handle -> {
				return handle.select(Fiqt.stoj(sql))
						.bindMap(mapParam)
						.mapTo(resultClazz)
						.findFirst();
			});

			if (result.isPresent()) {
				fdr.setValue(result.get());
			}
			fdr.setFdrBoResult(true);
		} catch (Exception ex) {
			Loghelper.errorLog(getClass(), "Query Problem");
			Loghelper.errorException(getClass(), ex);
			fdr.setFdrBoResult(false);
			fdr.setValue(null);
		}

		return fdr;
	}


//	public FiDbResult jdUpdateQueryListBindBeanWithTrans(List<String> queryList, Object bean) {
//
//		if (queryList == null || queryList.size() == 0) {
//			return new FiDbResult(null, "Çalıştırılacak sorgu yok.");
//		}
//
//		FiDbResult fiDbResultAll = new FiDbResult();
//
//		try {
//			getJdbi().useTransaction(handle -> {
//
//				for (String query : queryList) {
//					if (FiString.isEmpty(query.trim())) continue;
//					FiDbResult fiDbResultQuery = jdoUpdateBindObjectWithHandle(handle, query, bean);
//					fiDbResultAll.combineAnd(fiDbResultQuery);
//					//fiDbResultAll.appendRowsAffected(rowAffected);
//				}
//				//Loghelperr.getInstance(getClass()).debug("Affected:"+ rowAffectedLast);
//			});
//			fiDbResultAll.setBoResult(true);
//		} catch (Exception ex) {
//			Loghelper.errorException(getClass(), ex);
//			fiDbResultAll.setBoResult(false, ex);
//		}
//
//		return fiDbResultAll;
//	}


}
