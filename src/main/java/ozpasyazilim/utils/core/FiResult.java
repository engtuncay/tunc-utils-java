package ozpasyazilim.utils.core;

import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.returntypes.Fdr;

public class FiResult {


	public void consoleResult(Boolean result){

		if(result){
			System.out.println("İşlem Başarıyla Gerçekleşti.");
		}else{
			System.out.println("Hata Oluştu.");
		}

	}


	public void popFiDbResult(Fdr fdr) {

		if(fdr.getBoResult()){
			FxDialogShow.build().showPopWarn("*** İşlem Başarıyla Gerçekleşti ***");
		}else{
			FxDialogShow.build().showPopWarn("!!! Hata Oluştu.!!!");

			if(fdr.getException()!=null){
				FxDialogShow.build().showModalWarningAlert("Hata Detayı:\n"+ fdr.getException().getMessage());
			}

		}


	}
}
