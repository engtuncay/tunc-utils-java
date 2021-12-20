package ozpasyazilim.utils.gui.components;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

/*
 * This work is hereby released into the Public Domain.
 * To view a copy of the public domain dedication, visit
 * http://creativecommons.org/licenses/publicdomain/
 */
public class SearchCombobox {

	JComboBox comboBox;
	ComboBoxModel model;
	JTextComponent editor;

	// flag to indicate if setSelectedItem has been called
	// subsequent calls to remove/insertString should be ignored
	boolean selecting = false;
	boolean hidePopupOnFocusLoss;
	boolean hitBackspace = false;
	boolean hitBackspaceOnSelection;

	KeyListener editorKeyListener;
	FocusListener editorFocusListener;

	public SearchCombobox(final JComboBox comboBox) {

		this.comboBox = comboBox;
		model = comboBox.getModel();

		SearchableComboboxModel searchmodel = new SearchableComboboxModel();

		comboBox.setModel(searchmodel);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//selecting false ise (true degilse)
				if (!selecting) highlightCompletedText(0);
			}

		});


		comboBox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				//propertychange ya editor ya da model fırlatıyor
				if (e.getPropertyName().equals("editor")) configureEditor((ComboBoxEditor) e.getNewValue());
				if (e.getPropertyName().equals("model")) model = (ComboBoxModel) e.getNewValue();
			}
		});

		editorKeyListener = new KeyAdapter() {

			public void keyPressed(KeyEvent e) {

				if (comboBox.isDisplayable()) comboBox.setPopupVisible(true);

				JTextComponent jc = (JTextComponent) e.getSource();

				System.out.println("text:" + ((JTextComponent) e.getSource()).getText());
				System.out.println("keycode:" + e.getKeyCode());
				System.out.println("keychar:" + e.getKeyChar());
				System.out.println("caret:" + jc.getCaretPosition());


				//                hitBackspace=false;
				//                switch (e.getKeyCode()) {
				//                    // determine if the pressed key is backspace (needed by the remove method)
				//                    case KeyEvent.VK_BACK_SPACE : hitBackspace=true;
				//                    hitBackspaceOnSelection=editor.getSelectionStart()!=editor.getSelectionEnd();
				//                    break;
				//                    // ignore delete key
				//                    case KeyEvent.VK_DELETE : e.consume();
				//                    comboBox.getToolkit().beep();
				//                    break;
				//                }
			}
		};

		// Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out
		hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");

		// Highlight whole text when gaining focus
		editorFocusListener = new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				highlightCompletedText(0);
			}

			public void focusLost(FocusEvent e) {
				// Workaround for Bug 5100422 - Hide Popup on focus loss
				if (hidePopupOnFocusLoss) comboBox.setPopupVisible(false);
			}

		};

		configureEditor(comboBox.getEditor());
		// Handle initially selected object
		Object selected = comboBox.getSelectedItem();
		//if (selected!=null) setText(selected.toString());
		highlightCompletedText(0);

	}

	// combobox u editable yapar , kendi instance ın constructor na gönderir
	public static void enable(JComboBox comboBox) {
		// has to be editable
		comboBox.setEditable(true);
		// change the editor's document
		new SearchCombobox(comboBox);
	}

	void configureEditor(ComboBoxEditor newEditor) {

		if (editor != null) { //editor null degilse
			editor.removeKeyListener(editorKeyListener);
			editor.removeFocusListener(editorFocusListener);
		}

		if (newEditor != null) { // neweditor-parametre olarak gelen null degilse
			editor = (JTextComponent) newEditor.getEditorComponent();
			editor.addKeyListener(editorKeyListener);
			editor.addFocusListener(editorFocusListener);
			//editor (jtextcomponent) setdocument ediliyor

		}
	}


	private void highlightCompletedText(int start) {
		//editor.setCaretPosition(getLength());
		editor.moveCaretPosition(start);
	}

	private void setSelectedItem(Object item) {
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private Object lookupItem(String pattern) {

		Object selectedItem = model.getSelectedItem();

		// only search for a different item if the currently selected does not match
		if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
			return selectedItem;
		} else {
			// iterate over all items
			for (int i = 0, n = model.getSize(); i < n; i++) {
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {
					return currentItem;
				}
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	// checks if str1 starts with str2 - ignores case
	private boolean startsWithIgnoreCase(String str1, String str2) {
		return str1.toUpperCase().startsWith(str2.toUpperCase());
	}

	private static void createAndShowGUI() {
		// the combo box (add/modify items if you like to)
		final JComboBox comboBox = new JComboBox(new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" });
		enable(comboBox);

		// create and show a window containing the combo box
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().add(comboBox);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}


class SearchableComboboxModel<E> extends DefaultComboBoxModel<E> {

	List<Integer> indexes;

	@Override
	public int getSize() {

        return indexes == null ? 0 : indexes.size();
    }

	@Override
	public E getElementAt(int index) {
        return super.getElementAt(index);
    }

	public List<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Integer> indexes) {
		this.indexes = indexes;
	}



}