package ozpasyazilim.utils.returntypes;

/**
 * Fdr Generic Usage
 */
public class Fdrg extends Fdr<Object>{

	public Fdrg() {
	}

	public Fdrg(Boolean boResult) {
		super(boResult);
	}

	public Fdrg(FnResultGen fnKayitSonuc) {
		super(fnKayitSonuc);
	}

	public Fdrg(Integer rowCountUpdateWithUpBoResult) {
		super(rowCountUpdateWithUpBoResult);
	}

	public Fdrg(Boolean boResult, Integer rowCountUpdate) {
		super(boResult, rowCountUpdate);
	}

	public Fdrg(Integer rowCountUpdate, Exception ex) {
		super(rowCountUpdate, ex);
	}

	public Fdrg(Integer rowCountUpdate, Boolean boResult) {
		super(rowCountUpdate, boResult);
	}

	public Fdrg(Boolean boResult, Exception ex) {
		super(boResult, ex);
	}

	public Fdrg(FiResponse fiResponse) {
		super(fiResponse);
	}

	public Fdrg(Boolean boResult, String message) {
		super(boResult, message);
	}

	public Fdrg(Boolean boResult, String txMessage, Boolean boAddException) {
		super(boResult, txMessage, boAddException);
	}

	public Fdrg(String txMessage) {
		super(txMessage);
	}

}
