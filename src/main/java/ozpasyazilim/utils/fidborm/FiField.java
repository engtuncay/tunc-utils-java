package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.fidbanno.FiCollation;
import ozpasyazilim.utils.fidbanno.FiSqlDateTypes;
import ozpasyazilim.utils.fidbanno.FiIdGenerationType;

import javax.persistence.TemporalType;
import java.util.List;

public class FiField {

	// Code ve Db Field Name , farklı ise dbFieldName e alanı yazılır
	String name;
	String dbFieldName;
	String label;

	Integer size;
	Boolean boCandidateId1;
	Boolean boCandidateId2;

	Boolean boFiSelect;

	/**
	 * Seçili alanlar ile select sorgusu oluşturmak için işaretlenir
	 */
	Boolean boFiSelect1;

	Boolean isTransient;
	Integer precision;
	Integer scale;
	Integer length;
	Boolean unique;
	Boolean nullable;
	String colCustomType;
	String colDefinitionExtra;

	TemporalType temporalType;
	FiSqlDateTypes dateType;
	String classNameSimple;
	String sqlFieldDefinition;
	Boolean boIdField;
	String defaultValue;
	//String typeSimpleName;
	String colFieldType;
	Boolean boOnlyFirstInsert;
	Boolean boUtfSupport;
	Boolean boScopeIdField;
	FiCollation fiCollation;
	Boolean boSeperatedField;
	Boolean boDtoField;
	Boolean boDefaultUpdateField;
	String typeName;
	Boolean boCusFieldUserId;
	Boolean boCusFieldDtChange;
	Boolean boCusFieldDtCreate;
	Boolean boComboField;

	// tabloda firma alanı mı (ayırıcı alan)
	Boolean boFirmField;
	// Excelde sütun açıklamasına yazılacak metin
	String txComment;

	List<FiField> uniqueGroup1;
	List<FiField> uniqueGroup2;

	// doldurulması gereken alan , null ise, null check true olur, yoksa false olur
	Boolean nullCheck;

	Boolean boInsertMaxPlus;
	Boolean boGuidField;

	Boolean boWhere1;

	FiIdGenerationType idGenerationType;
	Boolean boExcludeFromAutoColList;

	// true olur FiMapParam a alan eklenirken % işareti ekler
	Boolean boFilterLike;

	// Insert Select sorguların where alanında kullanıcak alan (tarih genelde)
	Boolean boDateSeperatorField;

	Boolean boUnique1;

	String txUnique1Name;

	Boolean boFiWhere1;

	public FiField() {
	}

	public FiField(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public String getDbParamName(){
		if(FiString.isAllUpperCase(getDbFieldName())){
			return getDbFieldName();
		}
		return FiString.firstLetterLowerOnly(getDbFieldName());
	}

	public Boolean getBoIdField() {
		return boIdField;
	}

	public void setBoIdField(Boolean boIdField) {
		this.boIdField = boIdField;
	}

	public String getSqlFieldDefinition() {
		return sqlFieldDefinition;
	}

	public void setSqlFieldDefinition(String sqlFieldDefinition) {
		this.sqlFieldDefinition = sqlFieldDefinition;
	}

	public String getClassNameSimple() {
		return classNameSimple;
	}

	public void setClassNameSimple(String classNameSimple) {
		this.classNameSimple = classNameSimple;
	}

	public String getColDefinitionExtra() {
		return colDefinitionExtra;
	}

	public void setColDefinitionExtra(String colDefinitionExtra) {
		this.colDefinitionExtra = colDefinitionExtra;
	}

	public TemporalType getTemporalType() {
		return temporalType;
	}

	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}

	public String getColCustomType() {
		return colCustomType;
	}

