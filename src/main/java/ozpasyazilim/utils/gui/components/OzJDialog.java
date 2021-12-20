package ozpasyazilim.utils.gui.components;

import ozpasyazilim.utils.gui.fxcomponents.FxButton;

import javax.swing.*;

public class OzJDialog extends JDialog {

	FxButton ofxButton;
	Integer index;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public FxButton getOfxButton() {
		return ofxButton;
	}

	public void setOfxButton(FxButton ofxButton) {
		this.ofxButton = ofxButton;
	}




}
