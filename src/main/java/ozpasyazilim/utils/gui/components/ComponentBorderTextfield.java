package ozpasyazilim.utils.gui.components;

import javax.swing.JButton;
import javax.swing.JTextField;

public class ComponentBorderTextfield extends JTextField {

	JButton compbutton;

	public ComponentBorderTextfield() {
		compbutton = new JButton("?");
		ComponentBorder cb = new ComponentBorder(compbutton);
		cb.install(this);
	}

	public JButton getCompbutton() {
		return compbutton;
	}

	public void setCompbutton(JButton compbutton) {
		this.compbutton = compbutton;
	}

}
