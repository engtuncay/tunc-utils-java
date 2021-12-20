package ozpasyazilim.utils.gui.components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

/**
 * Key adapter which only allows numbers in the field.
 */
public class KeyAdapterNumbersOnly extends KeyAdapter {

	/**
	 * Regular expression which defines the allowed characters.
	 */
	private String allowedRegex = "[^0-9]";

	/**
	 * Key released on field.
	 */
	public void keyReleased(KeyEvent e) {
		String curText = ((JTextComponent) e.getSource()).getText();
		curText = curText.replaceAll(allowedRegex, "");

		((JTextComponent) e.getSource()).setText(curText);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
	}
}