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

		if(fdr.getFdrBoResult()){
			FxDialogShow.showPopWarn("*** İşlem Başarıyla Gerçekleşti ***");
		}else{
			FxDialogShow.showPopWarn("!!! Hata Oluştu.!!!");

			if(fdr.getException()!=null){
				FxDialogShow.showModalWarningAlert("Hata Detayı:\n"+ fdr.getException().getMessage());
			}

		}


	}
}
