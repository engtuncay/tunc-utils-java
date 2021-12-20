package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;

public interface IFxSimpleCont {

	void initCont();

	IFxSimpleView getModView();

	// Stage tanımı controller üzerinden kullanılacak
	Stage getFxStage();
	void setFxStage(Stage stage);

	String getModuleCode();
	String getModuleLabel();

	String getCloseReason();
	void setCloseReason(String closeReason);

}
