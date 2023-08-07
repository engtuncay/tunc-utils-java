package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiType;
import ozpasyazilim.utils.returntypes.FiResponse2;

public class FxDialogHelper {

	public void showFiResponseResultPop(FiResponse2 fiResponse){
		if(FiType.isTrue(fiResponse.getBoResult())){
			new FxDialogShow().showPopInfo("İşlem Başarılı");
			return;
		}

		if(fiResponse.getBoResult()==null){
			FxDialogShow.showModalWarningAlert("Güncelleme olmadı.");
			return;
		}

		FxDialogShow.showModalWarningAlert("Hata Oluştu."+"\n"+fiResponse.getTxErrorMsgShort());
	}
}
