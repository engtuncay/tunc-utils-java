package ozpasyazilim.utils.gui.fxcomponents;

public class FxTextFieldBtnCsv<EntClazz> extends FxTextFieldBtn<EntClazz> implements IfxNode {

	public FxTextFieldBtnCsv() {
		super();
		getFxTextField().setEditable(false);

		getBtnSearch().setOnAction(event -> actBtnSearch());


	}

	private void actBtnSearch() {




	}


}
