package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog2;
import ozpasyazilim.utils.javafx.FiNodeFx;
import ozpasyazilim.utils.table.OzColType;

import java.util.function.Consumer;

public class FxTextFieldBtnCsv<EntClazz> extends FxTextFieldBtn<EntClazz> implements IfxNode {

	public FxTextFieldBtnCsv() {
		super();
		getFxTextField().setEditable(false);

		getBtnSearch().setOnAction(event -> actBtnSearch());


	}

	private void actBtnSearch() {




	}


}
