package ozpasyazilim.utils.gui.fxcomponents;

import java.util.List;

public class FxFormMig extends FxFormMigGen<Object> {

	public FxFormMig() {
		super();
//		Loghelper.get(getClass()).debug("fxformig initialized");
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
