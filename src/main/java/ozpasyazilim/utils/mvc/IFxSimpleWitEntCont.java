package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;

public interface IFxSimpleWitEntCont<E> extends IFxSimpleCont {

	E getEntitySelected();
	void setEntityDefault(E entitySelected);

}

