package ozpasyazilim.utils.gui.fxcomponents;

import java.util.List;

public class FxFormMig extends FxFormMig3<Object>{

	public FxFormMig() {
	}

	public FxFormMig(Class entityClazz) {
		super(entityClazz);
	}

	public FxFormMig(List listFormElements) {
		super(listFormElements);
	}

	public FxFormMig(List colsForm, FormType formType) {
		super(colsForm, formType);
	}

}
