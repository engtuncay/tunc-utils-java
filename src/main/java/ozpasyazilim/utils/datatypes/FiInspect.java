package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Bir liste içerisinde aynı id li kaç entity var , onu hesaplamak için yardımcı obje
 *
 * @param <T>
 */
public class FiInspect<T> {

    private String txId;

    private Integer lnId;

    private Integer lnCount;

    private List<T> listEntity;

    /**
     * count'u bir artırır
     */
    public void incCount1() {
        setLnCount(FiNumber.orZero(getLnCount())+1);
    }

    // GS

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Integer getLnId() {
        return lnId;
    }

    public void setLnId(Integer lnId) {
        this.lnId = lnId;
    }

    public Integer getLnCount() {
        return lnCount;
    }

    public void setLnCount(Integer lnCount) {
        this.lnCount = lnCount;
    }

    public List<T> getListEntityInit() {
        if (listEntity == null) {
            listEntity = new ArrayList<>();
        }
        return listEntity;
    }

    public void setListEntity(List<T> listEntity) {
        this.listEntity = listEntity;
    }


}
