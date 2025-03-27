package ozpasyazilim.utils.returntypes;

import ozpasyazilim.utils.datatypes.FkbList;
import ozpasyazilim.utils.log.MetaLogType;

public class FdrFkb extends Fdr<FkbList> {

    public FdrFkb() {
    }

    public FdrFkb(Boolean boResult) {
        super(boResult);
    }

    public FdrFkb(FnResultGen fnKayitSonuc) {
        super(fnKayitSonuc);
    }

    public FdrFkb(Integer rowCountUpdateWithUpBoResult) {
        super(rowCountUpdateWithUpBoResult);
    }

    public FdrFkb(Boolean boResult, Integer rowCountUpdate) {
        super(boResult, rowCountUpdate);
    }

    public FdrFkb(Integer rowCountUpdate, Exception ex) {
        super(rowCountUpdate, ex);
    }

    public FdrFkb(Integer rowCountUpdate, Boolean boResult) {
        super(rowCountUpdate, boResult);
    }

    public FdrFkb(Boolean boResult, Exception ex) {
        super(boResult, ex);
    }

    public FdrFkb(FiResponse fiResponse) {
        super(fiResponse);
    }

    public FdrFkb(Boolean boResult, String message) {
        super(boResult, message);
    }

    public FdrFkb(Boolean boResult, String message, MetaLogType metaLogType) {
        super(boResult, message, metaLogType);
    }

    public FdrFkb(Boolean boResult, String txMessage, Boolean boAddException) {
        super(boResult, txMessage, boAddException);
    }

    public FdrFkb(String txMessage) {
        super(txMessage);
    }

}
