package ozpasyazilim.utils.gui.fxcomponents;

import java.util.List;

public class FxFormMiga extends FxFormMigaGen<Object> {

	public FxFormMiga() {
		super();
//		Loghelper.get(getClass()).debug("fxformig initialized");
	}

	public FxFormMiga(Class entityClazz) {
		super(entityClazz);
	}

	public FxFormMiga(List listFormElements) {
		super(listFormElements);
	}

	public FxFormMiga(List colsForm, FormType formType) {
		super(colsForm, formType);
	}


}
