package ozpasyazilim.utils.gui.fxcomponents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import ozpasyazilim.utils.core.FiThread;

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

	public FxMenuItem(String text, EventHandler<ActionEvent> eventHandler, FxMenuButton mbButton) {
		super(text);
		setOnAction(eventHandler);
		mbButton.addItem(this);
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

	public void setOnActionWithThread(Runnable runnable, FxMenuButton mbEslesme) {
		setOnAction(event -> FiThread.startThreadMb(runnable, mbEslesme,null));
	}
}
