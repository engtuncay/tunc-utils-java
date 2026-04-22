package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class FxTextArea extends TextArea {

	public FxTextArea() {
	}

	public FxTextArea(String text) {
		super(text);
	}


	public void appendTextNlAsyn(String text) {
		Platform.runLater(() -> {
			appendText("\n"+text);
		});
	}

	public void appendHrLine() {
		appendText("\n"+"------------------------"+"\n");
	}

	public void appendNewLine() {
		appendText("\n");
	}
}
