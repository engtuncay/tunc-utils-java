package ozpasyazilim.utils.returntypes;

/**
 * FiReturnObject
 */
public class Fro extends Fdr<Object>{

	public Fro() {
	}

	public Fro(Boolean boResult) {
		super(boResult);
	}

	public Fro(FnResultGen fnKayitSonuc) {
		super(fnKayitSonuc);
	}

	public Fro(Integer rowCountUpdateWithUpBoResult) {
		super(rowCountUpdateWithUpBoResult);
	}

	public Fro(Boolean boResult, Integer rowCountUpdate) {
		super(boResult, rowCountUpdate);
	}

	public Fro(Integer rowCountUpdate, Exception ex) {
		super(rowCountUpdate, ex);
	}

	public Fro(Integer rowCountUpdate, Boolean boResult) {
		super(rowCountUpdate, boResult);
	}

	public Fro(Boolean boResult, Exception ex) {
		super(boResult, ex);
	}

	public Fro(FiResponse fiResponse) {
		super(fiResponse);
	}

	public Fro(Boolean boResult, String message) {
		super(boResult, message);
	}

	public Fro(Boolean boResult, String txMessage, Boolean boAddException) {
		super(boResult, txMessage, boAddException);
	}

	public Fro(String txMessage) {
		super(txMessage);
	}

	public static Fro creBoResult(Boolean boResult) {
		Fro fdr = new Fro();
		fdr.setBoResult(boResult);
		return fdr;
	}

}
