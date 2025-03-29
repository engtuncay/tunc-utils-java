package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.fidbanno.FiCollation;
import ozpasyazilim.utils.fidbanno.FiSqlDateTypes;
import ozpasyazilim.utils.fidbanno.FiIdGenerationType;

import javax.persistence.TemporalType;
import java.util.List;

public class FiField implements IFiField{

	// Code ve Db Field Name , farklı ise dbFieldName e alanı yazılır
	String ofcTxFieldName;
	String ofcTxDbFieldName;
	String ofcTxHeader;

	Integer size;
	Boolean boCandidateId1;
	Boolean boCandidateId2;

	private String ofiTxIdType;

	Boolean boFiSelect;

	/**
	 * Seçili alanlar ile select sorgusu oluşturmak için işaretlenir
	 */
	Boolean boFiSelect1;

	Boolean oftBoTransient;

	Integer ofcLnLength;
	Integer ofcLnPrecision;
	Integer ofcLnScale;

	Boolean ofcBoUnique;
	Boolean ofcBoNullable;
	Boolean ofcBoUtfSupport;
	String ofcTxCollation;


	String colCustomType;
	String colDefinitionExtra;

	TemporalType temporalType;
	FiSqlDateTypes dateType;
	String classNameSimple;

	String sqlFieldDefinition;
	Boolean boKeyIdField;
	String ofcTxDefValue;
	//String typeSimpleName;
	String ofcTxFieldType;
	Boolean boOnlyFirstInsert;

	Boolean boScopeIdField;
	FiCollation fiCollation;
	Boolean boSeperatedField;
	Boolean boDtoField;
	Boolean boDefaultUpdateField;
	String ofcTxTypeName;
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
	Boolean ofcBoFilterLike;

	// Insert Select sorguların where alanında kullanıcak alan (tarih genelde)
	Boolean boDateSeperatorField;

	Boolean ofcBoUniqGro1;

	String txUnique1Name;

	Boolean boFiWhere1;

	public FiField() {
	}

	public FiField(String ofcTxFieldName, String ofcTxHeader) {
		this.ofcTxFieldName = ofcTxFieldName;
		this.ofcTxHeader = ofcTxHeader;
	}

	public String getDbParamName(){
		if(FiString.isAllUpperCase(getOfcTxDbField())){
			return getOfcTxDbField();
		}
		return FiString.firstLetterLowerOnly(getOfcTxDbField());
	}

	public Boolean getBoKeyIdField() {
		return boKeyIdField;
	}

