package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class ClearingFocusListener implements FocusListener {
	  final private String initialText;
	  final private JTextField field;

	  public ClearingFocusListener (final JTextField field) {
	    this.initialText = field.getText ();
	    this.field = field;
	  }

	  @Override
	  public void focusGained (FocusEvent e) {
	    if (initialText.equals (field.getText ())) {
	      field.setText ("");
	      field.setForeground (Color.DARK_GRAY);
	    }
	  }

	  @Override
	  public void focusLost (FocusEvent e) {
	    if ("".equals (field.getText ())) {
	      field.setText (initialText);
	      field.setForeground (Color.LIGHT_GRAY);
	    }
	  }
	}
