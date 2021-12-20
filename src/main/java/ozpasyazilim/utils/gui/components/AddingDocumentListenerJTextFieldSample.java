package ozpasyazilim.utils.gui.components;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class AddingDocumentListenerJTextFieldSample {
	public static void main(String args[]) {
		final JFrame frame = new JFrame("Default Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextField textField = new JTextField();
		frame.add(textField, BorderLayout.NORTH);

		DocumentListener documentListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}

			public void insertUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}

			public void removeUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}

			private void printIt(DocumentEvent documentEvent) {
				DocumentEvent.EventType type = documentEvent.getType();
				String typeString = null;
				if (type.equals(DocumentEvent.EventType.CHANGE)) {
					typeString = "Change";
				} else if (type.equals(DocumentEvent.EventType.INSERT)) {
					typeString = "Insert";
				} else if (type.equals(DocumentEvent.EventType.REMOVE)) {
					typeString = "Remove";
				}
				System.out.print("Type : " + typeString + "");
				Document source = documentEvent.getDocument();

				int length = source.getLength();
				try {
					System.out.println("Length: "
						+ length
						+ " sgt: "
						+ source.getText(0, length)
						+ " tfgt:"
						+ textField.getText());

				} catch (BadLocationException e) {

					e.printStackTrace();
				}

				/// + source.getText(0, length -
				/// 2)
			}

		};
		textField.getDocument().addDocumentListener(documentListener);

		frame.setSize(250, 150);
		frame.setVisible(true);
	}
}