	public void setBoKeyIdField(Boolean boKeyIdField) {
		this.boKeyIdField = boKeyIdField;
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

	public Boolean getOfcBoNullable() {
		return ofcBoNullable;
	}

	public void setOfcBoNullable(Boolean ofcBoNullable) {
		this.ofcBoNullable = ofcBoNullable;
	}

	public Boolean getOfcBoUnique() {
		return ofcBoUnique;
	}

	public void setOfcBoUnique(Boolean ofcBoUnique) {
		this.ofcBoUnique = ofcBoUnique;
	}

	public Integer getOfcLnLength() {
		return ofcLnLength;
	}

	public void setOfcLnLength(Integer ofcLnLength) {
		this.ofcLnLength = ofcLnLength;
	}

	public Integer getOfcLnScale() {
		return ofcLnScale;
	}

	public void setOfcLnScale(Integer ofcLnScale) {
		this.ofcLnScale = ofcLnScale;
	}

	public String getOfcTxFieldName() {return ofcTxFieldName;}

	public void setOfcTxFieldName(String ofcTxFieldName) {this.ofcTxFieldName = ofcTxFieldName;}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getBoCandidateId1() {return boCandidateId1;}

	public void setBoCandidateId1(Boolean idSelected) {
		boCandidateId1 = idSelected;}

	public Boolean getOfcBoTransient() {
		return oftBoTransient;
	}

	public void setOfcBoTransient(Boolean ofcBoTransient) {
		this.oftBoTransient = ofcBoTransient;
	}

	public Integer getOfcLnPrecision() {
		return ofcLnPrecision;
	}

	public void setOfcLnPrecision(Integer ofcLnPrecision) {
		this.ofcLnPrecision = ofcLnPrecision;
	}

	public Boolean getBoFiSelect() {return boFiSelect;}

	public void setBoFiSelect(Boolean boFiSelect) {this.boFiSelect = boFiSelect;}

	public String getOfcTxDefValue() {return ofcTxDefValue;}

	public void setOfcTxDefValue(String ofcTxDefValue) {this.ofcTxDefValue = ofcTxDefValue;}

	public String getOfcTxHeader() {return ofcTxHeader;}

	public void setOfcTxHeader(String ofcTxHeader) {this.ofcTxHeader = ofcTxHeader;}

	public Boolean getNullCheck() {return nullCheck;}

	public void setNullCheck(Boolean nullCheck) {this.nullCheck = nullCheck;}

	public FiSqlDateTypes getDateType() {return dateType;}

	public void setDateType(FiSqlDateTypes dateType) {this.dateType = dateType;}

	public String getOfcTxDbField() {return ofcTxDbFieldName;}

	public void setOfcTxDbField(String ofcTxDbField) {this.ofcTxDbFieldName = ofcTxDbField;}

	public String getOfcTxFieldType() {return ofcTxFieldType;}

	public void setOfcTxFieldType(String ofcTxFieldType) {this.ofcTxFieldType = ofcTxFieldType;}

	public Boolean getBoOnlyFirstInsert() {return boOnlyFirstInsert;}

	public void setBoOnlyFirstInsert(Boolean boOnlyFirstInsert) {this.boOnlyFirstInsert = boOnlyFirstInsert;}

	public Boolean getOfcBoUtfSupport() {return ofcBoUtfSupport;}

	public void setOfcBoUtfSupport(Boolean ofcBoUtfSupport) {this.ofcBoUtfSupport = ofcBoUtfSupport;}

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

	public String getOfcTxTypeName() {return ofcTxTypeName;}

	public void setOfcTxTypeName(String ofcTxTypeName) {this.ofcTxTypeName = ofcTxTypeName;}

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
			return FiIdGenerationType.undefined;
		}
		return idGenerationType;
	}

	public void setIdGenerationType(FiIdGenerationType idGenerationType) {
		this.idGenerationType = idGenerationType;
	}

	public String getDbFieldNameOrName() {
		if(!FiString.isEmpty(getOfcTxDbField())){
			return getOfcTxDbField();
		}else{
			return getOfcTxFieldName();
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

	public Boolean getOfcBoFilterLike() {return ofcBoFilterLike;}

	public void setOfcBoFilterLike(Boolean ofcBoFilterLike) {this.ofcBoFilterLike = ofcBoFilterLike;}

	public Boolean getBoDateSeperatorField() {return boDateSeperatorField;}

	public void setBoDateSeperatorField(Boolean boDateSeperatorField) {this.boDateSeperatorField = boDateSeperatorField;
	}

	public Boolean getOfcBoUniqGro1() {
		return ofcBoUniqGro1;
	}

	public void setOfcBoUniqGro1(Boolean ofcBoUniqGro1) {
		this.ofcBoUniqGro1 = ofcBoUniqGro1;
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

	@Override
	public String getOfcTxIdType() {
		return ofiTxIdType;
	}

	@Override
	public void setOfcTxIdType(String ofcTxIdType) {
		this.ofiTxIdType = ofcTxIdType;
	}

	@Override
	public String getOfcTxCollation() {
		return ofcTxCollation;
	}

	@Override
	public void setOfcTxCollation(String ofcTxCollation) {
		this.ofcTxCollation = ofcTxCollation;
	}
}
