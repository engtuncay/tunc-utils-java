package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.datatypes.FiListString;
import ozpasyazilim.utils.db.TableScheme;
import ozpasyazilim.utils.entitysql.EntSqlColumn;
//import ozpasyazilim.utils.ficRfcCoding;
import ozpasyazilim.utils.fidbanno.*;
import ozpasyazilim.utils.gui.fxcomponents.FxEditorFactory;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metas.ocg.FimOcgSql;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.repoSql.RepoSqlColumn;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import javax.persistence.Table;
import javax.persistence.TemporalType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * FiSql Query Generation Class for MS SQL Server
 */
public class FiSqlGenMs {

  /**
   * Update Query Generation
   * <p>
   * FiSqlGenConfig Fields Used : iFiTableMeta, ficUpFields, ficWhereFields
   *
   * @param fiSqlGenConfig
   * @return
   */
  public static String upQuery(FiSqlGenConfig fiSqlGenConfig) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiSqlGenConfig.getiFiTableMeta();
    FicList ficUpFields = fiSqlGenConfig.getFicUpFields();

    //FimOcgSql.sfTableName();
    //FimOcgSql.sfTxWhere();

    String template = "UPDATE {{sfTableName}} SET {{sfTxSet}} \n"
        + " WHERE {{sfTxWhere}} ";

    StringBuilder sbTxSetBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();

    //int indexSetBlock = 1;
    int indexWhereBlock = 0;
    String txAnd = " AND ";

    for (FiCol fiCol : ficUpFields) {

//      if (FiBool.isTrue(fiCol.getBoKeyIdField())) {
//        if (indexWhereBlock != 1) sbTxWhereBlock.append(" AND ");
//        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName());
//        indexWhereBlock++;
//      } else {

//      if (FiBool.isTrue(boUpdateFieldsOnly)) {
//
//        if (FiBool.isTrue(fiCol.getBoUpdateFieldForQuery())) {
//          if (indexSetBlock != 1) sbTxSetBlock.append(", ");
//          sbTxSetBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName());
//          indexSetBlock++;
//        } else {
//          continue;
//        }
//      } else {

      //}

      //}


      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(txAnd);
      } else {
        sbTxSetBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(txAnd);
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, txAnd);
    FiString.rtrimSb(sbTxSetBlock, txAnd);

    FiKeybean fkbParams = new FiKeybean();
    fkbParams.addFieldBy(FimOcgSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcgSql.sfTxSet(), sbTxSetBlock.toString());
    fkbParams.addFieldBy(FimOcgSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock==0) sql = "no where fields";

    //UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    return sql;
  }

}
