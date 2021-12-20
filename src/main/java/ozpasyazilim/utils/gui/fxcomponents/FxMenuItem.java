package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import ozpasyazilim.utils.security.SecurityRight;

public class FxMenuItem extends MenuItem implements IFxSecureNode {

	String moduleCode;

	public FxMenuItem() {
		super();
	}

	public FxMenuItem(String text) {
		super(text);
	}

	public FxMenuItem(String text, Node graphic) {
		super(text, graphic);
	}

	@Override
	public String getModuleCode() {
		return moduleCode;
	}

	@Override
	public void setNodeDisabledBySecurity(Boolean isDisabled) {
		setDisable(isDisabled);
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
}
