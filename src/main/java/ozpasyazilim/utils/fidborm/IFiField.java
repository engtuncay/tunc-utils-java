package ozpasyazilim.utils.fidborm;

/**
 * FiCol ile FiField arasındaki ortak alanlar
 */
public interface IFiField {

    public String getFcTxIdType();

    public void setFcTxIdType(String fcTxIdType);

    public Boolean getFcBoUniqGro1();

    public void setFcBoUniqGro1(Boolean fcBoUniqGro1);

    public Boolean getFcBoNullable();

    public void setFcBoNullable(Boolean fcBoNullable);

    public Boolean getFcBoUnique();

    public void setFcBoUnique(Boolean fcBoUnique);

    public Boolean getFcBoUtfSupport();

    public void setFcBoUtfSupport(Boolean fcBoUtfSupport);

    public String getFcTxDefValue();

    public void setFcTxDefValue(String fcTxDefValue);

    public String getFcTxCollation();

    public void setFcTxCollation(String fcTxCollation);

    public String getFcTxTypeName();

    public void setFcTxTypeName(String fcTxTypeName);

    public String getFcTxFieldType();

    public void setFcTxFieldType(String fcTxFieldType);

    public Integer getFcLnLength();

    public void setFcLnLength(Integer fcLnLength);

    public Integer getFcLnPrecision();

    public void setFcLnPrecision(Integer fcLnPrecision);

    public Integer getFcLnScale();

    public void setFcLnScale(Integer fcLnScale);


    public Boolean getFcBoFilterLike();

    public void setFcBoFilterLike(Boolean fcBoFilterLike);

    public Boolean getFcBoTransient();

    public void setFcBoTransient(Boolean fcBoTransient);


    public String getFcTxFieldName();

    public void setFcTxFieldName(String fcTxFieldName);

    public String getFcTxHeader();

    public void setFcTxHeader(String fcTxHeader);

    public String getFcTxDbField();

    public void setFcTxDbField(String fcTxDbField);


}
