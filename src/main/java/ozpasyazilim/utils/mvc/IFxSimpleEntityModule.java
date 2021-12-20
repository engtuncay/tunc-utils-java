package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;

public interface IFxSimpleEntityModule<EntClazz> {

	IFxSimpleView getModView();

	Stage getFxStage();
	void setFxStage(Stage stage);

	String getModuleCode();
	String getModuleLabel();

	String getCloseReason();
	void setCloseReason(String closeReason);

	EntClazz getSelectedEntity();
	void setSelectedEntity(EntClazz selectedEntity);

}
