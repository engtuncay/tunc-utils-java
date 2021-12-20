package ozpasyazilim.utils.gui.components;

import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import static java.lang.System.out;


public class AutoComboBox2 extends JComboBox<Object> {


	public static void main(String[] args) {

		JFrame framedemo = new JFrame("Demo");
		framedemo.setLayout(new FlowLayout());

		AutoComboBox2 ac = new AutoComboBox2();
		String[] itemArray = { "Malith", "John", "Jack" };
		ac.setKeyWord(itemArray);

		JComboBox comboBox = new JComboBox(new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" });
		// swingx component
		AutoCompleteDecorator.decorate(comboBox);

		framedemo.add(comboBox);
		framedemo.add(ac);


		framedemo.setSize(500, 500);
		framedemo.setVisible(true);

	}

	String keyWord[] = { "itema1", "itema2", "itemb3" };
	Vector myVector = new Vector();

	public AutoComboBox2() {

		setModel(new DefaultComboBoxModel(myVector));
		setSelectedIndex(-1);
		setEditable(true);

		JTextField text = (JTextField) this.getEditor().getEditorComponent();
		text.setFocusable(true);
		text.setText("");
		text.addKeyListener(new ComboListener(this, myVector));


		text.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
                try {
                    String aa = e.getDocument().getText(0, e.getLength());
                    out.println("aar:" + aa);
                } catch (BadLocationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }


            }

			@Override
			public void insertUpdate(DocumentEvent e) {
                try {
                    String aa = e.getDocument().getText(1, e.getLength());
                    out.println("aai:" + aa);
                } catch (BadLocationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }


            }

			@Override
			public void changedUpdate(DocumentEvent e) {

				try {
					String aa = e.getDocument().getText(0, e.getLength());
					System.out.println("aac:" + aa);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		setMyVector();
	}

	/**
	 * set the item list of the AutoComboBox
	 * 
	 * @param keyWord
	 *            an String array
	 */
	public void setKeyWord(String[] keyWord) {
		this.keyWord = keyWord;
		setMyVectorInitial();
	}

	private void setMyVector() {
		int a;
		for (a = 0; a < keyWord.length; a++) {
			myVector.add(keyWord[a]);
		}
	}

	private void setMyVectorInitial() {
		myVector.clear();
		int a;
		for (a = 0; a < keyWord.length; a++) {

			myVector.add(keyWord[a]);
		}
	}

}