	public void setColCustomType(String colCustomType) {
		this.colCustomType = colCustomType;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getBoCandidateId1() {return boCandidateId1;}

	public void setBoCandidateId1(Boolean idSelected) {
		boCandidateId1 = idSelected;}

	public Boolean getTransient() {
		return isTransient;
	}

	public void setTransient(Boolean aTransient) {
		isTransient = aTransient;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Boolean getBoFiSelect() {return boFiSelect;}

	public void setBoFiSelect(Boolean boFiSelect) {this.boFiSelect = boFiSelect;}

	public String getDefaultValue() {return defaultValue;}

	public void setDefaultValue(String defaultValue) {this.defaultValue = defaultValue;}

	public String getLabel() {return label;}

	public void setLabel(String label) {this.label = label;}

	public Boolean getNullCheck() {return nullCheck;}

	public void setNullCheck(Boolean nullCheck) {this.nullCheck = nullCheck;}

	public FiSqlDateTypes getDateType() {return dateType;}

	public void setDateType(FiSqlDateTypes dateType) {this.dateType = dateType;}

	public String getDbFieldName() {return dbFieldName;}

	public void setDbFieldName(String dbFieldName) {this.dbFieldName = dbFieldName;}

	public String getColFieldType() {return colFieldType;}

	public void setColFieldType(String colFieldType) {this.colFieldType = colFieldType;}

	public Boolean getBoOnlyFirstInsert() {return boOnlyFirstInsert;}

	public void setBoOnlyFirstInsert(Boolean boOnlyFirstInsert) {this.boOnlyFirstInsert = boOnlyFirstInsert;}

	public Boolean getBoUtfSupport() {return boUtfSupport;}

	public void setBoUtfSupport(Boolean boUtfSupport) {this.boUtfSupport = boUtfSupport;}

	public Boolean getBoScopeIdField() {return boScopeIdField;}

	public void setBoScopeIdField(Boolean boScopeIdField) {this.boScopeIdField = boScopeIdField;}

	public List<FiField> getUniqueGroup1() {return uniqueGroup1;}

	public void setUniqueGroup1(List<FiField> uniqueGroup1) {this.uniqueGroup1 = uniqueGroup1;}

	public List<FiField> getUniqueGroup2() {return uniqueGroup2;}

	public void setUniqueGroup2(List<FiField> uniqueGroup2) {this.uniqueGroup2 = uniqueGroup2;}

	public Boolean getBoFiSelect1() {return boFiSelect1;}

	public void setBoFiSelect1(Boolean boFiSelect1) {this.boFiSelect1 = boFiSelect1;}

	public FiCollation getFiCollation() {return fiCollation;}

	public void setFiCollation(FiCollation fiCollation) {this.fiCollation = fiCollation;}

//	public Boolean getBoSeperatedField() {return boSeperatedField;}

//	public void setBoSeperatedField(Boolean boSeperatedField) {this.boSeperatedField = boSeperatedField;}

	public Boolean getBoDtoField() {return boDtoField;}

	public void setBoDtoField(Boolean boDtoField) {this.boDtoField = boDtoField;}

	public Boolean getBoDefaultUpdateField() {return boDefaultUpdateField;}

	public void setBoDefaultUpdateField(Boolean boDefaultUpdateField) {this.boDefaultUpdateField = boDefaultUpdateField;}

	public String getTypeName() {return typeName;}

	public void setTypeName(String typeName) {this.typeName = typeName;}

	public Boolean getBoCusFieldUserId() {return boCusFieldUserId;}

	public void setBoCusFieldUserId(Boolean boCusFieldUserId) {this.boCusFieldUserId = boCusFieldUserId;}

	public Boolean getBoCusFieldDtChange() {return boCusFieldDtChange;}

	public void setBoCusFieldDtChange(Boolean boCusFieldDtChange) {this.boCusFieldDtChange = boCusFieldDtChange;}

	public Boolean getBoCusFieldDtCreate() {return boCusFieldDtCreate;}

	public void setBoCusFieldDtCreate(Boolean boCusFieldDtCreate) {this.boCusFieldDtCreate = boCusFieldDtCreate;}

	public Boolean getBoComboField() {
		return boComboField;
	}

	public void setBoComboField(Boolean boComboField) {
		this.boComboField = boComboField;
	}

	public Boolean getBoCandidateId2() {
		return boCandidateId2;
	}

	public void setBoCandidateId2(Boolean boCandidateId2) {
		this.boCandidateId2 = boCandidateId2;
	}

	public String getTxComment() {return txComment;}

	public void setTxComment(String txComment) {this.txComment = txComment;}

	public Boolean getBoInsertMaxPlus() {return boInsertMaxPlus;}

	public void setBoInsertMaxPlus(Boolean boInsertMaxPlus) {this.boInsertMaxPlus = boInsertMaxPlus;}

	public Boolean getBoGuidField() {return boGuidField;}

	public void setBoGuidField(Boolean boGuidField) {this.boGuidField = boGuidField;}

	public FiIdGenerationType getIdGenerationType() {
		if (idGenerationType == null) {
			return FiIdGenerationType.Undefined;
		}
		return idGenerationType;
	}

	public void setIdGenerationType(FiIdGenerationType idGenerationType) {
		this.idGenerationType = idGenerationType;
	}

	public String getDbFieldNameOrName() {
		if(!FiString.isEmpty(getDbFieldName())){
			return getDbFieldName();
		}else{
			return getName();
		}
	}

	public Boolean getBoWhere1() {
		return boWhere1;
	}

	public void setBoWhere1(Boolean boWhere1) {
		this.boWhere1 = boWhere1;
	}

	public Boolean getBoExcludeFromAutoColList() {return boExcludeFromAutoColList;}

	public void setBoExcludeFromAutoColList(Boolean boExcludeFromAutoColList) {
		this.boExcludeFromAutoColList = boExcludeFromAutoColList;
	}

	public Boolean getBoFirmField() {
		return boFirmField;
	}

	public void setBoFirmField(Boolean boFirmField) {
		this.boFirmField = boFirmField;
	}

	public Boolean getBoFilterLike() {return boFilterLike;}

	public void setBoFilterLike(Boolean boFilterLike) {this.boFilterLike = boFilterLike;}

	public Boolean getBoDateSeperatorField() {return boDateSeperatorField;}

	public void setBoDateSeperatorField(Boolean boDateSeperatorField) {this.boDateSeperatorField = boDateSeperatorField;
	}

	public Boolean getBoUnique1() {
		return boUnique1;
	}

	public void setBoUnique1(Boolean boUnique1) {
		this.boUnique1 = boUnique1;
	}

	public Boolean getBoSeperatedField() {
		return boSeperatedField;
	}

	public void setBoSeperatedField(Boolean boSeperatedField) {
		this.boSeperatedField = boSeperatedField;
	}

	public String getTxUnique1Name() {
		return txUnique1Name;
	}

	public void setTxUnique1Name(String txUnique1Name) {
		this.txUnique1Name = txUnique1Name;
	}

	public Boolean getBoFiWhere1() {
		return boFiWhere1;
	}

	public void setBoFiWhere1(Boolean boFiWhere1) {
		this.boFiWhere1 = boFiWhere1;
	}
}
