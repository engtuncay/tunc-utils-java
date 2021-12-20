package ozpasyazilim.utils.gui.components;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomDocumentListenerDouble implements DocumentListener{

	private JTextField jtextfield;
	
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
	
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {

		
	}

	public JTextField getJtextfield() {
		return jtextfield;
	}

	public void setJtextfield(JTextField jtextfield) {
		this.jtextfield = jtextfield;
	}

}
