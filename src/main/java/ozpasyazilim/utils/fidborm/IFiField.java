package ozpasyazilim.utils.fidborm;

/**
 * FiCol ile FiField arasındaki ortak alanlar
 */
public interface IFiField {

    public String getOfiTxIdType();

    public void setOfiTxIdType(String ofiTxIdType);

    public Boolean getOfcBoUniqGro1();

    public void setOfcBoUniqGro1(Boolean ofcBoUniqGro1);

    public Boolean getOfcBoNullable();

    public void setOfcBoNullable(Boolean ofcBoNullable);

    public Boolean getOfcBoUnique();

    public void setOfcBoUnique(Boolean ofcBoUnique);

    public Boolean getOfcBoUtfSupport();

    public void setOfcBoUtfSupport(Boolean ofcBoUtfSupport);

    public String getOfcTxDefValue();

    public void setOfcTxDefValue(String ofcTxDefValue);

    public String getOfcTxCollation();

    public void setOfcTxCollation(String ofcTxCollation);

    public String getOfcTxTypeName();

    public void setOfcTxTypeName(String ofcTxTypeName);

    public Integer getOfcLnLength();

    public void setOfcLnLength(Integer ofcLnLength);

    public Integer getOfcLnPrecision();

    public void setOfcLnPrecision(Integer ofcLnPrecision);

    public Integer getOfcLnScale();

    public void setOfcLnScale(Integer ofcLnScale);


    public Boolean getOfcBoFilterLike();

    public void setOfcBoFilterLike(Boolean ofcBoFilterLike);

    public Boolean getOftBoTransient();

    public void setOftBoTransient(Boolean oftBoTransient);

    public String getOfcTxFieldType();

    public void setOfcTxFieldType(String ofcTxFieldType);

    public String getOfcTxFieldName();

    public void setOfcTxFieldName(String ofcTxFieldName);

    public String getOfcTxHeader();

    public void setOfcTxHeader(String ofcTxHeader);

    public String getOfcTxDbFieldName();

    public void setOfcTxDbFieldName(String ofcTxDbFieldName);


}
