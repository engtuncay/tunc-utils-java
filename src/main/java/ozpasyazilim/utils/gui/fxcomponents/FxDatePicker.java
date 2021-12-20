package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiDate;

import java.util.Date;

public class FxDatePicker extends FxDatePickerCore {

	public FxDatePicker() {
		super();
		setPrefWidth(90d);
	}

	public FxDatePicker(Date dtValue) {
		super(dtValue);
		setPrefWidth(90d);
	}

}
