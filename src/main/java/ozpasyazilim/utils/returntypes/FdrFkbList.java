package ozpasyazilim.utils.returntypes;

import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.log.MetaLogType;

public class FdrFkbList extends Fdr<FkbList> {

    public FdrFkbList() {
    }

    public FdrFkbList(Boolean boResult) {
        super(boResult);
    }

    public FdrFkbList(FnResultGen fnKayitSonuc) {
        super(fnKayitSonuc);
    }

    public FdrFkbList(Integer rowCountUpdateWithUpBoResult) {
        super(rowCountUpdateWithUpBoResult);
    }

    public FdrFkbList(Boolean boResult, Integer rowCountUpdate) {
        super(boResult, rowCountUpdate);
    }

    public FdrFkbList(Integer rowCountUpdate, Exception ex) {
        super(rowCountUpdate, ex);
    }

    public FdrFkbList(Integer rowCountUpdate, Boolean boResult) {
        super(rowCountUpdate, boResult);
    }

    public FdrFkbList(Boolean boResult, Exception ex) {
        super(boResult, ex);
    }

    public FdrFkbList(FiResponse fiResponse) {
        super(fiResponse);
    }

    public FdrFkbList(Boolean boResult, String message) {
        super(boResult, message);
    }

    public FdrFkbList(Boolean boResult, String message, MetaLogType metaLogType) {
        super(boResult, message, metaLogType);
    }

    public FdrFkbList(Boolean boResult, String txMessage, Boolean boAddException) {
        super(boResult, txMessage, boAddException);
    }

    public FdrFkbList(String txMessage) {
        super(txMessage);
    }

}
