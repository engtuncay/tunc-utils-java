package ozpasyazilim.utils.gui.components;

import ozpasyazilim.utils.gui.fxcomponents.FxButton;

import javax.swing.JInternalFrame;

public class OzInternalFrame extends JInternalFrame {

	public OzInternalFrame() { super();	}

	public OzInternalFrame(String string, boolean b, boolean c, boolean d, boolean e) {
		super(string, b, c, d, e);
	}

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
