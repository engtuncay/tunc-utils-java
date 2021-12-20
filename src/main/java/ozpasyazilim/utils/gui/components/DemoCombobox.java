package ozpasyazilim.utils.gui.components;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import java.awt.Dimension;

public class DemoCombobox extends JFrame {
	private JComboBox comboBox;

	public static void main(String[] args) {

		DemoCombobox fr = new DemoCombobox();
		fr.pack();
		fr.setBounds(0, 0, 500, 500);
		fr.setVisible(true);

	}

	public DemoCombobox() {

		createContents();
	}

	private void createContents() {
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().add(getComboBox());
	}

	public JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
			comboBox.setPreferredSize(new Dimension(100, 20));
			comboBox.addItem("afg1");
			comboBox.addItem("afg2");
			comboBox.addItem("alg1");
			comboBox.addItem("alg2");
			comboBox.setEditable(true);

		}
		return comboBox;
	}
}
